package org.nic.lmd.wenderapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.entities.DenomintionEntity;
import org.nic.lmd.wenderapp.entities.ProposalTypeEntity;
import org.nic.lmd.wenderapp.entities.VehicleTankDetails;
import org.nic.lmd.wenderapp.entities.WeightCategoriesEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;

import java.util.ArrayList;

public class TankDetailAdapter extends RecyclerView.Adapter<TankDetailAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<VehicleTankDetails> vehicleTankDetails;
    DenomintionEntity denomintionEntity;
    ProposalTypeEntity proposalTypeEntity;
    String valid_year;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tank_reg_no, tank_eng_no, tank_chechis_no, tank_own_firm, tank_country,tank_denom;
        public Button remove;

        public MyViewHolder(View view) {
            super(view);
            tank_denom = view.findViewById(R.id.tank_denom);
            tank_reg_no = view.findViewById(R.id.tank_reg_no);
            tank_eng_no = view.findViewById(R.id.tank_eng_no);
            tank_chechis_no = view.findViewById(R.id.tank_chechis_no);
            tank_own_firm = view.findViewById(R.id.tank_own_firm);
            tank_country = view.findViewById(R.id.tank_country);
            remove = view.findViewById(R.id.remove_tank);
        }
    }


    public TankDetailAdapter(Activity activity,String denomid,String valid_year) {
        this.activity = activity;
        vehicleTankDetails=new DataBaseHelper(this.activity).getTotalTank(denomid);
        denomintionEntity=new DataBaseHelper(this.activity).getWeightDenominationByID(denomid);
        WeightCategoriesEntity weightCategoriesEntity = new DataBaseHelper(activity).getCategoryById(denomintionEntity.getCategoryId().trim());
        this.proposalTypeEntity= new DataBaseHelper(activity).getProByID(weightCategoriesEntity.getPro_id().trim());
        this.valid_year=valid_year;
    }

    @Override
    public TankDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tank_details_item, parent, false);
        return new TankDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TankDetailAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final VehicleTankDetails vehicleTankDetail=vehicleTankDetails.get(position);
        holder.tank_denom.setText(denomintionEntity.getDenominationDesc());
        holder.tank_reg_no.setText("" + vehicleTankDetail.getRegNumber());
        holder.tank_eng_no.setText("" + vehicleTankDetail.getEngineNumber());
        holder.tank_chechis_no.setText("" + vehicleTankDetail.getChechisNumber());
        holder.tank_own_firm.setText("" + vehicleTankDetail.getOwnerFirmName());
        holder.tank_country.setText("" + vehicleTankDetail.getCountry());
        holder.remove.setOnClickListener(v -> {
            new DataBaseHelper(activity).deleteTankById(vehicleTankDetail);
            vehicleTankDetails.remove(position);
            new DataBaseHelper(activity).updateDenomination("", "", denomintionEntity.getValue(), "Y", String.valueOf(vehicleTankDetails.size()), 0, "0", valid_year, proposalTypeEntity.getId().trim(), false);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return vehicleTankDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

