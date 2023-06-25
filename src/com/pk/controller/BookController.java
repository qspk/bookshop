package com.pk.controller;

import com.pk.domain.Book;
import com.pk.service.BookService;
import com.pk.service.impl.BookServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 书籍管理/操作页面
 */
public class BookController {
    private final Scanner sc = new Scanner(System.in);
    public static final Logger LOGGER = LoggerFactory.getLogger("BookController");
    private final BookService bookService = new BookServiceImpl();

    public void start(String username) {
        while (true) {
            System.out.println("<------书籍管理系统------>");
            System.out.println("您可以进行如下操作:");
            System.out.println("# 0.退出 1.查看全部书籍 2.添加书籍 3.修改书籍价格和余量 4.删除书籍 ");
            switch (sc.next()) {
                case "0":
                    System.out.println("即将退出书籍管理系统");
                    LOGGER.info("管理员:" + username + "退出书籍管理系统");
                    return;
                case "1":
                    findAllBooks();
                    break;
                case "2":
                    addBook(username);
                    break;
                case "3":
                    updateBook(username);
                    break;
                case "4":
                    deleteBook(username);
                    break;

                default:
                    System.out.println("您的选择有误,请重新输入");
            }
        }
    }

    //更新书籍价格和数量
    private void updateBook(String username) {
        if (findAllBooks() != null) {
            while (true) {
                System.out.println("请输入要修改的书码(输入'q'退出删除操作):");
                String bookId = sc.next();
                if (bookId.equals("q")) {
                    System.out.println("您取消了修改操作");
                    return;
                } else {
                    if (bookService.isExist(bookId)) {
                        Book book = bookService.getBookById(bookId);
                        System.out.println("查询到书籍信息如下:");
                        System.out.println(book.toString());
                        double price;
                        while (true) {
                            System.out.println("请输入新的价格:");
                            if (sc.hasNextDouble()) {
                                price = sc.nextDouble();
                                break;
                            } else System.out.println("请正确输入价格");
                        }
                        int number;
                        while (true) {
                            System.out.println("请输入新的存量:");
                            if (sc.hasNextInt()) {
                                number = sc.nextInt();
                                break;
                            } else System.out.println("请正确输入数量");
                        }

                        book.setPrice(price);
                        book.setNumber(number);
                        bookService.updateBook(book);
                        LOGGER.info("管理员:" + username + "修改书籍:" + book.getShowName());
                        return;

                    } else {
                        System.out.println("书码不存在,请检查后重试");
                    }
                }
            }
        }
    }


    private void deleteBook(String username) {
        if (findAllBooks() != null) {
            while (true) {
                System.out.println("请输入要删除的书码(输入'q'退出删除操作):");
                String bookId = sc.next();
                if (bookId.equals("q")) {
                    System.out.println("您取消了删除操作");
                    return;
                } else {
                    if (bookService.isExist(bookId)) {
                        System.out.println("是否确认删除,输入'y'确认");
                        if (sc.next().equals("y")) {
                            bookService.deleteBook(bookId);
                            System.out.println("删除成功");
                            Book book = bookService.getBookById(bookId);
                            LOGGER.info("管理员:" + username + "删除书籍:" + book.getShowName());
                            return;
                        } else System.out.println("您取消了删除操作");
                    } else {
                        System.out.println("书码不存在,请检查后重试");
                    }
                }
            }
        }
    }


    //添加一本书
    private void addBook(String username) {
        String bookId = null;
        while (true) {
            System.out.println("请输入书码:");
            bookId = sc.next();
            if (bookService.isExist(bookId)) {
                System.out.println("书名已存在,请重新输入");
            } else {
                break;
            }
        }
        System.out.println("请输入书名:");
        String bookName = sc.next();
        System.out.println("请输入作者:");
        String author = sc.next();
        double price;
        while (true) {
            System.out.println("请输入价格:");
            if (sc.hasNextDouble()) {
                price = sc.nextDouble();
                break;
            } else System.out.println("请正确输入价格");
        }
        int number;
        while (true) {
            System.out.println("请输入存量:");
            if (sc.hasNextInt()) {
                number = sc.nextInt();
                break;
            } else System.out.println("请正确输入数量");
        }

        Book book = new Book(bookId, bookName, author, price, number);

        bookService.addBook(book);
        LOGGER.info("管理员:" + username + "添加书籍:" + book.getShowName());
        System.out.println("添加成功");

    }

    //查看全部书籍
    public ArrayList<Book> findAllBooks() {
        ArrayList<Book> books = bookService.findAllBooks();
        if (books == null) { //判断当前有没有书籍
            System.out.println("当前没有书籍,快去添加书籍吧");
        } else {
            System.out.println("全部书籍信息如下");
            int i = 1;  //标记当前是第多少本书
            System.out.println("--------------------------------");
            System.out.print("序号\t\t书码\t\t");
            System.out.printf("%15s\t","书名");
            System.out.println("作者\t\t价格(元)\t余量(本)");
            for (Book book : books) {
                System.out.printf("%3d.\t", i++);
                System.out.print(book.getBookId() + "\t" );
                System.out.printf("%15s\t",book.getBookName());
                System.out.println(book.getAuthor() + "\t" + book.getPrice() + "\t" + book.getNumber());
                System.out.println();
//                System.out.println("序号:" + i++);
//                System.out.println(book.toString());
//                System.out.println("--------------------------------");
            }
            System.out.println("--------------------------------");

        }
        return books;
    }


}
