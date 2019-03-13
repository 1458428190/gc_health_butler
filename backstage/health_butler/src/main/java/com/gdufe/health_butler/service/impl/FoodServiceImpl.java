package com.gdufe.health_butler.service.impl;

import com.alibaba.fastjson.JSON;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        logger.info("[op:getFoodList, keyword: {} fcid:{}]", keyword, fcid);

        List<Food> foodRestList = new ArrayList<>();
        if(StringUtils.isNotBlank(keyword)) {
            fcid = 0;
        }
        // 根据分类和关键字搜索
        if(fcid != 0) {
            // 指定分类中 关键字搜索
            QueryWrapper<Food> foodQueryWrapper = new QueryWrapper<>();
            foodQueryWrapper.lambda().eq(Food::getFcid, fcid);
            foodRestList.addAll(list(foodQueryWrapper));
        } else {
            // 非指定分类中 关键字搜索
            if(StringUtils.isNotBlank(keyword)) {
                // 去重
                Set<Long> fidSet = new HashSet<>();
                // 关键字可能是分类名
                QueryWrapper<FoodCategory> foodCategoryQueryWrapper = new QueryWrapper<>();
                foodCategoryQueryWrapper.lambda().select(FoodCategory::getId).like(FoodCategory::getName, keyword);
                List<FoodCategory> foodCategoryList = foodCategoryService.list(foodCategoryQueryWrapper);
                List<Long> fcidList = new ArrayList<>();
                foodCategoryList.forEach(foodCategory -> fcidList.add(foodCategory.getId()));
                if(fcidList.size() > 0) {
                    QueryWrapper<Food> foodQueryWrapperItem = new QueryWrapper<>();
                    foodQueryWrapperItem.lambda().in(Food::getFcid, fcidList);
                    foodRestList.addAll(list(foodQueryWrapperItem));
                    foodRestList.forEach(food -> fidSet.add(food.getId()));
                }
                QueryWrapper<Food> foodQueryWrapper = new QueryWrapper<>();
                foodQueryWrapper.lambda().like(Food::getName, keyword)
                        .or().like(Food::getBrief, keyword);
                // 从食物中 搜索关键字食物
                list(foodQueryWrapper).forEach(food -> {
                    if(!fidSet.contains(food.getId())) {
                        fidSet.add(food.getId());
                        foodRestList.add(food);
                    }
                });

                // 使用模糊查找
                QueryWrapper<FoodDetail> foodDetailQueryWrapper = new QueryWrapper<>();
                foodDetailQueryWrapper.lambda().like(FoodDetail::getAliasName, keyword).or()
                        .like(FoodDetail::getAffect, keyword).or().like(FoodDetail::getNutrition, keyword)
                        .or().like(FoodDetail::getAvoid, keyword).or().like(FoodDetail::getSuitable, keyword).or()
                        .like(FoodDetail::getFoodsSuit, keyword).or().like(FoodDetail::getFoodsAvoid, keyword);

                List<FoodDetail> foodDetailList = foodDetailService.list(foodDetailQueryWrapper);
                foodDetailList.forEach(foodDetail -> {
                    if (!fidSet.contains(foodDetail.getFid())) {
                        fidSet.add(foodDetail.getFid());
                        foodRestList.add(getById(foodDetail.getId()));
                    }
                });
            }
        }
        logger.info("[op_rslt: sueccess, foodRestList: {}]", JSON.toJSON(foodRestList));
        return foodRestList;
    }
}
