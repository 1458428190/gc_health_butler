package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户消息记录表
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InfoRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    /**
     * 信息类型
     *  详见 {@link com.gdufe.health_butler.common.enums.InfoRecordType}
     */
    private Integer type;

    /**
     * 是否已读
     */
    private Boolean readStatus;

    /**
     * 消息对应的id
     */
    private Long mid;

    private Long createTime;

    private Long modifiedTime;


}
