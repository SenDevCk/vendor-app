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
import org.nic.lmd.wenderapp.entities.PremisesTypeEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;
import org.nic.lmd.wenderapp.webHandler.WebServiceHelper;

import java.util.ArrayList;

public class PremisesLoader extends AsyncTask<String,Void, ArrayList<PremisesTypeEntity>> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public PremisesLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {

        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("PREMISSES LOADING...");
        this.dialog1.show();
    }

    @Override
    protected ArrayList<PremisesTypeEntity> doInBackground(String... strings) {
        String json_res= WebHandler.callByGet(Urls_this_pro.LOAD_PREMISES);

        return WebServiceHelper.premisesParser(json_res);
    }

    @Override
    protected void onPostExecute(ArrayList<PremisesTypeEntity> res) {
        super.onPostExecute(res);
        if (res!=null){
            if (res.size()>0){
                long c=new DataBaseHelper(activity).savePremises(res);
                Log.d("Premises","Total= "+c);
                if (dialog1.isShowing())dialog1.dismiss();
                if (c>0){
                    CommonPref.setCheckUpdate(activity,true);
                    if (CommonPref.getCheckUpdate(activity)) {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    }else{
                        Toast.makeText(activity, "Something went Wrong !", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(activity, "Premises not saved !", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(activity, "No data found !", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","res null on PremisesLoader");
        }
    }
}

