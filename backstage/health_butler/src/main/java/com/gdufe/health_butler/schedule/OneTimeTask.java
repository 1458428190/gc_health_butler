package com.gdufe.health_butler.schedule;

import com.gdufe.health_butler.schedule.work.ArticleCollectorWork;
import com.gdufe.health_butler.schedule.work.FoodCollectorWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author: laichengfeng
 * @Description: 初始化数据任务
 * @Date: 2019/2/24 10:37
 */
@Component
@Order(1)
@ConditionalOnExpression("${schedule.oneTimeTask.start}")
public class OneTimeTask implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FoodCollectorWork foodCollectorWork;

    @Autowired
    private ArticleCollectorWork articleCollectorWork;

    @Override
    public void run(String... args) {
        logger.info("[op:OneTimeTask Run]");
        foodCollectorWork.collectFoodCategory();
        foodCollectorWork.collectFoodList();
        foodCollectorWork.collectFoodDetail();

        articleCollectorWork.collectorArticleCategory();
        articleCollectorWork.collectHistoryArticle();
        logger.info("[op_rslt:success]");
    }
}
