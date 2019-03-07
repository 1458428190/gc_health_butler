package com.gdufe.health_butler.bean.vo;

import com.gdufe.health_butler.entity.User;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 排名 界面 VO  (4大: 指今日步数,健康财富,健康早起,昨日早睡)
 * @Date: 2019/3/1 15:11
 */
@Data
public class RankVO {

    /**
     * 4大冠军
     */
    private List<User> championList;

    /**
     * 登录用户的4大排名
     */
    private List<Integer> userRank;

    /**
     * 用户信息
     */
    private User user;

    /**
     * 登录用户的4大值
     */
    private List<String> userData;

    /**
     * 4大排名总数据
     */
    private List<List<Map<String, Object>>> rankData;

}
