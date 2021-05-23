package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.entities.InsCapacityEntity;
import org.nic.lmd.wenderapp.entities.PatnerEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;

import java.util.ArrayList;

public class PatnerAdapter extends BaseAdapter {
    Activity activity;
    String cot_id;
    ArrayList<PatnerEntity> patnerEntities;
    LayoutInflater layoutInflater;

    public PatnerAdapter(Activity activity) {
        this.activity = activity;
        layoutInflater = this.activity.getLayoutInflater();
        patnerEntities = new DataBaseHelper(activity).getAllPatners();

    }

    @Override
    public int getCount() {
        return patnerEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return patnerEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View rootview, ViewGroup viewGroup) {
        rootview = layoutInflater.inflate(R.layout.partner_item, null, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.item_firm_type =  rootview.findViewById(R.id.item_firm_type);
        viewHolder.item_firm_type.setText(new DataBaseHelper(activity).getFirmType(""+patnerEntities.get(position).getFirm_type()));
        viewHolder.text_lc_date =  rootview.findViewById(R.id.text_lc_date);
        viewHolder.text_lc_date.setText(""+patnerEntities.get(position).getDate_of_com());
        viewHolder.tv_reg_no =  rootview.findViewById(R.id.tv_reg_no);
        viewHolder.tv_reg_no.setText(""+patnerEntities.get(position).getIs_nom_under49());
        viewHolder.tv_desig =  rootview.findViewById(R.id.tv_desig);
        viewHolder.tv_desig.setText(""+new DataBaseHelper(activity).getDesignationName(patnerEntities.get(position).getDesignation_id()));
        viewHolder.tv_name =  rootview.findViewById(R.id.tv_name);
        viewHolder.tv_name.setText(""+patnerEntities.get(position).getName());
        viewHolder.tv_fname =  rootview.findViewById(R.id.tv_fname);
        viewHolder.tv_fname.setText(""+patnerEntities.get(position).getFather_name());
        viewHolder.tv_adhar =  rootview.findViewById(R.id.tv_adhar);
        viewHolder.tv_adhar.setText(""+patnerEntities.get(position).getAdhar_vid());
        viewHolder.tv_add =  rootview.findViewById(R.id.tv_add);
        viewHolder.tv_add.setText(""+patnerEntities.get(position).getAdd1()+" "+patnerEntities.get(position).getAdd2());
        viewHolder.landmark_tv =  rootview.findViewById(R.id.landmark_tv);
        viewHolder.landmark_tv.setText(""+patnerEntities.get(position).getLandmark());
        viewHolder.tv_city =  rootview.findViewById(R.id.tv_city);
        viewHolder.tv_city.setText(""+patnerEntities.get(position).getCity());
        viewHolder.tv_dis =  rootview.findViewById(R.id.tv_dis);
        viewHolder.tv_dis.setText((new DataBaseHelper(activity).getDistrictByID(patnerEntities.get(position).getDistrict())).getName());
        viewHolder.tv_bol =  rootview.findViewById(R.id.tv_bol);
        viewHolder.tv_bol.setText(""+(new DataBaseHelper(activity).getBlockByID(patnerEntities.get(position).getBlock())).getName());
        viewHolder.tv_pin =  rootview.findViewById(R.id.tv_pin);
        viewHolder.tv_pin.setText(""+patnerEntities.get(position).getPinCode());
        viewHolder.tv_mb =  rootview.findViewById(R.id.tv_mb);
        viewHolder.tv_mb.setText(""+patnerEntities.get(position).getMobile());
        viewHolder.tv_land =  rootview.findViewById(R.id.tv_land);
        viewHolder.tv_land.setText(""+patnerEntities.get(position).getLandline());
        viewHolder.tv_email =  rootview.findViewById(R.id.tv_email);
        viewHolder.tv_email.setText(""+patnerEntities.get(position).getEmail());
        viewHolder.remove =  rootview.findViewById(R.id.remove);
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long c=new DataBaseHelper(activity).deletePartner(patnerEntities.get(position).getId().trim());
                if (c>0){
                    patnerEntities=new DataBaseHelper(activity).getAllPatners();
                    notifyDataSetChanged();
                }
            }
        });
        return rootview;
    }
    class ViewHolder {
        TextView item_firm_type,text_lc_date,tv_reg_no,tv_desig,tv_name,tv_fname;
        TextView tv_adhar,tv_add,landmark_tv,tv_city,tv_dis,tv_bol,tv_pin,tv_mb,tv_land,tv_email;
        Button remove;
    }
}
