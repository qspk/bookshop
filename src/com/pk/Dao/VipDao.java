package com.pk.Dao;

import com.pk.domain.Vip;

public interface VipDao {
    int getIndex(String phone);

    void addVip(Vip vip);

    Vip getVip(String phone);

    void updateVip(Vip vip);
}
