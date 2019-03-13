package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-13
 */
@RestController
@RequestMapping("/operation")
public class OperationController {

    @Autowired
    private OperationService operationService;

    /**
     * 操作list
     * @return
     */
    @RequestMapping("/list")
    public ResponseVO list() {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, operationService.list());
    }
}

