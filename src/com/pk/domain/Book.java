package com.pk.domain;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
    public static final long serialVersionUID = 2L;

    private String bookId;  //书码
    private String bookName;    //书名
    private String author;  //作者
    private Double price;   //价格
    private Integer number; //余量

    public Book() {
    }


    public Book(String bookId, String name, String author, Double price, Integer number) {
        this.bookId = bookId;
        this.bookName = name;
        this.author = author;
        this.price = price;
        this.number = number;

    }

    public void numberSubtract() {
        number--;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }


    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getShowName() {
        return bookName + "(" + bookId + ")";
    }

    @Override
    public String toString() {
        return
                "书码=" + bookId + '\n' +
                        "书名=" + bookName + '\n' +
                        "作者=" + author + '\n' +
                        "价格=" + price + '\n' +
                        "余量=" + number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) && Objects.equals(bookName, book.bookName) && Objects.equals(author, book.author) && Objects.equals(price, book.price) && Objects.equals(number, book.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, bookName, author, price, number);
    }

}
