package com.coderbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.database.DbHelper;

public class ConnectivityReceiver extends BroadcastReceiver
{
    public ConnectivityReceiver()
    {


    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // throw new UnsupportedOperationException("Not yet implemented");

        UpdateToServerAllLocalDatabase commonServerSenderHelper;


        String status = ConnectionUtil.getConnectivityStatusString(context);

        //boolean flag = getIsFirstNetPref(context);

        DbHelper dbHelper = new DbHelper(context);


        if (status.contains("enabled") && !(status.contains("Not connected to Internet")))
            {
            commonServerSenderHelper = new UpdateToServerAllLocalDatabase(context);
            //commonServerSenderHelper.DepositPostTest();

            if (dbHelper.checkLocalGeoTagMappingCount())
            {
                commonServerSenderHelper.geoTagMappingPostTest();
            }
            else if (dbHelper.checkLocalWareHouseInspectionCount())
            {
                commonServerSenderHelper.wareHouseInspectionPostTest();
            }
            else if (dbHelper.checkLocalAtteanceCount())
            {
                commonServerSenderHelper.attendancePostTest();
            }
            else if (dbHelper.checkLocalDepositCount())
            {
                commonServerSenderHelper.DepositPostTest();
            }
            else if (dbHelper.checkLocalGodownAuditPointCount())
            {
                commonServerSenderHelper.auditPointPostTest();
            }

            //commonServerSenderHelper.geoTagMappingPostTest();
        }
        else
        {
            Log.v("codition false", "status.contains(enabled) && !(status.contains(Not connected to Internet)");
        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();

    }


    /*
    private void putIsFirstNetPref(boolean value, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_net_first", value);
        editor.commit();
    }

    private boolean getIsFirstNetPref(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("is_net_first", true);
    }
    */

}
