package org.nic.lmd.wenderapp.asynctask;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nic.lmd.wenderapp.R;
import org.nic.lmd.wenderapp.activities.ApplyNewActivity;
import org.nic.lmd.wenderapp.entities.Class_ins;
import org.nic.lmd.wenderapp.entities.DenomintionEntity;
import org.nic.lmd.wenderapp.entities.InstrumentEntity;
import org.nic.lmd.wenderapp.entities.Nozzle;
import org.nic.lmd.wenderapp.entities.PatnerEntity;
import org.nic.lmd.wenderapp.entities.UserData;
import org.nic.lmd.wenderapp.entities.VehicleTankDetails;
import org.nic.lmd.wenderapp.fragments.ApplyNew1Fragment;
import org.nic.lmd.wenderapp.fragments.ApplyNew2Fragment;
import org.nic.lmd.wenderapp.fragments.ApplyNew3Fragment;
import org.nic.lmd.wenderapp.fragments.UploadDocumentFragment;
import org.nic.lmd.wenderapp.mdatabase.DataBaseHelper;
import org.nic.lmd.wenderapp.prefrences.CommonPref;
import org.nic.lmd.wenderapp.prefrences.GlobalVariable;
import org.nic.lmd.wenderapp.utilities.Urls_this_pro;
import org.nic.lmd.wenderapp.utilities.Utiilties;
import org.nic.lmd.wenderapp.utilities.WebHandler;

import java.util.ArrayList;

public class UploadTradorService extends AsyncTask<String, Void, String> {

    Activity activity;
    private ProgressDialog dialog1;
    private AlertDialog alertDialog;
    String pay_method;
    Intent intent;

    public UploadTradorService(Activity activity, String pay_method, Intent intent) {
        this.activity = activity;
        this.pay_method = pay_method;
        dialog1 = new ProgressDialog(this.activity);
        alertDialog = new AlertDialog.Builder(this.activity).create();
        this.intent = intent;
    }

