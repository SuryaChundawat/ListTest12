package com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.coder.sample.LoginActivity;
import com.com.pojo.AuditPoinDetailsPojo;
import com.com.pojo.AuditPointDetailsServerPojo;
import com.com.pojo.AuditTypePojo;
import com.com.pojo.AuditorPojo;
import com.com.pojo.BankPojo;
import com.com.pojo.BranchPojo;
import com.com.pojo.CMPPojo;
import com.com.pojo.CommodityOnLocationPojo;
import com.com.pojo.CommodityPojo;
import com.com.pojo.DistrictPojo;
import com.com.pojo.ExchangeTypePojo;
import com.com.pojo.GodownCmAndCmpPojo;
import com.com.pojo.LocationCmAndCmpPojo;
import com.com.pojo.SegmentPojo;
import com.com.pojo.StatePojo;
import com.com.pojo.UserRolePojo;
import com.com.pojo.WareHouseInspectionDynamicPojo;
import com.com.pojo.WareHouseInspectionHeaderPojo;
import com.com.pojo.WareHouseInspectionPojo;
import com.com.pojo.WareHousePojo;
import com.nispok.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashitosh on 17-08-2015.
 */
public class DbHelper extends SQLiteOpenHelper {

   /* Context context;*/

    public String local_addressTemp;

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "sample_database";
    private SQLiteDatabase db;

    private static final String COMMON_ID = "_id";

    private static final String LOGIN_TABLE = "login_table";
    private static final String USER_ROLE_TABLE = "user_role_table";
    private static final String LOCATION_TABLE = "location_table";
    private static final String SEGMENT_TABLE = "segment_table";
    private static final String STATE_TABLE = "state_table";
    private static final String GODOWN_TABLE = "godown_table";
    private static final String AUDIT_TYPE_TABLE = "audit_typ" + "e_table";
    private static final String AUDITOR_TABLE = "auditor_table";
    private static final String WAREHOUSE_TABLE = "warehouse_table";
    private static final String AUDIT_POINT_TABLE = "audit_point_table";
    private static final String BANK_TABLE = "bank_table";
    private static final String BRANCH_TABLE = "branch_table";
    private static final String DISTRICT_TABLE = "district_table";
    private static final String DEPOSIT_TABLE = "deposit_table";
    private static final String DEPOSIT_TABLE_DETAILS = "deposit_table_details";

    private static final String WITHDRAWAL_TABLE = "withdrawal_table";
    private static final String WITHDRAWAL_TABLE_DETAILS = "withdrawal_table_details";


    //for ExchangeType Table

    private static final String EXCHANGE_TYPE_TABLE = "Exchange_type_table";
    private static final String LOCAL_TABLE_EXCHANGE_TYPE_ID = "exchange_type_id";
    private static final String LOCAL_TABLE_EXCHANGE_TYPE_NAME = "exchange_type_name";

    //For Commodity on Location
    private static final String COMMODITY_ON_LOCATION_MAPPING = "Commoidty_Location_Mapping";
    private static final String LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_NAME = "commodity_location_mapping_commodity_name";
    private static final String LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_ID = "commodity_location_mapping_commodity_id";
    private static final String LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME = "commodity_location_mapping_exchange_type_name";
    private static final String LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_ID = "commodity_location_mapping_exchange_id";
    private static final String LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME = "commodity_location_mapping_location_name";
    private static final String LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_ID = "commodity_location_mapping_location_id";
    private static final String LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_ID = "commodity_location_mapping_state_id";
    private static final String LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_NAME = "commodity_location_mapping_state_name";

    //FOR SM_CMP

    private static final String CMP_TABLE = "cmp_table";
    private static final String LOCAL_TABLE_CMP_ID = "cmp_id";
    private static final String LOCAL_TABLE_CMP_NAME = "cmp_name";
    private static final String LOCAL_TABLE_CMP_LOCATION_ID = "cmp_location_id";
    private static final String LOCAL_TABLE_CMP_CONCURRENCY = "concurrency";

    // For ComModity namee

    private static final String COMMODITY_TYPE_TABLE = "commodity_type_table";
    private static final String LOCAL_TABLE_COMMODITY_TYPE_ID = "commidity_type_id";
    private static final String LOCAL_TABLE_COMMODITY_TYPE_NAME = "commodity_type_name";
    private static final String LOCAL_TABLE_COMMODITY_EXCHANGE_TYPE_ID = "commodity_exchange_type_id";
    private static final String LOCAL_TABLE_COMMODITY_EXCHANGE_TYPE_NAME = "commodity_exchange_type_name";


    //for Deposit Header Table
    private static final String DEPOSIT_DYNAMIC_TABLE = "Deposit_dynamic_table";
    private static final String LOCAL_DEPOSIT_TABLE_DEPOSIT_ID = "deposit_depositid";
    private static final String LOCAL_DEPOSIT_TABLE_CLIENT_COPYNO = "deposit_client_copyno";
    private static final String LOCAL_DEPOSIT_TABLE_EXCHANGE_TYPEID = "deposit_exchange_type_id";
    private static final String LOCAL_DEPOSIT_TABLE_EXCHANGE_TYPE_NAME = "deposit_exchange_type_name";
    private static final String LOCAL_DEPOSIT_TABLE_STATE_ID = "State_id";
    private static final String LOCAL_DEPOSIT_TABLE_IS_SCM_DEPOSIT = "deposit_is_scm_deposit";
    private static final String LOCAL_DEPOSIT_TABLE_TRANSACTION_DATE = "deposit_transaction_date";
    private static final String LOCAL_DEPOSIT_TABLE_LOCATION_ID = "deposit_location_id";
    private static final String LOCAL_DEPOSIT_TABLE_SM_PREFIX = "deposit_sm_prefix";
    private static final String LOCAL_DEPOSIT_TABLE_CMPID = "deposit_cmp_id";
    private static final String LOCAL_DEPOSIT_TABLE_CMP_NAME = "deposit_cmp_name";
    private static final String LOCAL_DEPOSIT_TABLE_TRUCKNO = "deposit_truck_no";
    private static final String LOCAL_DEPOSIT_TABLE_COMMODITY_ID = "deposit_commodity_id";
    private static final String LOCAL_DEPOSIT_TABLE_COMMODITY_NAME = "deposit_commodity_name";
    private static final String LOCAL_DEPOSIT_TABLE_PACKING_KG = "deposit_packing";
    private static final String LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_TYPE = "packing_material_type";
    private static final String LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_WEIGHT = "deposit_packing_material_weight";
    private static final String LOCAL_DEPOSIT_TABLE_SUPPLIER_NAME = "deposit_supplier_name";
    private static final String LOCAL_DEPOSIT_TABLE_NCML_GATE_PASSNO = "deposit_ncml_gate_passno";
    private static final String LOCAL_DEPOSIT_TABLE_LR_GR_NO = "deposit_lr_gr_no";
    private static final String LOCAL_DEPOSIT_TABLE_NO_OF_BAGS = "deposit_no_of_bags";
    private static final String LOCAL_DEPOSIT_TABLE_EMPTY_GUNNIES_WEIGHT = "deposit_empty_gunnies_weight";
    private static final String LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_NAME = "deposit_weigh_bridge_name";
    private static final String LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_SLIP_NO = "deposit_weigh_bridge_slip_no";
    private static final String LOCAL_DEPOSIT_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN = "deposit_distance_weigh_bridge_from_godown";
    private static final String LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEIN = "deposit_weigh_bridge_time_in";
    private static final String LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEOUT = "deposit_weigh_bridge_time_out";
    private static final String LOCAL_DEPOSIT_TABLE_TOTAL_WEIGHT = "deposit_total_weight";
    private static final String LOCAL_DEPOSIT_TABLE_TARE_WEIGHT = "deposit_tare_weight";
    private static final String LOCAL_DEPOSIT_TABLE_GROSS_WEIGHT = "deposit_gross_weight";
    private static final String LOCAL_DEPOSIT_TABLE_NET_WEIGHT = "deposit_net_weight";
    private static final String LOCAL_DEPOSIT_TABLE_AVERAGE_WEIGHT = "deposit_average_weight";
    private static final String LOCAL_DEPOSIT_TABLE_NO_CUT_AND_TOM_BAGS = "deposit_no_cut_and_tom_bags";
    private static final String LOCAL_DEPOSIT_TABLE_NO_PALLA_BAGS = "deposit_no_palla_bags";
    private static final String LOCAL_DEPOSIT_TABLE_MOISTURE = "deposit_moisture";
    private static final String LOCAL_DEPOSIT_TABLE_FOREIGN_MATTER = "deposit_foreign_matter";
    private static final String LOCAL_DEPOSIT_TABLE_FUNGUS_DMG_DISCOLORED = "deposit_dmg_disclosured";
    private static final String LOCAL_DEPOSIT_TABLE_BROKEN_IMMATURE = "deposit_broken_immature";
    private static final String LOCAL_DEPOSIT_TABLE_WEEVILLED = "deposit_weevilled";
    private static final String LOCAL_DEPOSIT_TABLE_LIVE_INSECTS = "deposit_live_insects";
    private static final String LOCAL_DEPOSIT_TABLE_REMARKS = "deposit_deposit_remakrs";
    private static final String LOCAL_DEPOSIT_TABLE_TRUCK_FRONT_IMAGE_NAME = "deposit_truck_front_image_name";
    private static final String LOCAL_DEPOSIT_TABLE_TRUCK_REAR_IMAGE_NAME = "deposit_truck_rear_image_name";
    private static final String LOCAL_DEPOSIT_TABLE_DEPOSITOR_IMAGE_NAME = "deposit_depositor_image_name";
    private static final String LOCAL_DEPOSIT_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME = "deposit_emp_accep_goods_image_name";
    private static final String LOCAL_DEPOSIT_TABLE_SAMPLE_TAKEN_IMAGE_NAME = "deposit_sample_taken_image_name";
    private static final String LOCAL_DEPOSIT_TABLE_CREATED_BY = "deposit_created_by";
    private static final String LOCAL_DEPOSIT_TABLE_CREATED_DATE = "deposit_created_date";
    private static final String LOCAL_DEPOSIT_TABLE_IS_POSTED_ON_SERVER = "is_posted_on_server_deposit";


    //for Withdrawal Header Table
    private static final String WITHDRAWAL_DYNAMIC_TABLE = "WITHDRAWAL_dynamic_table";
    private static final String LOCAL_WITHDRAWAL_TABLE_WITHDRAWAL_ID = "WITHDRAWAL_WITHDRAWALid";
    private static final String LOCAL_WITHDRAWAL_TABLE_CLIENT_COPYNO = "WITHDRAWAL_client_copyno";
    private static final String LOCAL_WITHDRAWAL_TABLE_EXCHANGE_TYPEID = "WITHDRAWAL_exchange_type_id";
    private static final String LOCAL_WITHDRAWAL_TABLE_EXCHANGE_TYPE_NAME = "WITHDRAWAL_exchange_type_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_STATE_ID = "State_id";
    private static final String LOCAL_WITHDRAWAL_TABLE_IS_SCM_WITHDRAWAL = "WITHDRAWAL_is_scm_WITHDRAWAL";
    private static final String LOCAL_WITHDRAWAL_TABLE_IS_REJECTED_WITHDRAWAL = "WITHDRAWAL_is_scm_Rejected";
    private static final String LOCAL_WITHDRAWAL_TABLE_TRANSACTION_DATE = "WITHDRAWAL_transaction_date";
    private static final String LOCAL_WITHDRAWAL_TABLE_LOCATION_ID = "WITHDRAWAL_location_id";
    private static final String LOCAL_WITHDRAWAL_TABLE_SM_PREFIX = "WITHDRAWAL_sm_prefix";
    private static final String LOCAL_WITHDRAWAL_TABLE_CMPID = "WITHDRAWAL_cmp_id";
    private static final String LOCAL_WITHDRAWAL_TABLE_CMP_NAME = "WITHDRAWAL_cmp_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_TRUCKNO = "WITHDRAWAL_truck_no";
    private static final String LOCAL_WITHDRAWAL_TABLE_COMMODITY_ID = "WITHDRAWAL_commodity_id";
    private static final String LOCAL_WITHDRAWAL_TABLE_COMMODITY_NAME = "WITHDRAWAL_commodity_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_PACKING_KG = "WITHDRAWAL_packing";
    private static final String LOCAL_WITHDRAWAL_TABLE_PACKING_MATERIAL_TYPE = "packing_material_type";
    private static final String LOCAL_WITHDRAWAL_TABLE_PACKING_MATERIAL_WEIGHT = "WITHDRAWAL_packing_material_weight";
    private static final String LOCAL_WITHDRAWAL_TABLE_SUPPLIER_NAME = "WITHDRAWAL_supplier_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_NCML_GATE_PASSNO = "WITHDRAWAL_ncml_gate_passno";
    private static final String LOCAL_WITHDRAWAL_TABLE_LR_GR_NO = "WITHDRAWAL_lr_gr_no";
    private static final String LOCAL_WITHDRAWAL_TABLE_NO_OF_BAGS = "WITHDRAWAL_no_of_bags";
    private static final String LOCAL_WITHDRAWAL_TABLE_EMPTY_GUNNIES_WEIGHT = "WITHDRAWAL_empty_gunnies_weight";
    private static final String LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_NAME = "WITHDRAWAL_weigh_bridge_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_SLIP_NO = "WITHDRAWAL_weigh_bridge_slip_no";
    private static final String LOCAL_WITHDRAWAL_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN = "WITHDRAWAL_distance_weigh_bridge_from_godown";
    private static final String LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_TIMEIN = "WITHDRAWAL_weigh_bridge_time_in";
    private static final String LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_TIMEOUT = "WITHDRAWAL_weigh_bridge_time_out";
    private static final String LOCAL_WITHDRAWAL_TABLE_TOTAL_WEIGHT = "WITHDRAWAL_total_weight";
    private static final String LOCAL_WITHDRAWAL_TABLE_TARE_WEIGHT = "WITHDRAWAL_tare_weight";
    private static final String LOCAL_WITHDRAWAL_TABLE_GROSS_WEIGHT = "WITHDRAWAL_gross_weight";
    private static final String LOCAL_WITHDRAWAL_TABLE_NET_WEIGHT = "WITHDRAWAL_net_weight";
    private static final String LOCAL_WITHDRAWAL_TABLE_AVERAGE_WEIGHT = "WITHDRAWAL_average_weight";
    private static final String LOCAL_WITHDRAWAL_TABLE_NO_CUT_AND_TOM_BAGS = "WITHDRAWAL_no_cut_and_tom_bags";
    private static final String LOCAL_WITHDRAWAL_TABLE_NO_PALLA_BAGS = "WITHDRAWAL_no_palla_bags";
    private static final String LOCAL_WITHDRAWAL_TABLE_MOISTURE = "WITHDRAWAL_moisture";
    private static final String LOCAL_WITHDRAWAL_TABLE_FOREIGN_MATTER = "WITHDRAWAL_foreign_matter";
    private static final String LOCAL_WITHDRAWAL_TABLE_FUNGUS_DMG_DISCOLORED = "WITHDRAWAL_dmg_disclosured";
    private static final String LOCAL_WITHDRAWAL_TABLE_BROKEN_IMMATURE = "WITHDRAWAL_broken_immature";
    private static final String LOCAL_WITHDRAWAL_TABLE_WEEVILLED = "WITHDRAWAL_weevilled";
    private static final String LOCAL_WITHDRAWAL_TABLE_LIVE_INSECTS = "WITHDRAWAL_live_insects";
    private static final String LOCAL_WITHDRAWAL_TABLE_REMARKS = "WITHDRAWAL_WITHDRAWAL_remakrs";
    private static final String LOCAL_WITHDRAWAL_TABLE_TRUCK_FRONT_IMAGE_NAME = "WITHDRAWAL_truck_front_image_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_TRUCK_REAR_IMAGE_NAME = "WITHDRAWAL_truck_rear_image_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_WITHDRAWALOR_IMAGE_NAME = "WITHDRAWAL_WITHDRAWALor_image_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME = "WITHDRAWAL_emp_accep_goods_image_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_SAMPLE_TAKEN_IMAGE_NAME = "WITHDRAWAL_sample_taken_image_name";
    private static final String LOCAL_WITHDRAWAL_TABLE_CREATED_BY = "WITHDRAWAL_created_by";
    private static final String LOCAL_WITHDRAWAL_TABLE_CREATED_DATE = "WITHDRAWAL_created_date";
    private static final String LOCAL_WITHDRAWAL_TABLE_IS_POSTED_ON_SERVER = "is_posted_on_server_WITHDRAWAL";


    /// Deposit Details Table variable creation
    private static final String DEPOSIT_DYNAMIC_DETAILS_TABLE = "Deposit_Details_dynamic_table";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_ID = "deposit_details_id";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID = "deposit_details_deposit_id";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID = "deposit_details_godown_id";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO = "deposit_details_stack_no";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO = "deposit_details_lot_no";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS = "deposit_details_no_of_bags";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_QTY = "deposit_details_qty";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_ID = "deposit_details_client_id";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_NAME = "deposit_details_client_name";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_ID = "deposit_details_depository_id";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_NAME = "deposit_details_depository_name";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_IS_POSTED_ON_SERVER = "deposit_details_is_posted_on_server";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_BY = "deposit_details_created_by";
    private static final String LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_DATE = "deposit_details_created_date";


    /// Withdrawal Details Table variable creation
    private static final String WITHDRAWAL_DYNAMIC_DETAILS_TABLE = "withdrawal_Details_dynamic_table";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_ID = "withdrawal_details_id";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_WITHDRAWAL_ID = "withdrawal_details_deposit_id";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_GODOWN_ID = "withdrawal_details_godown_id";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_STACK_NO = "withdrawal_details_stack_no";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_LOT_NO = "withdrawal_details_lot_no";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_NO_OF_BAGS = "withdrawal_details_no_of_bags";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_QTY = "withdrawal_details_qty";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_RRN_WHT_NO = "withdrawal_details_client_id";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_IS_POSTED_ON_SERVER = "withdrawal_details_is_posted_on_server";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_CREATED_BY = "withdrawal_details_created_by";
    private static final String LOCAL_WITHDRAWAL_DETAILS_TABLE_CREATED_DATE = "withdrawal_details_created_date";

    //private static final String WAREHOUSE_INSPECTION_HEADER_TABLE = "warehouse_header_table";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE = "warehouse_dynamic_table";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE = "warehouse_dynamic_temp_table";
    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE = "local_warehouse_dynamic_table";
    private static final String LOCAL_ATTENDANCE_TABLE = "local_attendance_table";
    private static final String LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE = "local_godown_geo_tag_mapping_table";
    private static final String LOCAL_GODOWN_AUDIT_TABLE = "local_godown_audit_table";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE = "local_godown_audit_point_details_table";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE = "local_godown_audit_point_details_final_table";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE = "local_warehouse_header_table";
    private static final String LOCAL_UPDATED_DATABASE_DATETIME_TABLE = "local_updated_database_datetime_table";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE = "local_warehouse_inspection_information_table";
    // Deposite
    private static final String LOCAL_SM_MST_CLIENT_TABLE = "local_sm_mst_client_table";
    private static final String LOCAL_SM_MST_DEPOSITORY_TABLE = "local_sm_mst_client_table";

    private static final String LOCAL_SM_MST_CLIENT_TABLE_CLIENT_ID = "local_sm_mst_client_table_client_id";
    private static final String LOCAL_SM_MST_CLIENT_TABLE_CLIENT_NAME = "local_sm_mst_client_table_client_name";
    private static final String LOCAL_SM_MST_CLIENT_TABLE_CLIENT_DEPOSITORY_ID = "local_sm_mst_client_table_client_depository_id";

    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_INSPECTIONID = "local_warehouse_inspection_information_table_inspectionid";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_INSPECTIONNO = "local_warehouse_inspection_information_table_inspectionno";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_GODOWNADDRESS = "local_warehouse_inspection_information_table_godownAddress";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_LOCATIONID = "local_warehouse_inspection_information_table_locationid";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_NOOFGODOWNS = "local_warehouse_inspection_information_table_noofgodowns";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_BANKID = "local_warehouse_inspection_information_table_bankid";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_BANKRECREQDATE = "local_warehouse_inspection_information_table_bankrecreqdate";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_REMARKS = "local_warehouse_inspection_information_table_remarks";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_CONCURRENCY = "local_warehouse_inspection_information_table_concurrency";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_STATUSID = "local_warehouse_inspection_information_table_statusid";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_STATEID = "local_warehouse_inspection_information_table_stateid";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_DISTRICTID = "local_warehouse_inspection_information_table_districtid";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_RECORDCOUNT_INSPECTION_DETAILS = "local_warehouse_inspection_information_table_recordcount_inspectiondetails";
    private static final String LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_STATENAME = "local_warehouse_inspection_information_table_statename";

    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID = "local_warehouse_header_table_inspection_id";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DATE = "local_warehouse_header_table_date";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION = "local_warehouse_header_table_inspection";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATE_ID = "local_warehouse_header_table_state_id";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_LOCATION_ID = "local_warehouse_header_table_location_id";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_PINCODE = "local_warehouse_header_table_pincode";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DISTRICT_ID = "local_warehouse_header_table_district";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_GODOWN_ADDRESS = "local_warehouse_header_table_godown_address";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NAME_OF_GODOWN_OWNER = "local_warehouse_header_table_name_of_godown_owner";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CC_LOCATION_ID = "local_warehouse_header_table_cc_location";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BANK_ID = "local_warehouse_header_table_bank";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BRANCH_ID = "local_warehouse_header_table_branch";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_SURVEY_DONE_BY_NAME = "local_warehouse_header_table_survey_done_by_name";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DESIGNATION_ID = "local_warehouse_header_table_designation_id";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CONTACT_NO = "local_warehouse_header_table_contact_no";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER = "local_warehouse_header_table_creadted_by_date";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_RECORDCOUNT_INSPECTION_DETAILS = "local_warehouse_header_table_recordcount_inspectiondetails";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG = "local_warehouse_header_table_status_flag";
    private static final String LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NO_OF_GODOWNS = "local_warehouse_header_table_no_of_godowns";

    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE_PARTICULAR = "local_particular";
    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE_PARTICULAR_ID = "local_particular_id";
    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE_NAME = "local_name";
    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE_NAME_ID = "local_name_id";
    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE_VALUES = "local_values";
    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE_REMARK = "local_remark";
    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE_ROW_FLAG = "local_row_flag";
    private static final String LOCAL_WAREHOUSE_INSPECTION_TABLE_TYPE_FLAG = "local_type_flag";


    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR = "particular";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_ID = "particular_id";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_SEQUENCE_NO = "particular_sequence_no";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_COL = "name";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID = "name_id";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_SEQUENCE_NO = "name_sequence_no";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_BOOLEAN = "boolean";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_DATE = "date";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES = "values_name";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES_MANDATORY_VALIDATION = "values_mandatory";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MULTI_SELECT_VALIDATION = "multi_select_validation";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NUMBER_VALIDATION = "number_validation";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_TEXT_VALIDATION = "text_validation";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER = "grp_header";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER_ID = "grp_header_id";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MASTER_ITEM_TYPE_ID = "master_item_type_id";

    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_VISIBLE = "particular_visible_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_VISIBLE = "name_visible_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR = "particular_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_ID = "particular_id_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_SEQUENCE_NO = "particular_sequence_no_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL = "name_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_ID = "name_id_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_SEQUENCE_NO = "name_sequence_no_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_BOOLEAN = "boolean_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_DATE = "date_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_VALUES = "values_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_VALUES_MANDATORY_VALIDATION = "values_mandatory_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_MULTI_SELECT_VALIDATION = "multi_select_validation_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NUMBER_VALIDATION = "number_validation_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_TEXT_VALIDATION = "text_validation_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_GROUP_HEADER = "grp_header_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_GROUP_HEADER_ID = "grp_header_id_temp";
    private static final String WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_MASTER_ITEM_TYPE_ID = "master_item_type_id_temp";

    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_NO_OF_STACKS = "No_Of_Stacks";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_REASON_NOT_STACK = "Reason_Not_Stack";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_NO_OF_SAMPLES = "No_Of_Samples";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_REASON_NOT_SAMPLES = "Reason_Not_Samples";

    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_FINANCIAL_REMARK = "financial_remark";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_QUALITY_REMARK = "quality_remark";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_OVER_RULED_REMARK = "over_ruled_remark";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_ONE_ROW_CHECKER = "one_row_checker";


    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_AUDIT_POINT = "audit_point";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_AUDIT_POINT_ID = "audit_point_id";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_RESPONSE = "response";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_REMARK = "remark";
    private static final String LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_ONE_ROW_CHECKER = "one_row_checker";


    private static final String LOCAL_GODOWN_AUDIT_TABLE_AUDIT_ID = "audit_id";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_DATE = "date";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_CREATED_BY_DATE = "created_by_date";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER = "one_row_checker";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_SEGMENT = "segment";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_SEGMENT_ID = "segment_id";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_AUDIT_TYPE = "audit_type";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_AUDIT_TYPE_ID = "audit_type_id";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_AUDITOR = "auditor";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_AUDITOR_ID = "auditor_id";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_STATE = "state";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_STATE_ID = "state_id";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_LOCATION = "location";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_LOCATION_ID = "location_id";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_GODOWN = "godown";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_GODOWN_ID = "godown_id";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_ENTRY_TYPE = "entry_type";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE = "warehouse";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE_ID = "warehouse_id";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_ADDRESS = "address";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_FORM_NO = "form_no";
    private static final String LOCAL_GODOWN_AUDIT_TABLE_GODOWN_PIC = "godown_pic";
    private static final String LOCAL_DEPOSIT_TABLE_ONE_ROW_CHECKER = "one_row_checker_no";


    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN_GEO_TAG_ID = "geo_tag_id";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_SEGMENT = "geo_tag_segment";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_CURRENT_DATE = "geo_tag_current_date";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_SEGMENT_ID = "geo_tag_segment_id";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_EMP_ID = "geo_tag_emp_id";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_STATE = "geo_tag_state";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_STATE_ID = "geo_tag_state_id";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_LOCATION = "geo_tag_location";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_LOCATION_ID = "geo_tag_location_id";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN = "geo_tag_godown";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN_ID = "geo_tag_godown_id";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_LATTITUTE = "geo_tag_lat";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_LONGITUTE = "geo_tag_long";
    private static final String LOCAL_GODOWN_GEO_TAG_TABLE_ADDRESS = "geo_tag_address";


    private static final String LOCAL_ATTENDANCE_ID = "attendance_id";
    private static final String LOCAL_ATTENDANCE_EMP_ID = "attendance_name_id";
    private static final String LOCAL_ATTENDANCE_NAME = "attendance_name";
    private static final String LOCAL_ATTENDANCE_LOCATION_NAME = "attendance_location_name";
    private static final String LOCAL_ATTENDANCE_LOCATION_ID = "attendance_location_id";
    private static final String LOCAL_ATTENDANCE_LATTITUTE = "attendance_lat";
    private static final String LOCAL_ATTENDANCE_LONGITUTE = "attendance_lang";
    private static final String LOCAL_ATTENDANCE_ON_BEHALF_ID = "attendance_on_behalf_id";
    private static final String LOCAL_ATTENDANCE_SEGMENT = "attendance_segment";
    private static final String LOCAL_ATTENDANCE_SEGMENT_ID = "attendance_segment_id";
    private static final String LOCAL_ATTENDANCE_GODOWN = "attendance_godown";
    private static final String LOCAL_ATTENDANCE_GODOWN_ID = "attendance_godown_id";
    private static final String LOCAL_ATTENDANCE_DATE_TIME = "attendance_date_time";
    private static final String LOCAL_ATTENDANCE_REMARKS = "attendance_remarks";
    private static final String LOCAL_ATTENDANCE_GODOWN_PIC = "attendance_godown_pic";
    private static final String LOCAL_ATTENDANCE_LOCAL_ADDRESS = "attendance_local_address";


    private static final String LOGIN_MOBILE_NO = "lgoin_mobile_no";
    private static final String LOGIN_IMEI_NO = "lgoin_imei_no";
    private static final String LOGIN_EMPLOYEE_ID = "lgoin_emp_id";
    private static final String LOGIN_PASSWORD = "login_password";
    private static final String LOGIN_DESIGNATION_ID = "login_desingation_id";
    private static final String LOGIN_DESIGNATION_NAME = "login_desingation_name";
    private static final String LOGIN_EMP_NAME = "login_emp_name";
    private static final String LOGIN_EMP_NUM = "login_emp_num";

    private static final String BANK_TABLE_BANK_ID = "bank_id";
    private static final String BANK_TABLE_BANK_NAME = "bank_name";


    private static final String DISTRICT_TABLE_DISTRICT_ID = "district_id";
    private static final String DISTRICT_TABLE_DISTRICT_NAME = "district_name";
    private static final String DISTRICT_TABLE_DISTRICT_STATE_ID = "district_state_id";
    private static final String DISTRICT_TABLE_DISTRICT_SEGMENT_NAME = "district_segment_name";

    private static final String BRANCH_TABLE_BANK_ID = "bank_id";
    private static final String BRANCH_TABLE_BRANCH_ID = "branch_id";
    private static final String BRANCH_TABLE_BRANCH_NAME = "branch_name";


    private static final String USER_ROLE_TABLE_NAME = "role_name";
    private static final String USER_ROLE_CONDITION = "condition";


    private static final String SEGMENT_TABLE_EMP_ID = "emp_id";
    private static final String SEGMENT_TABLE_EMP_NO = "emp_no";
    private static final String SEGMENT_TABLE_EMP_NAME = "emp_name";
    private static final String SEGMENT_TABLE_SEGMENT_NAME = "segment_name";
    private static final String SEGMENT_TABLE_SEGMENT_ID = "segment_id";
    // private static final String SEGMENT_TABLE_CM_AND_CMP_TABLE = "cm_and_cmp_table";

    private static final String GODOWN_TABLE_GODOWN_ID = "godown_id";
    private static final String GODOWN_TABLE_LOCATION_ID = "location_id";
    private static final String GODOWN_TABLE_GODOWN_NAME = "godown_name";
    private static final String GODOWN_TABLE_GODOWN_ADDRESS = "godown_address";
    private static final String GODOWN_TABLE_CM_AND_CMP_TABLE = "cm_and_cmp_table";
    private static final String GODOWN_TABLE_ACTIVE = "active";
    private static final String GODOWN_TABLE_WAREHOUSE_ID = "WarehouseId";
    private static final String GODOWN_TABLE_WAREHOUSE_NAME = "WarehouseName";
    private static final String GODOWN_TABLE_WAREHOUSE_CLOSEDATE = "WarehouseCloseDate";
    private static final String GODOWN_TABLE_GODOWN_CLOSEDATE = "GodownCloseDAte";

    private static final String AUDIT_TYPE_TABLE_AUDIT_TYPE_ID = "audit_type_id";
    private static final String AUDIT_TYPE_TABLE_AUDIT_TYPE = "audit_type";
    private static final String AUDIT_TYPE_TABLE_SEGMENT_ID = "audit_type_segment_id";

    private static final String AUDITOR_TABLE_EMP_NO = "emp_no";
    private static final String AUDITOR_TABLE_EMP_ID = "emp_id";
    private static final String AUDITOR_TABLE_EMP_NAME = "emp_name";

    private static final String WAREHOUSE_TABLE_WAREHOUSE_ID = "warehouse_id";
    private static final String WAREHOUSE_TABLE_WAREHOUSE_NAME = "warehouse_name";
    private static final String WAREHOUSE_TABLE_WAREHOUSE_ADDRESS = "warehouse_address";
    private static final String WAREHOUSE_TABLE_LOCATION = "warehouse_location";
    private static final String WAREHOUSE_TABLE_LOCATION_ID = "warehouse_location_id";
    private static final String WAREHOUSE_TABLE_CM_AND_CMP = "warehouse_cm_cmp";

    private static final String AUDIT_POINT_TABLE_AUDIT_POINT_ID = "audit_point_id";
    private static final String AUDIT_POINT_TABLE_AUDIT_POINT = "audit_point";
    private static final String AUDIT_POINT_TABLE_SEGMENT_NAME = "segment_name";
    private static final String AUDIT_POINT_TABLE_SEGMENT_ID = "segment_id";
    private static final String AUDIT_POINT_TABLE_AUDIT_TYPE_ID = "audit_type_id";
    private static final String AUDIT_POINT_TABLE_AUDIT_GAP_FLAG = "audit_gap_flag";
    private static final String AUDIT_POINT_TABLE_AUDIT_SERIAL_NO = "serial_no";

    private static final String LOCATION_TABLE_LOCATION_ID = "location_id";
    private static final String LOCATION_TABLE_LOCATION_NAME = "location_name";
    private static final String LOCATION_TABLE_STATE = "state";
    private static final String LOCATION_TABLE_STATE_ID = "location_state_id";
    private static final String LOCATION_TABLE_CM_AND_CMP_TABLE = "cm_and_cmp_table";
    private static final String LOCATION_TABLE_DISTRICT_ID = "location_district_id";
    private static final String STATE_TABLE_STATE_NAME = "state_name";
    private static final String STATE_TABLE_STATE_ID = "state_state_id";
    private static final String STATE_TABLE_CM_AND_CMP_SEGMENT = "cm_and_cmp";
    private static final String STATE_TABLE_SEGMENT_ID = "segment_id";
    private static final String LOCAL_UPDATED_DATABASE_DATETIME_TABLE_ID = "Updated_database_id";
    private static final String LOCAL_UPDATED_DATABASE_DATETIME_TABLE_DATETIME = "Updated_database_datetime";

