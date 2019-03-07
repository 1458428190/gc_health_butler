package com.gdufe.health_butler.service;

import com.gdufe.health_butler.entity.Food;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 食物表 服务类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
public interface FoodService extends IService<Food> {

    /**
     * 查询食物列表
     * @param keyword
     *          关键字
     * @param fcid
     *          分类id
     * @return
     */
    List<Food> getFoodList(String keyword, long fcid);
}
