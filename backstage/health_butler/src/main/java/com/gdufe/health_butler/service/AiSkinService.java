package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.AiSkinVO;
import com.gdufe.health_butler.entity.AiSkin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * ai测肤记录表 服务类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-10
 */
public interface AiSkinService extends IService<AiSkin> {

    /**
     * 测肤
     * @param token
     * @param file
     * @return
     */
    AiSkinVO measure(String token, MultipartFile file);

    /**
     * 查询历史
     * @param token
     * @return
     */
    List<AiSkin> history(String token);

    /**
     * 查询指定记录
     * @param token
     * @param id
     * @return
     */
    AiSkinVO inquiry(String token, long id);
}
