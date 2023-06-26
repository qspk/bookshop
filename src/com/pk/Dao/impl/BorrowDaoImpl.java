package com.pk.Dao.impl;

import com.pk.Dao.BorrowDao;
import com.pk.domain.Borrow;
import com.pk.domain.Vip;
import com.pk.utils.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class BorrowDaoImpl implements BorrowDao {

    private static ArrayList<Borrow> borrows = new ArrayList<>();
    public static final Logger LOGGER = LoggerFactory.getLogger("BorrowDaoImpl");
    static {reload();}



    @Override
    public boolean isBorrow(Vip vip) {
        return !getVipBorrows(vip).isEmpty();
    }

    @Override
    public ArrayList<Borrow> getVipBorrows(Vip vip) {
        //单个vip客户借书信息集合
        ArrayList<Borrow> vipBorrows = new ArrayList<>();
        for (Borrow borrow : borrows) {
            if (borrow.getVip().getPhone().equals(vip.getPhone())) {
                vipBorrows.add(borrow);
            }
        }
        return vipBorrows;
    }

    //添加一条借阅信息
    @Override
    public void addInfo(Borrow borrow) {
        borrows.add(borrow);
        reSave();
        LOGGER.info(borrow.getVip().getShowName() + "借阅了一本图书:" + borrow.getBook().getShowName());
    }

    @Override
    public void deleteInfo(Borrow vipBorrow) {
        reload();
        for (int i = 0; i < borrows.size(); i++) {
            if (borrows.get(i).getVip().getPhone().equals(vipBorrow.getVip().getPhone())) {
                if (borrows.get(i).getBook().getBookId().equals(vipBorrow.getBook().getBookId())) {
                    borrows.remove(i);
                    break;
                }

            }
        }
        reSave();
    }

    @Override
    public ArrayList<Borrow> findAllBorrows() {
        reload();
        return borrows;
    }

    //将借阅信息读入集合
    @SuppressWarnings("unchecked")
    private static void reload() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path.BORROW_INFO))) {
            borrows = (ArrayList<Borrow>) ois.readObject();
        } catch (Exception e) {
            System.out.println();
        }
    }

    //将借阅信息写入文本
    private static void reSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path.BORROW_INFO))) {
            oos.writeObject(borrows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
