package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 食物详请表
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FoodDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 食物详请id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 食物id
     */
    private Long fid;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 功效
     */
    private String affect;

    /**
     * 营养
     */
    private String nutrition;

    /**
     * 禁忌人群
     */
    private String avoid;

    /**
     * 适宜人群
     */
    private String suitable;

    /**
     * 宜搭食物
     */
    private String foodsSuit;

    /**
     * 相克食物
     */
    private String foodsAvoid;

    private Long createTime;

    private Long modifiedTime;


}
