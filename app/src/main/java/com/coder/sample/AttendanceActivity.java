package com.coder.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;

import com.database.DbHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.ByteArrayOutputStream;

public class AttendanceActivity extends AppCompatActivity
{

    private static final int CAMERA_REQUEST = 1888;
    private TextView textView_radius_label, textView_emp_id, textView_emp_name, textView_date_and_time, textView_lattitute, textView_longitute, textView_id,textView_local_address;
    private EditText editText_radius, editText_remark, editText_on_behalf_id;

    private GPSTracker gps;
    private ImageView imageView_pic;
    private Bitmap photo;
    private Spinner spinner_location, spinner_segment, spinner_godown,spinner_state;
    private String location_name,string_local_address;
    private DbHelper dbHelper;
    private LinearLayout linearLayout_radius;
    private ArrayAdapter<String> adapter_location, adapter_godown, adapter_segment,adapter_state;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_layout);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getRef();

        locationSetUp(10);
        attendanceSetUp();


        spinner_segment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
               /* if (spinner_location.getCount() > 0) {
                    setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), spinner_location.getSelectedItem().toString().trim());
                } else {
                    setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), location_name);
                }*/
                setState(spinner_segment.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (spinner_location.getCount() > 0)
                {
                    setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), spinner_location.getSelectedItem().toString().trim(), spinner_state.getSelectedItem().toString().trim());
                }
                else
                {
                    setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), location_name, spinner_state.getSelectedItem().toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                setGodown(spinner_segment.getSelectedItem().toString(), spinner_location.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
        );



    }

   /* @Override
    protected void onResume() {
        super.onResume();
        locationSetUp(10);
        attendanceSetUp();

    }
*/
    private void attendanceSetUp() {

        textView_emp_name.setText(LoginActivity.EMP_NAME_GLOBAL);
        setSegment();
        //setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase());
        //setGodown(spinner_segment.getSelectedItem().toString().trim().toUpperCase(),spinner_location.getSelectedItem().toString().trim());


    }

    private void setState(String segment_name) {
        adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getState(segment_name));
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapter_state);
    }


    private void setGodown(String cmOrCmp,String locationName) {
        adapter_godown = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getGodownAttendance(cmOrCmp, locationName));
        adapter_godown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_godown.setAdapter(adapter_godown);

    }

    private void setSegment() {
        adapter_segment = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getSegments());
        adapter_segment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_segment.setAdapter(adapter_segment);

    }
    private void setLocation(String cmOrCmp, String locationName, String state_name) {

        adapter_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getLocationArrayMapping(locationName, cmOrCmp, state_name));
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(adapter_location);

    }

    public void attendanceSubmit(View view)
    {
        if (editText_on_behalf_id.getText().toString().trim().length() > 0 &&
            !(textView_lattitute.getText().toString().trim().equals("0.0")) && !(textView_lattitute.getText().toString().trim().equals("")) &&
            !(textView_longitute.getText().toString().trim().equals("0.0")) && !(textView_longitute.getText().toString().trim().equals("")) &&
            spinner_segment.getCount() > 0 && spinner_state.getCount() > 0 && spinner_location.getCount() > 0  && photo != null )
        {
            if (spinner_godown.getSelectedItem().toString().trim().contains("SELECT GODOWN"))
            {
                if (editText_remark.getText().toString().trim().length() > 0)
                {
                    attandanceStoreEmptyGodown();
                    new CommonServerSenderHelper(AttendanceActivity.this).attendancePostTest();
                    AttendanceActivity.this.finish();
                    Toast.makeText(AttendanceActivity.this, "added successfully.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(AttendanceActivity.this, "Please enter Remark", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                attandanceStore();
                new CommonServerSenderHelper(AttendanceActivity.this).attendancePostTest();
                AttendanceActivity.this.finish();
                Toast.makeText(AttendanceActivity.this, "added successfully.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            if(editText_on_behalf_id.getText().toString().trim().length() <= 0)
            {
                Toast.makeText(AttendanceActivity.this, "Please enter On behalf ID .", Toast.LENGTH_LONG).show();
            }
            else if(textView_lattitute.getText().toString().trim().equals("0.0") || textView_lattitute.getText().toString().trim().equals(""))
            {
                Toast.makeText(AttendanceActivity.this, "Not Valid lattitute .", Toast.LENGTH_LONG).show();
            }
            else if(textView_longitute.getText().toString().trim().equals("0.0") || textView_longitute.getText().toString().trim().equals(""))
            {
                Toast.makeText(AttendanceActivity.this, "Not Valid longitutes .", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_segment.getCount()>0))
            {
                Toast.makeText(AttendanceActivity.this, "Segment Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_state.getCount()>0))
            {
                Toast.makeText(AttendanceActivity.this, "State Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_location.getCount()>0))
            {
                Toast.makeText(AttendanceActivity.this, "Location Empty.", Toast.LENGTH_LONG).show();
            }
            else if( photo == null)
            {
                Toast.makeText(AttendanceActivity.this, "Please click picture.", Toast.LENGTH_LONG).show();
            }
            //editText_remark.setError("");
        }
    }


    public void attandanceStoreEmptyGodown()
    {
        String id = textView_id.getText().toString();
        String emp_id = textView_emp_id.getText().toString();
        String emp_name = textView_emp_name.getText().toString();
        String on_behalf_id = editText_on_behalf_id.getText().toString();
        String lat = textView_lattitute.getText().toString();
        String lang = textView_longitute.getText().toString();
        String location_name = spinner_location.getSelectedItem().toString();
        String segment = spinner_segment.getSelectedItem().toString();
        String godown = "";
        String date = textView_date_and_time.getText().toString();
        String remark = editText_remark.getText().toString();

        dbHelper.insertAttandance(id, emp_id, emp_name, on_behalf_id, lat, lang, location_name, segment, godown, date, remark, photo, string_local_address);


    }

    public void attandanceStore() {

        String id = textView_id.getText().toString();
        String emp_id = textView_emp_id.getText().toString();
        String emp_name = textView_emp_name.getText().toString();
        String on_behalf_id = editText_on_behalf_id.getText().toString();
        String lat = textView_lattitute.getText().toString();
        String lang = textView_longitute.getText().toString();
        String location_name = spinner_location.getSelectedItem().toString();
        String segment = spinner_segment.getSelectedItem().toString();
        String godown = spinner_godown.getSelectedItem().toString();
        String date = textView_date_and_time.getText().toString();
        String remark = editText_remark.getText().toString();

        dbHelper.insertAttandance(id, emp_id, emp_name, on_behalf_id, lat, lang, location_name, segment, godown, date, remark, photo, string_local_address);
    }


    public void takePic(View view)
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            photo = (Bitmap) data.getExtras().get("data");
            imageView_pic.setImageBitmap(photo);
            Toast.makeText(AttendanceActivity.this, "Successfully capture image.", Toast.LENGTH_LONG).show();
        }
    }

    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,int quality)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }


    public void getRef()
    {
        textView_radius_label = (TextView) findViewById(R.id.text_view_radius_in_meter);
        editText_radius = (EditText) findViewById(R.id.edit_text_radius_on_meter);
        linearLayout_radius = (LinearLayout) findViewById(R.id.linear_layout_radious);


        textView_emp_id = (TextView) findViewById(R.id.edit_text_emp_id);
        textView_id = (TextView) findViewById(R.id.textview_id);
        textView_emp_name = (TextView) findViewById(R.id.text_view_emp_name);
        textView_local_address = (TextView) findViewById(R.id.textview_local_address);

        textView_date_and_time = (TextView) findViewById(R.id.text_view_date_and_time);
        textView_lattitute = (TextView) findViewById(R.id.text_view_latitute);
        textView_longitute = (TextView) findViewById(R.id.text_view_longitute);
        editText_on_behalf_id = (EditText) findViewById(R.id.edit_text_on_behalf_id);
        editText_remark = (EditText) findViewById(R.id.edit_text_remark);
        imageView_pic = (ImageView) findViewById(R.id.image_view_attendance);

        spinner_location = (Spinner) findViewById(R.id.spinner_location);
        spinner_segment = (Spinner) findViewById(R.id.spinner_segment);
        spinner_godown = (Spinner) findViewById(R.id.spinner_godown);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);

        textView_emp_id.setText(getIntent().getExtras().getString("emp_id"));
        textView_date_and_time.setText(getDateTime());


        editText_on_behalf_id.setText(getIntent().getExtras().getString("emp_id"));

        dbHelper = AppController.getDatabaseInstance();


        if (isNetworkAvaliable(AttendanceActivity.this)) {
            // textView_radius_label.setVisibility(View.VISIBLE);
            // editText_radius.setVisibility(View.VISIBLE);
            // linearLayout_radius.setVisibility(View.VISIBLE);


        }

        editText_radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText_radius.getText().toString().trim().length()>0) {
                    locationSetUp(Long.parseLong(editText_radius.getText().toString().trim()));
                }
            }
        });




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

    private void locationSetUp(long l) {

        gps = new GPSTracker(AttendanceActivity.this, l);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            textView_lattitute.setText("" + latitude);
            textView_longitute.setText("" + longitude);

            location_name = gps.getLocation_name();
            string_local_address = gps.getLocal_address();

            textView_local_address.setText(string_local_address);

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }

    private String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit DepositeNext ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        finish();
                        AttendanceActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}
