package org.nic.lmd.wenderapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.adapters.BillingDetailsAdapter;
import org.nic.lmd.wenderapp.adapters.DocumentRecyclerAdapter;
import org.nic.lmd.wenderapp.adapters.PatnerRecyclerAdapter;
import org.nic.lmd.wenderapp.asynctask.VenderDataForBillingLoader;
import org.nic.lmd.wenderapp.asynctask.VenderDataSingle;
import org.nic.lmd.wenderapp.entities.PatnerEntity;
import org.nic.lmd.wenderapp.interfaces.VendorDataListener;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.Utiilties;
import org.nic.lmd.wenderapp.webHandler.WebServiceHelper;


import java.util.ArrayList;
import java.util.Calendar;

public class ApplicationBillingDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    RadioGroup rd_gp_place;
    String place = "";
    RadioButton radioButton1, radioButton2;
    Button button_submit;
    JSONObject jsonObject;
    RecyclerView recyclerView, list_all_patners, list_documents;
    BillingDetailsAdapter billingDetailsAdapter;
    PatnerRecyclerAdapter patnerRecyclerAdapter;
    DocumentRecyclerAdapter documentRecyclerAdapter;
    private int mYear, mMonth, mDay;
    DatePickerDialog datedialog;
    TextView tv_com_date,tv_vid,tv_name,tv_add_ven,tv_pre_type,text_no_doc;
    ImageView bt_com_date;
    TextView text_total_vf, text_total_af, text_total_ur, text_total_cc, text_total_co, text_grand_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_billing_details);
        toolbar = findViewById(R.id.toolbar_bill);
        toolbar.setTitle("Vendor Details");
        toolbar.setSubtitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationBillingDetailsActivity.super.onBackPressed();
            }
        });

        rd_gp_place =  findViewById(R.id.rd_gp_place);
        rd_gp_place.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radio1) {
                    place = "OWNER OFFICE";
                } else {
                    place = "LMO OFFICE";
                }
            }
        });

        text_no_doc = findViewById(R.id.text_no_doc);
        list_documents = findViewById(R.id.list_documents);
        list_all_patners = findViewById(R.id.list_all_patners);
        recyclerView = findViewById(R.id.list_denomination_bill);
        button_submit = findViewById(R.id.button_submit);

        tv_vid = findViewById(R.id.tv_vid);
        tv_name = findViewById(R.id.tv_name);
        tv_pre_type = findViewById(R.id.tv_pre_type);
        tv_add_ven = findViewById(R.id.tv_add_ven);


        text_total_vf = findViewById(R.id.text_total_vf);
        text_total_af = findViewById(R.id.text_total_af);
        text_total_ur = findViewById(R.id.text_total_ur);
        text_total_cc = findViewById(R.id.text_total_cc);
        text_total_co = findViewById(R.id.text_total_co);
        text_grand_total = findViewById(R.id.text_grand_total);

        tv_com_date = findViewById(R.id.tv_com_date);
        bt_com_date =  findViewById(R.id.bt_com_date);
        callService();
    }

    private void callService() {
        VenderDataSingle.listenVendor(new VendorDataListener() {
            @Override
            public void responseFound(String res) {
                try {
                    Log.d("json res",res);
                    JSONObject jsonObject_res = new JSONObject(res);
                    jsonObject = jsonObject_res.getJSONObject("data");
                    if (!jsonObject.isNull("placeOfverification"))
                        place = jsonObject.getString("placeOfverification");
                    if (place.equals("OWNER OFFICE")) {
                        radioButton1 = rd_gp_place.findViewById(R.id.radio1);
                        radioButton1.setChecked(true);
                    } else {
                        radioButton2 = rd_gp_place.findViewById(R.id.radio2);
                        radioButton2.setChecked(true);
                    }
                    tv_pre_type.setText(""+new DataBaseHelper(ApplicationBillingDetailsActivity.this).getNatureofBusinessByID(jsonObject.getString("natureOfBusiness").trim()).getValue());
                    tv_vid.setText(""+jsonObject.getString("vendorId"));
                    tv_name.setText(""+jsonObject.getString("nameOfBusinessShop"));
                    tv_add_ven.setText(""+jsonObject.getString("address1")+"\n"+jsonObject.getString("address2"));
                    tv_com_date.setText((jsonObject.isNull("commencementDate"))?"N/A":jsonObject.getString("commencementDate"));
                    if (!jsonObject.isNull("instrumentVFTotal")) {
                        tv_com_date.setText("" + jsonObject.getString("commencementDate"));
                        text_total_vf.setText("" + Double.parseDouble(jsonObject.getString("instrumentVFTotal")) + Double.parseDouble(jsonObject.getString("weightVFTotal")));
                        text_total_af.setText("" +Double.parseDouble(jsonObject.getString("instrumentAFTotal")) + Double.parseDouble(jsonObject.getString("weightAFTotal")));
                        text_total_ur.setText("" + Double.parseDouble(jsonObject.getString("instrumentURTotal")) + Double.parseDouble(jsonObject.getString("weightURTotal")));
                        text_total_cc.setText("0.0");
                        text_total_co.setText("0.0");
                        text_grand_total.setText("" + jsonObject.getString("grandTotal"));
                    }
                    if (jsonObject.getString("status").equals("CAL")){
                        button_submit.setText("PAY HERE");
                        button_submit.setVisibility(View.VISIBLE);
                        button_submit.setOnClickListener(ApplicationBillingDetailsActivity.this);
                    }else{
                        button_submit.setVisibility(View.GONE);
                    }
                    populateRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ApplicationBillingDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void responseNotFound(String msg) {
                Toast.makeText(ApplicationBillingDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        if (Utiilties.isOnline(ApplicationBillingDetailsActivity.this)) {
            String sts=getIntent().getStringExtra("venderid");
            new VenderDataSingle(ApplicationBillingDetailsActivity.this).execute(getIntent().getStringExtra("venderid"));
        } else {
            Toast.makeText(this, "Internet not avalable !", Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowDialog2() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        datedialog = new DatePickerDialog(ApplicationBillingDetailsActivity.this,mDateSetListener1, mYear, mMonth, mDay);
        datedialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datedialog.show();
    }

    DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            mYear = selectedYear;
            mMonth = selectedMonth;
            mDay = selectedDay;
            try {
                if (mDay < 10 && (mMonth + 1) > 9) {
                    mDay = Integer.parseInt("0" + mDay);
                    tv_com_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append("0" + mDay));
                } else if ((mMonth + 1) < 10 && mDay > 9) {
                    mMonth = Integer.parseInt("0" + mMonth);
                    tv_com_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append(mDay));
                } else if ((mMonth + 1) < 10 && mDay < 10) {
                    mDay = Integer.parseInt("0" + mDay);
                    mMonth = Integer.parseInt("0" + mMonth);
                    tv_com_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append("0" + mDay));
                } else {
                    tv_com_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void populateRecyclerView() {
        list_all_patners.invalidate();
        ArrayList<PatnerEntity> patnerEntities = WebServiceHelper.parsePatner(jsonObject.toString(), ApplicationBillingDetailsActivity.this);
        patnerRecyclerAdapter = new PatnerRecyclerAdapter(patnerEntities, ApplicationBillingDetailsActivity.this);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(ApplicationBillingDetailsActivity.this);
        list_all_patners.setLayoutManager(mLayoutManager2);
        list_all_patners.setItemAnimator(new DefaultItemAnimator());
        list_all_patners.setAdapter(patnerRecyclerAdapter);

        recyclerView.invalidate();
        billingDetailsAdapter = new BillingDetailsAdapter(jsonObject, ApplicationBillingDetailsActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ApplicationBillingDetailsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(billingDetailsAdapter);

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("docs");
            if (jsonArray.length() > 0) {
                text_no_doc.setText("");
                list_documents.invalidate();
                documentRecyclerAdapter = new DocumentRecyclerAdapter(jsonArray, ApplicationBillingDetailsActivity.this);
                RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(ApplicationBillingDetailsActivity.this);
                list_documents.setLayoutManager(mLayoutManager3);
                list_documents.setItemAnimator(new DefaultItemAnimator());
                list_documents.setAdapter(documentRecyclerAdapter);
            }else{
                text_no_doc.setText("No Documents Found !");
            }
        } catch (Exception e) {
            Log.e("json-error", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button_submit){
            //Payment Code here
            try {
                Intent intent = new Intent(ApplicationBillingDetailsActivity.this, LoadUrlActivity.class);
                intent.putExtra("vendorId", jsonObject.getString("vendorId"));
                intent.putExtra("type", "T");
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}