    private static final String CREATE_TABLE_UPDATED_DATABASE = "CREATE TABLE "
            + LOCAL_UPDATED_DATABASE_DATETIME_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_UPDATED_DATABASE_DATETIME_TABLE_ID + " TEXT,"
            + LOCAL_UPDATED_DATABASE_DATETIME_TABLE_DATETIME + " TEXT);";

    private static final String CREATE_TABLE_WAREHOUSE_INSPECTION_INFORMATION = "CREATE TABLE "
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_INSPECTIONID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_INSPECTIONNO + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_GODOWNADDRESS + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_LOCATIONID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_NOOFGODOWNS + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_BANKID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_BANKRECREQDATE + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_REMARKS + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_CONCURRENCY + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_STATUSID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_STATEID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_DISTRICTID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_RECORDCOUNT_INSPECTION_DETAILS + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_INFORMATION_TABLE_STATENAME + " TEXT);";

    private static final String CREATE_TABLE_LOCATION = "CREATE TABLE "
            + LOCATION_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCATION_TABLE_LOCATION_ID + " TEXT,"
            + LOCATION_TABLE_LOCATION_NAME + " TEXT,"
            + LOCATION_TABLE_STATE + " TEXT,"
            + LOCATION_TABLE_STATE_ID + " TEXT,"
            + LOCATION_TABLE_CM_AND_CMP_TABLE + " TEXT,"
            + LOCATION_TABLE_DISTRICT_ID + " TEXT);";

    private static final String CREATE_TABLE_SEGMENT = "CREATE TABLE "
            + SEGMENT_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + SEGMENT_TABLE_EMP_ID + " TEXT,"
            + SEGMENT_TABLE_EMP_NO + " TEXT,"
            + SEGMENT_TABLE_EMP_NAME + " TEXT,"
            + SEGMENT_TABLE_SEGMENT_ID + " TEXT,"
            + SEGMENT_TABLE_SEGMENT_NAME + " TEXT);";

    private static final String CREATE_TABLE_GODOWN = "CREATE TABLE "
            + GODOWN_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + GODOWN_TABLE_GODOWN_NAME + " TEXT,"
            + GODOWN_TABLE_GODOWN_ADDRESS + " TEXT,"
            + GODOWN_TABLE_GODOWN_ID + " TEXT,"
            + GODOWN_TABLE_LOCATION_ID + " TEXT,"
            + GODOWN_TABLE_ACTIVE + " TEXT,"
            + GODOWN_TABLE_CM_AND_CMP_TABLE + " TEXT,"
            + GODOWN_TABLE_WAREHOUSE_ID + " TEXT,"
            + GODOWN_TABLE_WAREHOUSE_NAME + " TEXT,"
            + GODOWN_TABLE_WAREHOUSE_CLOSEDATE + " TEXT,"
            + GODOWN_TABLE_GODOWN_CLOSEDATE + " TEXT);";

    private static final String CREATE_TABLE_AUDIT_TYPE = "CREATE TABLE "
            + AUDIT_TYPE_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + AUDIT_TYPE_TABLE_AUDIT_TYPE_ID + " TEXT,"
            + AUDIT_TYPE_TABLE_SEGMENT_ID + " TEXT,"
            + AUDIT_TYPE_TABLE_AUDIT_TYPE + " TEXT);";

    private static final String CREATE_TABLE_STATE = "CREATE TABLE "
            + STATE_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + STATE_TABLE_STATE_NAME + " TEXT,"
            + STATE_TABLE_STATE_ID + " TEXT,"
            + STATE_TABLE_CM_AND_CMP_SEGMENT + " TEXT,"
            + STATE_TABLE_SEGMENT_ID + " TEXT);";

    private static final String CREATE_TABLE_BANK = "CREATE TABLE "
            + BANK_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + BANK_TABLE_BANK_ID + " TEXT,"
            + BANK_TABLE_BANK_NAME + " TEXT);";

    private static final String CREATE_TABLE_USER_ROLE = "CREATE TABLE "
            + USER_ROLE_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + USER_ROLE_TABLE_NAME + " TEXT,"
            + USER_ROLE_CONDITION + " TEXT);";

    private static final String CREATE_TABLE_DISTRICT = "CREATE TABLE "
            + DISTRICT_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + DISTRICT_TABLE_DISTRICT_ID + " TEXT,"
            + DISTRICT_TABLE_DISTRICT_NAME + " TEXT,"
            + DISTRICT_TABLE_DISTRICT_STATE_ID + " TEXT,"
            + DISTRICT_TABLE_DISTRICT_SEGMENT_NAME + " TEXT);";

    private static final String CREATE_TABLE_BRANCH = "CREATE TABLE "
            + BRANCH_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + BRANCH_TABLE_BANK_ID + " TEXT,"
            + BRANCH_TABLE_BRANCH_ID + " TEXT,"
            + BRANCH_TABLE_BRANCH_NAME + " TEXT);";

    private static final String CREATE_TABLE_AUDITOR = "CREATE TABLE "
            + AUDITOR_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + AUDITOR_TABLE_EMP_NO + " TEXT,"
            + AUDITOR_TABLE_EMP_ID + " TEXT,"
            + AUDITOR_TABLE_EMP_NAME + " TEXT);";

    private static final String CREATE_TABLE_WAREHOUSE = "CREATE TABLE "
            + WAREHOUSE_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + WAREHOUSE_TABLE_WAREHOUSE_ID + " TEXT,"
            + WAREHOUSE_TABLE_WAREHOUSE_NAME + " TEXT,"
            + WAREHOUSE_TABLE_LOCATION + " TEXT,"
            + WAREHOUSE_TABLE_LOCATION_ID + " TEXT,"
            + WAREHOUSE_TABLE_CM_AND_CMP + " TEXT,"
            + WAREHOUSE_TABLE_WAREHOUSE_ADDRESS + " TEXT);";

    private static final String CREATE_TABLE_AUDIT_POINT = "CREATE TABLE "
            + AUDIT_POINT_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + AUDIT_POINT_TABLE_AUDIT_POINT_ID + " TEXT,"
            + AUDIT_POINT_TABLE_AUDIT_POINT + " TEXT,"
            + AUDIT_POINT_TABLE_SEGMENT_NAME + " TEXT,"
            + AUDIT_POINT_TABLE_SEGMENT_ID + " TEXT,"
            + AUDIT_POINT_TABLE_AUDIT_GAP_FLAG + " TEXT,"
            + AUDIT_POINT_TABLE_AUDIT_SERIAL_NO + " TEXT,"
            + AUDIT_POINT_TABLE_AUDIT_TYPE_ID + " TEXT);";

    private static final String CREATE_TABLE_LOCAL_ATTENDANCE = "CREATE TABLE "
            + LOCAL_ATTENDANCE_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_ATTENDANCE_ID + " TEXT,"
            + LOCAL_ATTENDANCE_EMP_ID + " TEXT,"
            + LOCAL_ATTENDANCE_NAME + " TEXT,"
            + LOCAL_ATTENDANCE_LATTITUTE + " TEXT,"
            + LOCAL_ATTENDANCE_LONGITUTE + " TEXT,"
            + LOCAL_ATTENDANCE_ON_BEHALF_ID + " TEXT,"
            + LOCAL_ATTENDANCE_LOCATION_NAME + " TEXT,"
            + LOCAL_ATTENDANCE_LOCATION_ID + " TEXT,"
            + LOCAL_ATTENDANCE_SEGMENT + " TEXT,"
            + LOCAL_ATTENDANCE_SEGMENT_ID + " TEXT,"
            + LOCAL_ATTENDANCE_GODOWN + " TEXT,"
            + LOCAL_ATTENDANCE_GODOWN_ID + " TEXT,"
            + LOCAL_ATTENDANCE_DATE_TIME + " TEXT,"
            + LOCAL_ATTENDANCE_LOCAL_ADDRESS + " TEXT,"
            + LOCAL_ATTENDANCE_GODOWN_PIC + " BLOB,"
            + LOCAL_ATTENDANCE_REMARKS + " TEXT);";

    private static final String CREATE_TABLE_WAREHOUSE_INSPECTION_DYNAMIC_TABLE = "CREATE TABLE "
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_ID + " INTEGER,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_SEQUENCE_NO + " INTEGER ,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_COL + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_BOOLEAN + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_DATE + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES_MANDATORY_VALIDATION + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MULTI_SELECT_VALIDATION + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NUMBER_VALIDATION + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_TEXT_VALIDATION + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER_ID + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MASTER_ITEM_TYPE_ID + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_SEQUENCE_NO + " TEXT);";

    private static final String CREATE_TABLE_WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE = "CREATE TABLE "
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_VISIBLE + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_VISIBLE + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_ID + " INTEGER,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_SEQUENCE_NO + " INTEGER,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_ID + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_BOOLEAN + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_DATE + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_VALUES + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_VALUES_MANDATORY_VALIDATION + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_MULTI_SELECT_VALIDATION + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NUMBER_VALIDATION + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_TEXT_VALIDATION + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_GROUP_HEADER + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_GROUP_HEADER_ID + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_MASTER_ITEM_TYPE_ID + " TEXT,"
            + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_SEQUENCE_NO + " TEXT);";

    private static final String CREATE_TABLE_LOCAL_WAREHOUSE_INSPECTION_TABLE = "CREATE TABLE "
            + LOCAL_WAREHOUSE_INSPECTION_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_WAREHOUSE_INSPECTION_TABLE_PARTICULAR + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_TABLE_PARTICULAR_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_TABLE_NAME + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_TABLE_NAME_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_TABLE_VALUES + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_TABLE_ROW_FLAG + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_TABLE_TYPE_FLAG + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_TABLE_REMARK + " TEXT);";

    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE "
            + LOGIN_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOGIN_MOBILE_NO + " TEXT,"
            + LOGIN_IMEI_NO + " TEXT,"
            + LOGIN_EMPLOYEE_ID + " TEXT,"
            + LOGIN_EMP_NAME + " TEXT,"
            + LOGIN_EMP_NUM + " TEXT,"
            + LOGIN_DESIGNATION_NAME + " TEXT,"
            + LOGIN_DESIGNATION_ID + " TEXT,"
            + LOGIN_PASSWORD + " TEXT);";

    private static final String CREATE_TABLE_LOCAL_GEO_TAG_MAPPING = "CREATE TABLE "
            + LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN_GEO_TAG_ID + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_SEGMENT + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_SEGMENT_ID + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_EMP_ID + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_STATE + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_STATE_ID + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_LOCATION + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_LOCATION_ID + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN_ID + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_ADDRESS + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_CURRENT_DATE + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_LATTITUTE + " TEXT,"
            + LOCAL_GODOWN_GEO_TAG_TABLE_LONGITUTE + " TEXT);";

    private static final String CREATE_TABLE_LOCAL_GODOWN_AUDIT = "CREATE TABLE "
            + LOCAL_GODOWN_AUDIT_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_GODOWN_AUDIT_TABLE_AUDIT_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_DATE + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_CREATED_BY_DATE + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_SEGMENT + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_AUDIT_TYPE + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_AUDITOR + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_STATE + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_LOCATION + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_GODOWN + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_ENTRY_TYPE + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_ADDRESS + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_SEGMENT_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_AUDIT_TYPE_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_AUDITOR_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_STATE_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_LOCATION_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_GODOWN_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_TABLE_GODOWN_PIC + " BLOB,"
            + LOCAL_GODOWN_AUDIT_TABLE_FORM_NO + " TEXT);";

    private static final String CREATE_TABLE_LOCAL_GODOWN_AUDIT_POINT_DETAILS = "CREATE TABLE "
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_AUDIT_POINT + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_RESPONSE + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_AUDIT_POINT_ID + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_ONE_ROW_CHECKER + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_REMARK + " TEXT);";

    private static final String CREATE_TABLE_LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE = "CREATE TABLE "
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_FINANCIAL_REMARK + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_QUALITY_REMARK + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_ONE_ROW_CHECKER + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_OVER_RULED_REMARK + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_NO_OF_STACKS + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_REASON_NOT_STACK + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_NO_OF_SAMPLES + " TEXT,"
            + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_REASON_NOT_SAMPLES + " TEXT);";

    private static final String CREATE_TABLE_LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE = "CREATE TABLE "
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DATE + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATE_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_LOCATION_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_PINCODE + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DISTRICT_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_GODOWN_ADDRESS + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NAME_OF_GODOWN_OWNER + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CC_LOCATION_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BANK_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BRANCH_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_SURVEY_DONE_BY_NAME + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DESIGNATION_ID + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CONTACT_NO + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_RECORDCOUNT_INSPECTION_DETAILS + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + " TEXT,"
            + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NO_OF_GODOWNS + " TEXT);";

    //For Creating Deposit Header Table
    private static final String CREATE_TABLE_DEPOSIT = "CREATE TABLE "
            + DEPOSIT_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_DEPOSIT_TABLE_DEPOSIT_ID + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_CLIENT_COPYNO + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_EXCHANGE_TYPEID + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_EXCHANGE_TYPE_NAME + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_STATE_ID + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_IS_SCM_DEPOSIT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_TRANSACTION_DATE + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_LOCATION_ID + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_SM_PREFIX + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_CMPID + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_CMP_NAME + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_TRUCKNO + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_COMMODITY_ID + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_COMMODITY_NAME + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_PACKING_KG + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_TYPE + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_WEIGHT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_SUPPLIER_NAME + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_NCML_GATE_PASSNO + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_LR_GR_NO + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_NO_OF_BAGS + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_EMPTY_GUNNIES_WEIGHT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_NAME + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_SLIP_NO + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEIN + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEOUT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_TOTAL_WEIGHT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_TARE_WEIGHT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_GROSS_WEIGHT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_NET_WEIGHT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_AVERAGE_WEIGHT + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_NO_CUT_AND_TOM_BAGS + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_NO_PALLA_BAGS + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_MOISTURE + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_FOREIGN_MATTER + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_FUNGUS_DMG_DISCOLORED + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_BROKEN_IMMATURE + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_WEEVILLED + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_LIVE_INSECTS + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_REMARKS + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_TRUCK_FRONT_IMAGE_NAME + " BLOB,"
            + LOCAL_DEPOSIT_TABLE_TRUCK_REAR_IMAGE_NAME + " BLOB,"
            + LOCAL_DEPOSIT_TABLE_DEPOSITOR_IMAGE_NAME + " BLOB,"
            + LOCAL_DEPOSIT_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME + " BLOB,"
            + LOCAL_DEPOSIT_TABLE_SAMPLE_TAKEN_IMAGE_NAME + " BLOB,"
            + LOCAL_DEPOSIT_TABLE_IS_POSTED_ON_SERVER + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_CREATED_BY + " TEXT,"
            + LOCAL_DEPOSIT_TABLE_CREATED_DATE + " TEXT);";


    //Withdrawal
    private static final String CREATE_TABLE_WITHDRAWAL = "CREATE TABLE "
            + WITHDRAWAL_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_WITHDRAWAL_TABLE_WITHDRAWAL_ID + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_CLIENT_COPYNO + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_EXCHANGE_TYPEID + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_EXCHANGE_TYPE_NAME + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_STATE_ID + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_IS_SCM_WITHDRAWAL + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_IS_REJECTED_WITHDRAWAL + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_TRANSACTION_DATE + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_LOCATION_ID + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_SM_PREFIX + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_CMPID + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_CMP_NAME + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_TRUCKNO + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_COMMODITY_ID + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_COMMODITY_NAME + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_PACKING_KG + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_PACKING_MATERIAL_TYPE + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_PACKING_MATERIAL_WEIGHT + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_SUPPLIER_NAME + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_NCML_GATE_PASSNO + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_LR_GR_NO + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_NO_OF_BAGS + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_EMPTY_GUNNIES_WEIGHT + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_NAME + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_SLIP_NO + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_TIMEIN + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_TIMEOUT + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_TOTAL_WEIGHT + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_TARE_WEIGHT + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_GROSS_WEIGHT + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_NET_WEIGHT + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_AVERAGE_WEIGHT + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_NO_CUT_AND_TOM_BAGS + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_NO_PALLA_BAGS + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_MOISTURE + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_FOREIGN_MATTER + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_FUNGUS_DMG_DISCOLORED + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_BROKEN_IMMATURE + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_WEEVILLED + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_LIVE_INSECTS + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_REMARKS + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_TRUCK_FRONT_IMAGE_NAME + " BLOB,"
            + LOCAL_WITHDRAWAL_TABLE_TRUCK_REAR_IMAGE_NAME + " BLOB,"
            + LOCAL_WITHDRAWAL_TABLE_WITHDRAWALOR_IMAGE_NAME + " BLOB,"
            + LOCAL_WITHDRAWAL_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME + " BLOB,"
            + LOCAL_WITHDRAWAL_TABLE_SAMPLE_TAKEN_IMAGE_NAME + " BLOB,"
            + LOCAL_WITHDRAWAL_TABLE_IS_POSTED_ON_SERVER + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_CREATED_BY + " TEXT,"
            + LOCAL_WITHDRAWAL_TABLE_CREATED_DATE + " TEXT);";

    //creation of deposit details table
    private static final String CREATE_TABLE_DEPOSIT_DETAILS = "CREATE TABLE "
            + DEPOSIT_TABLE_DETAILS + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_ID + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_QTY + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_ID + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_NAME + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_ID + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_NAME + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_IS_POSTED_ON_SERVER + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_BY + " TEXT,"
            + LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_DATE + " TEXT);";

    //Start for Withdrawal Details Table

    //creation of deposit details table
    private static final String CREATE_TABLE_WITHDRAWAL_DETAILS = "CREATE TABLE "
            + WITHDRAWAL_TABLE_DETAILS + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_ID + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_WITHDRAWAL_ID + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_GODOWN_ID + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_STACK_NO + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_LOT_NO + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_NO_OF_BAGS + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_QTY + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_IS_POSTED_ON_SERVER + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_CREATED_BY + " TEXT,"
            + LOCAL_WITHDRAWAL_DETAILS_TABLE_CREATED_DATE + " TEXT);";


    //End


    //------Creating a new table of Exchange Type
    private static final String CREATE_TABLE_EXCHANGE_TYPE = "CREATE TABLE "
            + EXCHANGE_TYPE_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_TABLE_EXCHANGE_TYPE_ID + " TEXT,"
            + LOCAL_TABLE_EXCHANGE_TYPE_NAME + " TEXT);";


    //  start
    private static final String CREATE_TABLE_COMMODITY_LOCATION_MAPPING = "CREATE TABLE "
            + COMMODITY_ON_LOCATION_MAPPING + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_NAME + " TEXT,"
            + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_ID + " TEXT,"
            + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME + " TEXT,"
            + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_ID + " TEXT,"
            + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME + " TEXT,"
            + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_ID + " TEXT,"
            + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_ID + " TEXT,"
            + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_NAME + " TEXT);";

    // end

    // Commodity table

    private static final String CREATE_TABLE_COMMODITY_TYPE = "CREATE TABLE "
            + COMMODITY_TYPE_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_TABLE_COMMODITY_TYPE_ID + " TEXT,"
            + LOCAL_TABLE_COMMODITY_TYPE_NAME + " TEXT,"
            + LOCAL_TABLE_COMMODITY_EXCHANGE_TYPE_ID + " TEXT,"
            + LOCAL_TABLE_COMMODITY_EXCHANGE_TYPE_NAME + " TEXT);";

    //SM_CMP table

