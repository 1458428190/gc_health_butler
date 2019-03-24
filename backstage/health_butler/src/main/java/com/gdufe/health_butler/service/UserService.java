package com.gdufe.health_butler.service;

import com.gdufe.health_butler.bean.vo.MainVO;
import com.gdufe.health_butler.bean.vo.RankVO;
import com.gdufe.health_butler.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
public interface UserService extends IService<User> {

    /**
     * 获取code2Session对应的token(暂以自身数据库的uid)
     * @param code
     * @return
     */
    String getToken(String code);

    /**
     * 保存或更新用户信息
     */
    void saveOrUpdateUserInfo(String token, String iv, String encryptedData);

    /**
     * 获取用户
     * @param openId
     * @return
     */
    User getByOpenId(String openId);

    /**
     * 获取用户信息及排名
     * @param token
     * @return
     */
    MainVO getMainInfo(String token);

    /**
     * 查询排名
     * @param token
     * @return
     */
    RankVO getRank(String token);

    /**
     * 设置或者取消提醒
     * @param token
     * @param type
     * @param time
     * @return
     */
    void setClockInRemind(String token, int type, String time);

    /**
     * 访问他人主页
     * @param token
     * @param toUid
     * @return
     */
    User seeHomePage(String token, String toUid);

    /**
     * 邀请用户
     * @param token
     * @return
     */
    String invite(String token);

    /**
     * 过滤敏感信息
     */
    User filter(User user);

    /**
     * 打赏
     * @param token
     * @param toUid
     *          打赏的对象
     * @param coin
     * @return
     */
    String reward(String token, long toUid, long coin);

    /**
     * 封面上传
     * @param token
     * @param imgNo
     * @param file
     * @return
     */
    String coverUpload(String token, int imgNo, MultipartFile file);

    /**
     * 清除今日步数
     */
    void cleanNowStep();

    /**
     * 重置封面
     * @param token
     * @return
     */
    boolean coverReset(String token);
}
