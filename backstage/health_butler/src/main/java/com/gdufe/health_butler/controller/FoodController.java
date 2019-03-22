package com.gdufe.health_butler.controller;

import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.FoodCategoryService;
import com.gdufe.health_butler.service.FoodDetailService;
import com.gdufe.health_butler.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 食物表 前端控制器
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodCategoryService foodCategoryService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodDetailService foodDetailService;

    /**
     * 查询分类
     * @return
     */
    @RequestMapping(value = "/category")
    public ResponseVO getFoodCategory() {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, foodCategoryService.listAll());
    }

    /**
     * 查询食物列表
     * @param keyword
     *          关键字
     * @param fcid
     *          食物分类
     * @return
     */
    @RequestMapping("/list")
    public ResponseVO getFoodList(@RequestParam String keyword,
                                  @RequestParam long fcid) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, foodService.getFoodList(keyword, fcid));
    }

    /**
     * 查询食物详情
     * @param fid
     * @return
     */
    @RequestMapping("/detail")
    public ResponseVO getFoodDetail(@RequestParam long fid) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, foodDetailService.getByFid(fid));
    }
}