    private static final String CREATE_TABLE_CMP = "CREATE TABLE "
            + CMP_TABLE + "(" + COMMON_ID + " INTEGER PRIMARY KEY,"
            + LOCAL_TABLE_CMP_ID + " TEXT,"
            + LOCAL_TABLE_CMP_NAME + " TEXT,"
            + LOCAL_TABLE_CMP_LOCATION_ID + " TEXT,"
            + LOCAL_TABLE_CMP_CONCURRENCY + " TEXT);";


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCAL_ATTENDANCE);
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_STATE);
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_SEGMENT);
        db.execSQL(CREATE_TABLE_GODOWN);
        db.execSQL(CREATE_TABLE_AUDIT_TYPE);
        db.execSQL(CREATE_TABLE_AUDITOR);
        db.execSQL(CREATE_TABLE_WAREHOUSE);
        db.execSQL(CREATE_TABLE_AUDIT_POINT);
        db.execSQL(CREATE_TABLE_WAREHOUSE_INSPECTION_DYNAMIC_TABLE);
        db.execSQL(CREATE_TABLE_WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE);
        db.execSQL(CREATE_TABLE_LOCAL_GEO_TAG_MAPPING);
        db.execSQL(CREATE_TABLE_LOCAL_GODOWN_AUDIT);
        db.execSQL(CREATE_TABLE_LOCAL_GODOWN_AUDIT_POINT_DETAILS);
        db.execSQL(CREATE_TABLE_LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE);
        db.execSQL(CREATE_TABLE_LOCAL_WAREHOUSE_INSPECTION_TABLE);
        db.execSQL(CREATE_TABLE_BANK);
        db.execSQL(CREATE_TABLE_BRANCH);
        db.execSQL(CREATE_TABLE_DISTRICT);
        db.execSQL(CREATE_TABLE_LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE);
        db.execSQL(CREATE_TABLE_USER_ROLE);
        db.execSQL(CREATE_TABLE_UPDATED_DATABASE);
        db.execSQL(CREATE_TABLE_WAREHOUSE_INSPECTION_INFORMATION);
        db.execSQL(CREATE_TABLE_DEPOSIT);
        db.execSQL(CREATE_TABLE_EXCHANGE_TYPE);
        db.execSQL(CREATE_TABLE_COMMODITY_TYPE);
        db.execSQL(CREATE_TABLE_CMP);
        db.execSQL(CREATE_TABLE_DEPOSIT_DETAILS);
        db.execSQL(CREATE_TABLE_COMMODITY_LOCATION_MAPPING);
        db.execSQL(CREATE_TABLE_WITHDRAWAL);
        db.execSQL(CREATE_TABLE_WITHDRAWAL_DETAILS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_ATTENDANCE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + STATE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SEGMENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GODOWN_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + AUDIT_TYPE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + AUDITOR_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + WAREHOUSE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + AUDIT_POINT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + WAREHOUSE_INSPECTION_DYNAMIC_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_GODOWN_AUDIT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_WAREHOUSE_INSPECTION_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + BANK_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + BRANCH_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DISTRICT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + USER_ROLE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_UPDATED_DATABASE);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_WAREHOUSE_INSPECTION_INFORMATION);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DEPOSIT);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CMP);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DEPOSIT_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_COMMODITY_LOCATION_MAPPING);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_WITHDRAWAL);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_WITHDRAWAL_DETAILS);
            onCreate(db);
        }
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        db = this.getWritableDatabase();
    }

    public void close() {
        db.close();
    }


    public void insertUserRole(ArrayList<UserRolePojo> list) {

        ContentValues values;
        UserRolePojo pojo;
        open();

        for (int i = 0; i < list.size(); i++) {

            values = new ContentValues();
            pojo = list.get(i);
            values.put(USER_ROLE_TABLE_NAME, pojo.getFormFileName());
            values.put(USER_ROLE_CONDITION, pojo.getCondition());

            db.insert(USER_ROLE_TABLE, null, values);

            pojo = null;
            values = null;
        }

        close();

    }

    public void insertUpdatedUserRole(ArrayList<UserRolePojo> list) {

        ContentValues values;
        UserRolePojo pojo;
        open();

        db.delete(USER_ROLE_TABLE, null, null);

        for (int i = 0; i < list.size(); i++) {

            values = new ContentValues();
            pojo = list.get(i);
            values.put(USER_ROLE_TABLE_NAME, pojo.getFormFileName());
            values.put(USER_ROLE_CONDITION, pojo.getCondition());

            db.insert(USER_ROLE_TABLE, null, values);

            pojo = null;
            values = null;
        }

        close();

    }

    public ArrayList<String> getUserRole() {
        ArrayList<String> list = new ArrayList<String>();
        open();

        Cursor cursor = db.query(USER_ROLE_TABLE, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                list.add(cursor.getString(cursor.getColumnIndex(USER_ROLE_TABLE_NAME)));

            } while (cursor.moveToNext());

        }

        cursor.close();

        close();

        return list;

    }

    public ArrayList<String> getUserRoleCondition() {
        ArrayList<String> list = new ArrayList<String>();
        open();

        Cursor cursor = db.query(USER_ROLE_TABLE, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                list.add(cursor.getString(cursor.getColumnIndex(USER_ROLE_CONDITION)));

            } while (cursor.moveToNext());

        }

        cursor.close();

        close();

        return list;

    }


    public ArrayList<String> getUserInfo(String emp_no) {

        ArrayList<String> list = new ArrayList<String>();
        open();

        Cursor cursor = db.query(LOGIN_TABLE, null, null, null, null, null, null);


        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            list.add(cursor.getString(cursor.getColumnIndex(LOGIN_EMPLOYEE_ID)));
            list.add(cursor.getString(cursor.getColumnIndex(LOGIN_PASSWORD)));
            list.add(cursor.getString(cursor.getColumnIndex(LOGIN_MOBILE_NO)));
            list.add(cursor.getString(cursor.getColumnIndex(LOGIN_IMEI_NO)));
        }

        cursor.close();

        close();

        return list;

    }


    public void putUserLogin(String mobile, String imei, String emp_id, String password, String emp_name, String emp_num, String designation_name, String designation_id) {
        open();
        ContentValues values;
        values = new ContentValues();
        values.put(LOGIN_MOBILE_NO, mobile);
        values.put(LOGIN_IMEI_NO, imei);
        values.put(LOGIN_EMPLOYEE_ID, emp_id);
        values.put(LOGIN_PASSWORD, password);
        values.put(LOGIN_DESIGNATION_NAME, designation_name);
        values.put(LOGIN_DESIGNATION_ID, designation_id);
        values.put(LOGIN_EMP_NAME, emp_name);
        values.put(LOGIN_EMP_NUM, emp_num);


        Cursor cursor = db.query(LOGIN_TABLE, null, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {

            db.update(LOGIN_TABLE, values, LOGIN_EMP_NAME + "=?", new String[]{emp_name});
        } else {
            db.insert(LOGIN_TABLE, null, values);
        }

        cursor.close();
        close();
    }

    public boolean checkUserLogin(String emp_id, String pass) {

        boolean flag;
        open();
        Cursor cursor;
        cursor = db.query(LOGIN_TABLE, null, LOGIN_EMPLOYEE_ID + "=? AND " + LOGIN_PASSWORD + " =?", new String[]{emp_id, pass}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            flag = true;
        } else {
            flag = false;
        }


        close();

        return flag;
    }

    public void insertAuditorTable(ArrayList<AuditorPojo> list) {
        open();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();
            AuditorPojo pojo = list.get(i);

            values.put(AUDITOR_TABLE_EMP_NO, pojo.getEmp_num());
            values.put(AUDITOR_TABLE_EMP_NAME, pojo.getEmp_full_name());

            db.insert(AUDITOR_TABLE, null, values);

            values = null;
            pojo = null;
        }

        close();
    }

    public void insertUpdateAuditorTable(ArrayList<AuditorPojo> list) {
        open();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();
            AuditorPojo pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(AUDITOR_TABLE, null, AUDITOR_TABLE_EMP_NO + "=?", new String[]{pojo.getEmp_num()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(AUDITOR_TABLE, AUDITOR_TABLE_EMP_NO + "=?", new String[]{pojo.getEmp_num()});

                values.put(AUDITOR_TABLE_EMP_NO, pojo.getEmp_num());
                values.put(AUDITOR_TABLE_EMP_NAME, pojo.getEmp_full_name());

                db.insert(AUDITOR_TABLE, null, values);
            } else {
                values.put(AUDITOR_TABLE_EMP_NO, pojo.getEmp_num());
                values.put(AUDITOR_TABLE_EMP_NAME, pojo.getEmp_full_name());

                db.insert(AUDITOR_TABLE, null, values);
            }

            values = null;
            pojo = null;
        }

        close();
    }

    public void insertState(ArrayList<StatePojo> list) {
        open();

        ContentValues values;
        StatePojo pojo;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(STATE_TABLE_STATE_NAME, pojo.getState_name());
            values.put(STATE_TABLE_STATE_ID, pojo.getState_id());
            values.put(STATE_TABLE_CM_AND_CMP_SEGMENT, pojo.getSegment());
            values.put(STATE_TABLE_SEGMENT_ID, pojo.getSegment_id());

            db.insert(STATE_TABLE, null, values);

            pojo = null;
            values = null;
        }

        close();
    }


    public void insertUpdateState(ArrayList<StatePojo> list) {
        open();
        ContentValues values;
        StatePojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(STATE_TABLE, null, STATE_TABLE_STATE_ID + "=?", new String[]{pojo.getState_id()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(STATE_TABLE, STATE_TABLE_STATE_ID + "=?", new String[]{pojo.getState_id()});

                values.put(STATE_TABLE_STATE_NAME, pojo.getState_name());
                values.put(STATE_TABLE_STATE_ID, pojo.getState_id());
                values.put(STATE_TABLE_CM_AND_CMP_SEGMENT, pojo.getSegment());
                values.put(STATE_TABLE_SEGMENT_ID, pojo.getSegment_id());

                db.insert(STATE_TABLE, null, values);
            } else {
                values.put(STATE_TABLE_STATE_NAME, pojo.getState_name());
                values.put(STATE_TABLE_STATE_ID, pojo.getState_id());
                values.put(STATE_TABLE_CM_AND_CMP_SEGMENT, pojo.getSegment());
                values.put(STATE_TABLE_SEGMENT_ID, pojo.getSegment_id());

                db.insert(STATE_TABLE, null, values);
            }

            pojo = null;
            values = null;

        }
        close();
    }

    public void insertGodownTable(ArrayList<GodownCmAndCmpPojo> list, String cmAndCmp) {
        open();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();
            GodownCmAndCmpPojo pojo = list.get(i);

            values.put(GODOWN_TABLE_GODOWN_ID, pojo.getGodown_id());
            values.put(GODOWN_TABLE_GODOWN_ADDRESS, pojo.getGodown_Address());
            values.put(GODOWN_TABLE_GODOWN_NAME, pojo.getGodown_name());
            values.put(GODOWN_TABLE_CM_AND_CMP_TABLE, cmAndCmp);
            values.put(GODOWN_TABLE_LOCATION_ID, pojo.getLocation_id());
            values.put(GODOWN_TABLE_ACTIVE, pojo.getIs_godown_map());
            values.put(GODOWN_TABLE_WAREHOUSE_ID, pojo.getWarehouseId());
            values.put(GODOWN_TABLE_WAREHOUSE_NAME, pojo.getWarehouseName());
            values.put(GODOWN_TABLE_WAREHOUSE_CLOSEDATE, pojo.getWarehouseCloseDate());
            values.put(GODOWN_TABLE_GODOWN_CLOSEDATE, pojo.getGodownCloseDAte());

            db.insert(GODOWN_TABLE, null, values);

            values = null;
            pojo = null;

        }

        close();

    }

    public void insertUpdateGodownTable(ArrayList<GodownCmAndCmpPojo> list, String cmAndCmp) {
        open();

        for (int i = 0; i < list.size(); i++) {
            ContentValues values = new ContentValues();
            GodownCmAndCmpPojo pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_GODOWN_ID + "=?", new String[]{pojo.getGodown_id()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(GODOWN_TABLE, GODOWN_TABLE_GODOWN_ID + "=?", new String[]{pojo.getGodown_id()});

                values.put(GODOWN_TABLE_GODOWN_ID, pojo.getGodown_id());
                values.put(GODOWN_TABLE_GODOWN_ADDRESS, pojo.getGodown_Address());
                values.put(GODOWN_TABLE_GODOWN_NAME, pojo.getGodown_name());
                values.put(GODOWN_TABLE_CM_AND_CMP_TABLE, cmAndCmp);
                values.put(GODOWN_TABLE_LOCATION_ID, pojo.getLocation_id());
                values.put(GODOWN_TABLE_ACTIVE, pojo.getIs_godown_map());
                values.put(GODOWN_TABLE_WAREHOUSE_ID, pojo.getWarehouseId());
                values.put(GODOWN_TABLE_WAREHOUSE_NAME, pojo.getWarehouseName());
                values.put(GODOWN_TABLE_WAREHOUSE_CLOSEDATE, pojo.getWarehouseCloseDate());
                values.put(GODOWN_TABLE_GODOWN_CLOSEDATE, pojo.getGodownCloseDAte());

                db.insert(GODOWN_TABLE, null, values);
            } else {
                values.put(GODOWN_TABLE_GODOWN_ID, pojo.getGodown_id());
                values.put(GODOWN_TABLE_GODOWN_ADDRESS, pojo.getGodown_Address());
                values.put(GODOWN_TABLE_GODOWN_NAME, pojo.getGodown_name());
                values.put(GODOWN_TABLE_CM_AND_CMP_TABLE, cmAndCmp);
                values.put(GODOWN_TABLE_LOCATION_ID, pojo.getLocation_id());
                values.put(GODOWN_TABLE_ACTIVE, pojo.getIs_godown_map());
                values.put(GODOWN_TABLE_WAREHOUSE_ID, pojo.getWarehouseId());
                values.put(GODOWN_TABLE_WAREHOUSE_NAME, pojo.getWarehouseName());
                values.put(GODOWN_TABLE_WAREHOUSE_CLOSEDATE, pojo.getWarehouseCloseDate());
                values.put(GODOWN_TABLE_GODOWN_CLOSEDATE, pojo.getGodownCloseDAte());

                db.insert(GODOWN_TABLE, null, values);
            }

            values = null;
            pojo = null;

        }

        close();
    }

    public void insertLocationTable(ArrayList<LocationCmAndCmpPojo> list, String cmAndCmp) {

        open();
        LocationCmAndCmpPojo pojo;
        ContentValues values;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(LOCATION_TABLE_LOCATION_ID, pojo.getLocation_id());
            values.put(LOCATION_TABLE_LOCATION_NAME, pojo.getLocation_name());
            values.put(LOCATION_TABLE_STATE, pojo.getState());
            values.put(LOCATION_TABLE_STATE_ID, pojo.getState_id());
            values.put(LOCATION_TABLE_CM_AND_CMP_TABLE, cmAndCmp);
            values.put(LOCATION_TABLE_DISTRICT_ID, pojo.getDistrict_Id());

            db.insert(LOCATION_TABLE, null, values);

            values = null;
            pojo = null;
        }

        close();
    }

    public void insertUpdateLocationTable(ArrayList<LocationCmAndCmpPojo> list, String cmAndCmp) {
        open();
        LocationCmAndCmpPojo pojo;
        ContentValues values;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_ID + "=?", new String[]{pojo.getLocation_id()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(LOCATION_TABLE, LOCATION_TABLE_LOCATION_ID + "=?", new String[]{pojo.getLocation_id()});

                values.put(LOCATION_TABLE_LOCATION_ID, pojo.getLocation_id());
                values.put(LOCATION_TABLE_LOCATION_NAME, pojo.getLocation_name());
                values.put(LOCATION_TABLE_STATE, pojo.getState());
                values.put(LOCATION_TABLE_STATE_ID, pojo.getState_id());
                values.put(LOCATION_TABLE_CM_AND_CMP_TABLE, cmAndCmp);
                values.put(LOCATION_TABLE_DISTRICT_ID, pojo.getDistrict_Id());

                db.insert(LOCATION_TABLE, null, values);
            } else {
                values.put(LOCATION_TABLE_LOCATION_ID, pojo.getLocation_id());
                values.put(LOCATION_TABLE_LOCATION_NAME, pojo.getLocation_name());
                values.put(LOCATION_TABLE_STATE, pojo.getState());
                values.put(LOCATION_TABLE_STATE_ID, pojo.getState_id());
                values.put(LOCATION_TABLE_CM_AND_CMP_TABLE, cmAndCmp);
                values.put(LOCATION_TABLE_DISTRICT_ID, pojo.getDistrict_Id());

                db.insert(LOCATION_TABLE, null, values);
            }

            values = null;
            pojo = null;
        }

        close();
    }

    public void isertSegmentTable(ArrayList<SegmentPojo> list) {
        open();
        ContentValues values;
        SegmentPojo pojo;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(SEGMENT_TABLE_SEGMENT_NAME, pojo.getSegment_name().toUpperCase());
            values.put(SEGMENT_TABLE_SEGMENT_ID, pojo.getSegment_id());

            db.insert(SEGMENT_TABLE, null, values);

            pojo = null;
            values = null;
        }
        close();
    }

    public void isertUpdateSegmentTable(ArrayList<SegmentPojo> list) {
        open();
        ContentValues values;
        SegmentPojo pojo;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(SEGMENT_TABLE, null, SEGMENT_TABLE_SEGMENT_ID + "=?", new String[]{pojo.getSegment_id()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(SEGMENT_TABLE, SEGMENT_TABLE_SEGMENT_ID + "=?", new String[]{pojo.getSegment_id()});

                values.put(SEGMENT_TABLE_SEGMENT_NAME, pojo.getSegment_name().toUpperCase());
                values.put(SEGMENT_TABLE_SEGMENT_ID, pojo.getSegment_id());

                db.insert(SEGMENT_TABLE, null, values);
            } else {
                values.put(SEGMENT_TABLE_SEGMENT_NAME, pojo.getSegment_name().toUpperCase());
                values.put(SEGMENT_TABLE_SEGMENT_ID, pojo.getSegment_id());

                db.insert(SEGMENT_TABLE, null, values);
            }

            pojo = null;
            values = null;
        }
        close();
    }

    public void insertAuditTypeTable(ArrayList<AuditTypePojo> list) {
        open();
        ContentValues values;
        AuditTypePojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(AUDIT_TYPE_TABLE_AUDIT_TYPE, pojo.getAudit_type());
            values.put(AUDIT_TYPE_TABLE_AUDIT_TYPE_ID, pojo.getAudit_type_id());
            values.put(AUDIT_TYPE_TABLE_SEGMENT_ID, pojo.getSegment_id());

            db.insert(AUDIT_TYPE_TABLE, null, values);

            pojo = null;
            values = null;
        }

        close();

    }

    public void insertUpdateAuditTypeTable(ArrayList<AuditTypePojo> list) {
        open();
        ContentValues values;
        AuditTypePojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(AUDIT_TYPE_TABLE, null, AUDIT_TYPE_TABLE_AUDIT_TYPE_ID + "=? AND " + AUDIT_TYPE_TABLE_SEGMENT_ID + "= ? ", new String[]{pojo.getAudit_type_id(), pojo.getSegment_id()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(AUDIT_TYPE_TABLE, AUDIT_TYPE_TABLE_AUDIT_TYPE_ID + "=? AND " + AUDIT_TYPE_TABLE_SEGMENT_ID + "= ? ", new String[]{pojo.getAudit_type_id(), pojo.getSegment_id()});

                values.put(AUDIT_TYPE_TABLE_AUDIT_TYPE, pojo.getAudit_type());
                values.put(AUDIT_TYPE_TABLE_AUDIT_TYPE_ID, pojo.getAudit_type_id());
                values.put(AUDIT_TYPE_TABLE_SEGMENT_ID, pojo.getSegment_id());

                db.insert(AUDIT_TYPE_TABLE, null, values);
            } else {
                values.put(AUDIT_TYPE_TABLE_AUDIT_TYPE, pojo.getAudit_type());
                values.put(AUDIT_TYPE_TABLE_AUDIT_TYPE_ID, pojo.getAudit_type_id());
                values.put(AUDIT_TYPE_TABLE_SEGMENT_ID, pojo.getSegment_id());

                db.insert(AUDIT_TYPE_TABLE, null, values);
            }


            pojo = null;
            values = null;
        }

        close();

    }

    public void insertwarehouseTable(ArrayList<WareHousePojo> list, String cmAndCmp) {
        open();
        ContentValues values;
        WareHousePojo pojo;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(WAREHOUSE_TABLE_WAREHOUSE_ID, pojo.getWarehouse_id());
            values.put(WAREHOUSE_TABLE_WAREHOUSE_ADDRESS, pojo.getWarehouse_address());
            values.put(WAREHOUSE_TABLE_WAREHOUSE_NAME, pojo.getWarehouse_name());
            values.put(WAREHOUSE_TABLE_LOCATION, pojo.getLocation());
            values.put(WAREHOUSE_TABLE_LOCATION_ID, pojo.getLocation_id());
            values.put(WAREHOUSE_TABLE_CM_AND_CMP, cmAndCmp);


            db.insert(WAREHOUSE_TABLE, null, values);

            pojo = null;
            values = null;
        }

        close();
    }


    public void insertUpdatewarehouseTable(ArrayList<WareHousePojo> list, String cmAndCmp) {
        open();
        ContentValues values;
        WareHousePojo pojo;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(WAREHOUSE_TABLE, null, WAREHOUSE_TABLE_WAREHOUSE_ID + "=?", new String[]{pojo.getWarehouse_id()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(WAREHOUSE_TABLE, WAREHOUSE_TABLE_WAREHOUSE_ID + "=?", new String[]{pojo.getWarehouse_id()});

                values.put(WAREHOUSE_TABLE_WAREHOUSE_ID, pojo.getWarehouse_id());
                values.put(WAREHOUSE_TABLE_WAREHOUSE_ADDRESS, pojo.getWarehouse_address());
                values.put(WAREHOUSE_TABLE_WAREHOUSE_NAME, pojo.getWarehouse_name());
                values.put(WAREHOUSE_TABLE_LOCATION, pojo.getLocation());
                values.put(WAREHOUSE_TABLE_LOCATION_ID, pojo.getLocation_id());
                values.put(WAREHOUSE_TABLE_CM_AND_CMP, cmAndCmp);

                db.insert(WAREHOUSE_TABLE, null, values);
            } else {
                values.put(WAREHOUSE_TABLE_WAREHOUSE_ID, pojo.getWarehouse_id());
                values.put(WAREHOUSE_TABLE_WAREHOUSE_ADDRESS, pojo.getWarehouse_address());
                values.put(WAREHOUSE_TABLE_WAREHOUSE_NAME, pojo.getWarehouse_name());
                values.put(WAREHOUSE_TABLE_LOCATION, pojo.getLocation());
                values.put(WAREHOUSE_TABLE_LOCATION_ID, pojo.getLocation_id());
                values.put(WAREHOUSE_TABLE_CM_AND_CMP, cmAndCmp);

                db.insert(WAREHOUSE_TABLE, null, values);
            }
            pojo = null;
            values = null;
        }

        close();
    }

    public void insertWarehouseHeaderTable(ArrayList<WareHouseInspectionHeaderPojo> list) {
        open();
        ContentValues values;
        WareHouseInspectionHeaderPojo pojo;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID, pojo.getInspectionID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DATE, pojo.getSurveyDate());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION, pojo.getInspectionNo());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATE_ID, pojo.getStateID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_LOCATION_ID, pojo.getLocationID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_PINCODE, pojo.getLocationPinCode());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DISTRICT_ID, pojo.getDistrictID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_GODOWN_ADDRESS, pojo.getGodownAddress());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NAME_OF_GODOWN_OWNER, pojo.getGodownOwner());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CC_LOCATION_ID, pojo.getCCLocEmpID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BANK_ID, pojo.getBankID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BRANCH_ID, pojo.getBranchID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_SURVEY_DONE_BY_NAME, pojo.getSurveyDoneEmpID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DESIGNATION_ID, pojo.getDesigID());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CONTACT_NO, pojo.getBorrowerContact());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER, pojo.getCreatedDate());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_RECORDCOUNT_INSPECTION_DETAILS, pojo.getRecordCount_InspectionDetails());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG, pojo.getStatusFlag());
            values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NO_OF_GODOWNS, pojo.getNoOfGodowns());
            db.insert(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, values);

            pojo = null;
            values = null;
        }

        close();
    }

    public void insertUpdateWarehouseHeaderTable(ArrayList<WareHouseInspectionHeaderPojo> list) {
        open();
        ContentValues values;
        WareHouseInspectionHeaderPojo pojo;
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID + "=?", new String[]{pojo.getInspectionID()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID + "=?", new String[]{pojo.getInspectionID()});

                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID, pojo.getInspectionID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DATE, pojo.getSurveyDate());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION, pojo.getInspectionNo());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATE_ID, pojo.getStateID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_LOCATION_ID, pojo.getLocationID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_PINCODE, pojo.getLocationPinCode());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DISTRICT_ID, pojo.getDistrictID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_GODOWN_ADDRESS, pojo.getGodownAddress());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NAME_OF_GODOWN_OWNER, pojo.getGodownOwner());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CC_LOCATION_ID, pojo.getCCLocEmpID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BANK_ID, pojo.getBankID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BRANCH_ID, pojo.getBranchID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_SURVEY_DONE_BY_NAME, pojo.getSurveyDoneEmpID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DESIGNATION_ID, pojo.getDesigID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CONTACT_NO, pojo.getBorrowerContact());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER, pojo.getCreatedDate());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_RECORDCOUNT_INSPECTION_DETAILS, pojo.getRecordCount_InspectionDetails());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG, pojo.getStatusFlag());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NO_OF_GODOWNS, pojo.getNoOfGodowns());

                db.insert(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, values);
            } else {
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID, pojo.getInspectionID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DATE, pojo.getSurveyDate());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION, pojo.getInspectionNo());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATE_ID, pojo.getStateID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_LOCATION_ID, pojo.getLocationID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_PINCODE, pojo.getLocationPinCode());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DISTRICT_ID, pojo.getDistrictID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_GODOWN_ADDRESS, pojo.getGodownAddress());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NAME_OF_GODOWN_OWNER, pojo.getGodownOwner());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CC_LOCATION_ID, pojo.getCCLocEmpID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BANK_ID, pojo.getBankID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BRANCH_ID, pojo.getBranchID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_SURVEY_DONE_BY_NAME, pojo.getSurveyDoneEmpID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DESIGNATION_ID, pojo.getDesigID());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CONTACT_NO, pojo.getBorrowerContact());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER, pojo.getCreatedDate());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_RECORDCOUNT_INSPECTION_DETAILS, pojo.getRecordCount_InspectionDetails());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG, pojo.getStatusFlag());
                values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NO_OF_GODOWNS, pojo.getNoOfGodowns());

                db.insert(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, values);
            }

            pojo = null;
            values = null;
        }
        close();
    }

    public ArrayList<String> getWarehouseInspectionHeaderDetail(String inspection_no) {
        ArrayList<String> list = new ArrayList<String>();
        open();

        Cursor cursor = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID + "=?", new String[]{inspection_no}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            list.add(cursor.getString(cursor.getColumnIndex(COMMON_ID)));
            list.add(cursor.getString(cursor.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID)));
            list.add(cursor.getString(cursor.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_RECORDCOUNT_INSPECTION_DETAILS)));
            cursor.close();
        }

        close();
        return list;
    }

    public void insertAuditPointDetails(ArrayList<AuditPointDetailsServerPojo> list) {
        open();
        ContentValues values;
        AuditPointDetailsServerPojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(AUDIT_POINT_TABLE_AUDIT_POINT_ID, pojo.getAudit_point_id());
            values.put(AUDIT_POINT_TABLE_AUDIT_POINT, pojo.getAudit_point_name());
            values.put(AUDIT_POINT_TABLE_AUDIT_TYPE_ID, pojo.getAudit_type_id());
            values.put(AUDIT_POINT_TABLE_SEGMENT_ID, pojo.getSegment_id());
            values.put(AUDIT_POINT_TABLE_SEGMENT_NAME, pojo.getSegment_name());
            values.put(AUDIT_POINT_TABLE_AUDIT_GAP_FLAG, pojo.getGap_flag());
            values.put(AUDIT_POINT_TABLE_AUDIT_SERIAL_NO, pojo.getSerial_no());

            db.insert(AUDIT_POINT_TABLE, null, values);

            pojo = null;
            values = null;
        }

        close();
    }


    public void insertUpdateAuditPointDetails(ArrayList<AuditPointDetailsServerPojo> list) {
        open();
        ContentValues values;
        AuditPointDetailsServerPojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(AUDIT_POINT_TABLE, null, AUDIT_POINT_TABLE_AUDIT_POINT_ID + "=?", new String[]{pojo.getAudit_point_id()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(AUDIT_POINT_TABLE, AUDIT_POINT_TABLE_AUDIT_POINT_ID + "=?", new String[]{pojo.getAudit_point_id()});

                values.put(AUDIT_POINT_TABLE_AUDIT_POINT_ID, pojo.getAudit_point_id());
                values.put(AUDIT_POINT_TABLE_AUDIT_POINT, pojo.getAudit_point_name());
                values.put(AUDIT_POINT_TABLE_AUDIT_TYPE_ID, pojo.getAudit_type_id());
                values.put(AUDIT_POINT_TABLE_SEGMENT_ID, pojo.getSegment_id());
                values.put(AUDIT_POINT_TABLE_SEGMENT_NAME, pojo.getSegment_name());
                values.put(AUDIT_POINT_TABLE_AUDIT_GAP_FLAG, pojo.getGap_flag());
                values.put(AUDIT_POINT_TABLE_AUDIT_SERIAL_NO, pojo.getSerial_no());

                db.insert(AUDIT_POINT_TABLE, null, values);
            } else {
                values.put(AUDIT_POINT_TABLE_AUDIT_POINT_ID, pojo.getAudit_point_id());
                values.put(AUDIT_POINT_TABLE_AUDIT_POINT, pojo.getAudit_point_name());
                values.put(AUDIT_POINT_TABLE_AUDIT_TYPE_ID, pojo.getAudit_type_id());
                values.put(AUDIT_POINT_TABLE_SEGMENT_ID, pojo.getSegment_id());
                values.put(AUDIT_POINT_TABLE_SEGMENT_NAME, pojo.getSegment_name());
                values.put(AUDIT_POINT_TABLE_AUDIT_GAP_FLAG, pojo.getGap_flag());
                values.put(AUDIT_POINT_TABLE_AUDIT_SERIAL_NO, pojo.getSerial_no());

                db.insert(AUDIT_POINT_TABLE, null, values);
            }

            pojo = null;
            values = null;
        }

        close();
    }


    public String getStateNameUsingId(String id) {
        String name = "";
        open();

        Cursor cursor = db.query(STATE_TABLE, null, STATE_TABLE_STATE_ID + "=?", new String[]{id}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(STATE_TABLE_STATE_NAME));
            cursor.close();
        }

        close();

        return name;
    }

    public ArrayList<String> getLocationArray(String location_name, String cmOrCmp) {
        ArrayList<String> list = new ArrayList<String>();
        open();
        Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=? AND " + LOCATION_TABLE_CM_AND_CMP_TABLE + "=?", new String[]{location_name, cmOrCmp.trim()}, LOCATION_TABLE_LOCATION_NAME, null, LOCATION_TABLE_LOCATION_NAME + " ASC");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            list.add(cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_NAME)));
            cursor.close();
        } else {
            cursor = null;
            cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_CM_AND_CMP_TABLE + "=?", new String[]{cmOrCmp.trim()}, LOCATION_TABLE_LOCATION_NAME, null, LOCATION_TABLE_LOCATION_NAME + " ASC");

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    list.add(cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_NAME)));
                } while (cursor.moveToNext());
                cursor.close();
            }

        }
        close();
        return list;
    }

    public ArrayList<String> getLocationArrayMapping(String location_name, String cmOrCmp, String state_name) {
        ArrayList<String> list = new ArrayList<String>();
        open();
        String state_id = "";

        Cursor cursor_state_id = db.query(STATE_TABLE, null, STATE_TABLE_STATE_NAME + "=? AND " + STATE_TABLE_CM_AND_CMP_SEGMENT + "= ? ", new String[]{state_name, cmOrCmp}, null, null, null);
        if (cursor_state_id != null && cursor_state_id.getCount() > 0) {
            cursor_state_id.moveToFirst();
            state_id = cursor_state_id.getString(cursor_state_id.getColumnIndex(STATE_TABLE_STATE_ID));
            cursor_state_id.close();
        }

        if (!cmOrCmp.equals("CM") && !cmOrCmp.equals("CMP")) {
            cmOrCmp = "HR";
        }

        Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_CM_AND_CMP_TABLE + "=? AND " + LOCATION_TABLE_STATE_ID + "=?", new String[]{cmOrCmp.trim(), state_id}, LOCATION_TABLE_LOCATION_NAME, null, LOCATION_TABLE_LOCATION_NAME + " ASC");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_NAME)));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return list;
    }


    public ArrayList<String> getCommodity_ExchangeType(String exch_type_name, String location_name) {
        ArrayList<String> list = new ArrayList<String>();
        open();

        //Cursor cursor = db.query(COMMODITY_ON_LOCATION_MAPPING, null, LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME + "=?  AND "  + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME + "= ?  ", new String[]{exch_type_name, location_name}, null, null, LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_NAME + " ASC");
        final String MY_QUERY = "SELECT  DISTINCT " + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_NAME +
                " FROM " + COMMODITY_ON_LOCATION_MAPPING +
                " WHERE " + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME + " = '" + exch_type_name + "'" +
                " AND " + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME + " = '" + location_name + "'" +
                " Order by " + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_NAME;
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                list.add(cursor.getString(cursor.getColumnIndex(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_NAME)));

            } while (cursor.moveToNext());

            cursor.close();
        }

        /*String exch_type_id = "";
        Cursor cursor_exh_type_id = db.query(EXCHANGE_TYPE_TABLE, null, LOCAL_TABLE_EXCHANGE_TYPE_NAME + "=? ", new String[]{exch_type_name}, null, null, null);
        if (cursor_exh_type_id != null && cursor_exh_type_id.getCount() > 0)
        {
            cursor_exh_type_id.moveToFirst();
            exch_type_id = cursor_exh_type_id.getString(cursor_exh_type_id.getColumnIndex(LOCAL_TABLE_EXCHANGE_TYPE_ID));
            cursor_exh_type_id.close();
        }

        Cursor cursor = db.query(COMMODITY_TYPE_TABLE, null, LOCAL_TABLE_COMMODITY_EXCHANGE_TYPE_ID + "=?", new String[]{exch_type_id}, LOCAL_TABLE_COMMODITY_TYPE_NAME, null, LOCAL_TABLE_COMMODITY_TYPE_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                list.add(cursor.getString(cursor.getColumnIndex(LOCAL_TABLE_COMMODITY_TYPE_NAME)));

            } while (cursor.moveToNext());

            cursor.close();
        }*/
        close();
        return list;
    }


    public ArrayList<String> getCommodity_Location(String exch_type_name, String state_name) {
        ArrayList<String> list = new ArrayList<String>();
        open();

        final String MY_QUERY = "SELECT  DISTINCT " + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME +
                " FROM " + COMMODITY_ON_LOCATION_MAPPING +
                " WHERE " + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME + " = '" + exch_type_name + "'" +
                " AND " + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_NAME + " = '" + state_name + "'" +
                " Order by " + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME;
        Cursor cursor = db.rawQuery(MY_QUERY, null);


        // Cursor cursor = db.query(COMMODITY_ON_LOCATION_MAPPING, null, LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME + "=?" + " AND "  + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_NAME + "= ?", new String[]{exch_type_name, state_name}, null, null, LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME)));
            } while (cursor.moveToNext());

            cursor.close();
        }
        close();
        return list;


        //Cursor cursor = db.query(COMMODITY_ON_LOCATION_MAPPING, null, LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME + "=? ", new String[]{exch_type_name}, null, null, null);

        //Cursor cursor = db.query(COMMODITY_ON_LOCATION_MAPPING, null,LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_ID + "= ?  ", new String[]{state_id}, null, null, null);
        //Cursor cursor = db.query(COMMODITY_ON_LOCATION_MAPPING, null, LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME + "=?" + " AND "  + LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_NAME + "= ?", new String[]{exch_type_name, state_name}, null, null, null);
        /*Cursor cursor = db.query(COMMODITY_ON_LOCATION_MAPPING, null, LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_ID + "=?", new String[]{"17"}, null, null, null);
        //Cursor cursor = db.query(COMMODITY_ON_LOCATION_MAPPING, null, null, new String[]{}, null, null, null);


        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                //list.add(cursor.getString(cursor.getColumnIndex(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_NAME)));
                list.add(cursor.getString(cursor.getColumnIndex(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME)));

            } while (cursor.moveToNext());

            cursor.close();
        }*/

    }


    public ArrayList<String> getLocationArrayMappingGeoTag(String location_name, String cmOrCmp, String state_name) {
        ArrayList<String> list = new ArrayList<String>();
        open();
        String state_id = "";
        Cursor cursor_state_id = db.query(STATE_TABLE, null, STATE_TABLE_STATE_NAME + "=? AND " + STATE_TABLE_CM_AND_CMP_SEGMENT + "= ? ", new String[]{state_name, cmOrCmp}, null, null, null);

        if (cursor_state_id != null && cursor_state_id.getCount() > 0) {
            cursor_state_id.moveToFirst();
            state_id = cursor_state_id.getString(cursor_state_id.getColumnIndex(STATE_TABLE_STATE_ID));
            cursor_state_id.close();
        }

        //Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=? AND " + LOCATION_TABLE_CM_AND_CMP_TABLE + "=? AND " + LOCATION_TABLE_STATE_ID + "=?", new String[]{location_name, cmOrCmp.trim(), state_id}, LOCATION_TABLE_LOCATION_NAME, null, LOCATION_TABLE_LOCATION_NAME + " ASC");

        Cursor cursor = db.query(LOCATION_TABLE,
                null,
                LOCATION_TABLE_LOCATION_ID + " in ( select " + LOCATION_TABLE_LOCATION_ID + " from " + GODOWN_TABLE + " inner join " + WAREHOUSE_TABLE + " on " +
                        GODOWN_TABLE_WAREHOUSE_ID + " = " + WAREHOUSE_TABLE_WAREHOUSE_ID + " Where " + GODOWN_TABLE_ACTIVE + " = 'NO' ) AND " + LOCATION_TABLE_CM_AND_CMP_TABLE + " = ? AND " + LOCATION_TABLE_STATE_ID + " = ?",
                new String[]{cmOrCmp.trim(), state_id},
                null,
                null,
                LOCATION_TABLE_LOCATION_NAME + " ASC");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_NAME)));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return list;
    }

    public ArrayList<String> getLocationArrayGodownAudit(String location_name, String cmOrCmp, String state_name) {
        ArrayList<String> list = new ArrayList<String>();
        open();
        String state_id = "";
        Cursor cursor_state_id = db.query(STATE_TABLE, null, STATE_TABLE_STATE_NAME + "=? AND " + STATE_TABLE_CM_AND_CMP_SEGMENT + "= ? ", new String[]{state_name, cmOrCmp}, null, null, null);

        if (cursor_state_id != null && cursor_state_id.getCount() > 0) {
            cursor_state_id.moveToFirst();
            state_id = cursor_state_id.getString(cursor_state_id.getColumnIndex(STATE_TABLE_STATE_ID));
            cursor_state_id.close();
        }


        //Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=? AND " + LOCATION_TABLE_CM_AND_CMP_TABLE + "=? AND " + LOCATION_TABLE_STATE_ID + "=?", new String[]{location_name, cmOrCmp.trim(), state_id}, LOCATION_TABLE_LOCATION_NAME, null, LOCATION_TABLE_LOCATION_NAME + " ASC");
        /*




        Cursor cursor = db.query(LOCATION_TABLE,
                null,
                LOCATION_TABLE_LOCATION_ID + " in ( select " + LOCATION_TABLE_LOCATION_ID + " from " + GODOWN_TABLE + " inner join " + WAREHOUSE_TABLE + " on " +
                        LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE_ID + " = " + WAREHOUSE_TABLE_WAREHOUSE_ID + " Where " + GODOWN_TABLE_ACTIVE + " = 'YES' ) AND " +
                        LOCATION_TABLE_CM_AND_CMP_TABLE + " = ? AND " + LOCATION_TABLE_STATE_ID + " = ?",
                new String[]{cmOrCmp.trim(), state_id},
                null,
                null,
                LOCATION_TABLE_LOCATION_NAME + " ASC");

                select * from CMPro_Mst_Location
          where CMPro_Mst_Location.LocationID in (
          select CMPro_Mst_Warehouse.LocationID  from CMPro_Mst_Godown inner join CMPro_Mst_Warehouse
          on CMPro_Mst_Godown.WarehouseID = CMPro_Mst_Warehouse.WarehouseID where CMPro_Mst_Godown.GodownCloseDate is null
          AND CMPro_Mst_Godown.IsMappedInMobile = 1 AND CMPro_Mst_Warehouse.WarehouseCloseDate IS NULL) AND StateID = 33
         */


        Cursor cursor = db.query(LOCATION_TABLE,
                null,
                LOCATION_TABLE_LOCATION_ID + " in ( select " + WAREHOUSE_TABLE_LOCATION_ID + " from " + GODOWN_TABLE + " inner join " + WAREHOUSE_TABLE + " on " +
                        GODOWN_TABLE_WAREHOUSE_ID + " = " + WAREHOUSE_TABLE_WAREHOUSE_ID + " Where " + GODOWN_TABLE_ACTIVE + " = 'YES' ) AND " +
                        LOCATION_TABLE_CM_AND_CMP_TABLE + " = ? AND " + LOCATION_TABLE_STATE_ID + " = ?",
                new String[]{cmOrCmp.trim(), state_id},
                null,
                null,
                LOCATION_TABLE_LOCATION_NAME + " ASC");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_NAME)));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return list;
    }

    public String getLocationNameUsingId(String id) {
        String name = "";
        open();
        Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_ID + "=?", new String[]{id}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_NAME));
            cursor.close();
        }

        close();
        return name;
    }

    public ArrayList<String> getSegments() {
        ArrayList<String> list = new ArrayList<String>();

        open();
        Cursor cursor = db.query(SEGMENT_TABLE, new String[]{COMMON_ID, SEGMENT_TABLE_SEGMENT_NAME}, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(SEGMENT_TABLE_SEGMENT_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        close();

        return list;
    }

    public ArrayList<String> getSegments_cmcmp() {
        ArrayList<String> list = new ArrayList<String>();

        open();
        Cursor cursor;
        //Cursor cursor = db.query(SEGMENT_TABLE, new String[]{COMMON_ID, SEGMENT_TABLE_SEGMENT_NAME}, SEGMENT_TABLE_SEGMENT_ID = 1 AND SEGMENT_TABLE_SEGMENT_ID = 2, null, null, null, null);
        final String MY_QUERY = "SELECT " + COMMON_ID + "," + SEGMENT_TABLE_SEGMENT_NAME + " FROM " + SEGMENT_TABLE +
                " WHERE " + SEGMENT_TABLE_SEGMENT_ID + " IN (1,2) Order by " + SEGMENT_TABLE_SEGMENT_NAME;
        cursor = db.rawQuery(MY_QUERY, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(SEGMENT_TABLE_SEGMENT_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        close();

        return list;
    }


    public ArrayList<String> getGodownAttendance(String cmOrCmp, String locationName) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("SELECT GODOWN");
        open();

        String location_id = "";
        Cursor cursor_loaction_id = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{locationName}, null, null, null);

        if (cursor_loaction_id != null && cursor_loaction_id.getCount() > 0) {
            cursor_loaction_id.moveToFirst();
            location_id = cursor_loaction_id.getString(cursor_loaction_id.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor_loaction_id.close();
        }

        Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_LOCATION_ID + "=? AND " + GODOWN_TABLE_ACTIVE + " =? ", new String[]{cmOrCmp.trim(), location_id, "YES"}, GODOWN_TABLE_GODOWN_NAME, null, GODOWN_TABLE_GODOWN_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_NAME)));
            } while (cursor.moveToNext());

            cursor.close();
        }
        close();
        return list;
    }


    //public ArrayList<String> get_CMPGodown(String CMPName)
   /* public ArrayList<String> get_CMPGodown_Depo()
    {
        ArrayList<String> list = new ArrayList<String>();
        open();

        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_ACTIVE + "=? " + " AND "  + GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_LOCATION_ID + "=?", new String[]{"YES", CMorCMP.trim(), location_id}, null, null, GODOWN_TABLE_GODOWN_NAME + " ASC");
        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_ACTIVE + "=? " + " AND " + GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_WAREHOUSE_NAME + "=? ", new String[]{"YES", "CMP", CMPName}, null, null, null);
        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_ACTIVE + "=? " + " AND " + GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_WAREHOUSE_NAME + "=? ", new String[]{"YES", "CMP", "Akola 897"}, null, null, null);
        Cursor cursor = db.query(STATE_TABLE, null, STATE_TABLE_CM_AND_CMP_SEGMENT + "=? " , new String[]{"CMP"}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(STATE_TABLE_STATE_NAME)));
                //GODOWN_TABLE_WAREHOUSE_NAME
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return list;
    }
*/

    /* public ArrayList<String> get_Godown_No_CMP()
     {
         ArrayList<String> list = new ArrayList<String>();
         open();

         Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_ACTIVE + "=? " + " AND "  + GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_WAREHOUSE_NAME + "=?", new String[]{"YES", "CMP", "Akola 897"}, null, null, GODOWN_TABLE_GODOWN_NAME + " ASC");
         if (cursor != null && cursor.getCount() > 0) {
             cursor.moveToFirst();
             //mapped_status=cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_ACTIVE)).toString();
             do {

                 list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_NAME)));
             } while (cursor.moveToNext());

             cursor.close();
         }
         close();
         return list;
     }

     public ArrayList<String> get_Godown_No()
     {
         ArrayList<String> list = new ArrayList<String>();
         open();

         Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_ACTIVE + "=? " + " AND "  + GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_WAREHOUSE_NAME + "=?", new String[]{"YES", "CMP", "Akola 897"}, null, null, GODOWN_TABLE_GODOWN_NAME + " ASC");
         if (cursor != null && cursor.getCount() > 0) {
             cursor.moveToFirst();
             //mapped_status=cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_ACTIVE)).toString();
             do {

                 list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_NAME)));
             } while (cursor.moveToNext());

             cursor.close();
         }
         close();
         return list;
     }
 */
    public ArrayList<String> get_CMP(String CMorCMP, String locationName) {
        ArrayList<String> list = new ArrayList<String>();
        String location_id = "";
        String mapped_status = "";
        open();

        Cursor cursor_loaction_id = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{locationName}, null, null, null);
        if (cursor_loaction_id != null && cursor_loaction_id.getCount() > 0) {
            cursor_loaction_id.moveToFirst();
            location_id = cursor_loaction_id.getString(cursor_loaction_id.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor_loaction_id.close();
        }

        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_ACTIVE + "=? " + " AND "  + GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_LOCATION_ID + "=?", new String[]{"YES", CMorCMP.trim(), location_id}, null, null, GODOWN_TABLE_GODOWN_NAME + " ASC");
        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_ACTIVE + "=? "
        // + " AND "  + GODOWN_TABLE_CM_AND_CMP_TABLE + "=? "
        // + " AND " + GODOWN_TABLE_LOCATION_ID + "=?",
        // new String[]{"YES", CMorCMP.trim(), location_id}, null, null,
        // GODOWN_TABLE_GODOWN_NAME + " ASC");

        final String MY_QUERY = "SELECT  DISTINCT " + GODOWN_TABLE_WAREHOUSE_NAME +
                " FROM " + GODOWN_TABLE +
                " WHERE " + GODOWN_TABLE_ACTIVE + " = 'YES' " +
                " AND " + GODOWN_TABLE_CM_AND_CMP_TABLE + " = '" + CMorCMP.trim() + "'" +
                " AND " + GODOWN_TABLE_LOCATION_ID + " = '" + location_id + "'" +
                " Order by " + GODOWN_TABLE_WAREHOUSE_NAME;
        Cursor cursor = db.rawQuery(MY_QUERY, null);


        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            //mapped_status=cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_ACTIVE)).toString();
            do {

                list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_WAREHOUSE_NAME)));
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();
        return list;
    }

    public ArrayList<String> getGodown(String cmOrCmp, String WarehouseName) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("SELECT GODOWN");
        open();
       /* String location_id = "";
        Cursor cursor_loaction_id = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{locationName}, null, null, null);
        if (cursor_loaction_id != null && cursor_loaction_id.getCount() > 0)
        {
            cursor_loaction_id.moveToFirst();
            location_id = cursor_loaction_id.getString(cursor_loaction_id.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor_loaction_id.close();
        }*/

        Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_WAREHOUSE_NAME + "=?", new String[]{cmOrCmp.trim(), WarehouseName}, null, null, GODOWN_TABLE_GODOWN_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_NAME)));
            } while (cursor.moveToNext());

            cursor.close();
        }

        close();


        return list;
    }

    public ArrayList<String> getGodownNo(String cmOrCmp, String warehouseName) {
        ArrayList<String> list = new ArrayList<String>();
        open();
        Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_CM_AND_CMP_TABLE + "=? " + " AND " + GODOWN_TABLE_WAREHOUSE_NAME + "=?", new String[]{cmOrCmp.trim(), warehouseName}, null, null, GODOWN_TABLE_GODOWN_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_NAME)));
            } while (cursor.moveToNext());

            cursor.close();
        }
        close();

        return list;
    }


    public ArrayList<String> getWarehouse(String location_name) {
        ArrayList<String> list = new ArrayList<String>();

        String location_id = "";

        open();

        Cursor cursor_id = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{location_name}, null, null, null);

        if (cursor_id != null && cursor_id.getCount() > 0) {
            cursor_id.moveToFirst();
            location_id = cursor_id.getString(cursor_id.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor_id.close();
        }


        //Cursor cursor = db.query(WAREHOUSE_TABLE, new String[]{COMMON_ID, WAREHOUSE_TABLE_WAREHOUSE_NAME}, WAREHOUSE_TABLE_LOCATION_ID + " = ?", new String[]{location_id}, WAREHOUSE_TABLE_WAREHOUSE_NAME, null, WAREHOUSE_TABLE_WAREHOUSE_NAME + " ASC");
        /*
        select * from CMPro_Mst_Warehouse  where WarehouseCloseDate IS NULL AND WarehouseID in (
        select WarehouseID from CMPro_Mst_Godown where GodownCloseDate is null)

        select * from CMPro_Mst_Warehouse  where WarehouseCloseDate IS NULL AND LocationID = 1119 AND WarehouseID in (
        select WarehouseID from CMPro_Mst_Godown where GodownCloseDate is null)

        select * from CMPro_Mst_Warehouse
        where LocationId = 8
        AND
        WarehouseID in (select WarehouseID from CMPro_Mst_Godown where GodownCloseDate is null)

        Cursor cursor = db.query(WAREHOUSE_TABLE,
                null,
                WAREHOUSE_TABLE_WAREHOUSE_ID + " in ( select " + GODOWN_TABLE_WAREHOUSE_ID + " from " + GODOWN_TABLE + " ) AND " + WAREHOUSE_TABLE_LOCATION_ID + " = ?",
                //WAREHOUSE_TABLE_WAREHOUSE_ID + " in ( select " + GODOWN_TABLE_WAREHOUSE_ID + " from " + GODOWN_TABLE +  " Where "  + GODOWN_TABLE_GODOWN_CLOSEDATE + " IS NULL ) AND " + WAREHOUSE_TABLE_LOCATION_ID + " = ?",
                new String[]{location_id},
                null,
                null,
                WAREHOUSE_TABLE_WAREHOUSE_NAME + " ASC");

      select CMPro_Mst_Warehouse.WarehouseName, CMPro_Mst_Warehouse.WarehouseID from CMPro_Mst_Warehouse
      inner join CMPro_Mst_Godown
      on CMPro_Mst_Warehouse.WarehouseID = CMPro_Mst_Godown.WarehouseID
      and GodownCloseDate is null and WarehouseCloseDate is null
      and CMPro_Mst_Warehouse.LocationID=8
        */
        Cursor cursor;

        final String MY_QUERY = "SELECT " + WAREHOUSE_TABLE_WAREHOUSE_NAME + " FROM " + WAREHOUSE_TABLE + " INNER JOIN " + GODOWN_TABLE + " ON " +
                WAREHOUSE_TABLE_WAREHOUSE_ID + " = " + GODOWN_TABLE_WAREHOUSE_ID +
                " WHERE " + WAREHOUSE_TABLE_LOCATION_ID + " = ?  AND " + GODOWN_TABLE_ACTIVE + " = 'YES'" +
                "GROUP BY " + WAREHOUSE_TABLE_WAREHOUSE_NAME;
        cursor = db.rawQuery(MY_QUERY, new String[]{location_id});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(WAREHOUSE_TABLE_WAREHOUSE_NAME)));
            }
            while (cursor.moveToNext());
            cursor.close();
        }


        close();


        return list;
    }

    public void insertAttandance(String id, String emp_id, String emp_name, String on_behalf_id, String lat, String lang,
                                 String location_name, String segment, String godown, String date, String remark, Bitmap bitmap_pic, String local_address) {

        open();

        String location_id = "", segment_id = "", godown_id = "";
        String tempSegment = segment;

        if (!tempSegment.equals("CM") && !tempSegment.equals("CMP")) {
            tempSegment = "HR";
        }

        Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?  AND " + LOCATION_TABLE_CM_AND_CMP_TABLE + "= ? ", new String[]{location_name, tempSegment}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            location_id = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor.close();
        }
        cursor = null;

        cursor = db.query(SEGMENT_TABLE, null, SEGMENT_TABLE_SEGMENT_NAME + "=?", new String[]{segment}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            segment_id = cursor.getString(cursor.getColumnIndex(SEGMENT_TABLE_SEGMENT_ID));
            cursor.close();
        }
        cursor = null;

        if (godown.length() > 0) {
            cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_GODOWN_NAME + "=?", new String[]{godown}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                godown_id = cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_ID));
                cursor.close();
            }
        } else {
            godown_id = "0";
        }
        cursor = null;

        ContentValues values = new ContentValues();
        values.put(LOCAL_ATTENDANCE_ID, id);
        values.put(LOCAL_ATTENDANCE_EMP_ID, emp_id);
        values.put(LOCAL_ATTENDANCE_NAME, emp_name);
        values.put(LOCAL_ATTENDANCE_ON_BEHALF_ID, on_behalf_id);
        values.put(LOCAL_ATTENDANCE_LATTITUTE, lat);
        values.put(LOCAL_ATTENDANCE_LONGITUTE, lang);
        values.put(LOCAL_ATTENDANCE_LOCATION_NAME, location_name);
        values.put(LOCAL_ATTENDANCE_LOCATION_ID, location_id);
        values.put(LOCAL_ATTENDANCE_SEGMENT, segment);
        values.put(LOCAL_ATTENDANCE_SEGMENT_ID, segment_id);
        values.put(LOCAL_ATTENDANCE_GODOWN, godown);
        values.put(LOCAL_ATTENDANCE_GODOWN_ID, godown_id);
        values.put(LOCAL_ATTENDANCE_LOCAL_ADDRESS, local_address);
        values.put(LOCAL_ATTENDANCE_DATE_TIME, date);
        //values.put(LOCAL_ATTENDANCE_DATE_TIME, getDateTimeWithHHMMSS());
        values.put(LOCAL_ATTENDANCE_REMARKS, remark);
        values.put(LOCAL_ATTENDANCE_GODOWN_PIC, getBitmapAsByteArray(bitmap_pic));

        db.insert(LOCAL_ATTENDANCE_TABLE, null, values);

        close();

    }
    //----->>>>Surya

    public long ChkValidation_Submit_Bags(String sDeposit_id, String sTotalbags) {
        open();
        long lReturnvalue = 0;
        long lSum_OfTotalbags = 0;

        final String MY_QUERY3 = " SELECT sum(CAST(" + LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS + " AS INTEGER))" +
                " as SumTotalBags " +
                " FROM " + DEPOSIT_TABLE_DETAILS +
                " WHERE " + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " = '" + sDeposit_id + "'";
        Log.e("SUM Query", MY_QUERY3);
        Cursor cursor = db.rawQuery(MY_QUERY3, null);

        long lTotalBags_conversion = Long.parseLong(sTotalbags);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                lSum_OfTotalbags = cursor.getInt(0);
                Log.e("Sum of Bags", String.valueOf(lSum_OfTotalbags));
            }
            cursor.close();
        } else {
            lSum_OfTotalbags = 0;
        }

        if (lSum_OfTotalbags != lTotalBags_conversion) {
            lReturnvalue = 0;
        }
        lReturnvalue = lSum_OfTotalbags;
        close();
        return lReturnvalue;
    }


    public double ChkValidation_Submit_Qty(String sDeposit_id, String STotalQty) {
        open();
        double lReturnvalue = 0;
        double lSum_OfTotalQty = 0;

        final String MY_QUERY3 = " SELECT " +
                "  sum(CAST(" + LOCAL_DEPOSIT_DETAILS_TABLE_QTY + " AS NUMERIC(10,3)))" +
                " as SumTotalQty FROM " + DEPOSIT_TABLE_DETAILS +
                " WHERE " + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " = '" + sDeposit_id + "'";
        Log.e("SUM Query", MY_QUERY3);
        Cursor cursor = db.rawQuery(MY_QUERY3, null);

        double lTotalQty_conversion = Double.parseDouble(STotalQty);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                lSum_OfTotalQty = cursor.getDouble(0);
                Log.e("Sum of Qty", String.valueOf(lSum_OfTotalQty));
            }
            cursor.close();
        } else {
            lSum_OfTotalQty = 0;
        }

        if (lSum_OfTotalQty != lTotalQty_conversion) {
            lReturnvalue = 0;
        }
        lReturnvalue = lSum_OfTotalQty;
        close();
        return lReturnvalue;
    }


    //Insert Deposit Details Entry --- START
    public String insertDeposit_Details
    (
            String deposit_id, String godown_name, String stack_no, String lot_no, String no_of_bags, String qty, String client_id, String client_name, String depository_id, String depository_name, String created_by, String created_date, String lTotalBags, String lTotalqty) {
        open();
        String godown_id = "", one_row_checker_no = "", sReturnString = "";
        String sRecDupl = "";
        long lSum_OfTotalbags = 0, long_insert_row_index_details = 0;
        long lCurrent_bags = 0;
        double lSum_OfTotalQty = 0, lCurrent_qty = 0;


        final String MY_QUERY = " SELECT sum(CAST(" + LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS + " AS INTEGER))" +
                " as SumTotalBags , sum(CAST(" + LOCAL_DEPOSIT_DETAILS_TABLE_QTY + " AS INTEGER))" +
                " as SumTotalQty FROM " + DEPOSIT_TABLE_DETAILS +
                " WHERE " + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " = '" + deposit_id + "'";
        Log.e("SUM Query", MY_QUERY);

        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                lSum_OfTotalbags = cursor.getInt(0);
                lSum_OfTotalQty = cursor.getDouble(1);

                Log.e("Sum of Bags", String.valueOf(lSum_OfTotalbags));
                Log.e("Sum of Qty", String.valueOf(lSum_OfTotalQty));
            }
            cursor.close();
        } else {
            lSum_OfTotalbags = 0;
            lSum_OfTotalQty = 0;
        }

        if (godown_name.length() > 0) {
            Cursor cursor2 = db.query(GODOWN_TABLE, null, GODOWN_TABLE_GODOWN_NAME + "=?", new String[]{godown_name}, null, null, null);
            if (cursor2 != null && cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                //   ---->>>Surya
                godown_id = cursor2.getString(cursor2.getColumnIndex(GODOWN_TABLE_GODOWN_ID));
                cursor2.close();
            }
        }

        final String MY_QUERY2 = " SELECT  " + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID +
                " FROM " + DEPOSIT_TABLE_DETAILS +
                " WHERE " + LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID + " = '" + godown_id + "'" +
                " AND " + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " = '" + deposit_id + "'" +
                " AND " + LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO + " = '" + stack_no + "'" +
                " AND " + LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO + " = '" + lot_no + "'";
        Cursor cursor3 = db.rawQuery(MY_QUERY2, null);

        if (cursor3 != null && cursor3.getCount() > 0) {
            cursor3.moveToFirst();
            sRecDupl = "DUPLICATE RECORD";
            Log.e("duplicate", sRecDupl);
            cursor3.close();
        }

        if (no_of_bags != null) {
            lCurrent_bags = Long.parseLong(no_of_bags);
            lCurrent_qty = Double.parseDouble(qty);
        }

        long lTotalBags_conversion = Long.parseLong(lTotalBags);
        double lTotalQty_conversion = Double.parseDouble(lTotalqty);

        if ((lSum_OfTotalbags + lCurrent_bags) > lTotalBags_conversion) {
            //sReturnString="Number of Bags should be less then equal to "+ lTotalBags;
            sReturnString = "Bags";
        } else if ((lSum_OfTotalQty + lCurrent_qty) > lTotalQty_conversion) {
            //sReturnString="Number of Quantity should be less then equal to "+ lTotalqty;
            sReturnString = "Qty";
        } else if (sRecDupl.equals("DUPLICATE RECORD")) {
            //sReturnString="Duplicate Record of Godown : " + godown_name + ", " + "Stack No. : " +stack_no+ ", "+" Lot No. :" + lot_no;
            sReturnString = "Duplicate Record";
        } else {
            Cursor cursor4 = db.query(DEPOSIT_TABLE_DETAILS, null, null, null, null, null, null);

            if (cursor4 != null && cursor4.getCount() > 0) {

                cursor4.moveToLast();
                one_row_checker_no = cursor4.getString(cursor4.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_ID));
                one_row_checker_no = "" + ((Integer.parseInt(one_row_checker_no) + 1));
                cursor4.close();
            } else {
                one_row_checker_no = "1";
            }
            ContentValues values = new ContentValues();
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_ID, one_row_checker_no);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID, deposit_id);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID, godown_id);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO, stack_no);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO, lot_no);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS, no_of_bags);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_QTY, qty);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_ID, client_id);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_NAME, client_name);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_ID, depository_id);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_NAME, depository_name);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_IS_POSTED_ON_SERVER, "0");
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_BY, created_by);
            values.put(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_DATE, created_date);
            long_insert_row_index_details = db.insert(DEPOSIT_TABLE_DETAILS, null, values);

            sReturnString = "Inserted Successful";
        }
        close();
        return sReturnString;
    }

    //Start for Delete Acitivity in deposite next

    public String insertDeposit_Del_Details(String DelGod_no, String Delstack_No, String DelLotNo, String DelNoof_Bags, String DelQuantityIn_MT, String DelClient_Id, String DelClient_Name,
                                            String DelDp_Id, String DelDp_Name, String sDeposit_Header_Id) {
        open();
        String godown_id = "";

        if (DelGod_no.length() > 0) {
            Cursor cursor2 = db.query(GODOWN_TABLE, null, GODOWN_TABLE_GODOWN_NAME + "=?", new String[]{DelGod_no}, null, null, null);
            if (cursor2 != null && cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                //   ---->>>Surya
                godown_id = cursor2.getString(cursor2.getColumnIndex(GODOWN_TABLE_GODOWN_ID));
                cursor2.close();
                Log.e("GodownId id ", godown_id);
            }
        }


        final String MY_QUERY = "DELETE FROM " + DEPOSIT_TABLE_DETAILS + " WHERE " +
                LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID + " = '" + godown_id + "' AND " +
                LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO + " = '" + Delstack_No + "' AND " +
                LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO + " = '" + DelLotNo + "' AND " +
                LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS + " = '" + DelNoof_Bags + "' AND " +
                LOCAL_DEPOSIT_DETAILS_TABLE_QTY + " = '" + DelQuantityIn_MT + "' AND " +
                LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_ID + " = '" + DelClient_Id + "' AND " +
                LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_NAME + " = '" + DelClient_Name + "' AND " +
                LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_ID + " = '" + DelDp_Id + "' AND " +
                LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_NAME + " = '" + DelDp_Name + "'";

        db.execSQL(MY_QUERY);
       /* Cursor cursor = db.rawQuery(MY_QUERY, null);
        String DelItem =String.valueOf(cursor);*/
       /* String getCountcursor =String.valueOf(cursor.getCount());*/
        Log.e("My_Query database", MY_QUERY);
        /*Log.e("Get Count in cursor",getCountcursor);*/
       /* cursor.close();*/

        final String my_count = " SELECT COUNT(*) FROM " + DEPOSIT_TABLE_DETAILS + " WHERE " +
                LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " = '" + sDeposit_Header_Id + "' ";

        Log.e("SUM of Count Query", my_count);
        Log.e("Header ID of Deposit", sDeposit_Header_Id);

        Cursor cursor2 = db.rawQuery(my_count, null);

        if (cursor2 != null && cursor2.getCount() > 0) {
            cursor2.moveToFirst();
            //godown_id = cursor2.getString(cursor2.getColumnIndex("CountRow"));
            String getcount = String.valueOf(cursor2.getCount());
            cursor2.close();
            Log.e("Get Count in cursor2", getcount);
        }


        //-- Start
     /*   final String MY_QUERY1 = " SELECT sum(CAST(" + LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS + " AS INTEGER))" +
                " as SumTotalBags , sum(CAST(" + LOCAL_DEPOSIT_DETAILS_TABLE_QTY + " AS INTEGER))" +
                " as SumTotalQty FROM " + DEPOSIT_TABLE_DETAILS +
                " WHERE " + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " = '" + sDeposit_Header_Id + "'";
        Log.e("SUM Query", MY_QUERY1);
        long lSum_OfTotalbags = 0;
        double lSum_OfTotalQty = 0;

        Cursor cursor4 = db.rawQuery(MY_QUERY1, null);

        if (cursor4 != null && cursor4.getCount() > 0)
        {
            if (cursor4.moveToFirst())
            {
                lSum_OfTotalbags = cursor4.getInt(0);
                lSum_OfTotalQty = cursor4.getDouble(1);

                Log.e("Sum of Bag After Delete", String.valueOf(lSum_OfTotalbags));
                Log.e("Sum of Qty After Delete", String.valueOf(lSum_OfTotalQty));
            }
            cursor4.close();
        }
*/


        //-- End


        String Message = "Success";

        close();

        return Message;


    }
    // End for Delete Activity for deposite next


    // Start - for Withdrawal Details

    public String insertWithdrawal_Details(String withdrawal_id, String godown_name, String stack_no, String lot_no, String no_of_bags, String qty, String created_by, String created_date, String lTotalBags, String lTotalqty, String rrn_whr_no) {
        open();
        String godown_id = "", one_row_checker_no = "", sReturnString = "";
        String sRecDupl = "";
        long lSum_OfTotalbags = 0, long_insert_row_index_details = 0;
        long lCurrent_bags = 0;
        double lSum_OfTotalQty = 0, lCurrent_qty = 0;

        final String MY_QUERY = " SELECT sum(CAST(" + LOCAL_WITHDRAWAL_DETAILS_TABLE_NO_OF_BAGS + " AS INTEGER))" +
                " as SumTotalBags , sum(CAST(" + LOCAL_WITHDRAWAL_DETAILS_TABLE_QTY + " AS INTEGER))" +
                " as SumTotalQty FROM " + WITHDRAWAL_TABLE_DETAILS +
                " WHERE " + LOCAL_WITHDRAWAL_DETAILS_TABLE_WITHDRAWAL_ID + " = '" + withdrawal_id + "'";
        Log.e("SUM Query", MY_QUERY);

        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                lSum_OfTotalbags = cursor.getInt(0);
                lSum_OfTotalQty = cursor.getDouble(1);

                Log.e("Sum of Bags", String.valueOf(lSum_OfTotalbags));
                Log.e("Sum of Qty", String.valueOf(lSum_OfTotalQty));
            }
            cursor.close();
        } else {
            lSum_OfTotalbags = 0;
            lSum_OfTotalQty = 0;
        }

        if (godown_name.length() > 0) {
            Cursor cursor2 = db.query(GODOWN_TABLE, null, GODOWN_TABLE_GODOWN_NAME + "=?", new String[]{godown_name}, null, null, null);
            if (cursor2 != null && cursor2.getCount() > 0) {
                cursor2.moveToFirst();
                godown_id = cursor2.getString(cursor2.getColumnIndex(GODOWN_TABLE_GODOWN_ID));
                cursor2.close();
            }
        }

        final String MY_QUERY2 = " SELECT  " + LOCAL_WITHDRAWAL_DETAILS_TABLE_WITHDRAWAL_ID +
                " FROM " + WITHDRAWAL_TABLE_DETAILS +
                " WHERE " + LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID + " = '" + godown_id + "'" +
                " AND " + LOCAL_WITHDRAWAL_DETAILS_TABLE_WITHDRAWAL_ID + " = '" + withdrawal_id + "'" +
                " AND " + LOCAL_WITHDRAWAL_DETAILS_TABLE_STACK_NO + " = '" + stack_no + "'" +
                " AND " + LOCAL_WITHDRAWAL_DETAILS_TABLE_LOT_NO + " = '" + lot_no + "'";
        Cursor cursor3 = db.rawQuery(MY_QUERY2, null);

        if (cursor3 != null && cursor3.getCount() > 0) {
            cursor3.moveToFirst();
            sRecDupl = "DUPLICATE RECORD";
            Log.e("duplicate", sRecDupl);
            cursor3.close();
        }


        if (no_of_bags != null) {
            lCurrent_bags = Long.parseLong(no_of_bags);
            lCurrent_qty = Double.parseDouble(qty);
        }

        long lTotalBags_conversion = Long.parseLong(lTotalBags);
        double lTotalQty_conversion = Double.parseDouble(lTotalqty);

        if ((lSum_OfTotalbags + lCurrent_bags) > lTotalBags_conversion) {
            //sReturnString="Number of Bags should be less then equal to "+ lTotalBags;
            sReturnString = "Bags";
        } else if ((lSum_OfTotalQty + lCurrent_qty) > lTotalQty_conversion) {
            //sReturnString="Number of Quantity should be less then equal to "+ lTotalqty;
            sReturnString = "Qty";
        } else if (sRecDupl.equals("DUPLICATE RECORD")) {
            //sReturnString="Duplicate Record of Godown : " + godown_name + ", " + "Stack No. : " +stack_no+ ", "+" Lot No. :" + lot_no;
            sReturnString = "Duplicate Record";
        } else {
            Cursor cursor4 = db.query(WITHDRAWAL_TABLE_DETAILS, null, null, null, null, null, null);

            if (cursor4 != null && cursor4.getCount() > 0) {
                cursor4.moveToLast();
                one_row_checker_no = cursor4.getString(cursor4.getColumnIndex(LOCAL_WITHDRAWAL_DETAILS_TABLE_ID));
                one_row_checker_no = "" + ((Integer.parseInt(one_row_checker_no) + 1));
                cursor4.close();
            } else {
                one_row_checker_no = "1";
            }
            ContentValues values = new ContentValues();
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_ID, one_row_checker_no);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_WITHDRAWAL_ID, withdrawal_id);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_GODOWN_ID, godown_id);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_STACK_NO, stack_no);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_LOT_NO, lot_no);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_NO_OF_BAGS, no_of_bags);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_QTY, qty);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_RRN_WHT_NO, rrn_whr_no);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_IS_POSTED_ON_SERVER, "0");
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_CREATED_BY, created_by);
            values.put(LOCAL_WITHDRAWAL_DETAILS_TABLE_CREATED_DATE, created_date);
            long_insert_row_index_details = db.insert(WITHDRAWAL_TABLE_DETAILS, null, values);

            sReturnString = "Inserted Successful";
        }
        close();
        return sReturnString;
    }

    // End


    public String Update_Deposit_Table_Details(String Deposit_id) {
        open();
        String sRetrun = "";

        final String MY_QUERY1 = " UPDATE " + DEPOSIT_TABLE +
                " SET " + LOCAL_DEPOSIT_TABLE_IS_POSTED_ON_SERVER + " = '1' " +
                " WHERE " + LOCAL_DEPOSIT_TABLE_DEPOSIT_ID + " = '" + Deposit_id + "'";
        Cursor cursor = db.rawQuery(MY_QUERY1, null);

        final String MY_QUERY2 = " UPDATE " + DEPOSIT_TABLE_DETAILS +
                " SET " + LOCAL_DEPOSIT_DETAILS_TABLE_IS_POSTED_ON_SERVER + " = '1' " +
                " WHERE " + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " = '" + Deposit_id + "'";
        Cursor cursor3 = db.rawQuery(MY_QUERY2, null);

        if (cursor3 != null && cursor3.getCount() > 0) {
            cursor3.moveToFirst();
            sRetrun = "Success";
        } else {
            sRetrun = "UnSuccess";
        }

        close();
        return sRetrun;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return outputStream.toByteArray();
    }


    public String getGodownAddress(String name) {
        String add = "NO ADDRESS ";

        open();

        Cursor cursor = db.query(GODOWN_TABLE, new String[]{COMMON_ID, GODOWN_TABLE_GODOWN_ADDRESS}, GODOWN_TABLE_GODOWN_NAME + "=?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            do {
                add = cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_ADDRESS));
            } while (cursor.moveToNext());
        }

        close();

        return add;
    }

    public void changeActiveFlag(String godown_name) {
        open();

        ContentValues values = new ContentValues();

        values.put(GODOWN_TABLE_ACTIVE, "YES");

        db.update(GODOWN_TABLE, values, GODOWN_TABLE_GODOWN_NAME + "=?", new String[]{godown_name});


        close();
    }

    public void insertGodownMapping(String id, String emp_id, String lat, String lang, String segment, String godown, String location, String state, String address, String current_date) {

        open();

        String location_id = "", segment_id = "", godown_id = "", state_id = "";

        Cursor cursor_state_id = db.query(STATE_TABLE, null, STATE_TABLE_STATE_NAME + "=? AND " + STATE_TABLE_CM_AND_CMP_SEGMENT + "= ? ", new String[]{state, segment}, null, null, null);
        if (cursor_state_id != null && cursor_state_id.getCount() > 0) {
            cursor_state_id.moveToFirst();
            state_id = cursor_state_id.getString(cursor_state_id.getColumnIndex(STATE_TABLE_STATE_ID));
            cursor_state_id.close();
        }

        Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=? AND " + LOCATION_TABLE_STATE_ID + "=? AND " +
                LOCATION_TABLE_CM_AND_CMP_TABLE + "=?", new String[]{location, state_id, segment}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            location_id = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor.close();
        }
        cursor = null;

        /*if (segment.toUpperCase().contains("CM")) {
            state_id = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_STATE_ID));
        } else {
            state_id = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_STATE_ID));
        }*/

        cursor = db.query(SEGMENT_TABLE, null, SEGMENT_TABLE_SEGMENT_NAME + "=?", new String[]{segment}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            segment_id = cursor.getString(cursor.getColumnIndex(SEGMENT_TABLE_SEGMENT_ID));
            cursor.close();
        }
        cursor = null;

        cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_GODOWN_NAME + "=? AND " + GODOWN_TABLE_LOCATION_ID + " = ? AND " + GODOWN_TABLE_CM_AND_CMP_TABLE + "= ? ",
                new String[]{godown, location_id, segment}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            godown_id = cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_ID));
            cursor.close();
        }
        cursor = null;

        ContentValues values = new ContentValues();

        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN_GEO_TAG_ID, id);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_EMP_ID, emp_id);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_LATTITUTE, lat);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_LONGITUTE, lang);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_SEGMENT, segment);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_SEGMENT_ID, segment_id);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN, godown);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN_ID, godown_id);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_LOCATION, location);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_LOCATION_ID, location_id);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_STATE, state);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_STATE_ID, state_id);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_ADDRESS, address);
        values.put(LOCAL_GODOWN_GEO_TAG_TABLE_CURRENT_DATE, current_date);

        db.insert(LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE, null, values);

        close();
    }


    public ArrayList<String> getAuditor() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(LoginActivity.EMP_NAME_GLOBAL);
        open();

        Cursor cursor = db.query(AUDITOR_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(AUDITOR_TABLE_EMP_NAME)));
            } while (cursor.moveToNext());

        }
        close();

        return list;

    }


    public ArrayList<String> getAuditType(String segment_name) {
        ArrayList<String> list = new ArrayList<String>();

        open();

        String segment_id = "";
        Cursor cursor_segment_id = db.query(SEGMENT_TABLE, null, SEGMENT_TABLE_SEGMENT_NAME + "=? ", new String[]{segment_name}, null, null, null);
        if (cursor_segment_id != null && cursor_segment_id.getCount() > 0) {
            cursor_segment_id.moveToFirst();
            segment_id = cursor_segment_id.getString(cursor_segment_id.getColumnIndex(SEGMENT_TABLE_SEGMENT_ID));
            cursor_segment_id.close();
        }

        Cursor cursor = db.query(AUDIT_TYPE_TABLE, null, AUDIT_TYPE_TABLE_SEGMENT_ID + "=?", new String[]{segment_id}, AUDIT_TYPE_TABLE_AUDIT_TYPE, null, AUDIT_TYPE_TABLE_AUDIT_TYPE + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(AUDIT_TYPE_TABLE_AUDIT_TYPE)));
            } while (cursor.moveToNext());

        }
        close();

        return list;

    }


    public ArrayList<String> getGodownForGeoMapping(String cmOrCmp, String locationName) {
        ArrayList<String> list = new ArrayList<String>();


        open();


        String location_id = "";
        Cursor cursor_location_id = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{locationName}, null, null, null);
        if (cursor_location_id != null && cursor_location_id.getCount() > 0) {
            cursor_location_id.moveToFirst();
            location_id = cursor_location_id.getString(cursor_location_id.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor_location_id.close();
        }
        Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_CM_AND_CMP_TABLE + "=? AND " + GODOWN_TABLE_ACTIVE + " =? AND " + GODOWN_TABLE_LOCATION_ID + " = ?", new String[]{cmOrCmp.trim(), "NO", location_id}, GODOWN_TABLE_GODOWN_NAME, null, GODOWN_TABLE_GODOWN_NAME + " ASC");
        /*
        final String MY_QUERY = "SELECT " + GODOWN_TABLE_GODOWN_NAME + " FROM " + GODOWN_TABLE + " INNER JOIN " + WAREHOUSE_TABLE + " ON " +
                LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE_ID + " = " + WAREHOUSE_TABLE_WAREHOUSE_ID + " INNER JOIN " + LOCATION_TABLE + " ON " + WAREHOUSE_TABLE_LOCATION_ID + " = " + LOCATION_TABLE_LOCATION_ID +
                " WHERE ( " + GODOWN_TABLE_ACTIVE + " = 'NO') AND  (" + LOCATION_TABLE_LOCATION_ID + " = ?) AND " + GODOWN_TABLE_CM_AND_CMP_TABLE + " = ? ";
                cursor =  db.rawQuery(MY_QUERY, new String[]{location_id, cmOrCmp.trim()});
        */

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_NAME)));
            } while (cursor.moveToNext());

            cursor.close();
        }


        close();

        return list;
    }