    @Override
    protected void onPreExecute() {
        this.dialog1.setCanceledOnTouchOutside(false);
        this.dialog1.setMessage("UPLOADING WAIT...");
        this.dialog1.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String json_res = null;
        JSONArray jsonArray = getArrayOFIns();
        JSONArray jsonArray2 = getArrayOFWeight();
        JSONArray jsonArray3 = getArrayOFPatner();
        JSONArray jsonArray4 = getArrayOFVC();
        UserData userData = CommonPref.getUserDetails(activity);
        try {
            JSONObject jsonObject = new JSONObject();
            DataBaseHelper dataBaseHelper = new DataBaseHelper(activity);
            SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("Select* from VENDER_DETAILS where _id='" + GlobalVariable.count_data + "'", null);
            while (cursor.moveToNext()) {
                if (intent.hasExtra("vendorId"))
                    jsonObject.accumulate("vendorId", intent.getStringExtra("vendorId"));
                else jsonObject.accumulate("vendorId", JSONObject.NULL);
                jsonObject.accumulate("nameOfBusinessShop", cursor.getString(cursor.getColumnIndex("shop_name")));
                jsonObject.accumulate("premisesType", Integer.parseInt(cursor.getString(cursor.getColumnIndex("premise_type"))));
                jsonObject.accumulate("address1", cursor.getString(cursor.getColumnIndex("add1")));
                jsonObject.accumulate("address2", cursor.getString(cursor.getColumnIndex("add2")));
                jsonObject.accumulate("state", 10);
                jsonObject.accumulate("city", cursor.getString(cursor.getColumnIndex("city")));
                jsonObject.accumulate("district", Integer.parseInt(cursor.getString(cursor.getColumnIndex("dis_id"))));
                jsonObject.accumulate("landmark", cursor.getString(cursor.getColumnIndex("landmark")));
                jsonObject.accumulate("pincode", cursor.getString(cursor.getColumnIndex("pin")));
                jsonObject.accumulate("mobileNo", cursor.getString(cursor.getColumnIndex("mobile")));
                jsonObject.accumulate("landlineNo", cursor.getString(cursor.getColumnIndex("landline")));
                jsonObject.accumulate("emailId", cursor.getString(cursor.getColumnIndex("email")));
                jsonObject.accumulate("industrialAadhaarNo", "");
                jsonObject.accumulate("licenceNumber", cursor.getString(cursor.getColumnIndex("licence_no")));
                jsonObject.accumulate("firmType", Integer.parseInt(cursor.getString(cursor.getColumnIndex("type_firm"))));
                jsonObject.accumulate("registrationNo", cursor.getString(cursor.getColumnIndex("reg_no")));
                jsonObject.accumulate("tinNo", cursor.getString(cursor.getColumnIndex("tin")));
                jsonObject.accumulate("panNo", cursor.getString(cursor.getColumnIndex("pan")));
                jsonObject.accumulate("professionalTax", cursor.getString(cursor.getColumnIndex("pro")));
                jsonObject.accumulate("cstNo", cursor.getString(cursor.getColumnIndex("cst")));
                jsonObject.accumulate("tanNo", cursor.getString(cursor.getColumnIndex("tan")));
                jsonObject.accumulate("gstNo", cursor.getString(cursor.getColumnIndex("gst")));
                jsonObject.accumulate("natureOfBusiness", Integer.parseInt(cursor.getString(cursor.getColumnIndex("nat_of_bussiness"))));
                jsonObject.accumulate("block", Integer.parseInt(cursor.getString(cursor.getColumnIndex("block_id"))));
                if (!cursor.isNull(cursor.getColumnIndex("thana_id")))
                    jsonObject.accumulate("thanaCode", Integer.parseInt(cursor.getString(cursor.getColumnIndex("thana_id"))));
                else jsonObject.accumulate("thanaCode", JSONObject.NULL);
                jsonObject.accumulate("dateOfEstablishment", cursor.getString(cursor.getColumnIndex("date_of_est")));
                if (!cursor.isNull(cursor.getColumnIndex("licence_date")))
                    jsonObject.accumulate("licenceDate", cursor.getString(cursor.getColumnIndex("licence_date")));
                else jsonObject.accumulate("licenceDate", JSONObject.NULL);
                jsonObject.accumulate("subdivId", cursor.getString(cursor.getColumnIndex("sub_div_id")));
                jsonObject.accumulate("dateOfRegistration", cursor.getString(cursor.getColumnIndex("date_of_reg")));
                jsonObject.accumulate("commencementDate", cursor.getString(cursor.getColumnIndex("date_of_comm")));
                jsonObject.accumulate("placeOfverification", cursor.getString(cursor.getColumnIndex("place_of_var")));
                jsonObject.accumulate("latitude", cursor.isNull(cursor.getColumnIndex("latitude")) ? JSONObject.NULL : cursor.getString(cursor.getColumnIndex("latitude")));
                jsonObject.accumulate("longitude", cursor.isNull(cursor.getColumnIndex("longitude")) ? JSONObject.NULL : cursor.getString(cursor.getColumnIndex("longitude")));

                jsonObject.accumulate("shopImage", cursor.isNull(cursor.getColumnIndex("shop_img")) ? JSONObject.NULL : Utiilties.BitArrayToString(cursor.getBlob(cursor.getColumnIndex("shop_img"))));
            }
            jsonObject.accumulate("userId", userData.getUserid().trim());
            jsonObject.accumulate("xdelCountry", 1);
            jsonObject.accumulate("msg", "");
            //jsonObject.accumulate("payMode", pay_method);
            jsonObject.accumulate("payMode", JSONObject.NULL);
            jsonObject.accumulate("status", "INC");
            jsonObject.accumulate("instruments", jsonArray);
            jsonObject.accumulate("weights", jsonArray2);
            jsonObject.accumulate("vcs", jsonArray4);
            jsonObject.accumulate("vofficials", jsonArray3);
            //if (!intent.hasExtra("for"))
            json_res = WebHandler.callByPost(jsonObject.toString(), Urls_this_pro.LOAD_UPLOAD);
        } catch (Exception e) {
            e.printStackTrace();
            return json_res;
        }
        return json_res;
    }

