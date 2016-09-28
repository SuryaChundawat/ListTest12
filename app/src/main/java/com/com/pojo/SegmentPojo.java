package com.com.pojo;

/**
 * Created by Ashitosh on 06-09-2015.
 */
public class SegmentPojo {


    private String segment_name;
    private String segment_id;

    public String getSegment_id() {
        return segment_id;
    }

    public void setSegment_id(String segment_id) {
        this.segment_id = segment_id;
    }


    @Override
    public String toString() {
        return "SegmentPojo{" +
                "segment_name='" + segment_name + '\'' +
                ", segment_id='" + segment_id + '\'' +
                '}';
    }

    public String getSegment_name() {
        return segment_name;
    }

    public void setSegment_name(String segment_name) {
        this.segment_name = segment_name;
    }
}
