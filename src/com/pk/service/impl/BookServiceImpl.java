package com.pk.service.impl;

import com.pk.Dao.BookDao;
import com.pk.Dao.impl.BookDaoImpl;
import com.pk.domain.Book;
import com.pk.service.BookService;

import java.util.ArrayList;

public class BookServiceImpl implements BookService {
    private final BookDao bookDao = new BookDaoImpl();
    @Override
    public ArrayList<Book> findAllBooks() {
        ArrayList<Book> books = bookDao.findAllBooks();
        return books.isEmpty() ? null : books;

    }

    @Override
    public void addBook(Book book) {
        bookDao.addBook(book);
    }

    @Override
    public boolean isExist(String bookId) {
        return bookDao.getIndex(bookId)!=-1;

    }

    @Override
    public void deleteBook(String bookId) {
        bookDao.deleteBook(bookId);
    }

    @Override
    public Book getBookById(String bookId) {
        return bookDao.getBookById(bookId);
    }

    @Override
    public void updateBook(Book book) {
        bookDao.updateBook(book);
    }

    @Override
    public int getIndex(String bookId) {
        return bookDao.getIndex(bookId);
    }

    @Override
    public void updateBooks(ArrayList<Book> buyBooks) {
        bookDao.updateBooks(buyBooks);
    }

}
