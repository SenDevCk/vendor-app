package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.entities.Nozzle;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;

import java.util.ArrayList;

public class NozzleAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Nozzle> nozzles;
    LayoutInflater layoutInflater;
    public NozzleAdapter(Activity activity) {
        this.activity = activity;
        layoutInflater = this.activity.getLayoutInflater();
        nozzles=new DataBaseHelper(activity).getAddedNozzle(activity.getIntent().getStringExtra("id"));
    }

    @Override
    public int getCount() {
        return nozzles.size();
    }

    @Override
    public Object getItem(int position) {
        return nozzles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View rootview, ViewGroup viewGroup) {
        rootview = layoutInflater.inflate(R.layout.nozzle_item, null, false);
        ViewHolder viewHolder = new ViewHolder();
        Nozzle nozzle=nozzles.get(position);
        DataBaseHelper db=new DataBaseHelper(activity);
        viewHolder.item_noz_num =  rootview.findViewById(R.id.item_noz_num);
        viewHolder.item_noz_num.setText(""+nozzle.getNozzle_num());
        viewHolder.text_product =  rootview.findViewById(R.id.text_product);
        viewHolder.text_product.setText(""+db.getProductByID(nozzle.getProduct_id()).getName());
        viewHolder.tv_calno =  rootview.findViewById(R.id.tv_calno);
        viewHolder.tv_calno.setText(""+nozzle.getCal_num());
        viewHolder.tv_kfact =  rootview.findViewById(R.id.tv_kfact);
        viewHolder.tv_kfact.setText(""+nozzle.getK_factor());
        viewHolder.tv_tot_val =  rootview.findViewById(R.id.tv_tot_val);
        viewHolder.tv_tot_val.setText(""+nozzle.getTot_value());

        viewHolder.remove=rootview.findViewById(R.id.remove) ;
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               long c=new DataBaseHelper(activity).deleteNozzleById(nozzle);
               if (c>0) {
                   nozzles.remove(position);
                   notifyDataSetChanged();
               }else{
                   Toast.makeText(activity, "Not Deleting..", Toast.LENGTH_SHORT).show();
               }
            }
        });
        return rootview;
    }
    class ViewHolder {
        TextView item_noz_num,text_product,tv_calno,tv_kfact,tv_tot_val;
        Button remove;
    }
}
