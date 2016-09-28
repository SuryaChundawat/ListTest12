package com.coder.sample;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.com.pojo.WareHouseInspectionDynamicPojo;
import com.database.DbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WareHouseInsepctionDynamicGrid extends AppCompatActivity {

    private LinearLayout linearLayout, linearLayout_list, linearLayout_edit_text, linearLayout_spinner, linearLayout_date;
    private Spinner spinner_particular, spinner_name, spinner_valuse;
    private ArrayAdapter adapter_particular, adapter_name;
    private EditText editText_values_text, editText_remarks;
    private TextView textView_values_date;
    private LayoutInflater inflater;
    private DbHelper dbHelper;
    private ArrayList<WareHouseInspectionDynamicPojo> list_child_row = new ArrayList<WareHouseInspectionDynamicPojo>();
    private String header_last_row_index;
    private ArrayList<WareHouseInspectionDynamicPojo> list_mandatory;

    // Validation for Value Textbox on clicking of ADD Button.
    private boolean isMandatory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house_insepction_dynamic_grid);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getref();


        spinner_particular.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setUiInvisible();
                setName(spinner_particular.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUiVisible(spinner_name.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textView_values_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(WareHouseInsepctionDynamicGrid.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        textView_values_date.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
                // dpd.getDatePicker().setMaxDate(new Date().getTime());
                dpd.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setParticular();
    }

    private void setUiVisible(String name) {

        editText_remarks.setText("");
        editText_values_text.setText("");
        textView_values_date.setText("");

        String ui = dbHelper.getUiNameWarehouseInspection(name);

        if (ui.contains("drop_down")) {
            linearLayout_spinner.setVisibility(View.VISIBLE);
            linearLayout_date.setVisibility(View.GONE);
            linearLayout_edit_text.setVisibility(View.GONE);
        } else if (ui.contains("date")) {
            linearLayout_date.setVisibility(View.VISIBLE);
            textView_values_date.setText(getDateTime());

            linearLayout_spinner.setVisibility(View.GONE);

            linearLayout_edit_text.setVisibility(View.GONE);

        } else if (ui.contains("number")) {
            linearLayout_edit_text.setVisibility(View.VISIBLE);
            editText_values_text.setInputType(InputType.TYPE_CLASS_NUMBER);

            linearLayout_spinner.setVisibility(View.GONE);
            linearLayout_date.setVisibility(View.GONE);


        } else if (ui.contains("text")) {
            linearLayout_edit_text.setVisibility(View.VISIBLE);
            editText_values_text.setInputType(InputType.TYPE_CLASS_TEXT);
            linearLayout_spinner.setVisibility(View.GONE);
            linearLayout_date.setVisibility(View.GONE);

        }

    }

    private void setUiInvisible() {
        linearLayout_spinner.setVisibility(View.GONE);
        linearLayout_date.setVisibility(View.GONE);
        linearLayout_edit_text.setVisibility(View.GONE);


    }

    private void setName(String particular) {
        adapter_name = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dbHelper.getWareHouseInspectionName(particular));
        adapter_name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_name.setAdapter(adapter_name);
    }

    private void setParticular() {
        adapter_particular = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dbHelper.getWareHouseinspectionParticularList());
        adapter_particular.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_particular.setAdapter(adapter_particular);
        if(spinner_particular.getCount() == 0)
        {
            linearLayout.setVisibility(View.GONE);
        }

    }




    private boolean checkMandatoryNameValue(WareHouseInspectionDynamicPojo pojo) {

        boolean flag = true;
        String message = "Unknow Name value";


        if (list_mandatory.size() > 0)
        {

            WareHouseInspectionDynamicPojo pojo_mandatory;
            for (int i = 0; i < list_mandatory.size(); i++)
            {

                pojo_mandatory = list_mandatory.get(i);

                if (pojo_mandatory.getParticular().toString().trim().contentEquals(pojo.getParticular().toString().trim()))
                {
                    //isMandatory = true;
                    Log.e("check name ","mandatory = "+pojo_mandatory.getName()+" name = "+pojo.getName() +" "+pojo_mandatory.getName().toString().trim().contentEquals(pojo.getName().toString().trim()));

                    if (pojo_mandatory.getName().toString().trim().contentEquals(pojo.getName().toString().trim()))
                    {
                        break;
                    }
                    else
                    {
                        if (i == (list_mandatory.size() - 1))
                        {
                            flag = false;
                            message = "Please Select Name = " + pojo_mandatory.getName();
                            Toast.makeText(WareHouseInsepctionDynamicGrid.this, message, Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                /*if (flag) {} else { break; }*/

                }
                pojo_mandatory = null;
            }


        } else {
            flag = false;
            message = "No mandatory fields found .";
            Toast.makeText(WareHouseInsepctionDynamicGrid.this, message, Toast.LENGTH_LONG).show();

        }


        boolean check_drop_down = dbHelper.checkDropDownMandatory(pojo.getParticular());

        Log.e("Check drop dopwn in DB", "check_drop_down = " + check_drop_down);

        if (check_drop_down == true) {

            WareHouseInspectionDynamicPojo pojo_mandatory;

            for (int i = 0; i < list_mandatory.size(); i++) {
                pojo_mandatory = list_mandatory.get(i);
                if (pojo_mandatory.getParticular().contains(pojo.getParticular())) {

                    list_mandatory.remove(i);
                    --i;
                    Log.e("remove particular ", "particular = " + pojo.getParticular());


                }

            }


        }


        return flag;
    }

    public void warehouseInspectionSubmit(View view) {

        boolean flag = true;
        String message = "";

        if (list_child_row.size() > 0) {

            if (list_mandatory.size() > 0) {

                WareHouseInspectionDynamicPojo pojo_mandatory;
                WareHouseInspectionDynamicPojo pojo;


                for (int i = 0; i < list_mandatory.size(); i++) {

                    pojo_mandatory = list_mandatory.get(i);

                    for (int j = 0; j < list_child_row.size(); j++) {

                        pojo = list_child_row.get(j);


                        if (pojo_mandatory.getName().toString().trim().contentEquals(pojo.getName().toString().trim())) {

                            break;

                        } else {


                            if (j == (list_child_row.size() - 1)) {
                                flag = false;
                                message = "Please add particular = " + pojo_mandatory.getParticular() + " and  name = " + pojo_mandatory.getName();

                                break;
                            }


                        }


                    }

                    if (flag) {

                    } else {
                        break;
                    }


                    pojo_mandatory = null;
                }


            } else {
                flag = false;
                message = "No mandatory fields found .";
            }


            if (flag)
            {

                dbHelper.insertLocalWareHouseInspection(list_child_row, header_last_row_index);
                dbHelper.updateLocalWareHouseInspectionDetailCount(list_child_row.size(), header_last_row_index);
                dbHelper.deleteWareHouseInspectionDynamicTemp();

                new CommonServerSenderHelper(WareHouseInsepctionDynamicGrid.this).wareHouseInspectionPostTest();

                WareHouseInsepctionDynamicGrid.this.finish();

                Intent intent1 = new Intent(WareHouseInsepctionDynamicGrid.this, HomeActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);

                Toast.makeText(WareHouseInsepctionDynamicGrid.this, "added successfully.", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(WareHouseInsepctionDynamicGrid.this, message, Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(WareHouseInsepctionDynamicGrid.this, "Please enter data . ", Toast.LENGTH_LONG).show();
        }


    }


    public void warehouseInspectionAdd(View view)
    {

        WareHouseInspectionDynamicPojo pojo = new WareHouseInspectionDynamicPojo();
        String string_values = "";


        String ui = dbHelper.getUiNameWarehouseInspection(spinner_name.getSelectedItem().toString());

        if (ui.contains("drop_down"))
        {
            string_values = spinner_valuse.getSelectedItem().toString().trim();

        }
        else if (ui.contains("date"))
        {
            string_values = textView_values_date.getText().toString().trim();

        }
        else if (ui.contains("number"))
        {
            string_values = editText_values_text.getText().toString().trim();

        }
        else if (ui.contains("text"))
        {
            string_values = editText_values_text.getText().toString().trim();
        }

        pojo.setRemark(editText_remarks.getText().toString().trim());
        pojo.setValues(string_values);
        pojo.setName(spinner_name.getSelectedItem().toString().trim());
        pojo.setParticular(spinner_particular.getSelectedItem().toString().trim());
        pojo.setType_flag(ui);

        // Validation Value
        if (list_mandatory.size() > 0)
        {
            WareHouseInspectionDynamicPojo pojo_mandatory;
            for (int i = 0; i < list_mandatory.size(); i++)
            {
                pojo_mandatory = list_mandatory.get(i);
                if (pojo_mandatory.getParticular().toString().trim().contentEquals(pojo.getParticular().toString().trim()))
                {
                    boolean check_multi_selection = dbHelper.checkMultiSelection(spinner_particular.getSelectedItem().toString());
                    if(!check_multi_selection)
                    {
                        isMandatory = true;
                    }
                    break;
                }
                pojo_mandatory = null;
            }
        }

        if(isMandatory && string_values.equals(""))
        {
            Toast.makeText(WareHouseInsepctionDynamicGrid.this, "Please Enter Value for : - - >  " + spinner_name.getSelectedItem().toString().trim(), Toast.LENGTH_LONG).show();
            isMandatory = false;
        }

        else if (checkMandatoryNameValue(pojo) == true)
        {
            isMandatory = false;
            list_child_row.add(pojo);

            TextView textView_particular_row, textView_name_row, textView_values_row, textView_remarks_row;
            View view_child = inflater.inflate(R.layout.warehouse_inspection_dynamic_row, null, false);

            textView_particular_row = (TextView) view_child.findViewById(R.id.text_view_particular_row);
            textView_name_row = (TextView) view_child.findViewById(R.id.text_view_name_row);
            textView_values_row = (TextView) view_child.findViewById(R.id.text_view_values_row);
            textView_remarks_row = (TextView) view_child.findViewById(R.id.text_view_remark_row);

            textView_particular_row.setText("PARTICULARS : " + spinner_particular.getSelectedItem().toString().trim());
            textView_name_row.setText("NAME : " + spinner_name.getSelectedItem().toString().trim());
            textView_values_row.setText("VALUES : " + string_values);
            textView_remarks_row.setText("REMARK : " + editText_remarks.getText().toString().trim());

            linearLayout_list.addView(view_child);

            setUiInvisible();
            editText_remarks.setText("");
            editText_values_text.setText("");
            textView_values_date.setText("");

            boolean check_multi_selection = dbHelper.checkMultiSelection(spinner_particular.getSelectedItem().toString());

            if (check_multi_selection)
            {

                for (int i = 0; i < adapter_name.getCount(); i++)
                {
                    dbHelper.changeParticularVisible(spinner_particular.getSelectedItem().toString(), adapter_name.getItem(i).toString());
                }

            }
            else
            {
                dbHelper.changeParticularVisible(spinner_particular.getSelectedItem().toString(), spinner_name.getSelectedItem().toString());
            }

            setParticular();

        }


    }

    public void getref()
    {

        dbHelper = AppController.getDatabaseInstance();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout_list = (LinearLayout) findViewById(R.id.linear_layout_list);

        linearLayout_edit_text = (LinearLayout) findViewById(R.id.linear_layout_edit_text);
        linearLayout_spinner = (LinearLayout) findViewById(R.id.linear_layout_spinner);
        linearLayout_date = (LinearLayout) findViewById(R.id.linear_layout_date);

        spinner_particular = (Spinner) findViewById(R.id.spinner_particular);
        spinner_name = (Spinner) findViewById(R.id.spinner_name);
        spinner_valuse = (Spinner) findViewById(R.id.spinner_values);

        editText_remarks = (EditText) findViewById(R.id.edit_text_remarks);
        editText_values_text = (EditText) findViewById(R.id.edit_text_values);

        textView_values_date = (TextView) findViewById(R.id.textview_date_pickaer);

        inflater = (LayoutInflater) WareHouseInsepctionDynamicGrid.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        header_last_row_index = getIntent().getStringExtra("header_last_row_index");

        dbHelper.insertIntoWareHouseInspectionTempTable();
        list_mandatory = dbHelper.getWareHouseInspectionMandatoryField();

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
                        dbHelper.deleteLocalWareHouseInspectionHeaderLastRowInserted(Long.parseLong(header_last_row_index));
                        WareHouseInsepctionDynamicGrid.this.finish();
                        Intent intent1 = new Intent(WareHouseInsepctionDynamicGrid.this, HomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