/*
    public ArrayList<String> getGodownForGodownAudit(String cmOrCmp, String locationName, String warehouseName)
    {
        ArrayList<String> list = new ArrayList<String>();
        open();
        String warehouse_id;
        Cursor cursor_warehouse_id = db.query(WAREHOUSE_TABLE, null, WAREHOUSE_TABLE_WAREHOUSE_NAME + "=?", new String[]{warehouseName}, null, null, null);
        cursor_warehouse_id.moveToFirst();
        warehouse_id = cursor_warehouse_id.getString(cursor_warehouse_id.getColumnIndex(WAREHOUSE_TABLE_WAREHOUSE_ID));
        cursor_warehouse_id.close();

        Cursor cursor;
        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_CM_AND_CMP_TABLE + "=? AND " + GODOWN_TABLE_ACTIVE + " =? AND " + GODOWN_TABLE_LOCATION_ID + " = ?", new String[]{cmOrCmp.trim(), "NO", location_id}, GODOWN_TABLE_GODOWN_NAME, null, GODOWN_TABLE_GODOWN_NAME + " ASC");


        --------- Godown
        Not Working
        select GodownID, GodownCode from CMPro_Mst_Godown Inner join CMPro_Mst_Warehouse
        On CMPro_Mst_Godown.WarehouseID = CMPro_Mst_Warehouse.WarehouseID
        where GodownCloseDate IS NULL AND CMPro_Mst_Warehouse.WarehouseID = 27

        select * from CMPro_Mst_Godown INNER JOIN CMPro_Mst_Warehouse
        ON CMPro_Mst_Godown.WarehouseID = CMPro_Mst_Warehouse.WarehouseID
        where CMPro_Mst_Godown.WarehouseID = 3356

        final String MY_QUERY = "SELECT " + GODOWN_TABLE_GODOWN_NAME + " FROM " + GODOWN_TABLE + " INNER JOIN " + WAREHOUSE_TABLE + " ON " +
                LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE_ID + " = " + WAREHOUSE_TABLE_WAREHOUSE_ID +
                " WHERE ( " + GODOWN_TABLE_ACTIVE + " = 'YES') AND  (" + WAREHOUSE_TABLE_WAREHOUSE_NAME + " = ?) AND " + GODOWN_TABLE_CM_AND_CMP_TABLE + " = ? ";
                cursor =  db.rawQuery(MY_QUERY, new String[]{warehouseName, cmOrCmp.trim()});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_NAME)));
            } while (cursor.moveToNext());


            cursor.close();

        }


        close();

        return list;
    }
*/

    public ArrayList<String> getGodownForGodownAudit(String cmOrCmp, String locationName, String warehouseName) {
        ArrayList<String> list = new ArrayList<String>();
        open();
        String warehouse_id = "";
        Cursor cursor_warehouse_id = db.query(WAREHOUSE_TABLE, null, WAREHOUSE_TABLE_WAREHOUSE_NAME + "=?", new String[]{warehouseName}, null, null, null);
        if (cursor_warehouse_id != null && cursor_warehouse_id.getCount() > 0) {
            cursor_warehouse_id.moveToFirst();
            warehouse_id = cursor_warehouse_id.getString(cursor_warehouse_id.getColumnIndex(WAREHOUSE_TABLE_WAREHOUSE_ID));
            cursor_warehouse_id.close();
        }

        //Cursor cursor;
        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_CM_AND_CMP_TABLE + "=? AND " + GODOWN_TABLE_WAREHOUSE_NAME + " = ?", new String[]{cmOrCmp.trim(), warehouseName}, GODOWN_TABLE_GODOWN_NAME, null, GODOWN_TABLE_GODOWN_NAME + " ASC");
        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_CM_AND_CMP_TABLE + "=? AND " + GODOWN_TABLE_ACTIVE + " =? AND " + GODOWN_TABLE_WAREHOUSE_NAME + " = ?", new String[]{cmOrCmp.trim(), "YES", warehouseName}, GODOWN_TABLE_GODOWN_NAME, null, GODOWN_TABLE_GODOWN_NAME + " ASC");
        Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_WAREHOUSE_NAME + " = ?", new String[]{warehouseName}, GODOWN_TABLE_GODOWN_NAME, null, GODOWN_TABLE_GODOWN_NAME + " ASC");
            /*
            --------- Godown
            Not Working
            select GodownID, GodownCode from CMPro_Mst_Godown Inner join CMPro_Mst_Warehouse
            On CMPro_Mst_Godown.WarehouseID = CMPro_Mst_Warehouse.WarehouseID
            where GodownCloseDate IS NULL AND CMPro_Mst_Warehouse.WarehouseID = 27

            select * from CMPro_Mst_Godown INNER JOIN CMPro_Mst_Warehouse
            ON CMPro_Mst_Godown.WarehouseID = CMPro_Mst_Warehouse.WarehouseID
            where CMPro_Mst_Godown.WarehouseID = 3356

            final String MY_QUERY = "SELECT " + GODOWN_TABLE_GODOWN_NAME + " FROM " + GODOWN_TABLE + " INNER JOIN " + WAREHOUSE_TABLE + " ON " +
                    LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE_ID + " = " + WAREHOUSE_TABLE_WAREHOUSE_ID +
                    " WHERE ( " + GODOWN_TABLE_ACTIVE + " = 'YES') AND  (" + WAREHOUSE_TABLE_WAREHOUSE_ID + " = ?) AND " + GODOWN_TABLE_CM_AND_CMP_TABLE + " = ? ";
            cursor =  db.rawQuery(MY_QUERY, new String[]{warehouse_id, cmOrCmp.trim()});
    */


        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                list.add(cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_NAME)));
            }
            while (cursor.moveToNext());


            cursor.close();
        }


        close();

        return list;
    }

    public ArrayList<String> getExchangeType() {
        ArrayList<String> list = new ArrayList<String>();

        open();
        Cursor cursor = db.query(EXCHANGE_TYPE_TABLE, new String[]{LOCAL_TABLE_EXCHANGE_TYPE_ID, LOCAL_TABLE_EXCHANGE_TYPE_NAME}, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(LOCAL_TABLE_EXCHANGE_TYPE_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();

        return list;
    }

    public ArrayList<String> getState(String segment_name) {
        ArrayList<String> list = new ArrayList<String>();

        open();

        String segment_id = "";
        Cursor cursor_segment_id = db.query(SEGMENT_TABLE, null, SEGMENT_TABLE_SEGMENT_NAME + "=? ", new String[]{segment_name}, null, null, null);
        if (cursor_segment_id != null && cursor_segment_id.getCount() > 0) {
            cursor_segment_id.moveToFirst();
            segment_id = cursor_segment_id.getString(cursor_segment_id.getColumnIndex(SEGMENT_TABLE_SEGMENT_ID));
            cursor_segment_id.close();
        }

        Cursor cursor = db.query(STATE_TABLE, null, STATE_TABLE_SEGMENT_ID + "=?", new String[]{segment_id}, STATE_TABLE_STATE_NAME, null, STATE_TABLE_STATE_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                list.add(cursor.getString(cursor.getColumnIndex(STATE_TABLE_STATE_NAME)));

            } while (cursor.moveToNext());

            cursor.close();
        }


        close();


        return list;
    }

    public String getWarehouseAddress(String name) {
        String add = "NO ADDRESS ";

        open();

        Cursor cursor = db.query(WAREHOUSE_TABLE, null, WAREHOUSE_TABLE_WAREHOUSE_NAME + "=?", new String[]{name}, null, null, null);

        Log.e("inside if database", "count = " + cursor.getCount());

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            add = cursor.getString(cursor.getColumnIndex(WAREHOUSE_TABLE_WAREHOUSE_ADDRESS));
            Log.e("inside if database", "val = " + add);
        }

        close();

        return add;
    }

    public String exchange_type_id(String exchange_type) {
        String exchange_type_id = "";
        open();

        Cursor cursor = db.query(EXCHANGE_TYPE_TABLE, null, LOCAL_TABLE_EXCHANGE_TYPE_NAME + "=?", new String[]{exchange_type}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            exchange_type_id = cursor.getString(cursor.getColumnIndex(LOCAL_TABLE_EXCHANGE_TYPE_ID));
            cursor.close();
        }
        cursor = null;

        close();

        return exchange_type_id;
    }

    public String get_state_id(String state_name, String segment_name) {

        String state_id = "";
        open();
        //STATE_TABLE_CM_AND_CMP_SEGMENT
        //STATE_TABLE_STATE_NAME + "=? AND " + STATE_TABLE_CM_AND_CMP_SEGMENT + " =?", new String[]{state_name, segment_name}

        Cursor cursor = db.query(STATE_TABLE, null, STATE_TABLE_STATE_NAME + "=? AND " + STATE_TABLE_CM_AND_CMP_SEGMENT + " =?", new String[]{state_name, segment_name}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            state_id = cursor.getString(cursor.getColumnIndex(STATE_TABLE_STATE_ID));
            cursor.close();
        }
        cursor = null;

        close();

        return state_id;
    }

    public String get_location_id(String location_name, String state_name, String segment_name) {

        String location_id = "";
        open();
        //STATE_TABLE_CM_AND_CMP_SEGMENT
        //STATE_TABLE_STATE_NAME + "=? AND " + STATE_TABLE_CM_AND_CMP_SEGMENT + " =?", new String[]{state_name, segment_name}

        Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=? AND " + LOCATION_TABLE_STATE + "=? AND " + LOCATION_TABLE_CM_AND_CMP_TABLE + " =?", new String[]{location_name, state_name, segment_name}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            location_id = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor.close();
        }
        cursor = null;

        close();

        return location_id;
    }


    public String get_commodity_id(String commodity_name) {
        String commodity_id = "";
        open();
        Cursor cursor = db.query(COMMODITY_TYPE_TABLE, null, LOCAL_TABLE_COMMODITY_TYPE_NAME + " =?", new String[]{commodity_name}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            commodity_id = cursor.getString(cursor.getColumnIndex(LOCAL_TABLE_COMMODITY_TYPE_ID));
            cursor.close();
        }
        cursor = null;

        close();

        return commodity_id;
    }


    public String get_godown_id(String godown_name, String segment_name) {
        String godown_id = "";
        open();
        //Cursor cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_GODOWN_NAME+ "=? AND " +  GODOWN_TABLE_CM_AND_CMP_TABLE + " =?", new String[]{godown_name,  segment_name}, null, null, null);
        //String Cmp =cursor.getString()
        //Log.e("GetCmp",cursor);


        String MY_QUERY = "SELECT  DISTINCT " + GODOWN_TABLE_WAREHOUSE_ID +
                " FROM " + GODOWN_TABLE +
                " WHERE " + GODOWN_TABLE_WAREHOUSE_NAME + " = '" + godown_name + "'" +
                " AND " + GODOWN_TABLE_CM_AND_CMP_TABLE + " = '" + segment_name + "'";

        Cursor cursor = db.rawQuery(MY_QUERY, null);
        Log.e("Insert Table : ", " GodownTable CMP = " + MY_QUERY);


        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            godown_id = cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_WAREHOUSE_ID));
            cursor.close();
        }
        cursor = null;

        close();

        return godown_id;
    }

    public String getaudit_type_id(String audit_type) {
        String audit_type_id = "";
        open();

        Cursor cursor = db.query(AUDIT_TYPE_TABLE, null, AUDIT_TYPE_TABLE_AUDIT_TYPE + "=?", new String[]{audit_type}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            audit_type_id = cursor.getString(cursor.getColumnIndex(AUDIT_TYPE_TABLE_AUDIT_TYPE_ID));
            cursor.close();
        }
        cursor = null;

        close();

        return audit_type_id;
    }

    public String getsegment_id(String segment) {
        String segment_id = "";
        open();

        Cursor cursor = db.query(SEGMENT_TABLE, null, SEGMENT_TABLE_SEGMENT_NAME + "=?", new String[]{segment}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            segment_id = cursor.getString(cursor.getColumnIndex(SEGMENT_TABLE_SEGMENT_ID));
            cursor.close();
        }
        cursor = null;

        close();

        return segment_id;
    }
