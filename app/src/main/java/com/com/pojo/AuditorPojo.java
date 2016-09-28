package com.com.pojo;

/**
 * Created by Ashitosh on 06-09-2015.
 */
public class AuditorPojo {
    private String emp_num;
    private String emp_full_name;

    @Override
    public String toString() {
        return "AuditorPojo{" +
                "emp_num='" + emp_num + '\'' +
                ", emp_full_name='" + emp_full_name + '\'' +
                '}';
    }

    public String getEmp_num() {
        return emp_num;
    }

    public void setEmp_num(String emp_num) {
        this.emp_num = emp_num;
    }

    public String getEmp_full_name() {
        return emp_full_name;
    }

    public void setEmp_full_name(String emp_full_name) {
        this.emp_full_name = emp_full_name;
    }
}
