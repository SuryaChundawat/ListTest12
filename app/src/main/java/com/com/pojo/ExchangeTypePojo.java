package com.com.pojo;

/**
 * Created by Rajesh on 2/5/2016.
 */
public class ExchangeTypePojo {

    private String exchange_type_id;
    private String exchange_type_name;

    @Override

    public String toString(){

        return "Exchangetypepojo{" +
                "exchange_type_id='" + exchange_type_id + '\'' +
                ", exchange_type_name='" + exchange_type_name + '\'' +
                '}';
    }

    public String getExchange_type_id(){
        return exchange_type_id;
    }

    public void setExchange_type_id(String exchange_type_id){
        this.exchange_type_id=exchange_type_id;
    }

    public String getExchange_type_name() {
        return exchange_type_name;
    }

    public void setExchange_type_name(String exchange_type_name) {
        this.exchange_type_name = exchange_type_name;
    }
}
