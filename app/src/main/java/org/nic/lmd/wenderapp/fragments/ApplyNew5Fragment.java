package org.nic.lmd.wenderapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.ApplicationBillingDetailsActivity;
import org.nic.lmd.wenderapp.activities.ApplyNewActivity;
import org.nic.lmd.wenderapp.activities.InstrumetAddedActivity;
import org.nic.lmd.wenderapp.activities.SelectClassActivity;
import org.nic.lmd.wenderapp.activities.WeightDenominationActivity;
import org.nic.lmd.wenderapp.adapters.InstrumentClassAdapter;
import org.nic.lmd.wenderapp.adapters.PatnerRecyclerAdapter;
import org.nic.lmd.wenderapp.asynctask.UploadTradorService;
import org.nic.lmd.wenderapp.entities.Class_ins;
import org.nic.lmd.wenderapp.entities.InsCapacityEntity;
import org.nic.lmd.wenderapp.entities.InsCategoryEntity;
import org.nic.lmd.wenderapp.entities.InsProduct;
import org.nic.lmd.wenderapp.entities.InsProposalEntity;
import org.nic.lmd.wenderapp.entities.InstrumentEntity;
import org.nic.lmd.wenderapp.entities.Nozzle;
import org.nic.lmd.wenderapp.entities.PatnerEntity;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.utilities.Utiilties;

import java.util.ArrayList;

public class ApplyNew5Fragment extends Fragment {

    Spinner sp_per_type_f5, sp_cat_f5, sp_cap_f5, sp_valid, sp_class;
    private String cat_id = "0", pro_id = "0", cap_id = "0", validity_id = "0", class_id = "0", product_id = "0";
    TextView text_all_ins, text_nozz_count;
    Button button_add_ins, save_button;
    EditText edit_quantity, edit_gst, edit_cap_max, edit_cap_min, edit_mod_no, edit_ser_no, edit_eval;
    ScrollView scroll_od_f5;
    private View view_name;
    private String message = "";
    TextInputLayout text_ip_qt;

    ArrayList<Nozzle> nozzles = new ArrayList<>();

