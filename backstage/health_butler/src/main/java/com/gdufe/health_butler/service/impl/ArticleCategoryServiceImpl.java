package com.gdufe.health_butler.service.impl;

import com.gdufe.health_butler.entity.ArticleCategory;
import com.gdufe.health_butler.dao.ArticleCategoryMapper;
import com.gdufe.health_butler.service.ArticleCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 文章分类表 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {

}
