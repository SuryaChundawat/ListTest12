package com.com.pojo;

/**
 * Created by Ashitosh on 06-09-2015.
 */
public class AuditTypePojo {

    private String audit_type_id;
    private String audit_type;
    private String segment_id;

    @Override
    public String toString() {
        return "AuditTypePojo{" +
                "audit_type_id='" + audit_type_id + '\'' +
                ", audit_type='" + audit_type + '\'' +
                ", segment_id='" + segment_id + '\'' +
                '}';
    }

    public String getSegment_id() {
        return segment_id;
    }

    public void setSegment_id(String segment_id) {
        this.segment_id = segment_id;
    }

    public String getAudit_type() {
        return audit_type;
    }

    public void setAudit_type(String audit_type) {
        this.audit_type = audit_type;
    }

    public String getAudit_type_id() {
        return audit_type_id;
    }

    public void setAudit_type_id(String audit_type_id) {
        this.audit_type_id = audit_type_id;
    }
}
