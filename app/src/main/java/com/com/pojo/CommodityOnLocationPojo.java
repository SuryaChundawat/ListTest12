package com.com.pojo;

/**
 * Created by CODEGINGER on 27/05/2016.
 */

public class CommodityOnLocationPojo {

    private String Commodity;
    private String CommodityId;
    private String ExchangeType;
    private String ExchangeTypeId;
    private String Location;
    private String LocationId;
    private String StateId;
    private String StateName;


    @Override

    public String toString(){

        return "CommodityOnLocationPojo{" +
                "Commodity='" + Commodity + '\'' +
                ", CommodityId='" + CommodityId + '\'' +
                ", ExchangeType='" + ExchangeType + '\'' +
                ", ExchangeTypeId='" + ExchangeTypeId + '\'' +
                ", Location='" + Location + '\'' +
                ", LocationId='" + LocationId + '\'' +
                ", StateName='" + StateName + '\'' +
                ", StateId='" + StateId + '\'' +
                '}';
    }

    public String getCommodity(){
        return Commodity;
    }

    public void setCommodity(String Commodity){
        this.Commodity=Commodity;
    }

    public String getCommodityId() {
        return CommodityId;
    }

    public void setCommodityId(String CommodityId) {
        this.CommodityId = CommodityId;
    }

    public String getExchangeType(){
        return ExchangeType;
    }

    public void setExchangeType(String ExchangeType){
        this.ExchangeType=ExchangeType;
    }

    public String getExchangeTypeId() {
        return ExchangeTypeId;
    }

    public void setExchangeTypeId(String ExchangeTypeId) {
        this.ExchangeTypeId = ExchangeTypeId;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getLocationId() {
        return LocationId;
    }

    public void setLocationId(String LocationId) {
        this.LocationId = LocationId;
    }

    public String getStateId() {
        return StateId;
    }

    public void setStateId(String StateId) {
        this.StateId = StateId;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

}