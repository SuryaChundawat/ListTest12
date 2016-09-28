package com.com.pojo;

/**
 * Created by Ashitosh on 06-09-2015.
 */
public class LocationCmAndCmpPojo {
    private String location_id;
    private String location_name;
    private String state;
    private String state_id;
    private String district_id;

    @Override
    public String toString() {
        return "LocationCmAndCmpPojo{" +
                "location_id='" + location_id + '\'' +
                ", location_name='" + location_name + '\'' +
                ", state='" + state + '\'' +
                ", state_id='" + state_id + '\'' +
                ", district_id='" + district_id + '\'' +
                '}';
    }

    public String getState() {


        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_Id() {
        return district_id;
    }

    public void setDistrict_Id(String district_id) {
        this.district_id = district_id;
    }
}
