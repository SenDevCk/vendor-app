package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.TankDetailsActivity;
import org.nic.lmd.wenderapp.activities.WeightDenominationActivity;
import org.nic.lmd.wenderapp.entities.DenomintionEntity;
import org.nic.lmd.wenderapp.entities.ProposalTypeEntity;
import org.nic.lmd.wenderapp.entities.VehicleTankDetails;
import org.nic.lmd.wenderapp.entities.WeightCategoriesEntity;
import org.nic.lmd.wenderapp.interfaces.EventHandler;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;
import java.util.Objects;

public class WeightDenominationAdapter extends BaseAdapter {
    Activity activity;
    String cot_id, val_year;
    ArrayList<DenomintionEntity> denomintionEntities;
    LayoutInflater layoutInflater;
    TextView textView;
    String country="";

    public WeightDenominationAdapter(Activity activity, String cot_id, String val_year, TextView textView) {
        this.activity = activity;
        this.cot_id = cot_id;
        this.val_year = val_year;
        this.textView = textView;
        layoutInflater = this.activity.getLayoutInflater();
        notifyList();
    }

    @Override
    public int getCount() {
        return denomintionEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return denomintionEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View rootview, ViewGroup viewGroup) {
        rootview = layoutInflater.inflate(R.layout.denomination_item, null, false);

        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.text_qty =  rootview.findViewById(R.id.text_qty);
        viewHolder.text_val =  rootview.findViewById(R.id.text_val);
        viewHolder.text_item_name =  rootview.findViewById(R.id.text_item_name);
        viewHolder.tv_inc =  rootview.findViewById(R.id.tv_inc);
        viewHolder.img_inc =  rootview.findViewById(R.id.img_inc);
        viewHolder.img_dec =  rootview.findViewById(R.id.img_dec);
        viewHolder.cat_type =  rootview.findViewById(R.id.tv_pro);
        viewHolder.cat_name =  rootview.findViewById(R.id.cat_name);
        final DenomintionEntity denomintionEntity=denomintionEntities.get(position);
        WeightCategoriesEntity weightCategoriesEntity = new DataBaseHelper(activity).getCategoryById(denomintionEntity.getCategoryId().trim());
        final ProposalTypeEntity proposalTypeEntity = new DataBaseHelper(activity).getProByID(weightCategoriesEntity.getPro_id().trim());

        viewHolder.cat_type.setText(proposalTypeEntity.getValue());
        viewHolder.cat_name.setText("" + weightCategoriesEntity.getValue());
        viewHolder.tv_inc.setText("" + denomintionEntity.getQuantity());
        viewHolder.text_item_name.setText("" + denomintionEntity.getDenominationDesc());
        if (this.val_year.equals("0")) {
            this.val_year = denomintionEntity.getVal_year();
        }
        viewHolder.text_val.setText("Validity : " + val_year + " year");
        if (weightCategoriesEntity.getId().equals("19")) {
            viewHolder.text_qty.setText("Capacity of Each Compartment in Litres*");
            viewHolder.text_qty.setTextSize(7);
            viewHolder.text_qty.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }
                viewHolder.img_inc.setOnClickListener(view -> {
                    if (denomintionEntity.getCategoryId().trim().equals("19")){
                        AlertDialogForNozzleEntry(denomintionEntity,proposalTypeEntity);
                    }else {
                        int incre = Integer.parseInt(viewHolder.tv_inc.getText().toString().trim());
                        incre = incre + 1;
                        viewHolder.tv_inc.setText("" + incre);
                        if (Integer.parseInt(viewHolder.tv_inc.getText().toString()) > 0) {
                            new DataBaseHelper(activity).updateDenomination("", "", denomintionEntity.getValue(), "Y", viewHolder.tv_inc.getText().toString(), 0, "0", val_year, proposalTypeEntity.getId().trim(), false);
                        }
                        textView.setText("" + new DataBaseHelper(activity).getAddedWeightCount());
                        Utiilties.didTapButton(textView, activity);
                    }
                });

                viewHolder.img_dec.setOnClickListener(view -> {
                            if (denomintionEntity.getCategoryId().trim().equals("19")){
                                AlertDialogForNozzleEntry(denomintionEntity,proposalTypeEntity);
                            }else {
                                int decre = Integer.parseInt(viewHolder.tv_inc.getText().toString().trim());
                                if (decre > 0) {
                                    if (Integer.parseInt(denomintionEntities.get(position).getIs_set()) > 0 && decre <= (Integer.parseInt(denomintionEntities.get(position).getSet_m()) * Integer.parseInt(denomintionEntities.get(position).getIs_set()))) {
                                        Toast.makeText(activity, "It is Set Amount", Toast.LENGTH_SHORT).show();
                                    } else {
                                        decre = decre - 1;
                                        if (decre == 0) {
                                            viewHolder.tv_inc.setText("" + decre);
                                            new DataBaseHelper(activity).updateDenomination("", "", denomintionEntity.getValue(), "N", viewHolder.tv_inc.getText().toString(), 0, "0", val_year, proposalTypeEntity.getId().trim(), false);
                                        } else {
                                            viewHolder.tv_inc.setText("" + decre);
                                            new DataBaseHelper(activity).updateDenomination("", "", denomintionEntity.getValue(), "Y", viewHolder.tv_inc.getText().toString(), 0, "0", val_year, proposalTypeEntity.getId().trim(), false);
                                        }
                                        textView.setText("" + new DataBaseHelper(activity).getAddedWeightCount());
                                        Utiilties.didTapButton(textView, activity);
                                    }
                                }
                            }
                });




        viewHolder.tog_denomination =  rootview.findViewById(R.id.tog_denomination);
        if (denomintionEntity.getChecked().equals("Y")) {
            viewHolder.tog_denomination.setChecked(true);
        } else {
            viewHolder.tog_denomination.setChecked(false);
        }

        return rootview;
    }



