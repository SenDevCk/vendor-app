package org.nic.lmd.wenderapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.WeightDenominationActivity;
import org.nic.lmd.wenderapp.entities.DenomintionEntity;
import org.nic.lmd.wenderapp.entities.ProposalTypeEntity;
import org.nic.lmd.wenderapp.entities.TypeOfPump;
import org.nic.lmd.wenderapp.entities.VehicleTankDetails;
import org.nic.lmd.wenderapp.entities.WeightCategoriesEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.MyBounceInterpolator;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;
import java.util.Objects;

public class ApplyNew4Fragment extends Fragment {


    FrameLayout frameLayout, ll_single,frame_sel_type;
    FragmentManager fragmentManager;
    Button button_next4, button_adddenomination,add_tank_detils;
    Spinner sp_proposal_type_f4, sp_cat_f4, sp_valid, sp_sel_type;
    Spinner sp_min_den, sp_max_den;
    TextView sp_denomination;
    static String pro_id = "0", cat_id = "0", cat_val_year = "0";
    TextView text_get_all;
    EditText edit_set_no;
    String[] sel_type = {"-- SELECT TYPE --", "Individual", "Set"};
    LinearLayout ll_min_max,ll_entry_tank;
    String min_val = "0", max_val = "0";
    AppCompatEditText edit_tank_reg_no,edit_tank_eng_no,edit_tank_chechis,edit_tank_ow_name;
    AppCompatSpinner sp_companies;
    String country="";

