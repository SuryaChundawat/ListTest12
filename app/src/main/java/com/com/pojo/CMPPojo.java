package com.com.pojo;

/**
 * Created by Admin on 5/4/2016.
 */
public class CMPPojo {

    private String CMP_id;
    private String CMP_name;
    private String Location_id;
    private String Concurrency;

    @Override

    public String toString(){

        return "CMPpojo{" +
                "CMP_id='" + CMP_id + '\'' +
                ", CMP_name='" + CMP_name + '\'' +
                ", Location_id='" + Location_id + '\'' +
                ", Concurrency='" + Concurrency + '\'' +
                '}';
    }

    public String getCMP_id(){
        return CMP_id;
    }

    public void setCMP_id(String CMP_id){
        this.CMP_id=CMP_id;
    }

    public String getCMP_name() {
        return CMP_name;
    }

    public void setCMP_name(String CMP_name) {
        this.CMP_name = CMP_name;
    }

    public String getLocation_id(){
        return Location_id;
    }

    public void setLocation_id(String Location_id){
        this.Location_id=Location_id;
    }

    public String getConcurrency() {
        return Concurrency;
    }

    public void setConcurrency(String Concurrency) {
        this.Concurrency = Concurrency;
    }

}
