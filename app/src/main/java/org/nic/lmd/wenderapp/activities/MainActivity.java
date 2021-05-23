package org.nic.lmd.wenderapp.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.adapters.CustomExpandableListAdapter;
import org.nic.lmd.wenderapp.adapters.VendorAdapter;
import org.nic.lmd.wenderapp.asynctask.FirmTypeLoader;
import org.nic.lmd.wenderapp.asynctask.VenderListLoader;
import org.nic.lmd.wenderapp.entities.ExpandableListDataPump;
import org.nic.lmd.wenderapp.entities.UserData;
import org.nic.lmd.wenderapp.interfaces.VenderListListener;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView list_ven;
    ImageView imageViewheader;
    TextView text_header_name, text_header_mobile;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    Button button_new_ap;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =  findViewById(R.id.toolbar_main);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        //navigation header
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String code_v = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            TextView app_name_tip =  navigationView.findViewById(R.id.app_name_tip);
            app_name_tip.setText(getResources().getString(R.string.app_name) + " ( " + code_v + "." + version + " ) V");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String code_v = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            TextView app_name_tip =  navigationView.findViewById(R.id.app_name_tip);
            app_name_tip.setText(getResources().getString(R.string.app_name) + " ( " + code_v + "." + version + " ) V");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        View header = navigationView.getHeaderView(0);
        text_header_name =  header.findViewById(R.id.text_header_name);
        text_header_mobile =  header.findViewById(R.id.text_header_mobile);
        imageViewheader =  header.findViewById(R.id.imageViewheader);

        UserData userinfo = CommonPref.getUserDetails(MainActivity.this);
        text_header_name.setText("" + userinfo.getApplicantname());
        text_header_mobile.setText("" + userinfo.getContactNo());
        swipeRefreshLayout =  findViewById(R.id.swiperefresh);
        expandableListView =  navigationView.findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
               /* Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                drawer.closeDrawers();
                if (groupPosition == 0 && childPosition == 0) {
                    if (CommonPref.getCheckUpdateForApply(MainActivity.this)) {
                        Intent intent = new Intent(MainActivity.this, ApplyNewActivity.class);
                        startActivity(intent);
                    } else if (!Utiilties.isOnline(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, "Internet Not Avalable !", Toast.LENGTH_SHORT).show();
                    } else {
                        new FirmTypeLoader(MainActivity.this).execute();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Under Process", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        list_ven =  findViewById(R.id.list_ven);
        button_new_ap =  findViewById(R.id.button_new_ap);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               swipeRefreshLayout.setRefreshing(true);
               callService();
           }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* ArrayList<String> stringArrayList = new DataBaseHelper(MainActivity.this).getVendersAdded(CommonPref.getUserDetails(MainActivity.this).getApplicantId());
        if (stringArrayList.size() > 0) {
            list_ven.invalidate();
            list_ven.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stringArrayList));
            list_ven.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MainActivity.this, ApplyNewActivity.class);
                    intent.putExtra("ven_id", "" + adapterView.getItemAtPosition(i).toString().split(":")[1].trim());
                    intent.putExtra("from", "main");
                    startActivity(intent);
                }
            });
        }*/
        callService();
    }
    private void callService(){
        VenderListLoader.listenVendorList(new VenderListListener() {
            @Override
            public void responseFound(String res) {
                swipeRefreshLayout.setRefreshing(false);
                long jsonArraylen =0;
                try {
                    JSONArray jsonArray = new JSONArray(res);
                    jsonArraylen=jsonArray.length();
                    if (jsonArraylen>0){
                        button_new_ap.setVisibility(View.GONE);
                        list_ven.invalidate();
                        list_ven.setAdapter(new VendorAdapter(MainActivity.this,res));
                    }else{
                        button_new_ap.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void responseNotFound(String msg) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
            }
        });
        UserData userData= CommonPref.getUserDetails(MainActivity.this);
        Log.d("userid",userData.getUserid());
        if (!Utiilties.isOnline(MainActivity.this)){
            Toast.makeText(this, "Internet Not Avalable !", Toast.LENGTH_SHORT).show();
        }else {
            new VenderListLoader(MainActivity.this).execute("USR", userData.getUserid());
        }
    }
    public void ApplyNew(View view) {
        if (CommonPref.getCheckUpdateForApply(MainActivity.this)) {
            Intent intent = new Intent(MainActivity.this, ApplyNewActivity.class);
            startActivity(intent);
        }else if (!Utiilties.isOnline(MainActivity.this)){
            Toast.makeText(this, "Internet Not Avalable !", Toast.LENGTH_SHORT).show();
        }
        else {
            new FirmTypeLoader(MainActivity.this).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync_menu: {
                new FirmTypeLoader(MainActivity.this).execute();
                break;
            }
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Really Exit ?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                        finish();
                    }

                }).create().show();

    }
}