package org.nic.lmd.wenderapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.adapters.PatnerAdapter;
import org.nic.lmd.wenderapp.adapters.WeightDenominationAdapter;

public class PartnerActivity extends AppCompatActivity {
Toolbar toolbar;
ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        toolbar =  findViewById(R.id.toolbar_par);
        toolbar.setTitle("Selected Patner");
        toolbar.setSubtitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartnerActivity.super.onBackPressed();
            }
        });

        listView =  findViewById(R.id.list_partner);
        listView.setAdapter(new PatnerAdapter(PartnerActivity.this));

    }
}