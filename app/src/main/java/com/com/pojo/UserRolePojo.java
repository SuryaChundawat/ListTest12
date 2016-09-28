package com.com.pojo;

/**
 * Created by Codeginger on 29/11/2015.
 */
public class UserRolePojo
{
    private String condition;
    private String FormFileName;


    @Override
    public String toString()
    {
        return "UserRolePojo{" +
                "condition='" + condition + '\'' +
                ", FormFileName='" + FormFileName + '\'' +
                '}';
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getFormFileName() {
        return FormFileName;
    }

    public void setFormFileName(String FormFileName) {
        this.FormFileName = FormFileName;
    }
}
