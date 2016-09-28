package com.com.pojo;

/**
 * Created by Ashitosh on 26-09-2015.
 */
public class WareHouseInspectionPojo {


    private  String particular;
    private  int particular_id;
    private  int particular_sqe_no;
    private  String name;
    private  String name_id;
    private  String name_sqe_no;
    private  String boolean_validation;
    private  String date;
    private  String values;
    private  String values_mandatory_validation;
    private  String multi_select_validation;
    private  String number_validation;
    private  String text_validation;
    private  String grp_header;
    private  String grp_header_id;
    private  String master_item_type_it;

    @Override
    public String toString() {
        return "WareHouseInspectionPojo{" +
                "particular='" + particular + '\'' +
                ", particular_id='" + particular_id + '\'' +
                ", particular_sqe_no='" + particular_sqe_no + '\'' +
                ", name='" + name + '\'' +
                ", name_id='" + name_id + '\'' +
                ", name_sqe_no='" + name_sqe_no + '\'' +
                ", boolean_validation='" + boolean_validation + '\'' +
                ", date='" + date + '\'' +
                ", values='" + values + '\'' +
                ", values_mandatory_validation='" + values_mandatory_validation + '\'' +
                ", multi_select_validation='" + multi_select_validation + '\'' +
                ", number_validation='" + number_validation + '\'' +
                ", text_validation='" + text_validation + '\'' +
                ", grp_header='" + grp_header + '\'' +
                ", grp_header_id='" + grp_header_id + '\'' +
                ", master_item_type_it='" + master_item_type_it + '\'' +
                '}';
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public int getParticular_id() {
        return particular_id;
    }

    public void setParticular_id(int particular_id) {
        this.particular_id = particular_id;
    }

    public int getParticular_sqe_no() {
        return particular_sqe_no;
    }

    public void setParticular_sqe_no(int particular_sqe_no) {
        this.particular_sqe_no = particular_sqe_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_id() {
        return name_id;
    }

    public void setName_id(String name_id) {
        this.name_id = name_id;
    }

    public String getName_sqe_no() {
        return name_sqe_no;
    }

    public void setName_sqe_no(String name_sqe_no) {
        this.name_sqe_no = name_sqe_no;
    }

    public String getBoolean_validation() {
        return boolean_validation;
    }

    public void setBoolean_validation(String boolean_validation) {
        this.boolean_validation = boolean_validation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getValues_mandatory_validation() {
        return values_mandatory_validation;
    }

    public void setValues_mandatory_validation(String values_mandatory_validation) {
        this.values_mandatory_validation = values_mandatory_validation;
    }

    public String getMulti_select_validation() {
        return multi_select_validation;
    }

    public void setMulti_select_validation(String multi_select_validation) {
        this.multi_select_validation = multi_select_validation;
    }

    public String getNumber_validation() {
        return number_validation;
    }

    public void setNumber_validation(String number_validation) {
        this.number_validation = number_validation;
    }

    public String getText_validation() {
        return text_validation;
    }

    public void setText_validation(String text_validation) {
        this.text_validation = text_validation;
    }

    public String getGrp_header() {
        return grp_header;
    }

    public void setGrp_header(String grp_header) {
        this.grp_header = grp_header;
    }

    public String getGrp_header_id() {
        return grp_header_id;
    }

    public void setGrp_header_id(String grp_header_id) {
        this.grp_header_id = grp_header_id;
    }

    public String getMaster_item_type_it() {
        return master_item_type_it;
    }

    public void setMaster_item_type_it(String master_item_type_it) {
        this.master_item_type_it = master_item_type_it;
    }
}
