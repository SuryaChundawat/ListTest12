package com.coder.sample;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.database.DbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WarehouseInspectionActivity extends AppCompatActivity {

    private TextView textView_date;
    private EditText editText_inspection_no, edit_text_pin_code, edit_text_godown_address, edit_text_no_of_godowns, edit_text_name_of_godown_owner, edit_text_contact_no;

    private Spinner spinner_state, spinner_location, spinner_district, spinner_cc_location, spinner_survey_bank, spinner_branch_name, spinner_survey_done_by_name, spinner_designation;

    private ArrayAdapter<String> adapter_state, adapter_location, adapter_district, adapter_cc_location, adapter_survey_bank, adapter_branch_name, adapter_survey_done_by_name, adapter_designation;

    private DbHelper dbHelper;

    private String string_emp_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_inspection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getref();

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setDistrict(spinner_state.getSelectedItem().toString());
                //setLocation(spinner_state.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }

        });

        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setLocation(spinner_district.getSelectedItem().toString());
                //setDistrict(spinner_state.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }

        });


        spinner_survey_bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                setBankBranchName(spinner_survey_bank.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        textView_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dpd = new DatePickerDialog(WarehouseInspectionActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth)
                    {
                        /*textView_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);*/
                        // textView_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" +(year) );


                        String sdayOfMonth, smonthOfYear;

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

                        textView_date.setText(sdayOfMonth + "/" + (smonthOfYear) + "/" + (year));

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
        setUp();
    }

    private void setUp() {

        setState();
        // setCcLocation();
        setSurveyName();
        // setDesignation();
        setBankName();
    }

    private void setBankName() {

        adapter_survey_bank = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getBankNameArray());
        adapter_survey_bank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_survey_bank.setAdapter(adapter_survey_bank);
    }

    private void setBankBranchName(String bank_name) {

        adapter_branch_name = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getBankBranchNameArray(bank_name));
        adapter_branch_name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_branch_name.setAdapter(adapter_branch_name);
    }

    private void setDesignation() {

        adapter_designation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{dbHelper.getDesignationName(string_emp_name)});
        adapter_designation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_designation.setAdapter(adapter_designation);

    }

    private void setSurveyName() {

        adapter_survey_done_by_name = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{string_emp_name});
        adapter_survey_done_by_name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_survey_done_by_name.setAdapter(adapter_survey_done_by_name);

    }

    private void setCcLocation() {

        adapter_cc_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{string_emp_name});
        adapter_cc_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cc_location.setAdapter(adapter_cc_location);

    }

    private void setState()
    {
        adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getState("CM"));
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_state.setAdapter(adapter_state);
    }

    private void setDistrict(String state_name)
    {
        adapter_district = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getDistrictArray(state_name));
        adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_district.setAdapter(adapter_district);
    }

    private void setLocation(String district_name)
    {
        adapter_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getLocationArrayUsingState(district_name));
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(adapter_location);
    }

    private void getref()
    {
        editText_inspection_no = (EditText) findViewById(R.id.edit_text_inspection_no);
        edit_text_pin_code = (EditText) findViewById(R.id.edit_text_pin_code);
        edit_text_name_of_godown_owner = (EditText) findViewById(R.id.edit_text_name_of_godown_owner);
        edit_text_godown_address = (EditText) findViewById(R.id.edit_text_godown_address);
        edit_text_no_of_godowns = (EditText) findViewById(R.id.edit_text_no_of_godowns);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_location = (Spinner) findViewById(R.id.spinner_location);
        spinner_district = (Spinner) findViewById(R.id.spinner_district);
        spinner_survey_bank = (Spinner) findViewById(R.id.spinner_survey_bank);
        spinner_branch_name = (Spinner) findViewById(R.id.spinner_branch_name);
        spinner_survey_done_by_name = (Spinner) findViewById(R.id.spinner_survey_done_by_name);

        textView_date = (TextView) findViewById(R.id.textview_date);
        spinner_cc_location = (Spinner) findViewById(R.id.spinner_cc_location);
        spinner_designation = (Spinner) findViewById(R.id.spinner_designation);
        edit_text_contact_no = (EditText) findViewById(R.id.edit_text_contact_no);

        //textView_date.setText(getDateTime());
        dbHelper = AppController.getDatabaseInstance();
        string_emp_name = getEmpNameGlobal();
    }

    public void warehouseInspection(View view)
    {

        if (editText_inspection_no.getText().toString().length() > 0 &&
                edit_text_pin_code.getText().toString().length() > 5 &&
                edit_text_godown_address.getText().toString().length() > 0 &&
                edit_text_no_of_godowns.getText().toString().length() > 0 &&
                edit_text_name_of_godown_owner.getText().toString().length() > 0 &&
                spinner_state.getCount() > 0 &&
                spinner_location.getCount() > 0 &&
                spinner_district.getCount() > 0 &&
                spinner_survey_bank.getCount() > 0 &&
                spinner_branch_name.getCount() > 0 &&
                spinner_survey_done_by_name.getCount() > 0
                //spinner_cc_location.getCount() > 0 &&
                //spinner_designation.getCount() > 0 &&
                //edit_text_contact_no.getText().toString().length() > 9
                )
        {

        try
        {
            String inspection_no, state, location_name, pin_code, district, godown_address, noofgodowns, godown_owner, bank, branch,
                   survey_done_by_name, create_date_by_user, date = "", cc_location = "", designation = "", contact_no = "";

            inspection_no = editText_inspection_no.getText().toString();
            state = spinner_state.getSelectedItem().toString();
            location_name = spinner_location.getSelectedItem().toString();
            pin_code = edit_text_pin_code.getText().toString();
            district = spinner_district.getSelectedItem().toString();
            godown_address = edit_text_godown_address.getText().toString();
            noofgodowns  = edit_text_no_of_godowns.getText().toString();
            godown_owner = edit_text_name_of_godown_owner.getText().toString();
            bank = spinner_survey_bank.getSelectedItem().toString();
            branch = spinner_branch_name.getSelectedItem().toString();
            survey_done_by_name = spinner_survey_done_by_name.getSelectedItem().toString();
            create_date_by_user = getDateCurrentTime();

            // date = textView_date.getText().toString();
            // cc_location = spinner_cc_location.getSelectedItem().toString();
            // designation = spinner_designation.getSelectedItem().toString();
            // contact_no = edit_text_contact_no.getText().toString();

            /*
            long last_row_index=dbHelper.insertLocalWareHouseInspectionHeader(date, inspection_no, state, location_name, pin_code, district, godown_address, godown_owner, cc_location, bank, branch, survey_done_by_name, designation, contact_no, create_date_by_user);

            if (last_row_index>0)
            {
                Toast.makeText(WarehouseInspectionActivity.this, "Successfully added.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(WarehouseInspectionActivity.this, WareHouseInsepctionDynamicGrid.class).putExtra("header_last_row_index",""+last_row_index));
            }
            else
            {
                Toast.makeText(WarehouseInspectionActivity.this, "Error while inserting data.Please re-enter data.", Toast.LENGTH_LONG).show();
            }
            */

            // Check Warehouse Inspection No Exist or Not
            ArrayList<String> warehouseInformation = dbHelper.getWarehouseInspectionHeaderDetail(inspection_no);
            if (warehouseInformation.size() == 0)
            {
                // Warehouse Inspection No. Not Exist

                AlertDialog alertbox = new AlertDialog.Builder(this)
                        .setTitle("WareHouse Inspection No. Not Exist")
                        .setMessage("Do you want to discard this changes ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                finish();
                                WarehouseInspectionActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                /*
                long last_row_index = dbHelper.insertLocalWareHouseInspectionHeader(date, inspection_no, state, location_name, pin_code, district, godown_address, godown_owner, cc_location, bank, branch, survey_done_by_name, designation, contact_no, create_date_by_user);

                if (last_row_index > 0)
                {
                    Toast.makeText(WarehouseInspectionActivity.this, "Successfully added.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(WarehouseInspectionActivity.this, WareHouseInsepctionDynamicGrid.class).putExtra("header_last_row_index", "" + last_row_index));
                }
                else
                {
                    Toast.makeText(WarehouseInspectionActivity.this, "Error while inserting data.Please re-enter data.", Toast.LENGTH_LONG).show();
                }
                */


            }
            else
            {
                // Warehouse Inspection No. Exist

                if (warehouseInformation.get(2) != null)
                {
                    int tempcommonId = Integer.parseInt(warehouseInformation.get(0));
                    int tempInspId = Integer.parseInt(warehouseInformation.get(1));
                    int tempInspDetailCount = Integer.parseInt(warehouseInformation.get(2));

                    long last_row_index = dbHelper.updateLocalWareHouseInspectionHeader(date, inspection_no, state, location_name, pin_code, district, godown_address, godown_owner, cc_location, bank, branch, survey_done_by_name, designation, contact_no, create_date_by_user, tempcommonId, tempInspId, noofgodowns);

                    if (tempInspDetailCount > 0)
                    {
                        // Warehouse Inspection Detail Exist
                        AlertDialog alertbox = new AlertDialog.Builder(this)
                                .setTitle("WareHouse Inspection Detail Already Exist")
                                .setMessage("Do you want to Submit WareHouse Inspection ?")
                                .setPositiveButton("Submit", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface arg0, int arg1)
                                    {
                                        // Submit
                                        new CommonServerSenderHelper(WarehouseInspectionActivity.this).wareHouseInspectionPostTest();
                                        WarehouseInspectionActivity.this.finish();
                                        Intent intent1 = new Intent(WarehouseInspectionActivity.this, HomeActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent1);
                                        Toast.makeText(WarehouseInspectionActivity.this, "Saved successfully.", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                    else
                    {
                        // Warehouse Inspection Detail Not Exist
                        Toast.makeText(WarehouseInspectionActivity.this, "Successfully added.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(WarehouseInspectionActivity.this, WareHouseInsepctionDynamicGrid.class).putExtra("header_last_row_index", "" + tempcommonId));
                    }
                }

            }

        }
        catch (Exception e)
        {
            System.out.print(e);
        }

        }
        else if (!(editText_inspection_no.getText().toString().trim().length() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Inspection No empty.", Toast.LENGTH_LONG).show();
        }
        else if ((edit_text_pin_code.getText().toString().trim().length() <5 ))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Enter 6 digit Pin code.", Toast.LENGTH_LONG).show();
        }
        else if (!(edit_text_godown_address.getText().toString().trim().length() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Godown Address empty.", Toast.LENGTH_LONG).show();
        }
        else if (!(edit_text_no_of_godowns.getText().toString().trim().length() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "No of Godowns empty.", Toast.LENGTH_LONG).show();
        } else if (!(edit_text_name_of_godown_owner.getText().toString().trim().length() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Name of godown owner empty.", Toast.LENGTH_LONG).show();
        }
        else if (!(spinner_state.getCount() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "State empty.", Toast.LENGTH_LONG).show();
        }
        else if (!(spinner_location.getCount() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Location empty.", Toast.LENGTH_LONG).show();
        }
        else if (!(spinner_district.getCount() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "District empty.", Toast.LENGTH_LONG).show();
        }
        else if (!(spinner_survey_bank.getCount() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Bank empty.", Toast.LENGTH_LONG).show();
        }
        else if (!(spinner_branch_name.getCount() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Branch empty.", Toast.LENGTH_LONG).show();
        }
        else if (!(spinner_survey_done_by_name.getCount() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Survey done by name empty.", Toast.LENGTH_LONG).show();
        }


/*
        else if ((edit_text_contact_no.getText().toString().trim().length() < 10 ))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Enter 10 digit Contact no.", Toast.LENGTH_LONG).show();
        }
        else if (!(spinner_cc_location.getCount() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "CC location empty.", Toast.LENGTH_LONG).show();
        }
        else if (!(spinner_designation.getCount() > 0))
        {
            Toast.makeText(WarehouseInspectionActivity.this, "Designation empty.", Toast.LENGTH_LONG).show();
        }
*/

    }


    private String getEmpNameGlobal() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WarehouseInspectionActivity.this);
        return preferences.getString("emp_name", "UNKNOW");
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


    @Override
    public void onBackPressed()
    {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit WareHouseInspection ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        finish();
                        WarehouseInspectionActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
