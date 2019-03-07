package com.gdufe.health_butler.service;

import com.gdufe.health_butler.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 健康资讯 服务类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
public interface ArticleService extends IService<Article> {

    /**
     * 查询指定分类的文章列表(分页)
     * @param cid
     * @param page
     * @param size
     * @return
     */
    List<Article> pageListByCid(long cid, int page, int size);

    /**
     * 查询指定分类的文章数量
     */
    int countByCid(long cid);

    /**
     * 查询指定分类的文章(不包含详情)
     * @param cid
     * @return
     */
    List<Article> listByid(long cid);
}
