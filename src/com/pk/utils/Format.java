package com.pk.utils;

/**
 * "^-?\\d+(\\.\\d+)?$"
 * ^ 匹配字符串的开头
 * -? 匹配一个可选的负号
 * \d+ 匹配一个或多个数字
 * (\.\d+)? 匹配一个可选的小数部分，其中 \. 匹配小数点，\d+ 匹配一个或多个数字
 * $ 匹配字符串的结尾
 * 这个正则表达式可以匹配如下字符串：
 *
 * 3.14
 * -3.14
 * 0.123
 * -0.123
 * 123
 * -123
 */
public class Format {
    public static final String PHONE = "1\\d{9}";
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DOUBLE_STRING = "^-?\\d+(\\.\\d+)?$";

//    Pattern pattern = Pattern.compile("^[-\+]?\d*[.]\d+$");

}
