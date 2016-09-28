package com.com.pojo;

/**
 * Created by Ashitosh on 27-09-2015.
 */
public class BankPojo {
    private String banck_id;
    private String banck_name;

    @Override
    public String toString() {
        return "BankPojo{" +
                "banck_id='" + banck_id + '\'' +
                ", banck_name='" + banck_name + '\'' +
                '}';
    }

    public String getBanck_id() {
        return banck_id;
    }

    public void setBanck_id(String banck_id) {
        this.banck_id = banck_id;
    }

    public String getBanck_name() {
        return banck_name;
    }

    public void setBanck_name(String banck_name) {
        this.banck_name = banck_name;
    }
}
