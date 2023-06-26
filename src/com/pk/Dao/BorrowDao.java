package com.pk.Dao;

import com.pk.domain.Borrow;
import com.pk.domain.Vip;

import java.util.ArrayList;


public interface BorrowDao {
    boolean isBorrow(Vip vip);

    ArrayList<Borrow> getVipBorrows(Vip vip);

    void addInfo(Borrow borrow);

    void deleteInfo(Borrow vipBorrow);

    ArrayList<Borrow> findAllBorrows();
}
