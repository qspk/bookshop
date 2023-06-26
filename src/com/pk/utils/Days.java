package com.pk.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Days {

    //计算两个时间差的天数,未满一天按一天算
    public static int getDayNum(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end); // 计算时间差
        long days = duration.toDays(); // 获取相差天数
        if (duration.toHours() % 24 > 0) {
            days++; // 如果未满一天，按一天算
        }
       /* System.out.println(days);
        System.out.println((int)days);*/
        return (int) days;
    }
}
