package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.CommunityVO;
import com.gdufe.health_butler.entity.Community;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 社区分享表 服务类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
public interface CommunityService extends IService<Community> {

    /**
     * 查询社区分享
     * @param token
     * @return
     */
    List<CommunityVO> listByToken(String token);

    /**
     * 分享健康动态
     * @param token
     * @param content
     * @param onlyMe
     * @return
     */
    long share(String token, String content, boolean onlyMe);

    /**
     * 删除动态
     * @param token
     * @param cid
     * @return
     */
    String delete(String token, long cid);

    /**
     * 获取自己的动态
     * @param token
     * @return
     */
    List<CommunityVO> getMe(String token);

    /**
     * 接收上传的文件1
     * @param token
     * @param cid
     * @param file
     * @param imgNo
     *          第几张
     * @return
     */
    String upload(String token, long cid, MultipartFile file, int imgNo);

    /**
     * 更新imgUrlList
     */
    void updateImgUrlList(long cid, String newImgUrl, int imgNo);

    /**
     * 获取动态数
     * @return
     */
    int listSize(String token);

    /**
     * 分页获取所有动态
     * @param token
     * @param pageNo
     * @param size
     * @return
     */
    List<CommunityVO> pageList(String token, int pageNo, int size);

    /**
     * 获取指定动态
     * @param token
     * @param cid
     * @return
     */
    CommunityVO getByCid(String token, long cid);
}
