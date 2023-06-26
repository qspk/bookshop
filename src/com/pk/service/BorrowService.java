package com.pk.service;

import com.pk.domain.Book;
import com.pk.domain.Borrow;
import com.pk.domain.Vip;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface BorrowService {
    boolean isBorrow(Vip vip);

    ArrayList<Borrow> getVipBorrows(Vip vip);


    void addInfo(Borrow borrow);

    void deleteIfo(Borrow vipBorrow);

    boolean isBorrowByBookId(Vip vip,String bookId);
}
