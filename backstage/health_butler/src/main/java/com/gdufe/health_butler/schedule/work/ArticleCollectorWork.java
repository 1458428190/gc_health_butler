package com.gdufe.health_butler.schedule.work;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.common.enums.ArticleSourceType;
import com.gdufe.health_butler.common.util.HttpUtils;
import com.gdufe.health_butler.entity.Article;
import com.gdufe.health_butler.entity.ArticleCategory;
import com.gdufe.health_butler.service.ArticleCategoryService;
import com.gdufe.health_butler.service.ArticleService;
import com.vdurmont.emoji.EmojiParser;
import org.apache.http.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 文章收集器 (暂时来源为香山健康)
 * @Date: 2019/2/22 16:32
 */
@Component
public class ArticleCollectorWork {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DOMAIN = "www.senssun.com";

    private static final String ARTICLE_CATEGORY_URL = "https://"+DOMAIN + "/classifyLoad.htm";

    private static final String ARTICLE_URL = "https://" + DOMAIN + "/zxload.htm?pageNum=%d&childTypeCode=%s";

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Autowired
    private ArticleService articleService;

    /**
     * 收集文章分类
     * @throws HttpException
     */
    public void collectorArticleCategory() {
        logger.info("[op:collectorArticleCategory]");
        try {
            String content = HttpUtils.get(ARTICLE_CATEGORY_URL, null);
            JSONArray categoryContent = JSON.parseArray(content);
            List<ArticleCategory> list = new ArrayList<>();
            categoryContent.forEach(category -> {
                Map<String, Object> map = (Map<String, Object>) category;
                List<Map<String, Object>> secondClassify = (List<Map<String, Object>>) map.get("secondClassify");
                long firstId = Long.parseLong(map.get("id") + "");
                String firstClassify = (String) map.get("firstClassify");
                if(!firstClassify.contains("产品评测")) {
                    secondClassify.forEach((classify) -> {
                        String name = (String) classify.get("name");
                        long secondId = Long.parseLong(classify.get("id") + "");
                        ArticleCategory articleCategory = new ArticleCategory();
                        articleCategory.setFirstId(firstId);
                        articleCategory.setFirstClassify(firstClassify);
                        articleCategory.setSecondId(secondId);
                        articleCategory.setName(name);
                        articleCategory.setCreateTime(System.currentTimeMillis());
                        articleCategory.setModifiedTime(System.currentTimeMillis());
                        list.add(articleCategory);
                    });
                }
            });
            // 批量插入
            articleCategoryService.saveBatch(list);
            logger.info("[op_rslt:success]");
        } catch (Exception e) {
            logger.error("[op_rslt:exception]", e);
        }
    }

    /**
     * 收集历史文章
     */
    public void collectHistoryArticle() {
        logger.info("[op:collectHistoryArticle]");
        // 收集所有文章分类的文章
        articleCategoryService.list().forEach(articleCategory -> {
            long secondId = articleCategory.getSecondId();
            long cid = articleCategory.getId();
            int pageNum = 1;
            for(; collectArticleForPage(cid, secondId, pageNum); pageNum++);
        });
        logger.info("[op_rslt:success]");
    }

    /**
     * 收集新文章
     */
    public void collectNewArticle() {
        logger.info("[op:collectNewArticle]");
        // 收集所有文章分类的文章
        articleCategoryService.list().forEach(articleCategory -> {
            long secondId = articleCategory.getSecondId();
            long cid = articleCategory.getId();
            int pageNum = 1;
            collectArticleForPage(cid, secondId, pageNum);
        });
        logger.info("[op_rslt:success]");
    }

    /**
     * 收集指定分类指定页面
     * @param secondId
     *          对应香山健康
     * @param pageNum
     */
    private boolean collectArticleForPage(long cid, long secondId, int pageNum) {
        logger.info("[op:collectArticleForPage, cid:{}, secondId:{}, pageNum:{}]", cid, secondId, pageNum);
        try {
            String content = HttpUtils.get(String.format(ARTICLE_URL, pageNum, secondId), null);
            JSONArray articleContent = JSON.parseArray(content);
            List<Article> list = new ArrayList<>();
            articleContent.forEach(articleMap -> {
                Map<String, Object> map = (Map<String, Object>) articleMap;
                Article article = convertArticle(map, cid);
                if (null != article) {
                    list.add(article);
                }
            });
            articleService.saveBatch(list);
            logger.info("[op_rslt:success]");
            return list.size() > 0;
        } catch (Exception e) {
            logger.error("[op_rslt:exception]", e);
            return false;
        }
    }

    /**
     * 将第三方文章数据转为自定义文章
     * @param map
     * @param cid
     * @return
     */
    private Article convertArticle(Map<String, Object> map, long cid) {
        long createTime = Long.parseLong(map.get("createTime") + "");
        String imgUrl = ((List<String>)(map.get("images"))).get(0);
        if(!imgUrl.contains(DOMAIN)) {
            imgUrl = "https://" + DOMAIN + imgUrl;
        }
        long modifyTime = Long.parseLong(map.get("modifyTime") + "");
        String pageAuthor = map.get("pageAuthor") + "";
        String pageContent = convertToRichText(map.get("pageContent") + "");
        String pageTitle = map.get("pageTitle") + "";
        int pageView = Integer.parseInt(map.get("pageView") + "");
        String id = map.get("id") + "";

        Article article = new Article();
        article.setTitle(pageTitle);
        // 去除表情字符
        article.setContent(EmojiParser.removeAllEmojis(pageContent));
        article.setCreateTime(createTime);
        article.setImgUrl(imgUrl);
        article.setKeywords("");
        article.setModifiedTime(modifyTime);
        article.setSee(pageView);
        article.setSource(ArticleSourceType.XIANG_SHAN.getKey());
        article.setSourceId(id);
        article.setCid(cid);
        article.setAuthor(pageAuthor);

        // 构建查询条件,判断是否已经抓取过
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Article::getSourceId, id);
        if (articleService.getOne(queryWrapper) != null) {
            return null;
        }
        return article;
    }

    /**
     * 转换为微信富文本能识别的标签
     * @return
     */
    private String convertToRichText(String pageContent) {
        Document doc = Jsoup.parse(pageContent);
        Elements imgs = doc.select("img");
        imgs.forEach(img -> {
            String src = img.attr("src");
            if(!src.contains(DOMAIN)) {
                src = "https://" + DOMAIN + src;
            }
            img.attr("src", src);
        });
        pageContent = doc.body().html();
        return pageContent.replaceAll("<section", "<div")
                .replaceAll("section>", "div>")
                .replaceAll("px", "rpx");
    }

    public static void main(String[] args) {
        ArticleCollectorWork articleCollectorWork = new ArticleCollectorWork();
        articleCollectorWork.collectorArticleCategory();
    }
}
