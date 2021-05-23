package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.nic.lmd.wenderapp.activities.ApplyNewActivity;
import org.nic.lmd.wenderapp.entities.Class_ins;
import org.nic.lmd.wenderapp.entities.InsProduct;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;
import org.nic.lmd.wenderapp.webHandler.WebServiceHelper;

import java.util.ArrayList;

public class ProductLoader extends AsyncTask<String, Void, ArrayList<InsProduct>> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public ProductLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {

        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("LOADING PRODUCT...");
        this.dialog1.show();
    }

    @Override
    protected ArrayList<InsProduct> doInBackground(String... strings) {
        String json_res= WebHandler.callByGet(Urls_this_pro.LOAD_PRODUCT);

        return WebServiceHelper.productParser(json_res);
    }

    @Override
    protected void onPostExecute(ArrayList<InsProduct> res) {
        super.onPostExecute(res);
        if (res!=null){
            if (res.size()>0){
                long c=new DataBaseHelper(activity).saveProduct(res);
                Log.d("Product","Total= "+c);
                if (dialog1.isShowing())dialog1.dismiss();
                if (c>0) {
                    CommonPref.setCheckUpdateForApply(activity, true);
                    if (CommonPref.getCheckUpdateForApply(activity)) {
                        /*Intent intent = new Intent(activity, ApplyNewActivity.class);
                        activity.startActivity(intent);*/
                        Toast.makeText(activity, "Data Sync Successful ! Click Again !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Something went Wrong !", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(activity, "No data found !", Toast.LENGTH_SHORT).show();
            }
        }else{
            //Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","null on ProductLoader");
        }
    }
}

