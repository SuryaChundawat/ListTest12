package com.coderbroadcast;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.coder.sample.GPSTracker;
import com.coder.sample.HelperStatic;
import com.coder.sample.R;
import com.database.DbHelper;
import com.forgot.mail.Mail;

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


/**
 * Created by Ashitosh on 11-10-2015.
 */

public class UpdateToServerAllLocalDatabase
{

    private Context context;
    // GPS
    private GPSTracker gps;
    public String local_address;

    private Mail m;
    String string_json_Copy;
    private static boolean IsBackgroundWorking = false;

    public UpdateToServerAllLocalDatabase(Context context)
    {
        this.context = context;
    }

    private static boolean isNetworkAvaliable(Context ctx)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        if (isNetworkAvaliable(context))
        {
            new HttpAsyncTaskAttedance().execute(HelperStatic.ATTENDANCE_URL);
        }
        else
        {

        }
    }

    public void DepositPostTest()
    {
        if (isNetworkAvaliable(context))
        {
            new HttpAsyncTaskDeposit().execute(HelperStatic.INSERT_DEPOSIT_URL);
        }
        else
        {

        }
    }

    public void geoTagMappingPostTest()
    {
        if (isNetworkAvaliable(context))
        {
            new HttpAsyncTaskGeoTagMapping().execute(HelperStatic.GEO_TAG_MAPPING_URL);
        }
        else
        {
        }
    }


    public void auditPointPostTest()
    {
        if (isNetworkAvaliable(context))
        {
            new HttpAsyncTaskAuditPoint().execute(HelperStatic.AUDIT_DETAILS_URL);
        }
        else
        {
        }
    }

    public void wareHouseInspectionPostTest()
    {
        if (isNetworkAvaliable(context))
        {
            new HttpAsyncTaskWareHouseInspection().execute(HelperStatic.SERVER_INSERT_WAREHOUSE_INSPECTION_URL);
        }
        else
        {
        }

    }



    private class   HttpAsyncTaskGeoTagMapping extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls)
        {
            string_json_Copy = "";
            String string_json = getGeoTagMappingJson();
            string_json_Copy = string_json;
            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);
            return POSTGeoTagMapping(urls[0], string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(context, "Geo tag mapping Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();
            try
            {
                DbHelper dbHelper = new DbHelper(context);
                if(!result.equals(""))
                {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("message").contains("Sucess"))
                    {
                        dbHelper.deleteLocalGeoTagMapping();
                    }
                    else
                    {
                        String[] toArr = {"GeotagMapping", string_json_Copy, ""};
                        new EmailHelper().execute(toArr);
                    }
                }
                else
                {
                    String[] toArr = {"GeotagMapping", string_json_Copy, ""};
                    new EmailHelper().execute(toArr);
                }

                // Checking
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

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    private String getGeoTagMappingJson()
    {
        DbHelper dbHelper = new DbHelper(context);
        return dbHelper.getGeoTagMappingAllDataInJson();
    }

    public String POSTGeoTagMapping(String url, String json)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
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

        }
        catch (Exception e)
        {
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
            Toast.makeText(context, "wareHouse inspection Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();
            Log.e("respones", "respones val = " + result);

            String row_index=result.substring(0,result.indexOf(","));
            String string_json=result.substring(result.indexOf(",")+1,result.length());

            try
            {
                String tempInspId = "0";
                DbHelper dbHelper = new DbHelper(context);
                if(!string_json.equals(""))
                {
                    JSONObject jsonObject = new JSONObject(string_json);

                    if (jsonObject.getString("message").contains("Sucess"))
                    {
                        String s = jsonObject.getString("message");
                        tempInspId = s.substring(s.indexOf(",") + 1, s.length());

                        dbHelper.deleteLocalWareHouseInspection(row_index, tempInspId);
                    }
                    else
                    {
                        String[] toArr = {"WareHouseInspection", string_json_Copy, row_index, tempInspId};
                        new EmailHelper().execute(toArr);
                    }
                }
                else
                {
                    String[] toArr = {"WareHouseInspection", string_json_Copy, row_index, tempInspId};
                    new EmailHelper().execute(toArr);
                }

                if (dbHelper.checkLocalWareHouseInspectionCount())
                {
                    wareHouseInspectionPostTest();
                }
                else if (dbHelper.checkLocalAtteanceCount())
                {
                    attendancePostTest();
                }
                else if(dbHelper.checkLocalGodownAuditPointCount())
                {
                    auditPointPostTest();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String POSTWareHouseInspection(String url, String json)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
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

        }
        catch (Exception e)
        {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }

    private String getWareHouseInspectionJsonForServer()
    {
        DbHelper dbHelper = new DbHelper(context);
        return dbHelper.getWareHouseInspectionJosnForServer();
    }

    private class HttpAsyncTaskAttedance extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            string_json_Copy = "";
            String s=getAttendanceJson();
            String row_index=s.substring(0, s.indexOf(","));
            String string_json=s.substring(s.indexOf(",") + 1, s.length());
            string_json_Copy = string_json;

            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);
            return row_index+","+POSTAttedance(urls[0],string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(context, "Attendance Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();
            Log.e("respones", "val = " + result.toString());

            String row_index=result.substring(0,result.indexOf(","));
            String string_json=result.substring(result.indexOf(",")+1,result.length());

            try
            {
                DbHelper dbHelper = new DbHelper(context);
                if(!string_json.equals(""))
                {
                    JSONObject jsonObject = new JSONObject(string_json);

                    if (jsonObject.getString("message").contains("Sucess"))
                    {
                        dbHelper.deleteLocalAttendance(row_index);
                    }
                    else
                    {
                        String[] toArr = {"Attendance", string_json_Copy, row_index};
                        new EmailHelper().execute(toArr);
                    }
                }
                else
                {
                    String[] toArr = {"Attendance", string_json_Copy, row_index};
                    new EmailHelper().execute(toArr);
                }

                if (dbHelper.checkLocalAtteanceCount())
                {
                    attendancePostTest();
                }
                else if (dbHelper.checkLocalDepositCount())
                {
                    DepositPostTest();
                }
                else if (dbHelper.checkLocalGodownAuditPointCount())
                {
                    auditPointPostTest();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    private class HttpAsyncTaskDeposit extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            string_json_Copy = "";
            String s=getDepositJson();
            String row_index=s.substring(0, s.indexOf(","));
            String string_json=s.substring(s.indexOf(",") + 1, s.length());
            string_json_Copy = string_json;

            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);
            return row_index+","+POSTDeposit(urls[0],string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(context, "Deposit Data Sent! result = " + result.toString(), Toast.LENGTH_LONG).show();
            Log.e("respones", "val = " + result.toString());

            String row_index=result.substring(0,result.indexOf(","));
            String string_json=result.substring(result.indexOf(",")+1,result.length());

            try
            {
                DbHelper dbHelper = new DbHelper(context);
                if(!string_json.equals(""))
                {
                    JSONObject jsonObject = new JSONObject(string_json);

                    if (jsonObject.getString("message").contains("Sucess"))
                    {
                        dbHelper.deleteLocalDeposit(row_index);
                    }
                    else
                    {
                        String[] toArr = {"Deposit", string_json_Copy, row_index};
                        new EmailHelper().execute(toArr);
                    }
                }
                else
                {
                    String[] toArr = {"Deposit", string_json_Copy, row_index};
                    new EmailHelper().execute(toArr);
                }

                if (dbHelper.checkLocalDepositCount())
                {
                    DepositPostTest();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    private String getAttendanceJson()
    {
        DbHelper dbHelper = new DbHelper(context);
        return dbHelper.getAttendanceAllDataInJson1(context);
    }

    private String getDepositJson()
    {
        DbHelper dbHelper = new DbHelper(context);
        return dbHelper.getDepositAllDataInJson1(context);
    }


    public String POSTAttedance(String url, String json)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
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

        }
        catch (Exception e)
        {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }

    public String POSTDeposit(String url, String json)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
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

        }
        catch (Exception e)
        {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }



    private class HttpAsyncTaskAuditPoint extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            string_json_Copy = "";
            String s=getGodownDetailsJson();
            String row_index=s.substring(0, s.indexOf(","));
            String string_json=s.substring(s.indexOf(",")+1,s.length());
            string_json_Copy = string_json;

            Log.e("POST resquest", "url = " + urls[0] + " json String val = " + string_json);
            return row_index+","+POSTAuditPoint(urls[0], string_json);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String s)
        {
            Log.e("AuditPoint", "AuditPoint = " + s.toString());
            Toast.makeText(context, "AuditPoint Data Sent! result = " + s.toString(), Toast.LENGTH_LONG).show();

            String row_index=s.substring(0, s.indexOf(","));
            String string_json=s.substring(s.indexOf(",")+1,s.length());

            try
            {
                DbHelper dbHelper = new DbHelper(context);

                if(!string_json.equals(""))
                {
                    JSONObject jsonObject = new JSONObject(string_json);

                    if (jsonObject.getString("message").contains("Sucess"))
                    {
                        dbHelper.deleteLocalGodownAudit(row_index);
                    }
                    else
                    {
                        String[] toArr = {"GodownAudit", string_json_Copy, row_index};
                        new EmailHelper().execute(toArr);
                    }
                }
                else
                {
                    String[] toArr = {"GodownAudit", string_json_Copy, row_index};
                    new EmailHelper().execute(toArr);
                }

                if (dbHelper.checkLocalGodownAuditPointCount())
                {
                    auditPointPostTest();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String POSTAuditPoint(String url, String json)
    {
        InputStream inputStream = null;
        String result = "";
        try
        {
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

        }
        catch (Exception e)
        {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return result;
    }

    private String getGodownDetailsJson()
    {
        DbHelper dbHelper = new DbHelper(context);
        return dbHelper.getLocalAuditPointDetailsAllDataInJson();
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    class EmailHelper extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            //String[] toArr = {"Attendance", string_json_Copy, row_index};
            String module_name = params[0];
            String string_json_Copy = params[1];
            String row_index = params[2];
            String tempInspId = params[3];

            String email_flag = "false";
            //m = new Mail("dishaact@gmail.com", "dishaact2010");
            m = new Mail(context.getResources().getString(R.string.email_username), context.getResources().getString(R.string.email_password));

            //String[] toArr = {"pratik.codeginger@gmail.com", "tech@codeginger.com", "parag.r@ncml.com"}; // This is an array, you can add more emails, just separate them with a comma
            String[] toArr = {"pratik.codeginger@gmail.com", "tech@codeginger.com"}; // This is an array, you can add more emails, just separate them with a comma
            m.setTo(toArr); // load array to setTo function
            m.setFrom("dishaact@gmail.com"); // who is sending the email
            m.setSubject("NCML Mobile Application : " + module_name + " : UnsuccessFull Record");
            m.setBody("NCML Mobile Application : " + context.getString(R.string.app_Version) + " : " + module_name + " : UnsuccessFull Record" + "\n\nData :\nYou can see this data on 'http://json.parser.online.fr/'\n\n" +  string_json_Copy);
            try
            {
                // m.addAttachment("/sdcard/myPicture.jpg");  // path to file you want to attach
                if (m.send())
                {
                    // success
                    email_flag = "true";
                    Log.e("email", "Email was sent successfully.");
                }
                else
                {
                    // failure
                    email_flag = "false";
                    Log.e("email", "Email was not sent.");
                }
            }
            catch (Exception e)
            {
                // some other problem
                e.printStackTrace();
                Log.e("exception ", "" + e.getLocalizedMessage());
                //Toast.makeText(ForgotPassword.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            }

            if(email_flag == "true")
            {
                DbHelper dbHelper = new DbHelper(context);
                if(module_name.contains("Attendance"))
                {
                    dbHelper.deleteLocalAttendance(row_index);
                }
                else if(module_name.contains("GodownAudit"))
                {
                    dbHelper.deleteLocalGodownAudit(row_index);
                }
                else if(module_name.contains("GeotagMapping"))
                {
                    dbHelper.deleteLocalGeoTagMapping();
                }
                else if(module_name.contains("WareHouseInspection"))
                {
                    dbHelper.deleteLocalWareHouseInspection(row_index, tempInspId);
                }
            }
            return row_index;
        }

        @Override
        protected void onPostExecute(String row_index)
        {
        }
    }

    private void putIsBackgroundWorkingPref(boolean value, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("IsBackgroundWorking", value);
        editor.commit();
    }

    private boolean getIsBackgroundWorkingPref(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("IsBackgroundWorking", false);
    }

}





