package com.coder.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Set;


/**
 * Created by Admin on 5/9/2016.
 */
public class DepositeNextActivity extends AppCompatActivity  {
    private String string_CMP_intent = "", string_no_of_bags="", String_qty="";
    private Long row_index_intent ,lTotalBags,lTotalQty;
    private Spinner spinGodown;
    private EditText etxtstackNo, etxtDepLotNo, etxtDepNoOfBags, etxtDepQuantityInMT, etxtDepClientID, etxtDepClientname, etxtDepDPID, txtDepDPName;
    private TextView spinText;
    private Button button_add, button_submit_Add;
    private ArrayAdapter<String> adapter_godownNo;
    private DbHelper dbHelper;
    private TableLayout tl;
    private TableRow tr;
    private TextView tGodNo, StackNo, LotNo, NoOfbags, QuantityInMT, ClientId, ClientName, DpId, DpName;
    private ArrayList<String> sGodownNo;
    private String result;
    private String SplitedValue,DelGodNo,DelstackNo,DelLotNo,DelNoofBags,DelQuantityInMT,DelClientId,DelClientName, DelDpId,DelDpName;
    CheckBox cb;
    private volatile boolean paused;
    private final Object signal = new Object();
    private int x=0;
    private int valueDel;
    private String AllTextvalues;

  /*  long lBags=Long.valueOf(string_no_of_bags);
    double lQty=Double.valueOf(String_qty);*/

  //  private GoogleApiClient client;


    //   public void onClick(View v) {


    // }


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





       /* lTotalBags=Long.parseLong(getIntent().getStringExtra("no_of_bags"));
        lTotalQty=Long.parseLong(getIntent().getStringExtra("no_of_qty"));
*/

       /* StringBuilder builder1 = new StringBuilder();
        builder1.append(getIntent().getStringExtra("no_of_bags"));
        lTotalBags = Long.parseLong(builder1.toString());

        StringBuilder builder2 = new StringBuilder();
        builder2.append(getIntent().getStringExtra("no_of_qty"));
        lTotalQty = Long.parseLong(builder2.toString());*/


        //Toast.makeText(DepositeNextActivity.this, "CMP Name : " +  string_CMP_intent + " ---- Row Index -- "  + row_index_intent, Toast.LENGTH_LONG).show();
        spinGodown = (Spinner) findViewById(R.id.spGodownNo);
        etxtstackNo = (EditText) findViewById(R.id.etxtstackNo);
        etxtDepLotNo = (EditText) findViewById(R.id.etxtDepLotNo);
        etxtDepNoOfBags = (EditText) findViewById(R.id.etxtDepNoOfBags);
        etxtDepQuantityInMT = (EditText) findViewById(R.id.etxtDepQuantityInMT);
        etxtDepClientID = (EditText) findViewById(R.id.etxtDepClientID);
        etxtDepClientname = (EditText) findViewById(R.id.etxtDepClientname);
        etxtDepDPID = (EditText) findViewById(R.id.etxtDepDPID);
        button_add = (Button) findViewById(R.id.button_add);
        button_submit_Add = (Button) findViewById(R.id.button_submit_Add);
        txtDepDPName = (EditText) findViewById(R.id.txtDepDPName);
        spinText = (TextView) findViewById(R.id.spinText1);