    public ApplyNew4Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_new4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edit_tank_reg_no =  getActivity().findViewById(R.id.edit_tank_reg_no);
        edit_tank_eng_no =  getActivity().findViewById(R.id.edit_tank_eng_no);
        edit_tank_chechis =  getActivity().findViewById(R.id.edit_tank_chechis);
        edit_tank_ow_name =  getActivity().findViewById(R.id.edit_tank_ow_name);
        sp_companies =  getActivity().findViewById(R.id.sp_companies);
        edit_set_no =  getActivity().findViewById(R.id.edit_set_no);
        ll_single =  getActivity().findViewById(R.id.ll_single);
        frame_sel_type =  getActivity().findViewById(R.id.frame_sel_type);
        ll_single.setVisibility(View.GONE);
        ll_min_max =  getActivity().findViewById(R.id.ll_min_max);
        ll_entry_tank =  getActivity().findViewById(R.id.ll_entry_tank);
        ll_min_max.setVisibility(View.GONE);
        ll_entry_tank.setVisibility(View.GONE);
        text_get_all =  getActivity().findViewById(R.id.text_get_all);
        text_get_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new DataBaseHelper(getActivity()).getAddedWeightCount() > 0) {
                    Intent intent = new Intent(getActivity(), WeightDenominationActivity.class);
                    intent.putExtra("category_id", "0");
                    intent.putExtra("year", "0");
                    startActivityForResult(intent, 11);
                } else {
                    Toast.makeText(getActivity(), "Nothing added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sp_proposal_type_f4 =  getActivity().findViewById(R.id.sp_proposal_type_f4);
        sp_sel_type =  getActivity().findViewById(R.id.sp_sel_type);
        sp_cat_f4 =  getActivity().findViewById(R.id.sp_cat_f4);
        sp_min_den =  getActivity().findViewById(R.id.sp_min_den);
        sp_max_den =  getActivity().findViewById(R.id.sp_max_den);
        sp_denomination =  getActivity().findViewById(R.id.sp_denomination_f4);
        sp_denomination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cat_id.equals("0")) {
                    Intent intent = new Intent(getActivity(), WeightDenominationActivity.class);
                    intent.putExtra("category_id", cat_id);
                    intent.putExtra("year", cat_val_year);
                    startActivityForResult(intent, 11);
                } else {
                    Toast.makeText(getActivity(), "Select Category !", Toast.LENGTH_LONG).show();
                }
            }
        });
        sp_valid =  getActivity().findViewById(R.id.sp_valid);
        button_next4 =  getActivity().findViewById(R.id.button_next4);
        button_adddenomination =  getActivity().findViewById(R.id.button_adddenomination);
        button_next4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout =  getActivity().findViewById(R.id.frame_ap_new);
                ApplyNew5Fragment applyNew5Fragment = new ApplyNew5Fragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_ap_new, applyNew5Fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        button_adddenomination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (min_val.equals("0")) {
                    Toast.makeText(getActivity(), "Select Min Value", Toast.LENGTH_SHORT).show();
                } else if (max_val.equals("0")) {
                    Toast.makeText(getActivity(), "Select Max Value", Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(max_val)<Integer.parseInt(min_val)){
                    Toast.makeText(getActivity(), "MAX value must be greater than MIN value", Toast.LENGTH_SHORT).show();
                }
                else if (edit_set_no.getText().toString().trim().equals("") || Integer.parseInt(edit_set_no.getText().toString().trim()) < 1) {
                    Toast.makeText(getActivity(), "Enter Number of Set", Toast.LENGTH_SHORT).show();
                    edit_set_no.setError("Enter Number of Set");
                } else {
                    long count = new DataBaseHelper(getActivity()).updateWeightDenominationBetween(cat_id, max_val, min_val, Integer.parseInt(edit_set_no.getText().toString().trim()), cat_val_year, pro_id);
                    if (count > 0) {
                        edit_set_no.setText("");
                        sp_max_den.setSelection(0);
                        sp_min_den.setSelection(0);
                        Toast.makeText(getActivity(), "Added " + count + " denominations", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "No data found between range", Toast.LENGTH_SHORT).show();
                    }
                    setTotalAdded();
                }
            }
        });
        add_tank_detils.setOnClickListener((View v)->{
            addTankDetails();
        });
        initialiseSpinnerPro();
        categorySpinnerBinding("0");
        selectionTypeBinding();
        mindenominationBinding();
        maxdenominationBinding();
        validityBinding();
        setTotalAdded();

    }

    private void addTankDetails() {
        System.out.println("under addTankDetails");
        if (Objects.requireNonNull(edit_tank_reg_no.getText()).toString().trim().equals("")){
            Toast.makeText(getActivity(), "Enter "+getResources().getString(R.string.reg_no), Toast.LENGTH_SHORT).show();
        }else if (Objects.requireNonNull(edit_tank_eng_no.getText()).toString().trim().equals("")){
            Toast.makeText(getActivity(), "Enter "+getResources().getString(R.string.eng_no), Toast.LENGTH_SHORT).show();
        }else if (Objects.requireNonNull(edit_tank_chechis.getText()).toString().trim().equals("")){
            Toast.makeText(getActivity(), "Enter "+getResources().getString(R.string.chechis_number), Toast.LENGTH_SHORT).show();
        }else if (Objects.requireNonNull(edit_tank_ow_name.getText()).toString().trim().equals("")){
            Toast.makeText(getActivity(), "Enter "+getResources().getString(R.string.firm_own_name), Toast.LENGTH_SHORT).show();
        }else if (country.trim().equals("")){
            Toast.makeText(getActivity(), "Select Country", Toast.LENGTH_SHORT).show();
        }
        else{
            VehicleTankDetails vehicleTankDetail=new VehicleTankDetails();
            vehicleTankDetail.setRegNumber(edit_tank_reg_no.getText().toString().trim());
            vehicleTankDetail.setChechisNumber(edit_tank_reg_no.getText().toString().trim());
            vehicleTankDetail.setEngineNumber(edit_tank_reg_no.getText().toString().trim());
            vehicleTankDetail.setOwnerFirmName(edit_tank_reg_no.getText().toString().trim());
            vehicleTankDetail.setCountry(country);
            long c=new DataBaseHelper(getActivity()).addTank(vehicleTankDetail);
            if (c>0){
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Not Saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTotalAdded();
    }

    private void setTotalAdded() {
        long cou_to = new DataBaseHelper(getActivity()).getAddedWeightCount();
        long cou_to2 = new DataBaseHelper(getActivity()).getTankcount();
        if (cou_to > 0) {
            if (cou_to2<=0) {
                text_get_all.setText("" + cou_to);
            }else{
                text_get_all.setText("" + (cou_to+cou_to2-1));
            }
        } else {
            text_get_all.setText("0");
        }
        Utiilties.didTapButton(text_get_all, getActivity());
    }


    private void mindenominationBinding() {
        final ArrayList<DenomintionEntity> denomintionEntities = new DataBaseHelper(getActivity()).getWeightDenomination(cat_id, "1");
        ArrayList<String> list_string_den = new ArrayList<>();
        list_string_den.add("-- SELECT MIN --");
        if (denomintionEntities.size() > 0) {
            for (DenomintionEntity denomintionEntity : denomintionEntities) {
                list_string_den.add(denomintionEntity.getDenominationDesc());
            }
        }
        sp_min_den.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_den));
        sp_min_den.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    min_val = denomintionEntities.get(i - 1).getValue();
                } else {
                    min_val = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void maxdenominationBinding() {
        final ArrayList<DenomintionEntity> denomintionEntities = new DataBaseHelper(getActivity()).getWeightDenomination(cat_id, "1");
        ArrayList<String> list_string_den = new ArrayList<>();
        list_string_den.add("-- SELECT MAX --");
        if (denomintionEntities.size() > 0) {
            for (DenomintionEntity denomintionEntity : denomintionEntities) {
                list_string_den.add(denomintionEntity.getDenominationDesc());
            }
        }
        sp_max_den.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_den));
        sp_max_den.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    max_val = denomintionEntities.get(i - 1).getValue();
                } else {
                    max_val = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initialiseSpinnerPro() {
        final ArrayList<ProposalTypeEntity> proposalTypeEntities = new DataBaseHelper(getActivity()).getProposalType();
        ArrayList<String> list_string_pro = new ArrayList<>();
        list_string_pro.add("-- SELECT TYPE OF PROPOSAL --");
        if (proposalTypeEntities.size() > 0) {
            for (ProposalTypeEntity proposalTypeEntity : proposalTypeEntities) {
                list_string_pro.add(proposalTypeEntity.getValue());
            }
        }
        sp_proposal_type_f4.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_pro));
        sp_proposal_type_f4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    pro_id = "0";
                    categorySpinnerBinding(pro_id);
                } else {
                    pro_id = proposalTypeEntities.get(i - 1).getId();
                    categorySpinnerBinding(proposalTypeEntities.get(i - 1).getId().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void categorySpinnerBinding(String pro_id) {
        final ArrayList<WeightCategoriesEntity> weightCategories = new DataBaseHelper(getActivity()).getWeightCategories(pro_id);
        ArrayList<String> list_string_cat = new ArrayList<>();
        list_string_cat.add("-- SELECT TYPE OF CATEGORY --");
        if (weightCategories.size() > 0) {
            for (WeightCategoriesEntity proposalTypeEntity : weightCategories) {
                list_string_cat.add(proposalTypeEntity.getValue());
            }
        }
        sp_cat_f4.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_cat));
        sp_cat_f4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    cat_id = "0";
                    cat_val_year = "0";
                    frame_sel_type.setVisibility(View.GONE);
                } else {
                    frame_sel_type.setVisibility(View.VISIBLE);
                    cat_id = weightCategories.get(i - 1).getId().trim();
                    cat_val_year = weightCategories.get(i - 1).getValidity().trim();
                    sp_valid.setEnabled(true);
                    sp_valid.setSelection(((ArrayAdapter<String>) sp_valid.getAdapter()).getPosition(weightCategories.get(i - 1).getValidity().trim()));
                    sp_valid.setEnabled(false);
                    if (Integer.parseInt(cat_id)>10) {
                        sp_sel_type.setSelection(1);
                        sp_sel_type.setEnabled(false);
                        if (cat_id.equals("19")) {
                            ll_entry_tank.setVisibility(View.VISIBLE);
                            initialiseTankDetails();
                        } else {
                            ll_entry_tank.setVisibility(View.GONE);
                        }
                    }else{
                        sp_sel_type.setEnabled(true);
                        sp_sel_type.setSelection(0);
                    }
                }
                maxdenominationBinding();
                mindenominationBinding();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //sp_sel_type.setVisibility(View.GONE);
            }
        });
    }

    private void selectionTypeBinding() {
        sp_sel_type.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sel_type));
        sp_sel_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    ll_single.setVisibility(View.GONE);
                    ll_min_max.setVisibility(View.GONE);
                } else if (i == 1) {
                    ll_single.setVisibility(View.VISIBLE);
                    ll_min_max.setVisibility(View.GONE);
                } else {
                    ll_min_max.setVisibility(View.VISIBLE);
                    ll_single.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void CountyBinding(){
        sp_companies.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"India","Nepal"}));
        sp_companies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {

                }  else {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initialiseTankDetails(){
        CountyBinding();
        edit_tank_chechis.setText("");
        edit_tank_eng_no.setText("");
        edit_tank_ow_name.setText("");
        edit_tank_reg_no.setText("");
    }

    private void validityBinding() {
        //final ArrayList<DenomintionEntity> denomintionEntities = new DataBaseHelper(getActivity()).getWeightDenomination(pro_id);
        ArrayList<String> list_string_denomi = new ArrayList<>();
        list_string_denomi.add("-- SELECT Validity --");
        list_string_denomi.add("1");
        list_string_denomi.add("2");
        list_string_denomi.add("3");
        list_string_denomi.add("4");
        list_string_denomi.add("5");
        sp_valid.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_denomi));
        sp_valid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}