package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 食物分类表
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FoodCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 食物分类id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 食物分类名
     */
    private String name;

    /**
     * 食物分类图片
     */
    private String imgUrl;

    private Long createTime;

    private Long modifiedTime;


}
