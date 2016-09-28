package com.coder.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;

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
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    //DbHelper dbHelper;
    String emp_no;
    private ProgressDialog dialog;

    private String FINAL_URL, FINAL_URL_WAREHOUSE_INSPECTION, FINAL_URL_BANK, FINAL_URL_DISTRICT, FINAL_URL_AUDIT_TYPE_ADUTI_POINT, FINAL_URL_GODOWN, FINAL_WAREHOUSE_URL, FINAL_USER_ROLE_URL;
    private DbHelper dbHelper;
    private String STATE_NAME;
    private ImageView button_attendance, button_geo_tag_mapping, button_godown_audit, button_warehouse_inspection, button_cdf ;
    private TextView txtAttendanceCount, txtGeoTagMappingCount, txtGodownAuditCount, txtWarehouseInspectionCount,txDepositCount;
    private TextView txtAttendancelbl, txtGeoTagMappinglbl, txtGodownAuditlbl, txtWarehouseInspectionlbl, txtCdflbl;

    UpdateToServerAllLocalDatabase commonServerSenderHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getRef();

    }

    private void localDBCount()
    {
        txtAttendanceCount.setText("" + dbHelper.getAttendancelocalDBCount());
        txtGeoTagMappingCount.setText("" + dbHelper.getGeoTagMappinglocalDBCount());
        txtGodownAuditCount.setText("" + dbHelper.getGodownAuditPointlocalDBCount());
        txDepositCount.setText("" + dbHelper.getDepositlocalDBCount());
        txtWarehouseInspectionCount.setText("" + dbHelper.getWareHouseInspectionlocalDBCount());
    }

    private void getRef()
    {

        button_attendance = (ImageView) findViewById(R.id.button_attendance);
        button_geo_tag_mapping = (ImageView) findViewById(R.id.button_geo_tag_mapping);
        button_godown_audit = (ImageView) findViewById(R.id.button_godown_audit);
        button_warehouse_inspection = (ImageView) findViewById(R.id.button_warehouse_inspection);
        button_cdf = (ImageView) findViewById(R.id.button_cdf);

        txtAttendanceCount = (TextView) findViewById(R.id.txtAttendanceCount);
        txtGeoTagMappingCount = (TextView) findViewById(R.id.txtGeoTagMappingCount);
        txtGodownAuditCount = (TextView) findViewById(R.id.txtGodownAuditCount);
        txDepositCount= (TextView) findViewById(R.id.txDepositCount);
        txtWarehouseInspectionCount = (TextView) findViewById(R.id.txtWarehouseInspectionCount);

        txtAttendancelbl = (TextView) findViewById(R.id.txtAttendancelbl);
        txtGeoTagMappinglbl = (TextView) findViewById(R.id.txtGeoTagMappinglbl);
        txtGodownAuditlbl = (TextView) findViewById(R.id.txtGodownAuditlbl);
        txtWarehouseInspectionlbl = (TextView) findViewById(R.id.txtWarehouseInspectionlbl);
        txtCdflbl = (TextView) findViewById(R.id.txtCdflbl);

        emp_no = getEmpNumberGlobal();
        dbHelper = AppController.getDatabaseInstance();

        ArrayList<String> list_role = dbHelper.getUserRole();
        ArrayList<String> list_roleCondition = dbHelper.getUserRoleCondition();

        localDBCount();

        if (list_role.size() > 0)
        {
            String name= "", Condition = "";
            for (int i = 0; i < list_role.size(); i++)
            {
                name = list_role.get(i);
                Condition = list_roleCondition.get(i);

                if (name.contains("Attendance"))
                {
                    if(Condition.contains("N"))
                    {
                        button_attendance.setEnabled(false);
                        txtAttendancelbl.setTextColor(Color.BLACK);
                    }
                }
                else if (name.contains("GeoTagMapping"))
                {
                    if(Condition.contains("N"))
                    {
                        button_geo_tag_mapping.setEnabled(false);
                        txtGeoTagMappinglbl.setTextColor(Color.BLACK);
                    }
                }
                else if (name.contains("GodownAudit"))
                {
                    if(Condition.contains("N"))
                    {
                        button_godown_audit.setEnabled(false);
                        txtGodownAuditlbl.setTextColor(Color.BLACK);
                    }
                }
                else if (name.contains("WarehouseInspection"))
                {
                    if(Condition.contains("N"))
                    {
                        button_warehouse_inspection.setEnabled(false);
                        txtWarehouseInspectionlbl.setTextColor(Color.BLACK);
                    }
                }

/*
                else if (name.contains("CDF"))
                {
                    if(Condition.contains("N"))
                    {
                        button_cdf.setEnabled(false);
                    }
                }
*/


            }

        }

        // Deposit Disable
        // button_cdf.setEnabled(false);
        // txtCdflbl.setTextColor(Color.BLACK);

    }

    private String getEmpNumberGlobal() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        return preferences.getString("emp_number", "000000");
    }

    public void attendanceOnclick(View view) {

        // new CommonServerSenderHelper(HomeActivity.this).attendancePostTest();
        startActivity(new Intent(HomeActivity.this, AttendanceActivity.class).putExtra("emp_id", emp_no));
    }


    public void geoTagMapping(View view) {
        startActivity(new Intent(HomeActivity.this, GeoTagMappingActivity.class).putExtra("emp_id", emp_no));
    }

    public void godownAudit(View view) {

        // new CommonServerSenderHelper(HomeActivity.this).auditPointPostTest();

        startActivity(new Intent(HomeActivity.this, GodownAuditActivity.class).putExtra("emp_id", emp_no));
    }

    public void warehouseInspection(View view) {
        startActivity(new Intent(HomeActivity.this, WarehouseInspectionActivity.class).putExtra("emp_id", emp_no));

        //new CommonServerSenderHelper(HomeActivity.this).wareHouseInspectionPostTest();
    }

    public void cdf(View view)
    {
        //startActivity(new Intent(HomeActivity.this, CDFActivity.class).putExtra("emp_id", emp_no));
    }

    public void deposit(View view)
    {
        startActivity(new Intent(HomeActivity.this, DepositActivity.class));
    }

    public void withdrawalClick(View view)
    {
        startActivity(new Intent(HomeActivity.this, WithdrawalActivity.class));
    }

    public void updateLocalDatabase(View view)
    {
        if (isNetworkAvaliable(HomeActivity.this))
        {
            startActivity(new Intent(HomeActivity.this, UpdateDatabase.class));
        }
        else
        {
            Toast.makeText(HomeActivity.this, "Please turn on network to update database,", Toast.LENGTH_LONG).show();
        }
    }

    // DeleteAllLocalWarehouseInspection
    public void DeleteAllLocalWarehouseInspection(View view)
    {
        dbHelper.deleteAllLocalWareHouseInspection();
        Toast.makeText(HomeActivity.this, "All local warehouse inspection data deleted ,", Toast.LENGTH_LONG).show();
    }


    public void updateLocalDatabase1(View view) {

        if (isNetworkAvaliable(HomeActivity.this))
        {

            dialog = new ProgressDialog(HomeActivity.this);
            dialog.setMessage("Updating Database");
            dialog.setCancelable(false);

            commonServerSenderHelper = new UpdateToServerAllLocalDatabase(HomeActivity.this);

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
                                else if (dbHelper.checkLocalDepositCount())
                                {
                                    commonServerSenderHelper.DepositPostTest();
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


        } else {
            Toast.makeText(HomeActivity.this, "Please turn on network to update database,", Toast.LENGTH_LONG).show();
        }
    }

    private void dialogShow()
    {
        dialog.show();
    }

    private void dialogDismiss()
    {
        dialog.dismiss();
    }

    private static boolean isNetworkAvaliable(Context ctx) {
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
        } else {
            return false;
        }
    }


    public void DepositPostTest()
    {
        if (isNetworkAvaliable(HomeActivity.this))
        {
            new HttpAsyncTaskDeposit().execute(HelperStatic.INSERT_DEPOSIT_URL);
        } else
        {
            dialogDismiss();
        }
    }

    private String getDepositJson()
    {
        DbHelper dbHelper = new DbHelper(HomeActivity.this);
        return dbHelper.getDepositAllDataInJson();
    }

    private class HttpAsyncTaskDeposit extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {

            String s = getDepositJson();
            String row_index = s.substring(0, s.indexOf(","));
            String string_json = s.substring(s.indexOf(",") + 1, s.length());

            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);

            return row_index + "," + POSTDeposit(urls[0], string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(HomeActivity.this, "Deposit Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            Log.e("respones", "val = " + result.toString());

            String row_index = result.substring(0, result.indexOf(","));
            String string_json = result.substring(result.indexOf(",") + 1, result.length());

            try {
                JSONObject jsonObject = new JSONObject(string_json);

                if (jsonObject.getString("message").contains("Sucess"))
                {
                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

                    dbHelper.deleteLocalDeposit(row_index);

                    if (dbHelper.checkLocalDepositCount())
                    {
                        DepositPostTest();
                    }
                    else
                    {
                        checkAuthentication();
                    }
                }

            } catch (JSONException e) {
                dialogDismiss();
                e.printStackTrace();
            }
        }
    }






    public void attendancePostTest() {
        if (isNetworkAvaliable(HomeActivity.this)) {
            new HttpAsyncTaskAttedance().execute(HelperStatic.ATTENDANCE_URL);
        } else {
            dialogDismiss();
        }
    }

    public void geoTagMappingPostTest() {
        if (isNetworkAvaliable(HomeActivity.this)) {
            new HttpAsyncTaskGeoTagMapping().execute(HelperStatic.GEO_TAG_MAPPING_URL);
        } else {
            dialogDismiss();
        }
    }


    public void auditPointPostTest() {
        if (isNetworkAvaliable(HomeActivity.this)) {
            new HttpAsyncTaskAuditPoint().execute(HelperStatic.AUDIT_DETAILS_URL);
        } else {
            dialogDismiss();
        }
    }

    public void wareHouseInspectionPostTest() {
        if (isNetworkAvaliable(HomeActivity.this)) {
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
            Toast.makeText(HomeActivity.this, "Geo tag mapping Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

                    dbHelper.deleteLocalGeoTagMapping();
                    if (dbHelper.checkLocalWareHouseInspectionCount()) {

                        wareHouseInspectionPostTest();
                    } else if (dbHelper.checkLocalAtteanceCount()) {
                        attendancePostTest();
                    } else if (dbHelper.checkLocalDepositCount())
                    {
                        DepositPostTest();
                    }
                    else if (dbHelper.checkLocalGodownAuditPointCount()) {

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

        DbHelper dbHelper = new DbHelper(HomeActivity.this);

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
            // 7. Set some headers to inform server about the type of the content   

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


    private class HttpAsyncTaskWareHouseInspection extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + getWareHouseInspectionJsonForServer());
            return POSTWareHouseInspection(urls[0], getWareHouseInspectionJsonForServer());
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.e("respones", "respones val = " + result);

            Toast.makeText(HomeActivity.this, "wareHouse inspection Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

                    dbHelper.deleteLocalWareHouseInspection();

                    //logInLogic();
                    //checkAuthentication();
                    // attendancePostTest();


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
            // 7. Set some headers to inform server about the type of the content   

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

        DbHelper dbHelper = new DbHelper(HomeActivity.this);

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
        protected void onPostExecute(String result)
        {
            Toast.makeText(HomeActivity.this, "Attendance Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            Log.e("respones", "val = " + result.toString());


            String row_index = result.substring(0, result.indexOf(","));
            String string_json = result.substring(result.indexOf(",") + 1, result.length());

            try {
                JSONObject jsonObject = new JSONObject(string_json);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

                    dbHelper.deleteLocalAttendance(row_index);
                    if (dbHelper.checkLocalDepositCount())
                    {
                        Toast.makeText(HomeActivity.this, "Deposit Data Sending on Server = ", Toast.LENGTH_LONG).show();
                        DepositPostTest();
                    }

                    if (dbHelper.checkLocalAtteanceCount()) {
                        attendancePostTest();

                    }
                    else if (dbHelper.checkLocalGodownAuditPointCount()) {

                        auditPointPostTest();

                    }
                    else if (dbHelper.checkLocalDepositCount())
                    {
                        DepositPostTest();
                    }
                    else {
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
        DbHelper dbHelper = new DbHelper(HomeActivity.this);
        return dbHelper.getAttendanceAllDataInJson();
    }


    public String POSTDeposit(String url, String json) {
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
            // 7. Set some headers to inform server about the type of the content   

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
            // 7. Set some headers to inform server about the type of the content   

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
            Toast.makeText(HomeActivity.this, "AuditPoint Data Sent! result = " + s.toString(), Toast.LENGTH_LONG).show();

            String row_index = s.substring(0, s.indexOf(","));
            String string_json = s.substring(s.indexOf(",") + 1, s.length());


            try {
                JSONObject jsonObject = new JSONObject(string_json);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

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
            // 7. Set some headers to inform server about the type of the content   

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

        DbHelper dbHelper = new DbHelper(HomeActivity.this);

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


    /*private String getAttendanceJson() {
        DbHelper dbHelper = new DbHelper(HomeActivity.this);

        return dbHelper.getAttendanceAllDataInJson();
    }


    private String getGeoTagMappingJson() {

        DbHelper dbHelper = new DbHelper(HomeActivity.this);

        return dbHelper.getGeoTagMappingAllDataInJson();
    }


    private String getGodownDetailsJson() {

        DbHelper dbHelper = new DbHelper(HomeActivity.this);

        return dbHelper.getLocalAuditPointDetailsAllDataInJson();

    }

    private String getWareHouseInspectionJsonForServer() {

        DbHelper dbHelper = new DbHelper(HomeActivity.this);

        return dbHelper.getWareHouseInspectionJosnForServer();
    }


    public void geoTagMappingPostTest() {
        if (isNetworkAvaliable(HomeActivity.this)) {
            new HttpAsyncTaskGeoTagMapping().execute(HelperStatic.GEO_TAG_MAPPING_URL);
        } else {
            dialogDismiss();
        }
    }

    public void attendancePostTest() {
        if (isNetworkAvaliable(HomeActivity.this)) {
            new HttpAsyncTaskAttedance().execute(HelperStatic.ATTENDANCE_URL);
        } else {
            dialogDismiss();
        }
    }

    public void auditPointPostTest() {
        if (isNetworkAvaliable(HomeActivity.this)) {
            new HttpAsyncTaskAuditPoint().execute(HelperStatic.AUDIT_DETAILS_URL);
        } else {
            dialogDismiss();
        }
    }


    public void wareHouseInspectionPostTest() {
        if (isNetworkAvaliable(HomeActivity.this)) {
            new HttpAsyncTaskWareHouseInspection().execute(HelperStatic.SERVER_INSERT_WAREHOUSE_INSPECTION_URL);
        } else {
            dialogDismiss();
        }
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
            // 7. Set some headers to inform server about the type of the content   

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
            // 7. Set some headers to inform server about the type of the content   

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
            // 7. Set some headers to inform server about the type of the content   

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
            // 7. Set some headers to inform server about the type of the content   

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


    private class HttpAsyncTaskGeoTagMapping extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + getGeoTagMappingJson());
            return POSTGeoTagMapping(urls[0], getGeoTagMappingJson());
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(HomeActivity.this, "Geo tag mapping Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

                    dbHelper.deleteLocalGeoTagMapping();

                    auditPointPostTest();

                }

            } catch (JSONException e) {

                dialogDismiss();
                e.printStackTrace();

            }
        }
    }

    private class HttpAsyncTaskAttedance extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + getAttendanceJson());
            return POSTAttedance(urls[0], getAttendanceJson());
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(HomeActivity.this, "Attendance Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            Log.e("respones", "val = " + result.toString());


            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

                    dbHelper.deleteLocalAttendance();


                    geoTagMappingPostTest();

                }

            } catch (JSONException e) {
                dialogDismiss();
                e.printStackTrace();
            }


        }
    }

    private class HttpAsyncTaskAuditPoint extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + getGodownDetailsJson());
            return POSTAuditPoint(urls[0], getGodownDetailsJson());
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.e("Attendance", "Attendance = " + result.toString());
            Toast.makeText(HomeActivity.this, "Attendance Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();


            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

                    dbHelper.deleteLocalGodownAudit();

                    wareHouseInspectionPostTest();

                }

            } catch (JSONException e) {

                dialogDismiss();
                e.printStackTrace();
            }

        }
    }

    private class HttpAsyncTaskWareHouseInspection extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + getWareHouseInspectionJsonForServer());
            return POSTWareHouseInspection(urls[0], getWareHouseInspectionJsonForServer());
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.e("respones", "respones val = " + result);

            Toast.makeText(HomeActivity.this, "wareHouse inspection Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(HomeActivity.this);

                    dbHelper.deleteLocalWareHouseInspection();

                    //logInLogic();
                    checkAuthentication();
                }

            } catch (JSONException e) {

                dialogDismiss();
                e.printStackTrace();
            }

        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }*/

   /* private void logInLogic1() {
        putIsFirstPref(true);

        dialogDismiss();
        Toast.makeText(HomeActivity.this, "Please re-login to update database", Toast.LENGTH_LONG).show();

        HomeActivity.this.finish();

        Intent intent1 = new Intent(HomeActivity.this, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);


    }*/

    private void putIsFirstPref(boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_first", value);
        editor.commit();
    }


    //   Login Logic

    private void locationSetUp() {
        GPSTracker gps = new GPSTracker(HomeActivity.this, 10);
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

    private void checkAuthentication() {

        //locationSetUp();

        dbHelper.deleteAllTable();

        dialogShow();
        final ArrayList<String> user_data = dbHelper.getUserInfo(emp_no);
        FINAL_URL = HelperStatic.NEW_LOGIN_URL
                + user_data.get(2)
                + "/" + user_data.get(3)
                + "/" + user_data.get(0)
                + "/" + user_data.get(1)
                + "/" + STATE_NAME
                + "/" + "UpdateDatabase";


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

                        for (int i = 0; i < commonJsonArray.length(); i++) {
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

                        for (int i = 0; i < commonJsonArray.length(); i++) {
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

                        for (int i = 0; i < commonJsonArray.length(); i++) {
                            LocationCmAndCmpPojo pojo = new LocationCmAndCmpPojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setLocation_id(jsonObject.getString("locationid"));
                            pojo.setLocation_name(jsonObject.getString("LocationName"));
                            pojo.setState_id(jsonObject.getString("stateid"));
                            pojo.setState(dbHelper.getStateNameUsingId(jsonObject.getString("stateid")));
                            location_cm_list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }
                        commonJsonArray = null;

                        commonJsonArray = child.getJSONArray("Location_smp");

                        for (int i = 0; i < commonJsonArray.length(); i++) {
                            LocationCmAndCmpPojo pojo = new LocationCmAndCmpPojo();
                            JSONObject jsonObject = commonJsonArray.getJSONObject(i);

                            pojo.setLocation_id(jsonObject.getString("locationid"));
                            pojo.setLocation_name(jsonObject.getString("LocationName"));
                            pojo.setState_id(jsonObject.getString("stateid"));
                            pojo.setState(dbHelper.getStateNameUsingId(jsonObject.getString("stateid")));
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


        FINAL_URL_GODOWN = HelperStatic.GODOWN_URL + STATE_NAME;


        Log.e("FINAL_URL_GODOWN Url", " url = " + FINAL_URL_GODOWN);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_GODOWN, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject child = response.getJSONObject("GetGodownResult");

                    ArrayList<GodownCmAndCmpPojo> godown_cm_list = new ArrayList<GodownCmAndCmpPojo>();
                    ArrayList<GodownCmAndCmpPojo> godown_cmp_list = new ArrayList<GodownCmAndCmpPojo>();


                    JSONArray commonJsonArray = child.getJSONArray("Godown_cmp");


                    for (int i = 0; i < commonJsonArray.length(); i++) {
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

                    for (int i = 0; i < commonJsonArray.length(); i++) {
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

                    Log.e("POST resquest", "No. of godown_cm_list  = " + godown_cm_list.size());
                    Log.e("POST resquest", "No. of godown_cmp_list  = " + godown_cmp_list.size());

                    //System.out.print("No. of godown_cm_list" + godown_cm_list.size());
                    //System.out.print("No. of godown_cmp_list" + godown_cmp_list.size());

                    dbHelper.insertGodownTable(godown_cm_list, "CM");
                    dbHelper.insertGodownTable(godown_cmp_list, "CMP");


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


        FINAL_WAREHOUSE_URL = HelperStatic.WAREHOUSE_URL + STATE_NAME;


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


                    JSONArray commonJsonArray = child.getJSONArray("warehouse_ls_cmp");


                    for (int i = 0; i < commonJsonArray.length(); i++) {
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

                    for (int i = 0; i < commonJsonArray.length(); i++) {
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

                    Log.e("POST resquest", "No. of warehouse_list_cm  = " + warehouse_list_cm.size());
                    Log.e("POST resquest", "No. of warehouse_list_cmp  = " + warehouse_list_cmp.size());

                    //System.out.print("No. of warehouse_list_cm" + warehouse_list_cm.size());
                    //System.out.print("No. of warehouse_list_cmp" + warehouse_list_cmp.size());

                    dbHelper.insertwarehouseTable(warehouse_list_cm, "CM");
                    dbHelper.insertwarehouseTable(warehouse_list_cmp, "CMP");

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

    private void getAuditTypeAndAuditPoint() {

        FINAL_URL_AUDIT_TYPE_ADUTI_POINT = HelperStatic.AUDIT_POINT_AND_AUDIT_TYPE_URL;


        Log.e("audit Type and point", " url = " + FINAL_URL_AUDIT_TYPE_ADUTI_POINT);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_AUDIT_TYPE_ADUTI_POINT, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONObject child = response.getJSONObject("GetAudiTypeResult");

                    ArrayList<AuditTypePojo> audit_type_list = new ArrayList<AuditTypePojo>();
                    ArrayList<AuditPointDetailsServerPojo> audit_point_details_list = new ArrayList<AuditPointDetailsServerPojo>();

                    JSONArray commonJsonArray = child.getJSONArray("ls_Audit_type");


                    for (int i = 0; i < commonJsonArray.length(); i++) {
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

                    for (int i = 0; i < commonJsonArray.length(); i++) {
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

                    Log.e("POST resquest", "No. of audit_type_list  = " + audit_type_list.size());
                    Log.e("POST resquest", "No. of audit_point_details_list  = " + audit_point_details_list.size());

                    // System.out.print("No. of audit_type_list" + audit_type_list.size());
                    //System.out.print("No. of audit_point_details_list" + audit_point_details_list.size());

                    dbHelper.insertAuditTypeTable(audit_type_list);
                    dbHelper.insertAuditPointDetails(audit_point_details_list);

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


    private void getWareHouseInspection() {


        FINAL_URL_WAREHOUSE_INSPECTION = HelperStatic.WAREHOUSE_INSPECTION_URL;


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

                    if (jsonArray.length() > 0) {

                        for (int i = 0; i < jsonArray.length(); i++) {

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

                    Log.e("POST resquest", "No. of WareHouseInspectionMainTable  = " + list.size());

                    //System.out.print("No. of WareHouseInspectionMainTable" + list.size());
                    dbHelper.insertIntoWareHouseInspectionMainTable(list);


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


        FINAL_URL_BANK = HelperStatic.BANK_URL;


        Log.e("bank Url", " url = " + FINAL_URL_BANK);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_BANK, "", new Response.Listener<JSONObject>() {

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

                    if (jsonObjectParent.length() > 0) {


                        JSONArray jsonArray_bank = jsonObjectParent.getJSONArray("Bank");
                        JSONArray jsonArray_branch = jsonObjectParent.getJSONArray("Branch");


                        if (jsonArray_bank.length() > 0) {

                            for (int i = 0; i < jsonArray_bank.length(); i++) {

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


                        if (jsonArray_branch.length() > 0) {

                            for (int i = 0; i < jsonArray_branch.length(); i++) {

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

                        Log.e("POST resquest", "No. Bank  = " + list.size());
                        Log.e("POST resquest", "No. Branch = " + list_branch.size());

                        //System.out.print("No. Bank" + list.size());
                        //System.out.print("No. Branch" + list_branch.size());

                        dbHelper.insertBank(list);
                        dbHelper.insertBranch(list_branch);


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

    private void getUserRole() {


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

                        Log.e("POST resquest", "No. of UserRole  = " + list.size());

                        //System.out.print("No. of UserRole" + list.size());
                        dbHelper.insertUserRole(list);


                        getDistrict();
                    }


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


        FINAL_URL_DISTRICT = HelperStatic.DISTRICT_URL + STATE_NAME;


        Log.e("district Url", " url = " + FINAL_URL_DISTRICT);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, FINAL_URL_DISTRICT, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    ArrayList<DistrictPojo> list = new ArrayList<DistrictPojo>();


                    DistrictPojo pojo;

                    JSONObject jsonObject;

                    String DistrictID;
                    String DistrictName, StateID;


                    JSONArray jsonArray_district = response.getJSONArray("GetDistrictResult");

                    if (jsonArray_district.length() > 0) {


                        for (int i = 0; i < jsonArray_district.length(); i++) {

                            pojo = new DistrictPojo();

                            jsonObject = jsonArray_district.getJSONObject(i);

                            DistrictID = jsonObject.getString("DistrictID");
                            DistrictName = jsonObject.getString("DistrictName");
                            StateID = jsonObject.getString("StateID");


                            pojo.setDistrict_id(DistrictID);
                            pojo.setDistrict_name(DistrictName);
                            pojo.setState_id(StateID);


                            list.add(pojo);

                            pojo = null;
                            jsonObject = null;

                        }

                        Log.e("POST resquest", "No. of District  = " + list.size());

                        //System.out.print("No. of District" + list.size());
                        dbHelper.insertDistrict(list);

                        dialogDismiss();


                        Toast.makeText(HomeActivity.this, "Database Successfully Update", Toast.LENGTH_LONG).show();
                    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh)
        {
            /*
            //Start Home Activity
            finish();
            startActivity(getIntent());
            */

            localDBCount();
            Toast.makeText(HomeActivity.this, "Data Sync Successfully.", Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
