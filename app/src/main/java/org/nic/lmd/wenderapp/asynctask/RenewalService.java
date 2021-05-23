package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.entities.DenomintionEntity;
import org.nic.lmd.wenderapp.entities.InstrumentEntity;
import org.nic.lmd.wenderapp.entities.Nozzle;
import org.nic.lmd.wenderapp.entities.PatnerEntity;
import org.nic.lmd.wenderapp.entities.UserData;
import org.nic.lmd.wenderapp.fragments.UploadDocumentFragment;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.Utiilties;
import org.nic.lmd.wenderapp.utilities.WebHandler;

import java.util.ArrayList;

public class RenewalService extends AsyncTask<String, Void, String> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;
    private JSONObject jsonObject;
    public RenewalService(Activity activity,JSONObject jsonObject) {
        this.activity = activity;
        this.jsonObject=jsonObject;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {
        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("UPLOADING WAIT...");
        this.dialog1.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json_res = null;
     try{
                json_res = WebHandler.callByPost(jsonObject.toString(), Urls_this_pro.LOAD_RENEW);
        } catch (Exception e) {
            e.printStackTrace();
            return json_res;
        }
        return json_res;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        Log.d("res",res);
        if (this.dialog1.isShowing()) dialog1.dismiss();
        if (res != null) {
            try {
                final JSONObject jsonObject1 = new JSONObject(res);
                if (jsonObject1.has("statusCode")) {
                    if (jsonObject1.getInt("statusCode") == 200) {
                        Toast.makeText(activity, "Success : " + jsonObject1.getString("status"), Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }else{
                        Toast.makeText(activity, ""+jsonObject1.getString("remarks"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("error", "Status code not found in json");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("log", "res NULL on RenewalService");
            Toast.makeText(activity, "res null on RenewalService", Toast.LENGTH_SHORT).show();
        }
    }


}

