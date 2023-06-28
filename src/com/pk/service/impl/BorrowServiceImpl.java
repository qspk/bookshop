package com.pk.service.impl;

import com.pk.dao.BorrowDao;
import com.pk.dao.impl.BorrowDaoImpl;
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

    @Override
    public void addInfo(Borrow borrow) {
        borrowDao.addInfo(borrow);
    }

    @Override
    public void deleteIfo(Borrow vipBorrow) {
        borrowDao.deleteInfo(vipBorrow);
    }

    @Override
    public boolean isBorrowByBookId(Vip vip,String bookId) {
        ArrayList<Borrow> borrows = borrowDao.getVipBorrows(vip);
        if (borrows.isEmpty()) {
            return false;
        } else {
            for (Borrow borrow : borrows) {
                if (borrow.getBook().getBookId().equals(bookId)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public ArrayList<Borrow> findAllBorrows() {
        ArrayList<Borrow> borrows = borrowDao.findAllBorrows();
        if (borrows.isEmpty()) {
            return null;
        } else return borrows;
    }

}