    private JSONArray getArrayOFIns() {
        ArrayList<InstrumentEntity> instrumentEntities = new DataBaseHelper(activity).getInstrumentAdded();
        JSONArray jsonArray_ins = new JSONArray();
        try {
            for (int pos = 0; pos < instrumentEntities.size(); pos++) {
                InstrumentEntity instrumentEntity = instrumentEntities.get(pos);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("vendorId", (instrumentEntity.getVendorId() == null) ? JSONObject.NULL : instrumentEntity.getVendorId());
                jsonObject.accumulate("vcId", ((instrumentEntity.getVcId() == null) ? JSONObject.NULL : Integer.parseInt(instrumentEntity.getVcId())));
                jsonObject.accumulate("capacityId", Integer.parseInt(instrumentEntity.getCap_id()));
                jsonObject.accumulate("proposalId", Integer.parseInt(instrumentEntity.getPro_id()));
                jsonObject.accumulate("categoryId", Integer.parseInt(instrumentEntity.getCat_id()));
                /*JSONArray jsonArray_class=new JSONArray();
                for (Class_ins class_ins:instrumentEntity.getIns_class()){
                    JSONObject jsonObject_class=new JSONObject();
                    jsonObject_class.accumulate("value",Integer.parseInt(class_ins.getValue()));
                    jsonObject_class.accumulate("name",class_ins.getName());
                }*/
                jsonObject.accumulate("classId", Integer.parseInt(instrumentEntity.getIns_class()));
                if ((instrumentEntity.getCat_id().equals("16") && instrumentEntity.getCap_id().equals("219")) || (instrumentEntity.getCat_id().equals("19") && instrumentEntity.getCap_id().equals("225")) || (instrumentEntity.getCat_id().equals("22") && instrumentEntity.getCap_id().equals("230"))) {
                    jsonObject.accumulate("quantity", 1);
                    jsonObject.accumulate("nozzels",  Integer.parseInt(instrumentEntity.getQuantity()));
                }else {
                    jsonObject.accumulate("quantity", Integer.parseInt(instrumentEntity.getQuantity()));
                    jsonObject.accumulate("nozzels", 0);
                }
                jsonObject.accumulate("manufacturer", JSONObject.NULL);
                jsonObject.accumulate("capacityMax", "" + instrumentEntity.getCap_max());
                jsonObject.accumulate("capacityMin", "" + instrumentEntity.getCap_min());
                jsonObject.accumulate("validYear", Integer.parseInt(instrumentEntity.getVal_year()));
                jsonObject.accumulate("modelNo", "" + instrumentEntity.getModel_no());
                jsonObject.accumulate("mserialNo", "" + instrumentEntity.getSer_no());
                jsonObject.accumulate("vfRate", JSONObject.NULL);
                jsonObject.accumulate("vfAmount", JSONObject.NULL);
                jsonObject.accumulate("urAmount", JSONObject.NULL);
                jsonObject.accumulate("nextverification", JSONObject.NULL);
                jsonObject.accumulate("duesQtr", JSONObject.NULL);
                jsonObject.accumulate("status", JSONObject.NULL);
                jsonObject.accumulate("slNo", JSONObject.NULL);
                jsonObject.accumulate("evalue", "" + instrumentEntity.getE_val());
                jsonObject.accumulate("extensions", getArrayOFExtentions(instrumentEntity.getId(), pos));
                jsonArray_ins.put(jsonObject);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return jsonArray_ins;
    }

    private JSONArray getArrayOFExtentions(String sl_id, int pos) {
        ArrayList<Nozzle> nozzles = new DataBaseHelper(activity).getAddedNozzle(sl_id);
        JSONArray jsonArray_nozz = new JSONArray();
        try {
            for (int i = 0; i < nozzles.size(); i++) {
                Nozzle nozzle = nozzles.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("vendorId", (nozzle.getVendorId() == null) ? JSONObject.NULL : nozzle.getVendorId().trim());
                jsonObject.accumulate("vcId", (nozzle.getVcId() == null) ? JSONObject.NULL : Integer.parseInt(nozzle.getVcId().trim()));
                jsonObject.accumulate("insSlno", pos);
                jsonObject.accumulate("extSlno", i + 1);
                jsonObject.accumulate("nozalNo", nozzle.getNozzle_num());
                jsonObject.accumulate("kfactor", Double.parseDouble(nozzle.getK_factor()));
                jsonObject.accumulate("totalizerValue", Double.parseDouble(nozzle.getTot_value()));
                jsonObject.accumulate("product", Integer.parseInt(nozzle.getProduct_id().trim()));
                jsonObject.accumulate("calNo", Integer.parseInt(nozzle.getCal_num()));
                jsonArray_nozz.put(jsonObject);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            return jsonArray_nozz;
        }
        return jsonArray_nozz;
    }

    private JSONArray getArrayOFWeight() {
        ArrayList<DenomintionEntity> denomintionEntities = new DataBaseHelper(activity).getWeightDenomination("0", "0");
        JSONArray jsonArray_weight = new JSONArray();
        try {
            for (DenomintionEntity denomintionEntity : denomintionEntities) {
                if (denomintionEntity.getCategoryId().equals("19")) {
                    ArrayList<VehicleTankDetails> vehicleTankDetails = new DataBaseHelper(activity).getTotalTank(denomintionEntity.getValue());
                    for (int i = 0; i < vehicleTankDetails.size(); i++) {
                        jsonArray_weight.put(returnWeightObject(denomintionEntity, vehicleTankDetails.get(i)));
                    }
                } else {
                    jsonArray_weight.put(returnWeightObject(denomintionEntity, new VehicleTankDetails()));
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            return jsonArray_weight;
        }
        return jsonArray_weight;
    }

    private JSONObject returnWeightObject(DenomintionEntity denomintionEntity, VehicleTankDetails vehicleTankDetail) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.accumulate("vendorId", (denomintionEntity.getVendorId() == null) ? JSONObject.NULL : denomintionEntity.getVendorId().trim());
            jsonObject.accumulate("vcId", (denomintionEntity.getVcId() == null) ? JSONObject.NULL : Integer.parseInt(denomintionEntity.getVcId().trim()));
            jsonObject.accumulate("denomination", Integer.parseInt(denomintionEntity.getValue()));
            jsonObject.accumulate("proposalId", Integer.parseInt(denomintionEntity.getPro_id().trim()));
            jsonObject.accumulate("categoryId", Integer.parseInt(denomintionEntity.getCategoryId().trim()));
            jsonObject.accumulate("quantity", Integer.parseInt(denomintionEntity.getQuantity().trim()));
            jsonObject.accumulate("validYear", Integer.parseInt(denomintionEntity.getVal_year().trim()));
            jsonObject.accumulate("vfRate", JSONObject.NULL);
            jsonObject.accumulate("vfAmount", JSONObject.NULL);
            jsonObject.accumulate("afAmount", JSONObject.NULL);
            jsonObject.accumulate("urAmount", JSONObject.NULL);
            jsonObject.accumulate("nextverificationQtr", JSONObject.NULL);
            jsonObject.accumulate("duesQtr", JSONObject.NULL);
            jsonObject.accumulate("status", JSONObject.NULL);
            jsonObject.accumulate("vechile_registraction_no", (vehicleTankDetail.getDenomId() == 0) ? JSONObject.NULL : vehicleTankDetail.getRegNumber());
            jsonObject.accumulate("vechile_engine_no", (vehicleTankDetail.getDenomId() == 0) ? JSONObject.NULL : vehicleTankDetail.getEngineNumber());
            jsonObject.accumulate("vechile_chesis_no", (vehicleTankDetail.getDenomId() == 0) ? JSONObject.NULL : vehicleTankDetail.getChechisNumber());
            jsonObject.accumulate("vechile_owner_name", (vehicleTankDetail.getDenomId() == 0) ? JSONObject.NULL : vehicleTankDetail.getOwnerFirmName());
            jsonObject.accumulate("country_name", (vehicleTankDetail.getDenomId() == 0) ? JSONObject.NULL : vehicleTankDetail.getCountry());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONArray getArrayOFPatner() {
        ArrayList<PatnerEntity> patnerEntities = new DataBaseHelper(activity).getAllPatners();
        JSONArray jsonArray_ins = new JSONArray();
        try {
            for (PatnerEntity patnerEntity : patnerEntities) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("partnerId", ((patnerEntity.getPartnerId() == null) ? JSONObject.NULL : Integer.parseInt(patnerEntity.getPartnerId().trim())));
                jsonObject.accumulate("vendorId", ((patnerEntity.getVendorId() == null) ? JSONObject.NULL : patnerEntity.getVendorId().trim()));
                jsonObject.accumulate("partnerName", "" + patnerEntity.getName());
                jsonObject.accumulate("fatherHusbandName", "" + patnerEntity.getFather_name());
                jsonObject.accumulate("aadhaarNo", "" + patnerEntity.getAdhar_vid());
                jsonObject.accumulate("address1", "" + patnerEntity.getAdd1());
                jsonObject.accumulate("address2", "" + patnerEntity.getAdd2());
                jsonObject.accumulate("state", 10);
                jsonObject.accumulate("city", "" + patnerEntity.getCity());
                jsonObject.accumulate("district", Integer.parseInt(patnerEntity.getDistrict()));
                jsonObject.accumulate("block", Integer.parseInt(patnerEntity.getBlock()));
                jsonObject.accumulate("landmark", "" + patnerEntity.getLandmark());
                jsonObject.accumulate("pincode", patnerEntity.getPinCode());
                jsonObject.accumulate("mobileNo", "" + patnerEntity.getMobile());
                jsonObject.accumulate("landlineNo", "" + patnerEntity.getLandline());
                jsonObject.accumulate("emailId", "" + patnerEntity.getEmail());
                if (patnerEntity.getIs_nom_under49().equalsIgnoreCase("Y"))
                    jsonObject.accumulate("nominatedUnderSection", true);
                else jsonObject.accumulate("nominatedUnderSection", false);
                jsonObject.accumulate("designation", Integer.parseInt(patnerEntity.getDesignation_id()));
                //jsonObject.accumulate("xdelCountry","INDIA");
                jsonArray_ins.put(jsonObject);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            return jsonArray_ins;
        }
        return jsonArray_ins;
    }

    private JSONArray getArrayOFVC() {
        //ArrayList<PatnerEntity> patnerEntities=new DataBaseHelper(activity).getAllPatners();
        JSONArray jsonArray_ins = new JSONArray();
        try {
            if (intent.hasExtra("for")) {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("json"));
                jsonArray_ins = null;
                jsonArray_ins = jsonObject.getJSONArray("vcs");
            } else {
                JSONObject jsonObject = new JSONObject();
                if (intent.hasExtra("vendorId"))
                    jsonObject.accumulate("vendorId", intent.getStringExtra("vendorId"));
                else jsonObject.accumulate("vendorId", JSONObject.NULL);
                jsonObject.accumulate("vcId", JSONObject.NULL);
                jsonObject.accumulate("vcDate", JSONObject.NULL);
                jsonObject.accumulate("vcNumber", JSONObject.NULL);
                jsonObject.accumulate("nextVcDate", JSONObject.NULL);
                //jsonObject.accumulate("vendor",null);
                jsonArray_ins.put(jsonObject);
            }


        } catch (Exception exp) {
            exp.printStackTrace();
            return jsonArray_ins;
        }
        return jsonArray_ins;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        Log.d("res", res);
        if (this.dialog1.isShowing()) dialog1.dismiss();
        if (res != null) {
            try {
                final JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.has("statusCode")) {
                    if (jsonObject.getInt("statusCode") == 200) {
                        Toast.makeText(activity, "Success : " + jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                        new DataBaseHelper(activity).deleteAllInstruments();
                        new DataBaseHelper(activity).deleteAllPatner();
                        new DataBaseHelper(activity).updateweightDenomination();
                        new DataBaseHelper(activity).deleteAllNozzle();
                        new DataBaseHelper(activity).deleteAllTanks();
                        //String ven_id = jsonObject.getString("status").trim().split(":")[1];
                        String vid = Utiilties.extractInt(jsonObject.getString("status").split(":")[1]).trim();
                        //String ven_id = jsonObject.getString("status").split(":")[1].substring(0,9);
                        long c = new DataBaseHelper(activity).saveVender(vid, CommonPref.getUserDetails(activity).getApplicantId().trim());
                        if (c <= 0) {
                            Log.e("log : ", "Vendor Id not saved in UploadTradorService(line 249)");
                            Toast.makeText(activity, "Vendor Id not saved in UploadTradorService", Toast.LENGTH_SHORT).show();
                        } else {
                            FrameLayout frame_reg = (FrameLayout) activity.findViewById(R.id.frame_ap_new);
                            UploadDocumentFragment applyNew1Fragment = UploadDocumentFragment.newInstance(vid, "");
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame_ap_new, applyNew1Fragment);
                            //fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    } else {
                        if (jsonObject.getString("status") != null) {
                           /* Toast.makeText(activity, "" + jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                            alertDialog.setMessage("" + jsonObject.getString("status"));
                            alertDialog.show();*/
                            Toast.makeText(activity, "Success : " + jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                            new DataBaseHelper(activity).deleteAllInstruments();
                            new DataBaseHelper(activity).deleteAllPatner();
                            new DataBaseHelper(activity).updateweightDenomination();
                            new DataBaseHelper(activity).deleteAllNozzle();
                            new DataBaseHelper(activity).deleteAllTanks();
                            String id_ven = jsonObject.getString("status").split(":")[1].substring(0, 9);
                            long c = new DataBaseHelper(activity).saveVender(id_ven, CommonPref.getUserDetails(activity).getApplicantId().trim());
                            if (c <= 0) {
                                Log.e("log : ", "Vender Id not saved in UploadTradorService(line 249)");
                                Toast.makeText(activity, "Vender Id not saved in UploadTradorService", Toast.LENGTH_SHORT).show();
                            } else {
                                FrameLayout frame_reg = (FrameLayout) activity.findViewById(R.id.frame_ap_new);
                                UploadDocumentFragment applyNew1Fragment = UploadDocumentFragment.newInstance(id_ven, "");
                                FragmentManager fragmentManager = activity.getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.frame_ap_new, applyNew1Fragment);
                                //fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        } else {
                            Toast.makeText(activity, "Status null found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.e("error", "Status code not found in json");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("log", "res NULL on UploadTradorService");
            Toast.makeText(activity, "res null on UploadTradorService", Toast.LENGTH_SHORT).show();
        }
    }


}

