package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.entities.Class_ins;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;

import java.util.ArrayList;

public class InstrumentClassAdapter extends BaseAdapter {
    ArrayList<Class_ins> ins_classes;
    Activity activity;
    LayoutInflater layoutInflater;
    public InstrumentClassAdapter( Activity activity,ArrayList<Class_ins> ins_classes){
       this.activity=activity;
       this.ins_classes=ins_classes;
       layoutInflater=this.activity.getLayoutInflater();
    }
    @Override
    public int getCount() {
        return ins_classes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    /* @Override
     public int getViewTypeCount() {

         return ins_classes.size();
     }

     @Override
     public int getItemViewType(int position) {
         return ins_classes.get(position).getViewType();
     }*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=layoutInflater.inflate(R.layout.class_item,null,false);
        ItemHolder itemHolder=new ItemHolder();
        itemHolder.class_name=convertView.findViewById(R.id.class_name);
        itemHolder.is_class_sel=convertView.findViewById(R.id.is_class_sel);
        itemHolder.class_name.setText(""+ins_classes.get(position).getName());

        if (ins_classes.get(position).getIs_selected().equals("Y")){
            itemHolder.is_class_sel.setChecked(true);
        }else{
            itemHolder.is_class_sel.setChecked(false);
        }
            itemHolder.is_class_sel.setVisibility(View.VISIBLE);
            itemHolder.is_class_sel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    long c = 0;
                    if (isChecked) {
                        c = new DataBaseHelper(activity).updateInsClassParticular("Y", ins_classes.get(position).getValue());
                    } else {
                        c = new DataBaseHelper(activity).updateInsClassParticular("N", ins_classes.get(position).getValue());
                    }
                    if (c <= 0) {
                        Log.e("log", "Database not updated");
                    }
                }
            });


        return convertView;
    }

    private class ItemHolder{
        ToggleButton is_class_sel;
        TextView class_name;
    }
}
