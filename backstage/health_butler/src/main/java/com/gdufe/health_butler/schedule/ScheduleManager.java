package com.gdufe.health_butler.schedule;

import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.schedule.work.ArticleCollectorWork;
import com.gdufe.health_butler.schedule.work.TaskRewardWork;
import com.gdufe.health_butler.service.UserService;
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

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRewardWork taskRewardWork;

    /**
     * 每日3点收集文章
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void collectArticle() {
        articleCollectorWork.collectNewArticle();
    }

    /**
     * 清理过期token
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void tokenClean() {
        TokenContainer.clean();
    }

    /**
     * 每天23:59:59点清理步数
     */
    @Scheduled(cron = "59 59 23 * * ?")
    public void nowStepClean() {
        userService.cleanNowStep();
    }

    /**
     * 每天23.54分奖励打卡和步数
     */
    @Scheduled(cron = "0 54 23 * * ?")
    public void taskReward() {
        taskRewardWork.clockReward();
        taskRewardWork.stepReward();
    }
}
