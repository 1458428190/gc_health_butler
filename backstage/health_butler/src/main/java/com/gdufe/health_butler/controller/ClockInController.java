package com.gdufe.health_butler.controller;

import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.ClockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: laichengfeng
 * @Description: 打卡 前端控制器
 * @Date: 2019/3/3 20:43
 */
@RestController
@RequestMapping("/clockIn")
public class ClockInController {

    @Autowired
    private ClockInService clockInService;

    /**
     * 获取打卡记录
     * @param token
     * @return
     */
    @AuthToken
    @RequestMapping("/list")
    public ResponseVO list(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, clockInService.list(token));
    }

    /**
     * 打卡
     * @param token
     * @param type
     *          打卡类型
     *          详见 {@link com.gdufe.health_butler.common.enums.RecordType}
     * @return
     */
    @AuthToken
    @RequestMapping("/clockIn")
    public ResponseVO clockIn(@RequestParam String token, @RequestParam int type) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, clockInService.clockIn(token, type));
    }
}
