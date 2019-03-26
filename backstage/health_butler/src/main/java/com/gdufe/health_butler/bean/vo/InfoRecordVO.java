package com.gdufe.health_butler.bean.vo;

import com.gdufe.health_butler.common.enums.InfoRecordType;
import lombok.Data;

/**
 * @Author: laichengfeng
 * @Description: 消息记录VO
 * @Date: 2019/3/25 20:04
 */
@Data
public class InfoRecordVO {
    /**
     * 消息来源用户id
     */
    private long uid;

    /**
     * 消息来源用户头像
     */
    private String avatarUrl;

    /**
     * 消息类型
     */
    private int type;

    /**
     * 消息id
     */
    private long cid;

    /**
     * 消息来源用户名
     */
    private String nickName;

    /**
     * 消息内容
     */
    private String infoContent;

    /**
     * 源内容
     */
    private String originContent;

    /**
     * 源图片Url
     */
    private String originImgUrl;

    /**
     * 消息记录时间
     */
    private long recordTime;
}