//id, exchange_type_id, deposite_state_id, deposite_location_id, deposite_cmp_id, deposite_commodity_id, current_date,date)

    public long insert_Local_Deposit(String client_copy_no, String exchange_type_id, String is_scm_deposite, String state_id, String Transection_date,
                                     String location_id, String cmp_name, String truck_number, String commodity_name, String packing_materialkg,
                                     String packing_materialName, String wt_of_packing_material,
                                     String supplier_name, String ncml_gate_pass_no, String lorry_receipt, String no_of_bags, String wt_of_empty_gunnies, String wt_bridge_name,
                                     String wt_bridge_slip_no, String dist_of_wt_bridge_from_godown, String wt_bridge_time_id, String wt_bridge_time_out,
                                     String total_weight, String tare_wt_in_mt, String gross_wt_in_mt, String net_wt_in_mt, String average_wt_per_bag,
                                     String no_of_cut_tom_bags, String no_of_palla_bags, String moisture, String foreign_matter, String fungus, String broken, String weevilled,
                                     String live_insscets, String remark, Bitmap photo_front, Bitmap photo_reer, Bitmap photo_depositor, Bitmap photo_employee,
                                     Bitmap photo_sample_taken, String current_date) {
        String one_row_checker_no = "";
        long long_insert_row_index;
        open();
        Cursor cursor = db.query(DEPOSIT_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            one_row_checker_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSIT_ID));
            one_row_checker_no = "" + ((Integer.parseInt(one_row_checker_no) + 1));
        } else {
            one_row_checker_no = "1";
        }
        cursor = null;

        ContentValues values = new ContentValues();
        values.put(LOCAL_DEPOSIT_TABLE_DEPOSIT_ID, one_row_checker_no);
        values.put(LOCAL_DEPOSIT_TABLE_CLIENT_COPYNO, client_copy_no);
        values.put(LOCAL_DEPOSIT_TABLE_EXCHANGE_TYPEID, exchange_type_id);
        values.put(LOCAL_DEPOSIT_TABLE_IS_SCM_DEPOSIT, is_scm_deposite);
        values.put(LOCAL_DEPOSIT_TABLE_STATE_ID, state_id);
        values.put(LOCAL_DEPOSIT_TABLE_TRANSACTION_DATE, Transection_date);
        values.put(LOCAL_DEPOSIT_TABLE_LOCATION_ID, location_id);
        values.put(LOCAL_DEPOSIT_TABLE_CMPID, cmp_name);
        values.put(LOCAL_DEPOSIT_TABLE_TRUCKNO, truck_number);
        values.put(LOCAL_DEPOSIT_TABLE_COMMODITY_ID, commodity_name);
        values.put(LOCAL_DEPOSIT_TABLE_PACKING_KG, packing_materialkg);
        values.put(LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_TYPE, packing_materialName);
        values.put(LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_WEIGHT, wt_of_packing_material);
        values.put(LOCAL_DEPOSIT_TABLE_SUPPLIER_NAME, supplier_name);
        values.put(LOCAL_DEPOSIT_TABLE_NCML_GATE_PASSNO, ncml_gate_pass_no);
        values.put(LOCAL_DEPOSIT_TABLE_LR_GR_NO, lorry_receipt);
        values.put(LOCAL_DEPOSIT_TABLE_NO_OF_BAGS, no_of_bags);
        values.put(LOCAL_DEPOSIT_TABLE_EMPTY_GUNNIES_WEIGHT, wt_of_empty_gunnies);
        values.put(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_NAME, wt_bridge_name);
        values.put(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_SLIP_NO, wt_bridge_slip_no);
        values.put(LOCAL_DEPOSIT_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN, dist_of_wt_bridge_from_godown);
        values.put(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEIN, wt_bridge_time_id);
        values.put(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEOUT, wt_bridge_time_out);
        values.put(LOCAL_DEPOSIT_TABLE_TOTAL_WEIGHT, total_weight);
        values.put(LOCAL_DEPOSIT_TABLE_TARE_WEIGHT, tare_wt_in_mt);
        values.put(LOCAL_DEPOSIT_TABLE_GROSS_WEIGHT, gross_wt_in_mt);
        values.put(LOCAL_DEPOSIT_TABLE_NET_WEIGHT, net_wt_in_mt);
        values.put(LOCAL_DEPOSIT_TABLE_AVERAGE_WEIGHT, average_wt_per_bag);
        values.put(LOCAL_DEPOSIT_TABLE_NO_CUT_AND_TOM_BAGS, no_of_cut_tom_bags);
        values.put(LOCAL_DEPOSIT_TABLE_NO_PALLA_BAGS, no_of_palla_bags);
        values.put(LOCAL_DEPOSIT_TABLE_MOISTURE, moisture);
        values.put(LOCAL_DEPOSIT_TABLE_FOREIGN_MATTER, foreign_matter);
        values.put(LOCAL_DEPOSIT_TABLE_FUNGUS_DMG_DISCOLORED, fungus);
        values.put(LOCAL_DEPOSIT_TABLE_BROKEN_IMMATURE, broken);
        values.put(LOCAL_DEPOSIT_TABLE_WEEVILLED, weevilled);
        values.put(LOCAL_DEPOSIT_TABLE_LIVE_INSECTS, live_insscets);
        values.put(LOCAL_DEPOSIT_TABLE_REMARKS, remark);
        values.put(LOCAL_DEPOSIT_TABLE_TRUCK_FRONT_IMAGE_NAME, getBitmapAsByteArray(photo_front));
        values.put(LOCAL_DEPOSIT_TABLE_TRUCK_REAR_IMAGE_NAME, getBitmapAsByteArray(photo_reer));
        values.put(LOCAL_DEPOSIT_TABLE_DEPOSITOR_IMAGE_NAME, getBitmapAsByteArray(photo_depositor));
        values.put(LOCAL_DEPOSIT_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME, getBitmapAsByteArray(photo_employee));
        values.put(LOCAL_DEPOSIT_TABLE_SAMPLE_TAKEN_IMAGE_NAME, getBitmapAsByteArray(photo_sample_taken));
        values.put(LOCAL_DEPOSIT_TABLE_IS_POSTED_ON_SERVER, "0");
        values.put(LOCAL_DEPOSIT_TABLE_CREATED_BY, "1005");
        values.put(LOCAL_DEPOSIT_TABLE_CREATED_DATE, current_date);
        long_insert_row_index = db.insert(DEPOSIT_TABLE, null, values);
        close();

        return long_insert_row_index;
    }


    //Insert Withdrawal Header  -- Start

    public long insert_Local_Withdrawal(String client_copy_no, String exchange_type_id, String is_scm_withdrawal, String state_id, String Transection_date,
                                        String is_rejected_withdraw, String location_id, String cmp_name, String truck_number, String commodity_name, String packing_materialkg,
                                        String packing_materialName, String wt_of_packing_material,
                                        String supplier_name, String ncml_gate_pass_no, String lorry_receipt, String no_of_bags, String wt_of_empty_gunnies, String wt_bridge_name,
                                        String wt_bridge_slip_no, String dist_of_wt_bridge_from_godown, String wt_bridge_time_id, String wt_bridge_time_out,
                                        String total_weight, String tare_wt_in_mt, String gross_wt_in_mt, String net_wt_in_mt, String average_wt_per_bag,
                                        String no_of_cut_tom_bags, String no_of_palla_bags, String moisture, String foreign_matter, String fungus, String broken, String weevilled,
                                        String live_insscets, String remark, Bitmap photo_front, Bitmap photo_reer, Bitmap photo_withdrawal, Bitmap photo_employee,
                                        Bitmap photo_sample_taken, String current_date) {
        String one_row_checker_no = "";
        long long_insert_row_index;
        open();
        Cursor cursor = db.query(WITHDRAWAL_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            one_row_checker_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSIT_ID));
            one_row_checker_no = "" + ((Integer.parseInt(one_row_checker_no) + 1));
        } else {
            one_row_checker_no = "1";
        }
        cursor = null;

        ContentValues values = new ContentValues();
        values.put(LOCAL_WITHDRAWAL_TABLE_WITHDRAWAL_ID, one_row_checker_no);
        values.put(LOCAL_WITHDRAWAL_TABLE_CLIENT_COPYNO, client_copy_no);
        values.put(LOCAL_WITHDRAWAL_TABLE_EXCHANGE_TYPEID, exchange_type_id);
        values.put(LOCAL_WITHDRAWAL_TABLE_IS_SCM_WITHDRAWAL, is_scm_withdrawal);
        values.put(LOCAL_WITHDRAWAL_TABLE_IS_REJECTED_WITHDRAWAL, is_rejected_withdraw);
        values.put(LOCAL_WITHDRAWAL_TABLE_STATE_ID, state_id);
        values.put(LOCAL_WITHDRAWAL_TABLE_TRANSACTION_DATE, Transection_date);
        values.put(LOCAL_WITHDRAWAL_TABLE_LOCATION_ID, location_id);
        values.put(LOCAL_WITHDRAWAL_TABLE_CMPID, cmp_name);
        values.put(LOCAL_WITHDRAWAL_TABLE_TRUCKNO, truck_number);
        values.put(LOCAL_WITHDRAWAL_TABLE_COMMODITY_ID, commodity_name);
        values.put(LOCAL_WITHDRAWAL_TABLE_PACKING_KG, packing_materialkg);
        values.put(LOCAL_WITHDRAWAL_TABLE_PACKING_MATERIAL_TYPE, packing_materialName);
        values.put(LOCAL_WITHDRAWAL_TABLE_PACKING_MATERIAL_WEIGHT, wt_of_packing_material);
        values.put(LOCAL_WITHDRAWAL_TABLE_SUPPLIER_NAME, supplier_name);
        values.put(LOCAL_WITHDRAWAL_TABLE_NCML_GATE_PASSNO, ncml_gate_pass_no);
        values.put(LOCAL_WITHDRAWAL_TABLE_LR_GR_NO, lorry_receipt);
        values.put(LOCAL_WITHDRAWAL_TABLE_NO_OF_BAGS, no_of_bags);
        values.put(LOCAL_WITHDRAWAL_TABLE_EMPTY_GUNNIES_WEIGHT, wt_of_empty_gunnies);
        values.put(LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_NAME, wt_bridge_name);
        values.put(LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_SLIP_NO, wt_bridge_slip_no);
        values.put(LOCAL_WITHDRAWAL_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN, dist_of_wt_bridge_from_godown);
        values.put(LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_TIMEIN, wt_bridge_time_id);
        values.put(LOCAL_WITHDRAWAL_TABLE_WEIGH_BRIDGE_TIMEOUT, wt_bridge_time_out);
        values.put(LOCAL_WITHDRAWAL_TABLE_TOTAL_WEIGHT, total_weight);
        values.put(LOCAL_WITHDRAWAL_TABLE_TARE_WEIGHT, tare_wt_in_mt);
        values.put(LOCAL_WITHDRAWAL_TABLE_GROSS_WEIGHT, gross_wt_in_mt);
        values.put(LOCAL_WITHDRAWAL_TABLE_NET_WEIGHT, net_wt_in_mt);
        values.put(LOCAL_WITHDRAWAL_TABLE_AVERAGE_WEIGHT, average_wt_per_bag);
        values.put(LOCAL_WITHDRAWAL_TABLE_NO_CUT_AND_TOM_BAGS, no_of_cut_tom_bags);
        values.put(LOCAL_WITHDRAWAL_TABLE_NO_PALLA_BAGS, no_of_palla_bags);
        values.put(LOCAL_WITHDRAWAL_TABLE_MOISTURE, moisture);
        values.put(LOCAL_WITHDRAWAL_TABLE_FOREIGN_MATTER, foreign_matter);
        values.put(LOCAL_WITHDRAWAL_TABLE_FUNGUS_DMG_DISCOLORED, fungus);
        values.put(LOCAL_WITHDRAWAL_TABLE_BROKEN_IMMATURE, broken);
        values.put(LOCAL_WITHDRAWAL_TABLE_WEEVILLED, weevilled);
        values.put(LOCAL_WITHDRAWAL_TABLE_LIVE_INSECTS, live_insscets);
        values.put(LOCAL_WITHDRAWAL_TABLE_REMARKS, remark);
        values.put(LOCAL_WITHDRAWAL_TABLE_TRUCK_FRONT_IMAGE_NAME, getBitmapAsByteArray(photo_front));
        values.put(LOCAL_WITHDRAWAL_TABLE_TRUCK_REAR_IMAGE_NAME, getBitmapAsByteArray(photo_reer));
        values.put(LOCAL_WITHDRAWAL_TABLE_WITHDRAWALOR_IMAGE_NAME, getBitmapAsByteArray(photo_withdrawal));
        values.put(LOCAL_WITHDRAWAL_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME, getBitmapAsByteArray(photo_employee));
        values.put(LOCAL_WITHDRAWAL_TABLE_SAMPLE_TAKEN_IMAGE_NAME, getBitmapAsByteArray(photo_sample_taken));
        values.put(LOCAL_WITHDRAWAL_TABLE_IS_POSTED_ON_SERVER, "0");
        values.put(LOCAL_WITHDRAWAL_TABLE_CREATED_BY, "1005");
        values.put(LOCAL_WITHDRAWAL_TABLE_CREATED_DATE, current_date);
        long_insert_row_index = db.insert(WITHDRAWAL_TABLE, null, values);
        close();

        return long_insert_row_index;
    }


    // End

    public long insertLocalGodownAudit(String id, String segment, String auditor, String location,
                                       String godown, String entry_type, String state, String warehouse,
                                       String address, Bitmap bitmap_photo, String audit_type, String form_no, String current_date, String date) {

        long long_insert_row_index;

        open();

        String segment_id = "", one_row_checker_no = "";
        String audit_type_id = "", auditor_id = "", location_id = "", state_id = "", warehouse_id = "", godown_id = "";

        Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{location}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            location_id = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            if (segment.toUpperCase().contains("CM")) {
                state_id = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_STATE_ID));
            } else {
                state_id = cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_STATE_ID));
            }
            cursor.close();
        }
        cursor = null;

        cursor = db.query(AUDIT_TYPE_TABLE, null, AUDIT_TYPE_TABLE_AUDIT_TYPE + "=?", new String[]{audit_type}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            audit_type_id = cursor.getString(cursor.getColumnIndex(AUDIT_TYPE_TABLE_AUDIT_TYPE_ID));
            cursor.close();
        }
        cursor = null;

        cursor = db.query(WAREHOUSE_TABLE, null, WAREHOUSE_TABLE_WAREHOUSE_NAME + "=?", new String[]{warehouse}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            warehouse_id = cursor.getString(cursor.getColumnIndex(WAREHOUSE_TABLE_WAREHOUSE_ID));
            cursor.close();
        }
        cursor = null;

        cursor = db.query(AUDITOR_TABLE, null, AUDITOR_TABLE_EMP_NAME + "=?", new String[]{auditor}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            auditor_id = cursor.getString(cursor.getColumnIndex(AUDITOR_TABLE_EMP_ID));
            cursor.close();
        }
        cursor = null;

        cursor = db.query(SEGMENT_TABLE, null, SEGMENT_TABLE_SEGMENT_NAME + "=?", new String[]{segment}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            segment_id = cursor.getString(cursor.getColumnIndex(SEGMENT_TABLE_SEGMENT_ID));
            cursor.close();
        }
        cursor = null;

        cursor = db.query(GODOWN_TABLE, null, GODOWN_TABLE_GODOWN_NAME + "=?", new String[]{godown}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            godown_id = cursor.getString(cursor.getColumnIndex(GODOWN_TABLE_GODOWN_ID));
            cursor.close();
        }
        cursor = null;

        cursor = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            one_row_checker_no = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER));
            one_row_checker_no = "" + ((Integer.parseInt(one_row_checker_no) + 1));
        } else {
            one_row_checker_no = "1";
        }
        cursor = null;

        ContentValues values = new ContentValues();
        values.put(LOCAL_GODOWN_AUDIT_TABLE_AUDIT_ID, id);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_DATE, date);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_SEGMENT, segment);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_SEGMENT_ID, segment_id);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_AUDIT_TYPE, audit_type);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_AUDIT_TYPE_ID, audit_type_id);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_FORM_NO, form_no);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_AUDITOR, auditor);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_AUDITOR_ID, auditor_id);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_LOCATION, location);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_LOCATION_ID, location_id);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_GODOWN, godown);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_GODOWN_ID, godown_id);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_ENTRY_TYPE, entry_type);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_STATE, state);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_STATE_ID, state_id);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE, warehouse);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE_ID, warehouse_id);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_ADDRESS, address);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_CREATED_BY_DATE, current_date);
        //values.put(LOCAL_GODOWN_AUDIT_TABLE_CREATED_BY_DATE, getDateTimeWithHHMMSS());
        values.put(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER, one_row_checker_no);
        values.put(LOCAL_GODOWN_AUDIT_TABLE_GODOWN_PIC, getBitmapAsByteArray(bitmap_photo));

        long_insert_row_index = db.insert(LOCAL_GODOWN_AUDIT_TABLE, null, values);

        close();

        return long_insert_row_index;
    }


    public void inserLocalGodownAuditFinal(String financial, String quality, String over_ruled, String NoOfStacks, String ReasonNotStack, String NoOfSamples, String ReasonNotSamples) {
        open();
        String one_row_checker_no;
        Cursor cursor = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            one_row_checker_no = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER));
            one_row_checker_no = "" + (Integer.parseInt(one_row_checker_no));
        } else {
            one_row_checker_no = "1";
        }

        cursor = null;
        ContentValues values = new ContentValues();
        values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_FINANCIAL_REMARK, financial);
        values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_QUALITY_REMARK, quality);
        values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_OVER_RULED_REMARK, over_ruled);
        values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_ONE_ROW_CHECKER, one_row_checker_no);
        values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_NO_OF_STACKS, NoOfStacks);
        values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_REASON_NOT_STACK, ReasonNotStack);
        values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_NO_OF_SAMPLES, NoOfSamples);
        values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_REASON_NOT_SAMPLES, ReasonNotSamples);

        db.insert(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE, null, values);
        close();
    }

    public ArrayList<AuditPoinDetailsPojo> getAuditpoint(String segment_name, String audit_type_name) {
        open();
        ArrayList<AuditPoinDetailsPojo> list = new ArrayList<AuditPoinDetailsPojo>();
        AuditPoinDetailsPojo pojo = null;

        String segment_id = "", audit_type_id = "";

        Cursor cursor_seg_id = db.query(SEGMENT_TABLE, null, SEGMENT_TABLE_SEGMENT_NAME + "=?", new String[]{segment_name}, null, null, null);
        if (cursor_seg_id != null && cursor_seg_id.getCount() > 0) {
            cursor_seg_id.moveToFirst();
            segment_id = cursor_seg_id.getString(cursor_seg_id.getColumnIndex(SEGMENT_TABLE_SEGMENT_ID));
            cursor_seg_id.close();
        }

        Cursor cursor_audit_type_id = db.query(AUDIT_TYPE_TABLE, null, AUDIT_TYPE_TABLE_AUDIT_TYPE + "=?", new String[]{audit_type_name}, null, null, null);
        if (cursor_audit_type_id != null && cursor_audit_type_id.getCount() > 0) {
            cursor_audit_type_id.moveToFirst();
            audit_type_id = cursor_audit_type_id.getString(cursor_audit_type_id.getColumnIndex(AUDIT_TYPE_TABLE_AUDIT_TYPE_ID));
            cursor_audit_type_id.close();
        }

        Cursor cursor = db.query(AUDIT_POINT_TABLE, null, AUDIT_POINT_TABLE_SEGMENT_ID + "=? AND " + AUDIT_POINT_TABLE_AUDIT_TYPE_ID + "=?", new String[]{segment_id, audit_type_id}, AUDIT_POINT_TABLE_AUDIT_POINT, null, AUDIT_POINT_TABLE_AUDIT_SERIAL_NO + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                pojo = new AuditPoinDetailsPojo();

                pojo.setAudit_point(cursor.getString(cursor.getColumnIndex(AUDIT_POINT_TABLE_AUDIT_POINT)));
                pojo.setGap_flag(cursor.getString(cursor.getColumnIndex(AUDIT_POINT_TABLE_AUDIT_GAP_FLAG)));

                list.add(pojo);
                pojo = null;
            } while (cursor.moveToNext());
        }

        cursor.close();

        close();

        return list;
    }


    public long insertLocalAuditPointDetails(ArrayList<AuditPoinDetailsPojo> list, String string_audit_type_id_intent, String string_segment_id_intent) {

        open();
        AuditPoinDetailsPojo pojo = null;
        ContentValues values = null;

        String remark = "";

        String one_row_checker_no;

        Cursor cursor_one_row = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, null, null, null, null, null);
        if (cursor_one_row != null && cursor_one_row.getCount() > 0) {
            cursor_one_row.moveToLast();
            one_row_checker_no = cursor_one_row.getString(cursor_one_row.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER));
            one_row_checker_no = "" + (Integer.parseInt(one_row_checker_no));
        } else {
            one_row_checker_no = "1";
        }


        cursor_one_row = null;


        for (int i = 0; i < list.size(); i++) {

            values = new ContentValues();
            pojo = list.get(i);


            Log.e("pojo", "pojo = " + pojo.getRemark().toString());

            remark = pojo.getRemark();

            String point_id = "";

            Cursor cursor = db.query(AUDIT_POINT_TABLE, null, AUDIT_POINT_TABLE_AUDIT_POINT + "=? AND " + AUDIT_POINT_TABLE_AUDIT_TYPE_ID + "=? AND " + AUDIT_POINT_TABLE_SEGMENT_ID + "=?", new String[]{pojo.getAudit_point(), string_audit_type_id_intent, string_segment_id_intent}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                point_id = cursor.getString(cursor.getColumnIndex(AUDIT_POINT_TABLE_AUDIT_POINT_ID));
                cursor.close();
            }

            if (!(remark.length() > 0)) {
                remark = "nil";
            }

            values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_AUDIT_POINT, pojo.getAudit_point());
            values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_AUDIT_POINT_ID, point_id);
            values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_REMARK, remark);
            values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_RESPONSE, pojo.getRespones_yes_no());
            values.put(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_ONE_ROW_CHECKER, one_row_checker_no);

            db.insert(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE, null, values);

            values = null;
            pojo = null;
        }


       /* Cursor cursor = db.query(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            do {
                Log.v("insert remark", " remark = " + cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_REMARK)));
            } while (cursor.moveToNext());


        }*/

        close();

        return Long.parseLong(one_row_checker_no);

    }


    public String getAttendanceAllDataInJson1(Context context) {
        String jsonString = "[]";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        String row_id = "";
        open();

        Cursor cursor = db.query(LOCAL_ATTENDANCE_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();


            jsonObject = new JSONObject();


            row_id = cursor.getString(cursor.getColumnIndex(COMMON_ID));
            String emp_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_EMP_ID));
            String emp_name = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_NAME));
            String segment_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_SEGMENT_ID));
            String date = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_DATE_TIME));
            String location_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LOCATION_ID));
            String godown_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_GODOWN_ID));
            String lat = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LATTITUTE));
            String lang = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LONGITUTE));

            // If local_address is null
            String local_address = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LOCAL_ADDRESS));

            if (local_address == null) {
                double dlat = Double.parseDouble(lat);
                double dlang = Double.parseDouble(lang);
                do {
                    setLocationNameAndState(dlat, dlang, context);
                    local_address = local_addressTemp;
                }
                while (local_address == null);
            }

            String on_behalf_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_ON_BEHALF_ID));
            String remark = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_REMARKS));
            byte[] bytes_pic = cursor.getBlob(cursor.getColumnIndex(LOCAL_ATTENDANCE_GODOWN_PIC));
            String godown_pic = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);

            if (remark.length() <= 0) {
                remark = "null";
            }

            if (godown_id.length() <= 0) {
                godown_id = "null";
            }

            try {

                jsonObject.put("Emp_id", emp_id);
                jsonObject.put("Emp_Name", emp_name);
                jsonObject.put("segment_id", segment_id);
                jsonObject.put("date", date);
                jsonObject.put("Location_id", location_id);
                jsonObject.put("Godown_id", godown_id);
                jsonObject.put("latitude", lat);
                jsonObject.put("longitude", lang);
                jsonObject.put("createdby", emp_id);
                jsonObject.put("createddate", date);
                jsonObject.put("on_behalf_id", on_behalf_id);
                jsonObject.put("remark", remark);
                jsonObject.put("local_address", local_address);
                jsonObject.put("godown_pic", godown_pic);

                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        close();

        jsonString = jsonArray.toString();


        return row_id + "," + jsonString;
    }

    public String getAttendanceAllDataInJson(Context context) {
        String jsonString = "[]";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        String row_id = "";
        open();

        Cursor cursor = db.query(LOCAL_ATTENDANCE_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            jsonObject = new JSONObject();

            row_id = cursor.getString(cursor.getColumnIndex(COMMON_ID));
            String emp_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_EMP_ID));
            String emp_name = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_NAME));
            String segment_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_SEGMENT_ID));
            String date = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_DATE_TIME));
            String location_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LOCATION_ID));
            String godown_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_GODOWN_ID));
            String lat = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LATTITUTE));
            String lang = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LONGITUTE));

            // If local_address is null
            String local_address = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LOCAL_ADDRESS));

            if (local_address == null) {
                double dlat = Double.parseDouble(lat);
                double dlang = Double.parseDouble(lang);
                do {
                    setLocationNameAndState(dlat, dlang, context);
                    local_address = local_addressTemp;
                }
                while (local_address == null);
            }

            String on_behalf_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_ON_BEHALF_ID));
            String remark = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_REMARKS));
            byte[] bytes_pic = cursor.getBlob(cursor.getColumnIndex(LOCAL_ATTENDANCE_GODOWN_PIC));
            String godown_pic = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);


            if (remark.length() <= 0) {
                remark = "null";
            }

            if (godown_id.length() <= 0) {
                godown_id = "null";
            }

            try {

                jsonObject.put("Emp_id", emp_id);
                jsonObject.put("Emp_Name", emp_name);
                jsonObject.put("segment_id", segment_id);
                jsonObject.put("date", date);
                jsonObject.put("Location_id", location_id);
                jsonObject.put("Godown_id", godown_id);
                jsonObject.put("latitude", lat);
                jsonObject.put("longitude", lang);
                jsonObject.put("createdby", emp_id);
                jsonObject.put("createddate", date);
                jsonObject.put("on_behalf_id", on_behalf_id);
                jsonObject.put("remark", remark);
                jsonObject.put("local_address", local_address);
                jsonObject.put("godown_pic", godown_pic);

                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        close();

        jsonString = jsonArray.toString();


        return row_id + "," + jsonString;
    }

    //Original
    public String getAttendanceAllDataInJson() {
        String jsonString = "[]";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        String row_id = "";
        open();

        Cursor cursor = db.query(LOCAL_ATTENDANCE_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();


            jsonObject = new JSONObject();


            row_id = cursor.getString(cursor.getColumnIndex(COMMON_ID));
            String emp_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_EMP_ID));
            String emp_name = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_NAME));
            String segment_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_SEGMENT_ID));
            String date = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_DATE_TIME));
            String location_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LOCATION_ID));
            String godown_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_GODOWN_ID));
            String lat = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LATTITUTE));
            String lang = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LONGITUTE));
            String local_address = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_LOCAL_ADDRESS));
            String on_behalf_id = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_ON_BEHALF_ID));
            String remark = cursor.getString(cursor.getColumnIndex(LOCAL_ATTENDANCE_REMARKS));
            byte[] bytes_pic = cursor.getBlob(cursor.getColumnIndex(LOCAL_ATTENDANCE_GODOWN_PIC));
            String godown_pic = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);

            if (remark.length() <= 0) {
                remark = "null";
            }

            if (godown_id.length() <= 0) {
                godown_id = "null";
            }

            try {

                jsonObject.put("Emp_id", emp_id);
                jsonObject.put("Emp_Name", emp_name);
                jsonObject.put("segment_id", segment_id);
                jsonObject.put("date", date);
                jsonObject.put("Location_id", location_id);
                jsonObject.put("Godown_id", godown_id);
                jsonObject.put("latitude", lat);
                jsonObject.put("longitude", lang);
                jsonObject.put("createdby", emp_id);
                jsonObject.put("createddate", date);
                jsonObject.put("on_behalf_id", on_behalf_id);
                jsonObject.put("remark", remark);
                jsonObject.put("local_address", local_address);
                jsonObject.put("godown_pic", godown_pic);

                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        close();

        jsonString = jsonArray.toString();


        return row_id + "," + jsonString;
    }

    ///Start for Deposit Activity


    /// End for Deposit Activity


    // Start of DEpositNext Activity

    public String getDepositNextAllDataInJson(Context context) {
        String jsonString = "[]";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        String row_id = "";
        open();

        Cursor cursor = db.query(DEPOSIT_TABLE_DETAILS, null, null, null, null, null, LOCAL_DEPOSIT_DETAILS_TABLE_ID + " DESC");

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            jsonObject = new JSONObject();

            row_id = cursor.getString(cursor.getColumnIndex(COMMON_ID));
            String DepositDetails = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_ID));
            String DepositId = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID));
            String GodownId = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID));
            String StackNo = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO));
            String LotNo = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO));
            String NoOfBags = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS));
            String Qty = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_QTY));
            String ClientId = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_ID));
            String ClientName = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_NAME));
            String DepositoryID = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_ID));
            String DepositoryName = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_NAME));
            String created_by = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_BY));
            String created_date = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_DATE));

            try {
                jsonObject.put("DepositDetails", DepositDetails);
                jsonObject.put("DepositId", DepositId);
                jsonObject.put("GodownId", GodownId);
                jsonObject.put("StackNo", StackNo);
                jsonObject.put("LotNo", LotNo);
                jsonObject.put("NoOfBags", NoOfBags);
                jsonObject.put("Qty", Qty);
                jsonObject.put("ClientId", ClientId);
                jsonObject.put("ClientName", ClientName);
                jsonObject.put("DepositoryID", DepositoryID);
                jsonObject.put("DepositoryName", DepositoryName);

                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        close();

        jsonString = jsonArray.toString();
        // ------>>>>> log by Surya
        Log.e("Activity Json sender", jsonString);
        return row_id + "," + jsonString;
    }

    //End of DepositNext Activity


    //Both(deposite Header and Detail) Are combined Start


 /*   public String getLocalAuditPointDetailsAllDataInJson()
    {

        String jsonString = "[]";
        String row_index;
        String emp_id = "";
        JSONArray jsonArray = new JSONArray();

        String NoOfStacks = "";
        String ReasonNotStack = "";
        String NoOfSamples = "";
        String ReasonNotSamples = "";

        String financial_remark = "";
        String quality_remark = "";
        String over_ruled_remark = "";

        String audit_point_id_details = "";
        String response_details = "";
        String remark_details = "";


        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray_details = new JSONArray();
        JSONObject jsonObject_details = new JSONObject();

        String row_index_to_delete = "";

        open();

        Cursor cursor_emp_id = db.query(LOGIN_TABLE, null, null, null, null, null, null);
        if (cursor_emp_id != null && cursor_emp_id.getCount() > 0)
        {
            cursor_emp_id.moveToFirst();
           // emp_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_EMPLOYEE_ID));
            cursor_emp_id.close();
        }
        Cursor cursor_audit = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, null, null, null, null, null, null);

        if (cursor_audit != null && cursor_audit.getCount() > 0) {

            cursor_audit.moveToFirst();


           // row_index = cursor_audit.getString(cursor_audit.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER));
           // DEPOSIT_TABLE, null, null, null, null, null, LOCAL_DEPOSIT_TABLE_DEPOSIT_ID
            Cursor cursor = db.query(DEPOSIT_TABLE, null, null ,null, null, null, LOCAL_DEPOSIT_TABLE_DEPOSIT_ID+" DESC");

            cursor.moveToFirst();

            row_index_to_delete = cursor.getString(cursor.getColumnIndex(COMMON_ID));
            String deposit_depositid= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSIT_ID));
            String deposit_client_copyno= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CLIENT_COPYNO));
            String deposit_exchange_type_id= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EXCHANGE_TYPEID));
            String state_id  = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_STATE_ID));
            String Is_scm_deposite= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_IS_SCM_DEPOSIT));
            String transaction_date = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRANSACTION_DATE));
            String location_table_id= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LOCATION_ID));
            String cmp_id= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CMPID));
            String truck_no= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCKNO));
            String commodity_id  = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_COMMODITY_ID));
            String packing_in_kg= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_PACKING_KG));
            String packing_type = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_TYPE));
            String supplier_name= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_SUPPLIER_NAME));
            String gate_pass_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NCML_GATE_PASSNO));
            String deposit_lr_gr_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LR_GR_NO));
            String deposit_no_of_bags= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_OF_BAGS));
            String deposit_empty_gunnies_weight= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EMPTY_GUNNIES_WEIGHT));
            String deposit_weigh_bridge_name = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_NAME));
            String deposit_weigh_bridge_slip_no= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_SLIP_NO));
            String deposit_distance_weigh_bridge_from_godown= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN));
            String deposit_weigh_bridge_time_in = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEIN));
            String deposit_weigh_bridge_time_out= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEOUT));
            String deposit_total_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TOTAL_WEIGHT));
            String deposit_tare_weigh= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TARE_WEIGHT));
            String deposit_gross_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_GROSS_WEIGHT));
            String deposit_net_weight  = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NET_WEIGHT));
            String deposit_average_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_AVERAGE_WEIGHT));
            String deposit_no_cut_and_tom_bags= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_CUT_AND_TOM_BAGS));
            String deposit_no_palla_bags= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_PALLA_BAGS));
            String deposit_moisture= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_MOISTURE));
            String deposit_foreign_matter = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_FOREIGN_MATTER));
            String deposit_dmg_disclosured= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_FUNGUS_DMG_DISCOLORED));
            String deposit_broken_immature= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_BROKEN_IMMATURE));
            String deposit_weevilled= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEEVILLED));
            String deposit_live_insects= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LIVE_INSECTS));
            String deposit_deposit_remakrs= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_REMARKS));
            String deposit_created_by = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CREATED_BY));
            String deposit_created_date= cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CREATED_DATE));


            deposit_created_by = emp_id;

            byte[] bytes_pic1 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCK_FRONT_IMAGE_NAME));
            String deposit_truck_front_image_name = Base64.encodeToString(bytes_pic1, Base64.NO_WRAP);

            byte[] bytes_pic2 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCK_REAR_IMAGE_NAME));
            String deposit_truck_rear_image_name= Base64.encodeToString(bytes_pic2, Base64.NO_WRAP);

            byte[] bytes_pic3 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSITOR_IMAGE_NAME));
            String deposit_depositor_image_name  = Base64.encodeToString(bytes_pic3, Base64.NO_WRAP);

            byte[] bytes_pic4 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME));
            String deposit_emp_accep_goods_image_name= Base64.encodeToString(bytes_pic4, Base64.NO_WRAP);

            byte[] bytes_pic5 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_SAMPLE_TAKEN_IMAGE_NAME));
            String deposit_sample_taken_image_name= Base64.encodeToString(bytes_pic5, Base64.NO_WRAP);


            Cursor cursor_final = db.query(DEPOSIT_TABLE_DETAILS, null, null ,null, null, null, LOCAL_DEPOSIT_TABLE_DEPOSIT_ID+" DESC");

            if (cursor_audit != null && cursor_final.getCount() > 0)
             {
                cursor_final.moveToFirst();

                String DepositDetails = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_ID));
                String DepositId = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID));
                String GodownId = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID));
                String StackNo = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO));
                String LotNo = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO));
                String NoOfBags= cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS));
                String Qty = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_QTY));
                String ClientId= cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_ID));
                String ClientName= cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_NAME));
                String DepositoryID= cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_ID));
                String DepositoryName = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_NAME));
                String created_by= cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_BY));
                String created_date= cursor_final.getString(cursor_final.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_DATE));

             }
            jsonObject = new JSONObject();

            try {

                jsonObject.put("Deposit_depositid", deposit_depositid );
                jsonObject.put("Deposit_client_copyno", deposit_client_copyno);
                jsonObject.put("Deposit_exchange_type_id", deposit_exchange_type_id);
                jsonObject.put("State_id", state_id);
                jsonObject.put("Deposit_is_scm_deposit", Is_scm_deposite);
                jsonObject.put("Deposit_transaction_date", transaction_date);
                jsonObject.put("Deposit_location_id", location_table_id);
                jsonObject.put("Deposit_cmp_id", cmp_id);
                jsonObject.put("Deposit_truck_no", truck_no);
                jsonObject.put("Deposit_commodity_id", commodity_id);
                jsonObject.put("Deposit_packing", packing_in_kg);
                jsonObject.put("packing_material_type", packing_type);
                jsonObject.put("Deposit_packing_material_weight", material_weight);
                jsonObject.put("Deposit_supplier_name", supplier_name);
                jsonObject.put("Deposit_ncml_gate_passno", gate_pass_no);
                jsonObject.put("Deposit_lr_gr_no", deposit_lr_gr_no);
                jsonObject.put("Deposit_no_of_bags", deposit_no_of_bags);
                jsonObject.put("Deposit_empty_gunnies_weight", deposit_empty_gunnies_weight);
                jsonObject.put("Deposit_weigh_bridge_name", deposit_weigh_bridge_name);
                jsonObject.put("Deposit_weigh_bridge_slip_no", deposit_weigh_bridge_slip_no);
                jsonObject.put("Deposit_distance_weigh_bridge_from_godown", deposit_distance_weigh_bridge_from_godown);
                jsonObject.put("Deposit_weigh_bridge_time_in", deposit_weigh_bridge_time_in);
                jsonObject.put("Deposit_weigh_bridge_time_out", deposit_weigh_bridge_time_out);
                jsonObject.put("Deposit_total_weight", deposit_total_weight);
                jsonObject.put("Deposit_tare_weigh", deposit_tare_weigh);
                jsonObject.put("Deposit_gross_weight", deposit_gross_weight);
                jsonObject.put("Deposit_net_weight", deposit_net_weight);
                jsonObject.put("Deposit_average_weight", deposit_average_weight);
                jsonObject.put("Deposit_no_cut_and_tom_bags", deposit_no_cut_and_tom_bags);
                jsonObject.put("Deposit_no_palla_bags", deposit_no_palla_bags);
                jsonObject.put("Deposit_moisture", deposit_moisture);
                jsonObject.put("Deposit_foreign_matter", deposit_foreign_matter);
                jsonObject.put("Deposit_dmg_disclosured", deposit_dmg_disclosured);
                jsonObject.put("Deposit_broken_immature", deposit_broken_immature);
                jsonObject.put("Deposit_weevilled", deposit_weevilled);
                jsonObject.put("Deposit_live_insects", deposit_live_insects);
                jsonObject.put("Deposit_deposit_remakrs", deposit_deposit_remakrs);
                jsonObject.put("Deposit_created_by", deposit_created_by);
                jsonObject.put("Deposit_created_date", deposit_created_date);
                jsonObject.put("Deposit_truck_front_image_name", "");
                jsonObject.put("Deposit_truck_rear_image_name", "");
                jsonObject.put("Deposit_depositor_image_name", "");
                jsonObject.put("Deposit_sample_taken_image_name", "");
                jsonObject.put("Deposit_emp_accep_goods_image_name", "");
                jsonObject.put("Deposit_Details", DepositDetails);
                jsonObject.put("DepositId",DepositId);
                jsonObject.put("GodownId", GodownId);
                jsonObject.put("StackNo", StackNo);
                jsonObject.put("LotNo", LotNo);
                jsonObject.put("Qty", Qty);
                jsonObject.put("NoOfBags", NoOfBags);
                jsonObject.put("ClientId", ClientId);
                jsonObject.put("ClientName", ClientName);
                jsonObject.put("DepositoryID", DepositoryID);
                jsonObject.put("DepositoryName", DepositoryName);

                Cursor cursor_details = db.query(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE, null, LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_ONE_ROW_CHECKER + "=?", new String[]{row_index}, null, null, null);

                jsonArray_details = new JSONArray();


                if (cursor_details.getCount() > 0) {
                    cursor_details.moveToFirst();

                    do {
                        audit_point_id_details = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_AUDIT_POINT_ID));
                        response_details = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_RESPONSE));
                        remark_details = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_REMARK));


                        Log.e("remark_details", "remark_details = " + remark_details);


                        jsonObject_details = new JSONObject();


                        if (remark_details.trim().length() > 0 && remark_details.contains("nil")) {
                            remark_details = " ";
                        } else {
                            //remark_details = "null";
                        }


                        try {

                            jsonObject_details.put("remark", remark_details);
                            jsonObject_details.put("audit_point", audit_point_id_details);
                            jsonObject_details.put("respones", response_details);

                            jsonArray_details.put(jsonObject_details);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } while (cursor_details.moveToNext());

                    cursor_details.close();

                }


                jsonObject.put("godown_details", jsonArray_details);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonObject);

            cursor_final.close();
            cursor.close();


            jsonString = jsonArray.toString();

        }

        cursor_audit.close();
        cursor_emp_id.close();


        close();

        Log.e("dbhelper", "json string = " + jsonString.toString().trim());


        return row_index_to_delete + "," + jsonString.toString().trim();
    }

*/

    //Both(deposite Header and Detail) Are combined End


    public String getGeoTagMappingAllDataInJson() {
        String jsonString = "[]";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        open();

        Cursor cursor = db.query(LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                jsonObject = new JSONObject();

                String emp_id = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_GEO_TAG_TABLE_EMP_ID));
                String segment_id = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_GEO_TAG_TABLE_SEGMENT_ID));
                String state_id = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_GEO_TAG_TABLE_STATE_ID));
                String location_id = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_GEO_TAG_TABLE_LOCATION_ID));
                String godown_id = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_GEO_TAG_TABLE_GODOWN_ID));
                String lat = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_GEO_TAG_TABLE_LATTITUTE));
                String lang = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_GEO_TAG_TABLE_LONGITUTE));
                String create_date = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_GEO_TAG_TABLE_CURRENT_DATE));


                try {

                    jsonObject.put("Emp_id", emp_id);

                    jsonObject.put("segment_id", segment_id);
                    jsonObject.put("state_id", state_id);

                    jsonObject.put("Location_id", location_id);
                    jsonObject.put("Godown_id", godown_id);
                    jsonObject.put("latitude", lat);
                    jsonObject.put("longitude", lang);

                    jsonObject.put("createdby", emp_id);
                    jsonObject.put("createddate", create_date);


                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }

        close();

        jsonString = jsonArray.toString();


        return jsonString;
    }


    public String getDepositAllDataInJson() {

        String jsonString = "[]";
        String emp_id = "";
        String row_index_to_delete = "";
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray_details = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject_details = new JSONObject();


        String row_id = "";
        open();

        Cursor cursor_emp_id = db.query(LOGIN_TABLE, null, null, null, null, null, null);
        if (cursor_emp_id != null && cursor_emp_id.getCount() > 0) {
            cursor_emp_id.moveToFirst();
            emp_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_EMPLOYEE_ID));
            cursor_emp_id.close();
        }

        Cursor cursor = db.query(DEPOSIT_TABLE, null, null, null, null, null, LOCAL_DEPOSIT_TABLE_DEPOSIT_ID + " DESC");

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            //String row_index = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER));
            row_index_to_delete = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSIT_ID));
            Log.e("Row index Delete", row_index_to_delete);

            //Cursor cursor_audit_child = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER + "=?", new String[]{row_index}, null, null, null);

            String deposit_depositid = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSIT_ID));
            String deposit_client_copyno = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CLIENT_COPYNO));
            String deposit_exchange_type_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EXCHANGE_TYPEID));
            String state_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_STATE_ID));
            String Is_scm_deposite = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_IS_SCM_DEPOSIT));
            String transaction_date = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRANSACTION_DATE));
            String location_table_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LOCATION_ID));
            String cmp_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CMPID));
            String truck_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCKNO));
            String commodity_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_COMMODITY_ID));
            String packing_in_kg = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_PACKING_KG));
            String packing_type = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_TYPE));
            String material_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_WEIGHT));
            String supplier_name = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_SUPPLIER_NAME));
            String gate_pass_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NCML_GATE_PASSNO));
            String deposit_lr_gr_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LR_GR_NO));
            String deposit_no_of_bags = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_OF_BAGS));
            String deposit_empty_gunnies_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EMPTY_GUNNIES_WEIGHT));
            String deposit_weigh_bridge_name = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_NAME));
            String deposit_weigh_bridge_slip_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_SLIP_NO));
            String deposit_distance_weigh_bridge_from_godown = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN));
            String deposit_weigh_bridge_time_in = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEIN));
            String deposit_weigh_bridge_time_out = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEOUT));
            String deposit_total_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TOTAL_WEIGHT));
            String deposit_tare_weigh = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TARE_WEIGHT));
            String deposit_gross_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_GROSS_WEIGHT));
            String deposit_net_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NET_WEIGHT));
            String deposit_average_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_AVERAGE_WEIGHT));
            String deposit_no_cut_and_tom_bags = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_CUT_AND_TOM_BAGS));
            String deposit_no_palla_bags = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_PALLA_BAGS));
            String deposit_moisture = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_MOISTURE));
            String deposit_foreign_matter = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_FOREIGN_MATTER));
            String deposit_dmg_disclosured = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_FUNGUS_DMG_DISCOLORED));
            String deposit_broken_immature = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_BROKEN_IMMATURE));
            String deposit_weevilled = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEEVILLED));
            String deposit_live_insects = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LIVE_INSECTS));
            String deposit_deposit_remakrs = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_REMARKS));
            String deposit_created_date = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CREATED_DATE));

            byte[] bytes_pic1 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCK_FRONT_IMAGE_NAME));
            String deposit_truck_front_image_name = Base64.encodeToString(bytes_pic1, Base64.NO_WRAP);

            byte[] bytes_pic2 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCK_REAR_IMAGE_NAME));
            String deposit_truck_rear_image_name = Base64.encodeToString(bytes_pic2, Base64.NO_WRAP);

            byte[] bytes_pic3 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSITOR_IMAGE_NAME));
            String deposit_depositor_image_name = Base64.encodeToString(bytes_pic3, Base64.NO_WRAP);

            byte[] bytes_pic4 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME));
            String deposit_emp_accep_goods_image_name = Base64.encodeToString(bytes_pic4, Base64.NO_WRAP);

            byte[] bytes_pic5 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_SAMPLE_TAKEN_IMAGE_NAME));
            String deposit_sample_taken_image_name = Base64.encodeToString(bytes_pic5, Base64.NO_WRAP);

            jsonObject = new JSONObject();

            try {

                jsonObject.put("Deposit_truck_front_image_name", deposit_truck_front_image_name);
                jsonObject.put("Deposit_truck_rear_image_name", deposit_truck_rear_image_name);
                jsonObject.put("Deposit_depositor_image_name", deposit_depositor_image_name);
                jsonObject.put("Deposit_sample_taken_image_name", deposit_emp_accep_goods_image_name);
                jsonObject.put("Deposit_emp_accep_goods_image_name", deposit_sample_taken_image_name);

                jsonObject.put("Deposit_depositid", deposit_depositid);
                jsonObject.put("Deposit_client_copyno", deposit_client_copyno);
                jsonObject.put("Deposit_exchange_type_id", deposit_exchange_type_id);
                jsonObject.put("State_id", state_id);
                jsonObject.put("Deposit_is_scm_deposit", Is_scm_deposite);
                jsonObject.put("Deposit_transaction_date", transaction_date);
                jsonObject.put("Deposit_location_id", location_table_id);
                jsonObject.put("Deposit_cmp_id", cmp_id);
                jsonObject.put("Deposit_truck_no", truck_no);
                jsonObject.put("Deposit_commodity_id", commodity_id);
                jsonObject.put("Deposit_packing", packing_in_kg);
                jsonObject.put("packing_material_type", packing_type);
                jsonObject.put("Deposit_packing_material_weight", material_weight);
                jsonObject.put("Deposit_supplier_name", supplier_name);
                jsonObject.put("Deposit_ncml_gate_passno", gate_pass_no);
                jsonObject.put("Deposit_lr_gr_no", deposit_lr_gr_no);
                jsonObject.put("Deposit_no_of_bags", deposit_no_of_bags);
                jsonObject.put("Deposit_empty_gunnies_weight", deposit_empty_gunnies_weight);
                jsonObject.put("Deposit_weigh_bridge_name", deposit_weigh_bridge_name);
                jsonObject.put("Deposit_weigh_bridge_slip_no", deposit_weigh_bridge_slip_no);
                jsonObject.put("Deposit_distance_weigh_bridge_from_godown", deposit_distance_weigh_bridge_from_godown);
                jsonObject.put("Deposit_weigh_bridge_time_in", deposit_weigh_bridge_time_in);
                jsonObject.put("Deposit_weigh_bridge_time_out", deposit_weigh_bridge_time_out);
                jsonObject.put("Deposit_total_weight", deposit_total_weight);
                jsonObject.put("Deposit_tare_weigh", deposit_tare_weigh);
                jsonObject.put("Deposit_gross_weight", deposit_gross_weight);
                jsonObject.put("Deposit_net_weight", deposit_net_weight);
                jsonObject.put("Deposit_average_weight", deposit_average_weight);
                jsonObject.put("Deposit_no_cut_and_tom_bags", deposit_no_cut_and_tom_bags);
                jsonObject.put("Deposit_no_palla_bags", deposit_no_palla_bags);
                jsonObject.put("Deposit_moisture", deposit_moisture);
                jsonObject.put("Deposit_foreign_matter", deposit_foreign_matter);
                jsonObject.put("Deposit_dmg_disclosured", deposit_dmg_disclosured);
                jsonObject.put("Deposit_broken_immature", deposit_broken_immature);
                jsonObject.put("Deposit_weevilled", deposit_weevilled);
                jsonObject.put("Deposit_live_insects", deposit_live_insects);
                jsonObject.put("Deposit_deposit_remakrs", deposit_deposit_remakrs);
                jsonObject.put("Deposit_created_by", emp_id);
                jsonObject.put("Deposit_created_date", deposit_created_date);

              /*  jsonObject.put("Deposit_truck_front_image_name","");
                jsonObject.put("Deposit_truck_rear_image_name", "");
                jsonObject.put("Deposit_depositor_image_name","");
                jsonObject.put("Deposit_sample_taken_image_name","");
                jsonObject.put("Deposit_emp_accep_goods_image_name", "");*/

                Cursor cursor_details = db.query(DEPOSIT_TABLE_DETAILS, null, LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + "=?", new String[]{deposit_depositid}, null, null, null, null);

                jsonArray_details = new JSONArray();

                if (cursor_details.getCount() > 0) {
                    String GetCountTotal = String.valueOf(cursor_details.getCount());
                    Log.e("Details No rows", GetCountTotal);

                    cursor_details.moveToFirst();

                    do {
                        row_id = cursor_details.getString(cursor.getColumnIndex(COMMON_ID));
                        String DepositDetails = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_ID));
                        String DepositId = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID));
                        String GodownId = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID));
                        String StackNo = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO));
                        String LotNo = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO));
                        String NoOfBags = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS));
                        String Qty = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_QTY));
                        String ClientId = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_ID));
                        String ClientName = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_NAME));
                        String DepositoryID = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_ID));
                        String DepositoryName = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_NAME));
                        String created_by = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_BY));
                        String created_date = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_DATE));

                        jsonObject_details = new JSONObject();

                        try {
                            jsonObject_details.put("DepositDetails", DepositDetails);
                            jsonObject_details.put("DepositId", DepositId);
                            jsonObject_details.put("GodownId", GodownId);
                            jsonObject_details.put("StackNo", StackNo);
                            jsonObject_details.put("LotNo", LotNo);
                            jsonObject_details.put("NoOfBags", NoOfBags);
                            jsonObject_details.put("Qty", Qty);
                            jsonObject_details.put("ClientId", ClientId);
                            jsonObject_details.put("ClientName", ClientName);
                            jsonObject_details.put("DepositoryID", DepositoryID);
                            jsonObject_details.put("DepositoryName", DepositoryName);
                            jsonObject_details.put("datatransfer", "0");

                            jsonArray_details.put(jsonObject_details);
                            Log.e("json array string", "json details array string = " + jsonArray_details.toString().trim());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } while (cursor_details.moveToNext());
                    cursor_details.close();
                }

                jsonObject.put("deposit_details", jsonArray_details);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            cursor.close();
            jsonString = jsonArray.toString();
        }
        close();
        Log.e("dbhelper", "json string = " + jsonString.toString().trim());
        return row_index_to_delete + "," + jsonString.toString().trim();
    }


    public String getDepositAllDataInJson1(Context context) {

        String jsonString = "[]";
        String emp_id = "";
        String row_index_to_delete = "";
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray_details = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject_details = new JSONObject();


        String row_id = "";
        open();

        Cursor cursor_emp_id = db.query(LOGIN_TABLE, null, null, null, null, null, null);
        if (cursor_emp_id != null && cursor_emp_id.getCount() > 0) {
            cursor_emp_id.moveToFirst();
            emp_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_EMPLOYEE_ID));
            cursor_emp_id.close();
        }

        Cursor cursor = db.query(DEPOSIT_TABLE, null, null, null, null, null, LOCAL_DEPOSIT_TABLE_DEPOSIT_ID + " DESC");

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            //String row_index = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER));
            row_index_to_delete = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSIT_ID));
            Log.e("Row index Delete", row_index_to_delete);

            //Cursor cursor_audit_child = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER + "=?", new String[]{row_index}, null, null, null);

            String deposit_depositid = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSIT_ID));
            String deposit_client_copyno = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CLIENT_COPYNO));
            String deposit_exchange_type_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EXCHANGE_TYPEID));
            String state_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_STATE_ID));
            String Is_scm_deposite = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_IS_SCM_DEPOSIT));
            String transaction_date = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRANSACTION_DATE));
            String location_table_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LOCATION_ID));
            String cmp_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CMPID));
            String truck_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCKNO));
            String commodity_id = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_COMMODITY_ID));
            String packing_in_kg = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_PACKING_KG));
            String packing_type = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_TYPE));
            String material_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_PACKING_MATERIAL_WEIGHT));
            String supplier_name = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_SUPPLIER_NAME));
            String gate_pass_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NCML_GATE_PASSNO));
            String deposit_lr_gr_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LR_GR_NO));
            String deposit_no_of_bags = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_OF_BAGS));
            String deposit_empty_gunnies_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EMPTY_GUNNIES_WEIGHT));
            String deposit_weigh_bridge_name = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_NAME));
            String deposit_weigh_bridge_slip_no = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_SLIP_NO));
            String deposit_distance_weigh_bridge_from_godown = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DISTANCE_WEIGH_BRIDGE_FROM_GODOWN));
            String deposit_weigh_bridge_time_in = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEIN));
            String deposit_weigh_bridge_time_out = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEIGH_BRIDGE_TIMEOUT));
            String deposit_total_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TOTAL_WEIGHT));
            String deposit_tare_weigh = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TARE_WEIGHT));
            String deposit_gross_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_GROSS_WEIGHT));
            String deposit_net_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NET_WEIGHT));
            String deposit_average_weight = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_AVERAGE_WEIGHT));
            String deposit_no_cut_and_tom_bags = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_CUT_AND_TOM_BAGS));
            String deposit_no_palla_bags = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_NO_PALLA_BAGS));
            String deposit_moisture = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_MOISTURE));
            String deposit_foreign_matter = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_FOREIGN_MATTER));
            String deposit_dmg_disclosured = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_FUNGUS_DMG_DISCOLORED));
            String deposit_broken_immature = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_BROKEN_IMMATURE));
            String deposit_weevilled = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_WEEVILLED));
            String deposit_live_insects = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_LIVE_INSECTS));
            String deposit_deposit_remakrs = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_REMARKS));
            String deposit_created_date = cursor.getString(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_CREATED_DATE));

            byte[] bytes_pic1 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCK_FRONT_IMAGE_NAME));
            String deposit_truck_front_image_name = Base64.encodeToString(bytes_pic1, Base64.NO_WRAP);

            byte[] bytes_pic2 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_TRUCK_REAR_IMAGE_NAME));
            String deposit_truck_rear_image_name = Base64.encodeToString(bytes_pic2, Base64.NO_WRAP);

            byte[] bytes_pic3 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_DEPOSITOR_IMAGE_NAME));
            String deposit_depositor_image_name = Base64.encodeToString(bytes_pic3, Base64.NO_WRAP);

            byte[] bytes_pic4 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_EMP_ACCEPTING_GOODS_IMAGE_NAME));
            String deposit_emp_accep_goods_image_name = Base64.encodeToString(bytes_pic4, Base64.NO_WRAP);

            byte[] bytes_pic5 = cursor.getBlob(cursor.getColumnIndex(LOCAL_DEPOSIT_TABLE_SAMPLE_TAKEN_IMAGE_NAME));
            String deposit_sample_taken_image_name = Base64.encodeToString(bytes_pic5, Base64.NO_WRAP);

            jsonObject = new JSONObject();

            try {

                jsonObject.put("Deposit_truck_front_image_name", deposit_truck_front_image_name);
                jsonObject.put("Deposit_truck_rear_image_name", deposit_truck_rear_image_name);
                jsonObject.put("Deposit_depositor_image_name", deposit_depositor_image_name);
                jsonObject.put("Deposit_sample_taken_image_name", deposit_emp_accep_goods_image_name);
                jsonObject.put("Deposit_emp_accep_goods_image_name", deposit_sample_taken_image_name);

                jsonObject.put("Deposit_depositid", deposit_depositid);
                jsonObject.put("Deposit_client_copyno", deposit_client_copyno);
                jsonObject.put("Deposit_exchange_type_id", deposit_exchange_type_id);
                jsonObject.put("State_id", state_id);
                jsonObject.put("Deposit_is_scm_deposit", Is_scm_deposite);
                jsonObject.put("Deposit_transaction_date", transaction_date);
                jsonObject.put("Deposit_location_id", location_table_id);
                jsonObject.put("Deposit_cmp_id", cmp_id);
                jsonObject.put("Deposit_truck_no", truck_no);
                jsonObject.put("Deposit_commodity_id", commodity_id);
                jsonObject.put("Deposit_packing", packing_in_kg);
                jsonObject.put("packing_material_type", packing_type);
                jsonObject.put("Deposit_packing_material_weight", material_weight);
                jsonObject.put("Deposit_supplier_name", supplier_name);
                jsonObject.put("Deposit_ncml_gate_passno", gate_pass_no);
                jsonObject.put("Deposit_lr_gr_no", deposit_lr_gr_no);
                jsonObject.put("Deposit_no_of_bags", deposit_no_of_bags);
                jsonObject.put("Deposit_empty_gunnies_weight", deposit_empty_gunnies_weight);
                jsonObject.put("Deposit_weigh_bridge_name", deposit_weigh_bridge_name);
                jsonObject.put("Deposit_weigh_bridge_slip_no", deposit_weigh_bridge_slip_no);
                jsonObject.put("Deposit_distance_weigh_bridge_from_godown", deposit_distance_weigh_bridge_from_godown);
                jsonObject.put("Deposit_weigh_bridge_time_in", deposit_weigh_bridge_time_in);
                jsonObject.put("Deposit_weigh_bridge_time_out", deposit_weigh_bridge_time_out);
                jsonObject.put("Deposit_total_weight", deposit_total_weight);
                jsonObject.put("Deposit_tare_weigh", deposit_tare_weigh);
                jsonObject.put("Deposit_gross_weight", deposit_gross_weight);
                jsonObject.put("Deposit_net_weight", deposit_net_weight);
                jsonObject.put("Deposit_average_weight", deposit_average_weight);
                jsonObject.put("Deposit_no_cut_and_tom_bags", deposit_no_cut_and_tom_bags);
                jsonObject.put("Deposit_no_palla_bags", deposit_no_palla_bags);
                jsonObject.put("Deposit_moisture", deposit_moisture);
                jsonObject.put("Deposit_foreign_matter", deposit_foreign_matter);
                jsonObject.put("Deposit_dmg_disclosured", deposit_dmg_disclosured);
                jsonObject.put("Deposit_broken_immature", deposit_broken_immature);
                jsonObject.put("Deposit_weevilled", deposit_weevilled);
                jsonObject.put("Deposit_live_insects", deposit_live_insects);
                jsonObject.put("Deposit_deposit_remakrs", deposit_deposit_remakrs);
                jsonObject.put("Deposit_created_by", emp_id);
                jsonObject.put("Deposit_created_date", deposit_created_date);

              /*  jsonObject.put("Deposit_truck_front_image_name","");
                jsonObject.put("Deposit_truck_rear_image_name", "");
                jsonObject.put("Deposit_depositor_image_name","");
                jsonObject.put("Deposit_sample_taken_image_name","");
                jsonObject.put("Deposit_emp_accep_goods_image_name", "");*/

                Cursor cursor_details = db.query(DEPOSIT_TABLE_DETAILS, null, LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + "=?", new String[]{deposit_depositid}, null, null, null, null);

                jsonArray_details = new JSONArray();

                if (cursor_details.getCount() > 0) {
                    String GetCountTotal = String.valueOf(cursor_details.getCount());
                    Log.e("Details No rows", GetCountTotal);

                    cursor_details.moveToFirst();

                    do {
                        row_id = cursor_details.getString(cursor.getColumnIndex(COMMON_ID));
                        String DepositDetails = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_ID));
                        String DepositId = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID));
                        String GodownId = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_GODOWN_ID));
                        String StackNo = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_STACK_NO));
                        String LotNo = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_LOT_NO));
                        String NoOfBags = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_NO_OF_BAGS));
                        String Qty = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_QTY));
                        String ClientId = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_ID));
                        String ClientName = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CLIENT_NAME));
                        String DepositoryID = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_ID));
                        String DepositoryName = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSITORY_NAME));
                        String created_by = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_BY));
                        String created_date = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_DEPOSIT_DETAILS_TABLE_CREATED_DATE));

                        jsonObject_details = new JSONObject();

                        try {
                            jsonObject_details.put("DepositDetails", DepositDetails);
                            jsonObject_details.put("DepositId", DepositId);
                            jsonObject_details.put("GodownId", GodownId);
                            jsonObject_details.put("StackNo", StackNo);
                            jsonObject_details.put("LotNo", LotNo);
                            jsonObject_details.put("NoOfBags", NoOfBags);
                            jsonObject_details.put("Qty", Qty);
                            jsonObject_details.put("ClientId", ClientId);
                            jsonObject_details.put("ClientName", ClientName);
                            jsonObject_details.put("DepositoryID", DepositoryID);
                            jsonObject_details.put("DepositoryName", DepositoryName);
                            jsonObject_details.put("datatransfer", "0");

                            jsonArray_details.put(jsonObject_details);
                            Log.e("json array string", "json details array string = " + jsonArray_details.toString().trim());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } while (cursor_details.moveToNext());
                    cursor_details.close();
                }

                jsonObject.put("deposit_details", jsonArray_details);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            cursor.close();
            jsonString = jsonArray.toString();
        }
        close();
        Log.e("dbhelper", "json string = " + jsonString.toString().trim());
        return row_index_to_delete + "," + jsonString.toString().trim();
    }


    public String getLocalAuditPointDetailsAllDataInJson() {

        String jsonString = "[]";
        String row_index;
        String emp_id = "";


        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray_details = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject_details = new JSONObject();

        String NoOfStacks = "";
        String ReasonNotStack = "";
        String NoOfSamples = "";
        String ReasonNotSamples = "";

        String financial_remark = "";
        String quality_remark = "";
        String over_ruled_remark = "";

        String audit_point_id_details = "";
        String response_details = "";
        String remark_details = "";

        String id = "";
        String date = "";
        String segment_id = "";
        String audit_type = "";
        String auditor = "";
        String state = "";
        String location = "";
        String warehouse = "";
        String godown = "";
        String address = "";
        String entry_tyep = "";
        String form_no = "";
        String created_date = "";
        String created_by_name = "";
        byte[] bytes_pic;
        String godown_pic = "";

        String row_index_to_delete = "";
        open();

        Cursor cursor_emp_id = db.query(LOGIN_TABLE, null, null, null, null, null, null);
        if (cursor_emp_id != null && cursor_emp_id.getCount() > 0) {
            cursor_emp_id.moveToFirst();
            emp_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_EMPLOYEE_ID));
            cursor_emp_id.close();
        }

        Cursor cursor_audit = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, null, null, null, null, null, null);

        if (cursor_audit != null && cursor_audit.getCount() > 0) {

            cursor_audit.moveToFirst();


            row_index = cursor_audit.getString(cursor_audit.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER));

            Cursor cursor_audit_child = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER + "=?", new String[]{row_index}, null, null, null);

            cursor_audit_child.moveToFirst();

            row_index_to_delete = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(COMMON_ID));
            id = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_AUDIT_ID));
            date = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_DATE));
            segment_id = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_SEGMENT_ID));
            audit_type = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_AUDIT_TYPE_ID));
            auditor = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_AUDITOR_ID));
            state = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_STATE_ID));
            location = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_LOCATION_ID));
            warehouse = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_WAREHOUSE_ID));
            godown = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_GODOWN_ID));
            address = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ADDRESS));
            entry_tyep = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ENTRY_TYPE));
            form_no = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_FORM_NO));
            created_date = cursor_audit_child.getString(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_CREATED_BY_DATE));
            created_by_name = emp_id;
            bytes_pic = cursor_audit_child.getBlob(cursor_audit_child.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_GODOWN_PIC));
            godown_pic = Base64.encodeToString(bytes_pic, Base64.NO_WRAP);

            Cursor cursor_final = db.query(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE, null, LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_ONE_ROW_CHECKER + "=?", new String[]{row_index}, null, null, null);

            if (cursor_audit != null && cursor_final.getCount() > 0) {
                cursor_final.moveToFirst();

                NoOfStacks = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_NO_OF_STACKS));
                ReasonNotStack = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_REASON_NOT_STACK));
                NoOfSamples = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_NO_OF_SAMPLES));
                ReasonNotSamples = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_REASON_NOT_SAMPLES));

                financial_remark = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_FINANCIAL_REMARK));
                quality_remark = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_QUALITY_REMARK));
                over_ruled_remark = cursor_final.getString(cursor_final.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_OVER_RULED_REMARK));
            }
            jsonObject = new JSONObject();

            try {

                jsonObject.put("Id", id);
                jsonObject.put("date", date);
                jsonObject.put("segment", segment_id);
                jsonObject.put("audit_type", audit_type);
                jsonObject.put("auditor", auditor);
                jsonObject.put("state", state);
                jsonObject.put("location", location);
                jsonObject.put("warehouse", warehouse);
                jsonObject.put("godown", godown);
                jsonObject.put("address", address);
                jsonObject.put("entry_type", entry_tyep);
                jsonObject.put("form_no", form_no);
                jsonObject.put("createdby", created_by_name);
                jsonObject.put("godown_pic", godown_pic);
                jsonObject.put("NoOfStacks", NoOfStacks);
                jsonObject.put("ReasonNotStack", ReasonNotStack);
                jsonObject.put("NoOfSamples", NoOfSamples);
                jsonObject.put("ReasonNotSamples", ReasonNotSamples);
                jsonObject.put("financial_remark", financial_remark);
                jsonObject.put("quality_remark", quality_remark);
                jsonObject.put("over_ruled_remark", over_ruled_remark);
                jsonObject.put("createddate", created_date);

                Cursor cursor_details = db.query(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE, null, LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_ONE_ROW_CHECKER + "=?", new String[]{row_index}, null, null, null);

                jsonArray_details = new JSONArray();

                if (cursor_details.getCount() > 0) {
                    cursor_details.moveToFirst();

                    do {
                        audit_point_id_details = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_AUDIT_POINT_ID));
                        response_details = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_RESPONSE));
                        remark_details = cursor_details.getString(cursor_details.getColumnIndex(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_REMARK));

                        Log.e("remark_details", "remark_details = " + remark_details);

                        jsonObject_details = new JSONObject();

                        if (remark_details.trim().length() > 0 && remark_details.contains("nil")) {
                            remark_details = " ";
                        } else {
                            //remark_details = "null";
                        }

                        try {

                            jsonObject_details.put("remark", remark_details);
                            jsonObject_details.put("audit_point", audit_point_id_details);
                            jsonObject_details.put("respones", response_details);

                            jsonArray_details.put(jsonObject_details);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } while (cursor_details.moveToNext());
                    cursor_details.close();
                }

                jsonObject.put("godown_details", jsonArray_details);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonObject);

            cursor_final.close();
            cursor_audit_child.close();


            jsonString = jsonArray.toString();

        }

        cursor_audit.close();
        cursor_emp_id.close();


        close();

        Log.e("dbhelper", "json string = " + jsonString.toString().trim());


        return row_index_to_delete + "," + jsonString.toString().trim();
    }


    public void insertIntoWareHouseInspectionMainTable(ArrayList<WareHouseInspectionPojo> list) {
        open();

        ContentValues values;
        WareHouseInspectionPojo pojo;

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                values = new ContentValues();
                pojo = list.get(i);

                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR, pojo.getParticular());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_ID, pojo.getParticular_id());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_SEQUENCE_NO, pojo.getParticular_sqe_no());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_COL, pojo.getName());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID, pojo.getName_id());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_SEQUENCE_NO, pojo.getName_sqe_no());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_BOOLEAN, pojo.getBoolean_validation());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_DATE, pojo.getDate());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES, pojo.getValues());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES_MANDATORY_VALIDATION, pojo.getValues_mandatory_validation());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MULTI_SELECT_VALIDATION, pojo.getMulti_select_validation());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NUMBER_VALIDATION, pojo.getNumber_validation());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_TEXT_VALIDATION, pojo.getText_validation());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER, pojo.getGrp_header());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER_ID, pojo.getGrp_header_id());
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MASTER_ITEM_TYPE_ID, pojo.getMaster_item_type_it());

                db.insert(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, values);

                values = null;
                pojo = null;
            }
        }
        close();
    }


    public void insertUpdateIntoWareHouseInspectionMainTable(ArrayList<WareHouseInspectionPojo> list) {
        open();

        ContentValues values;
        WareHouseInspectionPojo pojo;

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                values = new ContentValues();
                pojo = list.get(i);

                Cursor cursor;
                cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID + "=?", new String[]{pojo.getName_id()}, null, null, null);

                if (cursor != null && cursor.getCount() > 0) {
                    db.delete(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID + "=?", new String[]{pojo.getName_id()});

                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR, pojo.getParticular());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_ID, pojo.getParticular_id());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_SEQUENCE_NO, pojo.getParticular_sqe_no());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_COL, pojo.getName());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID, pojo.getName_id());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_SEQUENCE_NO, pojo.getName_sqe_no());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_BOOLEAN, pojo.getBoolean_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_DATE, pojo.getDate());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES, pojo.getValues());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES_MANDATORY_VALIDATION, pojo.getValues_mandatory_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MULTI_SELECT_VALIDATION, pojo.getMulti_select_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NUMBER_VALIDATION, pojo.getNumber_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_TEXT_VALIDATION, pojo.getText_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER, pojo.getGrp_header());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER_ID, pojo.getGrp_header_id());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MASTER_ITEM_TYPE_ID, pojo.getMaster_item_type_it());

                    db.insert(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, values);

                } else {
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR, pojo.getParticular());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_ID, pojo.getParticular_id());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_SEQUENCE_NO, pojo.getParticular_sqe_no());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_COL, pojo.getName());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID, pojo.getName_id());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_SEQUENCE_NO, pojo.getName_sqe_no());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_BOOLEAN, pojo.getBoolean_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_DATE, pojo.getDate());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES, pojo.getValues());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES_MANDATORY_VALIDATION, pojo.getValues_mandatory_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MULTI_SELECT_VALIDATION, pojo.getMulti_select_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NUMBER_VALIDATION, pojo.getNumber_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_TEXT_VALIDATION, pojo.getText_validation());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER, pojo.getGrp_header());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER_ID, pojo.getGrp_header_id());
                    values.put(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MASTER_ITEM_TYPE_ID, pojo.getMaster_item_type_it());

                    db.insert(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, values);

                }


                values = null;
                pojo = null;
            }
        }
        close();
    }

    public void insertIntoWareHouseInspectionTempTable() {
        open();

        ContentValues values;


        //Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, null, null, null, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR + " ASC");
        //Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, null, null, null, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_SEQUENCE_NO + " ASC");
        Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, null, null, null, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_ID + " ASC");

        if (cursor.getCount() > 0 && cursor != null) {

            cursor.moveToFirst();

            do {

                values = new ContentValues();

                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_VISIBLE, "YES");
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_VISIBLE, "YES");
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_ID, cursor.getInt(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_ID)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_SEQUENCE_NO, cursor.getInt(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_SEQUENCE_NO)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_COL)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_ID, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_SEQUENCE_NO, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_SEQUENCE_NO)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_BOOLEAN, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_BOOLEAN)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_DATE, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_DATE)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_VALUES, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_VALUES_MANDATORY_VALIDATION, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES_MANDATORY_VALIDATION)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_MULTI_SELECT_VALIDATION, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MULTI_SELECT_VALIDATION)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NUMBER_VALIDATION, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NUMBER_VALIDATION)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_TEXT_VALIDATION, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_TEXT_VALIDATION)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_GROUP_HEADER, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_GROUP_HEADER_ID, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_GROUP_HEADER_ID)));
                values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_MASTER_ITEM_TYPE_ID, cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MASTER_ITEM_TYPE_ID)));

                db.insert(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, null, values);

                values = null;

            } while (cursor.moveToNext());
        }

        cursor.close();


        close();
    }


    public ArrayList<WareHouseInspectionDynamicPojo> getWareHouseInspectionMandatoryField() {
        ArrayList<WareHouseInspectionDynamicPojo> list = new ArrayList<WareHouseInspectionDynamicPojo>();


        open();


        Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_VALUES_MANDATORY_VALIDATION + " = ?", new String[]{"1"}, null, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR + " ASC");

        WareHouseInspectionDynamicPojo pojo;

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                pojo = new WareHouseInspectionDynamicPojo();

                pojo.setParticular(cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR)));
                pojo.setName(cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_COL)));


                list.add(pojo);
                pojo = null;

            } while (cursor.moveToNext());

        }

        cursor.close();

        close();


        return list;
    }


    public void deleteWareHouseInspectionDynamicTemp() {
        open();

        db.delete(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, null, null);

        close();
    }

    public ArrayList<String> getWareHouseinspectionParticularList() {

        ArrayList<String> list = new ArrayList<String>();
        String string_particular;
        open();
        //Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, new String[]{COMMON_ID, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR}, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_VISIBLE + "=?", new String[]{"YES"}, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR, null, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR + " ASC");
        //Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, new String[]{COMMON_ID, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR}, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_VISIBLE + "=?", new String[]{"YES"}, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR, null, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_SEQUENCE_NO + " ASC");
        Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, new String[]{COMMON_ID, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR}, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_VISIBLE + "=?", new String[]{"YES"}, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR, null, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_ID + " ASC");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                string_particular = cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR));
                list.add(string_particular);
            } while (cursor.moveToNext());


        }
        cursor.close();
        close();


