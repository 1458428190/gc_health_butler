package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 赏金
     */
    private Long reward;

    /**
     * 分类1
     * 详见 {@link }
     */
    private Integer category1;

    /**
     * 分类2
     */
    private Integer category2;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务图片
     */
    private String imgUrl;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 最大完成次数
     */
    private Integer maxCount;

    /**
     * 创建时间
     */
    private Long createTime;

    private Long modifiedTime;


}
