package org.nic.lmd.wenderapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.adapters.InstrumentClassAdapter;
import org.nic.lmd.wenderapp.adapters.PatnerAdapter;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;

public class SelectClassActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        toolbar =  findViewById(R.id.toolbar_par);
        toolbar.setTitle("Selected Class");
        toolbar.setSubtitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(view -> SelectClassActivity.super.onBackPressed());
        listView =  findViewById(R.id.list_partner);
        listView.setAdapter(new InstrumentClassAdapter(SelectClassActivity.this, new DataBaseHelper(SelectClassActivity.this).getInsClass(0)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.print_recept_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.print_menu:
            case android.R.id.home: {
                    finish();
                break;
            }
        }
        return true;
    }
}
