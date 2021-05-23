package org.nic.lmd.wenderapp.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.Camera2Activity;
import org.nic.lmd.wenderapp.activities.CameraActivity;
import org.nic.lmd.wenderapp.entities.NatureOfBusiness;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class BusRegDetailsFragment extends Fragment implements View.OnClickListener {

    Button next1;
    FragmentManager fragmentManager;
    Spinner sp_nat_of_business;
    String toast_msg;
    CheckBox check_man, check_rep, check_deler, check_rep_re, check_man_re, check_con;
    EditText edit_uid, edit_pass, edit_con_pass;
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private static final String USER_ID_PATTERN = "[A-Za-z][\\w.-]+[a-zA-Z0-9]";
    private static final String user_name = "^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)";

    private static final int CAMERA_PIC = 12;
    private static final int CAMERA_PIC2 = 10;
    ImageView img_trader, img_sign;
    byte[] imgData,imgData1;
    Bitmap bmp, bmp1;

    public BusRegDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_reg_details, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {

        sp_nat_of_business =  getActivity().findViewById(R.id.sp_nat_of_business);
        initializeSpiner();

        check_rep =  getActivity().findViewById(R.id.check_rep);
        check_man =  getActivity().findViewById(R.id.check_man);
        check_deler =  getActivity().findViewById(R.id.check_deler);
        check_man_re =  getActivity().findViewById(R.id.check_man_re);
        check_rep_re =  getActivity().findViewById(R.id.check_rep_re);
        check_con =  getActivity().findViewById(R.id.check_con);

        edit_uid =  getActivity().findViewById(R.id.edit_uid);
        edit_pass =  getActivity().findViewById(R.id.edit_pass);
        edit_con_pass =  getActivity().findViewById(R.id.edit_con_pass);

        img_sign = getActivity().findViewById(R.id.img_sign);
        img_trader = getActivity().findViewById(R.id.img_trader);
        img_trader.setOnClickListener(this);
        img_sign.setOnClickListener(this);
        next1 =  getActivity().findViewById(R.id.next1);
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGlobalData();
                if (validate()) {
                    NameAndODFragment homefragment = new NameAndODFragment();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_reg, homefragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), "" + toast_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setGlobalData() {

        if (check_man.isChecked()) GlobalVariable.isManufacturere = true;
        else GlobalVariable.isManufacturere = false;

        if (check_deler.isChecked()) GlobalVariable.isDeler = true;
        else GlobalVariable.isDeler = false;

        if (check_rep.isChecked()) GlobalVariable.isRepeirer = true;
        else GlobalVariable.isRepeirer = false;

        if (check_rep_re.isChecked()) GlobalVariable.isRepeirer_ra = true;
        else GlobalVariable.isRepeirer_ra = false;

        if (check_man_re.isChecked()) GlobalVariable.isManufacturere_ra = true;
        else GlobalVariable.isManufacturere_ra = false;

        if (check_con.isChecked()) GlobalVariable.isConsumer = true;
        else GlobalVariable.isConsumer = false;

        GlobalVariable.userID = edit_uid.getText().toString();
        GlobalVariable.password = edit_pass.getText().toString();

    }


    private void initializeSpiner() {
        final ArrayList<NatureOfBusiness> natureOfRequests = new DataBaseHelper(getActivity()).getNatureofBusiness();
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("--SELECT NATURE OF BUSINESS--");
        if (stringArrayList.size() > 0) {
            for (NatureOfBusiness natureOfRequest : natureOfRequests) {
                stringArrayList.add(natureOfRequest.getValue());
            }
        }
        sp_nat_of_business.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList));
        sp_nat_of_business.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //GlobalVariable.natureofBusiness_pos = i;

                if (i <= 0) {
                    GlobalVariable.nature_of_business = null;
                } else {
                    GlobalVariable.nature_of_business = natureOfRequests.get(i - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean validate() {
        boolean isvalid = false;
        if (GlobalVariable.nature_of_business == null) {
            isvalid = false;
            toast_msg = "Select Nature Of Business";
        }
        /* else if (validateCheck(new ArrayList<CheckBox>(Arrays.asList(check_deler,check_rep,check_man)))<=0){
             isvalid=false;
             toast_msg="Select any One from License";
         }else if (validateCheck(new ArrayList<CheckBox>(Arrays.asList(check_rep_re,check_man_re)))<=0){
             isvalid=false;
             toast_msg="Select any One from Registration";
         }*/
        else if (!check_con.isChecked()) {
            isvalid = false;
            toast_msg = "Select Certificate";
        } else if (!edit_uid.getText().toString().trim().matches(USER_ID_PATTERN)) {
            isvalid = false;
            toast_msg = "Enter alpha numeric User Name";
            edit_uid.setError(toast_msg);
        } else if (!edit_pass.getText().toString().trim().matches(PASSWORD_PATTERN)) {
            isvalid = false;
            toast_msg = "Enter Minimum 8 characters (Uper and lower case , number , special character)";
            edit_pass.setError(toast_msg);
        } else if (!edit_con_pass.getText().toString().trim().matches(PASSWORD_PATTERN)) {
            isvalid = false;
            toast_msg = "Enter Valid Conform Password";
            edit_con_pass.setError(toast_msg);
        } else if (!edit_pass.getText().toString().trim().equals(edit_con_pass.getText().toString())) {
            isvalid = false;
            toast_msg = "Passwod mismatch";
            edit_con_pass.setError(toast_msg);
            edit_pass.setError(toast_msg);
        } else {
            isvalid = true;
        }
        return isvalid;
    }

    private int validateCheck(ArrayList<CheckBox> ch) {
        int count = 0;
        for (CheckBox checkBox : ch) {
            if (checkBox.isChecked()) {
                count++;
            }
        }
        return count;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int ThumbnailSize = 300;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_PIC:
                    imgData = data.getByteArrayExtra("CapturedImage");
                    bmp = BitmapFactory.decodeByteArray(imgData, 0,
                            imgData.length);
                    img_trader.setImageBitmap(Utiilties.GenerateThumbnail(bmp, ThumbnailSize, ThumbnailSize));
                    break;
                case CAMERA_PIC2:
                    imgData1 = data.getByteArrayExtra("CapturedImage");
                    bmp1 = BitmapFactory.decodeByteArray(imgData1, 0, imgData1.length);
                    img_trader.setImageBitmap(Utiilties.GenerateThumbnail(bmp1, ThumbnailSize, ThumbnailSize));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent iCamera = new Intent(getActivity(), Camera2Activity.class);
        if (v.getId() == R.id.img_trader) {
            startActivityForResult(iCamera, CAMERA_PIC);
        } else if (v.getId() == R.id.img_sign) {
            startActivityForResult(iCamera, CAMERA_PIC2);
        }
    }

}