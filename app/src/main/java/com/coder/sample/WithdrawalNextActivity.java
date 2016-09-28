package com.coder.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.database.DbHelper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Admin on 5/9/2016.
 */
public class WithdrawalNextActivity extends AppCompatActivity  {
    private String string_CMP_intent = "", string_no_of_bags="", String_qty="";
    private Long row_index_intent ,lTotalBags,lTotalQty;
    private Spinner spGodownNoWith;
    private EditText etxtstackNo, etxtWithLotNo, etxtWithNoOfBags, etxtWithQuantityInMT, etxtWithRRnWHRno;
    private TextView spinTextWith;
    private Button button_add_With, button_submit_Add_With;
    private ArrayAdapter<String> adapter_godownNo;
    private DbHelper dbHelper;
    private TableLayout tl;
    private TableRow tr;
    private TextView tGodNo, StackNo, LotNo, NoOfbags, QuantityInMT, RRn_WHR_No;
    private ArrayList<String> sGodownNo;
    private String result;
    private volatile boolean paused;
    private final Object signal = new Object();

      //  String value =  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_next);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Intent SM_CMP = getIntent();
        Bundle extras = getIntent().getExtras();
        string_CMP_intent = extras.getString("CMPName");
        row_index_intent = Long.parseLong(getIntent().getStringExtra("insert_row_index"));
        sGodownNo = extras.getStringArrayList("CMPName");

        string_no_of_bags=getIntent().getStringExtra("no_of_bags");
        String_qty=getIntent().getStringExtra("no_of_qty");

        //Toast.makeText(WithdrawalNextActivity.this, "CMP Name : " +  string_CMP_intent + " ---- Row Index -- "  + row_index_intent, Toast.LENGTH_LONG).show();
        spGodownNoWith = (Spinner) findViewById(R.id.spGodownNoWith);
        etxtstackNo = (EditText) findViewById(R.id.etxtstackNo);
        etxtWithLotNo = (EditText) findViewById(R.id.etxtWithLotNo);
        etxtWithNoOfBags = (EditText) findViewById(R.id.etxtWithNoOfBags);
        etxtWithQuantityInMT = (EditText) findViewById(R.id.etxtWithQuantityInMT);
        etxtWithRRnWHRno = (EditText) findViewById(R.id.etxtWithRRnWHRno);
       
        button_add_With = (Button) findViewById(R.id.button_add_With);
        button_submit_Add_With = (Button) findViewById(R.id.button_submit_Add_With);

        spinTextWith = (TextView) findViewById(R.id.spinTextWith);

