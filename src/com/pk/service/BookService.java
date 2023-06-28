package com.pk.service;

import com.pk.domain.Book;

import java.util.ArrayList;

public interface BookService {
    ArrayList<Book> findAllBooks();

    void addBook(Book book);

    boolean isExist(String bookId);

    void deleteBook(String bookId);

    Book getBookById(String bookId);

    void updateBook(Book book);

    int getIndex(String bookId);

    void updateBooks(ArrayList<Book> buyBooks);

    ArrayList<Book> findAllBooksByNumber();
}
