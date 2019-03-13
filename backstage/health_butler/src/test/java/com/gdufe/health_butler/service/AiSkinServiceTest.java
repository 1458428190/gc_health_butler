package com.gdufe.health_butler.service;

import com.alibaba.fastjson.JSON;
import com.gdufe.health_butler.HealthButlerApplicationTests;
import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.AiSkinVO;
import com.gdufe.health_butler.common.util.HttpUtils;
import org.apache.http.HttpException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/3/12 12:05
 */
public class AiSkinServiceTest extends HealthButlerApplicationTests {


    private static final String DETECT_URL = "https://api-cn.faceplusplus.com/facepp/v3/detect";

    private static final String ANALYZE_URL = "https://api-cn.faceplusplus.com/facepp/v3/face/analyze";

    private static final String ADD_FACE_URL = "https://api-cn.faceplusplus.com/facepp/v3/faceset/addface";

    @Autowired
    private AiSkinService aiSkinService;

    @Value("${face.apiKey}")
    private String apiKey;

    @Value("${face.apiSecret}")
    private String apiSecret;

    @Value("${face.facesetToken}")
    private String faceSetToken;


    @Test
    public void measure() throws HttpException {
        Map<String, Object> params = new HashMap<>();
        params.put("api_key", apiKey);
        params.put("api_secret", apiSecret);
        params.put("image_url", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=154603817,3028123296&fm=27&gp=0.jpg");
        params.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        params.put("beauty_score_min", 0);
        params.put("beauty_score_max", 100);
        Map<String, Object> detect = (Map<String, Object>) JSON.parse(HttpUtils.post(DETECT_URL, params));

        List<Map<String, Object>> faceList = (List<Map<String, Object>>) detect.get("faces");
        if(faceList.size() <= 0) {
            AiSkinVO aiSkinVO = new AiSkinVO();
            aiSkinVO.setMessage("未能识别到人脸");
        }
        Map<String, Object> face = faceList.get(0);
        String faceToken = (String) face.get("face_token");

        AiSkinVO aiSkinVO = new AiSkinVO(face);
        System.out.println(aiSkinVO);
    }
}
