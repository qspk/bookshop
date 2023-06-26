package com.pk.service.impl;

import com.pk.Dao.BorrowDao;
import com.pk.Dao.impl.BorrowDaoImpl;
import com.pk.domain.Borrow;
import com.pk.domain.Vip;
import com.pk.service.BorrowService;

import java.util.ArrayList;

public class BorrowServiceImpl implements BorrowService {

    private final BorrowDao borrowDao = new BorrowDaoImpl();
    @Override
    public boolean isBorrow(Vip vip) {
        return borrowDao.isBorrow(vip);
    }

    @Override
    public ArrayList<Borrow> getVipBorrows(Vip vip) {
        return borrowDao.getVipBorrows(vip);
    }

}
