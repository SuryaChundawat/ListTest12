package com.com.pojo;

/**
 * Created by Ashitosh on 06-09-2015.
 */
public class WareHousePojo {

    private String warehouse_id;
    private String warehouse_name;
    private String warehouse_address;
    private String location;
    private String location_id;
    private String cmAndcmp;

    @Override
    public String toString() {
        return "WareHousePojo{" +
                "warehouse_id='" + warehouse_id + '\'' +
                ", warehouse_name='" + warehouse_name + '\'' +
                ", warehouse_address='" + warehouse_address + '\'' +
                ", location='" + location + '\'' +
                ", location_id='" + location_id + '\'' +
                ", cmAndcmp='" + cmAndcmp + '\'' +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getCmAndcmp() {
        return cmAndcmp;
    }

    public void setCmAndcmp(String cmAndcmp) {
        this.cmAndcmp = cmAndcmp;
    }

    public String getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getWarehouse_name() {
        return warehouse_name;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

    public String getWarehouse_address() {
        return warehouse_address;
    }

    public void setWarehouse_address(String warehouse_address) {
        this.warehouse_address = warehouse_address;
    }
}
