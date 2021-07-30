package org.nic.lmd.wenderapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.TankDetailsActivity;
import org.nic.lmd.wenderapp.activities.WeightDenominationActivity;
import org.nic.lmd.wenderapp.entities.DenomintionEntity;
import org.nic.lmd.wenderapp.entities.ProposalTypeEntity;
import org.nic.lmd.wenderapp.entities.WeightCategoriesEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;

public class WeightDenominationAdapter extends BaseAdapter {
    Activity activity;
    String cot_id, val_year;
    ArrayList<DenomintionEntity> denomintionEntities;
    LayoutInflater layoutInflater;
    TextView textView;

    public WeightDenominationAdapter(Activity activity, String cot_id, String val_year, TextView textView) {
        this.activity = activity;
        this.cot_id = cot_id;
        this.val_year = val_year;
        this.textView = textView;
        layoutInflater = this.activity.getLayoutInflater();
        if (this.cot_id.equals("0")) denomintionEntities = new DataBaseHelper(activity).getWeightDenomination(this.cot_id, "0");
        else denomintionEntities = new DataBaseHelper(activity).getWeightDenomination(this.cot_id, "1");

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
        WeightCategoriesEntity weightCategoriesEntity = new DataBaseHelper(activity).getCategoryById(denomintionEntities.get(position).getCategoryId().trim());
        final ProposalTypeEntity proposalTypeEntity = new DataBaseHelper(activity).getProByID(weightCategoriesEntity.getPro_id().trim());

        viewHolder.cat_type.setText(proposalTypeEntity.getValue());
        viewHolder.cat_name.setText("" + weightCategoriesEntity.getValue());
        viewHolder.tv_inc.setText("" + denomintionEntities.get(position).getQuantity());
        viewHolder.text_item_name.setText("" + denomintionEntities.get(position).getDenominationDesc());
        if (this.val_year.equals("0")) {
            this.val_year = denomintionEntities.get(position).getVal_year();
        }
        viewHolder.text_val.setText("Validity : " + val_year + " year");
        if (weightCategoriesEntity.getId().equals("19")) {
            viewHolder.text_qty.setText("Capacity of Each Compartment in Litres*");
            viewHolder.text_qty.setTextSize(7);
            viewHolder.text_qty.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
            viewHolder.tv_inc.setOnClickListener(v -> {
                activity.startActivity(new Intent(activity, TankDetailsActivity.class));
            });
        }else{
            viewHolder.img_inc.setOnClickListener(view -> {
                int incre = Integer.parseInt(viewHolder.tv_inc.getText().toString().trim());
                incre = incre + 1;
                viewHolder.tv_inc.setText("" + incre);
                if (Integer.parseInt(viewHolder.tv_inc.getText().toString()) > 0) {
                    new DataBaseHelper(activity).updateDenomination("","",denomintionEntities.get(position).getValue(), "Y", viewHolder.tv_inc.getText().toString(), 0, "0", val_year, proposalTypeEntity.getId().trim(), false);
                }
                textView.setText("" + new DataBaseHelper(activity).getAddedWeightCount());
                Utiilties.didTapButton(textView, activity);

            });

            viewHolder.img_dec.setOnClickListener(view -> {
                int decre = Integer.parseInt(viewHolder.tv_inc.getText().toString().trim());
                if (decre > 0) {
                    if (Integer.parseInt(denomintionEntities.get(position).getIs_set())>0 && decre <= (Integer.parseInt(denomintionEntities.get(position).getSet_m())*Integer.parseInt(denomintionEntities.get(position).getIs_set()))) {
                        Toast.makeText(activity, "It is Set Amount", Toast.LENGTH_SHORT).show();
                    } else {
                        decre = decre - 1;
                        if (decre == 0) {
                            viewHolder.tv_inc.setText("" + decre);
                            new DataBaseHelper(activity).updateDenomination("","",denomintionEntities.get(position).getValue(), "N", viewHolder.tv_inc.getText().toString(), 0, "0", val_year, proposalTypeEntity.getId().trim(),false);
                        } else {
                            viewHolder.tv_inc.setText("" + decre);
                            new DataBaseHelper(activity).updateDenomination("","",denomintionEntities.get(position).getValue(), "Y", viewHolder.tv_inc.getText().toString(), 0, "0", val_year, proposalTypeEntity.getId().trim(), false);
                        }
                        textView.setText("" + new DataBaseHelper(activity).getAddedWeightCount());
                        Utiilties.didTapButton(textView, activity);
                    }
                }
            });

        }

        viewHolder.tog_denomination =  rootview.findViewById(R.id.tog_denomination);
        if (denomintionEntities.get(position).getChecked().equals("Y")) {
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


}
