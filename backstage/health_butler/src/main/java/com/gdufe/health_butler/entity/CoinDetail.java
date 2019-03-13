package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康币交易明细表
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CoinDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 交易类型
     * 详见 {@link com.gdufe.health_butler.common.enums.DealType}
     */
    private Integer type;

    /**
     * 交易对象
     */
    @TableField("toUid")
    private Long toUid;

    /**
     * 交易金额
     */
    private Long coin;

    /**
     * 交易描述
     */
    private String description;

    private Long createTime;

    private Long modifiedTime;


}
