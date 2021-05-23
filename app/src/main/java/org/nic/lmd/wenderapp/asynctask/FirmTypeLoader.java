package org.nic.lmd.wenderapp.asynctask;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.nic.lmd.wenderapp.activities.LoginActivity;
import org.nic.lmd.wenderapp.entities.Designation;
import org.nic.lmd.wenderapp.entities.FirmType;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;
import org.nic.lmd.wenderapp.webHandler.WebServiceHelper;

import java.util.ArrayList;

public class FirmTypeLoader extends AsyncTask<String,Void, ArrayList<FirmType>> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public FirmTypeLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {

        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("FIRM LOADING...");
        this.dialog1.show();
    }

    @Override
    protected ArrayList<FirmType> doInBackground(String... strings) {
        String json_res= WebHandler.callByGet(Urls_this_pro.LOAD_FIRM_TYPE);
        return WebServiceHelper.firmTypeParser(json_res);
    }

    @Override
    protected void onPostExecute(ArrayList<FirmType> res) {
        super.onPostExecute(res);
        if (res!=null){
            if (res.size()>0){
                long c=new DataBaseHelper(activity).saveFirmType(res);
                Log.d("Firm Type","Total= "+c);
                if (dialog1.isShowing())dialog1.dismiss();
                if (c>0){
                     new TypeOfPumpLoader(activity).execute();
                }else {
                    Toast.makeText(activity, "Firm not saved !", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(activity, "No data found !", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","NULL on FirmTypeLoader");
        }
    }
}