    class ViewHolder {
        TextView text_item_name, tv_inc, cat_type, cat_name, text_qty, text_val;
        ToggleButton tog_denomination;
        ImageView img_inc, img_dec;
    }
   private void notifyList(){
       denomintionEntities=null;
       if (this.cot_id.equals("0")) denomintionEntities = new DataBaseHelper(activity).getWeightDenomination(this.cot_id, "0");
       else denomintionEntities = new DataBaseHelper(activity).getWeightDenomination(this.cot_id, "1");

   }
 public void AlertDialogForNozzleEntry(final DenomintionEntity denomintionEntity,final ProposalTypeEntity proposalTypeEntity) {
        TankDetailAdapter.handleEvent(() -> {
            notifyList();
            notifyDataSetChanged();
        });
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.tank_entry_dialog);
        dialog.setTitle("Tank Entry");
        EditText edit_tank_reg_no = dialog.findViewById(R.id.edit_tank_reg_no);
        EditText edit_tank_eng_no = dialog.findViewById(R.id.edit_tank_eng_no);
        EditText edit_tank_chechis = dialog.findViewById(R.id.edit_tank_chechis);
        EditText edit_tank_ow_name = dialog.findViewById(R.id.edit_tank_ow_name);
        Button add_nozzle = dialog.findViewById(R.id.add_tank);
        RelativeLayout end_dialog = dialog.findViewById(R.id.end_dialog);
        Spinner sp_contries = dialog.findViewById(R.id.sp_contries);
        final RecyclerView list_tanks = dialog.findViewById(R.id.list_tanks);
        CountyBinding(sp_contries);
        populateProductRecycler(list_tanks,denomintionEntity);
        // if button is clicked, close the custom dialog
        end_dialog.setOnClickListener(v -> dialog.dismiss());
        add_nozzle.setOnClickListener(v -> {  System.out.println("under addTankDetails");
            if (Objects.requireNonNull(edit_tank_reg_no.getText()).toString().trim().equals("")){
                Toast.makeText(activity, "Enter "+activity.getResources().getString(R.string.reg_no), Toast.LENGTH_SHORT).show();
            }else if (Objects.requireNonNull(edit_tank_eng_no.getText()).toString().trim().equals("")){
                Toast.makeText(activity, "Enter "+activity.getResources().getString(R.string.eng_no), Toast.LENGTH_SHORT).show();
            }else if (Objects.requireNonNull(edit_tank_chechis.getText()).toString().trim().equals("")){
                Toast.makeText(activity, "Enter "+activity.getResources().getString(R.string.chechis_number), Toast.LENGTH_SHORT).show();
            }else if (Objects.requireNonNull(edit_tank_ow_name.getText()).toString().trim().equals("")){
                Toast.makeText(activity, "Enter "+activity.getResources().getString(R.string.firm_own_name), Toast.LENGTH_SHORT).show();
            }else if (country.trim().equals("")){
                Toast.makeText(activity, "Select Country", Toast.LENGTH_SHORT).show();
            }
            else{
                VehicleTankDetails vehicleTankDetail=new VehicleTankDetails();
                vehicleTankDetail.setRegNumber(edit_tank_reg_no.getText().toString().trim());
                vehicleTankDetail.setChechisNumber(edit_tank_chechis.getText().toString().trim());
                vehicleTankDetail.setEngineNumber(edit_tank_eng_no.getText().toString().trim());
                vehicleTankDetail.setOwnerFirmName(edit_tank_ow_name.getText().toString().trim());
                vehicleTankDetail.setCountry(country);
                vehicleTankDetail.setDenomId(Integer.parseInt(denomintionEntity.getValue()));
                long c=new DataBaseHelper(activity).addTank(vehicleTankDetail);
                if (c>0){
                    edit_tank_reg_no.setText("");
                    edit_tank_eng_no.setText("");
                    edit_tank_chechis.setText("");
                    edit_tank_ow_name.setText("");
                    sp_contries.setSelection(0);
                    populateProductRecycler(list_tanks,denomintionEntity);
                    int count=new DataBaseHelper(activity).getTotalTank(String.valueOf(denomintionEntity.getValue())).size();
                    new DataBaseHelper(activity).updateDenomination("", "", denomintionEntity.getValue(), "Y", String.valueOf(count), 0, "0", val_year, proposalTypeEntity.getId().trim(), false);
                    textView.setText("" + new DataBaseHelper(activity).getAddedWeightCount());
                    Utiilties.didTapButton(textView, activity);
                    notifyList();
                    notifyDataSetChanged();
                    Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity, "Not Saved", Toast.LENGTH_SHORT).show();
                }
            }
         });
        dialog.show();
    }
    private void populateProductRecycler(RecyclerView list_tanks,final DenomintionEntity denomintionEntity) {
               list_tanks.invalidate();
               TankDetailAdapter nozzleRecyclerAdapter = new TankDetailAdapter(activity,denomintionEntity.getValue(),val_year,textView);
               RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(activity);
               list_tanks.setLayoutManager(mLayoutManager2);
               list_tanks.setItemAnimator(new DefaultItemAnimator());
               list_tanks.setAdapter(nozzleRecyclerAdapter);
       }
    private void CountyBinding(Spinner sp_contries){
        sp_contries.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, new String[]{"--Select--","India","Nepal"}));
        sp_contries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i> 0) {
                    country=adapterView.getItemAtPosition(i).toString();
                    Log.d("country",country);
                }  else {
                    country="";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }




}
