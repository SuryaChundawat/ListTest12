package com.com.pojo;

/**
 * Created by Ashitosh on 27-09-2015.
 */
public class BranchPojo {

    private String bank_id;
    private String branch_id;
    private String branch_name;

    @Override
    public String toString() {
        return "BranchPojo{" +
                "bank_id='" + bank_id + '\'' +
                ", branch_id='" + branch_id + '\'' +
                ", branch_name='" + branch_name + '\'' +
                '}';
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }
}
