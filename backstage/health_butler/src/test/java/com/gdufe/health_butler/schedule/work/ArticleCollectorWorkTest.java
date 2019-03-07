package com.gdufe.health_butler.schedule.work;

import com.gdufe.health_butler.HealthButlerApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/2/22 22:14
 */
public class ArticleCollectorWorkTest extends HealthButlerApplicationTests {

    @Autowired
    private ArticleCollectorWork articleCollectorWork;

    @Test
    public void collectorArticleCategory() {
        articleCollectorWork.collectorArticleCategory();
    }

    @Test
    public void collectHistoryArticle() {
        articleCollectorWork.collectHistoryArticle();
    }

    @Test
    public void collectArticleForPage() {
    }

    @Test
    public void convertArticle() {
    }

    @Test
    public void collectNewArticle() {
        articleCollectorWork.collectNewArticle();
    }
}
