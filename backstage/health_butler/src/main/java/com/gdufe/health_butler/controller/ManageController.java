package com.gdufe.health_butler.controller;

import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: laichengfeng
 * @Description: 管理控制器
 * @Date: 2019/2/26 14:05
 */
@RestController
@RequestMapping("/manager")
public class ManageController {

    @Autowired
    private ManagerService managerService;

    @RequestMapping("/give")
    public ResponseVO give(@RequestParam long uid, @RequestParam long coin) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, managerService.give(uid, coin));
    }
}
