package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.nic.lmd.wenderapp.entities.NatureOfBusiness;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;
import org.nic.lmd.wenderapp.webHandler.WebServiceHelper;

import java.util.ArrayList;

public class NatureOfBusinessLoader extends AsyncTask<String,Void, ArrayList<NatureOfBusiness>> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public NatureOfBusinessLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {

        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("BUSINESS LOADING...");
        this.dialog1.show();
    }

    @Override
    protected ArrayList<NatureOfBusiness> doInBackground(String... strings) {
        String json_res= WebHandler.callByGet(Urls_this_pro.LOAD_NATURE_BUSINESS);
        return WebServiceHelper.natureOfBusinessParser(json_res);
    }

    @Override
    protected void onPostExecute(ArrayList<NatureOfBusiness> res) {
        super.onPostExecute(res);
        if (dialog1.isShowing())dialog1.dismiss();
        if (res!=null){
            if (res.size()>0){
                long c=new DataBaseHelper(activity).saveNatureOfRequest(res);
                Log.d("ActivityGroup","Total= "+c);
                if (dialog1.isShowing())dialog1.dismiss();
                if (c>0){
                    new DistrictLoader(activity).execute();
                }else {
                    Toast.makeText(activity, "Nature of Bussiness not saved !", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(activity, "No data found !", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","null on NatureOfBusinessLoader");
        }
    }
}

