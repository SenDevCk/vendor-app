package org.nic.lmd.wenderapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.asynctask.RegisterLoader;
import org.nic.lmd.wenderapp.entities.Designation;
import org.nic.lmd.wenderapp.entities.District;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.Utiilties;
import org.nic.lmd.wenderapp.utilities.VerhoeffAlgorithm;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class NameAndODFragment extends Fragment {


    Spinner sp_desig, sp_district;
    Button button_submit;
    EditText edit_ap_name, edit_city, edit_add1, edit_add2, edit_landmark, edit_pin, edit_mob, edit_contact_landline, edit_email, edit_con_per_name, edit_con_per_mb;
    String district_id = "0", designation_id = "0";
    ScrollView scroll_od;
    private View view_to_scroll;

    public NameAndODFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_name_and_o_d, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scroll_od =  getActivity().findViewById(R.id.scroll_od);
        sp_desig =  getActivity().findViewById(R.id.sp_desig);
        sp_district =  getActivity().findViewById(R.id.sp_district);
        initializeSpinner();
        edit_ap_name =  getActivity().findViewById(R.id.edit_ap_name);
        edit_city =  getActivity().findViewById(R.id.edit_city);
        edit_add1 =  getActivity().findViewById(R.id.edit_add1);
        edit_add2 =  getActivity().findViewById(R.id.edit_add2);
        edit_landmark =  getActivity().findViewById(R.id.edit_landmark);
        edit_pin =  getActivity().findViewById(R.id.edit_pin);
        edit_mob =  getActivity().findViewById(R.id.edit_mob);
        edit_contact_landline =  getActivity().findViewById(R.id.edit_contact_landline);
        edit_email =  getActivity().findViewById(R.id.edit_email);
        edit_con_per_name =  getActivity().findViewById(R.id.edit_con_per_name);
        edit_con_per_mb =  getActivity().findViewById(R.id.edit_con_per_mb);
        button_submit =  getActivity().findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    if (Utiilties.isOnline(getActivity())) {
                        new RegisterLoader(getActivity()).execute(GlobalVariable.nature_of_business.getId(), edit_ap_name.getText().toString().trim(),
                                designation_id.trim(), edit_add1.getText().toString().trim(), edit_add2.getText().toString().trim(),
                                "India", "Bihar", edit_city.getText().toString().trim(), district_id.trim(), edit_landmark.getText().toString().trim(),
                                edit_pin.getText().toString().trim(), edit_mob.getText().toString().trim(), edit_contact_landline.getText().toString().trim()
                                , edit_email.getText().toString().trim(), edit_con_per_name.getText().toString(), edit_con_per_mb.getText().toString().trim(),
                                GlobalVariable.userID, GlobalVariable.password, Boolean.toString(GlobalVariable.isManufacturere), Boolean.toString(GlobalVariable.isDeler), Boolean.toString(GlobalVariable.isRepeirer_ra)
                                , Boolean.toString(GlobalVariable.isConsumer), Boolean.toString(GlobalVariable.isRepeirer_ra), Boolean.toString(GlobalVariable.isRepeirer)
                        );
                    }else{
                        Toast.makeText(getActivity(), "Internet not avalable !", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Utiilties.scrollToView(scroll_od,view_to_scroll);
                }
            }
        });

    }

    private boolean validate() {
        boolean validate = false;
        if (!edit_ap_name.getText().toString().matches(GlobalVariable.CITY_PATTERN)) {
            edit_ap_name.setError("VALID APPLICANT NAME");
            view_to_scroll=edit_ap_name;
        } else if (designation_id.trim().equals("0")) {
            Toast.makeText(getActivity(), "Select Designation !", Toast.LENGTH_SHORT).show();
            view_to_scroll=sp_desig;
        } else if (district_id.trim().equals("0")) {
            Toast.makeText(getActivity(), "Select District !", Toast.LENGTH_SHORT).show();
            view_to_scroll=sp_district;
        } else if (!edit_city.getText().toString().matches(GlobalVariable.CITY_PATTERN)) {
            edit_city.setError("Enter VALID City Name");
            view_to_scroll=edit_city;
        }else if (edit_add1.getText().toString().trim().length()<2) {
            edit_add1.setError("Enter Address 1");
            view_to_scroll=edit_add1;
        }
        else if (!edit_landmark.getText().toString().trim().matches(GlobalVariable.CITY_PATTERN)) {
            edit_landmark.setError("Enter Landmark");
            view_to_scroll=edit_landmark;
        }
        else if (!edit_pin.getText().toString().matches(GlobalVariable.PIN_PATTERN) ) {
            edit_pin.setError("Enter Pin Code");
            view_to_scroll=edit_pin;
        }
        else if (!edit_mob.getText().toString().trim().matches(GlobalVariable.MOB_PATTERN)) {
            edit_mob.setError("Enter Valid Mobile No.");
            view_to_scroll=edit_mob;
        }
        else if (!edit_contact_landline.getText().toString().trim().equals("") && edit_contact_landline.getText().toString().trim().length()<10) {
            edit_contact_landline.setError("Invalid landline Number");
            view_to_scroll = edit_contact_landline;
        }
        else if (!edit_email.getText().toString().trim().matches(GlobalVariable.EMAIL_PATTERN)) {
            edit_email.setError("Enter Valid Email ID");
            view_to_scroll=edit_email;
        }
        else if (edit_con_per_name.getText().toString().trim().equals("")||edit_con_per_name.getText().toString().trim().length()<3) {
            edit_con_per_name.setError("Must be three characters");
            view_to_scroll=edit_con_per_name;
        }
        else if (!edit_con_per_mb.getText().toString().trim().matches(GlobalVariable.MOB_PATTERN)){
            edit_con_per_mb.setError("Enter Contact Persion Mobile Number");
            view_to_scroll=edit_con_per_mb;
        }else{
            validate=true;
        }
        return validate;
    }
    public static boolean validateAadharNumber(String aadharNumber){
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }
    private void initializeSpinner() {
        final ArrayList<Designation> natureOfRequests = new DataBaseHelper(getActivity()).getDesignation();
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("--SELECT DESIGNATION--");
        if (stringArrayList.size() > 0) {
            for (Designation designation : natureOfRequests) {
                stringArrayList.add(designation.getName());
            }
        }
        sp_desig.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList));
        sp_desig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    designation_id = natureOfRequests.get(i-1).getId();
                } else {
                    designation_id = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                    district_id = districts.get(i-1).getId();
                } else {
                    district_id = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}