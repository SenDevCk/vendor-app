package org.nic.lmd.wenderapp.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.CameraActivity;
import org.nic.lmd.wenderapp.entities.NatureOfBusiness;
import org.nic.lmd.wenderapp.entities.TypeOfPump;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ApplyNew3Fragment extends Fragment {

    private static final int CAMERA_PIC = 12;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    Button button_next3;
    Spinner sp_nat_of_business, sp_pump_type_f3;
    FrameLayout lay_type_pump;
    RelativeLayout re_img_dukan;
    EditText edit_tin, edit_pan, edit_pro_tax, edit_cst, edit_tan, edit_gst;
    NatureOfBusiness nature_of_business = null;
    String tin = "", pan = "", pro_tax = "", cst = "", tan = "", gst = "";

    ImageView img_pic1;
    private int ThumbnailSize;
    byte[] imgData;
    Bitmap bmp;
    String latitude, longitude;

    public ApplyNew3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_new3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        re_img_dukan=getActivity().findViewById(R.id.re_img_dukan);
        if (getActivity().getIntent().hasExtra("for")){
            re_img_dukan.setVisibility(View.GONE);
        }else{
            re_img_dukan.setVisibility(View.VISIBLE);
        }
        img_pic1 =  getActivity().findViewById(R.id.img_pic1);
        img_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCamera = new Intent(getActivity(), CameraActivity.class);
                iCamera.putExtra("KEY_PIC", "6");
                iCamera.putExtra("idname", "meterphoto");
                startActivityForResult(iCamera, CAMERA_PIC);
            }
        });
        edit_tin =  getActivity().findViewById(R.id.edit_tin);
        edit_tin.setText(tin);
        edit_pan =  getActivity().findViewById(R.id.edit_pan);
        edit_pan.setText(pan);
        edit_pro_tax =  getActivity().findViewById(R.id.edit_pro_tax);
        edit_pro_tax.setText(pro_tax);
        edit_cst =  getActivity().findViewById(R.id.edit_cst);
        edit_cst.setText(cst);
        edit_tan =  getActivity().findViewById(R.id.edit_tan);
        edit_tan.setText(tan);
        edit_gst =  getActivity().findViewById(R.id.edit_gst);
        edit_gst.setText("" + gst);
        sp_nat_of_business =  getActivity().findViewById(R.id.sp_nat_of_business_f3);
        sp_pump_type_f3 =  getActivity().findViewById(R.id.sp_pump_type_f3);
        lay_type_pump =  getActivity().findViewById(R.id.lay_type_pump);
        lay_type_pump.setVisibility(View.GONE);
        initialiseSpinner();
        button_next3 = getActivity().findViewById(R.id.button_next3);
        button_next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nature_of_business == null) {
                    Toast.makeText(getActivity(), "Select Nature of Business !", Toast.LENGTH_SHORT).show();
                } else if (bmp == null && !getActivity().getIntent().hasExtra("for")) {
                    Toast.makeText(getActivity(), "Take Shop Image !", Toast.LENGTH_SHORT).show();
                } else {
                    tin = edit_tin.getText().toString().trim();
                    pan = edit_pan.getText().toString().trim();
                    pro_tax = edit_pro_tax.getText().toString().trim();
                    cst = edit_cst.getText().toString().trim();
                    gst = edit_gst.getText().toString().trim();
                    tan = edit_tan.getText().toString().trim();
                    long c = updateVanderDetails();
                    if (c > 0) {
                        frameLayout =  getActivity().findViewById(R.id.frame_ap_new);
                        ApplyNew4Fragment applyNew4Fragment = new ApplyNew4Fragment();
                        fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_ap_new, applyNew4Fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "Vender details not updated !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (new DataBaseHelper(getActivity()).getCountVenderDetails() > 0) {
            initialiseAllFields();
        }
    }

    private void initialiseAllFields() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select tin,pan,pro,cst,tan,gst,nat_of_bussiness,shop_img,latitude,longitude from VENDER_DETAILS where _id='" + GlobalVariable.count_data + "'", null);
        while (cursor.moveToNext()) {
            if(!cursor.isNull(cursor.getColumnIndex("nat_of_bussiness"))) {
                edit_tin.setText(cursor.isNull(cursor.getColumnIndex("tin")) ? "" : cursor.getString(cursor.getColumnIndex("tin")));
                edit_pan.setText(cursor.isNull(cursor.getColumnIndex("pan")) ? "" : cursor.getString(cursor.getColumnIndex("pan")));
                edit_pro_tax.setText(cursor.isNull(cursor.getColumnIndex("pro")) ? "" : cursor.getString(cursor.getColumnIndex("pro")));
                edit_cst.setText(cursor.isNull(cursor.getColumnIndex("cst")) ? "" : cursor.getString(cursor.getColumnIndex("cst")));
                edit_gst.setText(cursor.isNull(cursor.getColumnIndex("gst")) ? "" : cursor.getString(cursor.getColumnIndex("gst")));
                edit_tan.setText(cursor.isNull(cursor.getColumnIndex("tan")) ? "" : cursor.getString(cursor.getColumnIndex("tan")));
                sp_nat_of_business.setSelection(((ArrayAdapter<String>) sp_nat_of_business.getAdapter()).getPosition((dataBaseHelper.getNatureofBusinessByID(cursor.getString(cursor.getColumnIndex("nat_of_bussiness")))).getValue()));
            }
            if (!cursor.isNull(cursor.getColumnIndex("shop_img"))) {
                imgData = cursor.isNull(cursor.getColumnIndex("shop_img")) ? new byte[0] : cursor.getBlob(cursor.getColumnIndex("shop_img"));
                bmp=BitmapFactory.decodeByteArray(imgData , 0, imgData.length);
                latitude = cursor.isNull(cursor.getColumnIndex("latitude")) ? null : cursor.getString(cursor.getColumnIndex("latitude"));
                longitude = cursor.isNull(cursor.getColumnIndex("longitude")) ? null : cursor.getString(cursor.getColumnIndex("longitude"));
            }

        }
    }

    private long updateVanderDetails() {
        long c = -1;
        SQLiteDatabase db = new DataBaseHelper(getActivity()).getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("tin", tin.trim());
            values.put("pan", pan.trim());
            values.put("pro", pro_tax.trim());
            values.put("cst", cst.trim());
            values.put("tan", tan.trim());
            values.put("gst", gst.trim());
            values.put("nat_of_bussiness", nature_of_business.getId().trim());
            values.put("latitude", latitude);
            values.put("longitude", longitude);
            values.put("shop_img", imgData);
            c = db.update("VENDER_DETAILS", values, "_id=?", new String[]{String.valueOf(GlobalVariable.count_data)});
        } catch (Exception e) {
            Log.e("ERROR 3", " WRITING DATA in LOCAL DB for Vender_det");
            Log.e("ERROR 1", e.getLocalizedMessage());
            Log.e("ERROR 2", e.getMessage());
            // TODO: handle exception
        } finally {
            db.close();
        }
        return c;
    }

    private void initialiseSpinner() {
        final ArrayList<NatureOfBusiness> natureOfRequests = new DataBaseHelper(getActivity()).getNatureofBusiness();
        ArrayList<String> list_nat_of_business = new ArrayList<>();
        list_nat_of_business.add("--SELECT NATURE OF BUSINESS--");
        if (list_nat_of_business.size() > 0) {
            for (NatureOfBusiness natureOfRequest : natureOfRequests) {
                list_nat_of_business.add(natureOfRequest.getValue());
            }
        }
        sp_nat_of_business.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_nat_of_business));
        sp_nat_of_business.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i <= 0) {
                    nature_of_business = null;
                } else {
                    nature_of_business = (NatureOfBusiness) natureOfRequests.get(i - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //pump Spinner

        final ArrayList<TypeOfPump> typeOfPumps = new DataBaseHelper(getActivity()).getPumpType();
        ArrayList<String> list_string_pumps = new ArrayList<>();
        list_string_pumps.add("-- SELECT TYPE OF PUMP --");
        if (list_nat_of_business.size() > 0) {
            for (TypeOfPump typeOfPump : typeOfPumps) {
                list_string_pumps.add(typeOfPump.getName());
            }
        }
        sp_pump_type_f3.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_pumps));
        sp_pump_type_f3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //GlobalVariable.natureofBusiness_pos = i;
                if (i <= 0) {
                    //GlobalVariable.nature_of_business = "0";
                } else {
                    //GlobalVariable.nature_of_business = natureOfRequests.get(i - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ThumbnailSize = 300;

        long c = -1;
        switch (requestCode) {

            case CAMERA_PIC:
                if (resultCode == RESULT_OK) {
                    imgData = data.getByteArrayExtra("CapturedImage");
                    latitude = data.getStringExtra("Lat");
                    longitude = data.getStringExtra("Lng");
                    bmp = BitmapFactory.decodeByteArray(imgData, 0,
                            imgData.length);
                    img_pic1.setImageBitmap(Utiilties.GenerateThumbnail(bmp,
                            ThumbnailSize, ThumbnailSize));
                    SQLiteDatabase db = new DataBaseHelper(getActivity()).getWritableDatabase();
                    try {
                        ContentValues values = new ContentValues();
                            values.put("latitude", latitude);
                            values.put("longitude", longitude);
                            values.put("shop_img", imgData);
                        c = db.update("VENDER_DETAILS", values, "_id=?", new String[]{String.valueOf(GlobalVariable.count_data)});
                    } catch (Exception e) {
                        Log.e("ERROR 3", " WRITING DATA in LOCAL DB for Vender_det");
                        Log.e("ERROR 1", e.getLocalizedMessage());
                        Log.e("ERROR 2", e.getMessage());
                        // TODO: handle exception
                    } finally {
                        db.close();
                    }
                    break;
                }
        }
    }
}