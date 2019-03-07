package com.gdufe.health_butler.common.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: laichengfeng
 * @Description: 时间工具
 * @Date: 2019/3/1 15:35
 */
public class TimeUtils {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_2 = new SimpleDateFormat("HH:mm:ss");

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_3 = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取当日的开始时间和结束时间 (毫秒级)
     * @return
     */
    public static List<Long> getTodayTime() {
        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long startTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        long endTime = calendar.getTimeInMillis();
        List<Long> list = new ArrayList<>();
        list.add(startTime);
        list.add(endTime);
        return list;
    }

    /**
     * 格式化时间戳 为 yyyy-MM-dd hh:mm:ss
     * @param time
     * @return
     */
    public static String formatTimeFull(long time) {
        return SIMPLE_DATE_FORMAT_1.format(new Date(time));
    }

    /**
     * 格式化时间戳 为 hh:mm:ss
     * @param time
     * @return
     */
    public static String formatTimeDay(long time) {
        return SIMPLE_DATE_FORMAT_2.format(new Date(time));
    }

    /**
     * 格式化时间戳 为 yyyy-MM-dd
     * @param time
     * @return
     */
    public static String formatTimeDate(long time) {
        return SIMPLE_DATE_FORMAT_3.format(new Date(time));
    }


}
