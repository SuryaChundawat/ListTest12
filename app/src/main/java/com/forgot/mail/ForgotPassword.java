package com.forgot.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Toast;

import com.coder.sample.HelperStatic;
import com.coder.sample.LoginActivity;
import com.coder.sample.R;
import com.database.DbHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private EditText editText_email;
    private String emp_id;
    private Mail m;
    private ProgressDialog dialog;
    private String string_email;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emp_id = getEmpNumberGlobal();
        editText_email = (EditText) findViewById(R.id.edit_text_email_forgot);
        dbHelper = new DbHelper(ForgotPassword.this);
        m = new Mail("dishaact@gmail.com", "dishaact2010");

    }

    private String getEmpNumberGlobal()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ForgotPassword.this);
        return preferences.getString("emp_number", "000000");
    }


    public void sendMail(View view)
    {

        if (isNetworkAvaliable(ForgotPassword.this))
        {


            string_email = editText_email.getText().toString().trim();

            if (emailValidator(string_email))
            {

                dialog = new ProgressDialog(ForgotPassword.this);
                dialog.setMessage("Updating Database to server");
                dialog.setCancelable(false);
                dialogShow();

                if (dbHelper.checkLocalGeoTagMappingCount())
                {
                    geoTagMappingPostTest();
                }
                else if (dbHelper.checkLocalWareHouseInspectionCount())
                {
                    wareHouseInspectionPostTest();
                }
                else if (dbHelper.checkLocalAtteanceCount())
                {
                    attendancePostTest();
                }
                else if (dbHelper.checkLocalGodownAuditPointCount())
                {
                    auditPointPostTest();
                }
                else
                {
                    new EmailHelper().execute(string_email);
                }

                // new EmailHelper().execute(string_email);
            }
            else
            {
                Toast.makeText(ForgotPassword.this, "Invalid Email.", Toast.LENGTH_LONG).show();
            }
    }

    else
    {
        Toast.makeText(ForgotPassword.this, "Please Turn On Internet.", Toast.LENGTH_LONG).show();
    }
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
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void dialogShow() {
        dialog.show();
    }

    private void dialogDismiss() {
        dialog.dismiss();
    }


    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void attendancePostTest() {
        if (isNetworkAvaliable(ForgotPassword.this)) {
            new HttpAsyncTaskAttedance().execute(HelperStatic.ATTENDANCE_URL);
        } else {
            dialogDismiss();
        }
    }

    public void geoTagMappingPostTest() {
        if (isNetworkAvaliable(ForgotPassword.this)) {
            new HttpAsyncTaskGeoTagMapping().execute(HelperStatic.GEO_TAG_MAPPING_URL);
        } else {
            dialogDismiss();
        }
    }


    public void auditPointPostTest() {
        if (isNetworkAvaliable(ForgotPassword.this)) {
            new HttpAsyncTaskAuditPoint().execute(HelperStatic.AUDIT_DETAILS_URL);
        } else {
            dialogDismiss();
        }
    }

    public void wareHouseInspectionPostTest() {
        if (isNetworkAvaliable(ForgotPassword.this)) {
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
            Toast.makeText(ForgotPassword.this, "Geo tag mapping Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(ForgotPassword.this);

                    dbHelper.deleteLocalGeoTagMapping();



                    if (dbHelper.checkLocalWareHouseInspectionCount()) {

                        wareHouseInspectionPostTest();
                    } else if (dbHelper.checkLocalAtteanceCount()) {
                        attendancePostTest();
                    } else if (dbHelper.checkLocalGodownAuditPointCount()) {

                        auditPointPostTest();

                    }else {
                        new EmailHelper().execute(string_email);

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

        DbHelper dbHelper = new DbHelper(ForgotPassword.this);

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

            Toast.makeText(ForgotPassword.this, "wareHouse inspection Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(ForgotPassword.this);

                    dbHelper.deleteLocalWareHouseInspection();

                    //logInLogic();
                    //checkAuthentication();
                   // attendancePostTest();




                  if (dbHelper.checkLocalAtteanceCount()) {
                        attendancePostTest();
                    } else if (dbHelper.checkLocalGodownAuditPointCount()) {

                        auditPointPostTest();

                    }else {
                      new EmailHelper().execute(string_email);

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

        DbHelper dbHelper = new DbHelper(ForgotPassword.this);

        return dbHelper.getWareHouseInspectionJosnForServer();
    }

    private class HttpAsyncTaskAttedance extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {




            String s=getAttendanceJson();
            String row_index=s.substring(0, s.indexOf(","));
            String string_json=s.substring(s.indexOf(",") + 1, s.length());

            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);

            return row_index+","+POSTAttedance(urls[0],string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(ForgotPassword.this, "Attendance Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();

            Log.e("respones", "val = " + result.toString());


            String row_index=result.substring(0,result.indexOf(","));
            String string_json=result.substring(result.indexOf(",")+1,result.length());

            try {
                JSONObject jsonObject = new JSONObject(string_json);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(ForgotPassword.this);

                    dbHelper.deleteLocalAttendance(row_index);

                    if (dbHelper.checkLocalAtteanceCount()){
                        attendancePostTest();
                    }else if (dbHelper.checkLocalGodownAuditPointCount()){
                        auditPointPostTest();
                    }else {
                        new EmailHelper().execute(string_email);

                    }


                }

            } catch (JSONException e) {
                dialogDismiss();
                e.printStackTrace();
            }


        }
    }

    private String getAttendanceJson() {
        DbHelper dbHelper = new DbHelper(ForgotPassword.this);

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




            String s=getGodownDetailsJson();
            String row_index=s.substring(0, s.indexOf(","));
            String string_json=s.substring(s.indexOf(",")+1,s.length());

            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);

            return row_index+","+POSTAuditPoint(urls[0], string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String s) {

            Log.e("AuditPoint", "AuditPoint = " + s.toString());
            Toast.makeText(ForgotPassword.this, "AuditPoint Data Sent! result = " + s.toString(), Toast.LENGTH_LONG).show();

            String row_index=s.substring(0, s.indexOf(","));
            String string_json=s.substring(s.indexOf(",")+1,s.length());


            try {
                JSONObject jsonObject = new JSONObject(string_json);

                if (jsonObject.getString("message").contains("Sucess")) {


                    DbHelper dbHelper = new DbHelper(ForgotPassword.this);

                    dbHelper.deleteLocalGodownAudit(row_index);


                    if (dbHelper.checkLocalGodownAuditPointCount()){
                        auditPointPostTest();
                    }
                    else {
                        new EmailHelper().execute(string_email);
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

        DbHelper dbHelper = new DbHelper(ForgotPassword.this);

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


class EmailHelper extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String flag = "false";

        m = new Mail("dishaact@gmail.com", "dishaact2010");

        String string_email = params[0];


        //String[] toArr = {"ashitosh.prolifique@gmail.com", "dishaact@gmail.com", string_email}; // This is an array, you can add more emails, just separate them with a coma
        String[] toArr = {"pratik.codeginger@gmail.com", "tech@codeginger.com", string_email}; // This is an array, you can add more emails, just separate them with a coma
        m.setTo(toArr); // load array to setTo function
        m.setFrom("dishaact@gmail.com"); // who is sending the email
        m.setSubject("Reset Password");
        m.setBody(" Request to reset password " + emp_id);

        try {
            // m.addAttachment("/sdcard/myPicture.jpg");  // path to file you want to attach
            if (m.send()) {
                // success
                flag = "true";
                Log.e("email", "Email was sent successfully.");
            } else {
                // failure
                flag = "false";
                Log.e("email", "Email was not sent.");
            }
        } catch (Exception e) {
            // some other problem
            e.printStackTrace();
            Log.e("exception ", "" + e.getLocalizedMessage());
            dialogDismiss();
            //Toast.makeText(ForgotPassword.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
        }

        return flag;
    }

    @Override
    protected void onPostExecute(String s) {


        Log.e("onPostExecute", " val = " + s);
        message(s);

    }

}


    private void message(String s) {
        dialogDismiss();
        if (s.contains("true")) {
            Toast.makeText(ForgotPassword.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            logInLogic1();

        } else {
            Toast.makeText(ForgotPassword.this, "Email was not sent.", Toast.LENGTH_LONG).show();
        }
    }

        private void logInLogic1() {
        putIsFirstPref(true);

        dialogDismiss();
       // Toast.makeText(ForgotPassword.this, "Please re-login to update database", Toast.LENGTH_LONG).show();

        ForgotPassword.this.finish();

        Intent intent1 = new Intent(ForgotPassword.this, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);


    }

    private void putIsFirstPref(boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ForgotPassword.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_first", value);
        editor.commit();
    }


}



