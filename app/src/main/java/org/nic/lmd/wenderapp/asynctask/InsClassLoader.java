package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.nic.lmd.wenderapp.activities.ApplyNewActivity;
import org.nic.lmd.wenderapp.entities.Class_ins;
import org.nic.lmd.wenderapp.entities.District;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;
import org.nic.lmd.wenderapp.webHandler.WebServiceHelper;

import java.util.ArrayList;

public class InsClassLoader extends AsyncTask<String, Void, ArrayList<Class_ins>> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public InsClassLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {

        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("LOADING CLASS...");
        this.dialog1.show();
    }

    @Override
    protected ArrayList<Class_ins> doInBackground(String... strings) {
        String json_res= WebHandler.callByGet(Urls_this_pro.LOAD_CLASS);

        return WebServiceHelper.classParser(json_res);
    }


    @Override
    protected void onPostExecute(ArrayList<Class_ins> res) {
        super.onPostExecute(res);
        if (res!=null){
            if (res.size()>0){
                long c=new DataBaseHelper(activity).saveClass(res);
                Log.d("Class","Total= "+c);
                if (dialog1.isShowing())dialog1.dismiss();
                if (c>0) {
                    new ProductLoader(activity).execute();
                }else{
                    Log.e("error","Class Not Saved");
                }
            }else {
                Toast.makeText(activity, "No data found !", Toast.LENGTH_SHORT).show();
            }
        }else{
            //Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","null on InsClassLoader");
        }
    }
}

