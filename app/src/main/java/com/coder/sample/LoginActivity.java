package com.coder.sample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.com.pojo.AuditPointDetailsServerPojo;
import com.com.pojo.AuditTypePojo;
import com.com.pojo.AuditorPojo;
import com.com.pojo.BankPojo;
import com.com.pojo.BranchPojo;
import com.com.pojo.CMPPojo;
import com.com.pojo.CommodityOnLocationPojo;
import com.com.pojo.CommodityPojo;
import com.com.pojo.DistrictPojo;
import com.com.pojo.ExchangeTypePojo;
import com.com.pojo.GodownCmAndCmpPojo;
import com.com.pojo.LocationCmAndCmpPojo;
import com.com.pojo.SegmentPojo;
import com.com.pojo.StatePojo;
import com.com.pojo.UserRolePojo;
import com.com.pojo.WareHouseInspectionHeaderPojo;
import com.com.pojo.WareHouseInspectionPojo;
import com.com.pojo.WareHousePojo;
import com.database.DbHelper;
import com.forgot.mail.ForgotPassword;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoginActivity extends AppCompatActivity
{
    private TextView textView_imei_no, textView_forgot_password;
    private EditText editText_employee_id, editText_password, editText_mobile_no;
    private ProgressDialog dialog;
    private DbHelper dbHelper;
    private String STATE_NAME, downloadedDatetime;

    private String FINAL_URL, FINAL_URL_WAREHOUSE_INSPECTION, FINAL_URL_BANK, FINAL_URL_DISTRICT, FINAL_URL_AUDIT_TYPE_ADUTI_POINT, FINAL_URL_GODOWN, FINAL_WAREHOUSE_URL, FINAL_USER_ROLE_URL, FINAL_EXCHANGE_TYPE_URL, FINAL_COMMODITY_URL , FINAL_SM_CMP, FINAL_COMMODITY_ONLOCATION_URL;
    public static String EMP_NAME_GLOBAL;
    private Button button_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getRef();

        textView_forgot_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });
    }


    public void loginSubmit(View view)
    {
        button_submit.setEnabled(false);

        if (getIsFirstPref())
        {

            if (editText_employee_id.getText().toString().length() > 0 && editText_password.getText().toString().length() > 0 && editText_mobile_no.getText().toString().length() > 0 && editText_mobile_no.getText().toString().length() == 10)
            {

                if (AppController.getInstance().haveNetworkConnection())
                {

                    locationSetUp();

                    if(!STATE_NAME.equals(""))
                    {
                        checkAuthentication();
                    }
                    else
                    {
                        //Edited by surya
                        Toast.makeText(getApplicationContext(),"Please Check GPS",Toast.LENGTH_SHORT).show();
                        AlertDialog alertbox = new AlertDialog.Builder(this)
                                .setMessage("Internet / GPS is Not Detected.\nPlease Turn ON Internet / GPS and try again.")
                                .setPositiveButton("OK", null)
                                .show();
                        button_submit.setEnabled(true);
                    }

                    /*if (checkAuthentication()) {
                        putIsFirstPref(false);
                        putMobileNoPref(editText_mobile_no.getText().toString().trim());

                        dbHelper.putUserLogin(editText_mobile_no.getText().toString().trim(), textView_imei_no.getText().toString().trim(), editText_employee_id.getText().toString().trim(), editText_password.getText().toString().trim(),STATE_NAME);

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("emp_id", editText_employee_id.getText().toString().trim()));
                        editText_employee_id.setText("");
                        editText_password.setText("");
                    } else {
                        editText_employee_id.setText("");
                        editText_password.setText("");
                        editText_mobile_no.setText("");
                        Toast.makeText(LoginActivity.this, "Invalid Employee ID and Password and Mobile Number.", Toast.LENGTH_LONG).show();
                    }*/

                }
                else
                {
                    button_submit.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Please turn on internet connection.", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                if (!(editText_mobile_no.getText().toString().length() > 0) || !(editText_mobile_no.getText().toString().length() == 10))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_LONG).show();
                }
                else if (!(editText_employee_id.getText().toString().length() > 0))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Employee ID", Toast.LENGTH_LONG).show();
                }
                else if (!(editText_password.getText().toString().length() > 0))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter valid values", Toast.LENGTH_LONG).show();
                }
                button_submit.setEnabled(true);
            }
        }
        else
        {
            if (editText_employee_id.getText().toString().length() > 0 && editText_password.getText().toString().length() > 0)
            {
                if (dbHelper.checkUserLogin(editText_employee_id.getText().toString(), editText_password.getText().toString()))
                {

                    EMP_NAME_GLOBAL = getEmpNameGlobal();

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("emp_id", editText_employee_id.getText().toString()));
                    editText_employee_id.setText("");
                    editText_password.setText("");
                    button_submit.setEnabled(true);
                }
                else
                {
                    editText_employee_id.setText("");
                    editText_password.setText("");
                    button_submit.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Invalid Employee ID and Password.", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                if (!(editText_employee_id.getText().toString().length() > 0))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Employee ID", Toast.LENGTH_LONG).show();
                }
                else if (!(editText_password.getText().toString().length() > 0))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter valid values", Toast.LENGTH_LONG).show();
                }
                button_submit.setEnabled(true);
            }
        }
    }

    private void locationSetUp()
    {
        GPSTracker gps = new GPSTracker(LoginActivity.this, 10);
        // check if GPS enabled
        if (gps.canGetLocation())
        {
            STATE_NAME = gps.getState_name();
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
        }
    }

    private void checkAuthentication()
    {
        // locationSetUp();
        dialogShow();
        //http://test.4parents.mobi/Login/{SIM_Card_No}/{IME_NO}/{Employee_Id}/{Password}/{state}
        dbHelper.deleteAllTable();
        // Downloaded DateTime
        downloadedDatetime = getDateTime();
        // Special Character Validation in Password
        String EmployeePassword = editText_password.getText().toString();
        if(EmployeePassword.contains("#") || EmployeePassword.contains("%") || EmployeePassword.contains("\\") || EmployeePassword.contains("+") ||
                EmployeePassword.contains(".") || EmployeePassword.contains("/") || EmployeePassword.contains("?"))
        {
            if(EmployeePassword.contains("%"))
                EmployeePassword = EmployeePassword.replace("%", "}25987");
            if(EmployeePassword.contains("#"))
                EmployeePassword = EmployeePassword.replace("#", "}23987");
            //if(EmployeePassword.contains("\\"))
            //    EmployeePassword = EmployeePassword.replace("\\", "}5C987");
            if(EmployeePassword.contains("+"))
                EmployeePassword = EmployeePassword.replace("+","}2B987");
            if(EmployeePassword.contains("."))
                EmployeePassword = EmployeePassword.replace(".","}2E987");
            if(EmployeePassword.contains("/"))
                EmployeePassword = EmployeePassword.replace("/","}2F987");
            if(EmployeePassword.contains("?"))
                EmployeePassword = EmployeePassword.replace("?","}3F987");
        }
        // StateName
        if(STATE_NAME.contains(" "))
            STATE_NAME = STATE_NAME.toString().replace(" ","%20");

        FINAL_URL = HelperStatic.NEW_LOGIN_URL
                + editText_mobile_no.getText().toString()
                + "/" + textView_imei_no.getText().toString().trim()
                + "/" + editText_employee_id.getText().toString()
                + "/" + EmployeePassword
                + "/" + STATE_NAME
                + "/" + "Login"
                + "/" + "NULL";


        Log.e("Login Url", " url = " + FINAL_URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject child = response.getJSONObject("LoginResult");

                    String auth = child.getString("authenticate");

                    if (auth.contains("true"))
                    {

                        String emp_name = child.getString("Employee_name");
                        String emp_pwd = child.getString("Password");
                        String emp_num = child.getString("EmpployeeNo");
                        String designation_name = child.getString("DesignationName");
                        String designation_id = child.getString("DesignationID");
                        //String state_id_cm = child.getString("SMP_Stateid");
                        //String state_id_cmp = child.getString("CMP_Stateid");

                        ArrayList<AuditorPojo> auditor_list = new ArrayList<AuditorPojo>();
                        ArrayList<LocationCmAndCmpPojo> location_cm_list = new ArrayList<LocationCmAndCmpPojo>();
                        ArrayList<LocationCmAndCmpPojo> location_cmp_list = new ArrayList<LocationCmAndCmpPojo>();
                        ArrayList<LocationCmAndCmpPojo> location_HR_list = new ArrayList<LocationCmAndCmpPojo>();
                        ArrayList<SegmentPojo> segment_list = new ArrayList<SegmentPojo>();
                        ArrayList<StatePojo> state_cm_list = new ArrayList<StatePojo>();
                        ArrayList<StatePojo> state_cmp_list = new ArrayList<StatePojo>();
                        ArrayList<StatePojo> state_HR_list = new ArrayList<StatePojo>();

                        AuditorPojo pojo_auditor = new AuditorPojo();


                        pojo_auditor.setEmp_num(emp_num);
                        pojo_auditor.setEmp_full_name(emp_name);

                        auditor_list.add(pojo_auditor);

                        pojo_auditor = null;



                        /*JSONArray commonJsonArray = child.getJSONArray("Auditor_ls");


                        for (int i = 0; i < commonJsonArray.length(); i++) {
                            AuditorPojo pojo = new AuditorPojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setEmp_num(jsonObject.getString("EmpployeeNo"));
                            pojo.setEmp_full_name(jsonObject.getString("FullName"));

                            auditor_list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }

                        commonJsonArray = null;
*/

                        JSONArray commonJsonArray = child.getJSONArray("ls_state_CM");
                        for (int i = 0; i < commonJsonArray.length(); i++)
                        {
                            StatePojo pojo = new StatePojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setSegment_id(jsonObject.getString("BusinessSegmentid"));
                            pojo.setSegment(jsonObject.getString("SegmentName"));
                            pojo.setState_id(jsonObject.getString("StateID"));
                            pojo.setState_name(jsonObject.getString("StateName"));

                            state_cm_list.add(pojo);

                            pojo = null;
                            jsonObject = null;
                        }
                        commonJsonArray = null;

                        commonJsonArray = child.getJSONArray("ls_state_CMP");
                        for (int i = 0; i < commonJsonArray.length(); i++)
                        {
                            StatePojo pojo = new StatePojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setSegment_id(jsonObject.getString("BusinessSegmentid"));
                            pojo.setSegment(jsonObject.getString("SegmentName"));
                            pojo.setState_id(jsonObject.getString("StateID"));
                            pojo.setState_name(jsonObject.getString("StateName"));

                            state_cmp_list.add(pojo);

                            pojo = null;
                            jsonObject = null;
                        }
                        commonJsonArray = null;

                        commonJsonArray = child.getJSONArray("ls_state_HR");
                        for (int i = 0; i < commonJsonArray.length(); i++)
                        {
                            StatePojo pojo = new StatePojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setSegment_id(jsonObject.getString("BusinessSegmentid"));
                            pojo.setSegment(jsonObject.getString("SegmentName"));
                            pojo.setState_id(jsonObject.getString("StateID"));
                            pojo.setState_name(jsonObject.getString("StateName"));

                            state_HR_list.add(pojo);

                            pojo = null;
                            jsonObject = null;
                        }
                        commonJsonArray = null;


                        dbHelper.insertState(state_cm_list);
                        dbHelper.insertState(state_cmp_list);
                        dbHelper.insertState(state_HR_list);
                        Log.e("Insert Table : ", " state_cm_list = " + state_cm_list.size());
                        Log.e("Insert Table : ", " state_cmp_list = " + state_cmp_list.size());
                        Log.e("Insert Table : ", " state_HR_list = " + state_HR_list.size());


                        commonJsonArray = child.getJSONArray("Location_cmp");
                        for (int i = 0; i < commonJsonArray.length(); i++)
                        {
                            LocationCmAndCmpPojo pojo = new LocationCmAndCmpPojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setLocation_id(jsonObject.getString("locationid"));
                            pojo.setLocation_name(jsonObject.getString("LocationName"));
                            pojo.setState_id(jsonObject.getString("stateid"));
                            pojo.setState(dbHelper.getStateNameUsingId(jsonObject.getString("stateid")));
                            pojo.setDistrict_Id(jsonObject.getString("DistrictID"));
                            location_cm_list.add(pojo);

                            pojo = null;
                            jsonObject = null;
                        }
                        commonJsonArray = null;

                        commonJsonArray = child.getJSONArray("Location_smp");
                        for (int i = 0; i < commonJsonArray.length(); i++)
                        {
                            LocationCmAndCmpPojo pojo = new LocationCmAndCmpPojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setLocation_id(jsonObject.getString("locationid"));
                            pojo.setLocation_name(jsonObject.getString("LocationName"));
                            pojo.setState_id(jsonObject.getString("stateid"));
                            pojo.setState(dbHelper.getStateNameUsingId(jsonObject.getString("stateid")));
                            pojo.setDistrict_Id(jsonObject.getString("DistrictID"));
                            location_cmp_list.add(pojo);

                            pojo = null;
                            jsonObject = null;
                        }
                        commonJsonArray = null;

                        commonJsonArray = child.getJSONArray("Location_HR");
                        for (int i = 0; i < commonJsonArray.length(); i++)
                        {
                            LocationCmAndCmpPojo pojo = new LocationCmAndCmpPojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setLocation_id(jsonObject.getString("locationid"));
                            pojo.setLocation_name(jsonObject.getString("LocationName"));
                            pojo.setState_id(jsonObject.getString("stateid"));
                            pojo.setState(dbHelper.getStateNameUsingId(jsonObject.getString("stateid")));
                            pojo.setDistrict_Id(jsonObject.getString("DistrictID"));
                            location_HR_list.add(pojo);

                            pojo = null;
                            jsonObject = null;
                        }
                        commonJsonArray = null;

                        dbHelper.insertLocationTable(location_cm_list, "CM");
                        dbHelper.insertLocationTable(location_cmp_list, "CMP");
                        dbHelper.insertLocationTable(location_HR_list, "HR");
                        Log.e("Insert Table : ", " CM = " + location_cm_list.size());
                        Log.e("Insert Table : ", " CMP = " + location_cmp_list.size());
                        Log.e("Insert Table : ", " HR = " + location_HR_list.size());


                        commonJsonArray = child.getJSONArray("Segment_ls");
                        for (int i = 0; i < commonJsonArray.length(); i++) {
                            SegmentPojo pojo = new SegmentPojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);


                            pojo.setSegment_name(jsonObject.getString("SegmentName"));
                            pojo.setSegment_id(jsonObject.getString("Segmentid"));

                            segment_list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }
                        commonJsonArray = null;


                        dbHelper.isertSegmentTable(segment_list);
                        dbHelper.insertAuditorTable(auditor_list);
                        Log.e("Insert Table : ", " Segment = " + segment_list.size());
                        Log.e("Insert Table : ", " Auditor = " + auditor_list.size());

                        dialog.setMessage("Loading Database 1/14");

                        putEmpNameGlobal(emp_name);
                        putEmpNumberGlobal(emp_num);

                        EMP_NAME_GLOBAL = getEmpNameGlobal();

                        dbHelper.putUserLogin(editText_mobile_no.getText().toString(), textView_imei_no.getText().toString().trim(), editText_employee_id.getText().toString(), emp_pwd, emp_name, emp_num, designation_name, designation_id);

                        getGodown();

                    }
                    else
                    {
                        validInvalidUser(false);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);
                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void getAuditTypeAndAuditPoint()
    {

        FINAL_URL_AUDIT_TYPE_ADUTI_POINT = HelperStatic.AUDIT_POINT_AND_AUDIT_TYPE_URL + "/" + "NULL";;


        Log.e("audit Type and point", " url = " + FINAL_URL_AUDIT_TYPE_ADUTI_POINT);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_AUDIT_TYPE_ADUTI_POINT, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject child = response.getJSONObject("GetAudiTypeResult");

                    ArrayList<AuditTypePojo> audit_type_list = new ArrayList<AuditTypePojo>();
                    ArrayList<AuditPointDetailsServerPojo> audit_point_details_list = new ArrayList<AuditPointDetailsServerPojo>();

                    JSONArray commonJsonArray = child.getJSONArray("ls_Audit_type");


                    for (int i = 0; i < commonJsonArray.length(); i++)
                    {
                        AuditTypePojo pojo = new AuditTypePojo();
                        JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                        pojo.setAudit_type(jsonObject.getString("Audittype"));
                        pojo.setAudit_type_id(jsonObject.getString("AuditTypeID"));
                        pojo.setSegment_id(jsonObject.getString("segmentid"));
                        audit_type_list.add(pojo);

                        pojo = null;
                        jsonObject = null;
                    }


                    commonJsonArray = null;


                    commonJsonArray = child.getJSONArray("audit_point_ls");

                    for (int i = 0; i < commonJsonArray.length(); i++)
                    {
                        AuditPointDetailsServerPojo pojo = new AuditPointDetailsServerPojo();
                        JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                        pojo.setAudit_point_id(jsonObject.getString("audit_point_id"));
                        pojo.setAudit_point_name(jsonObject.getString("audit_point_name"));
                        pojo.setSegment_name(jsonObject.getString("SegmentName"));
                        pojo.setSegment_id(jsonObject.getString("BusinessSegmentID"));
                        pojo.setAudit_type_id(jsonObject.getString("AuditTypeID"));
                        pojo.setGap_flag(jsonObject.getString("GapFlag"));
                        pojo.setSerial_no(jsonObject.getString("SerialNo"));


                        audit_point_details_list.add(pojo);

                        pojo = null;
                        jsonObject = null;
                    }

                    commonJsonArray = null;


                    dbHelper.insertAuditTypeTable(audit_type_list);
                    dbHelper.insertAuditPointDetails(audit_point_details_list);

                    Log.e("Insert Table : ", " AuditType = " + audit_type_list.size());
                    Log.e("Insert Table : ", " AuditPoint = " + audit_point_details_list.size());

                    dialog.setMessage("Loading Database 4/14");

                    getWareHouseInspection();


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }, new Response.ErrorListener()
            {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);
                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void getGodown()
    {

        FINAL_URL_GODOWN = HelperStatic.GODOWN_URL + STATE_NAME + "/" + "NULL";


        Log.e("FINAL_URL_GODOWN Url", " url = " + FINAL_URL_GODOWN);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_GODOWN, "", new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {

                try
                {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject child = response.getJSONObject("GetGodownResult");

                    ArrayList<GodownCmAndCmpPojo> godown_cm_list = new ArrayList<GodownCmAndCmpPojo>();
                    ArrayList<GodownCmAndCmpPojo> godown_cmp_list = new ArrayList<GodownCmAndCmpPojo>();


                    JSONArray commonJsonArray = child.getJSONArray("Godown_cmp");


                    for (int i = 0; i < commonJsonArray.length(); i++)
                    {
                        GodownCmAndCmpPojo pojo = new GodownCmAndCmpPojo();
                        JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                        pojo.setGodown_id(jsonObject.getString("GodownID"));
                        pojo.setGodown_name(jsonObject.getString("GodownCode"));
                        pojo.setGodown_Address(jsonObject.getString("GodownAddress"));
                        pojo.setLocation_id(jsonObject.getString("location_id"));
                        String godown_map_val = jsonObject.getString("IsMappedInMobile");

                        if (godown_map_val.contains("1"))
                        {
                            godown_map_val = "YES";
                        }
                        else
                        {
                            godown_map_val = "NO";
                        }

                        pojo.setIs_godown_map(godown_map_val);
                        pojo.setWarehouseId(jsonObject.getString("WarehouseId"));
                        pojo.setWarehouseName(jsonObject.getString("WarehouseName"));
                        pojo.setWarehouseCloseDate(jsonObject.getString("WarehouseCloseDate"));
                        pojo.setGodownCloseDAte(jsonObject.getString("GodownCloseDAte"));

                        godown_cm_list.add(pojo);

                        pojo = null;
                        jsonObject = null;
                    }

                    commonJsonArray = null;


                    commonJsonArray = child.getJSONArray("Godown_list_smp");

                    for (int i = 0; i < commonJsonArray.length(); i++)
                    {
                        GodownCmAndCmpPojo pojo = new GodownCmAndCmpPojo();
                        JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                        pojo.setGodown_id(jsonObject.getString("GodownID"));
                        pojo.setGodown_name(jsonObject.getString("GodownCode"));
                        pojo.setGodown_Address(jsonObject.getString("GodownAddress"));
                        pojo.setLocation_id(jsonObject.getString("location_id"));
                        String godown_map_val = jsonObject.getString("IsMappedInMobile");
                        if (godown_map_val.contains("1"))
                        {
                            godown_map_val = "YES";
                        }
                        else
                        {
                            godown_map_val = "NO";
                        }
                        pojo.setIs_godown_map(godown_map_val);
                        pojo.setWarehouseId(jsonObject.getString("WarehouseId"));
                        pojo.setWarehouseName(jsonObject.getString("WarehouseName"));
                        pojo.setWarehouseCloseDate(jsonObject.getString("WarehouseCloseDate"));
                        pojo.setGodownCloseDAte(jsonObject.getString("GodownCloseDAte"));

                        godown_cmp_list.add(pojo);

                        pojo = null;
                        jsonObject = null;
                    }

                    commonJsonArray = null;


                    dbHelper.insertGodownTable(godown_cm_list, "CM");
                    dbHelper.insertGodownTable(godown_cmp_list, "CMP");
                    Log.e("Insert Table : ", " GodownTable CM = " + godown_cm_list.size());
                    Log.e("Insert Table : ", " GodownTable CMP = " + godown_cmp_list.size());

                    dialog.setMessage("Loading Database 2/14");

                    getWareHouse();

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);
                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void getWareHouse()
    {


        FINAL_WAREHOUSE_URL = HelperStatic.WAREHOUSE_URL + STATE_NAME + "/" + "NULL";;


        Log.e("FINAL_WAREHOUSE_URL Url", " url = " + FINAL_WAREHOUSE_URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_WAREHOUSE_URL, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject child = response.getJSONObject("GetWareHouseResult");

                    ArrayList<WareHousePojo> warehouse_list_cm = new ArrayList<WareHousePojo>();
                    ArrayList<WareHousePojo> warehouse_list_cmp = new ArrayList<WareHousePojo>();
                    ArrayList<WareHouseInspectionHeaderPojo> warehouse_ls_info = new ArrayList<WareHouseInspectionHeaderPojo>();

                    JSONArray commonJsonArray = child.getJSONArray("warehouse_ls_cmp");
                    for (int i = 0; i < commonJsonArray.length(); i++)
                    {
                        WareHousePojo pojo = new WareHousePojo();
                        JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                        pojo.setWarehouse_id(jsonObject.getString("Warehouseid"));
                        pojo.setWarehouse_address(jsonObject.getString("Warehouseaddress"));
                        pojo.setWarehouse_name(jsonObject.getString("Warehousename"));

                        pojo.setLocation_id(jsonObject.getString("Location_id"));
                        pojo.setLocation(dbHelper.getLocationNameUsingId(jsonObject.getString("Location_id")));
                        pojo.setCmAndcmp("CMP");

                        warehouse_list_cmp.add(pojo);

                        pojo = null;
                        jsonObject = null;
                    }
                    commonJsonArray = null;

                    commonJsonArray = child.getJSONArray("warehouse_ls_smp");
                    for (int i = 0; i < commonJsonArray.length(); i++)
                    {
                        WareHousePojo pojo = new WareHousePojo();
                        JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                        pojo.setWarehouse_id(jsonObject.getString("Warehouseid"));
                        pojo.setWarehouse_address(jsonObject.getString("Warehouseaddress"));
                        pojo.setWarehouse_name(jsonObject.getString("Warehousename"));

                        pojo.setLocation_id(jsonObject.getString("Location_id"));
                        //Log.e("Location_id", "val = " + jsonObject.getString("Location_id"));

                        pojo.setLocation(dbHelper.getLocationNameUsingId(jsonObject.getString("Location_id")));
                        pojo.setCmAndcmp("CM");

                        warehouse_list_cm.add(pojo);

                        pojo = null;
                        jsonObject = null;
                    }
                    commonJsonArray = null;

                    commonJsonArray = child.getJSONArray("warehouse_ls_info");
                    for (int i = 0; i < commonJsonArray.length(); i++)
                    {
                        WareHouseInspectionHeaderPojo pojo = new WareHouseInspectionHeaderPojo();
                        JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                        pojo.setInspectionID(jsonObject.getString("InspectionID"));
                        pojo.setSurveyDate(jsonObject.getString("SurveyDate"));
                        pojo.setInspectionNo(jsonObject.getString("InspectionNo"));
                        pojo.setStateID(jsonObject.getString("StateID"));
                        pojo.setLocationID(jsonObject.getString("LocationID"));
                        pojo.setLocationPinCode(jsonObject.getString("LocationPinCode"));
                        pojo.setDistrictID(jsonObject.getString("DistrictID"));
                        pojo.setGodownAddress(jsonObject.getString("GodownAddress"));
                        pojo.setGodownOwner(jsonObject.getString("GodownOwner"));
                        pojo.setCCLocEmpID(jsonObject.getString("CCLocEmpID"));
                        pojo.setBankID(jsonObject.getString("BankID"));
                        pojo.setBranchID(jsonObject.getString("BranchID"));
                        pojo.setSurveyDoneEmpID(jsonObject.getString("SurveyDoneEmpID"));
                        pojo.setDesigID(jsonObject.getString("DesigID"));
                        pojo.setBorrowerContact(jsonObject.getString("BorrowerContact"));
                        pojo.setCreatedDate(jsonObject.getString("CreatedDate"));
                        pojo.setRecordCount_InspectionDetails(jsonObject.getString("RecordCount_InspectionDetails"));
                        pojo.setStatusFlag(jsonObject.getString("StatusFlag"));
                        pojo.setNoOfGodowns(jsonObject.getString("NoOfGodowns"));

                        warehouse_ls_info.add(pojo);

                        pojo = null;
                        jsonObject = null;
                    }
                    commonJsonArray = null;

                    dbHelper.insertwarehouseTable(warehouse_list_cm, "CM");
                    dbHelper.insertwarehouseTable(warehouse_list_cmp, "CMP");
                    dbHelper.insertWarehouseHeaderTable(warehouse_ls_info);

                    Log.e("Insert Table : ", " warehouse_list_cm = " + warehouse_list_cm.size());
                    Log.e("Insert Table : ", " warehouse_list_cmp = " + warehouse_list_cmp.size());
                    Log.e("Insert Table : ", " warehouse_ls_info = " + warehouse_ls_info.size());

                    dialog.setMessage("Loading Database 3/14");

                    getAuditTypeAndAuditPoint();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);
                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void getWareHouseInspection()
    {


        FINAL_URL_WAREHOUSE_INSPECTION = HelperStatic.WAREHOUSE_INSPECTION_URL + "/" + "NULL";


        Log.e("warehouse inspections", " url = " + FINAL_URL_WAREHOUSE_INSPECTION);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_WAREHOUSE_INSPECTION, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    ArrayList<WareHouseInspectionPojo> list = new ArrayList<WareHouseInspectionPojo>();
                    WareHouseInspectionPojo pojo;
                    JSONObject jsonObject;

                    String particular;
                    int particular_id;
                    int particular_sqe_no;
                    String name;
                    String name_id;
                    String name_sqe_no;
                    String boolean_validation;
                    String date;
                    String values = "";
                    String values_mandatory_validation;
                    String multi_select_validation;
                    String number_validation;
                    String text_validation;
                    String grp_header;
                    String grp_header_id;
                    String master_item_type_it;

                    JSONArray jsonArray = response.getJSONArray("getFillinginGridResult");

                    if (jsonArray.length() > 0)
                    {

                        for (int i = 0; i < jsonArray.length(); i++)
                        {

                            pojo = new WareHouseInspectionPojo();

                            jsonObject = jsonArray.getJSONObject(i);

                            particular = jsonObject.getString("ItemType");
                            particular_id = jsonObject.getInt("ItemTypeID");
                            particular_sqe_no = jsonObject.getInt("SequenceNo");
                            name = jsonObject.getString("Condition");
                            name_id = jsonObject.getString("ItemTypeDetailID");
                            name_sqe_no = jsonObject.getString("ConditionSeqNo");
                            boolean_validation = jsonObject.getString("Boolean");
                            date = jsonObject.getString("Date");
                            values = "";
                            values_mandatory_validation = jsonObject.getString("Mandatory");
                            multi_select_validation = jsonObject.getString("MultiSelect");
                            number_validation = jsonObject.getString("Number");
                            text_validation = jsonObject.getString("Text");
                            grp_header = jsonObject.getString("GroupHeader");
                            grp_header_id = jsonObject.getString("GroupHeaderID");
                            master_item_type_it = jsonObject.getString("MasterItemTypeID");

                            pojo.setParticular(particular);
                            pojo.setParticular_id(particular_id);
                            pojo.setParticular_sqe_no(particular_sqe_no);
                            pojo.setName(name);
                            pojo.setName_id(name_id);
                            pojo.setName_sqe_no(name_sqe_no);
                            pojo.setBoolean_validation(boolean_validation);
                            pojo.setDate(date);
                            pojo.setValues(values);
                            pojo.setValues_mandatory_validation(values_mandatory_validation);
                            pojo.setMulti_select_validation(multi_select_validation);
                            pojo.setNumber_validation(number_validation);
                            pojo.setText_validation(text_validation);
                            pojo.setGrp_header(grp_header);
                            pojo.setGrp_header_id(grp_header_id);
                            pojo.setMaster_item_type_it(master_item_type_it);

                            list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }


                    }

                    dbHelper.insertIntoWareHouseInspectionMainTable(list);
                    Log.e("Insert Table : ", " WareHouseInspection = " + list.size());

                    dialog.setMessage("Loading Database 5/14");

                    getBank();


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);

                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void getBank()
    {


        FINAL_URL_BANK = HelperStatic.BANK_URL + "/" + "NULL";;


        Log.e("bank Url", " url = " + FINAL_URL_BANK);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_BANK,"", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    ArrayList<BankPojo> list = new ArrayList<BankPojo>();
                    ArrayList<BranchPojo> list_branch = new ArrayList<BranchPojo>();

                    BankPojo pojo;
                    BranchPojo pojo_branch;
                    JSONObject jsonObject;

                    String bank_name;
                    String bank_id, branch_id, branch_name;


                    JSONObject jsonObjectParent = response.getJSONObject("GetBankDetailsResult");

                    if (jsonObjectParent.length() > 0)
                    {


                        JSONArray jsonArray_bank = jsonObjectParent.getJSONArray("Bank");
                        JSONArray jsonArray_branch = jsonObjectParent.getJSONArray("Branch");


                        if (jsonArray_bank.length() > 0)
                        {

                            for (int i = 0; i < jsonArray_bank.length(); i++)
                            {

                                pojo = new BankPojo();

                                jsonObject = jsonArray_bank.getJSONObject(i);

                                bank_id = jsonObject.getString("BankID");
                                bank_name = jsonObject.getString("BankName");


                                pojo.setBanck_id(bank_id);
                                pojo.setBanck_name(bank_name);


                                list.add(pojo);

                                pojo = null;
                                jsonObject = null;

                            }


                        }


                        if (jsonArray_branch.length() > 0)
                        {

                            for (int i = 0; i < jsonArray_branch.length(); i++)
                            {

                                pojo_branch = new BranchPojo();

                                jsonObject = jsonArray_branch.getJSONObject(i);

                                bank_id = jsonObject.getString("BankID");
                                branch_id = jsonObject.getString("BranchID");
                                branch_name = jsonObject.getString("BranchName");


                                pojo_branch.setBank_id(bank_id);
                                pojo_branch.setBranch_id(branch_id);
                                pojo_branch.setBranch_name(branch_name);


                                list_branch.add(pojo_branch);

                                pojo_branch = null;
                                jsonObject = null;

                            }


                        }


                        dbHelper.insertBank(list);
                        dbHelper.insertBranch(list_branch);

                        Log.e("Insert Table : ", " Bank = " + list.size());
                        Log.e("Insert Table : ", " Branch = " + list_branch.size());

                        dialog.setMessage("Loading Database 6/14");

                        getUserRole();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);
                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void getUserRole() {


        FINAL_USER_ROLE_URL = HelperStatic.USER_ROLEN_URL + editText_employee_id.getText().toString();


        Log.e("FINAL_USER_ROLE_URL", " url = " + FINAL_USER_ROLE_URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_USER_ROLE_URL, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response)
            {

                try
                {
                    // Parsing json object response
                    //ArrayList<String> list = new ArrayList<String>();
                    ArrayList<UserRolePojo> list = new ArrayList<UserRolePojo>();

                    UserRolePojo pojo;
                    JSONObject jsonObject;

                    String role_name, role_condition;


                    JSONArray jsonArray_district = response.getJSONArray("Get_User_RoleResult");

                    if (jsonArray_district.length() > 0)
                    {

                        for (int i = 0; i < jsonArray_district.length(); i++)
                        {
                            pojo = new UserRolePojo();

                            jsonObject = jsonArray_district.getJSONObject(i);

                            role_name = jsonObject.getString("FormFileName");
                            role_condition = jsonObject.getString("ADD_");

                            pojo.setFormFileName(role_name);
                            pojo.setCondition(role_condition);

                            list.add(pojo);
                            pojo = null;
                            jsonObject = null;

                        }


                        dbHelper.insertUserRole(list);
                        Log.e("Insert Table : ", " UserRole = " + list.size());

                        dialog.setMessage("Loading Database 7/14");

                        //getDistrict();
                        getExchType();
                    }


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);

                dialogDismiss();

            }
        }

        );


        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq);


    }



    private void getDistrict()
    {


        FINAL_URL_DISTRICT = HelperStatic.DISTRICT_URL + STATE_NAME + "/" + "NULL";


        Log.e("district Url", " url = " + FINAL_URL_DISTRICT);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_DISTRICT, "", new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {

                try
                {
                    // Parsing json object response
                    ArrayList<DistrictPojo> list = new ArrayList<DistrictPojo>();


                    DistrictPojo pojo;

                    JSONObject jsonObject;

                    String DistrictID, DistrictName, StateID, SegmentName;


                    JSONArray jsonArray_district = response.getJSONArray("GetDistrictResult");

                    if (jsonArray_district.length() > 0)
                    {


                        for (int i = 0; i < jsonArray_district.length(); i++)
                        {

                            pojo = new DistrictPojo();

                            jsonObject = jsonArray_district.getJSONObject(i);

                            DistrictID = jsonObject.getString("DistrictID");
                            DistrictName = jsonObject.getString("DistrictName");
                            StateID = jsonObject.getString("StateID");
                            SegmentName = jsonObject.getString("SegmentName");


                            pojo.setDistrict_id(DistrictID);
                            pojo.setDistrict_name(DistrictName);
                            pojo.setState_id(StateID);
                            pojo.setSegment_Name(SegmentName);


                            list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }

                        dbHelper.insertDistrict(list);
                        Log.e("Insert Table : ", " District = " + list.size());

                        dialog.setMessage("Loading Database 13/14");

                        // Insert Downloaded Datetime
                        dbHelper.insertDownloadedDateTime(downloadedDatetime);

                        validInvalidUser(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);

                dialogDismiss();

            }
        }

        );


        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq);


    }


    private void getExchType()
    {


        //FINAL_URL_DISTRICT = HelperStatic.DISTRICT_URL + STATE_NAME + "/" + "NULL";

        //Take url from Helper Class and pass it in String
        FINAL_EXCHANGE_TYPE_URL = HelperStatic.EXCHANGE_TYPE_URL;

        // for cheaking error we gonna use it
        Log.e("exhange type Url", " url = " + FINAL_EXCHANGE_TYPE_URL);

         // json parsing
        // we can also pass the Url direclty
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_EXCHANGE_TYPE_URL, "", new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {

                try
                {
                    // Parsing json object response
                    //ArrayList<DistrictPojo> list = new ArrayList<DistrictPojo>();
                    //DistrictPojo pojo;
                    ArrayList<ExchangeTypePojo> list = new ArrayList<ExchangeTypePojo>();
                    ExchangeTypePojo pojo;

                    JSONObject jsonObject;

                    String ExchTypeId, ExchTypeName;


                    JSONArray jsonArray_exchangetype = response.getJSONArray("Get_Exchange_TypeResult");

                    if (jsonArray_exchangetype.length() > 0)
                    {


                        for (int i = 0; i < jsonArray_exchangetype.length(); i++)
                        {

                            pojo = new ExchangeTypePojo();

                            jsonObject = jsonArray_exchangetype.getJSONObject(i);

                            ExchTypeId = jsonObject.getString("ExchangeTypeId");
                            ExchTypeName = jsonObject.getString("ExchangeTypeName");

                            pojo.setExchange_type_id(ExchTypeId);
                            pojo.setExchange_type_name(ExchTypeName);

                            list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }

                        dbHelper.insertExchangeType(list);
                        Log.e("Insert Table : ", " Exchange Type = " + list.size());

                        dialog.setMessage("Loading Database 8/14");
                        getCommodity();

                        // Insert Downloaded Datetime
                        // dbHelper.insertDownloadedDateTime(downloadedDatetime);  Commented by Rajesh on 02.05.2016
                        //validInvalidUser(true);  Commented by Rajesh on 02.05.2016
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);

                dialogDismiss();

            }
        }

        );


        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq);


    }

    private void getCMP()
    {


        //FINAL_URL_DISTRICT = HelperStatic.DISTRICT_URL + STATE_NAME + "/" + "NULL";
        FINAL_SM_CMP = HelperStatic.SM_CMP_URL;

        Log.e("CMP Url", " url = " + FINAL_SM_CMP);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_SM_CMP, "", new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {

                try
                {
                    // Parsing json object response
                    //ArrayList<DistrictPojo> list = new ArrayList<DistrictPojo>();
                    //DistrictPojo pojo;
                    ArrayList<CMPPojo> list = new ArrayList<CMPPojo>();
                    CMPPojo pojo;

                    JSONObject jsonObject;

                    String CMP_Id, CMP_Name, Location_id, Concurrency;

                    JSONArray jsonArray_SM_CMP = response.getJSONArray("Get_SM_CMPResult");

                    if (jsonArray_SM_CMP.length() > 0)
                    {

                        for (int i = 0; i < jsonArray_SM_CMP.length(); i++)
                        {

                            pojo = new CMPPojo();

                            jsonObject = jsonArray_SM_CMP.getJSONObject(i);

                            CMP_Id = jsonObject.getString("CMPID");
                            CMP_Name = jsonObject.getString("CMPName");
                            Location_id = jsonObject.getString("locationid");
                            Concurrency= jsonObject.getString("Concurrency");

                            pojo.setCMP_id(CMP_Id);
                            pojo.setCMP_name(CMP_Name);
                            pojo.setLocation_id(Location_id);
                            pojo.setConcurrency(Concurrency);

                            list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }

                        dbHelper.insertCMP(list);
                        Log.e("Insert Table : ", " CMP = " + list.size());

                        dialog.setMessage("Loading Database 10/14");

                        getCommodity_OnLocation();

                        // Insert Downloaded Datetime
                        // dbHelper.insertDownloadedDateTime(downloadedDatetime);  Commented by Rajesh on 02.05.2016
                        //validInvalidUser(true);  Commented by Rajesh on 02.05.2016
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);

                dialogDismiss();

            }
        }

        );


        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq);


    }


    //---------------------- Start
    private void getCommodity_OnLocation() {


        //FINAL_URL_DISTRICT = HelperStatic.DISTRICT_URL + STATE_NAME + "/" + "NULL";
        FINAL_COMMODITY_ONLOCATION_URL = HelperStatic.COMMODITY_ONLOCATION_URL + STATE_NAME ;
//Raj
        Log.e("COMMODITYONLOCATION Url", " url = " + FINAL_COMMODITY_ONLOCATION_URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_COMMODITY_ONLOCATION_URL, "", new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    ArrayList<CommodityOnLocationPojo> list = new ArrayList<CommodityOnLocationPojo>();
                    CommodityOnLocationPojo pojo;

                    JSONObject jsonObject;

                    String Commodity, CommodityId, ExchangeType, ExchangeTypeId, Location, LocationId, StateId;
                    String StateName;

                    JSONArray jsonArray_Commodity_OnLocation = response.getJSONArray("Get_SM_CommodityOnLocationResult");

                    if (jsonArray_Commodity_OnLocation.length() > 0)
                    {

                        for (int i = 0; i < jsonArray_Commodity_OnLocation.length(); i++)
                        {

                            pojo = new CommodityOnLocationPojo();

                            jsonObject = jsonArray_Commodity_OnLocation.getJSONObject(i);

                            Commodity = jsonObject.getString("Commodity");
                            CommodityId = jsonObject.getString("CommodityId");
                            ExchangeType = jsonObject.getString("ExchangeType");
                            ExchangeTypeId= jsonObject.getString("ExchangeTypeId");
                            Location = jsonObject.getString("Location");
                            LocationId= jsonObject.getString("LocationId");
                            StateId= jsonObject.getString("StateId");
                            StateName= jsonObject.getString("StateName");

                            pojo.setCommodity(Commodity);
                            pojo.setCommodityId(CommodityId);
                            pojo.setExchangeType(ExchangeType);
                            pojo.setExchangeTypeId(ExchangeTypeId);
                            pojo.setLocation(Location);
                            pojo.setLocationId(LocationId);
                            pojo.setStateId(StateId);
                            pojo.setStateName(StateName);

                            list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }

                        dbHelper.insert_Commodity_Location_Mapping(list);
                        Log.e("Insert Table : ", " COMMODITY ON LOCATION MAPPING = " + list.size());

                        dialog.setMessage("Loading Database 11/14");

                        getDistrict();

                        // Insert Downloaded Datetime
                        // dbHelper.insertDownloadedDateTime(downloadedDatetime);  Commented by Rajesh on 02.05.2016
                        //validInvalidUser(true);  Commented by Rajesh on 02.05.2016
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);

                dialogDismiss();

            }
        }

        );


        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq);


    }


    //------------------ End


    // rAJAT comodity 04.05.2016

    private void getCommodity()
    {


        //FINAL_URL_DISTRICT = HelperStatic.DISTRICT_URL + STATE_NAME + "/" + "NULL";
        FINAL_COMMODITY_URL = HelperStatic.COMMODITY_TYPE_URL;

        Log.e("Commodity Url", " url = " + FINAL_COMMODITY_URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_COMMODITY_URL, "", new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {

                try
                {
                    // Parsing json object response
                    //ArrayList<DistrictPojo> list = new ArrayList<DistrictPojo>();
                    //DistrictPojo pojo;
                    ArrayList<CommodityPojo> list = new ArrayList<CommodityPojo>();
                    CommodityPojo pojo;

                    JSONObject jsonObject;

                    String Commodity_Id, Commodity_Name, Commodity_ExchTypeId, Commodity_ExchTypeName;


                    JSONArray jsonArray_commoditytype = response.getJSONArray("NewGet_CommodityResult");

                    if (jsonArray_commoditytype.length() > 0)
                    {


                        for (int i = 0; i < jsonArray_commoditytype.length(); i++)
                        {

                            pojo = new CommodityPojo();

                            jsonObject = jsonArray_commoditytype.getJSONObject(i);

                            Commodity_Id = jsonObject.getString("CommodityID");
                            Commodity_Name = jsonObject.getString("CommodityName");
                            Commodity_ExchTypeId = jsonObject.getString("ExchangeTypeId");
                            Commodity_ExchTypeName= jsonObject.getString("ExchangeType");

                            pojo.setCommodity_id(Commodity_Id);
                            pojo.setCommodity_name(Commodity_Name);
                            pojo.setCommodity_exchange_type_id(Commodity_ExchTypeId);
                            pojo.setCommodity_exchange_type_name(Commodity_ExchTypeName);

                            list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }

                        dbHelper.insertCommodity(list);
                        Log.e("Insert Table : ", " Commodity = " + list.size());

                        dialog.setMessage("Loading Database 9/14");

                        getCMP();

                        // Insert Downloaded Datetime
                        // dbHelper.insertDownloadedDateTime(downloadedDatetime);  Commented by Rajesh on 02.05.2016
                        //validInvalidUser(true);  Commented by Rajesh on 02.05.2016
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    button_submit.setEnabled(true);
                    dialogDismiss();
                }

            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                button_submit.setEnabled(true);

                dialogDismiss();

            }
        }

        );


        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq);


    }




    /// eND OF Commodity



