package com.pk.controller;

import com.pk.domain.Book;
import com.pk.domain.Vip;
import com.pk.service.BookService;
import com.pk.service.VipService;
import com.pk.service.impl.BookServiceImpl;
import com.pk.service.impl.VipServiceImpl;
import com.pk.utils.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 普通客户操作页
 */
public class BaseCustomerController {
    private final Scanner sc = new Scanner(System.in);
    public static final Logger LOGGER = LoggerFactory.getLogger("BaseCustomerController");
    private final VipService vipService = new VipServiceImpl();
    private final BookService bookService = new BookServiceImpl();

    public void start() {
        System.out.println("欢迎进入小黑书店,您可以进行如下操作");
        System.out.println("如果您是会员,请去会员页面登录,或者可以去注册成为会员");
        System.out.println("本店会员可以享受购书九五折优惠和攒积分兑换好礼,以及借书等服务");
        while (true) {
            System.out.println("# 0.离开 1.注册vip 2.浏览购买书籍");
            switch (sc.next()) {
                case "0":
                    System.out.println("谢谢惠顾,欢迎下次再来~~");
                    return;
                case "1":
                    register();
                    break;
                case "2":
                    buyBook();
                    break;
                default:
                    System.out.println("您的选择有误,请重新输入");
            }
        }
    }

    //买书
    private void buyBook() {
        ArrayList<Book> books = new ArrayList<>(new BookController().findAllBooks());
        ArrayList<Book> buyBooks = new ArrayList<>();
        System.out.println("您可以根据书码选购书籍");
        while (true) {
            System.out.println("请输入您要购买的书籍书码");
            String bookId = sc.next();
            int index;
            if ((index=bookService.getIndex(bookId))!=-1) {
                if (books.get(index).getNumber() <= 0) {
                    System.out.println("当前书籍余量不足,您可以选购其他书籍");
                } else {
                    System.out.println("您选购的书籍信息如下:");
                    System.out.println(books.get(index).toString());
                    buyBooks.add(books.get(index));
                    books.get(index).setNumber(books.get(index).getNumber() - 1);
                    System.out.println("是否继续选购('y'继续)");
                    if (!sc.next().equals("y")) {
                        System.out.println("您选购了" + buyBooks.size() + "本书");
                        double totalMoney = 0;
                        for (Book buyBook : buyBooks) {
                            totalMoney = BigDecimal.valueOf(buyBook.getPrice()).add(BigDecimal.valueOf(totalMoney)).doubleValue();
                        }
                        System.out.println("共计" + totalMoney + "元,您可以直接支付,或者登录或注册vip享受优惠");
                        while (true) {
                            System.out.println("# 0.取消支付 1.登录会员 2.注册会员 3.直接支付");
                            switch (sc.next()) {
                                case "0":
                                    System.out.println("您取消了支付,还需要什么服务么?");
                                    return;
                                case "1":
                                    if (new VipController().vipPay(buyBooks))  return;
                                    break;
                                case "2":
                                    register();
                                    System.out.println("即将跳转到vip登录界面");
                                    if (new VipController().vipPay(buyBooks))  return;
                                    break;
                                case "3":
                                    bookService.updateBooks(buyBooks);
                                    System.out.println("您支付了"+ totalMoney + "元,感谢您的购买!");
                                    return;
                                default:
                                    System.out.println("您的选择有误,请重新输入");
                            }
                        }
                    }
                }
            }else System.out.println("未找到书籍,请检查书码是否正确");
        }
    }

    //注册
    private void register() {
        System.out.println("--vip注册页面--");
        while (true) {
            System.out.println("请输入手机号(输入'q'离开注册):");
            String phone = sc.next();
            if (phone.equals("q")) {
                System.out.println("即将离开注册页面");
                return;
            }

            if (!phone.matches(Format.PHONE)) {
                System.out.println("号码格式不对,请重试");
            } else {
                if (!vipService.isExist(phone)) {
                    System.out.println("请输入您的名字:");
                    String name = sc.next();
                    System.out.println("请输入密码:");
                    String pwd = sc.next();
                    System.out.println("请再次输入密码以确认");
                    while (true) {
                        String pwd2 = sc.next();
                        if (pwd.equals(pwd2)) {
                            Vip vip = new Vip(phone, name, pwd);
                            vipService.addVip(vip);
                            System.out.println("恭喜您成为尊贵的vip用户,正在返回主页,您可以进入vip页面登录账号,享受vip服务");
                            LOGGER.info(vip.getShowName() + "注册成为vip");
                            return;
                        } else System.out.println("两次密码不一致请检查后再输入确认密码");
                    }
                } else {
                    System.out.println("电话号码已存在,请重新输入");
                }
            }

        }
    }

}

