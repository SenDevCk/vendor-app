package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;
import org.nic.lmd.wenderapp.interfaces.VendorDataListener;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;


public class VenderDataForBillingLoader extends AsyncTask<String,Void, String> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;
    private static VendorDataListener vendorDataListener;
    JSONObject jsonObject;
    //String status;

    public VenderDataForBillingLoader(Activity activity) {
        this.activity = activity;
        //this.status = status;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    public VenderDataForBillingLoader(Activity activity, JSONObject jsonObject) {
        this.activity = activity;
        this.jsonObject=jsonObject;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {
        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("Wait loading...");
        this.dialog1.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json_res=null;
        try{
            if (jsonObject==null) {
                json_res = WebHandler.callByGet(Urls_this_pro.LOAD_VENDOR_BILLING + strings[0].trim());
            }
            else{
                json_res= WebHandler.callByPost(jsonObject.toString(),Urls_this_pro.LOAD_VENDOR_BILLING2);
            }
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
            if (res.equals("")){
                vendorDataListener.responseNotFound("No data Found");
            }else {
                    vendorDataListener.responseFound(res);
            }
        }else{
            vendorDataListener.responseNotFound("Server not Responding VenderDataForBillingLoader");
        }
    }

    public static void listenVendor(VendorDataListener vendorDataListener1){
        vendorDataListener=vendorDataListener1;
    }
}

