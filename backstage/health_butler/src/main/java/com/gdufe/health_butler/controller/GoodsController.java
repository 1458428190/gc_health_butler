package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 查看兑换商品
     * @return
     */
    @RequestMapping("/list")
    public ResponseVO list() {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, goodsService.listToUser());
    }

    /**
     * 兑换商品
     * @param token
     * @param id
     * @return
     */
    @AuthToken
    @RequestMapping("/conversion")
    public ResponseVO conversion(@RequestParam String token, @RequestParam long id) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, goodsService.conversion(token, id));
    }
}

