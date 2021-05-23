package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;
import org.nic.lmd.wenderapp.activities.MainActivity;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;

public class RegisterLoader extends AsyncTask<String, Void, String> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public RegisterLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {
        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("Processing...");
        this.dialog1.setCancelable(false);
        this.dialog1.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json_res = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("natureOfBusiness", Integer.parseInt(strings[0]));
            jsonObject.accumulate("applicantName", strings[1]);
            jsonObject.accumulate("designation", Integer.parseInt(strings[2]));
            jsonObject.accumulate("address1", strings[3]);
            jsonObject.accumulate("address2", strings[4]);
            jsonObject.accumulate("country", strings[5]);
            jsonObject.accumulate("state", strings[6]);
            jsonObject.accumulate("city", strings[7]);
            jsonObject.accumulate("district", Integer.parseInt(strings[8]));
            jsonObject.accumulate("landMark", strings[9]);
            jsonObject.accumulate("pinCode", Long.parseLong(strings[10]));
            jsonObject.accumulate("mobileNo", strings[11]);
            jsonObject.accumulate("contactNo", strings[12]);
            jsonObject.accumulate("emailId", strings[13]);
            jsonObject.accumulate("contactPerson", strings[14]);
            jsonObject.accumulate("contactMobileNo", strings[15]);
            jsonObject.accumulate("userId", strings[16]);
            jsonObject.accumulate("password", strings[17]);
            jsonObject.accumulate("confirm", strings[17]);
            jsonObject.accumulate("manufacturer", Boolean.parseBoolean(strings[18]));
            jsonObject.accumulate("dealer", Boolean.parseBoolean(strings[19]));
            jsonObject.accumulate("importer", Boolean.parseBoolean(strings[20]));
            jsonObject.accumulate("trader", Boolean.parseBoolean(strings[21]));
            jsonObject.accumulate("packer", Boolean.parseBoolean(strings[22]));
            jsonObject.accumulate("repairer", Boolean.parseBoolean(strings[23]));
            json_res = WebHandler.callByPost(jsonObject.toString(), Urls_this_pro.LOAD_REGISTER);
        } catch (Exception e) {
            e.printStackTrace();
            return json_res;
        }


        return json_res;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        if (this.dialog1.isShowing()) dialog1.dismiss();
        if (res != null) {
            try {
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.has("statusCode")) {
                    if (jsonObject.getInt("statusCode") == 200) {
                        Toast.makeText(activity, "Success : " + jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "" + jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        alertDialog.setMessage("" + jsonObject.getString("data"));
                        alertDialog.show();
                    }
                } else {
                    Log.e("error", "Status code not found in json");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, "Server Problem Or Somthing went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","res null on RegisterLoader");
        }
    }


}

