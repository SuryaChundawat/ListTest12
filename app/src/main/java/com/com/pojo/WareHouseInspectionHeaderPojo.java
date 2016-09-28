package com.com.pojo;

/**
 * Created by Pratik Mehta on 06-09-2015.
 */
public class WareHouseInspectionHeaderPojo
{
    // InspectionID,SurveyDate,InspectionNo,StateID,LocationID,LocationPinCode,DistrictID,GodownAddress,GodownOwner,CCLocEmpID,BankID,
    // BranchID,SurveyDoneEmpID,DesigID,BorrowerContact,CreatedDate,Concurrency,StateName,RecordCount_InspectionDetails

    private String InspectionID;
    private String SurveyDate;
    private String InspectionNo;
    private String StateID;
    private String LocationID;
    private String LocationPinCode;
    private String DistrictID;
    private String GodownAddress;
    private String GodownOwner;
    private String CCLocEmpID;
    private String BankID;
    private String BranchID;
    private String SurveyDoneEmpID;
    private String DesigID;
    private String BorrowerContact;
    private String CreatedDate;
    private String RecordCount_InspectionDetails;
    private String StatusFlag;
    private String NoOfGodowns;

    @Override
    public String toString()
    {
        return "WareHouseInspectionInformationPojo{" +
                "InspectionID='" + InspectionID + '\'' +
                "SurveyDate='" + SurveyDate + '\'' +
                "InspectionNo='" + InspectionNo + '\'' +
                "StateID='" + StateID + '\'' +
                "LocationID='" + LocationID + '\'' +
                "LocationPinCode='" + LocationPinCode + '\'' +
                "DistrictID='" + DistrictID + '\'' +
                "GodownAddress='" + GodownAddress + '\'' +
                "GodownOwner='" + GodownOwner + '\'' +
                "CCLocEmpID='" + CCLocEmpID + '\'' +
                "BankID='" + BankID + '\'' +
                "BranchID='" + BranchID + '\'' +
                "SurveyDoneEmpID='" + SurveyDoneEmpID + '\'' +
                "DesigID='" + DesigID + '\'' +
                "BorrowerContact='" + BorrowerContact + '\'' +
                "CreatedDate='" + CreatedDate + '\'' +
                "RecordCount_InspectionDetails='" + RecordCount_InspectionDetails + '\'' +
                "StatusFlag='" + StatusFlag + '\'' +
                "NoOfGodowns='" + NoOfGodowns + '\'' +
                '}';
    }

    public String getInspectionID() {
        return InspectionID;
    }

    public void setInspectionID(String InspectionID) {
        this.InspectionID = InspectionID;
    }

    public String getSurveyDate() {
        return SurveyDate;
    }

    public void setSurveyDate(String SurveyDate) {
        this.SurveyDate = SurveyDate;
    }

    public String getInspectionNo() {
        return InspectionNo;
    }

    public void setInspectionNo(String InspectionNo) {
        this.InspectionNo = InspectionNo;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String StateID) {
        this.StateID = StateID;
    }

    public String getLocationID() {
        return LocationID;
    }

    public void setLocationID(String LocationID) {
        this.LocationID = LocationID;
    }

    public String getLocationPinCode() {
        return LocationPinCode;
    }

    public void setLocationPinCode(String LocationPinCode) {
        this.LocationPinCode = LocationPinCode;
    }

    public String getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(String DistrictID) {
        this.DistrictID = DistrictID;
    }

    public String getGodownAddress() {
        return GodownAddress;
    }

    public void setGodownAddress(String GodownAddress) {
        this.GodownAddress = GodownAddress;
    }

    public String getGodownOwner() {
        return GodownOwner;
    }

    public void setGodownOwner(String GodownOwner) {
        this.GodownOwner = GodownOwner;
    }

    public String getCCLocEmpID() {
        return CCLocEmpID;
    }

    public void setCCLocEmpID(String CCLocEmpID) {
        this.CCLocEmpID = CCLocEmpID;
    }

    public String getBankID() {
        return BankID;
    }

    public void setBankID(String BankID) {
        this.BankID = BankID;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String BranchID) {
        this.BranchID = BranchID;
    }

    public String getSurveyDoneEmpID() {
        return SurveyDoneEmpID;
    }

    public void setSurveyDoneEmpID(String SurveyDoneEmpID) {
        this.SurveyDoneEmpID = SurveyDoneEmpID;
    }

    public String getDesigID() {
        return DesigID;
    }

    public void setDesigID(String DesigID) {
        this.DesigID = DesigID;
    }

    public String getBorrowerContact() {
        return BorrowerContact;
    }

    public void setBorrowerContact(String BorrowerContact) {
        this.BorrowerContact = BorrowerContact;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getRecordCount_InspectionDetails() {
        return RecordCount_InspectionDetails;
    }

    public void setRecordCount_InspectionDetails(String RecordCount_InspectionDetails) {
        this.RecordCount_InspectionDetails = RecordCount_InspectionDetails;
    }

    public String getStatusFlag() {
        return StatusFlag;
    }

    public void setStatusFlag(String StatusFlag) {
        this.StatusFlag = StatusFlag;
    }

    public String getNoOfGodowns() {
        return NoOfGodowns;
    }

    public void setNoOfGodowns(String NoOfGodowns) {
        this.NoOfGodowns = NoOfGodowns;
    }


}
