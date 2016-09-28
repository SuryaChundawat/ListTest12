package com.com.pojo;

/**
 * Created by Ashitosh on 02-10-2015.
 */
public class DistrictPojo {

    private String district_id;
    private String district_name;
    private String state_id;
    private String segment_name;

    @Override
    public String toString() {
        return "DistrictPojo{" +
                "district_id='" + district_id + '\'' +
                ", district_name='" + district_name + '\'' +
                ", state_id='" + state_id + '\'' +
                ", district_id='" + segment_name + '\'' +
                '}';
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getSegment_Name() {
        return segment_name;
    }

    public void setSegment_Name(String segment_name) {
        this.segment_name = segment_name;
    }
}