        adapter_godownNo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sGodownNo);
        adapter_godownNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGodownNoWith.setAdapter(adapter_godownNo);

        dbHelper=AppController.getDatabaseInstance();

        tl = (TableLayout) findViewById(R.id.maintable);
        addHeaders();
        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public void depositSubmit(View view)
    {
        depositDetalisInsert();
    }

    public void depositDetalisInsert() {

        if (etxtstackNo.getText().toString().trim().length() <= 0) {
            Toast.makeText(WithdrawalNextActivity.this, "Please enter Stack Number ", Toast.LENGTH_LONG).show();
        } else if (etxtWithLotNo.getText().toString().trim().length() <= 0) {
            Toast.makeText(WithdrawalNextActivity.this, "Please enter Lot Number ", Toast.LENGTH_LONG).show();
        } else if (etxtWithNoOfBags.getText().toString().trim().length() <= 0) {
            Toast.makeText(WithdrawalNextActivity.this, "Please enter No Of Bags ", Toast.LENGTH_LONG).show();
        }
        else if (etxtWithQuantityInMT.getText().toString().trim().length() <= 0) {
            Toast.makeText(WithdrawalNextActivity.this, "Please enter Quantity In MT ", Toast.LENGTH_LONG).show();
        }
        else if (etxtWithRRnWHRno.getText().toString().trim().length() <= 0) {
            Toast.makeText(WithdrawalNextActivity.this, "Please enter RRn/WHR No.", Toast.LENGTH_LONG).show();
        }
        else
        {

            String sGodownName =GetGodownData();
            String sDeposit_Header_Id = row_index_intent.toString();
            String sStackNo = etxtstackNo.getText().toString().trim();
            String sLotNo = etxtWithLotNo.getText().toString().trim();
            String sNoOfBags = etxtWithNoOfBags.getText().toString().trim();
            String sQty = etxtWithQuantityInMT.getText().toString().trim();       
            String Srrn_whr_no = etxtWithRRnWHRno.getText().toString().trim();
            String sUserId = "3005";
            String sCreatedDate = getDateTime();

            String  sErrorMsg = dbHelper.insertWithdrawal_Details(sDeposit_Header_Id, sGodownName, sStackNo, sLotNo, sNoOfBags, sQty, sUserId, sCreatedDate, string_no_of_bags,String_qty, Srrn_whr_no);
            if (sErrorMsg.equals("Duplicate Record"))
            {
                Toast.makeText(getApplicationContext()," Already (1) Godown : " + sGodownName  + " -- (2) Stack No. : " +  sStackNo + " -- (3) sLotNo : "  + sLotNo + " Exist in Table Below " , Toast.LENGTH_SHORT).show();
            }
            else if(sErrorMsg.equals("Qty"))
            {
                Toast.makeText(getApplicationContext()," Entered Qty is greater than  : " + sQty , Toast.LENGTH_SHORT).show();
            }
            else if(sErrorMsg.equals("Bags"))
            {
               /*Snackbar.with(getApplicationContext())
                       .text("Single-line snackbar")
                       .show(this);*/


                Toast.makeText(getApplicationContext()," Entered Number of Bags are greater than  : " + sNoOfBags , Toast.LENGTH_SHORT).show();
            }
            else
            {
                addData();
                ClearControls();
                // Toast.makeText(getApplicationContext()," Added Successfully in Table and Database  : ", Toast.LENGTH_SHORT).show();
            }

        }
        button_submit_Add_With.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String sRow_Index =  Long.toString(row_index_intent);
                dbHelper.Update_Deposit_Table_Details(sRow_Index);

                SnackbarManager.show(
                        Snackbar.with(getApplicationContext())
                                .text(" Added Successfully in Database  : ")
                                .textColor(Color.BLACK)
                                .color(Color.YELLOW));

                new CommonServerSenderHelper(WithdrawalNextActivity.this).DepositPostTest();
                WithdrawalNextActivity.this.finish();
                Toast.makeText(WithdrawalNextActivity.this, "added successfully.", Toast.LENGTH_LONG).show();
            }

        });

    }

    public void addHeaders() {
        /*tr = new TableRow(this);


        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));


        TextView tGodNo = new TextView(this);
        tGodNo.setText("Godow No.");
        tGodNo.setTextColor(Color.GRAY);
        //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tGodNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tGodNo.setPadding(5, 5, 5, 0);
        tr.addView(tGodNo);  // Adding textView to tablerow.

        TextView StackNo = new TextView(this);
        StackNo.setText("Stack No.");
        StackNo.setTextColor(Color.GRAY);
        //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        StackNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        StackNo.setPadding(5, 5, 5, 0);
        tr.addView(StackNo);  // Adding textView to tablerow.

        TextView LotNo = new TextView(this);
        LotNo.setText("Lot No.");
        LotNo.setTextColor(Color.GRAY);
        //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LotNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        LotNo.setPadding(5, 5, 5, 0);
        tr.addView(LotNo);  // Adding textView to tablerow.

        TextView NoOfbags = new TextView(this);
        NoOfbags.setText("NoOfBags No.");
        NoOfbags.setTextColor(Color.GRAY);
        //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        NoOfbags.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        NoOfbags.setPadding(5, 5, 5, 0);
        tr.addView(NoOfbags);  // Adding textView to tablerow.

        TextView QuantityInMT = new TextView(this);
        QuantityInMT.setText("QuantityInMf No.");
        QuantityInMT.setTextColor(Color.GRAY);
        //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        QuantityInMT.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        QuantityInMT.setPadding(5, 5, 5, 0);
        tr.addView(QuantityInMT);  // Adding textView to tablerow.

        TextView ClientId = new TextView(this);
        ClientId.setText("ClientId No.");
        ClientId.setTextColor(Color.GRAY);
        //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        ClientId.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ClientId.setPadding(5, 5, 5, 0);
        tr.addView(ClientId);  // Adding textView to tablerow.

        TextView ClientName = new TextView(this);
        ClientName.setText("ClientName No.");
        ClientName.setTextColor(Color.GRAY);
        //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        ClientName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ClientName.setPadding(5, 5, 5, 0);
        tr.addView(ClientName);  // Adding textView to tablerow.

        TextView DpId = new TextView(this);
        DpId.setText("DpId No.");
        DpId.setTextColor(Color.GRAY);
        //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        DpId.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        DpId.setPadding(5, 5, 5, 0);
        tr.addView(DpId);  // Adding textView to tablerow.

           /*//* TextView DpName = new TextView(this);
            DpName.setText("DpName No.");
            DpName.setTextColor(Color.GRAY);
            //companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            DpName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            DpName.setPadding(5, 5, 5, 0);
            tr.addView(DpName);  // Adding textView to tablerow.*//**//*


        tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
*/

        //For Deivider----------------

        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        TextView GoDowndivider = new TextView(this);
        GoDowndivider.setText("-----------------");
        GoDowndivider.setTextColor(Color.GREEN);
        GoDowndivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        GoDowndivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(GoDowndivider); // Adding textView to tablerow.


        TextView StackNodivider = new TextView(this);
        StackNodivider.setText("-----------------");
        StackNodivider.setTextColor(Color.GREEN);
        StackNodivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        StackNodivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(StackNodivider); // Adding textView to tablerow.

        TextView LotNodivider = new TextView(this);
        LotNodivider.setText("-----------------");
        LotNodivider.setTextColor(Color.GREEN);
        LotNodivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        LotNodivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(LotNodivider); // Adding textView to tablerow.

        TextView NoOfBagsdivider = new TextView(this);
        NoOfBagsdivider.setText("-----------------");
        NoOfBagsdivider.setTextColor(Color.GREEN);
        NoOfBagsdivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        NoOfBagsdivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(NoOfBagsdivider); // Adding textView to tablerow.

        TextView QuantityInMTdivider = new TextView(this);
        QuantityInMTdivider.setText("-----------------");
        QuantityInMTdivider.setTextColor(Color.GREEN);
        QuantityInMTdivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        QuantityInMTdivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(QuantityInMTdivider); // Adding textView to tablerow.


        TextView ClientIddivider = new TextView(this);
        ClientIddivider.setText("-----------------");
        ClientIddivider.setTextColor(Color.GREEN);
        ClientIddivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ClientIddivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(ClientIddivider); // Adding textView to tablerow.


        TextView CLientNamedivider = new TextView(this);
        CLientNamedivider.setText("-----------------");
        CLientNamedivider.setTextColor(Color.GREEN);
        CLientNamedivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        CLientNamedivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(CLientNamedivider); // Adding textView to tablerow.


        TextView DpIddivider = new TextView(this);
        DpIddivider.setText("-----------------");
        DpIddivider.setTextColor(Color.GREEN);
        DpIddivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        DpIddivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(DpIddivider); // Adding textView to tablerow.


        TextView DpNamedivider = new TextView(this);
        DpNamedivider.setText("-----------------");
        DpNamedivider.setTextColor(Color.GREEN);
        DpNamedivider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        DpNamedivider.setPadding(5, 0, 0, 0);
        //divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(DpNamedivider); // Adding textView to tablerow.


        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));


    }
    private String GetGodownData()
    {
        spinTextWith = new TextView(this);
        spinTextWith = (TextView) spGodownNoWith.getSelectedView();
        result = spinTextWith.getText().toString();
        return result;
    }


    public String addData()

    {
        tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        result = "";

        spinTextWith = new TextView(this);
        spinTextWith = (TextView) spGodownNoWith.getSelectedView();
        result = spinTextWith.getText().toString();

        tGodNo = new TextView(this);
        tGodNo.setText(result);
        tGodNo.setTextColor(Color.BLACK);
        tGodNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tGodNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tGodNo.setPadding(4, 4, 4, 4);
        tr.addView(tGodNo);  // Adding textView to tablerow.


        StackNo = new TextView(this);
        StackNo.setText(etxtstackNo.getText().toString());
        StackNo.setTextColor(Color.BLACK);
        StackNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        StackNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        StackNo.setPadding(2, 2, 2, 2);
        tr.addView(StackNo);  // Adding textView to tablerow.


        LotNo = new TextView(this);
        LotNo.setText(etxtWithLotNo.getText().toString());
        LotNo.setTextColor(Color.BLACK);
        LotNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LotNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        LotNo.setPadding(2, 2, 2, 2);
        tr.addView(LotNo);  // Adding textView to tablerow.


        NoOfbags = new TextView(this);
        NoOfbags.setText(etxtWithNoOfBags.getText().toString());
        NoOfbags.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        NoOfbags.setTextColor(Color.BLACK);
        NoOfbags.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        NoOfbags.setPadding(2, 2, 2, 2);
        tr.addView(NoOfbags);  // Adding textView to tablerow.


        QuantityInMT = new TextView(this);
        QuantityInMT.setText(etxtWithQuantityInMT.getText().toString());
        QuantityInMT.setTextColor(Color.BLACK);
        QuantityInMT.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        QuantityInMT.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        QuantityInMT.setPadding(2, 2, 2, 2);
        tr.addView(QuantityInMT);  // Adding textView to tablerow.

        RRn_WHR_No = new TextView(this);
        RRn_WHR_No.setText(etxtWithRRnWHRno.getText().toString());
        RRn_WHR_No.setTextColor(Color.BLACK);
        RRn_WHR_No.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        RRn_WHR_No.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        RRn_WHR_No.setPadding(2, 2, 2, 2);
        tr.addView(RRn_WHR_No);  // Adding textView to tablerow.
        
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       /* client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DepositeNext Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.coder.sample/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       /* Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DepositeNext Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.coder.sample/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }




  /*  @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_add_With:
                addData();
                break;
            case R.id.button_submit_Add:
                Read_Table_Row(v);
                break;

        }

    }*/

   /* private void Read_Table_Row(View v)
    {


        TableRow t = (TableRow) v;
        TextView firstTextView = (TextView) t.getChildAt(0);
        TextView secondTextView = (TextView) t.getChildAt(1);
        TextView thirdTextView = (TextView) t.getChildAt(2);
        TextView fourthTextView = (TextView) t.getChildAt(3);
        TextView fifthTextView = (TextView) t.getChildAt(4);
        TextView sixthTextView = (TextView) t.getChildAt(5);
        TextView seventhTextView = (TextView) t.getChildAt(6);
        String firstText = firstTextView.getText().toString();
        String secondText = secondTextView.getText().toString();
        String thirdText = thirdTextView.getText().toString();
        String fourthText = fourthTextView.getText().toString();
        String fifthText = fifthTextView.getText().toString();
        String sixthText = sixthTextView.getText().toString();
        String seventhText = seventhTextView.getText().toString();
        Toast.makeText(getApplicationContext(), "First Row : " + firstText + " - " +  secondText + " - " + thirdText + " - " + fourthText + " - " + fifthText + " - " + sixthText + " - " + seventhText, Toast.LENGTH_LONG).show();
    }
*/

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }


    private void ClearControls() {

        etxtstackNo.setText("");
        etxtWithLotNo.setText("");
        etxtWithNoOfBags.setText("");
        etxtWithQuantityInMT.setText("");
        etxtWithRRnWHRno.setText("");
       

    }
}