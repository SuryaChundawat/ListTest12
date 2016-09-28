package com.coder.sample;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.com.pojo.ExchangeTypePojo;
import com.database.DbHelper;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WithdrawalActivity extends AppCompatActivity
{
    private static final int CAMERA_REQUEST =0;
    EditText etxtWithClientCopy, etxtWithTruckNumber, etxtWithPackingkg , etxtWithNoOfBags ,
                etxtWithWgtPkgMtl ,etxtWithWgtOfEmtGunnies , etxtWithTtlWgt ,etxtWithTareWgt, etxtWithSupplierName , etxtWithNCMLGatePassNo, etxtWithLRGRNo,
            etxtWithWgtBdgName, etxtWithWgtBdgSlNo, etxtWithDistOfWgtBdgfmGod, etxtWithWgtBdgTmIn, etxtWithWgtBdgTmOut, etxtDepNoOfCutTomBags,
            etxtWithNoOfPallaBags, etxtWithDepMoisture, etxtWithForeignMatter, etxtWithFungusDmgDiscol, etxtWithBrkImtrOtherSeed, etxtWithWeevilled,
            etxtWithLiveInssects, etxtWithRemarks ;
    CheckBox checkbox_meat_SCM,checkbox_meat_Rej;
    Spinner spWithState, spWithLocation, spWithCMPName, spWithExchangeType, spDepClientName, spWithCommodityName,spWithPackingMat;
    TextView textViewclient_copy_no,textViewis_scm_deposite,  textViewTransaction_date,
            textViewtruck_number,  textViewpacking, textViewpacking_material, textViewwt_of_packing_material,
            textViewsupplier_name, textViewncml_gate_pass_no, textViewlorry_receipt, textViewno_of_bags, textViewwt_of_empty_gunnies, textViewwt_bridge_name,
            textViewwt_bridge_slip_no, textViewdist_of_wt_bridge_from_godown, textViewwt_bridge_time_id, textViewwt_bridge_time_out,
            textViewtotal_weight, textViewtare_wt_in_mt, textViewgross_wt_in_mt, textViewnet_wt_in_mt, textViewaverage_wt_per_bag,
            textViewno_of_cut_tom_bags, textViewno_of_palla_bags, textViewmoisture, textViewforeign_matter, textViewfungus, textViewbroken, textViewweevilled,
            textViewlive_insscets, textViewtextViewremark, textViewphoto_front, textViewphoto_rear, textViewphoto_depositor, textViewphoto_employee,
            textViewphoto_sample_taken,txtWithDPName ,textView_id,textView_date_and_time,  Distance_weight_C, etxtWithGrsWgt, txtDepNetWgt, txtDepAvgWgt, etxtWithTxnDate;

    private ArrayAdapter<String> adapter_state, adapter_location, adapter_godown, adapter_exchtype, adapter_commodity;
    public int btn_sampleTaken=0, btn_Emp=0, btn_Front=0, btn_Rear=0, btn_Depositor=0;
    private DbHelper dbHelper;
    private GPSTracker gps;
    public Button btnButton;
    public double num_A,num_B,sum_c;
    Button btnWithPtTruckFrontSide,btnWithPtTruckRearSide,btnWithPtDepositor,btnWithPtEmp,btnWithPtSmpTkn,Depbutton_submit_Next;
    ImageView ivDepPtTruckFrontSide,ivDepPtTruckRearSide,ivDepPtDepositor,ivDepPtEmp,ivDepPtSmpTkn;
    private ImageView imageView_pic_front,imageView_pic_rear,imageView_pic_Depositor,imageView_pic_Emp,imageView_pic_SmpTkn;
    private Bitmap photo_front, photo_rear, photo_depositor, photo_emp, photo_sample_taken;
    Double dbDepWgtPkgMtl,dbDepNoOfBags;
    private int TruckFrontSide=1;
    private int TruckRearSide=2;
    private int DepPtDepositor=3;
    private int DepPtEmp=4;
    private int DepPtSmpTkn=5;

    String DepWgtPkgMtl,DepNoOfBags,DepWgtOfEmtGunnies,DepTtlWgt,DepTareWgt,DepClientCopy,WithTxnDate,WithGrsWgt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkbox_meat_SCM=(CheckBox) findViewById(R.id.checkbox_meat_SCM);
        checkbox_meat_Rej=(CheckBox) findViewById(R.id.checkbox_meat_Rejected);
        btnWithPtTruckFrontSide = (Button)findViewById(R.id.btnWithPtTruckFrontSide);
        btnWithPtTruckRearSide = (Button)findViewById(R.id.btnWithPtTruckRearSide);
        btnWithPtDepositor = (Button)findViewById(R.id.btnWithPtDepositor);
        btnWithPtEmp = (Button)findViewById(R.id.btnWithPtEmp);
        btnWithPtSmpTkn =(Button)findViewById(R.id.btnWithPtSmpTkn);

        ivDepPtTruckFrontSide = (ImageView)findViewById(R.id.ivWithPtTruckFrontSide);
        ivDepPtTruckRearSide = (ImageView)findViewById(R.id.ivWithPtTruckRearSide);
        ivDepPtDepositor = (ImageView)findViewById(R.id.ivWithPtDepositor);
        ivDepPtEmp = (ImageView)findViewById(R.id.ivWithPtEmp);
        ivDepPtSmpTkn = (ImageView)findViewById(R.id.ivWithPtSmpTkn);

        etxtWithWgtPkgMtl = (EditText)findViewById(R.id.etxtWithWgtPkgMtl);
        etxtWithNoOfBags = (EditText)findViewById(R.id.etxtWithNoOfBags);
        etxtWithWgtOfEmtGunnies = (EditText)findViewById(R.id.etxtWithWgtOfEmtGunnies);
        etxtWithTtlWgt= (EditText)findViewById(R.id.etxtWithTtlWgt);
        etxtWithTareWgt= (EditText)findViewById(R.id.etxtWithTareWgt);
        etxtWithClientCopy = (EditText) findViewById(R.id.etxtDepClientCopy);
        etxtWithTxnDate = (TextView) findViewById(R.id.etxtWithTxnDate);
        etxtWithGrsWgt= (TextView)findViewById(R.id.txtWithGrsWgt);

        DepWgtOfEmtGunnies = etxtWithWgtOfEmtGunnies.getText().toString();
        DepTtlWgt = etxtWithTtlWgt.getText().toString();
        DepTareWgt = etxtWithTareWgt.getText().toString();
        DepClientCopy = etxtWithClientCopy.getText().toString();
        WithTxnDate = etxtWithTxnDate.getText().toString();
        WithGrsWgt = etxtWithGrsWgt.getText().toString();

        getref();
        depositerSetUp();


        final Spinner spWithPackingMat=(Spinner)findViewById(R.id.spWithPackingMat);
        List<String> catega =new ArrayList<String>();
        catega.add("Jute");
        catega.add("Plastic");

        ArrayAdapter<String> dataAdapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,catega);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spWithPackingMat.setAdapter(dataAdapter);

        Depbutton_submit_Next = (Button) findViewById(R.id.Depbutton_submit_Next);

        etxtWithWgtPkgMtl.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtWithWgtPkgMtl.getText().toString().isEmpty() ? etxtWithWgtPkgMtl.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtWithNoOfBags.getText().toString().isEmpty() ? etxtWithNoOfBags.getText().toString() : "0");
                Double value1 =  (v1 * v2) / (1000*1000) ;  // Completed
                String value = String.format("%.2f", value1);// Math.round(value1,2);
                etxtWithWgtOfEmtGunnies.setText(value);
            }
        });

        etxtWithNoOfBags.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtWithWgtPkgMtl.getText().toString().isEmpty() ? etxtWithWgtPkgMtl.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtWithNoOfBags.getText().toString().isEmpty() ? etxtWithNoOfBags.getText().toString() : "0");
                Double value1 = (v1 * v2) / (1000 * 1000);
                String value = String.format("%.2f", value1);
                etxtWithWgtOfEmtGunnies.setText(value);
            }
        });

        etxtWithTxnDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(WithdrawalActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
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
                        etxtWithTxnDate.setText(sdayOfMonth + "/" + (smonthOfYear) + "/" + (year));
                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(new Date().getTime());
                dpd.show();
            }
        });

        etxtWithTtlWgt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtWithTtlWgt.getText().toString().isEmpty() ? etxtWithTtlWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtWithTareWgt.getText().toString().isEmpty() ? etxtWithTareWgt.getText().toString() : "0");
                Double value = v1 - v2 ;
                etxtWithGrsWgt.setText(value.toString());
            }
        });

        etxtWithTareWgt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtWithTtlWgt.getText().toString().isEmpty() ? etxtWithTtlWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtWithTareWgt.getText().toString().isEmpty() ? etxtWithTareWgt.getText().toString() : "0");
                Double value = v1 - v2 ;
                etxtWithGrsWgt.setText(value.toString());
            }
        });

        etxtWithGrsWgt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtWithGrsWgt.getText().toString().isEmpty() ? etxtWithGrsWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtWithWgtOfEmtGunnies.getText().toString().isEmpty() ? etxtWithWgtOfEmtGunnies.getText().toString() : "0");
                Double value = v1 - v2 ;
                txtDepNetWgt.setText(value.toString());
            }
        });

        etxtWithWgtOfEmtGunnies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtWithGrsWgt.getText().toString().isEmpty() ? etxtWithGrsWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtWithWgtOfEmtGunnies.getText().toString().isEmpty() ? etxtWithWgtOfEmtGunnies.getText().toString() : "0");
                Double value = v1 - v2 ;
                txtDepNetWgt.setText(value.toString());
            }
        });

        txtDepNetWgt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!txtDepNetWgt.getText().toString().isEmpty() ? txtDepNetWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtWithNoOfBags.getText().toString().isEmpty() ? etxtWithNoOfBags.getText().toString() : "0");
                Double value1 = (v1 / v2) * 1000;
                String value = String.format("%.2f", value1);
                txtDepAvgWgt.setText(value);
            }
        });

        Depbutton_submit_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( spWithExchangeType.getCount()>0
                        && spWithState.getCount()>0
                        && spWithLocation.getCount()>0
                        && spWithCMPName.getCount()>0
                        && spWithCommodityName.getCount()>0
                        && (photo_front != null)
                        && (photo_rear != null)
                        && (photo_depositor != null)
                        && (photo_emp != null)
                        && (photo_sample_taken != null)
                        && etxtWithTxnDate.getText().toString().trim().length() > 0
                        && etxtWithTruckNumber.getText().toString().trim().length() > 0
                        && etxtWithPackingkg.getText().toString().trim().length() > 0
                        && etxtWithWgtPkgMtl.getText().toString().trim().length() > 0
                        && etxtWithSupplierName.getText().toString().trim().length() > 0
                        && etxtWithNCMLGatePassNo.getText().toString().trim().length() > 0
                        && etxtWithLRGRNo.getText().toString().trim().length() > 0
                        && etxtWithNoOfBags.getText().toString().trim().length() > 0
                        && etxtWithWgtBdgName.getText().toString().trim().length() > 0
                        && etxtWithWgtBdgSlNo.getText().toString().trim().length() > 0
                        && etxtWithDistOfWgtBdgfmGod.getText().toString().trim().length() > 0
                        && etxtWithWgtBdgTmIn.getText().toString().trim().length() > 0
                        && etxtWithWgtBdgTmOut.getText().toString().trim().length() > 0
                        && etxtWithTtlWgt.getText().toString().trim().length() > 0
                        && etxtWithTareWgt.getText().toString().trim().length() > 0
                        && textViewgross_wt_in_mt.getText().toString().trim().length() > 0
                        && textViewnet_wt_in_mt.getText().toString().trim().length() > 0
                        && textViewaverage_wt_per_bag.getText().toString().trim().length() > 0
                        && etxtDepNoOfCutTomBags.getText().toString().trim().length() > 0
                        && etxtWithNoOfPallaBags.getText().toString().trim().length() > 0
                        && etxtWithDepMoisture.getText().toString().trim().length() > 0
                        && etxtWithForeignMatter.getText().toString().trim().length() > 0
                        && etxtWithFungusDmgDiscol.getText().toString().trim().length() > 0
                        && etxtWithBrkImtrOtherSeed.getText().toString().trim().length() > 0
                        && etxtWithWeevilled.getText().toString().trim().length() > 0
                        && etxtWithLiveInssects.getText().toString().trim().length() > 0
                        )
                {
                    String client_copy_no = etxtWithSupplierName .getText().toString();
                    String ChckValue, ChkValue_Withdraw;
                    if (checkbox_meat_SCM.isChecked()==true)
                    {
                        ChckValue="1";
                    }
                    else
                    {
                        ChckValue="0";
                    }
                    if (checkbox_meat_Rej.isChecked()==true)
                    {
                        ChkValue_Withdraw="1";
                    }
                    else
                    {
                        ChkValue_Withdraw="0";
                    }
                    String is_scm_deposite = ChckValue;
                    String is_rejected_withdraw= ChkValue_Withdraw;
                    String Transaction_date = etxtWithTxnDate.getText().toString();
                    String truck_number = etxtWithTruckNumber.getText().toString();
                    String packing_materialkg  = etxtWithPackingkg.getText().toString();
                    String wt_of_packing_material = etxtWithWgtPkgMtl.getText().toString();
                    String supplier_name = etxtWithSupplierName.getText().toString();
                    String ncml_gate_pass_no = etxtWithNCMLGatePassNo.getText().toString();
                    String lorry_receipt = etxtWithLRGRNo.getText().toString();
                    String no_of_bags = etxtWithNoOfBags.getText().toString();
                    String wt_of_empty_gunnies = etxtWithWgtOfEmtGunnies.getText().toString();
                    String wt_bridge_name = etxtWithWgtBdgName.getText().toString();
                    String wt_bridge_slip_no = etxtWithWgtBdgSlNo.getText().toString();
                    String dist_of_wt_bridge_from_godown = etxtWithDistOfWgtBdgfmGod.getText().toString();
                    String wt_bridge_time_In = etxtWithWgtBdgTmIn.getText().toString();
                    String wt_bridge_time_out = etxtWithWgtBdgTmOut.getText().toString();
                    String total_weight = etxtWithTtlWgt.getText().toString();
                    String tare_wt_in_mt = etxtWithTareWgt.getText().toString();
                    String gross_wt_in_mt = textViewgross_wt_in_mt.getText().toString();
                    String net_wt_in_mt = textViewnet_wt_in_mt.getText().toString();
                    String average_wt_per_bag = textViewaverage_wt_per_bag.getText().toString();
                    String no_of_cut_tom_bags = etxtDepNoOfCutTomBags.getText().toString();
                    String no_of_palla_bags = etxtWithNoOfPallaBags.getText().toString();
                    String moisture = etxtWithDepMoisture.getText().toString();

                    String foreign_matter = etxtWithForeignMatter.getText().toString();
                    String fungus = etxtWithFungusDmgDiscol.getText().toString();
                    String broken = etxtWithBrkImtrOtherSeed.getText().toString();
                    String weevilled = etxtWithWeevilled.getText().toString();
                    String live_insscets = etxtWithLiveInssects.getText().toString();
                    String remark = etxtWithRemarks.getText().toString();
                    String exchangetype = spWithExchangeType.getSelectedItem().toString();
                    String depositestate = spWithState.getSelectedItem().toString();
                    String depositelocation = spWithLocation.getSelectedItem().toString();
                    String depositecmpname = spWithCMPName.getSelectedItem().toString();
                    String depositecommodityname = spWithCommodityName.getSelectedItem().toString();
                    String packingMaterialName=  spWithPackingMat.getSelectedItem().toString();
                    String current_date = getDateCurrentTime();

                    String exchange_type_id = dbHelper.exchange_type_id(exchangetype);
                    String deposite_state_id = dbHelper.get_state_id(depositestate, "CMP");
                    String deposite_location_id = dbHelper.get_location_id(depositelocation, depositestate, "CMP");
                    String deposite_cmp_id =  dbHelper.get_godown_id(depositecmpname, "CMP");
                    String deposite_commodity_id = dbHelper.get_commodity_id(depositecommodityname);

                    long long_insert_row_index= dbHelper.insert_Local_Withdrawal(client_copy_no, exchange_type_id,is_scm_deposite, is_rejected_withdraw,
                            deposite_state_id, Transaction_date, deposite_location_id, deposite_cmp_id, truck_number, deposite_commodity_id,
                            packing_materialkg, packingMaterialName, wt_of_packing_material, supplier_name, ncml_gate_pass_no, lorry_receipt, no_of_bags,
                            wt_of_empty_gunnies, wt_bridge_name,wt_bridge_slip_no, dist_of_wt_bridge_from_godown, wt_bridge_time_In,
                            wt_bridge_time_out,total_weight, tare_wt_in_mt, gross_wt_in_mt, net_wt_in_mt, average_wt_per_bag,
                            no_of_cut_tom_bags, no_of_palla_bags, moisture, foreign_matter, fungus, broken, weevilled,
                            live_insscets, remark,photo_front,photo_rear, photo_depositor, photo_emp, photo_sample_taken,current_date);
                    if (long_insert_row_index>0){
                        String sCMPName = spWithCMPName.getSelectedItem().toString();
                        String sRowIndex = String.valueOf(long_insert_row_index);
                        Log.e("Last Record Number: ",sRowIndex);

                        ArrayList<String> ArrayStringGodownNo=  dbHelper.getGodownNo("CMP",sCMPName);
                        startActivity(new Intent(WithdrawalActivity.this, WithdrawalNextActivity.class)
                                .putExtra("insert_row_index",""+long_insert_row_index)
                                .putExtra("no_of_bags",""+ no_of_bags)
                                .putExtra("no_of_qty",""+net_wt_in_mt)
                                .putExtra("StaticGodownNo",sCMPName)
                                .putExtra("CMPName", ArrayStringGodownNo));
                    }else {
                        Toast.makeText(WithdrawalActivity.this,"Error while inserting data.Please re-enter data.",Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (!(spWithExchangeType.getCount()>0))
                    {
                        SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("2. Exchange Type Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW));
                    }
                    else if (!(spWithState.getCount()>0))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("3. State Empty.")
                                .show(WithdrawalActivity.this);
                    }
                    else if (!(spWithLocation.getCount()>0))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("3. Location Empty.")
                                .show(WithdrawalActivity.this);
                    }
                    else if (!(spWithCMPName.getCount()>0))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("6. CMP Name Empty.")
                                .show(WithdrawalActivity.this);

                        // Toast.makeText(WithdrawalActivity.this, "CMP Name Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (!(spWithCommodityName.getCount()>0))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("8. Commodity Empty.")
                                .show(WithdrawalActivity.this);
                    }
                    else if (!( photo_front != null))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("Front photo Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Front photo Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (!( photo_rear != null))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("Rear photo Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Rear photo Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (!( photo_depositor != null))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("Depositor photo Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Depositor photo Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (!( photo_emp != null))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("Employee photo Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Employee photo Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (!( photo_sample_taken != null))
                    {
                        Snackbar.with(getApplicationContext())
                                .text("Sample Taken photo Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Sample Taken photo Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithTxnDate.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("4. Transaction Date is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Transaction Date is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithTruckNumber.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Truck Numner is Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Truck Numner is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithPackingkg.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Packing Kg is Emtpy.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Packing Kg is Emtpy.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithWgtPkgMtl.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Weight Package Material is Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Weight Package Material is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithSupplierName.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Supplier Name is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Supplier Name is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithNCMLGatePassNo.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Gate Pass Number is Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Gate Pass Number is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithLRGRNo.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. LR/GR Number is Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "LR/GR Number is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithNoOfBags.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. No. Of Bags is Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "No. Of Bags is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithWgtBdgName.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Weigh Bridge Name is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Weigh Bridge Name is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithWgtBdgSlNo.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Weigh Bridge Slip Number is Empty.")
                                .show(WithdrawalActivity.this);
                        //   Toast.makeText(WithdrawalActivity.this, "Weigh Bridge Slip Number is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithDistOfWgtBdgfmGod.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Distance of Weigh Bridge From Godown is Empty")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Distance of Weigh Bridge From Godown is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithWgtBdgTmIn.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Weigh Bridge Time In is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Weigh Bridge Time In is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithWgtBdgTmOut.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Weigh Bridge Time Out is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Weigh Bridge Time Out is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithTtlWgt.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Total Weight is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Total Weight is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithTareWgt.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Tare Weight is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Tare Weight is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (textViewgross_wt_in_mt.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Gross Weight in MT is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Gross Weight in MT is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (textViewnet_wt_in_mt.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Net Weight in MT is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Net Weight in MT is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (textViewaverage_wt_per_bag.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Average Weight Per Bag Empty.")
                                .show(WithdrawalActivity.this);
                        //Toast.makeText(WithdrawalActivity.this, "Average Weight Per Bag Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtDepNoOfCutTomBags.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. No. Of Cut Tom Bags is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "No. Of Cut Tom Bags is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithNoOfPallaBags.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. No. Of Palla Bags is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "No. Of Palla Bags is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithDepMoisture.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Moisture is Empty.")
                                .show(WithdrawalActivity.this);
                        //  Toast.makeText(WithdrawalActivity.this, "Moisture is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithForeignMatter.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1.Foreign Matter is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Foreign Matter is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithFungusDmgDiscol.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1.Fungus_dmg_discolored is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Fungus_dmg_discolored is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithBrkImtrOtherSeed.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Broken+Immature+Other Seed is Empty.")
                                .show(WithdrawalActivity.this);
                        //Toast.makeText(WithdrawalActivity.this, "Broken+Immature+Other Seed is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithWeevilled.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Weevilled is Empty.")
                                .show(WithdrawalActivity.this);
                        //Toast.makeText(WithdrawalActivity.this, "Weevilled is Empty.", Toast.LENGTH_LONG).show();
                    }
                    else if (etxtWithLiveInssects.getText().toString().trim().length() <= 0)
                    {
                        Snackbar.with(getApplicationContext())
                                .text("1. Live Insects is Empty.")
                                .show(WithdrawalActivity.this);
                        // Toast.makeText(WithdrawalActivity.this, "Live Insects is Empty.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        spWithCMPName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setCMP(spWithLocation.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        spWithState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (spWithLocation.getCount() > 0)
                {
                    setLocation(spWithExchangeType.getSelectedItem().toString().trim(), spWithState.getSelectedItem().toString().trim());
                }
                else
                {
                    //setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), location_name, spinner_state.getSelectedItem().toString().trim());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spWithExchangeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //setCommodity(spWithExchangeType.getSelectedItem().toString(), spWithLocation.getSelectedItem().toString());
                String sText = spWithExchangeType.getSelectedItem().toString();
                if(sText.equals("SPOT"))
                {
                    checkbox_meat_SCM.setEnabled(false);
                }
                else if (sText.equals("NCDEX"))
                {
                    checkbox_meat_SCM.setEnabled(false);
                }
                else
                {
                    checkbox_meat_SCM.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spWithLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCMP(spWithLocation.getSelectedItem().toString());
                setCommodity(spWithExchangeType.getSelectedItem().toString(), spWithLocation.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnWithPtTruckFrontSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(TruckFrontSide);
            }
        });
        btnWithPtTruckRearSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(TruckRearSide);
            }
        });
        btnWithPtDepositor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(DepPtDepositor);
            }
        });
        btnWithPtEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(DepPtEmp);
            }
        });
        btnWithPtSmpTkn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(DepPtSmpTkn);
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_meat_SCM:
                if (checked) {
                }
                else
                {
                }   break;
        }
    }
    private String getDateCurrentTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private void depositerSetUp()
    {
        setState();
        setExchangeType();
    }

    private void setState()
    {
        adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getState("CMP"));
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWithState.setAdapter(adapter_state);
    }

    private void setExchangeType() {
        adapter_exchtype = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getExchangeType());
        //Log.e("exchange type123",""+adapter_exchtype);
        adapter_exchtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWithExchangeType.setAdapter(adapter_exchtype);
    }

    private void setLocation(String exchange_type_name,  String state_name)
    {
        adapter_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getCommodity_Location(exchange_type_name,  state_name));
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWithLocation.setAdapter(adapter_location);
    }

    private void setCommodity(String exchange_type_name, String location_name)
    {
        adapter_commodity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getCommodity_ExchangeType(exchange_type_name, location_name));
        adapter_commodity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWithCommodityName.setAdapter(adapter_commodity);
    }

    private void setCMP(String locationName)
    {
        adapter_godown = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.get_CMP("CMP", locationName));
        adapter_godown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWithCMPName.setAdapter(adapter_godown);
    }

    public void open(int requestCode)
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TruckFrontSide)
        {
            if (resultCode == RESULT_OK && data !=null )
            {
                photo_front = (Bitmap) data.getExtras().get("data");
                ivDepPtTruckFrontSide.setImageBitmap(photo_front);
                Toast.makeText(WithdrawalActivity.this, "Front Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == TruckRearSide)
        {
            if (resultCode == RESULT_OK && data !=null )
            {
                photo_rear = (Bitmap) data.getExtras().get("data");
                ivDepPtTruckRearSide.setImageBitmap(photo_rear);
                Toast.makeText(WithdrawalActivity.this, "Rear Side Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == DepPtDepositor)
        {
            if (resultCode == RESULT_OK && data !=null )
            {
                photo_depositor = (Bitmap) data.getExtras().get("data");
                ivDepPtDepositor.setImageBitmap(photo_depositor);
                Toast.makeText(WithdrawalActivity.this, "Depositor Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == DepPtEmp)
        {
            if (resultCode == RESULT_OK && data !=null )
            {
                photo_emp = (Bitmap) data.getExtras().get("data");
                ivDepPtEmp.setImageBitmap(photo_emp);
                Toast.makeText(WithdrawalActivity.this, "Employee Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == DepPtSmpTkn)
        {
            if (resultCode == RESULT_OK && data !=null )
            {
                photo_sample_taken = (Bitmap) data.getExtras().get("data");
                ivDepPtSmpTkn.setImageBitmap(photo_sample_taken);
                Toast.makeText(WithdrawalActivity.this, "Sample Taken Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,int quality)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    private void getref()
    {
        spWithExchangeType = (Spinner) findViewById(R.id.spWithExchangeType);
        spWithState = (Spinner) findViewById(R.id.spWithState);
        spWithLocation = (Spinner) findViewById(R.id.spWithLocation);
        spWithCMPName = (Spinner) findViewById(R.id.spWithCMPName);
        spWithCommodityName = (Spinner) findViewById(R.id.spWithCommodityName);
       // txtWithDPName = (TextView) findViewById(R.id.txtWithDPName);
        spWithPackingMat=(Spinner) findViewById(R.id.spWithPackingMat);
        dbHelper = AppController.getDatabaseInstance();
        txtDepAvgWgt= (TextView)findViewById(R.id.txtDepAvgWgt);
        txtDepNetWgt= (TextView) findViewById(R.id.txtDepNetWgt);

        etxtWithClientCopy =(EditText) findViewById(R.id.etxtWithClientCopy);
        textViewis_scm_deposite=(CheckBox) findViewById(R.id.checkbox_meat_SCM);
        checkbox_meat_SCM=(CheckBox) findViewById(R.id.checkbox_meat_SCM);
        etxtWithTxnDate=(TextView) findViewById(R.id.etxtWithTxnDate);
        etxtWithTruckNumber=(EditText) findViewById(R.id.etxtWithTruckNumber);
        etxtWithPackingkg=(EditText) findViewById(R.id.etxtWithPackingkg);
        etxtWithWgtPkgMtl=(EditText) findViewById(R.id.etxtWithWgtPkgMtl);
        etxtWithSupplierName=(EditText) findViewById(R.id.etxtWithSupplierName);
        etxtWithNCMLGatePassNo=(EditText) findViewById(R.id.etxtWithNCMLGatePassNo);
        etxtWithLRGRNo=(EditText) findViewById(R.id.etxtWithLRGRNo);
        etxtWithNoOfBags=(EditText) findViewById(R.id.etxtWithNoOfBags);
        etxtWithWgtOfEmtGunnies=(EditText) findViewById(R.id.etxtWithWgtOfEmtGunnies);
        etxtWithWgtBdgName=(EditText) findViewById(R.id.etxtWithpWgtBdgName);
        etxtWithWgtBdgSlNo=(EditText) findViewById(R.id.etxtWithWgtBdgSlNo);
        etxtWithDistOfWgtBdgfmGod=(EditText) findViewById(R.id.etxtWithDistOfWgtBdgfmGod);
        etxtWithWgtBdgTmIn=(EditText) findViewById(R.id.etxtWithWgtBdgTmIn);
        etxtWithWgtBdgTmOut=(EditText) findViewById(R.id.etxtWithWgtBdgTmOut);
        etxtWithTtlWgt=(EditText) findViewById(R.id.etxtWithTtlWgt);
        etxtWithTareWgt=(EditText) findViewById(R.id.etxtWithTareWgt);
        textViewgross_wt_in_mt=(TextView) findViewById(R.id.txtWithGrsWgt);
        textViewnet_wt_in_mt=(TextView) findViewById(R.id.txtWithNetWgt);
        textViewaverage_wt_per_bag=(TextView) findViewById(R.id.txtWithAvgWgt);
        etxtDepNoOfCutTomBags=(EditText) findViewById(R.id.etxtDepNoOfCutTomBags);
        etxtWithNoOfPallaBags=(EditText) findViewById(R.id.etxtWithNoOfPallaBags);
        etxtWithDepMoisture=(EditText) findViewById(R.id.etxtWithDepMoisture);
        etxtWithForeignMatter=(EditText) findViewById(R.id.etxtWithForeignMatter);
        etxtWithFungusDmgDiscol=(EditText) findViewById(R.id.etxtWithFungusDmgDiscol);
        etxtWithBrkImtrOtherSeed=(EditText) findViewById(R.id.etxtWithBrkImtrOtherSeed);
        etxtWithWeevilled=(EditText) findViewById(R.id.etxtWithWeevilled);
        etxtWithLiveInssects=(EditText) findViewById(R.id.etxtWithLiveInssects);
        etxtWithRemarks=(EditText) findViewById(R.id.etxtWithRemarks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_deposit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}