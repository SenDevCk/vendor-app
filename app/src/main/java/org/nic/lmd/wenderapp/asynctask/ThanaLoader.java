package org.nic.lmd.wenderapp.asynctask;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.nic.lmd.wenderapp.entities.SubDivision;
import org.nic.lmd.wenderapp.entities.ThanaEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;
import org.nic.lmd.wenderapp.webHandler.WebServiceHelper;

import java.util.ArrayList;

public class ThanaLoader extends AsyncTask<String, Void, ArrayList<ThanaEntity>> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public ThanaLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {

        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("THANA LOADING...");
        this.dialog1.show();
    }

    @Override
    protected ArrayList<ThanaEntity> doInBackground(String... strings) {
        String json_res = WebHandler.callByGet(Urls_this_pro.LOAD_THANA_ALL);
        return WebServiceHelper.thanaParser(json_res);
    }

    @Override
    protected void onPostExecute(ArrayList<ThanaEntity> res) {
        super.onPostExecute(res);
        if (res != null) {
            if (res.size() > 0) {
                long c = new DataBaseHelper(activity).saveThana(res);
                Log.d("THANA", "Total= " + c);
                if (dialog1.isShowing()) dialog1.dismiss();
                if (c > 0) {
                    new DesignationLoader(activity).execute();
                } else {
                    Toast.makeText(activity, "Thana not saved !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "No data found(Thana) !", Toast.LENGTH_SHORT).show();
            }
        }else {
            Log.e("log", "null on ThanaLoader");
            Toast.makeText(activity, "No data found(Thana) !", Toast.LENGTH_SHORT).show();
        }
    }
}

