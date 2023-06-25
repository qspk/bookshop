package com.pk.domain;

import java.io.Serializable;
import java.util.Objects;

public class Vip implements Serializable {
    private static final long serialVersionUID = 1L;
    private String phone;       //手机号/账户
    private String name;        //用户名
    private String password;    //密码
    private Double balance;     //余额
    private Integer integral;  //积分

    public String getShowName() {
        String substring = phone.substring(phone.length() - 4);
        return name + "(" + substring + ")";
    }

    public Vip() {
    }

    public String getPhone() {
        return phone;
    }

    public Vip(String phone, String name, String password) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        balance = 0.0;
        integral = 0;
    }

    public Vip(String phone, String name, String password, Double balance) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.balance = balance;
        integral = 0;
    }

    public Vip(String phone, String name, String password, Double balance, Integer integral) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.integral = integral;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        if (balance == null) {
            return 0.0;
        }
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vip vip = (Vip) o;
        return Objects.equals(phone, vip.phone) && Objects.equals(name, vip.name) && Objects.equals(password, vip.password) && Objects.equals(balance, vip.balance) && Objects.equals(integral, vip.integral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, name, password, balance, integral);
    }

    @Override
    public String toString() {
        return
                "手机号:" + phone + "\n" +
                        "姓名:" + name + "\n" +
                        "余额:" + balance + "元\n" +
                        "积分" + integral
                ;
    }
}
