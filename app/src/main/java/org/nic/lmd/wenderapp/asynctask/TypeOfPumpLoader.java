package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.nic.lmd.wenderapp.activities.ApplyNewActivity;
import org.nic.lmd.wenderapp.activities.MainActivity;
import org.nic.lmd.wenderapp.entities.District;
import org.nic.lmd.wenderapp.entities.TypeOfPump;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;
import org.nic.lmd.wenderapp.webHandler.WebServiceHelper;

import java.util.ArrayList;

public class TypeOfPumpLoader extends AsyncTask<String, Void, ArrayList<TypeOfPump>> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public TypeOfPumpLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {

        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("TYPE OF PUMP LOADING...");
        this.dialog1.show();
    }

    @Override
    protected ArrayList<TypeOfPump> doInBackground(String... strings) {
        String json_res = WebHandler.callByGet(Urls_this_pro.LOAD_DISTRICT);

        return WebServiceHelper.typeOFPumpParser(json_res);
    }

    @Override
    protected void onPostExecute(ArrayList<TypeOfPump> res) {
        super.onPostExecute(res);
        if (res != null) {
            if (res.size() > 0) {
                long c = new DataBaseHelper(activity).saveTypeOfPump(res);
                Log.d("TypeOfPump", "Total= " + c);
                if (dialog1.isShowing()) dialog1.dismiss();
                if (c > 0) {
                    new ProposalTypeLoader(activity).execute();
                } else {
                    Toast.makeText(activity, "TypeOfPump not saved !", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(activity, "No data found !", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","res null on TypeOfPumpLoader");
        }
    }
}

