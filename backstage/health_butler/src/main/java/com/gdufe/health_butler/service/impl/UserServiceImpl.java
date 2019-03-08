package com.gdufe.health_butler.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.health_butler.bean.dto.wx.Code2Session;
import com.gdufe.health_butler.bean.vo.MainVO;
import com.gdufe.health_butler.bean.vo.RankVO;
import com.gdufe.health_butler.common.enums.DealType;
import com.gdufe.health_butler.common.enums.RecordType;
import com.gdufe.health_butler.common.exception.ParamErrorException;
import com.gdufe.health_butler.common.exception.SystemErrorException;
import com.gdufe.health_butler.common.util.HttpUtils;
import com.gdufe.health_butler.common.util.MinioUtils;
import com.gdufe.health_butler.common.util.TimeUtils;
import com.gdufe.health_butler.common.util.WxBizDataCryptUtils;
import com.gdufe.health_butler.entity.CoinDetail;
import com.gdufe.health_butler.entity.Community;
import com.gdufe.health_butler.entity.Record;
import com.gdufe.health_butler.entity.User;
import com.gdufe.health_butler.dao.UserMapper;
import com.gdufe.health_butler.manager.TokenContainer;
import com.gdufe.health_butler.service.CoinDetailService;
import com.gdufe.health_butler.service.RecordService;
import com.gdufe.health_butler.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.MinioClient;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author laichengfeng
 * @since 2019-03-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Value("${appId}")
    private String appId;

    @Value("${appSecret}")
    private String secret;

    @Value("${minio.imgBucket}")
    private String imgBucket;

    @Value("${img.separator}")
    private String imgSeparator;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private RecordService recordService;

    @Autowired
    private CoinDetailService coinDetailService;

    @Override
    public String getToken(String code) {
        logger.info("[op:getToken, code:{}]", code);
        Code2Session code2Session = new Code2Session();
        code2Session.setGetTime(System.currentTimeMillis());
        String url = String.format(URL, appId, secret, code);
        try {
            String content = HttpUtils.get(url, null);
            logger.info("[content: {}]", content);
            Map<String, Object> code2SessionMap = (Map<String, Object>) JSON.parse(content);
            String openId = code2SessionMap.get("openid") + "";
            code2Session.setOpenId(openId);
            code2Session.setSessionKey(code2SessionMap.get("session_key") + "");
            code2Session.setExpiresIn((Integer) code2SessionMap.get("expires_in") * 1000);
            User user = getByOpenId(openId);
            if(user == null) {
                user = new User();
                user.setOpenId(openId);
                user.setCreateTime(System.currentTimeMillis());
                user.setModifiedTime(System.currentTimeMillis());
                user.setReward(0L);
                save(user);
            }
            // 加入容器管理中
            String token = TokenContainer.add(code2Session);
            logger.info("[op_rslt: success, token: {}]", token);
            return token;
        } catch (Exception e) {
            throw new SystemErrorException("请求: " + url + " 失败, 或者code不正确");
        }
    }

    @Override
    public void saveOrUpdateUserInfo(String token, String iv, String encryptedData) {
        User user = getByOpenId(TokenContainer.get(token).getOpenId());
        logger.info("[op:saveOrUpdateUserInfo, uid:{}, token:{}, iv:{}, encryptedData:{}]", user.getId(), token, iv, encryptedData);
        try {
            String sessionKey = TokenContainer.get(token).getSessionKey();
            String userInfoContent = WxBizDataCryptUtils.decrypt(sessionKey, iv, encryptedData);
            Map<String, Object> userInfoMap = (Map<String, Object>) JSON.parse(userInfoContent);
            user.setOpenId(userInfoMap.get("openId") + "");
            user.setNickName(userInfoMap.get("nickName") + "");
            user.setGender((Integer) userInfoMap.get("gender"));
            user.setCity(userInfoMap.get("city") + "");
            user.setProvince(userInfoMap.get("province") + "");
            user.setCountry(userInfoMap.get("country") + "");
            user.setAvatarUrl(userInfoMap.get("avatarUrl") + "");
            user.setModifiedTime(System.currentTimeMillis());
            updateById(user);
            logger.info("[op_rslt:success]");
        } catch (Exception e) {
            throw new ParamErrorException("数据有误");
        }
    }

    @Override
    public User getByOpenId(String openId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(User::getOpenId, openId);
        return getOne(userQueryWrapper);
    }

    @Override
    public MainVO getMainInfo(String token) {
        User user = getByOpenId(TokenContainer.get(token).getOpenId());
        logger.info("[op:getMainInfo, uid:{}, token:{}]", user.getId(), token);
        MainVO mainVO = new MainVO();
        mainVO.setUser(filter(user));

        // 查询排名
        QueryWrapper<User> userStepQueryWrapper = new QueryWrapper<>();
        userStepQueryWrapper.lambda().orderByDesc(User::getNowStep);
        List<User> listStep = list(userStepQueryWrapper);
        int rankStep = 1;
        for(int i=0; i<listStep.size(); i++) {
            if(user.getId() == listStep.get(i).getId()) {
                rankStep = i + 1;
                break;
            }
        }
        mainVO.setRankStep(rankStep);

        QueryWrapper<User> userCoinQueryWrapper = new QueryWrapper<>();
        userCoinQueryWrapper.lambda().orderByDesc(User::getHealthCoin);
        List<User> listCoin = list(userCoinQueryWrapper);
        int rankCoin = 1;
        for(int i=0; i<listCoin.size(); i++) {
            if(user.getId() == listCoin.get(i).getId()) {
                rankCoin = i + 1;
                break;
            }
        }
        mainVO.setRankCoin(rankCoin);

        List<Long> todayTime = TimeUtils.getTodayTime();
        long startTime = todayTime.get(0);
        long endTime = todayTime.get(1);

        // 早睡早起 打卡的排名
        int rankMorning = 1;
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.MORNING_CLOCK_IN.getValue())
                .between(Record::getCreateTime, startTime, endTime);
        List<Record> listRecordMorning = recordService.list(recordQueryWrapper);
        for(int i=0; i<listRecordMorning.size(); i++) {
            if(user.getId() == listRecordMorning.get(i).getUid()) {
                rankMorning = i + 1;
                break;
            }
        }
        mainVO.setRankMorning(rankMorning);

        int rankNight = 1;
        recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.GOOD_NIGHT_CLOCK_IN.getValue())
                .between(Record::getCreateTime, startTime, endTime);
        List<Record> listRecordNight = recordService.list(recordQueryWrapper);
        for(int i=0; i<listRecordNight.size(); i++) {
            if(user.getId() == listRecordMorning.get(i).getUid()) {
                rankNight = i + 1;
                break;
            }
        }
        mainVO.setRankNight(rankNight);

        recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.CONVERSION.getValue())
                .eq(Record::getUid, user.getId());
        mainVO.setRecordCount(recordService.count(recordQueryWrapper));
        logger.info("[op_rslt:success, mainVO:{}]", JSON.toJSON(mainVO));
        return mainVO;
    }

    @Override
    public RankVO getRank(String token) {
        User user = getByOpenId(TokenContainer.get(token).getOpenId());
        logger.info("[op:getRank, uid:{}, token:{}]", user.getId(), token);

        user = filter(user);
        // 4大冠军
        List<User> championList = new ArrayList<>();
        // 今日步数冠军
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().select(User::getId, User::getProvince, User::getCity, User::getNickName,
                User::getAvatarUrl, User::getNowStep)
                .orderByDesc(User::getNowStep);
        List<User> stepRank = list(userQueryWrapper);
        if(stepRank.size() > 0) {
            championList.add(stepRank.get(0));
        } else {
            championList.add(null);
        }

        // 健康财富冠军
        userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().select(User::getId, User::getProvince, User::getCity, User::getNickName,
                User::getAvatarUrl, User::getHealthCoin)
                .orderByDesc(User::getHealthCoin);
        List<User> coinRank = list(userQueryWrapper);
        if(coinRank.size() > 0) {
            championList.add(coinRank.get(0));
        } else {
            championList.add(null);
        }
        // 今日早起
        List<Long> todayTime = TimeUtils.getTodayTime();
        long startTime = todayTime.get(0);
        long endTime = todayTime.get(1);
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.MORNING_CLOCK_IN.getValue())
                .between(Record::getCreateTime, startTime, endTime);
        List<Record> morningRank = recordService.list(recordQueryWrapper);
        if(morningRank.size() > 0) {
            championList.add(filter(getById(morningRank.get(0).getUid())));
        } else {
            championList.add(null);
        }
        // 昨日早睡
        long yesterdayStartTime = startTime - 24 * 3600000;
        long yesterdayEndTime = endTime - 24 * 3600000;
        recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.lambda().eq(Record::getType, RecordType.GOOD_NIGHT_CLOCK_IN)
                .between(Record::getCreateTime, yesterdayStartTime, yesterdayEndTime);
        List<Record> nightRank = recordService.list(recordQueryWrapper);
        if(nightRank.size() > 0) {
            championList.add(filter(getById(nightRank.get(0).getUid())));
        } else {
            championList.add(null);
        }

        // 登录用户的4大排名 (可能存在用户没有数据, 便为null)
        List<Integer> userRank = new ArrayList<>();
        long uid = user.getId();
        userRank.add(getUserRank(uid, stepRank, null));
        userRank.add(getUserRank(uid, coinRank, null));
        userRank.add(getUserRank(uid, null, morningRank));
        userRank.add(getUserRank(uid, null, nightRank));

        // 登录用户的4大值
        List<String> userData = new ArrayList<>();
        userData.add(user.getNowStep() + "");
        userData.add(user.getHealthCoin() + "");
        if(userRank.get(2) != null) {
            userData.add(TimeUtils.formatTimeDay(morningRank.get(userRank.get(2)-1).getCreateTime()).substring(0, 5));
        } else {
            userData.add(null);
        }
        if(userRank.get(3) != null) {
            userData.add(TimeUtils.formatTimeDay(nightRank.get(userRank.get(3)-1).getCreateTime()).substring(0, 5));
        } else {
            userData.add(null);
        }

        // 4大排名数据
        List<List<Map<String, Object>>> rankData = new ArrayList<>();
        List<Map<String, Object>> stepRankMapList = new ArrayList<>();
        stepRank.forEach(stepUser-> {
            Map<String, Object> map = new HashMap<>();
            map.put("user", stepUser);
            map.put("value", stepUser.getNowStep() + "步");
            stepRankMapList.add(map);
        });
        List<Map<String, Object>> coinRankMapList = new ArrayList<>();
        coinRank.forEach(coinUser-> {
            Map<String, Object> map = new HashMap<>();
            map.put("user", coinUser);
            map.put("value", coinUser.getHealthCoin() + "币");
            coinRankMapList.add(map);
        });
        List<Map<String, Object>> morningRankMapList = getRestRank(morningRank);
        List<Map<String, Object>> nightRankMapList = getRestRank(nightRank);
        rankData.add(stepRankMapList);
        rankData.add(coinRankMapList);
        rankData.add(morningRankMapList);
        rankData.add(nightRankMapList);

        RankVO rankVO = new RankVO();
        rankVO.setUser(user);
        rankVO.setChampionList(championList);
        rankVO.setRankData(rankData);
        rankVO.setUserData(userData);
        rankVO.setUserRank(userRank);
        logger.info("[op_rslt:success, rankVO:{}]", JSON.toJSON(rankVO));
        return rankVO;
    }

    @Override
    public void setClockInRemind(String token, int type, String time) {
        User user = getByOpenId(TokenContainer.get(token).getOpenId());
        logger.info("[op:setClockInRemind, uid:{}, token:{}, openId:{}, type:{}, time:{}]", user.getId(), token, type, time);
        if(StringUtils.isNotBlank(time)) {
            boolean timeFormat = false;
            // 时间格式不正确
            try {
                if (time.contains(":") && time.length() == 5) {
                    int hour = Integer.parseInt(time.split(":")[0]);
                    int minute = Integer.parseInt(time.split(":")[1]);
                    if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
                        timeFormat = true;
                    }
                }
            } catch (Exception e) {
                throw new ParamErrorException("time不正确");
            }

            if(!timeFormat) {
                throw new ParamErrorException("time不正确");
            }
        }
        switch (RecordType.of(type)) {
            case MORNING_CLOCK_IN:
                if(StringUtils.isNotBlank(time)) {
                    user.setMorningTime(time + "-remind");
                } else {
                    user.setMorningTime(user.getMorningTime().replace("-remind", "-cancel"));
                }
                break;
            case SPORT_CLOCK_IN:
                if(StringUtils.isNotBlank(time)) {
                    user.setSportTime(time + "-remind");
                } else {
                    user.setSportTime(user.getSportTime().replace("-remind", "-cancel"));
                }
                break;
            case GOOD_NIGHT_CLOCK_IN:
                if(StringUtils.isNotBlank(time)) {
                    user.setNightTime(time + "-remind");
                } else {
                    user.setNightTime(user.getNightTime().replace("-remind", "-cancel"));
                }
                break;
            default:
                throw new ParamErrorException("提醒类型有误");
        }
        user.setModifiedTime(System.currentTimeMillis());
        updateById(user);
    }

    @Override
    public User seeHomePage(String token, String toUid) {
        User user = getByOpenId(TokenContainer.get(token).getOpenId());
        logger.info("[op:seeOther, uid:{}, token:{}, toUid:{}]", user.getId(), token, toUid);
        if(null != toUid && !StringUtils.equals(toUid+"", "null")) {
            user = getById(Long.parseLong(toUid+""));
        }
        user = filter(user);
        logger.info("[op_rslt:success, toUser:{}]", JSON.toJSON(user));
        return user;
    }


    /**
     * 打赏
     * @param token
     * @param toUid
     *          打赏的对象
     * @param coin
     * @return
     */
    @Override
    public String reward(String token, long toUid, long coin) {
        User user = getByOpenId(TokenContainer.get(token).getOpenId());
        logger.info("[op:reward, uid:{}, token:{}, toUid:{}, coin:{}]", user.getId(), token, toUid, coin);

        if(coin <= 0) {
            throw new ParamErrorException("打赏额度有误");
        }
        if(user.getHealthCoin() < coin) {
            throw new ParamErrorException("健康币不足，打赏失败！");
        }
        User toUser = getById(toUid);
        if( null == toUser) {
            throw new ParamErrorException("用户不存在，打赏失败！");
        }
        try {
            rewardDetail(user, toUser, coin);
            logger.info("[op_rslt: success]");
            return "打赏成功";
        } catch (Exception e) {
            logger.error("[op_rslt: error]", e);
            throw new SystemErrorException("系统错误，打赏失败！");
        }
    }

    @Override
    public String coverUpload(String token, int imgNo, MultipartFile file) {
        User user = getByOpenId(TokenContainer.get(token).getOpenId());
        long uid = user.getId();
        String originalFileName = file.getOriginalFilename();
        logger.info("[op:coverUpload, uid:{}, token:{}, imgNo:{}, file:{}]", uid, token, imgNo, file.getOriginalFilename());

        if(file.isEmpty()) {
            throw new ParamErrorException("文件有误");
        }

        try {
            String newImgUrl = MinioUtils.FileUploaderByStream(minioClient, imgBucket, file.getInputStream(), originalFileName);
            updateCoverImgUrlList(uid, newImgUrl, imgNo);
        } catch (Exception e) {
            throw new SystemErrorException("内部系统出错");
        }
        return "上传成功";
    }

    public void updateCoverImgUrlList(long uid, String newImgUrl, int imgNo) {
        logger.info("[op:updateCoverImgUrlList, uid: {}, newImgUrl:{}, imgNo:{}]", uid, newImgUrl, imgNo);
        // 第一张直接加
        if(imgNo == 1) {
            User user = getById(uid);
            user.setCoverImgUrl(newImgUrl);
            user.setModifiedTime(System.currentTimeMillis());
            updateById(user);
            return;
        }
        boolean lock = true;
        while(lock) {
            try {
                User user = getById(uid);
                String coverImgUrl = user.getCoverImgUrl();
                List<String> newImgUrlList = new ArrayList<>();
                if (StringUtils.isNotBlank(coverImgUrl)) {
                    newImgUrlList = new ArrayList<>(Arrays.asList(coverImgUrl.split(imgSeparator)));
                }
                if (newImgUrlList.size() >= 9) {
                    lock = false;
                    throw new ParamErrorException("您的封面图片数量已经超了");
                }
                if(newImgUrlList.size() == imgNo - 1) {
                    newImgUrlList.add(newImgUrl);
                    user.setCoverImgUrl(String.join(imgSeparator, newImgUrlList));
                    user.setModifiedTime(System.currentTimeMillis());
                    updateById(user);
                    lock = false;
                } else {
                    // 随机休息
                    Thread.sleep(100 + (long) (Math.random() * 200));
                }
            } catch (Exception e) {
                logger.warn("[op_rslt: lock conflict]");
            }
        }
    }

    @Override
    public String invite(String token) {
        User user = getByOpenId(TokenContainer.get(token).getOpenId());
        logger.info("[op:invite, uid:{}, token:{}]", user.getId(), token);

        // TODO 邀请用户

        return null;
    }

    @Transactional
    public void rewardDetail(User user, User toUser, long coin) {
        long nowTime = System.currentTimeMillis();
        user.setHealthCoin(user.getHealthCoin() - coin);
        user.setModifiedTime(nowTime);
        toUser.setHealthCoin(toUser.getHealthCoin() + coin);
        toUser.setModifiedTime(nowTime);
        updateById(user);
        updateById(toUser);

        CoinDetail userCoinDetail = new CoinDetail();
        userCoinDetail.setUid(user.getId());
        userCoinDetail.setType(DealType.REWARD.getValue());
        userCoinDetail.setModifiedTime(nowTime);
        userCoinDetail.setCreateTime(nowTime);
        userCoinDetail.setDescription("打赏健康币给" + toUser.getNickName());
        userCoinDetail.setCoin(0 - coin);
        userCoinDetail.setToUid(toUser.getId());

        CoinDetail toUserCoinDetail = new CoinDetail();
        toUserCoinDetail.setUid(toUser.getId());
        toUserCoinDetail.setType(DealType.REWARD.getValue());
        toUserCoinDetail.setModifiedTime(nowTime);
        toUserCoinDetail.setCreateTime(nowTime);
        toUserCoinDetail.setDescription(user.getNickName() + "打赏健康币给您");
        toUserCoinDetail.setCoin(coin);
        toUserCoinDetail.setToUid(user.getId());
        coinDetailService.save(userCoinDetail);
        coinDetailService.save(toUserCoinDetail);
    }

    /**
     * 获取作息排名 (早睡 早起)
     * @param list
     * @return
     */
    private List<Map<String, Object>> getRestRank(List<Record> list) {
        List<Map<String, Object>> resList = new ArrayList<>();
        list.forEach(restUser-> {
            Map<String, Object> map = new HashMap<>();
            map.put("user", filter(getById(restUser.getUid())));
            map.put("value", TimeUtils.formatTimeDay(restUser.getCreateTime()));
            resList.add(map);
        });
        return resList;
    }

    /**
     * 过滤敏感数据
     * @param user
     * @return
     */
    @Override
    public User filter(User user) {
        user.setOpenId(null);
        user.setMorningTime(null);
        user.setNightTime(null);
        user.setSportTime(null);
        return user;
    }

    /**
     * 计算用户排名
     * @throws HttpException
     */
    private Integer getUserRank(long uid, List<User> userList, List<Record> recordList) {
        if(null != userList) {
            for(int i=0; i<userList.size(); i++) {
                if(uid == userList.get(i).getId()) {
                    return i + 1;
                }
            }
            return null;
        }
        for(int i=0; i<recordList.size(); i++) {
            if(uid == recordList.get(i).getUid()) {
                return i + 1;
            }
        }
        return null;
    }

    public static void main(String[] args) throws HttpException {
        String url = String.format(URL, "wxbf2baa40c56ce7a1", "f73d96e17ad26231c69fcd5bdc39283d", "081S18Ix0IOKGc1mQ3Kx0n1ZHx0S18Ix");
        String s = HttpUtils.get(url, null);
        System.out.println(s);
    }
}
