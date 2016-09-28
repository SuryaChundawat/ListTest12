package com.com.pojo;

/**
 * Created by Ashitosh on 06-09-2015.
 */
public class GodownCmAndCmpPojo {

    private String godown_name;
    private String godown_Address;
    private String godown_id;
    private String location_id;
    private String is_godown_map;
    private String WarehouseId;
    private String WarehouseName;
    private String WarehouseCloseDate;
    private String GodownCloseDAte;


    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    @Override
    public String toString() {
        return "GodownCmAndCmpPojo{" +
                "godown_name='" + godown_name + '\'' +
                ", godown_Address='" + godown_Address + '\'' +
                ", godown_id='" + godown_id + '\'' +
                ", location_id='" + location_id + '\'' +
                ", is_godown_map='" + is_godown_map + '\'' +
                ", WarehouseId='" + WarehouseId + '\'' +
                ", WarehouseName='" + WarehouseName + '\'' +
                ", WarehouseCloseDate='" + WarehouseCloseDate + '\'' +
                ", GodownCloseDAte='" + GodownCloseDAte + '\'' +
                '}';
    }

    public String getIs_godown_map()
    {
        return is_godown_map;
    }

    public void setIs_godown_map(String is_godown_map)
    {
        this.is_godown_map = is_godown_map;
    }

    public String getGodown_id()
    {
        return godown_id;
    }

    public void setGodown_id(String godown_id)
    {
        this.godown_id = godown_id;
    }

    public String getGodown_name()
    {
        return godown_name;
    }

    public void setGodown_name(String godown_name)
    {
        this.godown_name = godown_name;
    }

    public String getGodown_Address()
    {
        return godown_Address;
    }

    public void setGodown_Address(String godown_Address)
    {
        this.godown_Address = godown_Address;
    }

    public String getWarehouseId()
    {
        return WarehouseId;
    }

    public void setWarehouseId(String WarehouseId)
    {
        this.WarehouseId = WarehouseId;
    }

    public String getWarehouseName()
    {
        return WarehouseName;
    }

    public void setWarehouseName(String WarehouseName)
    {
        this.WarehouseName = WarehouseName;
    }

    public String getWarehouseCloseDate()
    {
        return WarehouseCloseDate;
    }

    public void setWarehouseCloseDate(String WarehouseCloseDate)
    {
        this.WarehouseCloseDate = WarehouseCloseDate;
    }


    public String getGodownCloseDAte()
    {
        return GodownCloseDAte;
    }

    public void setGodownCloseDAte(String GodownCloseDAte)
    {
        this.GodownCloseDAte = GodownCloseDAte;
    }


}
