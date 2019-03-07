package com.gdufe.health_butler.service.impl;

import com.gdufe.health_butler.entity.FoodCategory;
import com.gdufe.health_butler.dao.FoodCategoryMapper;
import com.gdufe.health_butler.service.FoodCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 食物分类表 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
@Transactional
public class FoodCategoryServiceImpl extends ServiceImpl<FoodCategoryMapper, FoodCategory> implements FoodCategoryService {

}