// add elements to al, including duplicates
        /*HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);

        hs = null;*/


        return list;

    }

    public ArrayList<String> getWareHouseInspectionName(String particular_name) {

        String string_name;
        ArrayList<String> list = new ArrayList<String>();
        open();
        Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, new String[]{COMMON_ID, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL}, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_VISIBLE + "= ? AND " + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR + "=?", new String[]{"YES", particular_name}, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL, null, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                string_name = cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL));
                list.add(string_name);

            } while (cursor.moveToNext());

        }
        cursor.close();

        close();


        /*HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);

        hs = null;
*/
        return list;
    }

    public String getUiNameWarehouseInspection(String string_name) {
        String string_ui = "", string_edittext_char, string_edittext_numbers, string_drop_down, string_date;

        open();


        Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, null, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL + "=? AND " + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_VISIBLE + "=?", new String[]{string_name, "YES"}, null, null, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();


            string_edittext_char = cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_TEXT_VALIDATION));
            string_edittext_numbers = cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NUMBER_VALIDATION));
            string_drop_down = cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_BOOLEAN));
            string_date = cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_DATE));

            if (string_drop_down.contains("1")) {
                string_ui = "drop_down";
            } else if (string_date.contains("1")) {
                string_ui = "date";
            } else if (string_edittext_numbers.contains("1")) {
                string_ui = "number";
            } else if (string_edittext_char.contains("1")) {
                string_ui = "text";
            } else {
                string_ui = "text";
            }


        }

        cursor.close();
        close();

        return string_ui;
    }


    public boolean checkMultiSelection(String particular_name) {

        boolean flag = false;

        open();

        Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, null, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR + " = ? AND " + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_MULTI_SELECT_VALIDATION + " = ? ", new String[]{particular_name, "1"}, null, null, null);

        if (cursor.getCount() > 0) {
            flag = true;
        }


        close();

        return flag;
    }

    public void changeParticularVisible(String string_particular, String string_name_col) {

        open();

        ContentValues values = new ContentValues();
        values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_VISIBLE, "NO");

        db.update(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, values, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_COL + "=? AND " + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR + "=?", new String[]{string_name_col, string_particular});


        Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, null, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_NAME_VISIBLE + "=? AND " + WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR + "=?", new String[]{"YES", string_particular}, null, null, null);
        cursor.moveToFirst();


        if (cursor != null && cursor.getCount() <= 0) {

            values = null;
            values = new ContentValues();

            values.put(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR_VISIBLE, "NO");

            db.update(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, values, WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE_PARTICULAR + "=?", new String[]{string_particular});

            values = null;
        }

        cursor.close();

        close();


    }


    public void insertLocalWareHouseInspection(ArrayList<WareHouseInspectionDynamicPojo> list, String header_last_row_index) {

        WareHouseInspectionDynamicPojo pojo;
        ContentValues values;
        String string_name_id, string_particular_id, row_no;

        open();

/*
        Cursor cursor_row_no = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, null, null, null, null, null);
        cursor_row_no.moveToFirst();
        cursor_row_no.moveToLast();
        row_no = cursor_row_no.getString(cursor_row_no.getColumnIndex(COMMON_ID));
        cursor_row_no.close();
*/

        row_no = header_last_row_index;

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                values = new ContentValues();
                pojo = list.get(i);

                Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR + "=? AND " + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_COL + "=?", new String[]{pojo.getParticular(), pojo.getName()}, null, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR + " ASC");
                cursor.moveToFirst();

                string_name_id = cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID));
                string_particular_id = cursor.getString(cursor.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR_ID));

                cursor.close();

                values.put(LOCAL_WAREHOUSE_INSPECTION_TABLE_NAME, pojo.getName());
                values.put(LOCAL_WAREHOUSE_INSPECTION_TABLE_NAME_ID, string_name_id);
                values.put(LOCAL_WAREHOUSE_INSPECTION_TABLE_PARTICULAR, pojo.getParticular());
                values.put(LOCAL_WAREHOUSE_INSPECTION_TABLE_PARTICULAR_ID, string_particular_id);
                values.put(LOCAL_WAREHOUSE_INSPECTION_TABLE_VALUES, pojo.getValues());
                values.put(LOCAL_WAREHOUSE_INSPECTION_TABLE_REMARK, pojo.getRemark());
                values.put(LOCAL_WAREHOUSE_INSPECTION_TABLE_ROW_FLAG, row_no);
                values.put(LOCAL_WAREHOUSE_INSPECTION_TABLE_TYPE_FLAG, pojo.getType_flag());

                db.insert(LOCAL_WAREHOUSE_INSPECTION_TABLE, null, values);

                values = null;
                pojo = null;
            }
        }
        close();
    }


    public void updateLocalWareHouseInspectionDetailCount(int index, String header_last_row_index) {
        open();

        // Update Waehouse Inspection Process Table
        ContentValues values = new ContentValues();
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_RECORDCOUNT_INSPECTION_DETAILS, "" + index);
        db.update(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, values, COMMON_ID + "=?", new String[]{header_last_row_index});

        close();
    }

    public void insertBank(ArrayList<BankPojo> list) {
        ContentValues values;
        BankPojo pojo;
        open();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                values = new ContentValues();
                pojo = list.get(i);

                values.put(BANK_TABLE_BANK_ID, pojo.getBanck_id());
                values.put(BANK_TABLE_BANK_NAME, pojo.getBanck_name());

                db.insert(BANK_TABLE, null, values);

                pojo = null;
                values = null;
            }
        }
        close();
    }

    public void insertUpdateBank(ArrayList<BankPojo> list) {
        ContentValues values;
        BankPojo pojo;
        open();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                values = new ContentValues();
                pojo = list.get(i);

                Cursor cursor;
                cursor = db.query(BANK_TABLE, null, BANK_TABLE_BANK_ID + "=?", new String[]{pojo.getBanck_id()}, null, null, null);

                if (cursor != null && cursor.getCount() > 0) {
                    db.delete(BANK_TABLE, BANK_TABLE_BANK_ID + "=?", new String[]{pojo.getBanck_id()});

                    values.put(BANK_TABLE_BANK_ID, pojo.getBanck_id());
                    values.put(BANK_TABLE_BANK_NAME, pojo.getBanck_name());

                    db.insert(BANK_TABLE, null, values);
                } else {
                    values.put(BANK_TABLE_BANK_ID, pojo.getBanck_id());
                    values.put(BANK_TABLE_BANK_NAME, pojo.getBanck_name());

                    db.insert(BANK_TABLE, null, values);
                }
                pojo = null;
                values = null;
            }
        }
        close();
    }


    public void insertBranch(ArrayList<BranchPojo> list) {
        ContentValues values;
        BranchPojo pojo;
        open();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                values = new ContentValues();
                pojo = list.get(i);

                values.put(BRANCH_TABLE_BANK_ID, pojo.getBank_id());
                values.put(BRANCH_TABLE_BRANCH_ID, pojo.getBranch_id());
                values.put(BRANCH_TABLE_BRANCH_NAME, pojo.getBranch_name());

                db.insert(BRANCH_TABLE, null, values);

                pojo = null;
                values = null;
            }
        }
        close();
    }

    public void insertUpdateBranch(ArrayList<BranchPojo> list) {
        ContentValues values;
        BranchPojo pojo;
        open();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                values = new ContentValues();
                pojo = list.get(i);

                Cursor cursor;
                cursor = db.query(BRANCH_TABLE, null, BRANCH_TABLE_BRANCH_ID + "=?", new String[]{pojo.getBranch_id()}, null, null, null);

                if (cursor != null && cursor.getCount() > 0) {
                    db.delete(BRANCH_TABLE, BRANCH_TABLE_BRANCH_ID + "=?", new String[]{pojo.getBranch_id()});

                    values.put(BRANCH_TABLE_BANK_ID, pojo.getBank_id());
                    values.put(BRANCH_TABLE_BRANCH_ID, pojo.getBranch_id());
                    values.put(BRANCH_TABLE_BRANCH_NAME, pojo.getBranch_name());

                    db.insert(BRANCH_TABLE, null, values);
                } else {
                    values.put(BRANCH_TABLE_BANK_ID, pojo.getBank_id());
                    values.put(BRANCH_TABLE_BRANCH_ID, pojo.getBranch_id());
                    values.put(BRANCH_TABLE_BRANCH_NAME, pojo.getBranch_name());

                    db.insert(BRANCH_TABLE, null, values);
                }
                pojo = null;
                values = null;
            }
        }
        close();
    }


    /*public ArrayList<String> getBank() {
        open();

        ArrayList<String> list = new ArrayList<String>();
        String string_bank_name;

        Cursor cursor = db.query(BANK_TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                string_bank_name = cursor.getString(cursor.getColumnIndex(BANK_TABLE_BANK_NAME));
                list.add(string_bank_name);

            } while (cursor.moveToNext());
        }

        cursor.close();

        close();

        return list;
    }

    public ArrayList<String> getBranch(String bank_name) {

        String bank_id, branch_name;

        ArrayList<String> list = new ArrayList<String>();
        open();

        Cursor cursor_bank_id = db.query(BANK_TABLE, null, BANK_TABLE_BANK_NAME + "=?", new String[]{bank_name}, null, null, null);
        cursor_bank_id.moveToFirst();

        bank_id = cursor_bank_id.getString(cursor_bank_id.getColumnIndex(BANK_TABLE_BANK_ID));

        cursor_bank_id.close();

        Cursor cursor_branch = db.query(BRANCH_TABLE, null, BRANCH_TABLE_BANK_ID + "=?", new String[]{bank_id}, null, null, null);


        if (cursor_branch != null && cursor_branch.getCount() > 0) {
            cursor_branch.moveToFirst();

            do {
                branch_name = cursor_branch.getString(cursor_branch.getColumnIndex(BRANCH_TABLE_BRANCH_NAME));
                list.add(branch_name);


            } while (cursor_branch.moveToNext());

        }

        cursor_branch.close();

        close();

        return list;
    }*/


    public void insertDistrict(ArrayList<DistrictPojo> list) {
        open();
        ContentValues values;
        DistrictPojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(DISTRICT_TABLE_DISTRICT_ID, pojo.getDistrict_id());
            values.put(DISTRICT_TABLE_DISTRICT_NAME, pojo.getDistrict_name());
            values.put(DISTRICT_TABLE_DISTRICT_STATE_ID, pojo.getState_id());
            values.put(DISTRICT_TABLE_DISTRICT_SEGMENT_NAME, pojo.getSegment_Name());

            db.insert(DISTRICT_TABLE, null, values);

            pojo = null;
            values = null;
        }
        close();
    }

    //Written by Rajesh 02.05.2016
    public void insertExchangeType(ArrayList<ExchangeTypePojo> list) {
        open();
        ContentValues values;
        ExchangeTypePojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(LOCAL_TABLE_EXCHANGE_TYPE_ID, pojo.getExchange_type_id());
            values.put(LOCAL_TABLE_EXCHANGE_TYPE_NAME, pojo.getExchange_type_name());

            db.insert(EXCHANGE_TYPE_TABLE, null, values);

            pojo = null;
            values = null;
        }
        close();
    }

    public void insertCMP(ArrayList<CMPPojo> list) {
        open();
        ContentValues values;
        CMPPojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(LOCAL_TABLE_CMP_ID, pojo.getCMP_id());
            values.put(LOCAL_TABLE_CMP_NAME, pojo.getCMP_name());
            values.put(LOCAL_TABLE_CMP_LOCATION_ID, pojo.getLocation_id());
            values.put(LOCAL_TABLE_CMP_CONCURRENCY, pojo.getConcurrency());

            db.insert(CMP_TABLE, null, values);

            pojo = null;
            values = null;
        }
        close();
    }

    public void insert_Commodity_Location_Mapping(ArrayList<CommodityOnLocationPojo> list) {
        open();
        ContentValues values;
        CommodityOnLocationPojo pojo;
//RAJAVAd
        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_NAME, pojo.getCommodity());
            values.put(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_COMMODITY_ID, pojo.getCommodityId());
            values.put(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_NAME, pojo.getExchangeType());
            values.put(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_EXCHANGE_TYPE_ID, pojo.getExchangeTypeId());
            values.put(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_NAME, pojo.getLocation());
            values.put(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_LOCATION_ID, pojo.getLocationId());
            values.put(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_ID, pojo.getStateId());
            values.put(LOCAL_TABLE_COMMODITY_LOCATION_MAPPING_STATE_NAME, pojo.getStateName());

            db.insert(COMMODITY_ON_LOCATION_MAPPING, null, values);

            pojo = null;
            values = null;
        }
        close();
    }


    public void insertCommodity(ArrayList<CommodityPojo> list) {
        open();
        ContentValues values;
        CommodityPojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            values.put(LOCAL_TABLE_COMMODITY_TYPE_ID, pojo.getCommodity_id());
            values.put(LOCAL_TABLE_COMMODITY_TYPE_NAME, pojo.getCommodity_name());
            values.put(LOCAL_TABLE_COMMODITY_EXCHANGE_TYPE_ID, pojo.getCommodity_exchange_type_id());
            values.put(LOCAL_TABLE_COMMODITY_EXCHANGE_TYPE_NAME, pojo.getCommodity_exchange_type_name());

            db.insert(COMMODITY_TYPE_TABLE, null, values);

            pojo = null;
            values = null;
        }
        close();
    }

    public void insertUpdateDistrict(ArrayList<DistrictPojo> list) {
        open();
        ContentValues values;
        DistrictPojo pojo;

        for (int i = 0; i < list.size(); i++) {
            values = new ContentValues();
            pojo = list.get(i);

            Cursor cursor;
            cursor = db.query(DISTRICT_TABLE, null, DISTRICT_TABLE_DISTRICT_ID + "=?", new String[]{pojo.getDistrict_id()}, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                db.delete(DISTRICT_TABLE, DISTRICT_TABLE_DISTRICT_ID + "=?", new String[]{pojo.getDistrict_id()});

                values.put(DISTRICT_TABLE_DISTRICT_ID, pojo.getDistrict_id());
                values.put(DISTRICT_TABLE_DISTRICT_NAME, pojo.getDistrict_name());
                values.put(DISTRICT_TABLE_DISTRICT_STATE_ID, pojo.getState_id());
                values.put(DISTRICT_TABLE_DISTRICT_SEGMENT_NAME, pojo.getSegment_Name());

                db.insert(DISTRICT_TABLE, null, values);
            } else {
                values.put(DISTRICT_TABLE_DISTRICT_ID, pojo.getDistrict_id());
                values.put(DISTRICT_TABLE_DISTRICT_NAME, pojo.getDistrict_name());
                values.put(DISTRICT_TABLE_DISTRICT_STATE_ID, pojo.getState_id());
                values.put(DISTRICT_TABLE_DISTRICT_SEGMENT_NAME, pojo.getSegment_Name());

                db.insert(DISTRICT_TABLE, null, values);
            }

            pojo = null;
            values = null;
        }
        close();
    }

    public void insertDownloadedDateTime(String downloadedDatetime) {
        open();

        // Delete LOCAL_UPDATED_DATABASE_DATETIME_TABLE
        db.delete(LOCAL_UPDATED_DATABASE_DATETIME_TABLE, null, null);

        ContentValues values;
        values = new ContentValues();
        values.put(LOCAL_UPDATED_DATABASE_DATETIME_TABLE_DATETIME, downloadedDatetime);
        db.insert(LOCAL_UPDATED_DATABASE_DATETIME_TABLE, null, values);
        values = null;

        close();
    }

    public String getDownloadedDateTime() {
        String Datetime = "";
        open();

        Cursor cursor = db.query(LOCAL_UPDATED_DATABASE_DATETIME_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Datetime = cursor.getString(cursor.getColumnIndex(LOCAL_UPDATED_DATABASE_DATETIME_TABLE_DATETIME));
        } else {
            Datetime = getCurrentDateTime();
        }
        cursor.close();

        close();
        return Datetime;
    }

    public ArrayList<String> getDistrictArray(String state_name) {
        ArrayList<String> list = new ArrayList<String>();
        open();

        String state_id = "";

        Cursor cursor_state_id = db.query(STATE_TABLE, null, STATE_TABLE_STATE_NAME + "=?", new String[]{state_name}, null, null, null);
        if (cursor_state_id != null && cursor_state_id.getCount() > 0) {
            cursor_state_id.moveToFirst();
            state_id = cursor_state_id.getString(cursor_state_id.getColumnIndex(STATE_TABLE_STATE_ID));
            cursor_state_id.close();
        }

        Cursor cursor = db.query(DISTRICT_TABLE, null, DISTRICT_TABLE_DISTRICT_STATE_ID + "=?  AND " + DISTRICT_TABLE_DISTRICT_SEGMENT_NAME + "= ? ", new String[]{state_id, "CM"}, DISTRICT_TABLE_DISTRICT_NAME, null, DISTRICT_TABLE_DISTRICT_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(DISTRICT_TABLE_DISTRICT_NAME)));
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();

        close();

        return list;
    }


