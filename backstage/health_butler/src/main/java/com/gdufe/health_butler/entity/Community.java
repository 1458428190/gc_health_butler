package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 社区分享表
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Community implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 社区分享id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 发表内容
     */
    private String content;

    /**
     * 发表图片url list
     */
    private String imgUrlList;

    /**
     * 逻辑删除
     */
    private Boolean isDelete;

    /**
     * 点赞数
     */
    private Integer praise;

    /**
     * 打赏金额总数
     */
    private Integer reward;

    /**
     * 是否仅自己可见, 1-是, 0-不是
     */
    private Boolean onlyMe;

    /**
     * 发表时间
     */
    private Long createTime;

    private Long modifiedTime;


}
