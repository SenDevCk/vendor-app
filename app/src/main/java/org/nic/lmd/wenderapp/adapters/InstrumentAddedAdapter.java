package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.InstrumetAddedActivity;
import org.nic.lmd.wenderapp.activities.NozzlesActivity;
import org.nic.lmd.wenderapp.entities.Class_ins;
import org.nic.lmd.wenderapp.entities.DenomintionEntity;
import org.nic.lmd.wenderapp.entities.InsCapacityEntity;
import org.nic.lmd.wenderapp.entities.InstrumentEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;

public class InstrumentAddedAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<InstrumentEntity> instrumentEntities;
    LayoutInflater layoutInflater;
    TextView textView;
    public InstrumentAddedAdapter(Activity activity, TextView textView) {
        this.activity = activity;
        this.textView = textView;
        layoutInflater = this.activity.getLayoutInflater();
        instrumentEntities=new DataBaseHelper(activity).getInstrumentAdded();
    }

    @Override
    public int getCount() {
        return instrumentEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return instrumentEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View rootview, ViewGroup viewGroup) {
        rootview = layoutInflater.inflate(R.layout.instrument_item, null, false);
        ViewHolder viewHolder = new ViewHolder();
        InstrumentEntity instrumentEntity=instrumentEntities.get(position);
        DataBaseHelper db=new DataBaseHelper(activity);
        viewHolder.item_firm_type =  rootview.findViewById(R.id.item_firm_type);
        viewHolder.item_firm_type.setText(""+db.getINSProByID(instrumentEntity.getPro_id()).getName());
        viewHolder.text_cat =  rootview.findViewById(R.id.text_cat);
        viewHolder.text_cat.setText(""+db.getINSCatByID(instrumentEntity.getCat_id()).getName());
        viewHolder.tv_cap =  rootview.findViewById(R.id.tv_cap);
        viewHolder.tv_cap.setText(""+db.getINSCapByID(instrumentEntity.getCap_id()).getCapacityDesc());
        viewHolder.tv_quan =  rootview.findViewById(R.id.tv_quan);
        viewHolder.tv_quan.setText(""+instrumentEntity.getQuantity());
        viewHolder.tv_cl =  rootview.findViewById(R.id.tv_cl);
        /* StringBuilder sb=new StringBuilder();
       for (Class_ins class_ins:instrumentEntity.getIns_class()) {
            sb.append("" + class_ins.getName()+"\n");
        }
        if (sb.length()>0)viewHolder.tv_cl.setText(sb.toString());
        */
        viewHolder.tv_cl.setText(""+db.getINSClsByID(instrumentEntity.getIns_class()).getName());
        viewHolder.tv_bra =  rootview.findViewById(R.id.tv_bra);
        viewHolder.tv_bra.setText(""+instrumentEntity.getM_or_brand());
        viewHolder.tv_val =  rootview.findViewById(R.id.tv_val);
        viewHolder.tv_val.setText(""+instrumentEntity.getVal_year());
        viewHolder.tv_min_cap =  rootview.findViewById(R.id.tv_min_cap);
        viewHolder.tv_min_cap.setText(""+instrumentEntity.getCap_min());
        viewHolder.tv_max_cap =  rootview.findViewById(R.id.tv_max_cap);
        viewHolder.tv_max_cap.setText(""+instrumentEntity.getCap_max());
        viewHolder.tv_mod =  rootview.findViewById(R.id.tv_mod);
        viewHolder.tv_mod.setText(""+instrumentEntity.getModel_no());
        viewHolder.tv_ser =  rootview.findViewById(R.id.tv_ser);
        viewHolder.tv_ser.setText(""+instrumentEntity.getSer_no());
        viewHolder.tv_eval =  rootview.findViewById(R.id.tv_eval);
        viewHolder.tv_eval.setText(""+instrumentEntity.getE_val());
        viewHolder.show_Nozzles=rootview.findViewById(R.id.show_Nozzles) ;
        if (new DataBaseHelper(activity).getNozzleCountBySL_ID(instrumentEntity.getId())<=0){
            viewHolder.show_Nozzles.setVisibility(View.GONE);
        }else{
            viewHolder.show_Nozzles.setVisibility(View.VISIBLE);
        }
        viewHolder.show_Nozzles.setOnClickListener(v -> {
            Intent intent=new Intent(activity, NozzlesActivity.class);
            intent.putExtra("id",instrumentEntities.get(position).getId());
            activity.startActivity(intent);
        });
        viewHolder.remove=rootview.findViewById(R.id.remove_ins) ;
        viewHolder.remove.setOnClickListener(view -> {
           long c=new DataBaseHelper(activity).deleteSingleInstrument(instrumentEntities.get(position).getId());
           if (c>0) {
               instrumentEntities = new DataBaseHelper(activity).getInstrumentAdded();
               notifyDataSetChanged();
               textView.setText("" + new DataBaseHelper(activity).getInstrumentAddedCount());
               Utiilties.didTapButton(textView, activity);
           }else{
               Log.e("error","instrument not deleted");
           }
        });
        return rootview;
    }
    class ViewHolder {
        TextView item_firm_type,text_cat,tv_cap,tv_quan,tv_cl,tv_bra,tv_val,tv_min_cap,tv_max_cap;
        TextView tv_mod,tv_ser,tv_eval;
        Button remove,show_Nozzles;
    }
}
