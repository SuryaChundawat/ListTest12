package com.coder.sample;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

import com.database.DbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.ByteArrayOutputStream;

public class GodownAuditActivity extends AppCompatActivity
{
    private static final int CAMERA_REQUEST = 1889;

    private TextView textView_entry_type, textView_id, textView_godown_address;
    private TextView textView_date_and_time;
    private Spinner spinner_segment, spinner_auditor, spinner_location, spinner_godown, spinner_audit_type, spinner_state, spinner_warehouse;
    private ArrayAdapter<String> adapter_segment, adapter_auditor, adapter_location, adapter_godown, adapter_audit_type, adapter_state, adapter_warehouse;
    private DbHelper dbHelper;
    private GPSTracker gps;
    private String string_location_name;
    private ImageView imageView_pic;
    private Bitmap photo;
    private EditText editText_form_no;
    private LinearLayout linearLayout_radius;
    private String string_emp_name;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.godown_audit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getRef();
        string_emp_name=getEmpNameGlobal();
        locationSetUp();
        godownAuditSetup();

        spinner_segment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setState(spinner_segment.getSelectedItem().toString().trim().toUpperCase());
                setAuditType(spinner_segment.getSelectedItem().toString().trim().toUpperCase());
               /* setGodown(spinner_segment.getSelectedItem().toString().trim().toUpperCase());
                setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase());
                setwarehouse(spinner_segment.getSelectedItem().toString().trim().toUpperCase());*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_location.getCount() > 0) {
                    setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), spinner_location.getSelectedItem().toString().trim(), spinner_state.getSelectedItem().toString().trim());
                } else {
                    setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), string_location_name, spinner_state.getSelectedItem().toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                textView_godown_address.setText("");
                //setGodown(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), spinner_location.getSelectedItem().toString().trim());
                //setwarehouse(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), spinner_location.getSelectedItem().toString().trim());
                setwarehouse( spinner_location.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_godown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView_godown_address.setText(dbHelper.getGodownAddress(spinner_godown.getSelectedItem().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_warehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setGodown(spinner_segment.getSelectedItem().toString(), spinner_location.getSelectedItem().toString(), spinner_warehouse.getSelectedItem().toString());
                //textView_address.setText(dbHelper.getWarehouseAddress(spinner_warehouse.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        textView_date_and_time.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(GodownAuditActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                       /* textView_date_and_time.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);*/

                        //textView_date_and_time.setText((year)+"/"+(monthOfYear + 1) + "/"+dayOfMonth);
                        String sdayOfMonth,smonthOfYear;

                        if(dayOfMonth < 10)
                        {
                            sdayOfMonth = "0"+dayOfMonth;
                        }
                        else
                        {
                            sdayOfMonth = ""+dayOfMonth;
                        }

                        monthOfYear = monthOfYear+1;
                        if(monthOfYear < 10)
                        {
                            smonthOfYear = "0"+monthOfYear;
                        }
                        else
                        {
                            smonthOfYear = ""+monthOfYear;
                        }

