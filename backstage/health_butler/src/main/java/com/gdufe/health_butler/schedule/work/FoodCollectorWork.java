package com.gdufe.health_butler.schedule.work;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gdufe.health_butler.common.enums.FoodSourceType;
import com.gdufe.health_butler.common.util.HttpUtils;
import com.gdufe.health_butler.entity.Food;
import com.gdufe.health_butler.entity.FoodCategory;
import com.gdufe.health_butler.entity.FoodDetail;
import com.gdufe.health_butler.service.FoodCategoryService;
import com.gdufe.health_butler.service.FoodDetailService;
import com.gdufe.health_butler.service.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 食谱收集器
 * @Date: 2019/2/22 22:33
 */
@Component
public class FoodCollectorWork {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DOMAIN = "https://api.160nurse.com";

    private static final String FOOD_CATEGORY_URL = DOMAIN + "/index.php?c=foods&a=category";

    private static final String FOOD_LIST_URL = DOMAIN + "/foods/foods?cat_name=%s";

    private static final String FOOD_DETAIL_URL = DOMAIN + "/index.php?c=foods&a=food&food_id=%s";

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodCategoryService foodCategoryService;

    @Autowired
    private FoodDetailService foodDetailService;

    /**
     * 收集食谱分类
     */
    public void collectFoodCategory() {
        logger.info("[op:collectFoodCategory]");
        try {
            String content = HttpUtils.get(FOOD_CATEGORY_URL, null);
            JSONObject categoryContent = JSON.parseObject(content);
            List<FoodCategory> list = new ArrayList<>();
            ((List<Map<String, String>>)(categoryContent.get("data"))).forEach(categoryMap -> {
//                long id = Long.parseLong(category.get("id") + "");          // 第三方id
                String catName = categoryMap.get("cat_name") + "";
//                String parentName = category.get("parent_name") + "";       // 父名
                String imgUrl = categoryMap.get("img_url") + "";
//                int sort = Integer.parseInt(category.get("sort") + "");     // 排名
                FoodCategory foodCategory = new FoodCategory();
                foodCategory.setName(catName);
                foodCategory.setImgUrl(imgUrl);
                foodCategory.setCreateTime(System.currentTimeMillis());
                foodCategory.setModifiedTime(System.currentTimeMillis());
                list.add(foodCategory);
            });
            foodCategoryService.saveBatch(list);
            logger.info("[op_rslt: success]");
        } catch (Exception e) {
            logger.error("[op_rslt: exception]", e);
        }
    }

    /**
     * 收集水果简略列表
     */
    public void collectFoodList() {
        logger.info("[op:collectFoodList]");
        List<FoodCategory> foodCategoryList = foodCategoryService.list();
        foodCategoryList.forEach(foodCategory -> {
            try {
                logger.info("[op:collectFoodListItem, foodCategory: {}]", JSON.toJSON(foodCategory));
                String catName = foodCategory.getName();
                long fcid = foodCategory.getId();
                String content = HttpUtils.get(String.format(FOOD_LIST_URL, catName), null);
                JSONObject foodListContent = JSON.parseObject(content);
                List<Map<String, Object>> foodList = (List<Map<String, Object>>)
                        ((Map<String, Object>) (foodListContent.get("data"))).get("list");
                List<Food> list = new ArrayList<>();
                foodList.forEach((foodMap) -> {
                    String foodId = foodMap.get("food_id") + "";
                    String foodName = foodMap.get("food_name") + "";
                    String listImg = foodMap.get("list_img") + "";
//                    List<String> aliasName = (List<String>) foodMap.get("alias_name");
                    String brief = foodMap.get("brief") + "";
                    int affect = Integer.parseInt(foodMap.get("affect") + "");
                    int nutrition = Integer.parseInt(foodMap.get("nutrition") + "");
                    int avoidNum = Integer.parseInt(foodMap.get("avoid_num") + "");
                    int suitableNum = Integer.parseInt(foodMap.get("suitable_num") + "");
                    Food food = new Food();
                    food.setAffect(affect);
                    food.setAvoidNum(avoidNum);
                    food.setBrief(brief);
                    food.setCreateTime(System.currentTimeMillis());
                    food.setFcid(fcid);
                    food.setImgUrl(listImg);
                    food.setModifiedTime(System.currentTimeMillis());
                    food.setName(foodName);
                    food.setNutrition(nutrition);
                    food.setSource(FoodSourceType.HEAHTH_160.getKey());
                    food.setSourceId(foodId);
                    food.setSuitableNum(suitableNum);
                    list.add(food);
                });
                foodService.saveBatch(list);
                logger.info("[op_item_rslt: success]");
            } catch (Exception e) {
                logger.error("[op_item_rslt: exception]", e);
            }
        });
        logger.info("[op_rslt:success]");
    }

    /**
     * 收集所有食物详情
     */
    public void collectFoodDetail() {
        logger.info("[op:collectFoodList]");

        foodService.list().forEach(food -> {
            try {
                String sourceId = food.getSourceId();
                logger.info("[op:collectFoodList_item, sourceId:{}]", sourceId);
                long fid = food.getId();
                String content = HttpUtils.get(String.format(FOOD_DETAIL_URL, sourceId), null);
                JSONObject foodDetailContent = JSON.parseObject(content);
                Map<String, Object> foodDetailMap = (Map<String, Object>) foodDetailContent.get("data");
                String aliasName = foodDetailMap.get("alias_name") + "";
//                String brief = foodDetailMap.get("brief") + "";
//                String imgUrl = foodDetailMap.get("img_url") + "";
                String nutrition = foodDetailMap.get("nutrition") + "";
                String affect = foodDetailMap.get("affect") + "";
                String avoid = foodDetailMap.get("avoid") + "";
                String suitable = foodDetailMap.get("suitable") + "";
//                int foodsVoidSuit = Integer.parseInt(foodDetailMap.get("foods_void_suit")+"");
                String foodsAvoid = foodDetailMap.get("foods_avoid") + "";
                String foodsSuit = foodDetailMap.get("foods_suit") + "";

                FoodDetail foodDetail = new FoodDetail();
                foodDetail.setAffect(affect);
                foodDetail.setAliasName(aliasName);
                foodDetail.setAvoid(avoid);
                foodDetail.setCreateTime(System.currentTimeMillis());
                foodDetail.setFid(fid);
                foodDetail.setFoodsAvoid(foodsAvoid);
                foodDetail.setFoodsSuit(foodsSuit);
                foodDetail.setModifiedTime(System.currentTimeMillis());
                foodDetail.setNutrition(nutrition);
                foodDetail.setSuitable(suitable);
                foodDetailService.save(foodDetail);
                logger.info("[op_item_rslt: success]");
            } catch (Exception e) {
                logger.error("[op_item_rslt: exception]", e);
            }
        });
        logger.info("[op_rslt:success]");
    }

}
