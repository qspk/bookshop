package com.pk.utils;

/**
 * "^-?\\d+(\\.\\d+)?$"
 * ^ 匹配字符串的开头
 * -? 匹配一个可选的负号
 * \d+ 匹配一个或多个数字
 * (\.\d+)? 匹配一个可选的小数部分，其中 \. 匹配小数点，\d+ 匹配一个或多个数字
 * $ 匹配字符串的结尾
 * 这个正则表达式可以匹配如下字符串：
 * <p>
 * 3.14
 * -3.14
 * 0.123
 * -0.123
 * 123
 * -123
 */
public class Format {
    //手机号格式
//    public static final String PHONE = "1\\d{10}";
//    public static final String PHONE = "^1[3-9]\\d{9}$";
    public static final String PHONE = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$";
/*手机号示例
13956781234
14712345678
15887654321
17698765432
18823456789
13676543210
15098765432
17087654321
18634567890
19987654321
 */

    //日期格式
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    //判断一个字符串是不是浮点数
    public static final String DOUBLE_STRING = "^-?\\d+(\\.\\d+)?$";

//    Pattern pattern = Pattern.compile("^[-\+]?\d*[.]\d+$");

}