                        textView_date_and_time.setText(sdayOfMonth + "/" + (smonthOfYear) + "/" + (year));

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(new Date().getTime());
                dpd.show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*string_emp_name=getEmpNameGlobal();
        locationSetUp();
        godownAuditSetup();*/
    }

    private void godownAuditSetup()
    {
        setSegment();
        setAuditor();
        textView_date_and_time.setText(getDateTime());
        //textView_godown_address.setText(dbHelper.getWarehouseAddress(spinner_warehouse.getSelectedItem().toString()));
       /* Log.e("ashu 1", "" + spinner_warehouse.getSelectedItem().toString());
        String warehouse_name = spinner_warehouse.getSelectedItem().toString().trim();
        Log.e("ashu 2", "" + dbHelper.getWarehouseAddress(warehouse_name));*/
    }

    private void setGodown(String cmOrCmp, String locationName, String warehouseName) {
        adapter_godown = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getGodownForGodownAudit(cmOrCmp, locationName, warehouseName));
        adapter_godown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_godown.setAdapter(adapter_godown);
    }


    private void setwarehouse(String locationName) {
        adapter_warehouse = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getWarehouse(locationName));
        adapter_warehouse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_warehouse.setAdapter(adapter_warehouse);
    }

    private void setState(String segment_name) {
        adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getState(segment_name));
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapter_state);
    }

    private void setAuditor() {
        adapter_auditor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{string_emp_name});
        adapter_auditor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_auditor.setAdapter(adapter_auditor);
    }

    private void setAuditType(String segment_name) {
        adapter_audit_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getAuditType(segment_name));
        adapter_audit_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_audit_type.setAdapter(adapter_audit_type);
    }

    private void setSegment() {
        adapter_segment = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getSegments_cmcmp());
        adapter_segment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_segment.setAdapter(adapter_segment);
    }

    private void setLocation(String cmOrCmp, String locationName, String state_name) {
        adapter_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getLocationArrayGodownAudit(locationName, cmOrCmp, state_name));
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(adapter_location);
    }

    private void locationSetUp() {
        gps = new GPSTracker(GodownAuditActivity.this, 10);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            string_location_name = gps.getLocation_name();
            // string_state = gps.getState_name();
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
        }
    }


    public void next(View view)
    {
        setAuditor();
        if ( spinner_segment.getCount()>0
                && spinner_audit_type.getCount()>0
                && spinner_auditor.getCount()>0
                && spinner_state.getCount()>0
                && spinner_location.getCount()>0
                && spinner_warehouse.getCount()>0
                && spinner_godown.getCount()>0
                && (photo != null))
        {
            String id = textView_id.getText().toString();
            String segment = spinner_segment.getSelectedItem().toString();
            String auditor = spinner_auditor.getSelectedItem().toString();
            String location = spinner_location.getSelectedItem().toString();
            String godown = spinner_godown.getSelectedItem().toString();
            String entry_type = textView_entry_type.getText().toString();
            String state = spinner_state.getSelectedItem().toString();
            String warehouse = spinner_warehouse.getSelectedItem().toString();
            String address = textView_godown_address.getText().toString();
            String audit_type = spinner_audit_type.getSelectedItem().toString();
            String form_no = editText_form_no.getText().toString();
            String current_date = getDateCurrentTime();
            String date=textView_date_and_time.getText().toString();
            // To Find Audit Type Id
            String audit_type_id = dbHelper.getaudit_type_id(audit_type);
            // To Find Sagment Id
            String segment_id = dbHelper.getsegment_id(segment);
           long long_insert_row_index= dbHelper.insertLocalGodownAudit(id, segment, auditor, location, godown, entry_type, state, warehouse, address,photo, audit_type, form_no, current_date,date);
            if (long_insert_row_index>0){
                startActivity(new Intent(GodownAuditActivity.this, AuditPointDetailsActivity.class).putExtra("insert_row_index",""+long_insert_row_index).putExtra("segment_name", spinner_segment.getSelectedItem().toString()).putExtra("audit_type_name", spinner_audit_type.getSelectedItem().toString()).putExtra("audit_type_id", audit_type_id).putExtra("segment_id", segment_id));
            }else {
                Toast.makeText(GodownAuditActivity.this,"Error while inserting data.Please re-enter data.",Toast.LENGTH_LONG).show();
            }
        } else {

            if (!(spinner_segment.getCount()>0))
            {
                Toast.makeText(GodownAuditActivity.this, "Segment Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_audit_type.getCount()>0))
            {
                Toast.makeText(GodownAuditActivity.this, "Audit Type Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_auditor.getCount()>0))
            {
                Toast.makeText(GodownAuditActivity.this, "Auditor Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_state.getCount()>0))
            {
                Toast.makeText(GodownAuditActivity.this, "State Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_location.getCount()>0))
            {
                Toast.makeText(GodownAuditActivity.this, "Location Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_warehouse.getCount() > 0))
            {
                Toast.makeText(GodownAuditActivity.this, "Warehouse Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_godown.getCount() > 0))
            {
                Toast.makeText(GodownAuditActivity.this, "Godown Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!( photo != null))
            {
                Toast.makeText(GodownAuditActivity.this, "photo Empty.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDateTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private String getDateCurrentTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public void getRef()
    {
        textView_id = (TextView) findViewById(R.id.textview_id);
        textView_entry_type = (TextView) findViewById(R.id.text_text_entry_type);
        textView_date_and_time = (TextView) findViewById(R.id.text_view_date_and_time);
        textView_godown_address = (TextView) findViewById(R.id.textview_address);
        spinner_segment = (Spinner) findViewById(R.id.spinner_segment);
        spinner_auditor = (Spinner) findViewById(R.id.spinner_auditor);
        spinner_location = (Spinner) findViewById(R.id.spinner_location);
        spinner_godown = (Spinner) findViewById(R.id.spinner_godown);
        spinner_audit_type = (Spinner) findViewById(R.id.spinner_audit_type);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_warehouse = (Spinner) findViewById(R.id.spinner_warehouse);
        editText_form_no = (EditText) findViewById(R.id.edit_text_form_no);
        imageView_pic = (ImageView) findViewById(R.id.image_view_attendance);
        dbHelper = AppController.getDatabaseInstance();
    }

    public void takePicGodown(View view)
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            photo = (Bitmap) data.getExtras().get("data");

            //Bitmap bJPGcompress = codec(photo, Bitmap.CompressFormat.JPEG,100);
            //photo = bJPGcompress;
            imageView_pic.setImageBitmap(photo);
            Toast.makeText(GodownAuditActivity.this, "Successfully capture image.", Toast.LENGTH_LONG).show();
            setAuditor();
        }
    }

    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,int quality)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    private String getEmpNameGlobal() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GodownAuditActivity.this);
        return preferences.getString("emp_name", "UNKNOW");
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog alertbox = new AlertDialog.Builder(this).setMessage("Do you want to exit GodownAudit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        finish();
                        GodownAuditActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}
