package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.ApplicationBillingDetailsActivity;
import org.nic.lmd.wenderapp.activities.ApplyNewActivity;
import org.nic.lmd.wenderapp.activities.MainActivity;
import org.nic.lmd.wenderapp.activities.RenawalActivity;
import org.nic.lmd.wenderapp.asynctask.FirmTypeLoader;
import org.nic.lmd.wenderapp.asynctask.VenderDataForBillingLoader;
import org.nic.lmd.wenderapp.asynctask.VenderDataSingle;
import org.nic.lmd.wenderapp.entities.UserData;
import org.nic.lmd.wenderapp.interfaces.VendorDataListener;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.uploadFileUtility.PdfOpenHelper;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.Utiilties;


public class VendorAdapter extends BaseAdapter {
    Activity activity;
    String res;
    LayoutInflater layoutInflater;

    public VendorAdapter(Activity activity, String res) {
        this.activity = activity;
        layoutInflater = this.activity.getLayoutInflater();
        this.res = res;
    }

    @Override
    public int getCount() {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(res);
            return jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View rootview, ViewGroup viewGroup) {
        rootview = layoutInflater.inflate(R.layout.application_list_item, null, false);
        ViewHolder viewHolder = new ViewHolder();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(res);
            final JSONObject jsonObject = jsonArray.getJSONObject(position);
            viewHolder.ll_renew = rootview.findViewById(R.id.ll_renew);
            viewHolder.ll_edit = rootview.findViewById(R.id.ll_edit);
            viewHolder.text_ven_id = rootview.findViewById(R.id.text_ven_id);
            viewHolder.text_ven_id.setText(jsonObject.getString("vendorId"));
            viewHolder.text_soap = rootview.findViewById(R.id.text_soap);
            viewHolder.text_soap.setText("" + jsonObject.getString("nameofShop"));
            viewHolder.text_pre_type = rootview.findViewById(R.id.text_pre_type);
            viewHolder.text_pre_type.setText("[ " + new DataBaseHelper(activity).getPremissesByID(jsonObject.getString("premisesType")).getName() + " ]");
            viewHolder.text_mob = rootview.findViewById(R.id.text_mob);
            viewHolder.text_mob.setText("" + jsonObject.getString("mobileNo"));
            viewHolder.button_renew = rootview.findViewById(R.id.button_renew);
            viewHolder.button_renew.setPaintFlags(viewHolder.button_renew.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            viewHolder.button_upload = rootview.findViewById(R.id.button_upload);
            viewHolder.button_upload.setPaintFlags(viewHolder.button_upload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            viewHolder.button_cert = rootview.findViewById(R.id.button_cert);
            viewHolder.button_cert.setPaintFlags(viewHolder.button_cert.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            viewHolder.button_pay = rootview.findViewById(R.id.button_pay);
            viewHolder.button_pay.setPaintFlags(viewHolder.button_pay.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            viewHolder.button_view = rootview.findViewById(R.id.button_view);
            viewHolder.button_view.setPaintFlags(viewHolder.button_view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            viewHolder.text_status = rootview.findViewById(R.id.text_status);
            viewHolder.button_edit = rootview.findViewById(R.id.button_edit);
            viewHolder.button_edit.setPaintFlags(viewHolder.button_edit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            viewHolder.button_cert.setVisibility(View.GONE);
            viewHolder.ll_edit.setVisibility(View.GONE);
            viewHolder.ll_renew.setVisibility(View.GONE);
            //viewHolder.button_pay.setVisibility(View.GONE);
            if (!jsonObject.isNull("status")) {
                if (jsonObject.getString("status").equals("INC")) {
                    viewHolder.text_status.setText("Application Submitted");
                    viewHolder.text_status.setTextColor(activity.getResources().getColor(android.R.color.black));
                    viewHolder.ll_edit.setVisibility(View.VISIBLE);
                } else if (jsonObject.getString("status").equals("RCV")) {
                    viewHolder.text_status.setText("Application Received");
                    viewHolder.text_status.setTextColor(activity.getResources().getColor(R.color.orange));
                } else if (jsonObject.getString("status").equals("CAL")) {
                    viewHolder.ll_renew.setVisibility(View.VISIBLE);
                    viewHolder.button_pay.setVisibility(View.VISIBLE);
                    viewHolder.text_status.setText("Ready for Payment");
                    viewHolder.text_status.setTextColor(activity.getResources().getColor(R.color.greenDark));
                } else if (jsonObject.getString("status").equals("PMR")) {
                    viewHolder.button_cert.setVisibility(View.VISIBLE);
                    viewHolder.text_status.setText("Payment Completed");
                    viewHolder.text_status.setTextColor(activity.getResources().getColor(R.color.greenDark));
                } else if (jsonObject.getString("status").equals("RFR")) {
                    viewHolder.text_status.setText("Requested For Renewal");
                    viewHolder.text_status.setTextColor(activity.getResources().getColor(R.color.greenDark));
                } else if (jsonObject.getString("status").equals("CRT")) {
                    viewHolder.ll_renew.setVisibility(View.VISIBLE);
                    viewHolder.button_renew.setVisibility(View.VISIBLE);
                    viewHolder.button_cert.setVisibility(View.VISIBLE);
                    viewHolder.text_status.setText("Certificate Generated");
                    viewHolder.text_status.setTextColor(activity.getResources().getColor(R.color.greenDark));
                } else {
                    viewHolder.text_status.setText("" + jsonObject.getString("status"));
                }
            } else {
                viewHolder.text_status.setText("status not avalable");
            }
            viewHolder.button_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ApplyNewActivity.class);
                    try {
                        intent.putExtra("ven_id", jsonObject.getString("vendorId"));
                        intent.putExtra("from", "main");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    activity.startActivity(intent);
                }
            });
            viewHolder.button_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CommonPref.getCheckUpdateForApply(activity)) {
                        try {
                            Intent intent = new Intent(activity, ApplicationBillingDetailsActivity.class);
                            intent.putExtra("venderid", jsonObject.getString("vendorId"));
                            intent.putExtra("status", jsonObject.getString("status"));
                            activity.startActivity(intent);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }else if (!Utiilties.isOnline(activity)){
                        Toast.makeText(activity, "Internet Not Avalable !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new FirmTypeLoader(activity).execute();
                    }
                }
            });

            viewHolder.button_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        callService(jsonObject.getString("vendorId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            viewHolder.button_renew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonPref.getCheckUpdateForApply(activity)) {
                        try {
                            Intent intent = new Intent(activity, RenawalActivity.class);
                            intent.putExtra("venderid", jsonObject.getString("vendorId"));
                            activity.startActivity(intent);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }else if (!Utiilties.isOnline(activity)){
                        Toast.makeText(activity, "Internet Not Avalable !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        new FirmTypeLoader(activity).execute();
                    }
                }
            });

            viewHolder.button_cert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        System.out.println(jsonObject.getString("vendorId")+"---"+jsonObject.getString("subdivId"));
                        PdfOpenHelper.openPdfFromUrl(Urls_this_pro.baseURL + "downloadFile/T/"+jsonObject.getString("subdivId")+"/" + jsonObject.getString("vendorId"),activity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootview;
    }

    private class ViewHolder {
        TextView text_ven_id, text_soap, text_pre_type, text_mob, text_status;
        TextView button_upload, button_view, button_edit, button_renew, button_cert, button_pay;
        LinearLayout ll_renew, ll_edit;
    }

    private void callService(String venderid) {
        VenderDataSingle.listenVendor(new VendorDataListener() {
            @Override
            public void responseFound(String res) {
                DataBaseHelper db = new DataBaseHelper(activity);
                db.deleteAllInstruments();
                db.deleteAllPatner();
                db.updateweightDenomination();
                db.deleteAllNozzle();
                long c = new DataBaseHelper(activity).saveVenderJsonData(res);
                if (CommonPref.getCheckUpdateForApply(activity) && c > 0) {
                    Intent intent = new Intent(activity, ApplyNewActivity.class);
                    intent.putExtra("for", "edit");
                    intent.putExtra("json", res);
                    intent.putExtra("vendorId", venderid);
                    activity.startActivity(intent);
                } else if (!Utiilties.isOnline(activity)) {
                    Toast.makeText(activity, "Internet Not Avalable !", Toast.LENGTH_SHORT).show();
                } else {
                    new FirmTypeLoader(activity).execute();
                }
            }

            @Override
            public void responseNotFound(String msg) {
                Toast.makeText(activity, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });

        if (Utiilties.isOnline(activity)) {
            new VenderDataSingle(activity).execute(venderid);
        } else {
            Toast.makeText(activity, "Internet not avalable !", Toast.LENGTH_SHORT).show();
        }
    }
}
