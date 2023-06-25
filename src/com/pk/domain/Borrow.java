package com.pk.domain;

import com.pk.utils.Format;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Borrow implements Serializable {
    public static final long serialVersionUID = 3L;

    private Vip vip;
    private Book book;
    private LocalDateTime localDateTime;

    public Borrow(Vip vip, Book book, LocalDateTime localDateTime) {
        this.vip = vip;
        this.book = book;
        this.localDateTime = localDateTime;
    }

    public Borrow() {
    }

    public Vip getVip() {
        return vip;
    }
    public String showInfo() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Format.DATE_TIME);
        String ldt = dtf.format(localDateTime);
        return vip.getShowName() + "\t" + book.getShowName() + "\t" + ldt;
    }
    public void setVip(Vip vip) {
        this.vip = vip;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrow borrow = (Borrow) o;
        return Objects.equals(vip, borrow.vip) && Objects.equals(book, borrow.book) && Objects.equals(localDateTime, borrow.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vip, book, localDateTime);
    }

    @Override
    public String toString() {
        String phone = vip.getPhone();
        String substring = phone.substring(phone.length() - 4);
        return localDateTime + " - " + vip.getName() + "(" + substring + ")" +
                " - " + book.getBookId() + " - " + book.getBookName();
    }
}
