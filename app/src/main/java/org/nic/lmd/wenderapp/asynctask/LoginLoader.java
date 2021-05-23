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

public class LoginLoader extends AsyncTask<String,Void, String> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;

    public LoginLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {
        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("LOGIN LOADING...");
        this.dialog1.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json_res=null;
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.accumulate("natureOfBusiness",0);
            jsonObject.accumulate("applicantName","");
            jsonObject.accumulate("designation",0);
            jsonObject.accumulate("address1","");
            jsonObject.accumulate("address2","");
            jsonObject.accumulate("country","");
            jsonObject.accumulate("state","");
            jsonObject.accumulate("city","");
            jsonObject.accumulate("district",0);
            jsonObject.accumulate("landMark","");
            jsonObject.accumulate("pinCode",0);
            jsonObject.accumulate("mobileNo","");
            jsonObject.accumulate("contactNo","");
            jsonObject.accumulate("emailId","");
            jsonObject.accumulate("contactPerson","");
            jsonObject.accumulate("contactMobileNo","");
            jsonObject.accumulate("userId",strings[0]);
            jsonObject.accumulate("password",strings[1]);
            jsonObject.accumulate("manufacturer",false);
            jsonObject.accumulate("dealer",false);
            jsonObject.accumulate("importer",false);
            jsonObject.accumulate("trader",false);
            jsonObject.accumulate("packer",false);
            jsonObject.accumulate("repairer",false);
            json_res= WebHandler.callByPost(jsonObject.toString(), Urls_this_pro.LOG_IN_URL);
        }catch (Exception e){
            e.printStackTrace();
            return json_res;
        }


        return json_res;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        if (dialog1.isShowing())dialog1.dismiss();
        if (res!=null){
            try {
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.has("statusCode")){
                    if (jsonObject.getInt("statusCode") == 1){
                        CommonPref.setUserDetails(activity,res);
                        Intent intent=new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }else{
                        Log.e("log",""+jsonObject.getString("status"));
                        Toast.makeText(activity, "UserId or Password Error", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.e("error","Status code not found in json");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(activity, "Server Problem Or Somthing went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","null on LoginLoader");
        }
    }

}

