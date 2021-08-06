package org.nic.lmd.wenderapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.fragments.ApplyNew1Fragment;
import org.nic.lmd.wenderapp.fragments.UploadDocumentFragment;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;

public class ApplyNewActivity extends AppCompatActivity {

    Toolbar toolbar;
    FrameLayout frame_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_new);
        toolbar=findViewById(R.id.toolbar_ap_new);
        toolbar.setTitle("Apply New");
        toolbar.setSubtitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(view -> ApplyNewActivity.super.onBackPressed());
        frame_reg =  findViewById(R.id.frame_ap_new);
        if (getIntent().hasExtra("from")){
            if (getIntent().hasExtra("for")) {
                if (!getIntent().getStringExtra("for").equals("edit")) {
                    new DataBaseHelper(ApplyNewActivity.this).deleteAllInstruments();
                    new DataBaseHelper(ApplyNewActivity.this).deleteAllNozzle();
                    new DataBaseHelper(ApplyNewActivity.this).updateweightDenomination();
                    new DataBaseHelper(ApplyNewActivity.this).deleteAllPatner();
                    new DataBaseHelper(ApplyNewActivity.this).deleteAllTanks();
                }
            }
            UploadDocumentFragment applyNew1Fragment = UploadDocumentFragment.newInstance(getIntent().getStringExtra("ven_id"),"");
            android.app.FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_ap_new, applyNew1Fragment);
            fragmentTransaction.commit();
        }else {
            if (!getIntent().getStringExtra("for").equals("edit")) {
                new DataBaseHelper(ApplyNewActivity.this).deleteAllInstruments();
                new DataBaseHelper(ApplyNewActivity.this).deleteAllNozzle();
                new DataBaseHelper(ApplyNewActivity.this).updateweightDenomination();
                new DataBaseHelper(ApplyNewActivity.this).deleteAllPatner();
                new DataBaseHelper(ApplyNewActivity.this).deleteAllTanks();
            }
            ApplyNew1Fragment applyNew1Fragment = new ApplyNew1Fragment();
            androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_ap_new, applyNew1Fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}