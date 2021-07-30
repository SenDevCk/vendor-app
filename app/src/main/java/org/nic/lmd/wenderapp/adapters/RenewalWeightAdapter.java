package org.nic.lmd.wenderapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class  RenewalWeightAdapter extends RecyclerView.Adapter<RenewalWeightAdapter.MyViewHolder> {

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


    public RenewalWeightAdapter( Activity activity) {
        this.activity = activity;
        try{
            for (int i=0;i<RenawalActivity.jsonObject.getJSONArray("weights").length();i++){
                JSONObject jsonObject=RenawalActivity.jsonObject.getJSONArray("weights").getJSONObject(i);
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.renewal_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try{
            final int corr_pos=jsonArray.getJSONObject(position).getInt("position");
            RenawalActivity.jsonObject.getJSONArray("weights").getJSONObject(corr_pos).accumulate("mCheck", false);
            RenawalActivity.jsonObject.getJSONArray("weights").getJSONObject(corr_pos).accumulate("isDeleted",false);
            final JSONObject jsonObject= RenawalActivity.jsonObject.getJSONArray("weights").getJSONObject(corr_pos);
            DataBaseHelper db=new DataBaseHelper(activity);
            holder.text_name.setText(""+db.getWeightDenominationByID(jsonObject.getString("denomination")));
            holder.text_cat.setText(""+db.getCategoryById(jsonObject.getString("categoryId")).getValue());
            holder.text_vcid.setText(""+jsonObject.getString("vcId"));
            holder.text_cur_qtr.setText(""+jsonObject.getString("currentQtr"));
            holder.text_next_vc_qtr.setText(""+(jsonObject.has("nextVcDate")?jsonObject.getString("nextVcDate"):"N/A"));
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
                    try {
                        RenawalActivity.jsonObject.getJSONArray("weights").getJSONObject(corr_pos).put("mCheck", isChecked);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            holder.toggle_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        RenawalActivity.jsonObject.getJSONArray("weights").getJSONObject(corr_pos).put("isDeleted",isChecked);
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