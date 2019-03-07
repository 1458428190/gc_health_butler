package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 食物表
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 食物id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据来源平台
     * 详见 {@link com.gdufe.health_butler.common.enums.FoodSourceType}
     */
    private Integer source;

    /**
     * 第三方id
     */
    private String sourceId;

    /**
     * 分类id
     */
    private Long fcid;

    /**
     * 食物名
     */
    private String name;

    /**
     * 食物图片
     */
    private String imgUrl;

    /**
     * 功能
     */
    private String brief;

    /**
     * 功效个数
     */
    private Integer affect;

    /**
     * 营养个数
     */
    private Integer nutrition;

    /**
     * 相克个数
     */
    private Integer avoidNum;

    /**
     * 宜搭个数
     */
    private Integer suitableNum;

    private Long createTime;

    private Long modifiedTime;


}
