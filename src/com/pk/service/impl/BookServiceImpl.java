package com.pk.service.impl;

import com.pk.dao.BookDao;
import com.pk.dao.impl.BookDaoImpl;
import com.pk.domain.Book;
import com.pk.service.BookService;

import java.util.ArrayList;
import java.util.Comparator;

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

    @Override
    public ArrayList<Book> findAllBooksByNumber() {
        ArrayList<Book> books = bookDao.findAllBooks();
     /*   books.sort(new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });  */
        books.sort(Comparator.comparingInt(Book::getNumber));
        return books.isEmpty() ? null : books;
    }

}
