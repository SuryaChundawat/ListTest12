package com.com.pojo;

/**
 * Created by Pratik Mehta on 06-09-2015.
 */
public class WareHouseInspectionInformationPojo
{
    //InspectionID	InspectionNo	GodownAddress	LocationID	NoOfGodowns	BankID
    // BankRecReqDate	Remarks	Concurrency	StatusID	StateID	DistrictID	RecordCount_InspectionDetails	StateName

    private String InspectionID;
    private String InspectionNo;
    private String GodownAddress;
    private String LocationID;
    private String NoOfGodowns;
    private String BankID;
    private String BankRecReqDate;
    private String Remarks;
    private String Concurrency;
    private String StatusID;
    private String StateID;
    private String DistrictID;
    private String RecordCount_InspectionDetails;
    private String StateName;

    @Override
    public String toString()
    {
        return "WareHouseInspectionInformationPojo{" +
                "InspectionID='" + InspectionID + '\'' +
                ", InspectionNo='" + InspectionNo + '\'' +
                ", GodownAddress='" + GodownAddress + '\'' +
                ", LocationID='" + LocationID + '\'' +
                ", NoOfGodowns='" + NoOfGodowns + '\'' +
                ", BankID='" + BankID + '\'' +
                ", BankRecReqDate='" + BankRecReqDate + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", Concurrency='" + Concurrency + '\'' +
                ", StatusID='" + StatusID + '\'' +
                ", StateID='" + StateID + '\'' +
                ", DistrictID='" + DistrictID + '\'' +
                ", RecordCount_InspectionDetails='" + RecordCount_InspectionDetails + '\'' +
                ", StateName='" + StateName + '\'' +
                '}';
    }

    public String getInspectionID() {
        return InspectionID;
    }

    public void setInspectionID(String InspectionID) {
        this.InspectionID = InspectionID;
    }


    public String getInspectionNo() {
        return InspectionNo;
    }

    public void setInspectionNo(String InspectionNo) {
        this.InspectionNo = InspectionNo;
    }


    public String getGodownAddress() {
        return GodownAddress;
    }

    public void setGodownAddress(String GodownAddress) {
        this.GodownAddress = GodownAddress;
    }


    public String getLocationID() {
        return LocationID;
    }

    public void setLocationID(String LocationID) {
        this.LocationID = LocationID;
    }


    public String getNoOfGodowns() {
        return NoOfGodowns;
    }

    public void setNoOfGodowns(String NoOfGodowns) {
        this.NoOfGodowns = NoOfGodowns;
    }


    public String getBankID() {
        return BankID;
    }

    public void setBankID(String BankID) {
        this.BankID = BankID;
    }


    public String getBankRecReqDate() {
        return BankRecReqDate;
    }

    public void setBankRecReqDate(String BankRecReqDate) {
        this.BankRecReqDate = BankRecReqDate;
    }


    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }


    public String getConcurrency() {
        return Concurrency;
    }

    public void setConcurrency(String Concurrency) {
        this.Concurrency = Concurrency;
    }


    public String getStatusID() {
        return StatusID;
    }

    public void setStatusID(String StatusID) {
        this.StatusID = StatusID;
    }


    public String getStateID() {
        return StateID;
    }

    public void setStateID(String StateID) {
        this.StateID = StateID;
    }


    public String getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(String DistrictID) {
        this.DistrictID = DistrictID;
    }


    public String getRecordCount_InspectionDetails() {
        return RecordCount_InspectionDetails;
    }

    public void setRecordCount_InspectionDetails(String RecordCount_InspectionDetails) {
        this.RecordCount_InspectionDetails = RecordCount_InspectionDetails;
    }


    public String getStateName() {
        return StateName;
    }

    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

}
