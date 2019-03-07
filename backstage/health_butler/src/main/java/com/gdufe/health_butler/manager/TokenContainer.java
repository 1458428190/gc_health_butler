package com.gdufe.health_butler.manager;

import com.gdufe.health_butler.bean.dto.wx.Code2Session;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: Token容器
 * @Date: 2019/2/26 21:41
 */
public class TokenContainer {

    private static String SECRET = "laichengfeng";

    /**
     * 有效期为3小时
     */
    private static int expiresIn = 3 * 3600000;

    private static Map<String, Code2Session> tokenMap = new HashMap<>();

    private static Map<String, String> openidMap = new HashMap<>();

    public static String add(Code2Session code2Session) {
        String token = DigestUtils.md5Hex(code2Session.getOpenId()+
                        code2Session.getSessionKey() + System.currentTimeMillis());
        code2Session.setExpiresIn(expiresIn);
        // 移除旧token
        String oldToken = openidMap.get(code2Session.getOpenId());
        if(oldToken!=null) {
            tokenMap.remove(oldToken);
        }
        tokenMap.put(token, code2Session);
        openidMap.put(code2Session.getOpenId(), token);
        return token;
//        String token = JWT.create().withAudience(code2Session.getOpenId())
//                .sign(Algorithm.HMAC256(SECRET));
//        return token;
    }

    public static Code2Session get(String token) {
        return tokenMap.get(token);
    }

    /**
     * 去除过期的
     * @param token
     */
    public static void remove(String token) {
        tokenMap.remove(token);
    }

    /**
     * 清理
     */
    public static void clean() {
        for(String token: tokenMap.keySet()) {
            Code2Session code2Session = tokenMap.get(token);
            if (code2Session.getGetTime() + code2Session.getExpiresIn() <= System.currentTimeMillis()) {
                TokenContainer.remove(token);
            }
        }
    }
}
