package com.pk.service;

import com.pk.domain.Vip;

import java.util.ArrayList;

public interface VipService {
    boolean isExist(String phone);

    void addVip(Vip vip);

    Vip getVip(String phone);

    void updateVip(Vip vip);

    ArrayList<Vip>  findAllVips();
}
