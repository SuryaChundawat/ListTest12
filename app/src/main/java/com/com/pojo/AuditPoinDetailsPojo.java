package com.com.pojo;

/**
 * Created by Ashitosh on 30-08-2015.
 */
public class AuditPoinDetailsPojo {

    private int index;
    private String audit_point;
    private String remark="nil";
    private String respones_yes_no="Select Respones";
    private String gap_flag;
    private String serial_no;


    @Override
    public String toString() {
        return "AuditPoinDetailsPojo{" +
                "index=" + index +
                ", audit_point='" + audit_point + '\'' +
                ", remark='" + remark + '\'' +
                ", respones_yes_no='" + respones_yes_no + '\'' +
                ", gap_flag='" + gap_flag + '\'' +
                ", serial_no='" + serial_no + '\'' +
                '}';
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }






    public String getGap_flag() {
        return gap_flag;
    }

    public void setGap_flag(String gap_flag) {
        this.gap_flag = gap_flag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAudit_point() {
        return audit_point;
    }

    public void setAudit_point(String audit_point) {
        this.audit_point = audit_point;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRespones_yes_no() {
        return respones_yes_no;
    }

    public void setRespones_yes_no(String respones_yes_no) {
        this.respones_yes_no = respones_yes_no;
    }
}
