package com.pk.dao.impl;

import com.pk.dao.BookDao;
import com.pk.domain.Book;
import com.pk.utils.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BookDaoImpl implements BookDao {
    private static ArrayList<Book> books = new ArrayList<>();
    public static final Logger LOGGER = LoggerFactory.getLogger("BookDaoImpl");

    static {
        reload();
    }

    //获取全部书籍信息
    @Override
    public ArrayList<Book> findAllBooks() {
        reload();
        return books;
    }

    //添加一本书
    @Override
    public void addBook(Book book) {
        books.add(book);
        reSave();
    }

    //通过书码获取其集合索引
    @Override
    public int getIndex(String bookId) {
        int index = -1;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId().equals(bookId)) {
                index = i;
            }
        }
        return index;
    }

    //通过书码删除书籍
    @Override
    public void deleteBook(String bookId) {
        int index = getIndex(bookId);
        books.remove(index);
        reSave();
    }

    //通过书码获得对应书籍信息
    @Override
    public Book getBookById(String bookId) {
        int index = getIndex(bookId);
        return books.get(index);
    }

    //通过书籍对象,更改书籍信息
    @Override
    public void updateBook(Book book) {
        reload();
        int index = getIndex(book.getBookId());
        books.set(index, book);
        reSave();
    }

    //通过卖书集合更改书籍信息-余量
    @Override
    public void updateBooks(ArrayList<Book> buyBooks) {
        reload();
        for (Book buyBook : buyBooks) {
            for (Book book : books) {
                if (buyBook.getBookId().equals(book.getBookId())) {
                    LOGGER.info(book.getShowName() + "卖出了1本");
                    book.numberSubtract();
//                    int number = book.getNumber() - 1;
//                    book.setNumber(number);
                    break;
                }
            }
        }
        reSave();
    }


    //将书籍信息读入集合
    @SuppressWarnings("unchecked")
    private static void reload() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path.BOOKS))) {
            books = (ArrayList<Book>) ois.readObject();
        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("");
        }
    }

    //将书籍信息写入文本
    private static void reSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path.BOOKS))) {
            oos.writeObject(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
