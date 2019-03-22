package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdufe.health_butler.entity.Article;
import com.gdufe.health_butler.dao.ArticleMapper;
import com.gdufe.health_butler.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 健康资讯 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
@CacheConfig(cacheNames = "article", keyGenerator = "keyGenerator")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Cacheable
    public List<Article> pageListByCid(long cid, int page, int size) {
        logger.info("[op:pageListByCid, cid={}, page={}, size={}]", cid, page, size);
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.lambda().select(Article::getId, Article::getAuthor, Article::getTitle,
                Article::getImgUrl, Article::getKeywords, Article::getSee, Article::getCreateTime)
                .eq(Article::getCid, cid).orderByDesc(Article::getCreateTime);
        Page<Article> articlePage = new Page<>(page, size);
        return page(articlePage, articleQueryWrapper).getRecords();
    }

    @Override
    @Cacheable
    public int countByCid(long cid) {
        logger.info("[op:countByCid, cid={}]", cid);
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.lambda().eq(Article::getCid, cid);
        return count(articleQueryWrapper);
    }

    @Override
    @Cacheable
    public List<Article> listByid(long cid) {
        logger.info("[op:listByid, cid={}]", cid);
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.lambda().select(Article::getId, Article::getAuthor, Article::getTitle,
                Article::getImgUrl, Article::getKeywords, Article::getSee, Article::getCreateTime)
                .eq(Article::getCid, cid).orderByDesc(Article::getCreateTime);
        return list(articleQueryWrapper);
    }

    @Override
    public void updateSee(long id) {
        logger.info("[op:updateSee, id={}]", id);
        UpdateWrapper<Article> articleUpdateWrapper = new UpdateWrapper<>();
        articleUpdateWrapper.lambda().setSql("see = see+1").eq(Article::getId, id);
        update(articleUpdateWrapper);
    }

    @Override
    @Cacheable
    public Article getByIdAndCache(long id) {
        return filter(getById(id));
    }

    /**
     * 过滤文章
     * @param article
     * @return
     */
    private Article filter(Article article) {
        return article.setContent(article.getContent().replaceAll("px", "rpx"));
    }
}
