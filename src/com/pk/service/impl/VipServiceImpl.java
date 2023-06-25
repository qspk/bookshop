package com.pk.service.impl;

import com.pk.Dao.VipDao;
import com.pk.Dao.impl.VipDapImpl;
import com.pk.domain.Vip;
import com.pk.service.VipService;

public class VipServiceImpl implements VipService {
    private final VipDao vipDao = new VipDapImpl();
    @Override
    public boolean isExist(String phone) {
  /*      if (vipDao.getIndex(phone) == -1) {
            return false;
        } else {
            return true;
        }*/

        return vipDao.getIndex(phone) != -1;
    }

    @Override
    public void addVip(Vip vip) {
        vipDao.addVip(vip);
    }

    @Override
    public Vip getVip(String phone) {
        return vipDao.getVip(phone);
    }

    @Override
    public void updateVip(Vip vip) {
        vipDao.updateVip(vip);
    }
}
