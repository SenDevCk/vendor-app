package org.nic.lmd.wenderapp.fragments;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.entities.Block;
import org.nic.lmd.wenderapp.entities.Designation;
import org.nic.lmd.wenderapp.entities.District;
import org.nic.lmd.wenderapp.entities.FirmType;
import org.nic.lmd.wenderapp.entities.PremisesTypeEntity;
import org.nic.lmd.wenderapp.entities.SubDivision;
import org.nic.lmd.wenderapp.entities.ThanaEntity;
import org.nic.lmd.wenderapp.entities.UserData;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.stream.Collectors;


public class ApplyNew1Fragment extends Fragment {

    Button button_next1;
    FrameLayout frame_ap_new,fra_pol_station;
    FragmentManager fragmentManager;
    private int mYear, mMonth, mDay;
    DatePickerDialog datedialog;
    TextView tv_lic_date, tv_est_date,tv_lic_date_clr,tv_est_date_clr,tv_reg_date_clr;
    ImageView bt_est_date, bt_lic_date;
    RadioButton radioButton1,radioButton2;
    int flag = 0;
    Spinner sp_district, sp_permisses, sp_block, sp_subdiv, sp_firm_type;
    EditText edit_ap_name, edit_add1, edit_add2, edit_landmark, edit_city, edit_pin, edit_mob, edit_contact_landline, edit_email, edit_lice_no, edit_reg_f1;
    ScrollView scrollView;
    View view_spin;
    Block block_f1 = null;
    District district_f1;
    PremisesTypeEntity premisesTypeEntity = null;
    String dist_name = "";

    FirmType firmType_f1 = null;
    TextView tv_reg_date, tv_com_date;
    ImageView bt_reg_date, bt_com_date;
    RadioGroup rd_gp_place;
    private String place = "";
    private ThanaEntity thana=null;


