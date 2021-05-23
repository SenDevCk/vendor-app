package org.nic.lmd.wenderapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.RenawalActivity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;


/**
 * Created by chandan on 30.09.2020
 */

public class RenewalInstrumentAdapter extends RecyclerView.Adapter<RenewalInstrumentAdapter.MyViewHolder> {

    Activity activity;
    JSONArray jsonArray;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text_name,text_cat,text_vcid,text_qty,text_cur_qtr,text_next_vc_qtr;
        public ToggleButton toggle_request_for_vc,toggle_delete;
        public RadioGroup redio_group_action;
        public MyViewHolder(View view) {
            super(view);
            text_name =  view.findViewById(R.id.text_name);
            text_cat =  view.findViewById(R.id.text_cat);
            text_vcid =  view.findViewById(R.id.text_vcid);
            text_qty =  view.findViewById(R.id.text_qty);
            text_cur_qtr =  view.findViewById(R.id.text_cur_qtr);
            text_next_vc_qtr =  view.findViewById(R.id.text_next_vc_qtr);
            toggle_request_for_vc =  view.findViewById(R.id.toggle_request_for_vc);
            toggle_delete =  view.findViewById(R.id.toggle_delete);
            redio_group_action =  view.findViewById(R.id.redio_group_action);

        }
    }


    public RenewalInstrumentAdapter(Activity activity) {
        this.activity = activity;
        try{
            for (int i=0;i<RenawalActivity.jsonObject.getJSONArray("instruments").length();i++){
                JSONObject jsonObject=RenawalActivity.jsonObject.getJSONArray("instruments").getJSONObject(i);
                if (jsonObject.getString("status").equals("D")||jsonObject.getString("status").equals("A")){
                    jsonObject.accumulate("position",i);
                    jsonArray.put(jsonObject);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public RenewalInstrumentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.renewal_item, parent, false);
        return new RenewalInstrumentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RenewalInstrumentAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try{
            int corr_pos=jsonArray.getJSONObject(position).getInt("position");
            RenawalActivity.jsonObject.getJSONArray("instruments").getJSONObject(corr_pos).accumulate("mCheck", false);
            RenawalActivity.jsonObject.getJSONArray("instruments").getJSONObject(corr_pos).accumulate("isDeleted",false);
            JSONObject jsonObject= RenawalActivity.jsonObject.getJSONArray("instruments").getJSONObject(corr_pos);
            DataBaseHelper db=new DataBaseHelper(activity);
            holder.text_name.setText(""+db.getINSCapByID(jsonObject.getString("capacityId")).getCapacityDesc());
            holder.text_cat.setText(""+db.getCategoryById(jsonObject.getString("categoryId")).getValue());
            holder.text_vcid.setText(""+jsonObject.getString("vcId"));
            holder.text_cur_qtr.setText(""+jsonObject.getString("currentQtr"));
            holder.text_next_vc_qtr.setText(""+jsonObject.getString("nextverificationQtr"));
            holder.redio_group_action.setVisibility(View.GONE);
            if(jsonObject.has("status")&&!jsonObject.getString("status").equals(JSONObject.NULL)) {
                if (jsonObject.getString("status").equals("D")) {
                    holder.toggle_request_for_vc.setClickable(true);
                    holder.toggle_request_for_vc.setChecked(true);
                    holder.toggle_request_for_vc.setClickable(false);
                }
            }
            holder.toggle_request_for_vc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //((RadioButton)holder.redio_group_action.getChildAt(0)).setChecked(isChecked);
                    try {
                        RenawalActivity.jsonObject.getJSONArray("instruments").getJSONObject(corr_pos).put("mCheck", isChecked);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            holder.toggle_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        RenawalActivity.jsonObject.getJSONArray("instruments").getJSONObject(corr_pos).put("isDeleted",isChecked);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
      return jsonArray.length();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}