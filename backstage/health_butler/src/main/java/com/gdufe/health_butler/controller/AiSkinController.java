package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.AiSkinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * ai测肤记录表 前端控制器
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-10
 */
@RestController
@RequestMapping("/aiSkin")
public class AiSkinController {

    @Autowired
    private AiSkinService aiSkinService;

    /**
     * 测肤
     * @param token
     * @param file
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/measure", method = RequestMethod.POST)
    public ResponseVO measure(@RequestParam String token, @RequestParam(value = "file", required = false) MultipartFile file) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, aiSkinService.measure(token, file));
    }

    /**
     * 测肤历史
     * @param token
     * @return
     */
    @AuthToken
    @RequestMapping("/history")
    public ResponseVO history(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, aiSkinService.history(token));
    }

    /**
     * 查询指定记录
     * @param token
     * @param id
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/inquiry")
    public ResponseVO inquiry(@RequestParam String token, @RequestParam long id) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, aiSkinService.inquiry(token, id));
    }
}

