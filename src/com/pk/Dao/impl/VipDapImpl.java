package com.pk.Dao.impl;

import com.pk.Dao.VipDao;
import com.pk.domain.Vip;
import com.pk.utils.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

public class VipDapImpl implements VipDao {
    //记录vip客户的集合
    public static ArrayList<Vip> vips = new ArrayList<>();
    static {
        reload();
    }
    public static final Logger LOGGER = LoggerFactory.getLogger("VipDapImpl");

    //将vip信息读入集合
    @SuppressWarnings("unchecked")
    private static void reload() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path.VIPS))) {
            vips = (ArrayList<Vip>) ois.readObject();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("书店还没有vip客户,要努力哦~~");
        }
    }

    //将vip写入文本
    private static void reSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path.VIPS))) {
            oos.writeObject(vips);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getIndex(String phone) {
        int index = -1;
        for (int i = 0; i < vips.size(); i++) {
            if (vips.get(i).getPhone().equals(phone)) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void addVip(Vip vip) {
        vips.add(vip);
        reSave();
    }

    @Override
    public Vip getVip(String phone) {
        int index = getIndex(phone);
        return vips.get(index);
    }

    @Override
    public void updateVip(Vip vip) {
        for (int i = 0; i < vips.size(); i++) {
            if (vips.get(i).equals(vip)) {
                Vip vip0 = vips.get(i);
                int integerUpdate = vip.getIntegral() - vip0.getIntegral();
                double balanceUpdate = BigDecimal.valueOf(vip.getBalance()).subtract(BigDecimal.valueOf(vip0.getBalance())).doubleValue();
                LOGGER.info(vip.getShowName()+"的账号信息发生改变,余额:"+balanceUpdate+"元,积分"+integerUpdate);
                vips.set(i, vip);
                break;
            }
        }
    }
}
