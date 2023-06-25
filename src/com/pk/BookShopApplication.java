package com.pk;

import com.pk.controller.AdminController;
import com.pk.controller.BaseCustomerController;
import com.pk.controller.BookController;
import com.pk.controller.VipController;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.Scanner;

/**
 * 书店系统主界面/程序入口
 */
public class BookShopApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("<<----------欢迎来到小黑书店----------->>");
            System.out.println("请先选择您的身份:");
            System.out.println("# 0.离开书店 1.普通顾客 2.vip用户 3.管理员 ");
            switch (sc.next()) {
                case "0":
                    System.out.println("谢谢惠顾,欢迎下次再来~~");
                    return;
                case "1":
                    new BaseCustomerController().start();
                    break;
                case "2":
                    new VipController().start();
                    break;
                case "3":
                    new AdminController().start();
                    break;
                default:
                    System.out.println("您的选择有误,请重新输入");
            }

        }

    }

}
