package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.CommunityRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 社区点赞打赏记录表 前端控制器
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-04
 */
@RestController
@RequestMapping("/communityRecord")
public class CommunityRecordController {

    @Autowired
    private CommunityRecordService communityRecordService;

    /**
     * 点赞或者取消赞
     * @param token
     * @param cid
     * @param type
     *          0-取消点赞
     *          1-点赞
     * @return
     */
    @AuthToken
    @RequestMapping("/praise")
    public ResponseVO praise(@RequestParam String token, @RequestParam long cid, @RequestParam int type) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, communityRecordService.praise(token, cid, type));
    }
}

