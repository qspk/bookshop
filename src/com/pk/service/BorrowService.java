package com.pk.service;

import com.pk.domain.Borrow;
import com.pk.domain.Vip;

import java.util.ArrayList;

public interface BorrowService {
    boolean isBorrow(Vip vip);

    ArrayList<Borrow> getVipBorrows(Vip vip);
}
