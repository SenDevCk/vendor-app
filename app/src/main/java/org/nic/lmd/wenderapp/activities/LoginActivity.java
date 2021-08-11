package org.nic.lmd.wenderapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.asynctask.LoginLoader;

public class LoginActivity extends AppCompatActivity {

    Button register,button_login;
    TextView textView,text_version;
    EditText user_name,password;
    /*Upper Case Character,Lower Case Character,Special Character,Number,and minimum of 8 digits.*/
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static final String USER_ID_PATTERN = "[A-Za-z][\\w.-]+[a-zA-Z0-9]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button_login=findViewById(R.id.button_login);
        register=findViewById(R.id.button_signup);
        textView=findViewById(R.id.text_log);
        text_version=findViewById(R.id.version);
        textView.setText(getResources().getString(R.string.app_name));
        user_name= findViewById(R.id.user_name);
        password= findViewById(R.id.password);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/header_font.ttf");
        textView.setTypeface(face);
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String code_v = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            text_version.setText(getResources().getString(R.string.app_name) + " ( " + code_v + "." + version + " ) V");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Register(View view) {
        Intent intent=new Intent(LoginActivity.this,ResitrationActivity.class);
        startActivity(intent);
    }


    public void Login(View view) {
        if (!user_name.getText().toString().trim().matches(USER_ID_PATTERN)){
            Toast.makeText(this, "Enter Valid User Name !", Toast.LENGTH_SHORT).show();
        }else if (!password.getText().toString().trim().matches(PASSWORD_PATTERN)){
            Toast.makeText(this, "Enter Valid Password !", Toast.LENGTH_SHORT).show();
        }else{
            new LoginLoader(LoginActivity.this).execute(user_name.getText().toString().trim(),password.getText().toString().trim());
        }
    }
}