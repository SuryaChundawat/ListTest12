package com.coder.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.database.DbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeoTagMappingActivity extends AppCompatActivity {

    private TextView textView_emp_id, textView_lattitute, textView_longitute, textView_id, textView_godown_address;
    private GPSTracker gps;
    private Spinner spinner_location, spinner_segment, spinner_godown, spinner_state;
    private ArrayAdapter<String> adapter_location, adapter_godown, adapter_segment, adapter_state;
    private DbHelper dbHelper;

    private String string_location_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_tag_mapping);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getRef();

        spinner_segment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //setGodown(spinner_segment.getSelectedItem().toString().trim().toUpperCase());
                //setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase());


                setState(spinner_segment.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (spinner_location.getCount() > 0)
                {
                    setLocation(spinner_segment.getSelectedItem().toString(), spinner_location.getSelectedItem().toString(), spinner_state.getSelectedItem().toString());
                }
                else
                {
                    setLocation(spinner_segment.getSelectedItem().toString(), string_location_name, spinner_state.getSelectedItem().toString());
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
                setGodown(spinner_segment.getSelectedItem().toString(), spinner_location.getSelectedItem().toString());

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

    }


    @Override
    protected void onResume() {
        super.onResume();
        locationSetUp();
        mappingSetUp();

    }

    private void mappingSetUp() {
        setSegment();


        textView_godown_address.setText(dbHelper.getGodownAddress(spinner_godown.getSelectedItem().toString()));
    }

    private void setState(String segment_name) {
        adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getState(segment_name));
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapter_state);
    }


    private void setGodown(String cmOrCmp, String locationName) {
        adapter_godown = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getGodownForGeoMapping(cmOrCmp, locationName));
        adapter_godown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_godown.setAdapter(adapter_godown);

    }

    private void setSegment() {
        adapter_segment = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getSegments_cmcmp());
        adapter_segment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_segment.setAdapter(adapter_segment);

    }

    private void setLocation(String cmOrCmp, String locationName, String state_name) {

        adapter_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getLocationArrayMappingGeoTag(locationName, cmOrCmp, state_name));
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(adapter_location);

    }


    public void getRef() {
        textView_emp_id = (TextView) findViewById(R.id.textview_emp_id);
        textView_id = (TextView) findViewById(R.id.textview_id);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        textView_godown_address = (TextView) findViewById(R.id.textview_godown_address);

        textView_lattitute = (TextView) findViewById(R.id.text_view_latitute);
        textView_longitute = (TextView) findViewById(R.id.text_view_longitute);

        spinner_location = (Spinner) findViewById(R.id.spinner_location);
        spinner_segment = (Spinner) findViewById(R.id.spinner_segment);
        spinner_godown = (Spinner) findViewById(R.id.spinner_godown);

        textView_emp_id.setText(getIntent().getExtras().getString("emp_id"));

        dbHelper = AppController.getDatabaseInstance();
    }

    public void geoTagSubmit(View view)
    {

        if (spinner_segment.getCount() > 0 && spinner_state.getCount() > 0 &&
            spinner_location.getCount() > 0 && spinner_godown.getCount() > 0 &&
            !(textView_lattitute.getText().toString().trim().equals("0.0")) && !(textView_lattitute.getText().toString().trim().equals("")) &&
            !(textView_longitute.getText().toString().trim().equals("0.0")) && !(textView_longitute.getText().toString().trim().equals("")))
        {

            dbHelper.changeActiveFlag(spinner_godown.getSelectedItem().toString());

            String id = textView_id.getText().toString();
            String emp_id = textView_emp_id.getText().toString();
            String lat = textView_lattitute.getText().toString();
            String lang = textView_longitute.getText().toString();
            String segment = spinner_segment.getSelectedItem().toString();
            String godown = spinner_godown.getSelectedItem().toString();
            String location = spinner_location.getSelectedItem().toString();
            String state = spinner_state.getSelectedItem().toString();
            String address = textView_godown_address.getText().toString();
            String current_date = getDateTime();

            dbHelper.insertGodownMapping(id, emp_id, lat, lang, segment, godown, location, state, address, current_date);
            new CommonServerSenderHelper(GeoTagMappingActivity.this).geoTagMappingPostTest();
            GeoTagMappingActivity.this.finish();
            Toast.makeText(GeoTagMappingActivity.this, "Successfully Updated.", Toast.LENGTH_LONG).show();

        }
        else
        {
            if (!(spinner_segment.getCount()>0))
            {
                Toast.makeText(GeoTagMappingActivity.this, "Segment Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_state.getCount()>0))
            {
                Toast.makeText(GeoTagMappingActivity.this, "State Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_location.getCount()>0))
            {
                Toast.makeText(GeoTagMappingActivity.this, "Location Empty.", Toast.LENGTH_LONG).show();
            }
            else if (!(spinner_godown.getCount()>0))
            {
                Toast.makeText(GeoTagMappingActivity.this, "Godown Empty.", Toast.LENGTH_LONG).show();
            }
            else if(textView_lattitute.getText().toString().trim().equals("0.0") || textView_lattitute.getText().toString().trim().equals(""))
            {
                Toast.makeText(GeoTagMappingActivity.this, "Not Valid lattitute .", Toast.LENGTH_LONG).show();
            }
            else if(textView_longitute.getText().toString().trim().equals("0.0") || textView_longitute.getText().toString().trim().equals(""))
            {
                Toast.makeText(GeoTagMappingActivity.this, "Not Valid longitutes .", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void locationSetUp() {
        gps = new GPSTracker(GeoTagMappingActivity.this, 10);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            textView_lattitute.setText("" + latitude);
            textView_longitute.setText("" + longitude);

            string_location_name = gps.getLocation_name();
            //string_state = gps.getState_name();

            //textView_state.setText(string_state);

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            //gps.showSettingsAlert();
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
                .setMessage("Do you want to exit GeoTagMapping ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        finish();
                        GeoTagMappingActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
