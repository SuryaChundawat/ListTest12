package com.coder.sample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.coderbroadcast.UpdateToServerAllLocalDatabase;
import com.com.pojo.AuditPointDetailsServerPojo;
import com.com.pojo.AuditTypePojo;
import com.com.pojo.AuditorPojo;
import com.com.pojo.BankPojo;
import com.com.pojo.BranchPojo;
import com.com.pojo.DistrictPojo;
import com.com.pojo.GodownCmAndCmpPojo;
import com.com.pojo.LocationCmAndCmpPojo;
import com.com.pojo.SegmentPojo;
import com.com.pojo.StatePojo;
import com.com.pojo.UserRolePojo;
import com.com.pojo.WareHouseInspectionHeaderPojo;
import com.com.pojo.WareHouseInspectionInformationPojo;
import com.com.pojo.WareHouseInspectionPojo;
import com.com.pojo.WareHousePojo;
import com.database.DbHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Codeginger on 23/12/2015.
 */
public class UpdateDatabase extends AppCompatActivity
{
    private String emp_no, downloadedDatetime;
    private int updateId;
    private ProgressDialog dialog;
    private String FINAL_URL, FINAL_URL_WAREHOUSE_INSPECTION, FINAL_URL_BANK, FINAL_URL_DISTRICT, FINAL_URL_AUDIT_TYPE_ADUTI_POINT, FINAL_URL_GODOWN, FINAL_WAREHOUSE_URL, FINAL_USER_ROLE_URL;
    private RadioGroup rdgUpdate;
    private RadioButton rdUpdate, rdDeleteUpdate;
    private Button btnUpdate;
    private DbHelper dbHelper;
    private String STATE_NAME;
    String string_json_Copy;
    UpdateToServerAllLocalDatabase commonServerSenderHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_database);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getRef();
    }

    private void getRef()
    {
        rdgUpdate = (RadioGroup) findViewById(R.id.rdgUpdate);
        rdUpdate = (RadioButton) findViewById(R.id.rdUpdate);
        rdDeleteUpdate = (RadioButton) findViewById(R.id.rdDeleteUpdate);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        emp_no = getEmpNumberGlobal();
        dbHelper = AppController.getDatabaseInstance();
    }

    private String getEmpNumberGlobal()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UpdateDatabase.this);
        return preferences.getString("emp_number", "000000");
    }

    private void dialogShow()
    {
        dialog.show();
    }

    private void dialogDismiss()
    {
        dialog.dismiss();
    }

    public void updateDatabase(View view)
    {
        if (isNetworkAvaliable(UpdateDatabase.this))
        {
            updateId = rdgUpdate.indexOfChild(findViewById(rdgUpdate.getCheckedRadioButtonId()));

            dialog = new ProgressDialog(UpdateDatabase.this);
            dialog.setMessage("Updating Database");
            dialog.setCancelable(false);

            commonServerSenderHelper = new UpdateToServerAllLocalDatabase(UpdateDatabase.this);

            locationSetUp();

            if(STATE_NAME != null && !STATE_NAME.equals(""))
            {
                AlertDialog alertbox = new AlertDialog.Builder(this)
                        .setMessage("You have logged in '" + STATE_NAME + "' \nWould you like to Update Database ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                dialogShow();

                                if (dbHelper.checkLocalGeoTagMappingCount())
                                {
                                    commonServerSenderHelper.geoTagMappingPostTest();
                                }
                                else if (dbHelper.checkLocalWareHouseInspectionCount())
                                {
                                    commonServerSenderHelper.wareHouseInspectionPostTest();
                                }
                                else if (dbHelper.checkLocalAtteanceCount())
                                {
                                    commonServerSenderHelper.attendancePostTest();
                                }
                                else if (dbHelper.checkLocalGodownAuditPointCount())
                                {
                                    commonServerSenderHelper.auditPointPostTest();
                                }
                                checkAuthentication();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            else
            {
                AlertDialog alertbox = new AlertDialog.Builder(this)
                        .setMessage("Internet / GPS is Not Detected.\nPlease Turn ON Internet / GPS and try again.")
                        .setPositiveButton("OK", null)
                        .show();
            }


            // attendancePostTest();


        }
        else
        {
            Toast.makeText(UpdateDatabase.this, "Please turn on network to update database,", Toast.LENGTH_LONG).show();
        }
    }


    //   Login Logic

    private void locationSetUp() {
        GPSTracker gps = new GPSTracker(UpdateDatabase.this, 10);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            STATE_NAME = gps.getState_name();

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
        }


    }

    private void checkAuthentication()
    {
        //locationSetUp();
        //dialogShow();

        if(updateId == 1)
        {
            dbHelper.deleteAllTable();
        }

        dialog.setMessage("Updating Database 1/10");
        final ArrayList<String> user_data = dbHelper.getUserInfo(emp_no);

        // Last Downloaded DateTime
        downloadedDatetime = dbHelper.getDownloadedDateTime();
        // Encoding DateTime
        downloadedDatetime = downloadedDatetime.toString().replace(" ","%20");

        // StateName
        if(STATE_NAME.contains(" "))
            STATE_NAME = STATE_NAME.toString().replace(" ","%20");

        if(updateId == 1)
        {
            FINAL_URL = HelperStatic.NEW_LOGIN_URL + user_data.get(2) + "/" + user_data.get(3) + "/" + user_data.get(0)
                    + "/" + user_data.get(1) + "/" + STATE_NAME + "/" + "UpdateDatabase" + "/" + "NULL";
        }
        else
        {
            FINAL_URL = HelperStatic.NEW_LOGIN_URL + user_data.get(2) + "/" + user_data.get(3) + "/" + user_data.get(0)
                    + "/" + user_data.get(1) + "/" + STATE_NAME + "/" + "UpdateDatabase" + "/" + downloadedDatetime;
        }

        Log.e("Login Url", " url = " + FINAL_URL);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL, "", new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
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

                        ArrayList<StatePojo> state_cm_list = new ArrayList<StatePojo>();
                        ArrayList<StatePojo> state_cmp_list = new ArrayList<StatePojo>();
                        ArrayList<StatePojo> state_HR_list = new ArrayList<StatePojo>();

                        ArrayList<LocationCmAndCmpPojo> location_cm_list = new ArrayList<LocationCmAndCmpPojo>();
                        ArrayList<LocationCmAndCmpPojo> location_cmp_list = new ArrayList<LocationCmAndCmpPojo>();
                        ArrayList<LocationCmAndCmpPojo> location_HR_list = new ArrayList<LocationCmAndCmpPojo>();

                        ArrayList<AuditorPojo> auditor_list = new ArrayList<AuditorPojo>();
                        ArrayList<SegmentPojo> segment_list = new ArrayList<SegmentPojo>();



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


                        if(updateId == 1)
                        {
                            dbHelper.insertState(state_cm_list);
                            dbHelper.insertState(state_cmp_list);
                            dbHelper.insertState(state_HR_list);
                        }
                        else
                        {
                            if(state_cm_list.size()>0)
                                dbHelper.insertUpdateState(state_cm_list);
                            if(state_cmp_list.size()>0)
                                dbHelper.insertUpdateState(state_cmp_list);
                            if(state_HR_list.size()>0)
                                dbHelper.insertUpdateState(state_HR_list);
                        }

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

                        if(updateId == 1)
                        {
                            dbHelper.insertLocationTable(location_cm_list, "CM");
                            dbHelper.insertLocationTable(location_cmp_list, "CMP");
                            dbHelper.insertLocationTable(location_HR_list, "HR");
                        }
                        else
                        {
                            if(location_cm_list.size()>0)
                                dbHelper.insertUpdateLocationTable(location_cm_list, "CM");
                            if(location_cmp_list.size()>0)
                                dbHelper.insertUpdateLocationTable(location_cmp_list, "CMP");
                            if(location_HR_list.size()>0)
                                dbHelper.insertUpdateLocationTable(location_HR_list, "HR");
                        }

                        Log.e("Insert Table : ", " CM = " + location_cm_list.size());
                        Log.e("Insert Table : ", " CMP = " + location_cmp_list.size());
                        Log.e("Insert Table : ", " HR = " + location_HR_list.size());

                        commonJsonArray = child.getJSONArray("Segment_ls");
                        for (int i = 0; i < commonJsonArray.length(); i++)
                        {
                            SegmentPojo pojo = new SegmentPojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);


                            pojo.setSegment_name(jsonObject.getString("SegmentName"));
                            pojo.setSegment_id(jsonObject.getString("Segmentid"));

                            segment_list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }
                        commonJsonArray = null;

                        AuditorPojo pojo_auditor = new AuditorPojo();
                        pojo_auditor.setEmp_num(emp_num);
                        pojo_auditor.setEmp_full_name(emp_name);
                        auditor_list.add(pojo_auditor);
                        pojo_auditor = null;

                        if(updateId == 1)
                        {
                            dbHelper.isertSegmentTable(segment_list);
                            dbHelper.insertAuditorTable(auditor_list);
                        }
                        else
                        {
                            if(segment_list.size()>0)
                                dbHelper.isertUpdateSegmentTable(segment_list);
                            if(auditor_list.size()>0)
                                dbHelper.insertUpdateAuditorTable(auditor_list);
                        }

                        Log.e("Insert Table : ", " Segment = " + segment_list.size());
                        Log.e("Insert Table : ", " Auditor = " + auditor_list.size());

                        dialog.setMessage("Updating Database 2/10");

                        //dbHelper.putUserLogin(editText_mobile_no.getText().toString().trim(), textView_imei_no.getText().toString().trim(), editText_employee_id.getText().toString().trim(), emp_pwd, STATE_NAME, emp_name, emp_num, designation_name, designation_id);
                        dbHelper.putUserLogin(user_data.get(2), user_data.get(3), user_data.get(0), emp_pwd, emp_name, emp_num, designation_name, designation_id);

                        getGodown();

                    }
                    else
                    {
                        dialogDismiss();
                    }


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    dialogDismiss();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "msg = " + error.toString(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    private void getGodown()
    {

        if(updateId == 1)
        {
            FINAL_URL_GODOWN = HelperStatic.GODOWN_URL + STATE_NAME + "/" + "NULL";
        }
        else
        {
            FINAL_URL_GODOWN = HelperStatic.GODOWN_URL + STATE_NAME + "/" + downloadedDatetime;
        }

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

                    if(updateId == 1)
                    {
                        dbHelper.insertGodownTable(godown_cm_list, "CM");
                        dbHelper.insertGodownTable(godown_cmp_list, "CMP");
                    }
                    else
                    {
                        if(godown_cm_list.size()>0)
                            dbHelper.insertUpdateGodownTable(godown_cm_list, "CM");
                        if(godown_cmp_list.size()>0)
                            dbHelper.insertUpdateGodownTable(godown_cmp_list, "CMP");
                    }

                    Log.e("POST resquest", "No. of godown_cm_list  = " + godown_cm_list.size());
                    Log.e("POST resquest", "No. of godown_cmp_list  = " + godown_cmp_list.size());

                    dialog.setMessage("Updating Database 3/10");

                    getWareHouse();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

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

                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    private void getWareHouse() {

        if(updateId == 1)
        {
            FINAL_WAREHOUSE_URL = HelperStatic.WAREHOUSE_URL + STATE_NAME + "/" + "NULL";
        }
        else
        {
            FINAL_WAREHOUSE_URL = HelperStatic.WAREHOUSE_URL + STATE_NAME + "/" + downloadedDatetime;
        }

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

                    if(updateId == 1)
                    {
                        dbHelper.insertwarehouseTable(warehouse_list_cm, "CM");
                        dbHelper.insertwarehouseTable(warehouse_list_cmp, "CMP");
                        dbHelper.insertWarehouseHeaderTable(warehouse_ls_info);
                    }
                    else
                    {
                        if(warehouse_list_cm.size()>0)
                            dbHelper.insertUpdatewarehouseTable(warehouse_list_cm, "CM");
                        if(warehouse_list_cmp.size()>0)
                            dbHelper.insertUpdatewarehouseTable(warehouse_list_cmp, "CMP");
                        if(warehouse_ls_info.size()>0)
                            dbHelper.insertUpdateWarehouseHeaderTable(warehouse_ls_info);
                    }

                    Log.e("POST resquest", "No. of warehouse_list_cm  = " + warehouse_list_cm.size());
                    Log.e("POST resquest", "No. of warehouse_list_cmp  = " + warehouse_list_cmp.size());
                    Log.e("Insert Table : ", " warehouse_ls_info = " + warehouse_ls_info.size());

                    dialog.setMessage("Updating Database 4/10");

                    getAuditTypeAndAuditPoint();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

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

                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    private void getAuditTypeAndAuditPoint()
    {
        if(updateId == 1)
        {
            FINAL_URL_AUDIT_TYPE_ADUTI_POINT = HelperStatic.AUDIT_POINT_AND_AUDIT_TYPE_URL + "/" + "NULL";
        }
        else
        {
            FINAL_URL_AUDIT_TYPE_ADUTI_POINT = HelperStatic.AUDIT_POINT_AND_AUDIT_TYPE_URL + "/" + downloadedDatetime;
        }
        Log.e("audit Type and point", " url = " + FINAL_URL_AUDIT_TYPE_ADUTI_POINT);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_AUDIT_TYPE_ADUTI_POINT, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response)
            {

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

                    if(updateId == 1)
                    {
                        dbHelper.insertAuditTypeTable(audit_type_list);
                        dbHelper.insertAuditPointDetails(audit_point_details_list);
                    }
                    else
                    {
                        if(audit_type_list.size()>0)
                            dbHelper.insertUpdateAuditTypeTable(audit_type_list);
                        if(audit_point_details_list.size()>0)
                            dbHelper.insertUpdateAuditPointDetails(audit_point_details_list);
                    }

                    Log.e("POST resquest", "No. of audit_type_list  = " + audit_type_list.size());
                    Log.e("POST resquest", "No. of audit_point_details_list  = " + audit_point_details_list.size());

                    dialog.setMessage("Updating Database 5/10");

                    getWareHouseInspection();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

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

                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void getWareHouseInspection()
    {
        if(updateId == 1)
        {
            FINAL_URL_WAREHOUSE_INSPECTION = HelperStatic.WAREHOUSE_INSPECTION_URL + "/" + "NULL";
        }
        else
        {
            FINAL_URL_WAREHOUSE_INSPECTION = HelperStatic.WAREHOUSE_INSPECTION_URL + "/" + downloadedDatetime;
        }

        Log.e("warehouse inspections", " url = " + FINAL_URL_WAREHOUSE_INSPECTION);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_WAREHOUSE_INSPECTION, "", new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject response)
            {

                try
                {
                    // Parsing json object response
                    ArrayList<WareHouseInspectionPojo> list = new ArrayList<WareHouseInspectionPojo>();
                    WareHouseInspectionPojo pojo;
                    JSONObject jsonObject;

                    String particular;
                    int particular_id;
                    int  particular_sqe_no;
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

                    if(updateId == 1)
                    {
                        dbHelper.insertIntoWareHouseInspectionMainTable(list);
                    }
                    else
                    {
                        dbHelper.insertUpdateIntoWareHouseInspectionMainTable(list);
                    }

                    Log.e("POST resquest", "No. of WareHouseInspectionMainTable  = " + list.size());

                    dialog.setMessage("Updating Database 6/10");

                    getBank();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

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


                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    private void getBank() {

        if(updateId == 1)
        {
            FINAL_URL_BANK = HelperStatic.BANK_URL + "/" + "NULL";
        }
        else
        {
            FINAL_URL_BANK = HelperStatic.BANK_URL + "/" + downloadedDatetime;
        }

        Log.e("bank Url", " url = " + FINAL_URL_BANK);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_BANK, "", new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
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

                        if(updateId == 1)
                        {
                            dbHelper.insertBank(list);
                            dbHelper.insertBranch(list_branch);
                        }
                        else
                        {
                            if(list.size()>0)
                                dbHelper.insertUpdateBank(list);
                            if(list_branch.size()>0)
                                dbHelper.insertUpdateBranch(list_branch);
                        }

                        Log.e("POST resquest", "No. Bank  = " + list.size());
                        Log.e("POST resquest", "No. Branch = " + list_branch.size());

                        dialog.setMessage("Updating Database 7/10");

                        getUserRole();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

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

                dialogDismiss();

            }
        });


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    private void getUserRole()
    {

        FINAL_USER_ROLE_URL = HelperStatic.USER_ROLEN_URL + emp_no;


        Log.e("FINAL_USER_ROLE_URL", " url = " + FINAL_USER_ROLE_URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_USER_ROLE_URL, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    ArrayList<UserRolePojo> list = new ArrayList<UserRolePojo>();

                    UserRolePojo pojo;
                    JSONObject jsonObject;

                    String role_name, role_condition;


                    JSONArray jsonArray_district = response.getJSONArray("Get_User_RoleResult");
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

                    //System.out.print("No. of UserRole" + list.size());
                    if(updateId == 1)
                    {
                        dbHelper.insertUserRole(list);
                    }
                    else
                    {
                        dbHelper.insertUpdatedUserRole(list);
                    }

                    Log.e("POST resquest", "No. of UserRole  = " + list.size());

                    dialog.setMessage("Updating Database 8/10");

                    getDistrict();



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

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

    private void getDistrict() {

        if(updateId == 1)
        {
            FINAL_URL_DISTRICT = HelperStatic.DISTRICT_URL + STATE_NAME + "/" + "NULL";
        }
        else
        {
            FINAL_URL_DISTRICT = HelperStatic.DISTRICT_URL + STATE_NAME + "/" + downloadedDatetime;
        }

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

                    if(updateId == 1)
                    {
                        dbHelper.insertDistrict(list);
                    }
                    else
                    {
                        if(list.size()>0)
                            dbHelper.insertUpdateDistrict(list);
                    }
                    Log.e("POST resquest", "No. of District  = " + list.size());

                    dialog.setMessage("Updating Database 10/10");

                    // Insert Downloaded Datetime
                    dbHelper.insertDownloadedDateTime(downloadedDatetime);

                    dialogDismiss();

                    Toast.makeText(UpdateDatabase.this, "Database Successfully Update", Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

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


                dialogDismiss();

            }
        }

        );


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // Adding request to request queue
        AppController.getInstance().

                addToRequestQueue(jsonObjReq);


    }



    private static boolean isNetworkAvaliable(Context ctx)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    public void attendancePostTest()
    {
        if (isNetworkAvaliable(UpdateDatabase.this))
        {
            new HttpAsyncTaskAttedance().execute(HelperStatic.ATTENDANCE_URL);
        }
        else
        {
            dialogDismiss();
        }
    }

    public void geoTagMappingPostTest() {
        if (isNetworkAvaliable(UpdateDatabase.this)) {
            new HttpAsyncTaskGeoTagMapping().execute(HelperStatic.GEO_TAG_MAPPING_URL);
        } else {
            dialogDismiss();
        }
    }


    public void auditPointPostTest() {
        if (isNetworkAvaliable(UpdateDatabase.this)) {
            new HttpAsyncTaskAuditPoint().execute(HelperStatic.AUDIT_DETAILS_URL);
        } else {
            dialogDismiss();
        }
    }

    public void wareHouseInspectionPostTest() {
        if (isNetworkAvaliable(UpdateDatabase.this)) {
            new HttpAsyncTaskWareHouseInspection().execute(HelperStatic.SERVER_INSERT_WAREHOUSE_INSPECTION_URL);
        } else {
            dialogDismiss();
        }

    }


    private class HttpAsyncTaskGeoTagMapping extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + getGeoTagMappingJson());
            return POSTGeoTagMapping(urls[0], getGeoTagMappingJson());
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(UpdateDatabase.this, "Geo tag mapping Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(UpdateDatabase.this);

                    dbHelper.deleteLocalGeoTagMapping();


                    if (dbHelper.checkLocalWareHouseInspectionCount()) {

                        wareHouseInspectionPostTest();
                    } else if (dbHelper.checkLocalAtteanceCount()) {
                        attendancePostTest();
                    } else if (dbHelper.checkLocalGodownAuditPointCount()) {

                        auditPointPostTest();

                    } else {
                        checkAuthentication();
                    }


                    // wareHouseInspectionPostTest();

                }

            } catch (JSONException e) {

                dialogDismiss();
                e.printStackTrace();

            }
        }
    }

    private String getGeoTagMappingJson() {

        DbHelper dbHelper = new DbHelper(UpdateDatabase.this);

        return dbHelper.getGeoTagMappingAllDataInJson();
    }

    public String POSTGeoTagMapping(String url, String json) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);


            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content� �

            httpPost.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            dialogDismiss();
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }


    private class HttpAsyncTaskWareHouseInspection extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            string_json_Copy = "";
            String s=getWareHouseInspectionJsonForServer();
            String row_index=s.substring(0, s.indexOf(","));
            String string_json=s.substring(s.indexOf(",") + 1, s.length());
            string_json_Copy = string_json;
            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);
            return row_index+","+POSTWareHouseInspection(urls[0], string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(UpdateDatabase.this, "wareHouse inspection Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();
            Log.e("respones", "respones val = " + result);

            String row_index=result.substring(0,result.indexOf(","));
            String string_json=result.substring(result.indexOf(",")+1,result.length());

            try
            {
                String tempInspId = "0";
                if(!string_json.equals(""))
                {
                    JSONObject jsonObject = new JSONObject(string_json);
                    DbHelper dbHelper = new DbHelper(UpdateDatabase.this);

                    if (jsonObject.getString("message").contains("Sucess"))
                    {
                        String s = jsonObject.getString("message");
                        tempInspId = s.substring(s.indexOf(",") + 1, s.length());

                        dbHelper.deleteLocalWareHouseInspection(row_index, tempInspId);
                    }

                    if (dbHelper.checkLocalAtteanceCount())
                    {
                        attendancePostTest();
                    }
                    else if (dbHelper.checkLocalGodownAuditPointCount())
                    {
                        auditPointPostTest();
                    }
                    else
                    {
                        checkAuthentication();
                    }
                }
            }
            catch (JSONException e)
            {
                dialogDismiss();
                e.printStackTrace();
            }
        }
    }

    public String POSTWareHouseInspection(String url, String json) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);


            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content� �

            httpPost.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            dialogDismiss();
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }

    private String getWareHouseInspectionJsonForServer() {

        DbHelper dbHelper = new DbHelper(UpdateDatabase.this);

        return dbHelper.getWareHouseInspectionJosnForServer();
    }

    private class HttpAsyncTaskAttedance extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            String s = getAttendanceJson();
            String row_index = s.substring(0, s.indexOf(","));
            String string_json = s.substring(s.indexOf(",") + 1, s.length());

            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);

            return row_index + "," + POSTAttedance(urls[0], string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(UpdateDatabase.this, "Attendance Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            Log.e("respones", "val = " + result.toString());


            String row_index = result.substring(0, result.indexOf(","));
            String string_json = result.substring(result.indexOf(",") + 1, result.length());

            try {
                JSONObject jsonObject = new JSONObject(string_json);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(UpdateDatabase.this);

                    dbHelper.deleteLocalAttendance(row_index);

                    if (dbHelper.checkLocalAtteanceCount()) {
                        attendancePostTest();
                    } else if (dbHelper.checkLocalGodownAuditPointCount()) {

                        auditPointPostTest();

                    } else {
                        checkAuthentication();
                    }


                }

            } catch (JSONException e) {
                dialogDismiss();
                e.printStackTrace();
            }


        }
    }

    private String getAttendanceJson()
    {
        DbHelper dbHelper = new DbHelper(UpdateDatabase.this);

        return dbHelper.getAttendanceAllDataInJson();
    }


    public String POSTAttedance(String url, String json) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);


            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content� �

            httpPost.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            dialogDismiss();
        }
        // 11. return result
        return result;
    }


    private class HttpAsyncTaskAuditPoint extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            String s = getGodownDetailsJson();
            String row_index = s.substring(0, s.indexOf(","));
            String string_json = s.substring(s.indexOf(",") + 1, s.length());

            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);

            return row_index + "," + POSTAuditPoint(urls[0], string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String s) {

            Log.e("AuditPoint", "AuditPoint = " + s.toString());
            Toast.makeText(UpdateDatabase.this, "AuditPoint Data Sent! result = " + s.toString(), Toast.LENGTH_LONG).show();

            String row_index = s.substring(0, s.indexOf(","));
            String string_json = s.substring(s.indexOf(",") + 1, s.length());


            try {
                JSONObject jsonObject = new JSONObject(string_json);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(UpdateDatabase.this);

                    dbHelper.deleteLocalGodownAudit(row_index);


                    if (dbHelper.checkLocalGodownAuditPointCount()) {
                        auditPointPostTest();
                    } else {
                        // new EmailHelper().execute(string_email);
                        checkAuthentication();
                    }

                    // wareHouseInspectionPostTest();

                }

            } catch (JSONException e) {

                dialogDismiss();
                e.printStackTrace();
            }

        }
    }

    public String POSTAuditPoint(String url, String json) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);


            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);
            // 7. Set some headers to inform server about the type of the content� �

            httpPost.setHeader("Content-type", "application/json");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            dialogDismiss();
        }
        // 11. return result
        return result;
    }

    private String getGodownDetailsJson() {

        DbHelper dbHelper = new DbHelper(UpdateDatabase.this);

        return dbHelper.getLocalAuditPointDetailsAllDataInJson();

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private void putIsFirstPref(boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UpdateDatabase.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_first", value);
        editor.commit();
    }


}
