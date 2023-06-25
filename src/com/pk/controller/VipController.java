package com.pk.controller;

import com.pk.domain.Book;
import com.pk.domain.Vip;
import com.pk.service.BookService;
import com.pk.service.VipService;
import com.pk.service.impl.BookServiceImpl;
import com.pk.service.impl.VipServiceImpl;
import com.pk.utils.CheckCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class VipController {
    private Vip vip = new Vip();
    public static final Logger LOGGER = LoggerFactory.getLogger("VipController");
    private final Scanner sc = new Scanner(System.in);
    private final VipService vipService = new VipServiceImpl();
    private final BookService bookService = new BookServiceImpl();

    public void start() {
        if (!checkLogin()) {
            return;
        }
        System.out.println("尊贵的vip客户,您可以进行下列操作:");
        while (true) {
            System.out.println("# 0.离开 1.浏览书籍 2.购买书籍 3.余额充值 4.积分查询与兑换 5.借书 6.还书");
            switch (sc.next()) {
                case "0":
                    System.out.println("欢迎您的下次光临~~");
                    LOGGER.info(vip.getShowName() + "离开了");
                    vip = null;
                    return;
                case "1":
                    bookService.findAllBooks();
                    break;
                case "2":
                    vipBuyBooks();
                    break;
                case "3":
//                    chargeBalance();
                    break;


                default:
                    System.out.println("您的选择有误,请重新输入");

            }
        }
    }

    private void vipBuyBooks() {
        ArrayList<Book> books = bookService.findAllBooks();
        ArrayList<Book> buyBooks = new ArrayList<>();
        System.out.println("您可以根据书码选购书籍");
        while (true) {
            System.out.println("请输入您要购买的书籍书码");
            String bookId = sc.next();
            int index;
            if ((index = bookService.getIndex(bookId)) != -1) {
                if (books.get(index).getNumber() <= 0) {
                    System.out.println("当前书籍余量不足,您可以选购其他书籍");
                } else {
                    System.out.println("您选购的书籍信息如下:");
                    System.out.println(books.get(index).toString());
                    buyBooks.add(books.get(index));
                    books.get(index).setNumber(books.get(index).getNumber() - 1);
                    System.out.println("是否继续选购('y'继续)");
                    if (!sc.next().equals("y")) {
                        vipPay(buyBooks);
                    }
                }
            } else System.out.println("未找到书籍,请检查书码是否正确");
        }
    }


    public boolean checkLogin() {
        System.out.println("您需要先登录vip账号才可以进入");
        if (vipLogin()) {
            LOGGER.info(vip.getShowName() + "登录");
            System.out.println(vip.getShowName() + "欢迎回来,账户余额:" + vip.getBalance() + "元");
            return true;
        } else {
            System.out.println("登录失败或者取消登录");
            return false;
        }
    }

    private boolean vipLogin() {
        while (true) {
            System.out.println("请输入您的vip账户/手机号(输入'q'退出):");
            String phone = sc.next();
            if (!phone.equals("q")) {
                if (vipService.isExist(phone)) {
                    Vip vip = vipService.getVip(phone);
                    while (true) {
                        System.out.println("请选择登录方式 # 1.验证码 2.密码登录 q.退出登录");
                        switch (sc.next()) {
                            case "q":
                                return false;
                            case "1":
                                String code = CheckCode.getCode(4);
                                System.out.println("请输入验证码:");
                                if (sc.next().equals(code)) {
                                    this.vip = vip;
                                    return true;
                                } else System.out.println("验证码错误");
                                break;
                            case "2":
                                System.out.println("请输入密码");
                                if (sc.next().equals(vip.getPassword())) {
                                    this.vip = vip;
                                    return true;
                                } else System.out.println("密码错误,请检查后重试");
                                break;
                            default:
                                System.out.println("您的选择有误,请重新输入");

                        }
                    }
                } else System.out.println("账号不存在");
            } else return false;

        }
    }


    public boolean vipPay(ArrayList<Book> buyBooks) {
        if (!checkLogin()) {
            return false;
        }

        System.out.println("您选购了" + buyBooks.size() + "本书");
        double totalMoney = 0;
        for (Book buyBook : buyBooks) {
            totalMoney = BigDecimal.valueOf(buyBook.getPrice()).add(BigDecimal.valueOf(totalMoney)).doubleValue();
        }
        double payMoney = BigDecimal.valueOf(totalMoney).multiply(BigDecimal.valueOf(0.95)).doubleValue();
        int integer = (int) totalMoney / 10;
        System.out.println("原价一共" + totalMoney + "元,vip享受九五折,您需要支付" + payMoney + "元");
        System.out.println("共计" + totalMoney + "元,您可以直接支付,或者使用账户余额支付");
        while (true) {
            System.out.println("# 0.取消支付 1.现金支付 2.余额支付");
            switch (sc.next()) {
                case "0":
                    System.out.println("您取消了支付,还需要什么服务么?");
                    return false;
                case "1":
                    System.out.println("购买成功,您支付了" + payMoney + "元,请拿好您的书籍");
                    vip.setIntegral(vip.getIntegral() + integer);
                    System.out.println("您的账户积分增加了" + integer + ",当前用户积分为:" + vip.getIntegral());
                    bookService.updateBooks(buyBooks);
                    vipService.updateVip(vip);
                    return true;
                case "2":
                    System.out.println("您当前余额为:" + vip.getBalance() + "元");
                    if (vip.getBalance() < payMoney) {
                        System.out.println("当前余额不足,不足部分是否使用现金支付?('y'确定)");
                        if (sc.next().equals("y")) {
                            System.out.println("购买成功");
                            double money = BigDecimal.valueOf(payMoney).subtract(BigDecimal.valueOf(vip.getBalance())).doubleValue();
                            System.out.println("购买成功,您余额支付了" + vip.getBalance() + "元,额外支付现金" + money + "元,余额为0");
                            vip.setBalance(0.0);
                            vip.setIntegral(vip.getIntegral() + integer);
                            System.out.println("您的账户积分增加了" + integer + ",当前用户积分为:" + vip.getIntegral());
                            bookService.updateBooks(buyBooks);
                            vipService.updateVip(vip);
                            return true;
                        } else {
                            System.out.println("取消余额支付");
                        }
                    } else {
                        System.out.println("购买成功");
                        double money = BigDecimal.valueOf(vip.getBalance()).subtract(BigDecimal.valueOf(payMoney)).doubleValue();
                        System.out.println("购买成功,您余额支付了" + payMoney + "元,余额为" + money + "元");
                        vip.setBalance(money);
                        vip.setIntegral(vip.getIntegral() + integer);
                        System.out.println("您的账户积分增加了" + integer + ",当前用户积分为:" + vip.getIntegral());
                        bookService.updateBooks(buyBooks);
                        vipService.updateVip(vip);
                        return true;
                    }
                    break;
                default:
                    System.out.println("您的选择有误,请重新输入");
            }
        }

//        return false;
    }
}
