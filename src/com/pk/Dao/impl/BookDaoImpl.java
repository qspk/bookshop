package com.pk.Dao.impl;

import com.pk.Dao.BookDao;
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
    private static  ArrayList<Book> books = new ArrayList<>();
    public static final Logger LOGGER = LoggerFactory.getLogger("BookDaoImpl");
     static {
         reload();
     }
    @Override
    public ArrayList<Book> findAllBooks() {
        return books;
    }

    @Override
    public void addBook(Book book) {
        books.add(book);
        reSave();
    }

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

    @Override
    public void deleteBook(String bookId) {
        int index = getIndex(bookId);
        books.remove(index);
        reSave();
    }

    @Override
    public Book getBookById(String bookId) {
        int index = getIndex(bookId);
        return books.get(index);
    }

    @Override
    public void updateBook(Book book) {
        int index = getIndex(book.getBookId());
        books.set(index, book);
        reSave();
    }

    @Override
    public void updateBooks(ArrayList<Book> buyBooks) {
        for (Book buyBook : buyBooks) {
            for (int i = 0; i < books.size(); i++) {
                if (buyBook.getBookId().equals(books.get(i).getBookId())) {
                    LOGGER.info("书码:" + buyBook.getBookId() + "卖出了1本");
                    books.get(i).setNumber(books.get(i).getNumber() - 1);
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
