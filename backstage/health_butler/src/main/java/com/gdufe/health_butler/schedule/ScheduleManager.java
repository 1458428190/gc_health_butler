package com.gdufe.health_butler.schedule;

import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.schedule.work.ArticleCollectorWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: laichengfeng
 * @Description: 定时任务管理器
 * @Date: 2019/2/24 10:32
 */
@Component
public class ScheduleManager {

    @Autowired
    private ArticleCollectorWork articleCollectorWork;

    /**
     * 收集文章
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void collectArticle() {
        articleCollectorWork.collectNewArticle();
    }
//
//    /**
//     * 清理过期token
//     */
//    @Scheduled(cron = "0 0 * * * ?")
//    public void tokenClean() {
//        TokenContainer.clean();
//    }
}