//Written by Rajesh 02.05.2016


    private void validInvalidUser(boolean check) {

        button_submit.setEnabled(true);
        if (check)
        {
            putIsFirstPref(false);
            putMobileNoPref(editText_mobile_no.getText().toString());

            dialogDismiss();

            startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("emp_id", editText_employee_id.getText().toString()));
            editText_employee_id.setText("");
            editText_password.setText("");
            button_submit.setEnabled(true);
        }

        else
        {
            editText_employee_id.setText("");
            editText_password.setText("");
            editText_mobile_no.setText("");
            dialogDismiss();
            Toast.makeText(LoginActivity.this, "Invalid Employee ID and Password and Mobile Number.", Toast.LENGTH_LONG).show();
        }


    }

    private String getImeiNo()
    {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();
        return device_id;
    }

    public void getRef() {
        textView_imei_no = (TextView) findViewById(R.id.text_view_imei_number);
        editText_employee_id = (EditText) findViewById(R.id.edit_text_employee_id);
        editText_password = (EditText) findViewById(R.id.edit_password);
        editText_mobile_no = (EditText) findViewById(R.id.edit_text_mobile_number);
        textView_forgot_password = (TextView) findViewById(R.id.text_view_forgot_password);

        button_submit = (Button) findViewById(R.id.button_submit);

        textView_imei_no.setText(getImeiNo());

        if (getMobileNoPref().length() > 0) {
            editText_mobile_no.setText(getMobileNoPref());
            editText_mobile_no.setKeyListener(null);
        }


        dbHelper = new DbHelper(LoginActivity.this);

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Authentication");
        dialog.setCancelable(false);
        dialog.setCancelable(false);


    }

    private void dialogShow() {
        dialog.show();
    }

    private void dialogDismiss() {
        dialog.dismiss();
    }

    private void putMobileNoPref(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mobile_no", value);
        editor.commit();
    }

    private String getMobileNoPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        return preferences.getString("mobile_no", "");
    }

    private void putEmpNameGlobal(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("emp_name", value);
        editor.commit();
    }

    private void putEmpNumberGlobal(String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("emp_number", value);
        editor.commit();
    }


    private String getEmpNameGlobal() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        return preferences.getString("emp_name", "UNKNOW");
    }


    private void putIsFirstPref(boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_first", value);
        editor.commit();
    }

    private boolean getIsFirstPref()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        return preferences.getBoolean("is_first", true);
    }

    private String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

}



