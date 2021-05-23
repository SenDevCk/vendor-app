package org.nic.lmd.wenderapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.adapters.RenewalInstrumentAdapter;
import org.nic.lmd.wenderapp.adapters.RenewalWeightAdapter;
import org.nic.lmd.wenderapp.asynctask.RenewalService;
import org.nic.lmd.wenderapp.asynctask.UploadTradorService;
import org.nic.lmd.wenderapp.asynctask.VenderDataForBillingLoader;
import org.nic.lmd.wenderapp.asynctask.VenderDataSingle;
import org.nic.lmd.wenderapp.interfaces.VendorDataListener;
import org.nic.lmd.wenderapp.utilities.Utiilties;

public class RenawalActivity extends AppCompatActivity {

    RecyclerView recycler_weight, recycler_instrument;
    Button button_renew;
    Toolbar toolbar;
    RenewalWeightAdapter renewal_weight_adapter;
    RenewalInstrumentAdapter renewalInstrumentAdapter;
    public static JSONObject jsonObject, ori_json;
    TextView msg_weight, msg_instrument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renawal);
        toolbar =  findViewById(R.id.toolbar_ren);
        toolbar.setTitle("Renewal Details");
        toolbar.setSubtitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RenawalActivity.super.onBackPressed();
            }
        });

        msg_instrument = findViewById(R.id.msg_instrument);
        msg_weight = findViewById(R.id.msg_weight);
        button_renew = findViewById(R.id.button_renew);
        recycler_weight = findViewById(R.id.recycler_weight);
        recycler_instrument = findViewById(R.id.recycler_instrument);
        callService(getIntent().getStringExtra("venderid"));

        button_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(RenawalActivity.this)
                        .setTitle("Renew")
                        .setMessage("Really want to renew")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("before", jsonObject.toString());
                                int max_vc_id = 0;

                                try {
                                    JSONArray jsonArray_vc = jsonObject.getJSONArray("vcs");
                                    for (int i = 0; i < jsonArray_vc.length(); i++) {
                                        JSONObject jsonObject_vc = jsonArray_vc.getJSONObject(i);
                                        if (max_vc_id < jsonObject_vc.getInt("vcId")) {
                                            max_vc_id = jsonObject_vc.getInt("vcId");
                                        }
                                    }
                                    JSONObject new_vc = new JSONObject();
                                    new_vc.accumulate("nextVcDate", JSONObject.NULL);
                                    new_vc.accumulate("vcDate", JSONObject.NULL);
                                    new_vc.accumulate("vcId", max_vc_id + 1);
                                    new_vc.accumulate("vcNumber", JSONObject.NULL);
                                    new_vc.accumulate("vendorId", jsonObject.getString("vendorId"));
                                    jsonObject.getJSONArray("vcs").put(jsonArray_vc.length(), new_vc);
                                    manipulateDataForWeightAndInstrument(true, max_vc_id);
                                    manipulateDataForWeightAndInstrument(false, max_vc_id);
                                    jsonObject.put("status","RFR");
                                    Log.d("json_manipulated", jsonObject.toString());
                                } catch (Exception e) {
                                    Log.e("json error", e.getMessage());
                                }
                                Log.d("resultent json \n", jsonObject.toString());
                                new RenewalService(RenawalActivity.this,jsonObject).execute();
                            }
                        }).create().show();
            }
        });
    }

    private void manipulateDataForWeightAndInstrument(boolean flag, int max_vc_id) {
        try {
            JSONArray jsonArray_man = (flag) ? jsonObject.getJSONArray("weights") : jsonObject.getJSONArray("instruments");
            for (int i = 0; i < jsonArray_man.length(); i++) {
                JSONObject jsonObject_man = jsonArray_man.getJSONObject(i);
                if (jsonObject_man.getString("status").equals(JSONObject.NULL)) {
                    if (flag) {
                        jsonObject.getJSONArray("weights").getJSONObject(i).put("status", "E");
                    } else {
                        jsonObject.getJSONArray("instruments").getJSONObject(i).put("status", "E");
                    }
                } else {
                     if (jsonObject_man.getBoolean("mCheck") && jsonObject_man.getString("status").equals("D")) {
                        JSONObject new_man = jsonObject_man;
                        new_man.put("status", "A");
                        new_man.put("vcId", max_vc_id + 1);
                        new_man.remove("mCheck");
                        new_man.remove("isDeleted");
                        if (flag) {
                            jsonObject.getJSONArray("weights").getJSONObject(i).put("status", "E");
                            jsonObject.getJSONArray("weights").put(jsonObject.getJSONArray("weights").length(), new_man);
                        } else {
                            jsonObject.getJSONArray("instruments").getJSONObject(i).put("status", "E");
                            jsonObject.getJSONArray("instruments").put(jsonObject.getJSONArray("instruments").length(), new_man);
                        }
                    } /*else if (jsonObject_man.getBoolean("isDeleted")) {
                        if (flag) {
                            jsonObject.getJSONArray("weights").getJSONObject(i).put("status", "E");
                        } else {
                            jsonObject.getJSONArray("instruments").getJSONObject(i).put("status", "E");
                        }
                    }*/
                    if (flag) {
                        jsonObject.getJSONArray("weights").getJSONObject(i).remove("isDeleted");
                        jsonObject.getJSONArray("weights").getJSONObject(i).remove("mCheck");
                    } else {
                        jsonObject.getJSONArray("instruments").getJSONObject(i).remove("isDeleted");
                        jsonObject.getJSONArray("instruments").getJSONObject(i).remove("mCheck");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callService(String vendor_id) {
        VenderDataSingle.listenVendor(new VendorDataListener() {
            @Override
            public void responseFound(String res) {
                Log.e("data", res);
                try {
                    JSONObject jsonObjectres = new JSONObject(res);
                    jsonObject = jsonObjectres.getJSONObject("data");
                    ori_json = jsonObject;
                    populateRecyclerView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void responseNotFound(String msg) {
                Toast.makeText(RenawalActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
            }
        });
        if (Utiilties.isOnline(RenawalActivity.this)) {
            new VenderDataSingle(RenawalActivity.this).execute(vendor_id);
        } else {
            Toast.makeText(RenawalActivity.this, "Internet not avalable !", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateRecyclerView() {
        try {
            if (jsonObject.getJSONArray("weights").length() > 0) {
                recycler_weight.invalidate();
                renewal_weight_adapter = new RenewalWeightAdapter(RenawalActivity.this);
                RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(RenawalActivity.this);
                recycler_weight.setLayoutManager(mLayoutManager2);
                recycler_weight.setItemAnimator(new DefaultItemAnimator());
                recycler_weight.setAdapter(renewal_weight_adapter);
            } else {
                msg_weight.append(" not avalable");
                msg_weight.setTextSize(12);
                msg_weight.setTextColor(getResources().getColor(R.color.design_default_color_error));
            }
            if (jsonObject.getJSONArray("instruments").length() > 0) {
                recycler_instrument.invalidate();
                renewalInstrumentAdapter = new RenewalInstrumentAdapter(RenawalActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RenawalActivity.this);
                recycler_instrument.setLayoutManager(mLayoutManager);
                recycler_instrument.setItemAnimator(new DefaultItemAnimator());
                recycler_instrument.setAdapter(renewalInstrumentAdapter);
            } else {
                msg_instrument.append(" not avalable");
                msg_instrument.setTextSize(12);
                msg_instrument.setTextColor(getResources().getColor(R.color.design_default_color_error));
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg_weight.setText(e.getMessage());
            msg_weight.setTextSize(12);
            msg_weight.setTextColor(getResources().getColor(R.color.design_default_color_error));
            msg_instrument.setVisibility(View.GONE);
            button_renew.setVisibility(View.GONE);
        }
    }
}