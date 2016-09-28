package com.com.pojo;

/**
 * Created by Ashitosh on 19-09-2015.
 */
public class StatePojo {
    private String state_id;
    private String state_name;
    private String segment;
    private String segment_id;

    @Override
    public String toString() {
        return "StatePojo{" +
                "state_id='" + state_id + '\'' +
                ", state_name='" + state_name + '\'' +
                ", segment='" + segment + '\'' +
                ", segment_id='" + segment_id + '\'' +
                '}';
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getSegment_id() {
        return segment_id;
    }

    public void setSegment_id(String segment_id) {
        this.segment_id = segment_id;
    }
}