    public ApplyNew5Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_apply_new5, container, false);
        initUI(rootView);
        sp_per_type_f5 = rootView.findViewById(R.id.sp_per_type_f5);
        sp_cat_f5 = rootView.findViewById(R.id.sp_cat_f5);
        sp_cap_f5 = rootView.findViewById(R.id.sp_cap_f5);
        sp_valid = rootView.findViewById(R.id.sp_valid);
        sp_class = rootView.findViewById(R.id.sp_class);
        text_nozz_count = rootView.findViewById(R.id.text_nozz_count);
        text_nozz_count.setVisibility(View.GONE);
        /*text_nozz_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit_quantity.getText().toString().trim().equals("")) {
                    if (!cap_id.equals("0")) {
                        if ((cat_id.equals("16") && cap_id.equals("219")) || (cat_id.equals("19") && cap_id.equals("225")) || (cat_id.equals("22") && cap_id.equals("230"))) {
                            if (nozzles.size() > 0) {
                                AlertDialogForNozzleEntry();
                            }
                        }
                    }else {
                        edit_quantity.setText("");
                        Toast.makeText(getActivity(), "Please Select Capacity first", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
        initialiseSpinnerPro();
        categorySpinnerBinding();
        capacitySpinnerBinding();
        validityBinding();
        classSpinnerBinding();
         /* sp_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(),
                        SelectClassActivity.class), 11);
            }
        });*/
        return rootView;
    }

    private void initUI(View rootview) {
        text_ip_qt = rootview.findViewById(R.id.text_ip_qt);
        text_all_ins = rootview.findViewById(R.id.text_all_ins);
        edit_quantity = rootview.findViewById(R.id.edit_quantity);
        edit_gst = rootview.findViewById(R.id.edit_gst);
        edit_cap_max = rootview.findViewById(R.id.edit_cap_max);
        edit_cap_min = rootview.findViewById(R.id.edit_cap_min);
        edit_mod_no = rootview.findViewById(R.id.edit_mod_no);
        edit_ser_no = rootview.findViewById(R.id.edit_ser_no);
        edit_eval = rootview.findViewById(R.id.edit_eval);
      /*  edit_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if ((cat_id.equals("16") && cap_id.equals("219")) || (cat_id.equals("19") && cap_id.equals("225")) || (cat_id.equals("22") && cap_id.equals("230"))) {
                        if (nozzles.size() < Integer.parseInt(s.toString())) {
                            AlertDialogForNozzleEntry();
                        }
                    }
                }
            }
        });*/

        scroll_od_f5 = rootview.findViewById(R.id.scroll_od_f5);
        button_add_ins = rootview.findViewById(R.id.button_add_ins);
        save_button = rootview.findViewById(R.id.button_save_cont);


        button_add_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    InstrumentEntity instrumentEntity = new InstrumentEntity();
                    instrumentEntity.setPro_id(pro_id.trim());
                    instrumentEntity.setCat_id(cat_id.trim());
                    instrumentEntity.setCap_id(cap_id.trim());
                    instrumentEntity.setQuantity(edit_quantity.getText().toString().trim());
                    instrumentEntity.setIns_class(class_id);
                    instrumentEntity.setM_or_brand(edit_gst.getText().toString().trim());
                    instrumentEntity.setCap_min(edit_cap_min.getText().toString().trim());
                    instrumentEntity.setCap_max(edit_cap_max.getText().toString().trim());
                    instrumentEntity.setModel_no(edit_mod_no.getText().toString().trim());
                    instrumentEntity.setSer_no(edit_ser_no.getText().toString().trim());
                    instrumentEntity.setE_val(edit_eval.getText().toString().trim());
                    instrumentEntity.setVal_year(validity_id.trim());
                    instrumentEntity.setNozzles(nozzles);
                    long ins = new DataBaseHelper(getActivity()).addInstrument(instrumentEntity);
                    if (ins > 0) {
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        nozzles.clear();
                    } else {
                        Toast.makeText(getActivity(), "Not Saved", Toast.LENGTH_SHORT).show();
                    }
                    showData();
                } else {
                    Utiilties.scrollToView(scroll_od_f5, view_name);
                    Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper db = new DataBaseHelper(getActivity());
                if ((db.getInstrumentAddedCount() + db.getAddedWeightCount()) < 1) {
                    Toast.makeText(getActivity(), "Please add weight or Instrument Details !", Toast.LENGTH_SHORT).show();
                } else if (!Utiilties.isOnline(getActivity())) {
                    Toast.makeText(getActivity(), "Internet not avalable !", Toast.LENGTH_SHORT).show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        new UploadTradorService(getActivity(), "", getActivity().getIntent()).execute();
                    } else {
                        Log.d("error", "Minimum SDK Version CUPCAKE");
                        Toast.makeText(getActivity(), "Minimum SDK Version CUPCAKE", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validate() {
        boolean validated = false;
        if (pro_id.equals("0")) {
            message = "Please Select Proposal";
            view_name = sp_per_type_f5;
        } else if (cat_id.equals("0")) {
            message = "Please Select Category";
            view_name = sp_cat_f5;
        } else if (cap_id.equals("0")) {
            message = "Please Select Capacity";
            view_name = sp_cap_f5;
        } else if (edit_quantity.getText().toString().trim().equals("") || edit_quantity.getText().toString().trim().equals("0")) {
            edit_quantity.setError("Enter Quantity");
            view_name = edit_quantity;
        }
        /*else if (((cat_id.equals("16") && cap_id.equals("219")) || (cat_id.equals("19") && cap_id.equals("225")) || (cat_id.equals("22") && cap_id.equals("230")))&&(Integer.parseInt(edit_quantity.getText().toString().trim())!=nozzles.size())) {
            edit_quantity.setError("Quantity and number of Nozzle Added should be equal");
            message="Quantity and number of Nozzle Added should be equal";
            view_name = edit_quantity;
        }*/
        else if (class_id.equals("0")) {
            message = "Please Select Class";
            view_name = sp_class;
        } else if (edit_gst.getText().toString().trim().equals("")) {
            edit_gst.setError("Enter manufacturer/Brand");
            view_name = edit_gst;
        }else {
            validated = true;
        }
        return validated;
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    private void showData() {
        long count_ins = new DataBaseHelper(getActivity()).getInstrumentAddedCount();
        if (count_ins > 0)
            text_all_ins.setText("" + count_ins);
        else text_all_ins.setText("0");
        Utiilties.didTapButton(text_all_ins, getActivity());
        text_all_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(text_all_ins.getText().toString().trim()) > 0) {
                    Intent intent = new Intent(getActivity(), InstrumetAddedActivity.class);
                    startActivity(intent);
                }
            }
        });
        //text_nozz_count.setText("" + nozzles.size());
        //Utiilties.didTapButton(text_nozz_count, getActivity());
       /* ArrayList<Class_ins> classInsArrayList = new DataBaseHelper(getActivity()).getInsClass(1);
        sp_class.setText("--Select Class--");
        StringBuilder sb=new StringBuilder();
        for (Class_ins class_ins : classInsArrayList) {
            sb.append("" + class_ins.getName() + ", ");
        }
        if (sb.length()>0)sp_class.setText(""+sb.toString());
        else   sp_class.setText("--Select Class--");*/

    }

    private void initialiseSpinnerPro() {
        final ArrayList<InsProposalEntity> proposalTypeEntities = new DataBaseHelper(getActivity()).getInsProposal();
        ArrayList<String> list_string_pro = new ArrayList<>();
        list_string_pro.add("-- SELECT TYPE OF PROPOSAL --");
        if (proposalTypeEntities.size() > 0) {
            for (InsProposalEntity proposalTypeEntity : proposalTypeEntities) {
                list_string_pro.add(proposalTypeEntity.getName());
            }
        }
        sp_per_type_f5.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_pro));
        sp_per_type_f5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pro_id = (i <= 0) ? "0" : proposalTypeEntities.get(i - 1).getValue();
                categorySpinnerBinding();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void categorySpinnerBinding() {
        final ArrayList<InsCategoryEntity> insCategoryEntities = new DataBaseHelper(getActivity()).getInsCategory(pro_id);
        ArrayList<String> list_string_cat = new ArrayList<>();
        list_string_cat.add("-- SELECT TYPE OF CATEGORY --");
        if (insCategoryEntities.size() > 0) {
            for (InsCategoryEntity insCategoryEntity : insCategoryEntities) {
                list_string_cat.add(insCategoryEntity.getName());
            }
        }
        sp_cat_f5.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_cat));
        sp_cat_f5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                edit_quantity.setText("");
                if (i <= 0) {
                    cat_id = "0";
                } else {
                    sp_valid.setEnabled(true);
                    cat_id = insCategoryEntities.get(i - 1).getValue().trim();
                    if (cat_id.equals("14")) {
                        text_ip_qt.setHint("Capacity of Tank in Litres *");
                    } else {
                        text_ip_qt.setHint("Quantity *");
                    }
                    sp_valid.setSelection(((ArrayAdapter<String>) sp_valid.getAdapter()).getPosition(insCategoryEntities.get(i - 1).getValidity()));
                    sp_valid.setEnabled(false);
                    capacitySpinnerBinding();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void capacitySpinnerBinding() {
        final ArrayList<InsCapacityEntity> insCapacityEntities = new DataBaseHelper(getActivity()).getInsCapacity(cat_id, "1");
        ArrayList<String> list_string_cap = new ArrayList<>();
        list_string_cap.add("-- SELECT CAPACITY --");
        if (insCapacityEntities.size() > 0) {
            for (InsCapacityEntity insCapacityEntity : insCapacityEntities) {
                list_string_cap.add(insCapacityEntity.getCapacityDesc());
            }
        }
        sp_cap_f5.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_cap));
        sp_cap_f5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                edit_quantity.setText("");
                nozzles.clear();
                showData();
                if (i <= 0) {
                    cap_id = "0";
                    text_ip_qt.setHint("Quantity *");
                } else {
                    cap_id = insCapacityEntities.get(i - 1).getCapacityId().trim();
                    if (cat_id.equals("21") && cap_id.equals("229")) {
                        text_ip_qt.setHint("Capacity of Tank in Litres *");
                    } else if ((cat_id.equals("16") && cap_id.equals("219")) || (cat_id.equals("19") && cap_id.equals("225")) || (cat_id.equals("22") && cap_id.equals("230"))) {
                        text_ip_qt.setHint("No.s of Totlizers *");
                    } else {
                        text_ip_qt.setHint("Quantity *");
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void validityBinding() {
        final ArrayList<String> list_string_denomi = new ArrayList<>();
        list_string_denomi.add("-- SELECT Validity (years)--");
        list_string_denomi.add("1");
        list_string_denomi.add("2");
        list_string_denomi.add("3");
        list_string_denomi.add("4");
        list_string_denomi.add("5");
        sp_valid.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_denomi));
        sp_valid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    validity_id = "0";
                } else {
                    validity_id = list_string_denomi.get(i).trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void classSpinnerBinding() {
        final ArrayList<Class_ins> class_ins = new DataBaseHelper(getActivity()).getInsClass(0);
        ArrayList<String> list_string_cat = new ArrayList<>();
        list_string_cat.add("-- SELECT CLASS --");
        if (class_ins.size() > 0) {
            for (Class_ins classIns : class_ins) {
                list_string_cat.add(classIns.getName());
            }
        }
        sp_class.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_cat));
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    class_id = "0";
                } else {
                    class_id = class_ins.get(i - 1).getValue().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*
    private void classSpinnerBinding() {
        final ArrayList<Class_ins> class_ins = new DataBaseHelper(getActivity()).getInsClass(0);
        ArrayList<Class_ins> list_string_cat = new ArrayList<>();
        Class_ins insclass=new Class_ins();
        insclass.setName("-- SELECT CLASS --");
        insclass.setValue("0");
        insclass.setIs_selected("N");
        list_string_cat.add(insclass);
        if (class_ins.size() > 0) {
            for (Class_ins insCapacityEntity : class_ins) {
                list_string_cat.add(insCapacityEntity);
            }
        }
        sp_class.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_denomi));
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 0) {
                    class_id = "0";
                } else {
                    class_id = class_ins.get(i - 1).getValue().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    */

    private void loadProductSpinner(Spinner sp_product) {
        final ArrayList<InsProduct> insProducts = new DataBaseHelper(getActivity()).getInsProduct();
        ArrayList<String> list_string_pro = new ArrayList<>();
        list_string_pro.add("-- SELECT PRODUCT --");
        if (insProducts.size() > 0) {
            for (InsProduct insProduct : insProducts) {
                list_string_pro.add(insProduct.getName());
            }
        }
        sp_product.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list_string_pro));
        sp_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                product_id = (i <= 0) ? "0" : insProducts.get(i - 1).getValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

   /* public void AlertDialogForNozzleEntry() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.nozzle_entry_dialog);
        dialog.setTitle("Nozzle Entry");
        EditText edit_noz_num = dialog.findViewById(R.id.edit_noz_num);
        EditText edit_cal_num = dialog.findViewById(R.id.edit_cal_num);
        EditText edit_kfac = dialog.findViewById(R.id.edit_kfac);
        EditText edit_tot_value = dialog.findViewById(R.id.edit_tot_value);
        Button add_nozzle = dialog.findViewById(R.id.add_nozzle);
        RelativeLayout end_dialog = dialog.findViewById(R.id.end_dialog);
        Spinner sp_product = dialog.findViewById(R.id.sp_product);
        final RecyclerView list_nozzle = dialog.findViewById(R.id.list_nozzle);
        loadProductSpinner(sp_product);
        populateProductRecycler(list_nozzle);
        // if button is clicked, close the custom dialog
        end_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        add_nozzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_noz_num.getText().toString().trim().equals("")) {
                    edit_cal_num.setError("Enter Nozzle Number");
                } else if (product_id.equals("0")) {
                    Toast.makeText(getActivity(), "Select Product !", Toast.LENGTH_SHORT).show();
                } else if (edit_cal_num.getText().toString().trim().equals("")) {
                    edit_cal_num.setError("Enter CAL Number");
                } else if (edit_kfac.getText().toString().trim().equals("")) {
                    edit_kfac.setError("Enter K-Factor");
                } else if (edit_tot_value.getText().toString().trim().equals("")) {
                    edit_tot_value.setError("Enter Totalizer Value");
                } else {
                    if (nozzles.size() < Integer.parseInt(edit_quantity.getText().toString())) {
                        Nozzle nozzle = new Nozzle();
                        nozzle.setNozzle_num(edit_noz_num.getText().toString().trim());
                        nozzle.setProduct_id(product_id);
                        nozzle.setCal_num(edit_cal_num.getText().toString().trim());
                        nozzle.setK_factor(edit_kfac.getText().toString().trim());
                        nozzle.setTot_value(edit_tot_value.getText().toString().trim());
                        nozzles.add(nozzle);
                        text_nozz_count.setText("" + nozzles.size());
                        edit_noz_num.setText("");
                        edit_cal_num.setText("");
                        edit_kfac.setText("");
                        edit_tot_value.setText("");
                        sp_product.setSelection(0);
                       populateProductRecycler(list_nozzle);
                    } else {
                        dialogAlert(getActivity(), "Message", "Increase number of totalizer value,you can not enter more than totalizer value");
                    }

                }
            }
        });
        dialog.show();
    }*/

    /*private void populateProductRecycler(RecyclerView list_nozzle) {
        list_nozzle.invalidate();
        NozzleRecyclerAdapter nozzleRecyclerAdapter = new NozzleRecyclerAdapter(getActivity());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        list_nozzle.setLayoutManager(mLayoutManager2);
        list_nozzle.setItemAnimator(new DefaultItemAnimator());
        list_nozzle.setAdapter(nozzleRecyclerAdapter);
    }*/

    private void dialogAlert(FragmentActivity activity, String titel, String message) {
        new AlertDialog.Builder(activity)
                .setTitle(titel)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /*private class NozzleRecyclerAdapter extends RecyclerView.Adapter<NozzleRecyclerAdapter.MyViewHolder> {

        Activity activity;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView item_noz_num, text_product, tv_calno, tv_kfact, tv_tot_val;
            public Button remove;

            public MyViewHolder(View view) {
                super(view);
                item_noz_num = view.findViewById(R.id.item_noz_num);
                text_product = view.findViewById(R.id.text_product);
                tv_calno = view.findViewById(R.id.tv_calno);
                tv_kfact = view.findViewById(R.id.tv_kfact);
                tv_tot_val = view.findViewById(R.id.tv_tot_val);
                remove = view.findViewById(R.id.remove);
            }
        }


        public NozzleRecyclerAdapter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public NozzleRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nozzle_item, parent, false);
            return new NozzleRecyclerAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NozzleRecyclerAdapter.MyViewHolder holder, final int position) {
            holder.text_product.setText(new DataBaseHelper(activity).getProductByID(nozzles.get(position).getProduct_id().trim()).getValue());
            holder.item_noz_num.setText("" + nozzles.get(position).getNozzle_num());
            holder.tv_calno.setText("" + nozzles.get(position).getCal_num());
            holder.tv_kfact.setText("" + nozzles.get(position).getK_factor());
            holder.tv_tot_val.setText("" + nozzles.get(position).getTot_value());
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nozzles.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return nozzles.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }*/
}