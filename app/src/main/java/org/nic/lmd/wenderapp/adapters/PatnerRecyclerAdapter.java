package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.entities.PatnerEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;

import java.util.ArrayList;


/**
 * Created by chandan on 30.09.2020
 */

public class PatnerRecyclerAdapter extends RecyclerView.Adapter<PatnerRecyclerAdapter.MyViewHolder> {

    private ArrayList<PatnerEntity> patnerEntities;
    Activity activity;
    private int lastPosition = -1;
    int count_for_ins;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_firm_type,text_lc_date,tv_reg_no,tv_desig,tv_name,tv_fname;
        public TextView tv_adhar,tv_add,landmark_tv,tv_city,tv_dis,tv_bol,tv_pin,tv_mb,tv_land,tv_email;
        public Button remove;
        public MyViewHolder(View view) {
            super(view);
            item_firm_type =  view.findViewById(R.id.item_firm_type);
            text_lc_date =  view.findViewById(R.id.text_lc_date);
            tv_reg_no =  view.findViewById(R.id.tv_reg_no);
            tv_desig =  view.findViewById(R.id.tv_desig);
            tv_name =  view.findViewById(R.id.tv_name);
            tv_fname =  view.findViewById(R.id.tv_fname);
            tv_adhar =  view.findViewById(R.id.tv_adhar);
            tv_add =  view.findViewById(R.id.tv_add);
            landmark_tv =  view.findViewById(R.id.landmark_tv);
            tv_city =  view.findViewById(R.id.tv_city);
            tv_dis =  view.findViewById(R.id.tv_dis);
            tv_bol =  view.findViewById(R.id.tv_bol);
            tv_pin =  view.findViewById(R.id.tv_pin);
            tv_mb =  view.findViewById(R.id.tv_mb);
            tv_land =  view.findViewById(R.id.tv_land);
            tv_email =  view.findViewById(R.id.tv_email);
            remove =  view.findViewById(R.id.remove);
            remove.setVisibility(View.GONE);
        }
    }


    public PatnerRecyclerAdapter(ArrayList<PatnerEntity> patnerEntities, Activity activity) {
        this.patnerEntities = patnerEntities;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partner_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.item_firm_type.setText(new DataBaseHelper(activity).getFirmTypeByID(""+patnerEntities.get(position).getFirm_type()));
        holder.text_lc_date.setText(""+patnerEntities.get(position).getDate_of_com());
        holder.tv_reg_no.setText(""+patnerEntities.get(position).getIs_nom_under49());
        holder.tv_desig.setText(""+new DataBaseHelper(activity).getDesignationName(patnerEntities.get(position).getDesignation_id()));
        holder.tv_name.setText(""+patnerEntities.get(position).getName());
        holder.tv_fname.setText(""+patnerEntities.get(position).getFather_name());
        holder.tv_adhar.setText(""+patnerEntities.get(position).getAdhar_vid());
        holder.tv_email.setText((patnerEntities.get(position).getEmail().equals("null"))?"N/A":patnerEntities.get(position).getEmail());
        holder.tv_land.setText((patnerEntities.get(position).getLandline().equals("null"))?"N/A":patnerEntities.get(position).getLandline());
        holder.tv_mb.setText(""+patnerEntities.get(position).getMobile());
        holder.tv_pin.setText(""+patnerEntities.get(position).getPinCode());
        holder.tv_bol.setText(""+(new DataBaseHelper(activity).getBlockByID(patnerEntities.get(position).getBlock())).getName());
        holder.tv_dis.setText((new DataBaseHelper(activity).getDistrictByID(patnerEntities.get(position).getDistrict())).getName());
        holder.tv_city.setText(""+patnerEntities.get(position).getCity());
        holder.landmark_tv.setText(""+patnerEntities.get(position).getLandmark());
        holder.tv_add.setText(""+patnerEntities.get(position).getAdd1()+" "+patnerEntities.get(position).getAdd2());
       setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return patnerEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}