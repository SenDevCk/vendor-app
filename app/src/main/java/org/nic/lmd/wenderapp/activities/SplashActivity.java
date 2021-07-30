package org.nic.lmd.wenderapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.asynctask.NatureOfBusinessLoader;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 10;
    TextView text_head_sp;
    LinearLayout ll_sp;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        text_head_sp =  findViewById(R.id.splash_text);
        ll_sp =  findViewById(R.id.ll_sp);
        //text_ver =  findViewById(R.id.text_imei);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/header_font.ttf");
        text_head_sp.setTypeface(face);
        text_head_sp.setText(getResources().getString(R.string.app_name));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Call some material design APIs here
            if (checkAndRequestPermissions()) {
                init2();
            }

        } else {
            // Implement this feature without material design
            init2();
        }
    }

    private boolean checkAndRequestPermissions() {

        int read_phone = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int read_external = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write_external = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int media = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (read_external != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (write_external != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read_phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (media != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_MEDIA_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init2();
                } else {
                    //You did not accept the request can not use the functionality.
                    Toast.makeText(this, "Please Enable All permissions !", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void init2() {
        DataBaseHelper db = new DataBaseHelper(this);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        if (CommonPref.getCheckUpdate(SplashActivity.this)) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }, 2800);
        }else{
            ll_sp.setVisibility(View.GONE);
            if (Utiilties.isOnline(SplashActivity.this)) {
                new NatureOfBusinessLoader(SplashActivity.this).execute();
            }else{
                Toast.makeText(this, "Internate not avalable !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