    public ApplyNew1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_new1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = getActivity().findViewById(R.id.scroll_fra1);
        fra_pol_station =  getActivity().findViewById(R.id.fra_pol_station);
        bt_est_date =  getActivity().findViewById(R.id.bt_est_date);
        bt_est_date.setOnClickListener(view14 -> {
            flag = 1;
            ShowDialog();
        });
        bt_lic_date =  getActivity().findViewById(R.id.bt_lic_date);
        bt_lic_date.setOnClickListener(view1 -> {
            flag = 2;
            ShowDialog();
        });
        tv_est_date =  getActivity().findViewById(R.id.tv_est_date);
        tv_lic_date = getActivity().findViewById(R.id.tv_lic_date);
        tv_est_date_clr =  getActivity().findViewById(R.id.tv_est_date_clr);
        tv_lic_date_clr =  getActivity().findViewById(R.id.tv_lic_date_clr);
        tv_reg_date_clr =  getActivity().findViewById(R.id.tv_reg_date_clr);
        tv_reg_date =  getActivity().findViewById(R.id.tv_reg_date);
        bt_reg_date =  getActivity().findViewById(R.id.bt_reg_date);
        bt_reg_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 3;
                ShowDialog2();
            }
        });
        tv_lic_date_clr.setOnClickListener(v -> tv_lic_date.setText(""));
        tv_est_date_clr.setOnClickListener(v -> tv_est_date.setText(""));
        tv_reg_date_clr.setOnClickListener(v -> tv_reg_date.setText(""));
        tv_com_date = (TextView) getActivity().findViewById(R.id.tv_com_date);
        bt_com_date =  getActivity().findViewById(R.id.bt_com_date);
        bt_com_date.setOnClickListener(view12 -> {
            flag = 4;
            ShowDialog2();
        });
        sp_district =  getActivity().findViewById(R.id.sp_dist);
        sp_permisses =  getActivity().findViewById(R.id.sp_permisses);
        sp_block =  getActivity().findViewById(R.id.sp_block);
        sp_subdiv =  getActivity().findViewById(R.id.sp_subdiv);
        sp_firm_type =  getActivity().findViewById(R.id.sp_firm_type);

        initializePremisesSpinner();
        initializeDistrictSpinner();
        initializeBlockSpinner();
        initializeThana();
        initializeFirmSpiner();

        button_next1 =  getActivity().findViewById(R.id.button_next1);
        button_next1.setOnClickListener(view13 -> {
            if (validate()) {
                long c = addVenderDetails();
                if (c > 0) {
                    GlobalVariable.count_data = new DataBaseHelper(getActivity()).getIdVEN();
                    frame_ap_new =  getActivity().findViewById(R.id.frame_ap_new);
                    ApplyNew2Fragment applyNew2Fragment = new ApplyNew2Fragment();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_ap_new, applyNew2Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), "Error in saving vender details", Toast.LENGTH_SHORT).show();
                }
            } else {
                Utiilties.scrollToView(scrollView, view_spin);
            }
        });

        edit_ap_name =  getActivity().findViewById(R.id.edit_ap_name);
        edit_add1 =  getActivity().findViewById(R.id.edit_add1);
        edit_add2 =  getActivity().findViewById(R.id.edit_add2);
        edit_landmark =  getActivity().findViewById(R.id.edit_landmark);
        edit_city =  getActivity().findViewById(R.id.edit_city);
        edit_pin =  getActivity().findViewById(R.id.edit_pin);
        edit_mob =  getActivity().findViewById(R.id.edit_mob);
        edit_contact_landline =  getActivity().findViewById(R.id.edit_contact_landline);
        edit_email =  getActivity().findViewById(R.id.edit_email);
        edit_lice_no =  getActivity().findViewById(R.id.edit_lice_no);
        edit_reg_f1 =  getActivity().findViewById(R.id.edit_reg_f2);
        rd_gp_place =  getActivity().findViewById(R.id.rd_gp_place);
        rd_gp_place.setOnCheckedChangeListener((group, checkedId) -> {
            // find which radio button is selected
            if (checkedId == R.id.radio1) {
                place = "Other Than LMO Office";
            } else {
                place = "At LMO Office";
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        UserData userData = CommonPref.getUserDetails(getActivity());
        if (new DataBaseHelper(getActivity()).getCountVenderDetails() > 0) {
            GlobalVariable.count_data=new DataBaseHelper(getActivity()).getIdVEN();
            initialiseAllFields();
        } else {
           // edit_ap_name.setText("" + userData.getApplicantname());
            edit_add1.setText("" + userData.getAddress1());
            edit_add2.setText("" + userData.getAddress2());
            edit_mob.setText("" + userData.getMobile());
            edit_email.setText("" + userData.getEmailid());
            edit_landmark.setText("" + userData.getLandMark());
            edit_contact_landline.setText("" + userData.getContactNo());
            edit_pin.setText("" + userData.getPinCode());
            edit_city.setText("" + userData.getCity());
        }
    }

    private void initialiseAllFields() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select shop_name,add1,add2,mobile,email,landmark,landline,pin,city,date_of_est,licence_date,reg_no,date_of_comm,date_of_reg,licence_no,dis_id,block_id,premise_type,type_firm,thana_id from VENDER_DETAILS where _id='" + GlobalVariable.count_data + "'", null);
        while (cursor.moveToNext()) {
            place=cursor.isNull(cursor.getColumnIndex("place_of_var"))?"": cursor.getString(cursor.getColumnIndex("place_of_var"));
            if (place.equals("At LMO Office")) {
                radioButton2 =  rd_gp_place.findViewById(R.id.radio2);
                radioButton2.setChecked(true);
            } else {
                radioButton1 =  rd_gp_place.findViewById(R.id.radio1);
                radioButton1.setChecked(true);
            }
            edit_ap_name.setText(cursor.isNull(cursor.getColumnIndex("shop_name"))?"": cursor.getString(cursor.getColumnIndex("shop_name")));
            edit_add1.setText(cursor.isNull(cursor.getColumnIndex("add1"))?"":cursor.getString(cursor.getColumnIndex("add1")));
            edit_add2.setText(cursor.isNull(cursor.getColumnIndex("add2"))?"":cursor.getString(cursor.getColumnIndex("add2")));
            edit_mob.setText(cursor.isNull(cursor.getColumnIndex("mobile"))?"":cursor.getString(cursor.getColumnIndex("mobile")));
            edit_email.setText(cursor.isNull(cursor.getColumnIndex("email"))?"" :cursor.getString(cursor.getColumnIndex("email")));
            edit_landmark.setText(cursor.isNull(cursor.getColumnIndex("landmark"))?"":cursor.getString(cursor.getColumnIndex("landmark")));
            edit_contact_landline.setText(cursor.isNull(cursor.getColumnIndex("landline"))?"" :cursor.getString(cursor.getColumnIndex("landline")));
            edit_pin.setText(cursor.isNull(cursor.getColumnIndex("pin"))?"" :cursor.getString(cursor.getColumnIndex("pin")));
            edit_city.setText(cursor.isNull(cursor.getColumnIndex("city"))?"":cursor.getString(cursor.getColumnIndex("city")));
            tv_est_date.setText(cursor.isNull(cursor.getColumnIndex("date_of_est"))?"" : cursor.getString(cursor.getColumnIndex("date_of_est")));
            tv_lic_date.setText(cursor.isNull(cursor.getColumnIndex("licence_date"))?"": cursor.getString(cursor.getColumnIndex("licence_date")));
            edit_lice_no.setText(cursor.isNull(cursor.getColumnIndex("licence_no"))?"": cursor.getString(cursor.getColumnIndex("licence_no")));
            edit_reg_f1.setText(cursor.isNull(cursor.getColumnIndex("reg_no"))?"": cursor.getString(cursor.getColumnIndex("reg_no")));
            tv_com_date.setText(cursor.isNull(cursor.getColumnIndex("date_of_comm"))?"": cursor.getString(cursor.getColumnIndex("date_of_comm")));
            tv_reg_date.setText(cursor.isNull(cursor.getColumnIndex("date_of_reg"))?"": cursor.getString(cursor.getColumnIndex("date_of_reg")));
            sp_permisses.setSelection(((ArrayAdapter<String>)sp_permisses.getAdapter()).getPosition((dataBaseHelper.getPremissesByID(cursor.getString(cursor.getColumnIndex("premise_type")))).getName()));
            block_f1=dataBaseHelper.getBlockByID(cursor.getString(cursor.getColumnIndex("block_id")));
            thana = dataBaseHelper.getThanaByID(cursor.isNull(cursor.getColumnIndex("thana_id"))?null:cursor.getString(cursor.getColumnIndex("thana_id")));
            sp_district.setSelection(((ArrayAdapter<String>)sp_district.getAdapter()).getPosition((dataBaseHelper.getDistrictByID(cursor.getString(cursor.getColumnIndex("dis_id")))).getName()));
            sp_firm_type.setSelection(((ArrayAdapter<String>)sp_firm_type.getAdapter()).getPosition(dataBaseHelper.getFirmType(cursor.getString(cursor.getColumnIndex("type_firm")))));
        }
    }

    private void initializeFirmSpiner() {

        final ArrayList<FirmType> firmTypes = new DataBaseHelper(getActivity()).getFirmType();
        ArrayList<String> stringArrayList2 = new ArrayList<>();
        stringArrayList2.add("--SELECT TYPE OF FIRM--");
        if (stringArrayList2.size() > 0) {
            for (FirmType firmType : firmTypes) {
                stringArrayList2.add(firmType.getName());
            }
        }
        sp_firm_type.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList2));
        sp_firm_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    firmType_f1 = firmTypes.get(i - 1);
                } else {
                    firmType_f1 = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initializeDistrictSpinner() {
        final ArrayList<District> districts = new DataBaseHelper(getActivity()).getDistrict();
        ArrayList<String> stringArrayList2 = new ArrayList<>();
        stringArrayList2.add("--SELECT DISTRICT--");
        if (stringArrayList2.size() > 0) {
            for (District district : districts) {
                stringArrayList2.add(district.getName());
            }
        }
        sp_district.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList2));
        sp_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    district_f1 = districts.get(i - 1);
                    initializeBlockSpinner();
                } else {
                    district_f1 = null;
                    initializeBlockSpinner();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (district_f1 == null) {
            ArrayList<District> districtArrayList = new DataBaseHelper(getActivity()).getDistrict();
            for (District district : districtArrayList) {
                if (district.getId().equals(CommonPref.getUserDetails(getActivity()).getDistrictid())) {
                    dist_name = district.getName().trim();
                }
            }

        } else {
            dist_name = district_f1.getName().trim();
        }
        sp_district.setSelection(((ArrayAdapter<String>) sp_district.getAdapter()).getPosition(dist_name));
    }

    private void initializeBlockSpinner() {
        List<Block> blocks=null;
        String dist_id = "0";
        if (district_f1 != null) {
            dist_id = district_f1.getId().trim();
        } else {
            dist_id = "0";
        }
         final ArrayList<Block> blocks2 = new DataBaseHelper(getActivity()).getBlock(dist_id);
        if(district_f1==null){
            blocks=blocks2;
        }
        else if(district_f1.getId().trim().equals("212")) {
            blocks= blocks2.stream().
                    filter(block -> (block.getValue().equals(String.valueOf(1965)))||(block.getValue().equals(String.valueOf(5001)))||(block.getValue().equals(String.valueOf(5002)))||(block.getValue().equals(5003))||(block.getValue().equals(String.valueOf(5004)))).
                    collect(Collectors.toList());
        }else{
            blocks=blocks2;
        }
        ArrayList<String> stringArrayList2 = new ArrayList<>();
        stringArrayList2.add("--SELECT BLOCK--");
        if (stringArrayList2.size() > 0) {
            for (Block block : blocks) {
                stringArrayList2.add(block.getName());
            }
        }
        sp_block.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList2));
        List<Block> finalBlocks = blocks;
        sp_block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    block_f1 = finalBlocks.get(i - 1);
                    if (district_f1.getId().trim().equals("212")){
                        fra_pol_station.setVisibility(View.VISIBLE);
                    }
                    else{
                        fra_pol_station.setVisibility(View.GONE);
                    }
                    initializeThana();
                } else {
                    block_f1 = null;
                    initializeThana();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (block_f1!=null && stringArrayList2.contains(block_f1.getName().trim())){
            sp_block.setSelection(((ArrayAdapter<String>) sp_block.getAdapter()).getPosition(block_f1.getName().trim()));
            if (getActivity().getIntent().getStringExtra("for").equals("edit")){
                sp_block.setEnabled(false);
            }
        }
    }



    private void initializeThana() {
         List<ThanaEntity> thanaEntities=null;
        String bblockid="";
        if (block_f1 != null) {
            if (block_f1.getValue()!=null)
                bblockid = block_f1.getValue().trim();
            else bblockid="0";
        }else{
            bblockid="0";
        }
        List<ThanaEntity> thanaEntities2 = new DataBaseHelper(getActivity()).getThanaAll(bblockid);
        if (district_f1==null){
            thanaEntities=thanaEntities2;
        }
        else if(district_f1.getId().trim().equals("212")){thanaEntities= thanaEntities2.stream().
                filter(thana -> (thana.getBlockCode().equals(String.valueOf(1965)))||(thana.getBlockCode().equals(String.valueOf(5001)))||(thana.getBlockCode().equals(String.valueOf(5002)))||(thana.getBlockCode().equals(String.valueOf(5003)))||(thana.getBlockCode().equals(String.valueOf(5004)))).collect(Collectors.toList());
        }else{
            thanaEntities=thanaEntities2;
        }
        ArrayList<String> stringArrayList2 = new ArrayList<>();
        stringArrayList2.add("--Select Thana--");
        if (stringArrayList2.size() > 0) {
            for (ThanaEntity thanaEntity : thanaEntities) {
                stringArrayList2.add(thanaEntity.getThanaName());
            }
        }
        sp_subdiv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList2));
        List<ThanaEntity> finalThanaEntities = thanaEntities;
        sp_subdiv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    thana = finalThanaEntities.get(i-1);
                } else {
                    thana = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (thana!=null){
            sp_subdiv.setSelection(((ArrayAdapter<String>) sp_subdiv.getAdapter()).getPosition(thana.getThanaName()));
            //sp_subdiv.setEnabled(false);
        }
    }

    private void initializePremisesSpinner() {
        final ArrayList<PremisesTypeEntity> premises = new DataBaseHelper(getActivity()).getPremises();
        ArrayList<String> stringArrayList2 = new ArrayList<>();
        stringArrayList2.add("--SELECT PREMISES--");
        if (stringArrayList2.size() > 0) {
            for (PremisesTypeEntity block : premises) {
                stringArrayList2.add(block.getName());
            }
        }
        sp_permisses.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList2));
        sp_permisses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    premisesTypeEntity = premises.get(i - 1);
                } else {

                    premisesTypeEntity = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void ShowDialog() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        datedialog = new DatePickerDialog(getActivity(),
                mDateSetListener, mYear, mMonth, mDay);
        datedialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datedialog.show();

    }

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            mYear = selectedYear;
            mMonth = selectedMonth;
            mDay = selectedDay;
            try {

                if (mDay < 10 && (mMonth + 1) > 9) {
                    mDay = Integer.parseInt("0" + mDay);
                    if (flag == 1)
                        tv_est_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append("0" + mDay));
                    else
                        tv_lic_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append("0" + mDay));
                } else if ((mMonth + 1) < 10 && mDay > 9) {
                    mMonth = Integer.parseInt("0" + mMonth);
                    if (flag == 1)
                        tv_est_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append(mDay));
                    else
                        tv_lic_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append(mDay));
                } else if ((mMonth + 1) < 10 && mDay < 10) {
                    mDay = Integer.parseInt("0" + mDay);
                    mMonth = Integer.parseInt("0" + mMonth);
                    if (flag == 1)
                        tv_est_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append("0" + mDay));
                    else
                        tv_lic_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append("0" + mDay));
                } else {
                    if (flag == 1)
                        tv_est_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
                    else
                        tv_lic_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    private boolean validate() {
        boolean validated = false;
        if (premisesTypeEntity == null) {
            Toast.makeText(getActivity(), "Select Premises Type !", Toast.LENGTH_SHORT).show();
            view_spin = sp_permisses;
        } else if (!edit_ap_name.getText().toString().trim().matches(GlobalVariable.CITY_PATTERN)) {
            edit_ap_name.setError("Enter Business Name");
            view_spin = edit_ap_name;
        } else if (!edit_add1.getText().toString().trim().matches(GlobalVariable.CITY_PATTERN)) {
            edit_add1.setError("Enter Valid Address");
            view_spin = edit_add1;
        } else if (!edit_landmark.getText().toString().trim().trim().matches(GlobalVariable.CITY_PATTERN)) {
            edit_landmark.setError("Enter Landmark");
            view_spin = edit_landmark;
        } else if (!edit_city.getText().toString().trim().matches(GlobalVariable.CITY_PATTERN)) {
            edit_city.setError("Enter Valid City");
            view_spin = edit_city;
        } else if (district_f1 == null) {
            Toast.makeText(getActivity(), "Select District !", Toast.LENGTH_SHORT).show();
            view_spin = sp_district;
        } else if (block_f1 == null) {
            Toast.makeText(getActivity(), "Select Block !", Toast.LENGTH_SHORT).show();
            view_spin = sp_block;
        }
        else if (thana == null && district_f1.getId().trim().equals("212") && ((block_f1.getValue().equals(String.valueOf(1965)))||(block_f1.getValue().equals(String.valueOf(5001)))||(block_f1.getValue().equals(String.valueOf(5002)))||(block_f1.getValue().equals(5003))||(block_f1.getValue().equals(String.valueOf(5004))))) {
            Toast.makeText(getActivity(), "Select Thana !", Toast.LENGTH_SHORT).show();
            view_spin = sp_subdiv;
         }
        else if (!edit_pin.getText().toString().matches(GlobalVariable.PIN_PATTERN)) {
            edit_pin.setError("Enter Valid PIN CODE");
            view_spin = edit_pin;
        } else if (!edit_mob.getText().toString().trim().matches(GlobalVariable.MOB_PATTERN)) {
            edit_mob.setError("Enter Valid MOBILE NO.");
            view_spin = edit_mob;
        } else if (!edit_email.getText().toString().trim().matches(GlobalVariable.EMAIL_PATTERN)) {
            edit_email.setError("Enter Valid Email");
            view_spin = edit_email;
        } else if (tv_est_date.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Select date of Establishment !", Toast.LENGTH_SHORT).show();
            view_spin = tv_est_date;
        } /*else if (tv_lic_date.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Select Licence Date", Toast.LENGTH_SHORT).show();
            view_spin = tv_lic_date;
        } */else if (firmType_f1 == null) {
            Toast.makeText(getActivity(), "Select Firm Type", Toast.LENGTH_SHORT).show();
            view_spin = sp_firm_type;
        } /*else if (tv_reg_date.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Select Date of Registration", Toast.LENGTH_SHORT).show();
            view_spin = tv_reg_date;
        } else if (edit_reg_f1.getText().toString().trim().equals("")) {
            edit_reg_f1.setError("Enter Registration No.");
            view_spin = edit_reg_f1;
        }*/ else if (tv_com_date.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Select Date of Commencement", Toast.LENGTH_SHORT).show();
            view_spin = tv_com_date;
        } else if (place.equals("")) {
            Toast.makeText(getActivity(), "Select Place", Toast.LENGTH_SHORT).show();
            view_spin = rd_gp_place;
        } else {
            validated = true;
        }
        return validated;
    }

    public long addVenderDetails() {
        long c = -1;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("shop_name", edit_ap_name.getText().toString().trim());
            values.put("premise_type", premisesTypeEntity.getValue().trim());
            values.put("add1", edit_add1.getText().toString().trim());
            values.put("add2", edit_add2.getText().toString().trim());
            values.put("landmark", edit_landmark.getText().toString().trim());
            values.put("city", edit_city.getText().toString().trim());
            values.put("dis_id", district_f1.getId().trim());
            values.put("block_id", block_f1.getValue().trim());
            values.put("pin", edit_pin.getText().toString().trim());
            values.put("mobile", edit_mob.getText().toString().trim());
            values.put("landline", edit_contact_landline.getText().toString().trim());
            values.put("email", edit_email.getText().toString().trim());
            values.put("date_of_est", tv_com_date.getText().toString().trim());
            if (!tv_lic_date.getText().toString().trim().equals(""))values.put("licence_date", tv_lic_date.getText().toString().trim());
            values.put("licence_no", edit_lice_no.getText().toString().trim());
            values.put("type_firm", firmType_f1.getId().trim());
            values.put("date_of_reg", tv_reg_date.getText().toString().trim());
            values.put("reg_no", edit_reg_f1.getText().toString().trim());
            values.put("date_of_comm", tv_com_date.getText().toString().trim());
            values.put("place_of_var", place);
            if (district_f1.getId().trim().equals("212")) {
                values.put("thana_id", thana.getThanaCode().trim());
            }
            if (block_f1.getEstbSubdivId().trim() != null) {
                if (!block_f1.getEstbSubdivId().trim().equals("null")) {
                    values.put("sub_div_id", block_f1.getEstbSubdivId().trim());
                } else {
                    values.put("sub_div_id", "149");
                }
            } else {
                values.put("sub_div_id", "149");
            }
            if (dataBaseHelper.getCountVenderDetails() <= 0) {
                c = db.insert("VENDER_DETAILS", null, values);
            } else {
                long id_ven = dataBaseHelper.getIdVEN();
                c = db.update("VENDER_DETAILS", values, "_id=?", new String[]{String.valueOf(id_ven)});
            }
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

    private void ShowDialog2() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        if (flag == 3) {
            datedialog = new DatePickerDialog(getActivity(),
                    mDateSetListener1, mYear, mMonth, mDay);
        } else if (flag == 4) {
            datedialog = new DatePickerDialog(getActivity(),
                    mDateSetListener2, mYear, mMonth, mDay);
        }
        datedialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datedialog.show();

    }

    DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            mYear = selectedYear;
            mMonth = selectedMonth;
            mDay = selectedDay;
            try {

                if (mDay < 10 && (mMonth + 1) > 9) {
                    mDay = Integer.parseInt("0" + mDay);
                    tv_reg_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append("0" + mDay));
                } else if ((mMonth + 1) < 10 && mDay > 9) {
                    mMonth = Integer.parseInt("0" + mMonth);
                    tv_reg_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append(mDay));
                } else if ((mMonth + 1) < 10 && mDay < 10) {
                    mDay = Integer.parseInt("0" + mDay);
                    mMonth = Integer.parseInt("0" + mMonth);
                    tv_reg_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append("0" + mDay));
                } else {
                    tv_reg_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            mYear = selectedYear;
            mMonth = selectedMonth;
            mDay = selectedDay;
            try {
                if (mDay < 10 && (mMonth + 1) > 9){
                    mDay = Integer.parseInt("0" + mDay);
                    tv_com_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append("0" + mDay));
                } else if ((mMonth + 1) < 10 && mDay > 9) {
                    mMonth = Integer.parseInt("0" + mMonth);
                    tv_com_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append(mDay));
                } else if ((mMonth + 1) < 10 && mDay < 10) {
                    mDay = Integer.parseInt("0" + mDay);
                    mMonth = Integer.parseInt("0" + mMonth);
                    tv_com_date.setText(new StringBuilder().append(mYear).append("-").append("0" + (mMonth + 1)).append("-").append("0" + mDay));
                } else {
                    tv_com_date.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}