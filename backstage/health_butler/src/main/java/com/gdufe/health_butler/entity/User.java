package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * open_id用于标记小程序用户
     */
    private String openId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别, 0-未知, 1-男, 2-女
     */
    private Integer gender;

    /**
     * 城市
     */
    private String city;

    /**
     * 省
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 健康币
     */
    private Long healthCoin;

    /**
     * 赏金
     */
    private Long reward;

    /**
     * 早起提醒时间, 后缀为-remind即为提醒, cancel即为取消
     */
    private String morningTime;

    /**
     * 早睡提醒时间
     */
    private String nightTime;

    /**
     * 运动提醒时间
     */
    private String sportTime;

    /**
     * 总步数
     */
    private Integer sumStep;

    /**
     * 当天步数
     */
    private Integer nowStep;

    /**
     * 封面图片url
     */
    private String coverImgUrl;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后一次修改时间
     */
    private Long modifiedTime;


}
