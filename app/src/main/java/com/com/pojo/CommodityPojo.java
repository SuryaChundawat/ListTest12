package com.com.pojo;

/**
 * Created by Admin on 5/4/2016.
 */
public class CommodityPojo {

    private String commodity_id;
    private String commodity_name;
    private String commodity_exchange_type_id;
    private String commodity_exchange_type_name;

    @Override

    public String toString(){

        return "Commoditypojo{" +
                "commodity_id='" + commodity_id + '\'' +
                ", commodity_name='" + commodity_name + '\'' +
                ", commodity_exchange_type_id='" + commodity_exchange_type_id + '\'' +
                ", commodity_exchange_type_name='" + commodity_exchange_type_name + '\'' +
                '}';
    }

    public String getCommodity_id(){
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id){
        this.commodity_id=commodity_id;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public String getCommodity_exchange_type_id(){
        return commodity_exchange_type_id;
    }

    public void setCommodity_exchange_type_id(String commodity_exchange_type_id){
        this.commodity_exchange_type_id=commodity_exchange_type_id;
    }

    public String getCommodity_exchange_type_name() {
        return commodity_exchange_type_name;
    }

    public void setCommodity_exchange_type_name(String commodity_exchange_type_name) {
        this.commodity_exchange_type_name = commodity_exchange_type_name;
    }

}
