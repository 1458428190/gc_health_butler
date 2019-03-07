package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 社区点赞打赏记录表
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommunityRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分享id
     */
    private Long cid;

    /**
     * 操作的用户id
     */
    private Long fUid;

    /**
     * 被操作的用户id
     */
    private Long toUid;

    /**
     * 分类
     */
    private Integer category;

    /**
     * 内容
     */
    private String content;

    /**
     * 操作时间
     */
    private Long createTime;

    private Long modifiedTime;


}
