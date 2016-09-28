package com.com.pojo;

/**
 * Created by Ashitosh on 26-09-2015.
 */
public class WareHouseInspectionDynamicPojo {

    private String particular;
    private String name;
    private String values;
    private String remark;
    private String type_flag;

    public String getType_flag() {
        return type_flag;
    }

    public void setType_flag(String type_flag) {
        this.type_flag = type_flag;
    }

    @Override
    public String toString() {
        return "WareHouseInspectionDynamicPojo{" +
                "particular='" + particular + '\'' +
                ", name='" + name + '\'' +
                ", values='" + values + '\'' +
                ", remark='" + remark + '\'' +
                ", type_flag='" + type_flag + '\'' +
                '}';
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
