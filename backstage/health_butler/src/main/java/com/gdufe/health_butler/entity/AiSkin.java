package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * ai测肤记录表
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AiSkin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    private String imgUrl;

    /**
     * face_token
     */
    private String faceToken;

    /**
     * 男性认为的颜值评分
     */
    private Double beautyF;

    /**
     * 女性认为的颜值评分
     */
    private Double beautyM;

    /**
     * 人脸质量评分
     */
    private Double faceQuality;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 皮肤状况
     */
    private Integer skinStatus;

    private Long createTime;

    private Long modifiedTime;


}
