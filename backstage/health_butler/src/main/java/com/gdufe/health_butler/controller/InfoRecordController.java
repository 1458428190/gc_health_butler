package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.InfoRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户消息记录表 前端控制器
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-26
 */
@RestController
@RequestMapping("/infoRecord")
public class InfoRecordController {

    @Autowired
    private InfoRecordService infoRecordService;

    /**
     * 获取未读消息个数
     * @param token
     * @return
     */
    @AuthToken
    @RequestMapping("/getUnReadInfoCount")
    public ResponseVO getUnReadInfoCount(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, infoRecordService.getUnReadInfoCount(token));
    }

    /**
     * 获取未读消息
     * @param token
     * @return
     */
    @AuthToken
    @RequestMapping("/getUnReadInfo")
    public ResponseVO getUnReadInfo(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, infoRecordService.getUnReadInfo(token));
    }


    /**
     * 获取已读消息
     * @param token
     * @return
     */
    @AuthToken
    @RequestMapping("/getReadInfo")
    public ResponseVO getReadInfo(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, infoRecordService.getReadInfo(token));
    }
}

