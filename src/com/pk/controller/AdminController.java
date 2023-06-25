package com.pk.controller;

import com.pk.utils.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

/**
 * 管理员菜单
 */
public class AdminController {
    private boolean flag = false;  //标记管理员是否登录
    private String username = null;
    private final Scanner sc = new Scanner(System.in);
    public static final Logger LOGGER = LoggerFactory.getLogger("AdminController");

    public void start() {
        if (!flag) {
            System.out.println("请先登录您的管理员账号,才可以进行操作");
            if (adminLogin()) {
                flag = true;
                LOGGER.info("管理员:" + username + "登陆了");
                System.out.println("欢迎您,管理员" + username);
            } else {
                System.out.println("登录失败或您取消登录,即将返回主菜单");
                return;
            }
        }


        while (true) {
            System.out.println("<-----书店后台管理----->");
            System.out.println("您可以进行下列操作:");
            System.out.println("# 0.离开 1.管理书籍 2.查看vip信息 3.查看借阅信息");
            switch (sc.next()) {
                case "0":
                    System.out.println("即将离开后台管理,返回主界面");
                    flag = false;
                    LOGGER.info("管理员:" + username + "注销并离开了");
                    username = null;
                    return;
                case "1":
                    LOGGER.info("管理员:" + username + "进入了书籍管理系统");
                    new BookController().start(username);
                    break;
                case "2":
                    //todo:查看vip信息
                    break;
                case "3":
                    //todo:查看借阅信息
                default:
                    System.out.println("您的选择有误,请重新输入");
            }
        }
    }

    private boolean adminLogin() {
        for (int i = 2; i >= 0; i--) {
            System.out.println("请输入您的管理员用户名(输入'q'退出登录):");
            String username = sc.next();
            if (username.equals("q")) return false;
            try {
                Properties properties = new Properties();
                properties.load(new FileReader(Path.ADMIN));
                Set<String> usernames = properties.stringPropertyNames();
                if (usernames.contains(username)) {
                    System.out.println("请输入密码:");
                    String password = sc.next();
                    if (properties.getProperty(username).equals(password)) {
                        this.username = username;
                        return true;
                    } else {
                        System.out.println("密码不正确,请检查后重试,您还有" + i + "次机会");
                    }
                } else {
                    System.out.println("用户名不存在,请检查后重试,您还有" + i + "次机会");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
