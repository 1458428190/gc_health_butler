package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.RecordService;
import com.gdufe.health_butler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户记录表 前端控制器
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private TaskService taskService;

    /**
     * 上传步数数据
     */
    @AuthToken
    @RequestMapping("/getAndUploadRunData")
    public ResponseVO getAndUploadRunData(@RequestParam String token, @RequestParam String iv,
                                    @RequestParam String encryptedData) {
        recordService.saveOrUpdateRunData(token, iv.replaceAll(" ", "+"), encryptedData.replaceAll(" ", "+"));
        return new ResponseVO(ResponseStatusEnum.SUCCESS, "");
    }


    /**
     * 查询任务列表(静态)
     */
    @RequestMapping("/task")
    public ResponseVO getTask() {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, taskService.list());
    }

    /**
     * 查询兑换记录
     */
    @AuthToken
    @RequestMapping("/conversion")
    public ResponseVO getConversionRecord(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, recordService.getConversionRecord(token));
    }

    /**
     * 获取步数记录
     * @param token
     * @param uid
     *          访问他人时需要
     * @return
     */
    @AuthToken
    @RequestMapping("/step")
    public ResponseVO getStepData(@RequestParam String token, @RequestParam(required = false) String toUid) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, recordService.getRunData(token, toUid));
    }
}

