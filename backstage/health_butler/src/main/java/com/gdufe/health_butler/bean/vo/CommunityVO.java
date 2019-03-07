package com.gdufe.health_butler.bean.vo;

import com.gdufe.health_butler.entity.Community;
import com.gdufe.health_butler.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @Author: laichengfeng
 * @Description: 社区分享VO
 * @Date: 2019/3/4 16:39
 */
@Data
public class CommunityVO {

    /**
     * 发表用户
     */
    private User user;

    /**
     * 发表内容
     */
    private Community community;

    /**
     * 是否点了赞
     */
    private boolean isPraise;

    /**
     * 其他用户的赞
     */
    private List<User> praiseUser;
}