/*
        public ArrayList<String> getDistrictArray(String location_name) {

        ArrayList<String> list = new ArrayList<String>();
        open();
        String location_id;
        Cursor cursor_id = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{location_name}, null, null, null);
        cursor_id.moveToFirst();
        location_id = cursor_id.getString(cursor_id.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
        cursor_id.close();



        SELECT     CMPro_Mst_District.DistrictName, CMPro_Mst_District.DistrictID
        FROM         CMPro_Mst_State INNER JOIN
                              CMPro_Mst_District ON CMPro_Mst_State.StateID = CMPro_Mst_District.StateID INNER JOIN
                              CMPro_Mst_Location ON CMPro_Mst_State.StateID = CMPro_Mst_Location.StateID
        WHERE     (CMPro_Mst_Location.LocationID = 8)

        SELECT     CMPro_Mst_District.DistrictName, CMPro_Mst_Location.LocationID
        FROM         CMPro_Mst_District INNER JOIN
                              CMPro_Mst_Location ON CMPro_Mst_District.DistrictID = CMPro_Mst_Location.DistrictID
        WHERE     (CMPro_Mst_Location.LocationID = 8)

        	            final String MY_QUERY = "SELECT " + DISTRICT_TABLE_DISTRICT_NAME + " FROM " + DISTRICT_TABLE + " INNER JOIN " + LOCATION_TABLE + " ON " +
                    DISTRICT_TABLE_DISTRICT_ID + " = " + LOCATION_TABLE_STATE_ID +
                    " WHERE " + LOCATION_TABLE_LOCATION_ID +  " = ? ";

        select DistrictName from CMPro_Mst_District where DistrictID in (
        select DistrictID  from CMPro_Mst_Location where LocationID=8)


        //Cursor cursor = db.query(DISTRICT_TABLE, null, DISTRICT_TABLE_DISTRICT_STATE_ID + "=? ", new String[]{location_id}, DISTRICT_TABLE_DISTRICT_NAME, null, DISTRICT_TABLE_DISTRICT_NAME + " ASC");

            final String MY_QUERY = "SELECT " + DISTRICT_TABLE_DISTRICT_NAME + " FROM " + DISTRICT_TABLE + " INNER JOIN " + DISTRICT_TABLE + " ON " +
                    STATE_TABLE_STATE_ID + " = " + DISTRICT_TABLE_DISTRICT_STATE_ID + " INNER JOIN " + LOCATION_TABLE + " ON " +
                    STATE_TABLE_STATE_ID + " = " + LOCATION_TABLE_STATE_ID +
                    " WHERE " + LOCATION_TABLE_LOCATION_ID +  " = ? ";
            Cursor cursor =  db.rawQuery(MY_QUERY, new String[]{location_id});

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                list.add(cursor.getString(cursor.getColumnIndex(DISTRICT_TABLE_DISTRICT_NAME)));
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();


        close();

        return list;
    }
*/

    public String getDesignationName(String emp_name) {

        open();
        String designation_name = "";

        Cursor cursor = db.query(LOGIN_TABLE, null, LOGIN_EMP_NAME + "=?", new String[]{emp_name}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            designation_name = cursor.getString(cursor.getColumnIndex(LOGIN_DESIGNATION_NAME));

        }

        cursor.close();

        close();

        return designation_name;

    }

    public ArrayList<String> getBankNameArray() {

        ArrayList<String> list = new ArrayList<String>();
        String bank_name;

        open();

        Cursor cursor = db.query(BANK_TABLE, null, null, null, BANK_TABLE_BANK_NAME, null, BANK_TABLE_BANK_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {


            cursor.moveToFirst();

            do {
                bank_name = cursor.getString(cursor.getColumnIndex(BANK_TABLE_BANK_NAME));

                list.add(bank_name);
            } while (cursor.moveToNext());


        }

        cursor.close();


        close();


        return list;

    }


    public ArrayList<String> getBankBranchNameArray(String bank_name) {

        ArrayList<String> list = new ArrayList<String>();
        String branch_name;
        String bank_id = "";

        open();

        Cursor cursor_bank_id = db.query(BANK_TABLE, null, BANK_TABLE_BANK_NAME + "=?", new String[]{bank_name}, null, null, null);
        if (cursor_bank_id != null && cursor_bank_id.getCount() > 0) {
            cursor_bank_id.moveToFirst();
            bank_id = cursor_bank_id.getString(cursor_bank_id.getColumnIndex(BANK_TABLE_BANK_ID));
            cursor_bank_id.close();
        }

        Cursor cursor = db.query(BRANCH_TABLE, null, BRANCH_TABLE_BANK_ID + "=?", new String[]{bank_id}, BRANCH_TABLE_BRANCH_NAME, null, BRANCH_TABLE_BRANCH_NAME + " ASC");

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                branch_name = cursor.getString(cursor.getColumnIndex(BRANCH_TABLE_BRANCH_NAME));
                list.add(branch_name);

            } while (cursor.moveToNext());


        }

        cursor.close();


        close();


        return list;

    }


    public ArrayList<String> getLocationArrayUsingState(String district_name) {
        ArrayList<String> list = new ArrayList<String>();
        open();

        String district_id = "";

        Cursor cursor_district_id = db.query(DISTRICT_TABLE, null, DISTRICT_TABLE_DISTRICT_NAME + "=?", new String[]{district_name}, null, null, null);
        if (cursor_district_id != null && cursor_district_id.getCount() > 0) {
            cursor_district_id.moveToFirst();
            district_id = cursor_district_id.getString(cursor_district_id.getColumnIndex(DISTRICT_TABLE_DISTRICT_ID));
            cursor_district_id.close();
        }


        Cursor cursor = db.query(LOCATION_TABLE, null, LOCATION_TABLE_DISTRICT_ID + "= ?  AND " + LOCATION_TABLE_CM_AND_CMP_TABLE + "= ? ", new String[]{district_id, "CM"}, LOCATION_TABLE_LOCATION_NAME, null, LOCATION_TABLE_LOCATION_NAME + " ASC");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(cursor.getColumnIndex(LOCATION_TABLE_LOCATION_NAME)));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        cursor.close();
        close();
        return list;
    }


    public long insertLocalWareHouseInspectionHeader(String date,
                                                     String inspection_no,
                                                     String state,
                                                     String location_name,
                                                     String pin_code,
                                                     String district,
                                                     String godown_address,
                                                     String godown_owner,
                                                     String cc_location,
                                                     String bank,
                                                     String branch,
                                                     String survey_done_by_name,
                                                     String designation,
                                                     String contact_no,
                                                     String create_date_by_user) {

        String state_id = "", location_id = "", bank_id = "", emp_id = "", district_id = "", branch_id = "", designation_id = "";

        long last_row_index;

        open();

        Cursor cursor_state_id = db.query(STATE_TABLE, null, STATE_TABLE_STATE_NAME + "=?", new String[]{state}, null, null, null);
        if (cursor_state_id != null && cursor_state_id.getCount() > 0) {
            cursor_state_id.moveToFirst();
            state_id = cursor_state_id.getString(cursor_state_id.getColumnIndex(STATE_TABLE_STATE_ID));
            cursor_state_id.close();
        }

        Cursor cursor_location_id = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{location_name}, null, null, null);
        if (cursor_location_id != null && cursor_location_id.getCount() > 0) {
            cursor_location_id.moveToFirst();
            location_id = cursor_location_id.getString(cursor_location_id.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor_location_id.close();
        }

        Cursor cursor_bank_id = db.query(BANK_TABLE, null, BANK_TABLE_BANK_NAME + "=?", new String[]{bank}, null, null, null);
        if (cursor_bank_id != null && cursor_bank_id.getCount() > 0) {
            cursor_bank_id.moveToFirst();
            bank_id = cursor_bank_id.getString(cursor_bank_id.getColumnIndex(BANK_TABLE_BANK_ID));
            cursor_bank_id.close();
        }

        Cursor cursor_emp_id = db.query(LOGIN_TABLE, null, null, null, null, null, null);
        if (cursor_emp_id != null && cursor_emp_id.getCount() > 0) {
            cursor_emp_id.moveToFirst();
            emp_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_EMPLOYEE_ID));
            designation_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_DESIGNATION_ID));
            cursor_emp_id.close();
        }

        Cursor cursor_district_id = db.query(DISTRICT_TABLE, null, DISTRICT_TABLE_DISTRICT_NAME + "=?", new String[]{district}, null, null, null);
        if (cursor_district_id != null && cursor_district_id.getCount() > 0) {
            cursor_district_id.moveToFirst();
            district_id = cursor_district_id.getString(cursor_district_id.getColumnIndex(DISTRICT_TABLE_DISTRICT_ID));
            cursor_district_id.close();
        }

        Cursor cursor_branch_id = db.query(BRANCH_TABLE, null, BRANCH_TABLE_BRANCH_NAME + "=?", new String[]{branch}, null, null, null);
        if (cursor_branch_id != null && cursor_branch_id.getCount() > 0) {
            cursor_branch_id.moveToFirst();
            branch_id = cursor_branch_id.getString(cursor_branch_id.getColumnIndex(BRANCH_TABLE_BRANCH_ID));
            cursor_branch_id.close();
        }

        ContentValues values = new ContentValues();

        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID, "0");
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DATE, date);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION, inspection_no);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATE_ID, state_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_LOCATION_ID, location_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_PINCODE, pin_code);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DISTRICT_ID, district_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_GODOWN_ADDRESS, godown_address);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NAME_OF_GODOWN_OWNER, godown_owner);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CC_LOCATION_ID, emp_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BANK_ID, bank_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BRANCH_ID, branch_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_SURVEY_DONE_BY_NAME, emp_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DESIGNATION_ID, designation_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CONTACT_NO, contact_no);
        // values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER, create_date_by_user);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER, getDateTimeWithHHMMSS());
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG, "local");


        last_row_index = db.insert(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, values);

        close();

        return last_row_index;

    }

    public long updateLocalWareHouseInspectionHeader(String date, String inspection_no, String state, String location_name, String pin_code,
                                                     String district, String godown_address, String godown_owner, String cc_location,
                                                     String bank, String branch, String survey_done_by_name, String designation,
                                                     String contact_no, String create_date_by_user, int tempcommonId, int tempInspId, String noofgodowns) {

        String state_id = "", location_id = "", emp_id = "", district_id = "", designation_id = "";

        long last_row_index;

        open();

        Cursor cursor_state_id = db.query(STATE_TABLE, null, STATE_TABLE_STATE_NAME + "=?", new String[]{state}, null, null, null);
        if (cursor_state_id != null && cursor_state_id.getCount() > 0) {
            cursor_state_id.moveToFirst();
            state_id = cursor_state_id.getString(cursor_state_id.getColumnIndex(STATE_TABLE_STATE_ID));
            cursor_state_id.close();
        }

        Cursor cursor_location_id = db.query(LOCATION_TABLE, null, LOCATION_TABLE_LOCATION_NAME + "=?", new String[]{location_name}, null, null, null);
        if (cursor_location_id != null && cursor_location_id.getCount() > 0) {
            cursor_location_id.moveToFirst();
            location_id = cursor_location_id.getString(cursor_location_id.getColumnIndex(LOCATION_TABLE_LOCATION_ID));
            cursor_location_id.close();
        }

        Cursor cursor_emp_id = db.query(LOGIN_TABLE, null, null, null, null, null, null);
        if (cursor_emp_id != null && cursor_emp_id.getCount() > 0) {
            cursor_emp_id.moveToFirst();
            emp_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_EMPLOYEE_ID));
            // designation_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_DESIGNATION_ID));
            cursor_emp_id.close();
        }

        Cursor cursor_district_id = db.query(DISTRICT_TABLE, null, DISTRICT_TABLE_DISTRICT_NAME + "=?", new String[]{district}, null, null, null);
        if (cursor_district_id != null && cursor_district_id.getCount() > 0) {
            cursor_district_id.moveToFirst();
            district_id = cursor_district_id.getString(cursor_district_id.getColumnIndex(DISTRICT_TABLE_DISTRICT_ID));
            cursor_district_id.close();
        }

        ContentValues values = new ContentValues();

        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID, "" + tempInspId);
        // values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION, inspection_no);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION, location_name + "~" + bank + "~" + inspection_no);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATE_ID, state_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_LOCATION_ID, location_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_PINCODE, pin_code);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DISTRICT_ID, district_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_GODOWN_ADDRESS, godown_address);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NO_OF_GODOWNS, noofgodowns);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NAME_OF_GODOWN_OWNER, godown_owner);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_SURVEY_DONE_BY_NAME, emp_id);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER, getDateTimeWithHHMMSS());
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG, "update");

        // values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DATE, date);
        // values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CC_LOCATION_ID, emp_id);
        // values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DESIGNATION_ID, designation_id);
        // values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CONTACT_NO, contact_no);

        last_row_index = db.update(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, values, COMMON_ID + "=?", new String[]{"" + tempcommonId});

        close();

        return last_row_index;

    }


    public String getWareHouseInspectionJosnForServer() {
        String jsonString = "[]";
        String row_no = "";
        String date;
        String inspection_no;
        String state;
        String location_name;
        String pin_code;
        String district;
        String godown_address;
        String godown_owner;
        String cc_location;
        String bank;
        String branch;
        String survey_done_by_name;
        String designation;
        String contact_no;
        String emp_id;
        String create_by_date_by_user;
        String StatusFlag;
        String InspectionID;
        String NoOfGodowns;

        String insepction_name_id;
        String insepction_values;
        String insepction_remark;
        String insepction_type_flag = "";

        Cursor cursor_child_header;
        Cursor cursor_child_inspection;
        Cursor cursor_type;

        JSONObject jsonObject_parent;
        JSONArray jsonArray_final;
        JSONArray jsonArray_child;
        JSONObject jsonObject_child;

        open();

        //cursor_child_header = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, null, null, null, null, null);
        cursor_child_header = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + "=? OR " + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + " =?", new String[]{"local", "update"}, null, null, null);

        if (cursor_child_header.getCount() > 0) {
            jsonArray_final = new JSONArray();
            cursor_child_header.moveToFirst();
            do {
                jsonObject_parent = new JSONObject();

                row_no = cursor_child_header.getString(cursor_child_header.getColumnIndex(COMMON_ID));

                inspection_no = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION));
                state = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATE_ID));
                location_name = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_LOCATION_ID));
                pin_code = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_PINCODE));
                district = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DISTRICT_ID));
                godown_address = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_GODOWN_ADDRESS));
                NoOfGodowns = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NO_OF_GODOWNS));

                godown_owner = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_NAME_OF_GODOWN_OWNER));
                survey_done_by_name = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_SURVEY_DONE_BY_NAME));
                create_by_date_by_user = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CREATED_DATE_BY_USER));
                StatusFlag = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG));
                InspectionID = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID));

                // date = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DATE));
                // cc_location = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CC_LOCATION_ID));
                // bank = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BANK_ID));
                // branch = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_BRANCH_ID));
                // designation = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_DESIGNATION_ID));
                // contact_no = cursor_child_header.getString(cursor_child_header.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_CONTACT_NO));


                Cursor cursor_emp_id = db.query(LOGIN_TABLE, null, null, null, null, null, null);
                cursor_emp_id.moveToFirst();
                emp_id = cursor_emp_id.getString(cursor_emp_id.getColumnIndex(LOGIN_EMPLOYEE_ID));

                try {

                    jsonObject_parent.put("inspection_no", inspection_no);
                    jsonObject_parent.put("state", state);
                    jsonObject_parent.put("location_name", location_name);
                    jsonObject_parent.put("pin_code", pin_code);
                    jsonObject_parent.put("district", district);
                    jsonObject_parent.put("godown_address", godown_address);
                    jsonObject_parent.put("godown_owner", godown_owner);
                    jsonObject_parent.put("survey_done_by_name", survey_done_by_name);
                    jsonObject_parent.put("created_by", emp_id);
                    jsonObject_parent.put("created_date", create_by_date_by_user);
                    jsonObject_parent.put("status_flag", StatusFlag);
                    jsonObject_parent.put("inspection_id", InspectionID);
                    jsonObject_parent.put("no_of_godowns", NoOfGodowns);

                    // jsonObject_parent.put("date", date);
                    // jsonObject_parent.put("cc_location", cc_location);
                    // jsonObject_parent.put("bank", bank);
                    // jsonObject_parent.put("branch", branch);
                    // jsonObject_parent.put("designation", designation);
                    // jsonObject_parent.put("contact_no", contact_no);

                    cursor_child_inspection = db.query(LOCAL_WAREHOUSE_INSPECTION_TABLE, null, LOCAL_WAREHOUSE_INSPECTION_TABLE_ROW_FLAG + "=?", new String[]{row_no}, null, null, null);

                    if (cursor_child_inspection != null && cursor_child_inspection.getCount() > 0) {
                        jsonArray_child = new JSONArray();
                        cursor_child_inspection.moveToFirst();
                        do {
                            jsonObject_child = new JSONObject();

                            insepction_name_id = cursor_child_inspection.getString(cursor_child_inspection.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_TABLE_NAME_ID));
                            insepction_values = cursor_child_inspection.getString(cursor_child_inspection.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_TABLE_VALUES));
                            insepction_remark = cursor_child_inspection.getString(cursor_child_inspection.getColumnIndex(LOCAL_WAREHOUSE_INSPECTION_TABLE_REMARK));

                            cursor_type = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NAME_ID + "=?", new String[]{insepction_name_id}, null, null, null);
                            cursor_type.moveToFirst();

                            if (cursor_type.getString(cursor_type.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_DATE)).contains("1")) {
                                insepction_type_flag = "DATE";
                            } else if (cursor_type.getString(cursor_type.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_BOOLEAN)).contains("1")) {
                                insepction_type_flag = "BOOLEAN";
                            } else if (cursor_type.getString(cursor_type.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_NUMBER_VALIDATION)).contains("1")) {
                                insepction_type_flag = "NUMBER";
                            } else if (cursor_type.getString(cursor_type.getColumnIndex(WAREHOUSE_INSPECTION_DYNAMIC_TABLE_TEXT_VALIDATION)).contains("1")) {
                                insepction_type_flag = "TEXT";
                            }
                            jsonObject_child.put("insepction_type_flag", insepction_type_flag);
                            jsonObject_child.put("insepction_name_id", insepction_name_id);
                            jsonObject_child.put("insepction_values", insepction_values);
                            jsonObject_child.put("insepction_remark", insepction_remark);

                            cursor_type.close();
                            jsonArray_child.put(jsonObject_child);
                        }
                        while (cursor_child_inspection.moveToNext());
                        jsonObject_parent.put("insepction_row", jsonArray_child);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray_final.put(jsonObject_parent);
            } while (cursor_child_header.moveToNext());
            cursor_child_header.close();
            jsonString = jsonArray_final.toString();
        }

        close();

        Log.e("json Obj", "json obj = " + jsonString.toString());

        //return jsonString;
        return row_no + "," + jsonString;


    }


    public void deleteLocalAttendance(String index) {
        open();
        db.delete(LOCAL_ATTENDANCE_TABLE, COMMON_ID + "=?", new String[]{index});
        close();
    }

    public void deleteLocalDeposit(String index) {
        open();
        int count;
        Log.e("Primary key", index);

        final String MY_QUERY = "DELETE " +
                " FROM " + DEPOSIT_TABLE +
                " WHERE " + LOCAL_DEPOSIT_TABLE_DEPOSIT_ID + " = '" + index + "'";

        Cursor cursor = db.rawQuery(MY_QUERY, null);
        Log.e("Deposite Delete Query", MY_QUERY);


        Cursor cursor3 = db.query(DEPOSIT_TABLE, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }
        String Scount = "";
        Scount = String.valueOf(count);
        Log.e("Header rows Count", Scount);


        final String MY_QUERY2 = "DELETE " +
                " FROM " + DEPOSIT_TABLE_DETAILS +
                " WHERE " + LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + " = '" + index + "'";
        Cursor cursor2 = db.rawQuery(MY_QUERY2, null);
        Log.e("DepositeDetail Delete Query", MY_QUERY2);

        Cursor cursor4 = db.query(DEPOSIT_TABLE_DETAILS, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }
        String Scount2 = "";
        Scount2 = String.valueOf(count);
        Log.e("Details rows count", Scount2);

/*
        db.delete(DEPOSIT_TABLE, LOCAL_DEPOSIT_TABLE_DEPOSIT_ID + "=?", new String[]{index});
        db.delete(DEPOSIT_TABLE_DETAILS, LOCAL_DEPOSIT_DETAILS_TABLE_DEPOSIT_ID + "=?", new String[]{index});*/
        close();
    }

    public void deleteLocalDepositNext(String index) {
        open();
        db.delete(DEPOSIT_TABLE_DETAILS, COMMON_ID + "=?", new String[]{index});
        close();
    }


    public void deleteLocalGeoTagMapping() {
        open();
        db.delete(LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE, null, null);
        close();
    }


    public void deleteLocalGodownAudit(String audit_index) {

        open();

        Cursor cursor = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, COMMON_ID + "=?", new String[]{audit_index}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String audit_point_index = cursor.getString(cursor.getColumnIndex(LOCAL_GODOWN_AUDIT_TABLE_ONE_ROW_CHECKER));
            db.delete(LOCAL_GODOWN_AUDIT_TABLE, COMMON_ID + "=?", new String[]{audit_index});
            db.delete(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE, LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE_ONE_ROW_CHECKER + "=?", new String[]{audit_point_index});
            db.delete(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE, LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_ONE_ROW_CHECKER + "=?", new String[]{audit_point_index});
            cursor.close();
        }
        close();

    }

    /*
    public void deleteLocalWareHouseInspection()
    {
        open();
        db.delete(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, null);
        db.delete(LOCAL_WAREHOUSE_INSPECTION_TABLE, null, null);
        // db.delete(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, null);
        close();
    }
    */

    // Old Extra
    public void deleteLocalWareHouseInspection() {
        open();
        db.delete(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, null);
        db.delete(LOCAL_WAREHOUSE_INSPECTION_TABLE, null, null);
        // db.delete(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, null);
        close();
    }


    public void deleteLocalWareHouseInspection(String index, String tempInspId) {
        open();

        // Delete Temp Waehouse Inspection Detail Table
        db.delete(LOCAL_WAREHOUSE_INSPECTION_TABLE, COMMON_ID + "=?", new String[]{index});

        // Update Waehouse Inspection Process Table
        ContentValues values = new ContentValues();
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_INSPECTION_ID, tempInspId);
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG, "server");
        db.update(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, values, COMMON_ID + "=?", new String[]{index});
        close();
    }

    // Test Table To Delete All Local Warehouse Inspection Header
    public void deleteAllLocalWareHouseInspection() {
        open();
        db.delete(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + "=? OR " + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + " =?", new String[]{"local", "update"});
        close();
    }


    public void deleteLocalGodownAuditHeaderLastInsertedRow(long last_inserted_row) {

        open();

        db.delete(LOCAL_GODOWN_AUDIT_TABLE, COMMON_ID + "=?", new String[]{"" + last_inserted_row});

        close();

    }

    public void deleteLocalGodownAuditAllAuditPointsLastInsertedRow(long last_inserted_row) {

        open();

        db.delete(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE, LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE_ONE_ROW_CHECKER + "=?", new String[]{"" + last_inserted_row});

        close();

    }

    public void deleteLocalWareHouseInspectionHeaderLastRowInserted(long last_inserted_row) {
        open();

        ContentValues values = new ContentValues();
        values.put(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG, "server");
        db.update(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, values, COMMON_ID + "=?", new String[]{"" + last_inserted_row});

        db.delete(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, COMMON_ID + "=? AND " + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + "= ? ", new String[]{"" + last_inserted_row, "local"});
        close();

    }


    private String getDateTimeWithHHMMSS() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }


    public boolean checkLocalDepositCount() {
        open();

        boolean flag = false;
        Cursor cursor = db.query(DEPOSIT_TABLE, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            flag = true;
        }
        close();

        return flag;
    }


    public boolean checkLocalAtteanceCount() {
        open();

        boolean flag = false;
        Cursor cursor = db.query(LOCAL_ATTENDANCE_TABLE, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            flag = true;
        }

        close();

        return flag;

    }


    public boolean checkLocalGeoTagMappingCount() {
        open();

        boolean flag = false;
        Cursor cursor = db.query(LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            flag = true;
        }

        close();

        return flag;

    }


    public boolean checkLocalGodownAuditPointCount() {
        open();

        boolean flag = false;
        Cursor cursor = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            flag = true;
        }

        close();

        return flag;

    }


    public boolean checkLocalWareHouseInspectionCount() {
        open();

        boolean flag = false;
        // Cursor cursor = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, null, null, null, null, null);
        Cursor cursor = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + "=? OR " + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + " =?", new String[]{"local", "update"}, null, null, null);

        if (cursor.getCount() > 0) {
            flag = true;
        }

        close();

        return flag;

    }


    public void deleteAllTable() {

        open();

        db.delete(LOCAL_ATTENDANCE_TABLE, null, null);
        //db.delete(LOGIN_TABLE, null, null);
        db.delete(STATE_TABLE, null, null);
        db.delete(LOCATION_TABLE, null, null);
        db.delete(SEGMENT_TABLE, null, null);
        db.delete(GODOWN_TABLE, null, null);
        db.delete(AUDIT_TYPE_TABLE, null, null);
        db.delete(AUDITOR_TABLE, null, null);
        db.delete(WAREHOUSE_TABLE, null, null);
        db.delete(AUDIT_POINT_TABLE, null, null);
        db.delete(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, null);
        db.delete(WAREHOUSE_INSPECTION_DYNAMIC_TEMP_TABLE, null, null);
        db.delete(LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE, null, null);
        db.delete(LOCAL_GODOWN_AUDIT_TABLE, null, null);
        db.delete(LOCAL_GODOWN_AUDIT_POINT_DETAILS_TABLE, null, null);
        db.delete(LOCAL_GODOWN_AUDIT_POINT_DETAILS_FINAL_TABLE, null, null);
        db.delete(LOCAL_WAREHOUSE_INSPECTION_TABLE, null, null);
        db.delete(BANK_TABLE, null, null);
        db.delete(BRANCH_TABLE, null, null);
        db.delete(DISTRICT_TABLE, null, null);
        db.delete(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, null);
        db.delete(USER_ROLE_TABLE, null, null);
        db.delete(LOCAL_UPDATED_DATABASE_DATETIME_TABLE, null, null);

        close();

    }


    public boolean checkDropDownMandatory(String particular_name) {

        boolean flag = false;
        open();

        Cursor cursor = db.query(WAREHOUSE_INSPECTION_DYNAMIC_TABLE, null, WAREHOUSE_INSPECTION_DYNAMIC_TABLE_PARTICULAR + " = ? AND "
                        + WAREHOUSE_INSPECTION_DYNAMIC_TABLE_MULTI_SELECT_VALIDATION + "=?",
                new String[]{particular_name, "1"}, null, null, null);

        if (cursor.getCount() > 0) {
            flag = true;
        }


        close();


        return flag;
    }

    public void setLocationNameAndState(double lat, double lang, Context context) {

        try {

            Geocoder geocoder;
            List<Address> addresses;
            String result = "No Address found.";
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, lang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String location_name = addresses.get(0).getLocality();
            String state_name = addresses.get(0).getAdminArea();

            Address address = addresses.get(0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append(",");
            }
            sb.append(address.getLocality()).append(",");
            sb.append(address.getPostalCode()).append(",");
            sb.append(address.getCountryName());
            result = sb.toString();
            local_addressTemp = result;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public int getGeoTagMappinglocalDBCount() {
        open();
        int count = 0;
        Cursor cursor = db.query(LOCAL_GODOWN_GEO_TAG_MAPPING_TABLE, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }
        close();
        return count;
    }

    public int getAttendancelocalDBCount() {
        open();
        int count = 0;
        Cursor cursor = db.query(LOCAL_ATTENDANCE_TABLE, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }
        close();
        return count;
    }

    public int getWareHouseInspectionlocalDBCount() {
        open();
        int count = 0;
        //Cursor cursor = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, null, null, null, null, null);
        Cursor cursor = db.query(LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE, null, LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + "=? OR " + LOCAL_WAREHOUSE_INSPECTION_HEADER_TABLE_STATUS_FLAG + " =?", new String[]{"local", "update"}, null, null, null);

        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }
        close();
        return count;
    }

    public int getGodownAuditPointlocalDBCount() {
        open();
        int count = 0;
        Cursor cursor = db.query(LOCAL_GODOWN_AUDIT_TABLE, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }
        close();
        return count;
    }
    //DEPOSITE TOTAL ROWS COUNT START

    public int getDepositlocalDBCount() {
        open();
        int count = 0;
        Cursor cursor = db.query(DEPOSIT_TABLE, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            count = cursor.getCount();
        } else {
            count = 0;
        }
        close();
        return count;
    }
    //DEPOSITE TOTAL ROWS COUNT END


    // Test Delete ALL Attendance
    public void deleteAllLocalAttendance() {
        open();
        db.delete(LOCAL_ATTENDANCE_TABLE, null, null);
        close();
    }


    // Deposite


}
