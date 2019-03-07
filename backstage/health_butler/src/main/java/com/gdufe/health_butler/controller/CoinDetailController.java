package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.CoinDetailVO;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.CoinDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 健康币交易明细表 前端控制器
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
@RestController
@RequestMapping("/coinDetail")
public class CoinDetailController {

    @Autowired
    private CoinDetailService coinDetailService;

    /**
     * 查询健康币明细
     */
    @AuthToken
    @RequestMapping("/list")
    public ResponseVO getCoinDetail(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, coinDetailService.getDetail(token));
    }
}

