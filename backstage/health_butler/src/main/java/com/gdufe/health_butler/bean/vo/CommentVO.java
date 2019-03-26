package com.gdufe.health_butler.bean.vo;

import lombok.Data;

/**
 * @Author: laichengfeng
 * @Description: 评论
 * @Date: 2019/3/25 15:02
 */
@Data
public class CommentVO {

    /**
     * 记录id
     */
    private long rid;

    /**
     * 操作者id
     */
    private long fromId;

    /**
     * 操作的用户名
     */
    private String fromName;

    /**
     * 回复的用户id
     */
    private long toId;

    /**
     * 回复的用户名
     */
    private String toName;

    /**
     * 评论或回复内容
     */
    private String content;
}
