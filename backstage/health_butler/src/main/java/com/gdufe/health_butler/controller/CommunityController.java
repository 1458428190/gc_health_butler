package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 社区分享表 前端控制器
 * </p>
 *
 * @author Chengfeng
 * @since 2019-02-22
 */
@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    /**
     * 获取社区分享
     */
    @AuthToken
    @RequestMapping("/list")
    public ResponseVO list(@RequestParam String token) {
       return new ResponseVO(ResponseStatusEnum.SUCCESS, communityService.listByToken(token));
    }

    /**
     * 分享发表内容
     */
    @AuthToken
    @RequestMapping("/share")
    public ResponseVO share(@RequestParam String token, @RequestParam String content, @RequestParam boolean onlyMe) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, communityService.share(token, content, onlyMe));
    }

    /**
     * 接收上传的文件
     * @param token
     * @param cid
     * @param file
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseVO upload(@RequestParam String token, @RequestParam long cid, @RequestParam int imgNo,
                         @RequestParam(value = "file", required = false) MultipartFile file) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, communityService.upload(token, cid, file, imgNo));
    }

    /**
     * 删除动态
     * @param token
     * @param cid
     *          动态id
     * @return
     */
    @AuthToken
    @RequestMapping("/delete")
    public ResponseVO delete(@RequestParam String token, @RequestParam long cid) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, communityService.delete(token, cid));
    }

    /**
     * 只获取自己的动态
     * @param token
     * @return
     */
    @AuthToken
    @RequestMapping("/getMe")
    public ResponseVO getMe(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, communityService.getMe(token));
    }
}

