package com.gdufe.health_butler.service;

import com.gdufe.health_butler.entity.FoodCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 食物分类表 服务类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
public interface FoodCategoryService extends IService<FoodCategory> {
    List<FoodCategory> listAll();
}
