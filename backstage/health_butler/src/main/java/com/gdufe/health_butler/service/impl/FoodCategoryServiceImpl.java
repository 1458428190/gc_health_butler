package com.gdufe.health_butler.service.impl;

import com.gdufe.health_butler.entity.FoodCategory;
import com.gdufe.health_butler.dao.FoodCategoryMapper;
import com.gdufe.health_butler.service.FoodCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 食物分类表 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
@CacheConfig(cacheNames = "food", keyGenerator = "keyGenerator")
public class FoodCategoryServiceImpl extends ServiceImpl<FoodCategoryMapper, FoodCategory> implements FoodCategoryService {

    @Override
    @Cacheable
    public List<FoodCategory> listAll() {
        return list();
    }
}
