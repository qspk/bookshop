package com.pk.Dao.impl;

import com.pk.Dao.BorrowDao;
import com.pk.domain.Book;
import com.pk.domain.Borrow;
import com.pk.domain.Vip;
import com.pk.utils.Path;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BorrowDaoImpl implements BorrowDao {

    private static ArrayList<Borrow> borrows = new ArrayList<>();
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
            if (borrow.getVip().equals(vip)) {
                vipBorrows.add(borrow);
            }
        }
        return vipBorrows;
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
