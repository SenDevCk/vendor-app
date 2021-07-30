package org.nic.lmd.wenderapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.fragments.BusRegDetailsFragment;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;

public class ResitrationActivity extends AppCompatActivity {

    FrameLayout frame_reg;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resitration);
        toolbar=findViewById(R.id.toolbar_rg);
        toolbar.setTitle("Register");
        toolbar.setSubtitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(view -> ResitrationActivity.super.onBackPressed());
        frame_reg=findViewById(R.id.frame_reg);
        BusRegDetailsFragment homefragment = new BusRegDetailsFragment();
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_reg, homefragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        GlobalVariable.clearGlobalData();
        super.onDestroy();
    }
}