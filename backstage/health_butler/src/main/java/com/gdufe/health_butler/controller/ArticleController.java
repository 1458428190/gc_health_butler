package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.entity.Article;
import com.gdufe.health_butler.service.ArticleCategoryService;
import com.gdufe.health_butler.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 健康资讯 前端控制器
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Autowired
    private ArticleService articleService;

    /**
     * 查询文章分类
     * @return
     */
    @RequestMapping("/category")
    public ResponseVO getArticleCategoryList() {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, articleCategoryService.list());
    }

    /**
     * 查询指定分类文章
     * @param cid
     *          分类id
     * @param page
     *          页数
     * @param size
     *          大小
     * @return
     */
    @RequestMapping("/pageList")
    public ResponseVO getArticlePageList(@RequestParam long cid, @RequestParam int page, @RequestParam int size) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, articleService.pageListByCid(cid, page, size));
    }

    /**
     * 查询指定分类文章
     * @param cid
     * @return
     */
    @RequestMapping("/list")
    public ResponseVO getArticleList(@RequestParam long cid) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, articleService.listByid(cid));
    }


    /**
     * 查询文章数
     * @param cid
     * @return
     */
    @RequestMapping("/list_size")
    public ResponseVO getArticleListSize(@RequestParam long cid) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, articleService.countByCid(cid));
    }

    /**
     * 查询文章
     * @param id
     * @return
     */
    @RequestMapping("/detail")
    public ResponseVO getArticle(@RequestParam long id) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, filter(articleService.getById(id)));
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


