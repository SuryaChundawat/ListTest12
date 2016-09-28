package com.coder.sample;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.database.DbHelper;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DepositActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 0;
    EditText etxtDepClientCopy, etxtDepTruckNumber, etxtDepPackingkg, etxtDepNoOfBags,
            etxtDepWgtPkgMtl, etxtDepWgtOfEmtGunnies, etxtDepTtlWgt, etxtDepTareWgt, etxtDepSupplierName, etxtDepNCMLGatePassNo, etxtDepLRGRNo,
            etxtDepWgtBdgName, etxtDepWgtBdgSlNo, etxtDepDistOfWgtBdgfmGod, etxtDepWgtBdgTmIn, etxtDepWgtBdgTmOut, etxtDepNoOfCutTomBags,
            etxtDepNoOfPallaBags, etxtDepDepMoisture, etxtDepForeignMatter, etxtDepFungusDmgDiscol, etxtDepBrkImtrOtherSeed, etxtDepWeevilled,
            etxtDepLiveInssects, etxtDepRemarks;
    CheckBox checkbox_meat;
    Spinner spDepState, spDepLocation, spDepCMPName, spDepExchangeType, spDepClientName, spDepCommodityName, spDepPackingMat;
    TextView textViewclient_copy_no, textViewis_scm_deposite, textViewTransaction_date,
            textViewtruck_number, textViewpacking, textViewpacking_material, textViewwt_of_packing_material,
            textViewsupplier_name, textViewncml_gate_pass_no, textViewlorry_receipt, textViewno_of_bags, textViewwt_of_empty_gunnies, textViewwt_bridge_name,
            textViewwt_bridge_slip_no, textViewdist_of_wt_bridge_from_godown, textViewwt_bridge_time_id, textViewwt_bridge_time_out,
            textViewtotal_weight, textViewtare_wt_in_mt, textViewgross_wt_in_mt, textViewnet_wt_in_mt, textViewaverage_wt_per_bag,
            textViewno_of_cut_tom_bags, textViewno_of_palla_bags, textViewmoisture, textViewforeign_matter, textViewfungus, textViewbroken, textViewweevilled,
            textViewlive_insscets, textViewtextViewremark, textViewphoto_front, textViewphoto_rear, textViewphoto_depositor, textViewphoto_employee,
            textViewphoto_sample_taken, txtDepDPName, textView_id, textView_date_and_time, Distance_weight_C, etxtDepGrsWgt, txtDepNetWgt, txtDepAvgWgt, etxtDepTxnDate;

    private ArrayAdapter<String> adapter_state, adapter_location, adapter_godown, adapter_exchtype, adapter_commodity;
    public int btn_sampleTaken = 0, btn_Emp = 0, btn_Front = 0, btn_Rear = 0, btn_Depositor = 0;
    private DbHelper dbHelper;
    private GPSTracker gps;
    public Button btnButton;
    public double num_A, num_B, sum_c;
    Button btnDepPtTruckFrontSide, btnDepPtTruckRearSide, btnDepPtDepositor, btnDepPtEmp, btnDepPtSmpTkn, Depbutton_submit_Next;
    ImageView ivDepPtTruckFrontSide, ivDepPtTruckRearSide, ivDepPtDepositor, ivDepPtEmp, ivDepPtSmpTkn;
    private ImageView imageView_pic_front, imageView_pic_rear, imageView_pic_Depositor, imageView_pic_Emp, imageView_pic_SmpTkn;
    private Bitmap photo_front, photo_rear, photo_depositor, photo_emp, photo_sample_taken;
    Double dbDepWgtPkgMtl, dbDepNoOfBags;
    private int TruckFrontSide = 1;
    private int TruckRearSide = 2;
    private int DepPtDepositor = 3;
    private int DepPtEmp = 4;
    private int DepPtSmpTkn = 5;

    String DepWgtPkgMtl, DepNoOfBags, DepWgtOfEmtGunnies, DepTtlWgt, DepTareWgt, DepClientCopy, DepTxnDate, DepGrsWgt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkbox_meat = (CheckBox) findViewById(R.id.checkbox_meat);
        btnDepPtTruckFrontSide = (Button) findViewById(R.id.btnDepPtTruckFrontSide);
        btnDepPtTruckRearSide = (Button) findViewById(R.id.btnDepPtTruckRearSide);
        btnDepPtDepositor = (Button) findViewById(R.id.btnDepPtDepositor);
        btnDepPtEmp = (Button) findViewById(R.id.btnDepPtEmp);
        btnDepPtSmpTkn = (Button) findViewById(R.id.btnDepPtSmpTkn);

        ivDepPtTruckFrontSide = (ImageView) findViewById(R.id.ivDepPtTruckFrontSide);
        ivDepPtTruckRearSide = (ImageView) findViewById(R.id.ivDepPtTruckRearSide);
        ivDepPtDepositor = (ImageView) findViewById(R.id.ivDepPtDepositor);
        ivDepPtEmp = (ImageView) findViewById(R.id.ivDepPtEmp);
        ivDepPtSmpTkn = (ImageView) findViewById(R.id.ivDepPtSmpTkn);

        etxtDepWgtPkgMtl = (EditText) findViewById(R.id.etxtDepWgtPkgMtl);
        etxtDepNoOfBags = (EditText) findViewById(R.id.etxtDepNoOfBags);
        etxtDepWgtOfEmtGunnies = (EditText) findViewById(R.id.etxtDepWgtOfEmtGunnies);
        etxtDepTtlWgt = (EditText) findViewById(R.id.etxtDepTtlWgt);
        etxtDepTareWgt = (EditText) findViewById(R.id.etxtDepTareWgt);
        etxtDepClientCopy = (EditText) findViewById(R.id.etxtDepClientCopy);
        etxtDepTxnDate = (TextView) findViewById(R.id.etxtDepTxnDate);
        etxtDepGrsWgt = (TextView) findViewById(R.id.txtDepGrsWgt);

        DepWgtOfEmtGunnies = etxtDepWgtOfEmtGunnies.getText().toString();
        DepTtlWgt = etxtDepTtlWgt.getText().toString();
        DepTareWgt = etxtDepTareWgt.getText().toString();
        DepClientCopy = etxtDepClientCopy.getText().toString();
        DepTxnDate = etxtDepTxnDate.getText().toString();
        DepGrsWgt = etxtDepGrsWgt.getText().toString();

        getref();
        depositerSetUp();


        final Spinner spDepPackingMat = (Spinner) findViewById(R.id.spDepPackingMat);
        List<String> catega = new ArrayList<String>();
        catega.add("Jute");
        catega.add("Plastic");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catega);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spDepPackingMat.setAdapter(dataAdapter);

        Depbutton_submit_Next = (Button) findViewById(R.id.Depbutton_submit_Next);

        etxtDepWgtPkgMtl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtDepWgtPkgMtl.getText().toString().isEmpty() ? etxtDepWgtPkgMtl.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtDepNoOfBags.getText().toString().isEmpty() ? etxtDepNoOfBags.getText().toString() : "0");
                Double value1 = (v1 * v2) / (1000 * 1000);  // Completed
                String value = String.format("%.3f", value1);// Math.round(value1,2);
                etxtDepWgtOfEmtGunnies.setText(value);
            }
        });

        etxtDepNoOfBags.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtDepWgtPkgMtl.getText().toString().isEmpty() ? etxtDepWgtPkgMtl.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtDepNoOfBags.getText().toString().isEmpty() ? etxtDepNoOfBags.getText().toString() : "0");
                Double value1 = (v1 * v2) / (1000 * 1000);
                String value = String.format("%.3f", value1);
                etxtDepWgtOfEmtGunnies.setText(value);
            }
        });

        etxtDepTxnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(DepositActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                       /* textView_date_and_time.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);*/

                        //textView_date_and_time.setText((year)+"/"+(monthOfYear + 1) + "/"+dayOfMonth);
                        String sdayOfMonth, smonthOfYear;

                        if (dayOfMonth < 10) {
                            sdayOfMonth = "0" + dayOfMonth;
                        } else {
                            sdayOfMonth = "" + dayOfMonth;
                        }

                        monthOfYear = monthOfYear + 1;
                        if (monthOfYear < 10) {
                            smonthOfYear = "0" + monthOfYear;
                        } else {
                            smonthOfYear = "" + monthOfYear;
                        }

                        etxtDepTxnDate.setText(sdayOfMonth + "/" + (smonthOfYear) + "/" + (year));

                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(new Date().getTime());
                dpd.show();
            }
        });

        etxtDepTtlWgt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtDepTtlWgt.getText().toString().isEmpty() ? etxtDepTtlWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtDepTareWgt.getText().toString().isEmpty() ? etxtDepTareWgt.getText().toString() : "0");
                Double value = v1 - v2;
                etxtDepGrsWgt.setText(value.toString());
            }
        });

        etxtDepTareWgt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtDepTtlWgt.getText().toString().isEmpty() ? etxtDepTtlWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtDepTareWgt.getText().toString().isEmpty() ? etxtDepTareWgt.getText().toString() : "0");
                Double value = v1 - v2;
                etxtDepGrsWgt.setText(value.toString());
            }
        });

        etxtDepGrsWgt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Double v1 = Double.parseDouble(!etxtDepGrsWgt.getText().toString().isEmpty() ? etxtDepGrsWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtDepWgtOfEmtGunnies.getText().toString().isEmpty() ? etxtDepWgtOfEmtGunnies.getText().toString() : "0");
                Double value = v1 - v2;
                txtDepNetWgt.setText(value.toString());

            }
        });

        etxtDepWgtOfEmtGunnies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!etxtDepGrsWgt.getText().toString().isEmpty() ? etxtDepGrsWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtDepWgtOfEmtGunnies.getText().toString().isEmpty() ? etxtDepWgtOfEmtGunnies.getText().toString() : "0");
                Double value = v1 - v2;
                txtDepNetWgt.setText(value.toString());
            }
        });

        txtDepNetWgt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double v1 = Double.parseDouble(!txtDepNetWgt.getText().toString().isEmpty() ? txtDepNetWgt.getText().toString() : "0");
                Double v2 = Double.parseDouble(!etxtDepNoOfBags.getText().toString().isEmpty() ? etxtDepNoOfBags.getText().toString() : "0");
                Double value1 = (v1 / v2) * 1000;
                String value = String.format("%.3f", value1);
                txtDepAvgWgt.setText(value);
            }
        });

        Depbutton_submit_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spDepExchangeType.getCount() > 0
                        && spDepState.getCount() > 0
                        && spDepLocation.getCount() > 0
                        && spDepCMPName.getCount() > 0
                        && spDepCommodityName.getCount() > 0
                        && (photo_front != null)
                        && (photo_rear != null)
                        && (photo_depositor != null)
                        && (photo_emp != null)
                        && (photo_sample_taken != null)
                        && etxtDepTxnDate.getText().toString().trim().length() > 0
                        && etxtDepTruckNumber.getText().toString().trim().length() > 0
                        && etxtDepPackingkg.getText().toString().trim().length() > 0
                        && etxtDepWgtPkgMtl.getText().toString().trim().length() > 0
                        && etxtDepSupplierName.getText().toString().trim().length() > 0
                        && etxtDepNCMLGatePassNo.getText().toString().trim().length() > 0
                        && etxtDepLRGRNo.getText().toString().trim().length() > 0
                        && etxtDepNoOfBags.getText().toString().trim().length() > 0
                        && etxtDepWgtBdgName.getText().toString().trim().length() > 0
                        && etxtDepWgtBdgSlNo.getText().toString().trim().length() > 0
                        && etxtDepDistOfWgtBdgfmGod.getText().toString().trim().length() > 0
                        && etxtDepWgtBdgTmIn.getText().toString().trim().length() > 0
                        && etxtDepWgtBdgTmOut.getText().toString().trim().length() > 0
                        && etxtDepTtlWgt.getText().toString().trim().length() > 0
                        && etxtDepTareWgt.getText().toString().trim().length() > 0
                        && textViewgross_wt_in_mt.getText().toString().trim().length() > 0
                        && textViewnet_wt_in_mt.getText().toString().trim().length() > 0
                        && textViewaverage_wt_per_bag.getText().toString().trim().length() > 0
                        && etxtDepNoOfCutTomBags.getText().toString().trim().length() > 0
                        && etxtDepNoOfPallaBags.getText().toString().trim().length() > 0
                        && etxtDepDepMoisture.getText().toString().trim().length() > 0
                        && etxtDepForeignMatter.getText().toString().trim().length() > 0
                        && etxtDepFungusDmgDiscol.getText().toString().trim().length() > 0
                        && etxtDepBrkImtrOtherSeed.getText().toString().trim().length() > 0
                        && etxtDepWeevilled.getText().toString().trim().length() > 0
                        && etxtDepLiveInssects.getText().toString().trim().length() > 0
                        ) {
                    String client_copy_no = etxtDepClientCopy.getText().toString();
                    String ChckValue;
                    if (checkbox_meat.isChecked() == true) {
                        ChckValue = "1";
                    } else {
                        ChckValue = "0";
                    }
                    String is_scm_deposite = ChckValue;
                    String Transaction_date = etxtDepTxnDate.getText().toString();
                    String truck_number = etxtDepTruckNumber.getText().toString();
                    String packing_materialkg = etxtDepPackingkg.getText().toString();
                    String wt_of_packing_material = etxtDepWgtPkgMtl.getText().toString();
                    String supplier_name = etxtDepSupplierName.getText().toString();
                    String ncml_gate_pass_no = etxtDepNCMLGatePassNo.getText().toString();
                    String lorry_receipt = etxtDepLRGRNo.getText().toString();
                    String no_of_bags = etxtDepNoOfBags.getText().toString();
                    String wt_of_empty_gunnies = etxtDepWgtOfEmtGunnies.getText().toString();
                    String wt_bridge_name = etxtDepWgtBdgName.getText().toString();
                    String wt_bridge_slip_no = etxtDepWgtBdgSlNo.getText().toString();
                    String dist_of_wt_bridge_from_godown = etxtDepDistOfWgtBdgfmGod.getText().toString();
                    String wt_bridge_time_In = etxtDepWgtBdgTmIn.getText().toString();
                    String wt_bridge_time_out = etxtDepWgtBdgTmOut.getText().toString();
                    String total_weight = etxtDepTtlWgt.getText().toString();
                    String tare_wt_in_mt = etxtDepTareWgt.getText().toString();
                    String gross_wt_in_mt = textViewgross_wt_in_mt.getText().toString();
                    String net_wt_in_mt = textViewnet_wt_in_mt.getText().toString();
                    String average_wt_per_bag = textViewaverage_wt_per_bag.getText().toString();
                    String no_of_cut_tom_bags = etxtDepNoOfCutTomBags.getText().toString();
                    String no_of_palla_bags = etxtDepNoOfPallaBags.getText().toString();
                    String moisture = etxtDepDepMoisture.getText().toString();

                    String foreign_matter = etxtDepForeignMatter.getText().toString();
                    String fungus = etxtDepFungusDmgDiscol.getText().toString();
                    String broken = etxtDepBrkImtrOtherSeed.getText().toString();
                    String weevilled = etxtDepWeevilled.getText().toString();
                    String live_insscets = etxtDepLiveInssects.getText().toString();
                    String remark = etxtDepRemarks.getText().toString();
                    String exchangetype = spDepExchangeType.getSelectedItem().toString();
                    String depositestate = spDepState.getSelectedItem().toString();
                    String depositelocation = spDepLocation.getSelectedItem().toString();
                    String depositecmpname = spDepCMPName.getSelectedItem().toString();
                    String depositecommodityname = spDepCommodityName.getSelectedItem().toString();
                    String packingMaterialName = spDepPackingMat.getSelectedItem().toString();
                    String current_date = getDateCurrentTime();

                    String exchange_type_id = dbHelper.exchange_type_id(exchangetype);
                    String deposite_state_id = dbHelper.get_state_id(depositestate, "CMP");
                    String deposite_location_id = dbHelper.get_location_id(depositelocation, depositestate, "CMP");
                    String deposite_cmp_id = dbHelper.get_godown_id(depositecmpname, "CMP");
                    String deposite_commodity_id = dbHelper.get_commodity_id(depositecommodityname);

                    long long_insert_row_index = dbHelper.insert_Local_Deposit(client_copy_no, exchange_type_id, is_scm_deposite,
                            deposite_state_id, Transaction_date, deposite_location_id, deposite_cmp_id, truck_number, deposite_commodity_id,
                            packing_materialkg, packingMaterialName, wt_of_packing_material, supplier_name, ncml_gate_pass_no, lorry_receipt, no_of_bags,
                            wt_of_empty_gunnies, wt_bridge_name, wt_bridge_slip_no, dist_of_wt_bridge_from_godown, wt_bridge_time_In,
                            wt_bridge_time_out, total_weight, tare_wt_in_mt, gross_wt_in_mt, net_wt_in_mt, average_wt_per_bag,
                            no_of_cut_tom_bags, no_of_palla_bags, moisture, foreign_matter, fungus, broken, weevilled,
                            live_insscets, remark, photo_front, photo_rear, photo_depositor, photo_emp, photo_sample_taken, current_date);
                    if (long_insert_row_index > 0) {
                        String sCMPName = spDepCMPName.getSelectedItem().toString();
                        String sRowIndex = String.valueOf(long_insert_row_index);
                        Log.e("Last Record Number: ", sRowIndex);

                        ArrayList<String> ArrayStringGodownNo = dbHelper.getGodownNo("CMP", sCMPName);
                        startActivity(new Intent(DepositActivity.this, DepositeNextActivity.class)
                                .putExtra("insert_row_index", "" + long_insert_row_index)
                                .putExtra("no_of_bags", "" + no_of_bags)
                                .putExtra("no_of_qty", "" + net_wt_in_mt)
                                .putExtra("StaticGodownNo", sCMPName)
                                .putExtra("CMPName", ArrayStringGodownNo));
                    } else {
                        Toast.makeText(DepositActivity.this, "Error while inserting data.Please re-enter data.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (!(spDepExchangeType.getCount() > 0)) {

                      /*  SnackbarManager.show(
                            Snackbar.with(getApplicationContext())
                                    .text("Exchange Type Empty.")
                                    .textColor(Color.BLACK)
                                    .color(Color.YELLOW)
                    );*/
                        Toast.makeText(DepositActivity.this, "Exchange Type Empty.", Toast.LENGTH_LONG).show();


                    } else if (!(spDepState.getCount() > 0)) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                .text("2. State Empty.")
                                .textColor(Color.BLACK)
                                .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "State Empty.", Toast.LENGTH_LONG).show();

                    } else if (!(spDepLocation.getCount() > 0)) {

                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("3. Location Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Location Empty.", Toast.LENGTH_LONG).show();

                    } else if (!(spDepCMPName.getCount() > 0)) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("1. CMP Name Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/

                        Toast.makeText(DepositActivity.this, "CMP Name Empty.", Toast.LENGTH_LONG).show();
                    } else if (!(spDepCommodityName.getCount() > 0)) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("1. Commodity Empty")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Commodity Empty.", Toast.LENGTH_LONG).show();
                    } else if (!(photo_front != null)) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("1.Front photo Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Front photo Empty.", Toast.LENGTH_LONG).show();
                    } else if (!(photo_rear != null)) {
                        Toast.makeText(DepositActivity.this, "Rear photo Empty.", Toast.LENGTH_LONG).show();
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("1.Rear photo Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        //  Toast.makeText(DepositActivity.this, "Depositor photo Empty.", Toast.LENGTH_LONG).show();
                    } else if (!(photo_depositor != null)) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Depositor photo Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Depositor photo Empty.", Toast.LENGTH_LONG).show();
                    } else if (!(photo_emp != null)) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Employee photo Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Employee photo Empty.", Toast.LENGTH_LONG).show();
                    } else if (!(photo_sample_taken != null)) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Sample Taken photo Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Sample Taken photo Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepTxnDate.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Transaction Date is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Transaction Date is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepTruckNumber.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Truck Numner is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Truck Numner is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepPackingkg.getText().toString().trim().length() <= 0) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Packing Kg is Emtpy.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Packing Kg is Emtpy.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepWgtPkgMtl.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Weight Package Material is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Weight Package Material is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepSupplierName.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Supplier Name is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Supplier Name is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepNCMLGatePassNo.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Gate Pass Number is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Gate Pass Number is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepLRGRNo.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("LR/GR Number is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "LR/GR Number is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepNoOfBags.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("No. Of Bags is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "No. Of Bags is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepWgtBdgName.getText().toString().trim().length() <= 0) {

                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Weigh Bridge Name is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Weigh Bridge Name is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepWgtBdgSlNo.getText().toString().trim().length() <= 0) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Weigh Bridge Slip Number is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Weigh Bridge Slip Number is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepDistOfWgtBdgfmGod.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Distance of Weigh Bridge From Godown is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Distance of Weigh Bridge From Godown is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepWgtBdgTmIn.getText().toString().trim().length() <= 0) {

                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Weigh Bridge Time In is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Weigh Bridge Time In is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepWgtBdgTmOut.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Weigh Bridge Time Out is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Weigh Bridge Time Out is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepTtlWgt.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Total Weight is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Total Weight is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepTareWgt.getText().toString().trim().length() <= 0) {

                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Tare Weight is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Tare Weight is Empty.", Toast.LENGTH_LONG).show();
                    } else if (textViewgross_wt_in_mt.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Gross Weight in MT is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Gross Weight in MT is Empty.", Toast.LENGTH_LONG).show();
                    } else if (textViewnet_wt_in_mt.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Net Weight in MT is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Net Weight in MT is Empty.", Toast.LENGTH_LONG).show();
                    } else if (textViewaverage_wt_per_bag.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Average Weight Per Bag Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Average Weight Per Bag Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepNoOfCutTomBags.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("No. Of Cut Tom Bags is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "No. Of Cut Tom Bags is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepNoOfPallaBags.getText().toString().trim().length() <= 0) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("No. Of Palla Bags is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "No. Of Palla Bags is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepDepMoisture.getText().toString().trim().length() <= 0) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Moisture is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Moisture is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepForeignMatter.getText().toString().trim().length() <= 0) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Foreign Matter is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Foreign Matter is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepFungusDmgDiscol.getText().toString().trim().length() <= 0) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Fungus_dmg_discolored is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Fungus_dmg_discolored is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepBrkImtrOtherSeed.getText().toString().trim().length() <= 0) {
                        /*SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Broken+Immature+Other Seed is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Broken+Immature+Other Seed is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepWeevilled.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Weevilled is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Weevilled is Empty.", Toast.LENGTH_LONG).show();
                    } else if (etxtDepLiveInssects.getText().toString().trim().length() <= 0) {
                       /* SnackbarManager.show(
                                Snackbar.with(getApplicationContext())
                                        .text("Live Insects is Empty.")
                                        .textColor(Color.BLACK)
                                        .color(Color.YELLOW)
                        );*/
                        Toast.makeText(DepositActivity.this, "Live Insects is Empty.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        spDepCMPName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCMP(spDepLocation.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spDepState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spDepLocation.getCount() > 0) {
                    //setLocation("CMP", spDepLocation.getSelectedItem().toString().trim(), spDepState.getSelectedItem().toString().trim());
                    setLocation(spDepExchangeType.getSelectedItem().toString().trim(), spDepState.getSelectedItem().toString().trim());
                } else {
                    //setLocation(spinner_segment.getSelectedItem().toString().trim().toUpperCase(), location_name, spinner_state.getSelectedItem().toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spDepExchangeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setCommodity(spDepExchangeType.getSelectedItem().toString(), spDepLocation.getSelectedItem().toString());
                String sText = spDepExchangeType.getSelectedItem().toString();
                if (sText.equals("SPOT")) {
                    checkbox_meat.setEnabled(false);
                    checkbox_meat.setChecked(false);
                } else if (sText.equals("NCDEX")) {
                    checkbox_meat.setEnabled(false);
                    checkbox_meat.setChecked(false);
                } else {
                    checkbox_meat.setEnabled(true);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spDepLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCMP(spDepLocation.getSelectedItem().toString());
                setCommodity(spDepExchangeType.getSelectedItem().toString(), spDepLocation.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnDepPtTruckFrontSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(TruckFrontSide);
            }
        });
        btnDepPtTruckRearSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(TruckRearSide);
            }
        });
        btnDepPtDepositor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(DepPtDepositor);
            }
        });
        btnDepPtEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(DepPtEmp);
            }
        });
        btnDepPtSmpTkn.setOnClickListener(new View.OnClickListener() {
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
        switch (view.getId()) {
            case R.id.checkbox_meat:
                if (checked) {
                    // Toast.makeText(getApplicationContext(), "Checkbox Clicked", Toast.LENGTH_SHORT).show();
                    // textViewis_scm_deposite.setText("1");
                    // Put some meat on the sandwich}
                } else {
                    //textViewis_scm_deposite.setText("0");
                    // Remove the meat
                }
                break;
        }
    }

    private String getDateCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private void depositerSetUp() {
        setState();
        setExchangeType();
    }

    private void setState() {
        adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getState("CMP"));
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepState.setAdapter(adapter_state);
    }

    private void setExchangeType() {
        adapter_exchtype = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getExchangeType());
        //Log.e("exchange type123",""+adapter_exchtype);
        adapter_exchtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepExchangeType.setAdapter(adapter_exchtype);
    }

    private void setLocation(String exchange_type_name, String state_name) {
        adapter_location = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getCommodity_Location(exchange_type_name, state_name));
        adapter_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepLocation.setAdapter(adapter_location);
    }

    private void setCommodity(String exchange_type_name, String location_name) {
        adapter_commodity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.getCommodity_ExchangeType(exchange_type_name, location_name));
        adapter_commodity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepCommodityName.setAdapter(adapter_commodity);
    }

    private void setCMP(String locationName) {
        adapter_godown = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbHelper.get_CMP("CMP", locationName));
        adapter_godown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepCMPName.setAdapter(adapter_godown);
    }

    public void open(int requestCode) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TruckFrontSide) {
            if (resultCode == RESULT_OK && data != null) {
                photo_front = (Bitmap) data.getExtras().get("data");
                ivDepPtTruckFrontSide.setImageBitmap(photo_front);
                Toast.makeText(DepositActivity.this, "Front Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == TruckRearSide) {
            if (resultCode == RESULT_OK && data != null) {
                photo_rear = (Bitmap) data.getExtras().get("data");
                ivDepPtTruckRearSide.setImageBitmap(photo_rear);
                Toast.makeText(DepositActivity.this, "Rear Side Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == DepPtDepositor) {
            if (resultCode == RESULT_OK && data != null) {
                photo_depositor = (Bitmap) data.getExtras().get("data");
                ivDepPtDepositor.setImageBitmap(photo_depositor);
                Toast.makeText(DepositActivity.this, "Depositor Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == DepPtEmp) {
            if (resultCode == RESULT_OK && data != null) {
                photo_emp = (Bitmap) data.getExtras().get("data");
                ivDepPtEmp.setImageBitmap(photo_emp);
                Toast.makeText(DepositActivity.this, "Employee Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == DepPtSmpTkn) {
            if (resultCode == RESULT_OK && data != null) {
                photo_sample_taken = (Bitmap) data.getExtras().get("data");
                ivDepPtSmpTkn.setImageBitmap(photo_sample_taken);
                Toast.makeText(DepositActivity.this, "Sample Taken Successfully capture image .", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    private void getref() {
        spDepExchangeType = (Spinner) findViewById(R.id.spDepExchangeType);
        spDepState = (Spinner) findViewById(R.id.spDepState);
        spDepLocation = (Spinner) findViewById(R.id.spDepLocation);
        spDepCMPName = (Spinner) findViewById(R.id.spDepCMPName);
        spDepCommodityName = (Spinner) findViewById(R.id.spDepCommodityName);
        txtDepDPName = (TextView) findViewById(R.id.txtDepDPName);
        spDepPackingMat = (Spinner) findViewById(R.id.spDepPackingMat);
        dbHelper = AppController.getDatabaseInstance();
        txtDepAvgWgt = (TextView) findViewById(R.id.txtDepAvgWgt);
        txtDepNetWgt = (TextView) findViewById(R.id.txtDepNetWgt);

        etxtDepClientCopy = (EditText) findViewById(R.id.etxtDepClientCopy);
        textViewis_scm_deposite = (CheckBox) findViewById(R.id.checkbox_meat);
        checkbox_meat = (CheckBox) findViewById(R.id.checkbox_meat);
        etxtDepTxnDate = (TextView) findViewById(R.id.etxtDepTxnDate);
        etxtDepTruckNumber = (EditText) findViewById(R.id.etxtDepTruckNumber);
        etxtDepPackingkg = (EditText) findViewById(R.id.etxtDepPackingkg);
        etxtDepWgtPkgMtl = (EditText) findViewById(R.id.etxtDepWgtPkgMtl);
        etxtDepSupplierName = (EditText) findViewById(R.id.etxtDepSupplierName);
        etxtDepNCMLGatePassNo = (EditText) findViewById(R.id.etxtDepNCMLGatePassNo);
        etxtDepLRGRNo = (EditText) findViewById(R.id.etxtDepLRGRNo);
        etxtDepNoOfBags = (EditText) findViewById(R.id.etxtDepNoOfBags);
        etxtDepWgtOfEmtGunnies = (EditText) findViewById(R.id.etxtDepWgtOfEmtGunnies);
        etxtDepWgtBdgName = (EditText) findViewById(R.id.etxtDepWgtBdgName);
        etxtDepWgtBdgSlNo = (EditText) findViewById(R.id.etxtDepWgtBdgSlNo);
        etxtDepDistOfWgtBdgfmGod = (EditText) findViewById(R.id.etxtDepDistOfWgtBdgfmGod);
        etxtDepWgtBdgTmIn = (EditText) findViewById(R.id.etxtDepWgtBdgTmIn);
        etxtDepWgtBdgTmOut = (EditText) findViewById(R.id.etxtDepWgtBdgTmOut);
        etxtDepTtlWgt = (EditText) findViewById(R.id.etxtDepTtlWgt);
        etxtDepTareWgt = (EditText) findViewById(R.id.etxtDepTareWgt);
        textViewgross_wt_in_mt = (TextView) findViewById(R.id.txtDepGrsWgt);
        textViewnet_wt_in_mt = (TextView) findViewById(R.id.txtDepNetWgt);
        textViewaverage_wt_per_bag = (TextView) findViewById(R.id.txtDepAvgWgt);
        etxtDepNoOfCutTomBags = (EditText) findViewById(R.id.etxtDepNoOfCutTomBags);
        etxtDepNoOfPallaBags = (EditText) findViewById(R.id.etxtDepNoOfPallaBags);
        etxtDepDepMoisture = (EditText) findViewById(R.id.etxtDepDepMoisture);
        etxtDepForeignMatter = (EditText) findViewById(R.id.etxtDepForeignMatter);
        etxtDepFungusDmgDiscol = (EditText) findViewById(R.id.etxtDepFungusDmgDiscol);
        etxtDepBrkImtrOtherSeed = (EditText) findViewById(R.id.etxtDepBrkImtrOtherSeed);
        etxtDepWeevilled = (EditText) findViewById(R.id.etxtDepWeevilled);
        etxtDepLiveInssects = (EditText) findViewById(R.id.etxtDepLiveInssects);
        etxtDepRemarks = (EditText) findViewById(R.id.etxtDepRemarks);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deposit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
