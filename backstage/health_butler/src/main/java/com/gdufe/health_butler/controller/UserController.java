package com.gdufe.health_butler.controller;


import com.gdufe.health_butler.aop.AuthToken;
import com.gdufe.health_butler.bean.vo.ResponseVO;
import com.gdufe.health_butler.common.enums.ResponseStatusEnum;
import com.gdufe.health_butler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 保存或更新用户信息
     */
    @AuthToken
    @RequestMapping("/save/userInfo")
    public ResponseVO saveOrUpdateUserInfo(@RequestParam String token, @RequestParam String iv,
                                           @RequestParam String encryptedData) {
        userService.saveOrUpdateUserInfo(token, iv.replaceAll(" ", "+"), encryptedData.replaceAll(" ", "+"));
        return new ResponseVO(ResponseStatusEnum.SUCCESS, "");
    }

    /**
     * 获取code2Session对应的token
     */
    @RequestMapping("/getToken")
    public ResponseVO getToken(@RequestParam String code) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, userService.getToken(code));
    }

    /**
     * 查询主页信息(封面, 提醒, 总步数, 总健康币, 排名, 个人信息)
     */
    @AuthToken
    @RequestMapping("/info")
    public ResponseVO getInfo(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, userService.getMainInfo(token));
    }

    /**
     * 查询排名
     */
    @AuthToken
    @RequestMapping("/rank")
    public ResponseVO getRank(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, userService.getRank(token));
    }

    /**
     * 设置提醒 (或者取消提醒)
     * @param token
     * @param type
     *          提醒类型 (2, 3, 4)
     *          详见 {@link com.gdufe.health_butler.common.enums.RecordType}
     * @param time
     *          时间
     *          当time为空时, 为取消提醒
     * @return
     */
    @AuthToken
    @RequestMapping("/set/clockInRemind")
    public ResponseVO setClockInRemind(@RequestParam String token, @RequestParam int type,
                                       @RequestParam(required = false) String time) {
        userService.setClockInRemind(token, type, time);
        return new ResponseVO(ResponseStatusEnum.SUCCESS, "");
    }

    /**
     * 访问主页
     * @param toUid
     * @return
     */
    @AuthToken
    @RequestMapping("/seeHomePage")
    public ResponseVO seeOther(@RequestParam String token, @RequestParam(required = false) String toUid) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, userService.seeHomePage(token, toUid));
    }

    /**
     * 邀请用户
     * @param token
     * @return
     */
    @AuthToken
    @RequestMapping("/invite")
    public ResponseVO invite(@RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, userService.invite(token));
    }

    /**
     * 打赏
     */
    @AuthToken
    @RequestMapping("/reward")
    public ResponseVO reward(@RequestParam long toUid, @RequestParam long coin, @RequestParam String token) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, userService.reward(token, toUid, coin));
    }

    /**
     * 封面上传
     */
    @AuthToken
    @RequestMapping(value = "/cover/upload", method = RequestMethod.POST)
    public ResponseVO coverUpload(@RequestParam String token, @RequestParam int imgNo,
                                  @RequestParam(value = "file", required = false) MultipartFile file) {
        return new ResponseVO(ResponseStatusEnum.SUCCESS, userService.coverUpload(token, imgNo, file));
    }
}

