package com.gdufe.health_butler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康资讯
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资讯id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章来源
     * 详见 {@link com.gdufe.health_butler.common.enums.ArticleSourceType}
     */
    private Integer source;

    /**
     * 第三方id
     */
    private String sourceId;

    /**
     * 作者
     */
    private String author;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章缩略图
     */
    private String imgUrl;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 类别
     */
    private Long cid;

    /**
     * 观看人数
     */
    private Integer see;

    /**
     * 文章正文内容 (html格式)
     */
    private String content;

    /**
     * 创建时间
     */
    private Long createTime;

    private Long modifiedTime;


}
