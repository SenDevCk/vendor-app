package org.nic.lmd.wenderapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.adapters.WeightDenominationAdapter;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.Objects;

public class WeightDenominationActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ListView listView;
    TextView clear_text,textView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_denomination);
        toolbar =  findViewById(R.id.toolbar_den_list);
        toolbar.setTitle("Select Denomination");
        toolbar.setSubtitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(view -> WeightDenominationActivity.super.onBackPressed());
        clear_text =  findViewById(R.id.clear_text);
        clear_text.setOnClickListener(this);
        textView =  findViewById(R.id.tv_added_cart);
        textView.setText("" + new DataBaseHelper(WeightDenominationActivity.this).getAddedWeightCount());
        Utiilties.didTapButton(textView, WeightDenominationActivity.this);
        listView =  findViewById(R.id.list_denomination);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.invalidate();
        listView.setAdapter(new WeightDenominationAdapter(WeightDenominationActivity.this, getIntent().getStringExtra("category_id"), getIntent().getStringExtra("year"), textView));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.clear_text){
            new DataBaseHelper(WeightDenominationActivity.this).updateweightDenomination();
            listView.invalidate();
            listView.setAdapter(new WeightDenominationAdapter(WeightDenominationActivity.this, getIntent().getStringExtra("category_id"), getIntent().getStringExtra("year"), textView));
            textView.setText("" + new DataBaseHelper(WeightDenominationActivity.this).getAddedWeightCount());
        }
    }
}