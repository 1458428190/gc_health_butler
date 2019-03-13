package com.gdufe.health_butler.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.AiSkinVO;
import com.gdufe.health_butler.common.exception.ParamErrorException;
import com.gdufe.health_butler.common.exception.SystemErrorException;
import com.gdufe.health_butler.common.util.HttpUtils;
import com.gdufe.health_butler.common.util.MinioUtils;
import com.gdufe.health_butler.entity.AiSkin;
import com.gdufe.health_butler.dao.AiSkinMapper;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.AiSkinService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdufe.health_butler.service.UserService;
import io.minio.MinioClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ai测肤记录表 服务实现类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-10
 */
@Service
public class AiSkinServiceImpl extends ServiceImpl<AiSkinMapper, AiSkin> implements AiSkinService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DETECT_URL = "https://api-cn.faceplusplus.com/facepp/v3/detect";

    private static final String ANALYZE_URL = "https://api-cn.faceplusplus.com/facepp/v3/face/analyze";

    private static final String ADD_FACE_URL = "https://api-cn.faceplusplus.com/facepp/v3/faceset/addface";

    @Value("${minio.imgBucket}")
    private String imgBucket;

    @Value("${face.apiKey}")
    private String apiKey;

    @Value("${face.apiSecret}")
    private String apiSecret;

    @Value("${face.facesetToken}")
    private String faceSetToken;

    @Autowired
    private UserService userService;

    @Autowired
    private MinioClient minioClient;

    @Override
    public AiSkinVO measure(String token, MultipartFile file) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        String originalFileName = file.getOriginalFilename();
        logger.info("[op:measure, uid:{}, token:{}, originalFileName: {}]", uid, token, originalFileName);
        if(file.isEmpty()) {
            throw new ParamErrorException("文件有误");
        }
        try {
            String newImgUrl = MinioUtils.FileUploaderByStream(minioClient, imgBucket, file.getInputStream(),
                    System.currentTimeMillis() + originalFileName);
            Map<String, Object> params = new HashMap<>();
            params.put("api_key", apiKey);
            params.put("api_secret", apiSecret);
            params.put("image_url", newImgUrl);
            params.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
            params.put("beauty_score_min", 0);
            params.put("beauty_score_max", 100);
            Map<String, Object> detect = (Map<String, Object>) JSON.parse(HttpUtils.post(DETECT_URL, params));

            if(detect.containsKey("error_message")) {
                AiSkinVO aiSkinVO = new AiSkinVO();
                aiSkinVO.setMessage("图片格式错误或超出2M或图片破损");
                return aiSkinVO;
            }
            List<Map<String, Object>> faceList = (List<Map<String, Object>>) detect.get("faces");
            if(faceList.size() <= 0) {
                AiSkinVO aiSkinVO = new AiSkinVO();
                aiSkinVO.setMessage("未能识别到人脸");
                return aiSkinVO;
            }
            Map<String, Object> face = faceList.get(0);
            String faceToken = (String) face.get("face_token");

            AiSkinVO aiSkinVO = new AiSkinVO(face);
            aiSkinVO.setImgUrl(newImgUrl);

            AiSkin aiSkin = new AiSkin();
            aiSkin.setUid(uid);
            aiSkin.setBeautyF(aiSkinVO.getBeautyF());
            aiSkin.setBeautyM(aiSkinVO.getBeautyM());
            aiSkin.setCreateTime(System.currentTimeMillis());
            aiSkin.setFaceToken(faceToken);
            aiSkin.setGender(StringUtils.equals(aiSkinVO.getGender(),"男性")?1:2);
            aiSkin.setImgUrl(newImgUrl);
            aiSkin.setModifiedTime(System.currentTimeMillis());
            aiSkin.setFaceQuality(aiSkinVO.getFaceQuality());
            save(aiSkin);

            // 将face_token 保存到face_set中
            Map<String, Object> faceSetParams = new HashMap<>();
            faceSetParams.put("api_key", apiKey);
            faceSetParams.put("api_secret", apiSecret);
            faceSetParams.put("faceset_token", faceSetToken);
            faceSetParams.put("face_tokens", faceToken);
            Map<String, Object> addFaceMap = (Map<String, Object>) JSON.parse(HttpUtils.post(ADD_FACE_URL, faceSetParams));
            if(addFaceMap.containsKey("error_message")) {
                logger.error("[op_addFace_rslt: error, error_message:{}]", addFaceMap.get("error_message"));
            }
            return aiSkinVO;
        } catch (Exception e) {
            logger.error("[op_rslt: error]", e);
            throw new SystemErrorException("内部系统出错", e);
        }
    }

    @Override
    public List<AiSkin> history(String token) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:history, uid:{}, token:{}]", uid, token);
        QueryWrapper<AiSkin> aiSkinQueryWrapper = new QueryWrapper<>();
        aiSkinQueryWrapper.lambda().eq(AiSkin::getUid, uid).orderByDesc(AiSkin::getCreateTime);
        return list(aiSkinQueryWrapper);
    }

    @Override
    public AiSkinVO inquiry(String token, long id) {
        User user = userService.getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        logger.info("[op:inquiry, uid:{}, token:{}, id:{}]", uid, token, id);
        try {
            QueryWrapper<AiSkin> aiSkinQueryWrapper = new QueryWrapper<>();
            aiSkinQueryWrapper.lambda().eq(AiSkin::getUid, uid).eq(AiSkin::getId, id);
            AiSkin aiSkin = getOne(aiSkinQueryWrapper);
            Map<String, Object> params = new HashMap<>();
            params.put("api_key", apiKey);
            params.put("api_secret", apiSecret);
            params.put("face_tokens", aiSkin.getFaceToken());
            params.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
            params.put("beauty_score_min", 0);
            params.put("beauty_score_max", 100);

            Map<String, Object> detect = (Map<String, Object>) JSON.parse(HttpUtils.post(ANALYZE_URL, params));
            List<Map<String, Object>> faceList = (List<Map<String, Object>>) detect.get("faces");
            if(faceList.size() <= 0) {
                AiSkinVO aiSkinVO = new AiSkinVO();
                aiSkinVO.setMessage("未能识别到人脸");
                return aiSkinVO;
            }
            Map<String, Object> face = faceList.get(0);
            AiSkinVO aiSkinVO = new AiSkinVO(face);
            aiSkinVO.setImgUrl(aiSkin.getImgUrl());
            return aiSkinVO;
        }catch (Exception e) {
            logger.error("[op_rslt: error]", e);
            throw new SystemErrorException("内部系统出错", e);
        }
    }
}
