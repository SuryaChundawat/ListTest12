package com.coder.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.database.DbHelper;

public class AuditPointFinalActivity extends AppCompatActivity
{

    private EditText editText_details_1, editText_details_2, editText_summary;
    private DbHelper dbHelper;
    private String header_last_row_index;
    private String all_audit_point_last_row_index;
    private EditText etxtNoOfStacks, etxtReasonNotStack, etxtNoOfSamples, etxtReasonNotSamples;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audit_point_final_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getRef();
    }

    private void getRef()
    {
        editText_details_1 = (EditText) findViewById(R.id.edit_text_detail_1_audit_point_details_final);
        editText_details_2 = (EditText) findViewById(R.id.edit_text_detail_2_audit_point_details_final);
        editText_summary = (EditText) findViewById(R.id.edit_text_audit_summary_audit_point_details_final);

        etxtNoOfStacks = (EditText) findViewById(R.id.etxtNoOfStacks);
        etxtReasonNotStack = (EditText) findViewById(R.id.etxtReasonNotStack);
        etxtNoOfSamples = (EditText) findViewById(R.id.etxtNoOfSamples);
        etxtReasonNotSamples = (EditText) findViewById(R.id.etxtReasonNotSamples);

         header_last_row_index=getIntent().getStringExtra("header_last_row_index");
         all_audit_point_last_row_index=getIntent().getStringExtra("all_audit_point_last_row_inserted_index");

        dbHelper=AppController.getDatabaseInstance();
    }


    public void submitFinalAudit(View view)
    {
        //if (editText_details_1.getText().toString().trim().length() > 0 && editText_details_2.getText().toString().trim().length() > 0 && editText_summary.getText().toString().trim().length() > 0)
        //if (true)
        if((etxtNoOfStacks.getText().toString().length() > 0) &&
                (etxtNoOfStacks.getText().toString().equals("0") ? etxtReasonNotStack.getText().toString().length() > 0 : true) &&
                (etxtNoOfSamples.getText().toString().length() > 0) &&
                (etxtNoOfSamples.getText().toString().equals("0") ? etxtReasonNotSamples.getText().toString().length() > 0 : true) &&
                (editText_details_1.getText().toString().length() > 0))
        {
            String financial = editText_details_1.getText().toString();
            String quality = editText_details_2.getText().toString();
            String over_ruled = editText_summary.getText().toString();
            String NoOfStacks = etxtNoOfStacks.getText().toString();
            String ReasonNotStack = etxtReasonNotStack.getText().toString();
            String NoOfSamples = etxtNoOfSamples.getText().toString();
            String ReasonNotSamples = etxtReasonNotSamples.getText().toString();

            dbHelper.inserLocalGodownAuditFinal(financial, quality, over_ruled, NoOfStacks, ReasonNotStack, NoOfSamples, ReasonNotSamples);
            new CommonServerSenderHelper(AuditPointFinalActivity.this).auditPointPostTest();
            AuditPointFinalActivity.this.finish();
            Intent intent1 = new Intent(AuditPointFinalActivity.this, HomeActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);
            Toast.makeText(AuditPointFinalActivity.this, "Successfully Updated.", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!(etxtNoOfStacks.getText().toString().length() > 0))
            {
                Toast.makeText(AuditPointFinalActivity.this, "Please Enter No. Of Stack", Toast.LENGTH_LONG).show();
            }
            else if(!(etxtNoOfStacks.getText().toString().equals("0") ? etxtReasonNotStack.getText().toString().length() > 0 : true))
            {
                Toast.makeText(AuditPointFinalActivity.this, "Please Enter Reason For Not Conducting Deep Digging", Toast.LENGTH_LONG).show();
            }
            else if(!(etxtNoOfSamples.getText().toString().length() > 0))
            {
                Toast.makeText(AuditPointFinalActivity.this, "Please Enter No. Of Samples", Toast.LENGTH_LONG).show();
            }
            else if(!(etxtNoOfSamples.getText().toString().equals("0") ? etxtReasonNotSamples.getText().toString().length() > 0 : true))
            {
                Toast.makeText(AuditPointFinalActivity.this, "Please Enter Reason For Not Sending Samples", Toast.LENGTH_LONG).show();
            }
            else if(!(editText_details_1.getText().toString().length() > 0))
            {
                Toast.makeText(AuditPointFinalActivity.this, "Please Enter Audit Summary", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(AuditPointFinalActivity.this, "Please Enter Details", Toast.LENGTH_LONG).show();
            }

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
                        dbHelper.deleteLocalGodownAuditHeaderLastInsertedRow(Long.parseLong(header_last_row_index));
                        dbHelper.deleteLocalGodownAuditAllAuditPointsLastInsertedRow(Long.parseLong(all_audit_point_last_row_index));
                        AuditPointFinalActivity.this.finish();
                        Intent intent1 = new Intent(AuditPointFinalActivity.this, HomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }



}
