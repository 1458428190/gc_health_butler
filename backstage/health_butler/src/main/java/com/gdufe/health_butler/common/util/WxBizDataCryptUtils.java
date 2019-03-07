package com.gdufe.health_butler.common.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: 微信小程序加密数据解密工具
 * @Date: 2019/2/16 11:56
 */
public class WxBizDataCryptUtils {

    private static boolean hasInited = false;

    public static String decrypt(String session_key, String iv, String encryptedData) {
        String decryptString = "";
        init();
        byte[] sessionKeyByte = Base64.decodeBase64(session_key);
        byte[] ivByte = Base64.decodeBase64(iv);
        byte[] encryptDataByte = Base64.decodeBase64(encryptedData);

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key key = new SecretKeySpec(sessionKeyByte, "AES");
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
            algorithmParameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameters);
            byte[] bytes = cipher.doFinal(encryptDataByte);
            decryptString = new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptString;
    }

    public static void init() {
        if (hasInited) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        hasInited = true;
    }

    public static void main(String[] args) {
        String session_key = "S5HhbJGkQLy8LH4Edn/m8Q==";
        String iv = "DjebMFk/YcuABEKnySqP9w==";
        String encryptData = "dqQkzd6Raw9mdhYetz+f+fb2IA44Lw1ZbGBs+bMiqyWM5jjhTX72RN/jcP7NW1pVffxjBHKK7JyAAsrv3fBXr25vMwn9+j+VIuggrIU6orc9VUw88n+hvF6SehYmlEvVrAzh6YQknuZQ4NCuN+wn68mb/m5zyhv1D4LIiUAEQdUZngi5+MrXDKSA+AfnYWNhm/NkkwG/RXt3lYCYOG6rR8YxijmYVoyCj4928yUm7dc5UwnwMWLacPeB/DAnDZLllauu2KipMNqCP5Ia5158D7CQ3KlkIxjyuTZ5uEDQvX9Pq//s1GpZpZPNEFOyEbJDW8ybMfAZyAISF4+qCl0513+PEJKnq9rb0+tBKhi1YaLnvW+VyNfd6WT6AsB6/j5ssL3CmzXGdg/nvDObrTPCQuj5VicgC4RKhc1Ukwqf2eO/LksmSYQDiYgk1W+ojCB5eBH7kDwfHnrnWTP4MA2OiVrrg2n29dtb6nwawHMgw/IipR7FXO370PriWfaKsg0ODZitK8xqUYgQrnJ96n+bipFjI47+DYS4YJuVzwDUoL7BPuMQTCwTASNN9YIfeH2t0TEqfid+BxjdCLOjqaKn+FPuwmntAerua6qcklaPOJj8UryeO8GgJ7MKwXV2uP3IJyduB/YKcLL6w8AXmNJX2z9FIuX52zeMDdxO5qnFwoW56d/wmXZjmmLb5XAjColteaqtWfiqqsrxm51YsBnFO2AVX5u9wsg+MWdWCpTAn92oP57mI8/Qf3av3ywnDsRQdFFtMplUhpjKRALXAAjZDp5EFOxSVbP8/2Ias/1F0dbn1wsQe0+A/pL/shXggVQ4j4KwYnvcDBNs/jrVFD0+vND4yBoEWlcxwO0YjFeLYYB2YEod+yjNlebkiZBUZk4uAJ+gmNFz1aqUwh+jDtNRxBK8tRVe0HSUu0ZMGVHT86QIiovYXz+Q4pGxv7U4dXAUXIW/KELgkNQ8F52e+wlM+UdDO4GisoKownkrgGdJfWHvvuLQwrHD1r6OomOJo1IPwsYsyk31pgr8v9zDb540aEHBN0G6R8JaXiUQEFOqSi+j3qy409u0qOnm/Ng5k0egFKAoyxjdP+pYsCkesICro2wOEypfT478JcMBm2mjC5CO5u43KQsORT4fpYa07tnZ3tE6PW627v65Q3dDYPRuEiyDd7RzUz8i6seqi2kkmW04V/v4pWdJ5+mcL7buCWmn6ou5nLkIqJaxzsvblhSm+/e5aRwClO602vHGcHFbnKsmZMGBaOY02oNOtBvz9fywRLIfP8+B9qSvl5cqOgfz1RWxez57kEEXrdBVrNTjLznh3WItXg47qOoiXn/DI5Yncez5UXc17V5QMzxSLmsHbd94fUCCwQDs88OJzeFzaT0Gfy24ICbEUESo4g1kE7GMhIBaAqKXGitEhotueXzVx+p8S5/D9YtVwumeU9lTzhMXfOFi8tYbcjoL14D8UbY2H1K8GGLQwvNjCXHJFoUDx5z6VMOchaHd96L1DtHQANdzY+x0yjSs6d1OTpWMCV5Jp3lp3AeyAnfet7Gm1+fO4dhQFPkBGeJsxybiVpIfIEvN6bkqJPrUZyqNXJB4xIb1CxCriFoNM3/ew7KbYfS1BTnC6UnbVYPkDSbCjfxCAEE=";
        String decrypt = decrypt(session_key, iv, encryptData);
        System.out.println(decrypt);
    }
}
