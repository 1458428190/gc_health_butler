package com.gdufe.health_butler.service;

import com.gdufe.health_butler.entity.ArticleCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 文章分类表 服务类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
public interface ArticleCategoryService extends IService<ArticleCategory> {
    /**
     * 查询所有
     * @return
     */
    List<ArticleCategory> listAll();
}
