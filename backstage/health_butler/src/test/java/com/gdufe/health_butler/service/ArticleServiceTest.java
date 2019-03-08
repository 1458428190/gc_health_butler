package com.gdufe.health_butler.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdufe.health_butler.HealthButlerApplicationTests;
import com.gdufe.health_butler.entity.Article;
import com.gdufe.health_butler.entity.ArticleCategory;
import com.gdufe.health_butler.entity.Food;
import com.gdufe.health_butler.entity.Record;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/2/21 13:44
 */
public class ArticleServiceTest extends HealthButlerApplicationTests {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Test
    public void testSave() {
        Record record = new Record();
        record.setId(1L);
        record.setExtra("测试");
        record.setType(1);
        record.setUid(1L);
        record.setCreateTime(System.currentTimeMillis());
        record.setModifiedTime(System.currentTimeMillis());
        recordService.save(record);
        System.out.println(record);
    }

    @Test
    public void test() {
//        List<Long> list = new ArrayList<>();
//        list.add(1L);
//        list.add(2L);
//        QueryWrapper<Food> foodQueryWrapperItem = new QueryWrapper<>();
//        foodQueryWrapperItem.lambda().in(Food::getFcid, list);
//        List<Food> list1 = foodService.list(foodQueryWrapperItem);
//        System.out.println(list1);
//        List<ArticleCategory> list = articleCategoryService.list();
//        System.out.println(list);
//        List<Article> articles = articleService.pageListByCid(1, 0, 8);
//        System.out.println(articles);

        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        long endTime = calendar.getTimeInMillis();

        System.out.println(startTime + "     " + endTime);
    }

    @Test
    public void pageListByCid() {
//        List<Article> articles = articleService.pageListByCid(1, 1, 10);
//        System.out.println(articles);

        UpdateWrapper<Article> articleUpdateWrapper = new UpdateWrapper<>();
        articleUpdateWrapper.lambda().setSql("see = see+1").eq(Article::getId, 1);
        articleService.update(articleUpdateWrapper);
    }
}
