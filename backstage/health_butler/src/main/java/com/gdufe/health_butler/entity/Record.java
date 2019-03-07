package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户记录表
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 记录类型, (早睡早起运动打卡, 完成任务, 兑换, 步数)
     * 详见 {@link com.gdufe.health_butler.common.enums.RecordType}
     */
    private Integer type;

    /**
     * 扩展字段，各种记录类型可共用
     * 详情:
     *      Type为步数时: extra=step(该天步数)
     *      Type为任务时: extra=tid(任务id)
     *      Type为打卡时: extra=""
     *      Type为兑换时: extra=gid(商品id)
     */
    private String extra;

    /**
     * 记录时间
     */
    private Long createTime;

    private Long modifiedTime;


}
