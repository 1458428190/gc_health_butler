package com.gdufe.health_butler.bean.vo;

import com.alibaba.druid.util.StringUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: laichengfeng
 * @Description: ai测肤VO
 * @Date: 2019/3/9 19:01
 */
@Data
public class AiSkinVO {

    private final static Map<String, String> eyesStatusDes = new HashMap<>();

    private final static Map<String, String> emotionDes = new HashMap<>();

    private final static Map<String, String> skinStatusDes = new HashMap<>();

    private final static Map<String, String> ethniCityDes = new HashMap<>();

    static {
        eyesStatusDes.put(" ", "无");
        eyesStatusDes.put("occlusion", "眼睛被遮挡");
        eyesStatusDes.put("no_glass_eye_open", "不戴眼镜且睁眼");
        eyesStatusDes.put("normal_glass_eye_close", "佩戴普通眼镜且闭眼");
        eyesStatusDes.put("normal_glass_eye_open", "佩戴普通眼镜且睁眼");
        eyesStatusDes.put("dark_glasses", "佩戴墨镜");
        eyesStatusDes.put("no_glass_eye_close", "不戴眼镜且闭眼");

        emotionDes.put(" ", "无");
        emotionDes.put("anger", "愤怒");
        emotionDes.put("disgust", "厌恶");
        emotionDes.put("fear", "恐惧");
        emotionDes.put("happiness", "高兴");
        emotionDes.put("neutral", "平静");
        emotionDes.put("sadness", "伤心");
        emotionDes.put("surprise", "惊讶");

        skinStatusDes.put(" ", "无");
        skinStatusDes.put("health", "健康");
        skinStatusDes.put("stain", "色斑");
        skinStatusDes.put("acne", "青春痘");
        skinStatusDes.put("dark_circle", "黑眼圈");

        ethniCityDes.put(" ", "无");
        ethniCityDes.put("ASIAN", "亚洲人");
        ethniCityDes.put("WHITE", "白人");
        ethniCityDes.put("BLACK", "黑人");
    }

    /**
     * 照片
     */
    private String imgUrl;

    /**
     * 表情
     */
    private String emotion;

    /**
     * 颜值评分 -女性认为
     */
    private double beautyF;

    /**
     * 颜值评分 -男性认为
     */
    private double beautyM;

    private String gender;

    private int age;

    private String leftEyeStatus;

    private String rightEyeStatus;

    private String skinStatus;

    private String smile;

    private double faceQuality;

    private String ethniCity;

    private FaceRectangle faceRectangle;

    private String message;

    public AiSkinVO() {

    }

    /**
     * 将face转为AiSkinVO
     * @param face
     */
    public AiSkinVO (Map<String, Object> face) {
        Map<String, Object> attributes = (Map<String, Object>) face.get("attributes");
        String genderOrigin = ((Map<String,Object>)attributes.get("gender")).get("value") + "";
        int ageOrigin = (int) ((Map<String, Object>)(attributes.get("age"))).get("value");
        Map<String, Object> smileOrigin = (Map<String, Object>) (attributes.get("smile"));
        Map<String, Object> eyeStatusOrigin = (Map<String, Object>) attributes.get("eyestatus");
        Map<String, Double> emotionOrigin = (Map<String, Double>) attributes.get("emotion");
        Map<String, Object> faceQualityOrigin = (Map<String, Object>) attributes.get("facequality");
        String ethniCityOrigin = ((Map<String,Object>)attributes.get("ethnicity")).get("value") + "";
        Map<String, Double> beautyOrigin = (Map<String, Double>) attributes.get("beauty");
        Map<String, Double> skinStatusOrigin = (Map<String, Double>) attributes.get("skinstatus");

        setGender(StringUtils.equals(genderOrigin, "Male")?"男性":"女性");
        setAge(ageOrigin);
        if(Double.parseDouble(smileOrigin.get("value") + "") >= Double.parseDouble(smileOrigin.get("threshold") + "")) {
            setSmile("微笑");
        }
        if(Double.parseDouble(smileOrigin.get("value") + "") >= 90) {
            setSmile("大笑");
        }

        Map<String, Double> leftEyeStatusOrigin = (Map<String, Double>) eyeStatusOrigin.get("left_eye_status");
        Map<String, Double> rightEyeStatusOrigin = (Map<String, Double>) eyeStatusOrigin.get("right_eye_status");
        double leftEyeStatusMax = 0;
        String leftEyeKey = " ";
        double rightEyeStatusMax = 0;
        String rightEyeKey = " ";
        for(Map.Entry<String, Double> entry: leftEyeStatusOrigin.entrySet()) {
            if(Double.parseDouble(entry.getValue()+"") > leftEyeStatusMax) {
                leftEyeKey = entry.getKey();
                leftEyeStatusMax = Double.parseDouble(entry.getValue()+"");
            }
        }
        for(Map.Entry<String, Double> entry: rightEyeStatusOrigin.entrySet()) {
            if(Double.parseDouble(entry.getValue()+"") > rightEyeStatusMax) {
                rightEyeKey = entry.getKey();
                rightEyeStatusMax = Double.parseDouble(entry.getValue()+"");
            }
        }
        setLeftEyeStatus(eyesStatusDes.get(leftEyeKey));
        setRightEyeStatus(eyesStatusDes.get(rightEyeKey));

        String emotionKey = " ";
        double emotionStatusMax = 0;
        for(Map.Entry<String, Double> entry: emotionOrigin.entrySet()) {
            if(Double.parseDouble(entry.getValue()+"") > emotionStatusMax) {
                emotionKey = entry.getKey();
                emotionStatusMax = Double.parseDouble(entry.getValue()+"");
            }
        }
        setEmotion(emotionDes.get(emotionKey));
        setFaceQuality(Double.parseDouble(faceQualityOrigin.get("value")+""));
        setEthniCity(ethniCityDes.get(ethniCityOrigin));
        setBeautyM(Double.parseDouble(beautyOrigin.get("male_score")+""));
        setBeautyF(Double.parseDouble(beautyOrigin.get("female_score")+""));

        String skinStatusKey = " ";
        double skinStatusMax = 0;
        for(Map.Entry<String, Double> entry: skinStatusOrigin.entrySet()) {
            if(Double.parseDouble(entry.getValue()+"") > skinStatusMax) {
                skinStatusKey = entry.getKey();
                skinStatusMax = Double.parseDouble(entry.getValue()+"");
            }
        }
        setSkinStatus(skinStatusDes.get(skinStatusKey));
    }

}
