package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名
     */
    private String name;

    /**
     * 所需健康币
     */
    private Long price;

    /**
     * 商品图片url
     */
    private String imgUrl;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 剩余数
     */
    private Integer surplus;

    /**
     * 加入时间
     */
    private Long createTime;

    private Long modifiedTime;


}
