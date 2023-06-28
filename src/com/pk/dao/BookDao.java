package com.pk.dao;

import com.pk.domain.Book;

import java.util.ArrayList;

public interface BookDao {
    ArrayList<Book> findAllBooks();

    void addBook(Book book);

    int getIndex(String bookId);

    void deleteBook(String bookId);

    Book getBookById(String bookId);

    void updateBook(Book book);

    void updateBooks(ArrayList<Book> buyBooks);
}
