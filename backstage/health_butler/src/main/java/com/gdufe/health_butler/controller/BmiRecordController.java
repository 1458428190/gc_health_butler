package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.BmiRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  BMI指数 前端控制器
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-04
 */
@RestController
@RequestMapping("/bmiRecord")
public class BmiRecordController {

    @Autowired
    private BmiRecordService bmiRecordService;

    /**
     * 保存bmi的计算记录
     * @param token
     * @param height
     * @param weight
     * @return
     */
    @AuthToken
    @RequestMapping("/save")
    public ResponseVO save(@RequestParam String token, @RequestParam double height, @RequestParam double weight) {
        bmiRecordService.saveRecord(token, height, weight);
        return new ResponseVO(ResponseStatusEnum.SUCCESS, "");
    }

    @AuthToken
    @RequestMapping("/list")
    public ResponseVO list(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, bmiRecordService.listByToken(token));
    }
}

