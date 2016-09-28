package com.com.pojo;

/**
 * Created by Ashitosh on 06-09-2015.
 */
public class AuditPointDetailsServerPojo {

    private String audit_point_id;
    private String audit_point_name;
    private String audit_type_id;
    private String segment_name;
    private String segment_id;
    private String serial_no;


    @Override
    public String toString() {
        return "AuditPointDetailsServerPojo{" +
                "audit_point_id='" + audit_point_id + '\'' +
                ", audit_point_name='" + audit_point_name + '\'' +
                ", audit_type_id='" + audit_type_id + '\'' +
                ", segment_name='" + segment_name + '\'' +
                ", segment_id='" + segment_id + '\'' +
                ", serial_no='" + serial_no + '\'' +
                ", gap_flag='" + gap_flag + '\'' +
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

    private String gap_flag;



    public String getAudit_type_id() {
        return audit_type_id;
    }

    public void setAudit_type_id(String audit_type_id) {
        this.audit_type_id = audit_type_id;
    }

    public String getSegment_name() {
        return segment_name;
    }

    public void setSegment_name(String segment_name) {
        this.segment_name = segment_name;
    }

    public String getSegment_id() {
        return segment_id;
    }

    public void setSegment_id(String segment_id) {
        this.segment_id = segment_id;
    }

    public String getAudit_point_id() {
        return audit_point_id;
    }

    public void setAudit_point_id(String audit_point_id) {
        this.audit_point_id = audit_point_id;
    }

    public String getAudit_point_name() {
        return audit_point_name;
    }

    public void setAudit_point_name(String audit_point_name) {
        this.audit_point_name = audit_point_name;
    }
}
