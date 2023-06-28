package com.pk.dao;

import com.pk.domain.Vip;

import java.util.ArrayList;

public interface VipDao {
    int getIndex(String phone);

    void addVip(Vip vip);

    Vip getVip(String phone);

    void updateVip(Vip vip);

    ArrayList<Vip> findAllVips();

}
