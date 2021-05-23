package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.nic.lmd.wenderapp.interfaces.VenderListListener;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.WebHandler;


public class VenderListLoader extends AsyncTask<String,Void, String> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;
    private static VenderListListener venderListListener;
    public VenderListLoader(Activity activity) {
        this.activity = activity;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
    }

    @Override
    protected void onPreExecute() {
        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("wait loading...");
        this.dialog1.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json_res=null;
        try{
            json_res= WebHandler.callByGet(Urls_this_pro.LOAD_VENDOR_LIST+strings[0]+"/"+strings[1]);
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
                JSONArray jsonArray = new JSONArray(res);
                if (jsonArray.length()>0){
                    venderListListener.responseFound(res);
                }else{
                    venderListListener.responseNotFound("Data Not Found !");
                }
            }catch (Exception e){
                e.printStackTrace();
                venderListListener.responseNotFound("Exception Found !");
            }
        }else{
            Toast.makeText(activity, "Server Problem Or Somthing went wrong !", Toast.LENGTH_SHORT).show();
            Log.e("log","NUll on Vender VenderListLoader");
        }
    }

    public static void listenVendorList(VenderListListener venderListListener1){
        venderListListener=venderListListener1;
    }
}

