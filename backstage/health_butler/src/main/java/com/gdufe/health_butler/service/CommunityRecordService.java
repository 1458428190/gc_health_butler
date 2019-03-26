package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.CommentVO;
import com.gdufe.health_butler.entity.CommunityRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gdufe.health_butler.entity.User;

import java.util.List;

/**
 * <p>
 * 社区点赞打赏记录表 服务类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-04
 */
public interface CommunityRecordService extends IService<CommunityRecord> {

    /**
     * 获取点赞的用户
     * @param cid
     *          动态id
     * @return
     */
    List<User> getPraise(long cid);

    /**
     * 判断一个用户是否对某个动态点了赞
     * @param uid
     * @param cid
     * @return
     */
    boolean isPraise(long uid, long cid);

    /**
     * 点赞或者取消
     * @param token
     * @param cid
     * @param type
     * @return
     */
    String praise(String token, long cid, int type);

    /**
     * 评论
     * @param token
     * @param cid
     * @param content
     * @return
     */
    void comment(String token, long cid, String content);

    /**
     * 获取指定文章的评论
     * @param cid
     * @return
     */
    List<CommentVO> getComment(long cid);

    /**
     * 回复指定评论
     * @param token
     * @param cid
     * @param content
     * @param toUid
     */
    void replay(String token, long cid, String content, long toUid);

    /**
     * 删除回复
     * @param token
     * @param rid
     */
    void deleteComment(String token, long rid);
}
