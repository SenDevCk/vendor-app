package org.nic.lmd.wenderapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.entities.VehicleTankDetails;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;

import java.util.ArrayList;

public class TankDetailAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<VehicleTankDetails> tanks;
    LayoutInflater layoutInflater;
    public TankDetailAdapter(Activity activity) {
        this.activity = activity;
        layoutInflater = this.activity.getLayoutInflater();
        tanks=new DataBaseHelper(activity).getTotalTank();
    }

    @Override
    public int getCount() {
        return tanks.size();
    }

    @Override
    public Object getItem(int position) {
        return tanks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(final int position, View rootview, ViewGroup viewGroup) {
        rootview = layoutInflater.inflate(R.layout.tank_details_item, null, false);
        ViewHolder viewHolder = new ViewHolder();
        VehicleTankDetails tank=tanks.get(position);
        DataBaseHelper db=new DataBaseHelper(activity);
        viewHolder.tank_reg_no =  rootview.findViewById(R.id.tank_reg_no);
        viewHolder.tank_reg_no.setText(""+tank.getRegNumber());
        viewHolder.tank_eng_no =  rootview.findViewById(R.id.tank_eng_no);
        viewHolder.tank_eng_no.setText(""+tank.getEngineNumber());
        viewHolder.tank_chechis_no =  rootview.findViewById(R.id.tank_chechis_no);
        viewHolder.tank_chechis_no.setText(""+tank.getChechisNumber());
        viewHolder.tank_own_firm =  rootview.findViewById(R.id.tank_own_firm);
        viewHolder.tank_own_firm.setText(""+tank.getOwnerFirmName());
        viewHolder.tank_country =  rootview.findViewById(R.id.tank_country);
        viewHolder.tank_country.setText(""+tank.getCountry());

        viewHolder.remove_tank=rootview.findViewById(R.id.remove_tank) ;
        viewHolder.remove_tank.setOnClickListener(view -> {
           long c=new DataBaseHelper(activity).deleteTankById(tank);
           if (c>0) {
               tanks.remove(position);
               notifyDataSetChanged();
           }else{
               Toast.makeText(activity, "Not Deleting..", Toast.LENGTH_SHORT).show();
           }
        });
        return rootview;
    }
    class ViewHolder {
        TextView tank_reg_no,tank_eng_no,tank_own_firm,tank_country,tank_chechis_no;
        Button remove_tank;
    }
}
