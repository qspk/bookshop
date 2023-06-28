package com.pk.controller;

import com.pk.domain.Book;
import com.pk.domain.Borrow;
import com.pk.domain.Vip;
import com.pk.service.BookService;
import com.pk.service.BorrowService;
import com.pk.service.VipService;
import com.pk.service.impl.BookServiceImpl;
import com.pk.service.impl.BorrowServiceImpl;
import com.pk.service.impl.VipServiceImpl;
import com.pk.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class VipController {
    private Vip vip = null;
    public static final Logger LOGGER = LoggerFactory.getLogger("VipController");
    private final Scanner sc = new Scanner(System.in);
    private final VipService vipService = new VipServiceImpl();
    private final BookService bookService = new BookServiceImpl();
    private final BorrowService borrowService = new BorrowServiceImpl();

    public void start() {
        if (vip == null) {
            if (!checkLogin()) return;
        }
        System.out.println("尊贵的vip客户,您可以进行下列操作:");
        while (true) {
            System.out.println("# 0.离开 1.浏览书籍 2.购买书籍 3.余额充值 4.积分查询与兑换 5.借书 6.还书 7.查看个人信息");
            switch (sc.next()) {
                case "0":
                    System.out.println("欢迎您的下次光临~~");
                    LOGGER.info(vip.getShowName() + "离开了");
                    vip = null;
                    return;
                case "1":
                    new BookController().findAllBooks();
                    break;
                case "2":
                    vipBuyBooks();
                    break;
                case "3":
                    chargeBalance();
                    break;
                case "4":
                    quireInteger();
                    break;
                case "5":
                    borrowBook();
                    break;
                case "6":
                    returnBook();
                    break;
                case "7":
                    checkVipInfo();
                    break;

                default:
                    System.out.println("您的选择有误,请重新输入");

            }
        }
    }

    //还书
    private void returnBook() {
        System.out.println("--还书页面--");
        System.out.println("您的借阅信息如下:");
        ArrayList<Borrow> vipBorrows = borrowService.getVipBorrows(vip);
        if (borrowService.isBorrow(vip)) {
            for (Borrow vipBorrow : vipBorrows) {
                System.out.println(vipBorrow.showInfo());
            }
        } else {
            System.out.println("您当前没有借阅图书");
            System.out.println("即将返回vip操作页");
            return;
        }

        while (true) {
            if (vipBorrows.isEmpty()) {
                System.out.println("您已经没有借阅读书信息了,即将返回vip操作页");
                return;
            }
            System.out.println("提示:一本书一天0.3元,未满一天按一天算");
            System.out.println("您可以选择 # 0.离开 1.还一本书 2.书籍遗失");
            switch (sc.next()) {
                case "0":
                    System.out.println("您离开了还书页面");
                    return;
                case "1":
                    returnOneBook(vipBorrows);
                    break;
 /*               case "2":
                    returnAllBooks(vipBorrows);
                    break;*/
                case "2":
                    loseBook(vipBorrows);
                    break;
                default:
                    System.out.println("您的选择有误,请重新选择");

            }
        }


    }

    //书籍丢失
    private void loseBook(ArrayList<Borrow> vipBorrows) {
        System.out.println("请输入丢失的图书书码");
        String bookId = sc.next();
        for (Borrow vipBorrow : vipBorrows) {
            if (vipBorrow.getBook().getBookId().equals(bookId)) {
                System.out.println("您的借阅信息如下");
                System.out.println(vipBorrow.showInfo());

                double money = vipBorrow.getBook().getPrice();
                System.out.println("本书原价" + money + "元");

                while (true) {
                    System.out.println("请选择您的支付方式 #1.现金 2.账号余额");
                    switch (sc.next()) {
                        case "1":
                            System.out.println("您支付了" + money + "元,清除借阅信息");
                            LOGGER.info(vip.getShowName() + "支付了丢失的图书:" + vipBorrow.getBook().getShowName() + "损失");
                            borrowService.deleteIfo(vipBorrow);
                            return;
                        case "2":
                            System.out.println("当前账户余额:" + vip.getBalance() + "元");
                            if (vip.getBalance() >= money) {
                                double balance = BigDecimal.valueOf(vip.getBalance()).subtract(BigDecimal.valueOf(money)).doubleValue();
                                System.out.println("余额支付成功,一共支付了" + money + "元,清除借阅信息,余额剩余:" + balance + "元");
                                vip.setBalance(balance);
                            } else {
                                System.out.println("当前余额不足,不足的请用现金支付");
                                double balance = -(BigDecimal.valueOf(vip.getBalance()).subtract(BigDecimal.valueOf(money)).doubleValue());
                                System.out.println("一共支付了" + money + "元,余额支付" + vip.getBalance() + "元,现金支付" + balance + "元,清除借阅信息");
                                vip.setBalance(0.0);
                            }
                            LOGGER.info(vip.getShowName() + "支付了丢失的图书:" + vipBorrow.getBook().getShowName() + "损失");
                            vipService.updateVip(vip);
                            borrowService.deleteIfo(vipBorrow);
                            return;
                        default:
                            System.out.println("您的选择有误,请重新选择");
                    }
                }
            }
        }
        System.out.println("未找到书籍,请检查书码后重试");
    }

    //归还一本图书
    private void returnOneBook(ArrayList<Borrow> vipBorrows) {
        System.out.println("请输入您要归还的图书书码");
        String bookId = sc.next();
        for (Borrow vipBorrow : vipBorrows) {
            if (vipBorrow.getBook().getBookId().equals(bookId)) {
                System.out.println("您的借阅信息如下");
                System.out.println(vipBorrow.showInfo());
                LocalDateTime end = LocalDateTime.now();
                int dayNum = Days.getDayNum(vipBorrow.getLocalDateTime(), end);
                double money = BigDecimal.valueOf(dayNum).multiply(BigDecimal.valueOf(0.3)).doubleValue();
                System.out.println("您一共借阅了" + dayNum + "天,共计" + money + "元");

                while (true) {
                    System.out.println("请选择您的支付方式 #1.现金 2.账号余额");
                    switch (sc.next()) {
                        case "1":
                            System.out.println("您支付了" + money + "元,还书成功");
                            LOGGER.info(vip.getShowName() + "归还图书:" + vipBorrow.getBook().getShowName());
                            vipBorrow.getBook().setNumber(vipBorrow.getBook().getNumber() + 1);
                            bookService.updateBook(vipBorrow.getBook());
                            borrowService.deleteIfo(vipBorrow);
                            return;
                        case "2":
                            System.out.println("当前账户余额:" + vip.getBalance() + "元");
                            if (vip.getBalance() >= money) {
                                double balance = BigDecimal.valueOf(vip.getBalance()).subtract(BigDecimal.valueOf(money)).doubleValue();
                                System.out.println("余额支付成功,一共支付了" + money + "元,还书成功,余额剩余:" + balance + "元");
                                vip.setBalance(balance);

                            } else {
                                System.out.println("当前余额不足,不足的请用现金支付");
                                double balance = -(BigDecimal.valueOf(vip.getBalance()).subtract(BigDecimal.valueOf(money)).doubleValue());
                                System.out.println("一共支付了" + money + "元,余额支付" + vip.getBalance() + "元,现金支付" + balance + "元,还书成功");
                                vip.setBalance(0.0);
                            }
                            LOGGER.info(vip.getShowName() + "归还图书:" + vipBorrow.getBook().getShowName());
                            vipService.updateVip(vip);
                            vipBorrow.getBook().setNumber(vipBorrow.getBook().getNumber() + 1);
                            bookService.updateBook(vipBorrow.getBook());
                            borrowService.deleteIfo(vipBorrow);
                            return;
                        default:
                            System.out.println("您的选择有误,请重新选择");
                    }
                }
            }
        }
        System.out.println("未找到书籍,请检查书码后重试");
    }


    //借书
    private void borrowBook() {
        System.out.println("--借阅页面--");
        System.out.println("提示:借书业务需要您的余额大于等于100元,且同时只能借3本图书,一本书一天0.3元,未满一天按一天算");
        System.out.println("您当前的借阅信息:");
        ArrayList<Borrow> vipBorrows = borrowService.getVipBorrows(vip);
        if (borrowService.isBorrow(vip)) {
            for (Borrow vipBorrow : vipBorrows) {
                System.out.println(vipBorrow.showInfo());
            }
        } else {
            System.out.println("您当前没有借阅图书");
        }
        if (vipBorrows.size() >= 3) {
            System.out.println("您已借阅三本图书,请先还书后再进行借阅");
        } else if (vip.getBalance() < 100) {
            System.out.println("您当前账户余额为" + vip.getBalance() + "元,不足100元,请充值后再试");
        } else {
            ArrayList<Book> books = new BookController().findAllBooks();  //展示全部图书信息
            System.out.println("您可以根据书码借阅图书(输入'q'退出):");
            String bookId = sc.next();
            if (bookId.equals("q")) {
                System.out.println("您取消了借书操作");

            } else {
                if (!borrowService.isBorrowByBookId(vip,bookId)) {
                    if (bookService.isExist(bookId)) {
                        Book book = bookService.getBookById(bookId);
                        if (book.getNumber() <= 0) {
                            System.out.println("抱歉,当前图书没有余量了,我们会尽快添加");
                        } else {
                            System.out.println("借阅成功,您借阅的图书信息如下");
                            System.out.println(book.toString());
                            book.setNumber(book.getNumber() - 1);
                            bookService.updateBook(book);
                            LocalDateTime ldt = LocalDateTime.now();
                            Borrow borrow = new Borrow(vip, book, ldt);
                            borrowService.addInfo(borrow);
                        }
                    } else {
                        System.out.println("未查询到图书,请检查书码后重试");
                    }
                } else {
                    System.out.println("您已经借阅过该图书了,您可以选择其他图书");
                }
            }

        }
    }

    //积分查询与兑换
    private void quireInteger() {
        System.out.println("--积分兑换页面--");
        System.out.println("当前积分:" + vip.getIntegral());
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(Path.PRIZE));
            Set<String> prizeNames = properties.stringPropertyNames();
            Map<String, Integer> prizes = new HashMap<>();
            for (String prizeName : prizeNames) {
                int integer = Integer.parseInt(properties.getProperty(prizeName));
                prizes.put(prizeName, integer);
            }
            prizes = MapUtils.sortByValue(prizes);
            System.out.println("当前可兑换物品如下:");
            prizes.forEach((k, v) -> {
                System.out.println(k + "===" + v + "积分");
            });
            System.out.println("请输您要兑换的物品");
            String option = sc.next();
            if (prizes.containsKey(option)) {
                if (vip.getIntegral() >= prizes.get(option)) {
                    LOGGER.info(vip.getShowName() + "兑换了" + option);
                    vip.setIntegral(vip.getIntegral() - prizes.get(option));
                    System.out.println("兑换成功");
                    vipService.updateVip(vip);  //更新用户信息
                } else {
                    System.out.println("您的积分不足,可以看看其他奖品哦");
                }
            } else System.out.println("您选择的物品还没有哦~~");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //余额充值
    private void chargeBalance() {
        System.out.println("--余额充值页面--");
        System.out.println("当前余额:" + vip.getBalance() + "元");
        while (true) {
            System.out.println("请输入您要充值的数额(输入'q'退出)");
            String s;
            if (!(s = sc.next()).equals("q")) {
                if (s.matches(Format.DOUBLE_STRING)) {
                    double charge = Double.parseDouble(s);
                    if (charge <= 0) {
                        System.out.println("请输入一个大于0的值");
                    } else {
                        vip.setBalance(BigDecimal.valueOf(vip.getBalance()).add(BigDecimal.valueOf(charge)).doubleValue());
                        vipService.updateVip(vip);
                        System.out.println("充值成功,当前余额:" + vip.getBalance() + "元");
                        LOGGER.info(vip.getShowName() + "进行了充值");
                        vip = vipService.getVip(vip.getPhone());
                        return;
                    }
                } else {
                    System.out.println("请输入一个数字");
                }

            } else {
                System.out.println("您取消了充值");
                return;
            }

        }

    }

    //查看个人信息
    private void checkVipInfo() {
        System.out.println("--vip信息--------");
        System.out.println(vip.toString());
        if (borrowService.isBorrow(vip)) {
            ArrayList<Borrow> vipBorrows = borrowService.getVipBorrows(vip);
            System.out.println("借阅信息:");
            for (Borrow vipBorrow : vipBorrows) {
                System.out.println(vipBorrow.showInfo());
            }
        }
    }

    //vip购买图书
    private void vipBuyBooks() {
        System.out.println("--买书页面--");
        ArrayList<Book> books = new BookController().findAllBooks();
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
                        return;
                    }
                }
            } else System.out.println("未找到书籍,请检查书码是否正确");
        }
    }

    //检查登录
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

    //vip登录
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
                                System.out.println("验证码<" + code + ">已发送到您的手机");
                                System.out.println("请输入验证码:");
                                if (sc.next().equalsIgnoreCase(code)) {
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

    //vip支付方式
    public boolean vipPay(ArrayList<Book> buyBooks) {
        System.out.println("--会员支付页面--");
        if (vip == null) {
            if (!checkLogin()) return false;
        }

        System.out.println("您选购了" + buyBooks.size() + "本书");
        double totalMoney = 0;
        for (Book buyBook : buyBooks) {
            totalMoney = BigDecimal.valueOf(buyBook.getPrice()).add(BigDecimal.valueOf(totalMoney)).doubleValue();
        }
        double payMoney = BigDecimal.valueOf(totalMoney).multiply(BigDecimal.valueOf(0.95)).doubleValue();
        int integer = (int) totalMoney / 10;
        System.out.println("原价一共" + totalMoney + "元,vip享受九五折,您需要支付" + payMoney + "元");
        System.out.println("共计" + payMoney + "元,您可以直接支付,或者使用账户余额支付");
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
