package com.coder.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.AuditPointDetailsAdapter;
import com.com.pojo.AuditPoinDetailsPojo;
import com.database.DbHelper;

import java.util.ArrayList;

public class AuditPointDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    // private AuditPointDetailsAdapter adapter;
    //private ListView listView_audit_point_details;
    private AuditPoinDetailsPojo pojo = null;
    private ArrayList<AuditPoinDetailsPojo> list;

    //private Spinner spinner_audit_point;
    //private RadioGroup radiocom.android.ddmlib.AdbCommandRejectedException: more than one deviceGroup_respones;
    // private RadioButton radioButton_check;
    // private EditText editText_remarks;// editText_details_1, editText_details_2, editText_summary;
    private Button button_next;
    private LinearLayout linearLayout_audit_point_list;
    private DbHelper dbHelper;
    private String string_segment_name_intent, string_audit_type_name_intent, string_audit_type_id_intent, string_segment_id_intent;
    private long header_insert_row_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audit_point_details);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getRef();


        button_next.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAuditRows();

    }

    private void setAuditRows() {

        LayoutInflater layoutInflater = (LayoutInflater) AuditPointDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        for (int i = 0; i < list.size(); i++) {

            final AuditPoinDetailsPojo pojo = list.get(i);

            View view = layoutInflater.inflate(R.layout.new_audit_detail_row, null, false);

            TextView textView_audit_point = (TextView) view.findViewById(R.id.text_view_audit_point_row_new);
            TextView textView_audit_point_index = (TextView) view.findViewById(R.id.text_view_audit_point_index_row_new);
            final EditText editText_remak = (EditText) view.findViewById(R.id.edit_text_audit_point_remark_row_new);
            final Spinner spinner_respones = (Spinner) view.findViewById(R.id.spinner_respones_row_new);

            textView_audit_point.setText(pojo.getAudit_point());
            textView_audit_point_index.setText("" + (i + 1) + ".");
//adsasdasd
            spinner_respones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinner_respones.getSelectedItem().toString().trim().contains("NO") && pojo.getGap_flag().contains("N")) {
                        //editText_remak.setVisibility(View.VISIBLE);
                        //editText_remak.setError("Mandatory Field");
                        Toast.makeText(AuditPointDetailsActivity.this,"Mandatory Field",Toast.LENGTH_LONG).show();
                    }

                    if (spinner_respones.getSelectedItem().toString().trim().contains("YES") && pojo.getGap_flag().contains("Y")) {
                        //editText_remak.setVisibility(View.VISIBLE);
                        //editText_remak.setError("Mandatory Field");
                        Toast.makeText(AuditPointDetailsActivity.this,"Mandatory Field",Toast.LENGTH_LONG).show();
                    }
                    if (spinner_respones.getSelectedItem().toString().trim().contains("N/A")) {
                        //editText_remak.setVisibility(View.VISIBLE);
                        //editText_remak.setError("Mandatory Field");
                        Toast.makeText(AuditPointDetailsActivity.this,"Mandatory Field",Toast.LENGTH_LONG).show();
                    }
                    pojo.setRespones_yes_no(spinner_respones.getSelectedItem().toString().trim());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

           /* spinner_respones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (spinner_respones.getSelectedItem().toString().trim().contains("NO")) {

                        editText_remak.setVisibility(View.VISIBLE);
                    }
                    pojo.setRespones_yes_no(spinner_respones.getSelectedItem().toString().trim());
                }
            });*/


            editText_remak.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (editText_remak.getText().toString().trim().length() > 0) {
                        // pojo.setRespones_yes_no(spinner_respones.getSelectedItem().toString().trim());
                        pojo.setRemark(editText_remak.getText().toString().trim());
                    }
                }
            });
            linearLayout_audit_point_list.addView(view, i);
        }

    }

    /*public void addClick() {
        int selectedId = radioGroup_respones.getCheckedRadioButtonId();
        radioButton_check = (RadioButton) findViewById(selectedId);

        if (radioButton_check.getText().toString().trim().equals("No")) {

            if (editText_remarks.getText().toString().trim().length() > 0) {
                pojo = new AuditPoinDetailsPojo();
                pojo.setAudit_point(spinner_audit_point.getSelectedItem().toString());
                pojo.setRemark(editText_remarks.getText().toString().trim());
                pojo.setRespones_yes_no("No");
                list.add(list.size(), pojo);
                adapter.notifyDataSetChanged();
                pojo = null;
            } else {
                Toast.makeText(AuditPointDetailsActivity.this, "Enter Remarks .", Toast.LENGTH_LONG).show();
            }
        } else if (radioButton_check.getText().toString().trim().equals("Yes")) {
            pojo = new AuditPoinDetailsPojo();
            pojo.setAudit_point(spinner_audit_point.getSelectedItem().toString());
            pojo.setRemark("No Remark");
            pojo.setRespones_yes_no("Yes");
            list.add(list.size(), pojo);
            adapter.notifyDataSetChanged();
            pojo = null;
        } else {
            Toast.makeText(AuditPointDetailsActivity.this, "Please Check Respones .", Toast.LENGTH_LONG).show();
        }

    }
*/

    public void nextClick() {
        /*if (editText_details_1.getText().toString().trim().length() > 0 && editText_details_2.getText().toString().trim().length() > 0 && editText_summary.getText().toString().trim().length() > 0) {
            Toast.makeText(AuditPointDetailsActivity.this, "Successfully Updated.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AuditPointDetailsActivity.this, "Enter Details 1,Details 2 and Summary  .", Toast.LENGTH_LONG).show();
        }*/
       /* int i = 0;
        boolean flag = true;
        AuditPoinDetailsPojo pojo = null;
        while (flag) {
            pojo = list.get(i);
            if (pojo.getRespones_yes_no().contains("Select Respones")) {
                flag = false;
            }
            i++;
        }

        if (flag){
            dbHelper.insertLocalAuditPointDetails(list);
            startActivity(new Intent(AuditPointDetailsActivity.this, AuditPointFinalActivity.class));
        }else {
            Toast.makeText(AuditPointDetailsActivity.this,"Please Select All Audit point . ",Toast.LENGTH_LONG).show();
        }*/


        boolean flag = true;
        AuditPoinDetailsPojo model;
        String point_num="";
        for (int i = 0; i < list.size(); i++) {
            //Log.e("all list items","list  = "+list.get(i).toString());
            point_num=""+(i+1);

            if (flag) {
                model = null;
                model = list.get(i);

                if (model.getRespones_yes_no().contains("Select Respones")) {
                    point_num=""+(i+1);
                    flag = false;
                    break;
                } else if (model.getRespones_yes_no().contains("NO") && model.getGap_flag().contains("N")) {
                    //editText_remak.setVisibility(View.VISIBLE);
                    if (model.getRemark().trim().length() > 0 && !(model.getRemark().trim().equals("nil"))) {

                    } else {

                        point_num=""+(i+1);
                        flag = false;
                        break;


                    }


                } else if (model.getRespones_yes_no().contains("YES") && model.getGap_flag().contains("Y")) {

                    //editText_remak.setVisibility(View.VISIBLE);

                    if (model.getRemark().trim().length() > 0 && !(model.getRemark().trim().equals("nil"))) {

                    } else {

                        point_num=""+(i+1);
                        flag = false;
                        break;


                    }

                } else if (model.getRespones_yes_no().contains("N/A")) {

                    //editText_remak.setVisibility(View.VISIBLE);

                    if (model.getRemark().trim().length() > 0 && !(model.getRemark().trim().equals("nil"))) {

                    } else {

                        point_num=""+(i+1);
                        flag = false;
                        break;


                    }

                }


            } else {
                break;
            }

        }


        if (flag == true) {


            long last_row_inserted_index = dbHelper.insertLocalAuditPointDetails(list, string_audit_type_id_intent, string_segment_id_intent);

            startActivity(new Intent(AuditPointDetailsActivity.this, AuditPointFinalActivity.class).putExtra("header_last_row_index", "" + header_insert_row_index).putExtra("all_audit_point_last_row_inserted_index", "" + last_row_inserted_index));

        } else {

            Toast.makeText(AuditPointDetailsActivity.this, "Please insert Mandatory field "+point_num, Toast.LENGTH_LONG).show();

        }


    }


    public void getRef() {


        //listView_audit_point_details = (ListView) findViewById(R.id.list_view_audit_point_details);
        // spinner_audit_point = (Spinner) findViewById(R.id.spinner_audit_point);
        //radioGroup_respones = (RadioGroup) findViewById(R.id.radio_group_respones);
        //editText_remarks = (EditText) findViewById(R.id.edit_text_remark);
/*        editText_details_1 = (EditText) findViewById(R.id.edit_text_detail_1);
        editText_details_2 = (EditText) findViewById(R.id.edit_text_detail_2);
        editText_summary = (EditText) findViewById(R.id.edit_text_audit_summary);*/

        //button_add = (Button) findViewById(R.id.button_add_audit_point_details);


        string_audit_type_name_intent = getIntent().getStringExtra("audit_type_name");
        string_audit_type_id_intent = getIntent().getStringExtra("audit_type_id");
        string_segment_id_intent = getIntent().getStringExtra("segment_id");


        string_segment_name_intent = getIntent().getStringExtra("segment_name");
        header_insert_row_index = Long.parseLong(getIntent().getStringExtra("insert_row_index"));

        button_next = (Button) findViewById(R.id.button_next_audit_point_details);


        dbHelper = AppController.getDatabaseInstance();
        list = dbHelper.getAuditpoint(string_segment_name_intent, string_audit_type_name_intent);

        linearLayout_audit_point_list = (LinearLayout) findViewById(R.id.linear_layout_audit_detail_list);


        //adapter = new AuditPointDetailsAdapter(AuditPointDetailsActivity.this, list);
        //listView_audit_point_details.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        // Toast.makeText(AuditPointDetailsActivity.this, "ccccccccccccccccccc.", Toast.LENGTH_LONG).show();


        switch (v.getId()) {
          /*  case R.id.button_add_audit_point_details:
                addClick();
                break;*/

            case R.id.button_next_audit_point_details:
                nextClick();
                break;
        }

    }



    @Override
    public void onBackPressed()
    {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit GodownAudit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {

                        dbHelper.deleteLocalGodownAuditHeaderLastInsertedRow(header_insert_row_index);
                        AuditPointDetailsActivity.this.finish();
                        Intent intent1 = new Intent(AuditPointDetailsActivity.this, HomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}

