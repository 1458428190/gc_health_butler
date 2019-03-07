package com.gdufe.health_butler.schedule.work;

import com.gdufe.health_butler.HealthButlerApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/2/24 9:46
 */
public class FoodCollectorWorkTest extends HealthButlerApplicationTests {

    @Autowired
    private FoodCollectorWork foodCollectorWork;

    @Test
    public void collectFoodCategory() {
        foodCollectorWork.collectFoodCategory();
    }

    @Test
    public void collectFoodList() {
        foodCollectorWork.collectFoodList();
    }

    @Test
    public void collectFoodDetail() {
        foodCollectorWork.collectFoodDetail();
    }
}
