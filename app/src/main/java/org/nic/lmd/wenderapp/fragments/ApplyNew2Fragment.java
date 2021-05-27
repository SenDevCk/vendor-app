package org.nic.lmd.wenderapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.PartnerActivity;
import org.nic.lmd.wenderapp.entities.Block;
import org.nic.lmd.wenderapp.entities.Designation;
import org.nic.lmd.wenderapp.entities.District;
import org.nic.lmd.wenderapp.entities.FirmType;
import org.nic.lmd.wenderapp.entities.PatnerEntity;
import org.nic.lmd.wenderapp.entities.UserData;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.Utiilties;
import org.nic.lmd.wenderapp.utilities.VerhoeffAlgorithm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class ApplyNew2Fragment extends Fragment {

    Button button_next2, button_add_partner;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    TextView tv_added_par;
    Spinner sp_block_f2, sp_dis_f2, sp_desig;
    public EditText edit_name_f2, edit_fname_f2, edit_adhar_f2, edit_add1_f2, edit_add2_f2,
            edit_landmark_f2, edit_city_f2, edit_pin_f2, edit_mob_f2, edit_contact_landline_f2,
            edit_email_f2;
    ScrollView scroll_f2;
    View view_name;
    public static PatnerEntity partner;
    CheckBox check_49;
    long count_par = 0;
    int flag = 0;
    public static District district_f2;
    public static Block block_f2;
    public static Designation designation_f2 = null;

    public ApplyNew2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_new2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scroll_f2 = getActivity().findViewById(R.id.scroll_f2);
        check_49 = getActivity().findViewById(R.id.check_49);
        tv_added_par =  getActivity().findViewById(R.id.tv_added_par);
        tv_added_par.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = tv_added_par.getText().toString().trim();
                if (Integer.parseInt(value) > 0) {
                    Intent intent = new Intent(getActivity(), PartnerActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "No data added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sp_desig = getActivity().findViewById(R.id.sp_desig_f2);
        sp_dis_f2 = getActivity().findViewById(R.id.sp_dis_f2);
        sp_block_f2 = getActivity().findViewById(R.id.sp_block_f2);
        initialiseDesignationSpiner();
        initializeDistrictSpinner();
        initializeBlockSpinner();
        button_next2 =  getActivity().findViewById(R.id.button_next2);
        button_add_partner =  getActivity().findViewById(R.id.button_add_partner);
        button_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new DataBaseHelper(getActivity()).getAllPatnersCount() < 1) {
                    Toast.makeText(getActivity(), "Please add atleast one patner", Toast.LENGTH_SHORT).show();
                } else {
                    frameLayout =  getActivity().findViewById(R.id.frame_ap_new);
                    ApplyNew3Fragment applyNew3Fragment = new ApplyNew3Fragment();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_ap_new, applyNew3Fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        button_add_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    partner = new PatnerEntity();
                    partner.setFirm_type("0");
                    partner.setDate_of_com("");
                    partner.setPlace_of_var("");
                    partner.setDesignation_id("" + designation_f2.getId().trim());
                    partner.setName(edit_name_f2.getText().toString().trim());
                    partner.setFather_name(edit_fname_f2.getText().toString().trim());
                    partner.setAdhar_vid(edit_adhar_f2.getText().toString().trim());
                    partner.setAdd1(edit_add1_f2.getText().toString().trim());
                    partner.setAdd2(edit_add2_f2.getText().toString().trim());
                    partner.setLandmark(edit_landmark_f2.getText().toString().trim());
                    partner.setCity(edit_city_f2.getText().toString().trim());
                    partner.setDistrict(district_f2.getId().trim());
                    partner.setBlock(block_f2.getValue().trim());
                    partner.setPinCode(edit_pin_f2.getText().toString().trim());
                    partner.setMobile(edit_mob_f2.getText().toString().trim());
                    partner.setLandline(edit_contact_landline_f2.getText().toString().trim());
                    partner.setEmail(edit_email_f2.getText().toString().trim());
                    if (check_49.isChecked()) partner.setIs_nom_under49("Y");
                    else partner.setIs_nom_under49("N");
                    long c = new DataBaseHelper(getActivity()).addPartner(partner);
                    if (c > 0) {
                        Toast.makeText(getActivity(), "Saved !", Toast.LENGTH_SHORT).show();
                        setPartnerCount();
                    } else {
                        Toast.makeText(getActivity(), "Not saved !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Utiilties.scrollToView(scroll_f2, view_name);
                    Toast.makeText(getActivity(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        edit_name_f2 =  getActivity().findViewById(R.id.edit_name_f2);
        edit_fname_f2 =  getActivity().findViewById(R.id.edit_fname_f2);
        edit_adhar_f2 =  getActivity().findViewById(R.id.edit_adhar_f2);
        edit_add1_f2 =  getActivity().findViewById(R.id.edit_add1_f2);
        edit_add2_f2 =  getActivity().findViewById(R.id.edit_add2_f2);
        edit_landmark_f2 =  getActivity().findViewById(R.id.edit_landmark_f2);
        edit_city_f2 =  getActivity().findViewById(R.id.edit_city_f2);

        edit_pin_f2 =  getActivity().findViewById(R.id.edit_pin_f2);
        edit_mob_f2 =  getActivity().findViewById(R.id.edit_mob_f2);
        edit_contact_landline_f2 =  getActivity().findViewById(R.id.edit_contact_landline_f2);
        edit_email_f2 =  getActivity().findViewById(R.id.edit_email_f2);

        UserData userData = CommonPref.getUserDetails(getActivity());
        edit_add1_f2.setText("" + userData.getAddress1());
        edit_add2_f2.setText("" + userData.getAddress2());
        edit_mob_f2.setText("" + userData.getMobile());
        edit_email_f2.setText("" + userData.getEmailid());
        edit_landmark_f2.setText("" + userData.getLandMark());
        edit_contact_landline_f2.setText("" + userData.getContactNo());
        edit_pin_f2.setText("" + userData.getPinCode());
        edit_city_f2.setText("" + userData.getCity());
    }

    private void setPartnerCount() {
        count_par = new DataBaseHelper(getActivity()).getAllPatnersCount();
        if (count_par > 0) {
            tv_added_par.setText("" + count_par);
        } else {
            tv_added_par.setText("0");
        }
        Utiilties.didTapButton(tv_added_par, getActivity());
    }


    private void initialiseDesignationSpiner() {
        //designation Spinner
        final ArrayList<Designation> designations = new DataBaseHelper(getActivity()).getDesignation();
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("--SELECT DESIGNATION--");
        if (stringArrayList.size() > 0) {
            for (Designation designation : designations) {
                stringArrayList.add(designation.getName());
            }
        }
        sp_desig.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList));
        sp_desig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    designation_f2 = designations.get(i - 1);
                } else {
                    designation_f2 = null;
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
        sp_dis_f2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList2));
        sp_dis_f2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    district_f2 = districts.get(i - 1);
                    initializeBlockSpinner();

                } else {
                    district_f2 = null;
                    initializeBlockSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String dist_name = null;
        if (district_f2 == null) {
            ArrayList<District> districtArrayList = new DataBaseHelper(getActivity()).getDistrict();

            for (District district : districtArrayList) {
                if (district.getId().equals(CommonPref.getUserDetails(getActivity()).getDistrictid())) {
                    dist_name = district.getName().trim();
                }
            }
        } else {
            dist_name = district_f2.getName();
        }
        sp_dis_f2.setSelection(((ArrayAdapter<String>) sp_dis_f2.getAdapter()).getPosition(dist_name));
    }

    private void initializeBlockSpinner() {
        String dist_id = "";
        if (district_f2 != null) dist_id = district_f2.getId().trim();
        final ArrayList<Block> blocks = new DataBaseHelper(getActivity()).getBlock(dist_id);
        ArrayList<String> stringArrayList2 = new ArrayList<>();
        stringArrayList2.add("--SELECT BLOCK--");
        if (stringArrayList2.size() > 0) {
            for (Block block : blocks) {
                stringArrayList2.add(block.getName());
            }
        }
        sp_block_f2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList2));
        sp_block_f2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    block_f2 = blocks.get(i - 1);
                } else {
                    block_f2 = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (block_f2 != null) {
            sp_block_f2.setSelection(((ArrayAdapter<String>) sp_block_f2.getAdapter()).getPosition(block_f2.getName()));
        }
    }

    private boolean validate() {
        boolean validated = false;
        if (designation_f2 == null) {
            Toast.makeText(getActivity(), "Select Designation", Toast.LENGTH_SHORT).show();
            view_name = sp_desig;
        } else if (edit_name_f2.getText().toString().trim().length()<3) {
            edit_name_f2.setError("Enter Valid Full Name");
            view_name = edit_name_f2;
        } else if (edit_fname_f2.getText().toString().trim().length()<3) {
            edit_fname_f2.setError("Enter Valid Father/Mother/Husband Name");
            view_name = edit_fname_f2;
        } else if (!validateAadharNumber(edit_adhar_f2.getText().toString().trim())) {
            edit_adhar_f2.setError("Enter Valid Adhar Number");
            view_name = edit_adhar_f2;
        } else if (!edit_add1_f2.getText().toString().trim().matches(GlobalVariable.CITY_PATTERN)) {
            edit_add1_f2.setError("Enter Valid Address 1");
            view_name = edit_add1_f2;
        } else if (!edit_landmark_f2.getText().toString().trim().matches(GlobalVariable.CITY_PATTERN)) {
            edit_landmark_f2.setError("Enter Valid Landmark");
            view_name = edit_landmark_f2;
        } else if (!edit_contact_landline_f2.getText().toString().trim().equals("") && edit_contact_landline_f2.getText().toString().trim().length()<10) {
            edit_contact_landline_f2.setError("Invalid landline Number");
            view_name = edit_landmark_f2;
        } else if (!edit_city_f2.getText().toString().trim().matches(GlobalVariable.CITY_PATTERN)) {
            edit_city_f2.setError("Enter Valid City Name");
            view_name = edit_city_f2;
        } else if (district_f2 == null) {
            Toast.makeText(getActivity(), "Select District", Toast.LENGTH_LONG).show();
            view_name = sp_dis_f2;
        } else if (block_f2 == null) {
            Toast.makeText(getActivity(), "Select Block", Toast.LENGTH_LONG).show();
            view_name = sp_block_f2;
        } else if (!edit_pin_f2.getText().toString().matches(GlobalVariable.PIN_PATTERN)) {
            edit_pin_f2.setError("Invalid PIN Code");
            view_name = edit_pin_f2;
        } else if (!edit_mob_f2.getText().toString().trim().matches(GlobalVariable.MOB_PATTERN)) {
            edit_mob_f2.setError("Invalid Mobile");
            view_name = edit_mob_f2;
        } else if (!edit_email_f2.getText().toString().trim().matches(GlobalVariable.EMAIL_PATTERN)) {
            edit_email_f2.setError("Invalid Email");
            view_name = edit_email_f2;
        } else {
            validated = true;
        }
        return validated;
    }

    public static boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    @Override
    public void onResume() {
        setPartnerCount();
        super.onResume();
    }
}