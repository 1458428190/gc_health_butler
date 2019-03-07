package com.gdufe.health_butler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.entity.Food;
import com.gdufe.health_butler.dao.FoodMapper;
import com.gdufe.health_butler.entity.FoodCategory;
import com.gdufe.health_butler.entity.FoodDetail;
import com.gdufe.health_butler.service.FoodCategoryService;
import com.gdufe.health_butler.service.FoodDetailService;
import com.gdufe.health_butler.service.FoodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 食物表 服务实现类
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@Service
@Transactional
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    @Autowired
    private FoodDetailService foodDetailService;

    @Autowired
    private FoodCategoryService foodCategoryService;

    @Override
    public List<Food> getFoodList(String keyword, long fcid) {

        /**
         * TODO 使用Solr 或 ElasticSearch进行模糊查找
         *             (MySQL不太适合进行全表模糊查询)
         *      暂时使用低效率的代码实现(时间原因)
         */

        QueryWrapper<Food> foodQueryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Food> lambdaFoodQueryWrapper = foodQueryWrapper.lambda();
        List<Food> foodSourceList = new ArrayList<>();
        List<Food> foodRestList = new ArrayList<>();
        // 根据分类和关键字搜索
        if(fcid != 0) {
            // 指定分类中 关键字搜索
            lambdaFoodQueryWrapper.eq(Food::getFcid, fcid);
            foodSourceList.addAll(list(foodQueryWrapper));
            if(StringUtils.isNotBlank(keyword)) {
                foodSourceList.forEach(food -> {
                    if(food.toString().contains(keyword)) {
                        foodRestList.add(food);
                    }
                });
            } else {
                foodRestList.addAll(foodSourceList);
            }
        } else {
            // 非指定分类中 关键字搜索
            if(StringUtils.isNotBlank(keyword)) {
                // 去重
                Set<Long> fidSet = new HashSet<>();
                // 关键字可能是分类名
                List<FoodCategory> foodCategoryList = foodCategoryService.list();
                List<Long> fcidList = new ArrayList<>();
                foodCategoryList.forEach(foodCategory -> {
                    if (foodCategory.toString().contains(keyword)) {
                        fcidList.add(foodCategory.getId());
                    }
                });
                if(fcidList.size() > 0) {
                    QueryWrapper<Food> foodQueryWrapperItem = new QueryWrapper<>();
                    foodQueryWrapperItem.lambda().in(Food::getFcid, fcidList);
                    foodRestList.addAll(list(foodQueryWrapperItem));
                    foodRestList.forEach(food -> fidSet.add(food.getId()));
                }
                // 从食物中 搜索关键字食物
                list().forEach(food -> {
                    if(!fidSet.contains(food.getId()) && food.toString().contains(keyword)) {
                        fidSet.add(food.getId());
                        foodRestList.add(food);
                    }
                });
                // 从食物详情中 搜索关键字食物
                List<FoodDetail> foodDetailList = foodDetailService.list();
                foodDetailList.forEach(foodDetail -> {
                    if (!fidSet.contains(foodDetail.getFid()) && foodDetail.toString().contains(keyword)) {
                        fidSet.add(foodDetail.getFid());
                        foodRestList.add(getById(foodDetail.getId()));
                    }
                });
            }
        }
        return foodRestList;
    }
}