        adapter_godownNo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sGodownNo);
        adapter_godownNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGodown.setAdapter(adapter_godownNo);

        dbHelper=AppController.getDatabaseInstance();

        tl = (TableLayout) findViewById(R.id.maintable);
        addHeaders();
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public void depositSubmit(View view)  // For clicking on Add Button
    {

        String sRow_Index =String.valueOf( row_index_intent);
        //long lReturn = dbHelper.ChkValidation_Submit(sRow_Index, string_no_of_bags, String_qty);
        if(etxtDepQuantityInMT.getText().toString().trim().length()==0)
        {
            Toast.makeText(getApplicationContext(),"No of bags cannot be Zero",Toast.LENGTH_SHORT).show();
        }else if (etxtDepClientID.getText().toString().trim().length()==0 || etxtDepClientID.getText().toString().trim().length()==0.000)
        {
            Toast.makeText(getApplicationContext(),"Quantity in MT cannot be Zero",Toast.LENGTH_SHORT).show();
        }else
        {
            depositDetalisInsert();
        }
    }

    public void depositDetalisInsert()
    {

        if (
                etxtstackNo.getText().toString().trim().length() <= 0)
        {

            Toast.makeText(DepositeNextActivity.this, "Please enter Stack Number ", Toast.LENGTH_LONG).show();
        } else if (etxtDepLotNo.getText().toString().trim().length() <= 0) {

            Toast.makeText(DepositeNextActivity.this, "Please enter Lot Number ", Toast.LENGTH_LONG).show();
        } else if (etxtDepNoOfBags.getText().toString().trim().length() <= 0) {

            Toast.makeText(DepositeNextActivity.this, "Please enter No Of Bags ", Toast.LENGTH_LONG).show();
        } else if (etxtDepQuantityInMT.getText().toString().trim().length() <= 0) {


           Toast.makeText(DepositeNextActivity.this, "Please enter Quantity In MT ", Toast.LENGTH_LONG).show();
        } else if (etxtDepClientID.getText().toString().trim().length() <= 0) {

               Toast.makeText(DepositeNextActivity.this, "Please enter Client Id ", Toast.LENGTH_LONG).show();
        } else if (etxtDepClientname.getText().toString().trim().length() <= 0) {

            Toast.makeText(DepositeNextActivity.this, "Please enter Client Name ", Toast.LENGTH_LONG).show();
        } else if (etxtDepDPID.getText().toString().trim().length() <= 0) {

           Toast.makeText(DepositeNextActivity.this, "Please enter DP Id ", Toast.LENGTH_LONG).show();
        } else if (txtDepDPName.getText().toString().trim().length() <= 0) {

            Toast.makeText(DepositeNextActivity.this, "Please enter DP Names ", Toast.LENGTH_LONG).show();
        }
        else
        {
            String sGodownName =GetGodownData();
            String sDeposit_Header_Id = row_index_intent.toString();
            String sStackNo = etxtstackNo.getText().toString().trim();
            String sLotNo = etxtDepLotNo.getText().toString().trim();
            String sNoOfBags = etxtDepNoOfBags.getText().toString().trim();
            String sQty = etxtDepQuantityInMT.getText().toString().trim();
            String sClientId = etxtDepClientID.getText().toString().trim();
            String sClientName = etxtDepClientname.getText().toString().trim();
            String sDepository_id = etxtDepDPID.getText().toString().trim();
            String sDepository_Name = txtDepDPName.getText().toString().trim();
            String sUserId = "3005";
            String sCreatedDate = getDateTime();

            String  sErrorMsg = dbHelper.insertDeposit_Details(sDeposit_Header_Id, sGodownName, sStackNo, sLotNo, sNoOfBags, sQty, sClientId, sClientName, sDepository_id, sDepository_Name, sUserId, sCreatedDate, string_no_of_bags,String_qty);


           if (sErrorMsg.equals("Duplicate Record"))
            {
               Toast.makeText(getApplicationContext()," Already (1) Godown : " + sGodownName  + " -- (2) Stack No. : " +  sStackNo + " -- (3) sLotNo : "  + sLotNo + " Exist in Table Below " , Toast.LENGTH_SHORT).show();
            }
           else if(sErrorMsg.equals("Qty"))
           {
              Toast.makeText(getApplicationContext(), " Entered Qty is greater than  : " + sQty, Toast.LENGTH_SHORT).show();
           }
           else if(sErrorMsg.equals("Bags"))
           {
               Toast.makeText(getApplicationContext(), " Entered Number of Bags are greater than  : " + sNoOfBags, Toast.LENGTH_SHORT).show();
           }
           else
           {
               addData();
               x++;
               ClearControls();
           }
        }

        //Submit button click event
        button_submit_Add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String sRow_Index = Long.toString(row_index_intent);
                long lBags=Long.valueOf(string_no_of_bags);
                double lQty=Double.valueOf(String_qty);
                long lReturn_Bags = dbHelper.ChkValidation_Submit_Bags(sRow_Index, string_no_of_bags);
                double lReturn_Qty = dbHelper.ChkValidation_Submit_Qty(sRow_Index,  String_qty);

                if (lReturn_Bags<lBags)
                {
                    Toast.makeText(DepositeNextActivity.this, lReturn_Bags + ":- No. of Bags. are less than Total Bags :-" + lBags , Toast.LENGTH_LONG).show();
                }
                if (lReturn_Qty<lQty)
                {
                    Toast.makeText(DepositeNextActivity.this, lReturn_Qty + ":- No. of Qty. are less than Total Qty :-" + lQty , Toast.LENGTH_LONG).show();
                }
                if (lReturn_Bags==lBags && lReturn_Qty==lQty)
                {
                    dbHelper.Update_Deposit_Table_Details(sRow_Index);
                    new CommonServerSenderHelper(DepositeNextActivity.this).DepositPostTest();
                    DepositeNextActivity.this.finish();
                    Toast.makeText(DepositeNextActivity.this, "added successfully.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void addHeaders()
    {
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
        spinText = new TextView(this);
        spinText = (TextView) spinGodown.getSelectedView();
        result = spinText.getText().toString();
        return result;
    }



        //<<<<DDont touch>>>>>>
       //final CheckBox cb = new CheckBox(this);
        //tl.addView(cb);
        //tr.addView(cb);


        /*if (cb.isChecked())
        {
            tl.removeView(tr);

        }*/




      /*  Button delete = new Button(this);
        delete.setText("Delete");
        delete.setWidth(50);
        delete.setHeight(50);

       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               View row  = (View) v.getParent();
               ViewGroup container = ((ViewGroup)row.getParent());
               container.removeView(row);
               container.invalidate();
               Toast.makeText(getApplicationContext(), (CharSequence) container,Toast.LENGTH_SHORT).show();

           }
       });
*/
      public String addData()

      {
          final LayoutParams lp = new LayoutParams();
          lp.gravity = Gravity.CENTER;



           tr = new TableRow(this);
           tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
           result = "";
           int sCount=0;
           tr.setId(1000 + sCount);

           final Button Del = new Button(this);

          Del.setText("D");
          Del.setBackgroundColor(getResources().getColor(R.color.textColorforget));
          /*Del.setWidth(0);
          Del.setHeight(0)*/;
          Del.setPadding(0,0,0,0);
          Del.setId(x);





       /* tl.addView(delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tl.removeView(tr);


            }
        });
        delete.setText("Delete");
        delete.setWidth(50);
        delete.setHeight(50);*/






        spinText = new TextView(getApplicationContext());
        spinText = (TextView) spinGodown.getSelectedView();
        result = spinText.getText().toString();
        tGodNo = new TextView(getApplicationContext());
        tGodNo.setText(result+"~"+etxtstackNo.getText().toString()+"~"+etxtDepLotNo.getText().toString());
        tGodNo.setTextColor(Color.BLACK);
        tGodNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tGodNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tGodNo.setPadding(0, 0, 0, 0);
        tr.addView(Del);
        tr.addView(tGodNo);

       /* tl.addView(tGodNo);*/
      //  tr.addView(delete);
        // tr.addView(cb);




      /*  for(i < )
       do {
           tr.addView(delete);
       }while (tr.getChildAt(i))*/
        //tGodNo.setText(result);
        // Adding textView to tablerow.



       /* StackNo = new TextView(this);
        StackNo.setText(etxtstackNo.getText().toString());
        StackNo.setTextColor(Color.BLACK);
        StackNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        StackNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        StackNo.setPadding(2, 2, 2, 2);
        tr.addView(StackNo);  // Adding textView to tablerow.


        LotNo = new TextView(this);
        LotNo.setText(etxtDepLotNo.getText().toString());
        LotNo.setTextColor(Color.BLACK);
        LotNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LotNo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        LotNo.setPadding(2, 2, 2, 2);
        tr.addView(LotNo);  // Adding textView to tablerow.
        */



        NoOfbags = new TextView(getApplicationContext());
        NoOfbags.setText(etxtDepNoOfBags.getText().toString());
        NoOfbags.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        NoOfbags.setTextColor(Color.BLACK);
        NoOfbags.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        NoOfbags.setPadding(0, 0, 0, 0);
        tr.addView(NoOfbags);

        // Adding textView to tablerow.


        QuantityInMT = new TextView(getApplicationContext());
        QuantityInMT.setText(etxtDepQuantityInMT.getText().toString());
        QuantityInMT.setTextColor(Color.BLACK);
        QuantityInMT.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        QuantityInMT.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        QuantityInMT.setPadding(0, 0, 0, 0);
        tr.addView(QuantityInMT);  // Adding textView to tablerow.


        ClientId = new TextView(getApplicationContext());
        ClientId.setText(etxtDepClientID.getText().toString());
        ClientId.setTextColor(Color.BLACK);
        ClientId.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        ClientId.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ClientId.setPadding(0, 0, 0, 0);
        tr.addView(ClientId);  // Adding textView to tablerow.


        ClientName = new TextView(getApplicationContext());
        ClientName.setText(etxtDepClientname.getText().toString());
        ClientName.setTextColor(Color.BLACK);
        ClientName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        ClientName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        ClientName.setPadding(0, 0, 0, 0);
        tr.addView(ClientName);  // Adding textView to tablerow.


        DpId = new TextView(getApplicationContext());
        DpId.setText(etxtDepDPID.getText().toString());
        DpId.setTextColor(Color.BLACK);
        DpId.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        DpId.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        DpId.setPadding(0, 0, 0, 0);
        tr.addView(DpId);  // Adding textView to tablerow.

        DpName = new TextView(getApplicationContext());
        DpName.setText(txtDepDPName.getText().toString());
        DpName.setTextColor(Color.BLACK);
        DpName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        DpName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        DpName.setPadding(0, 0, 0, 0);
        tr.addView(DpName);

           /*godid=spinText.getText().toString();
          * etxtstackNo.getText().toString()
          * etxtDepLotNo.getText().toString()
          * etxtDepNoOfBags.getText().toString()
          * etxtDepQuantityInMT.getText().toString()
          * etxtDepClientID.getText().toString()
          * etxtDepClientname.getText().toString()
          * etxtDepDPID.getText().toString()
          * txtDepDPName.getText().toString()
          *
          * */

          final TextView allvalue = new TextView(getApplicationContext());
          allvalue.setVisibility(View.INVISIBLE);
          allvalue.setText(spinText.getText().toString()+"~"+
                           etxtstackNo.getText().toString()+"~"+
                           etxtDepLotNo.getText().toString()+"~"+
                           etxtDepNoOfBags.getText().toString()+"~"+
                           etxtDepQuantityInMT.getText().toString()+"~"+
                           etxtDepClientID.getText().toString()+"~"+
                           etxtDepClientname.getText().toString()+"~"+
                           etxtDepDPID.getText().toString()+"~"+
                           txtDepDPName.getText().toString());
          AllTextvalues = allvalue.getText().toString();
          Log.e("All values in Text is",AllTextvalues);

          Del.setOnClickListener(new View.OnClickListener()
          {
              @Override
              public void onClick(View v)
              {
                  valueDel = Del.getId();
                  Log.e("Delvalue",""+valueDel);

                  View Row = (View) v.getParent();
                  ViewGroup container = ((ViewGroup) Row.getParent());

                  if(Del.getId()==valueDel)
                  {
                      SplitedValue = allvalue.getText().toString();
                      String[] items1 = SplitedValue.split("~");
                      String sSpinnerVal = items1[0];
                      String sStackNo = items1[1];
                      String sLotNo = items1[2];
                      String sNoOfBags = items1[3];
                      String sQty = items1[4];
                      String sClientId = items1[5];
                      String sClientName = items1[6];
                      String sDepId = items1[7];
                      String sDepName = items1[8];

                      DelGodNo = String.valueOf(sSpinnerVal);
                      DelstackNo = String.valueOf(sStackNo);
                      DelLotNo = String.valueOf(sLotNo);
                      DelNoofBags = String.valueOf(sNoOfBags);;
                      DelQuantityInMT = String.valueOf(sQty);
                      DelClientId = String.valueOf(sClientId);
                      DelClientName = String.valueOf(sClientName);
                      DelDpId = String.valueOf(sDepId);
                      DelDpName = String.valueOf(sDepName);
                      String sDeposit_Header_Id = row_index_intent.toString();

                      Log.e("New Get godno", DelGodNo);
                      Log.e("New Get Stack", DelstackNo);
                      Log.e("New Get lotNo", DelLotNo);
                      Log.e("New Get DelNoofBags", DelNoofBags);
                      Log.e("New Get DelQuantityInMT", DelQuantityInMT);
                      Log.e("New Get DelClientId", DelClientId);
                      Log.e("New Get DelClientName", DelClientName);
                      Log.e("New Get DelDpId", DelDpId);
                      Log.e("New Get DelDpName", DelDpName);

                      String sErrorMsgDel = dbHelper.insertDeposit_Del_Details(DelGodNo, DelstackNo, DelLotNo, DelNoofBags, DelQuantityInMT,DelClientId ,DelClientName, DelDpId , DelDpName,sDeposit_Header_Id);

                      if (sErrorMsgDel == "Success")
                      {
                          container.removeView(Row);
                          container.invalidate();
                          Toast.makeText(getApplication(), "Data Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                      }
/*}*/
                  }
              }
          });
          sCount++;

        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT)    );
          return AllTextvalues;
      }

    @Override
    public void onStart()
    {
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
    public void onStop()
    {
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
            case R.id.button_add:
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

    private String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }


    private void ClearControls()
    {

        etxtstackNo.setText("");
        etxtDepLotNo.setText("");
        etxtDepNoOfBags.setText("");
        etxtDepQuantityInMT.setText("");
        etxtDepClientID.setText("");
        etxtDepClientname.setText("");
        etxtDepDPID.setText("");
        txtDepDPName.setText("");

    }

    public void onBackPressed()
    {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit DepositeNext ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Intent intent = new Intent(DepositeNextActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        DepositeNextActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}