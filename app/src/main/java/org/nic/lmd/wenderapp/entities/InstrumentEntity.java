package org.nic.lmd.wenderapp.entities;

import java.util.ArrayList;

public class InstrumentEntity {

    private String id;
    private String vendorId;
    private String vcId;
    private String pro_id;
    private String cat_id;
    private String cap_id;
    private String quantity;
    private String ins_class;
    private String m_or_brand;
    private String val_year;
    private String cap_max;
    private String cap_min;
    private String model_no;
    private String ser_no;
    private String e_val;
    private ArrayList<Nozzle> nozzles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCap_id() {
        return cap_id;
    }

    public void setCap_id(String cap_id) {
        this.cap_id = cap_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public  String getIns_class() {
        return ins_class;
    }

    public void setIns_class( String ins_class) {
        this.ins_class = ins_class;
    }

    public String getM_or_brand() {
        return m_or_brand;
    }

    public void setM_or_brand(String m_or_brand) {
        this.m_or_brand = m_or_brand;
    }

    public String getVal_year() {
        return val_year;
    }

    public void setVal_year(String val_year) {
        this.val_year = val_year;
    }

    public String getCap_max() {
        return cap_max;
    }

    public void setCap_max(String cap_max) {
        this.cap_max = cap_max;
    }

    public String getCap_min() {
        return cap_min;
    }

    public void setCap_min(String cap_min) {
        this.cap_min = cap_min;
    }

    public String getModel_no() {
        return model_no;
    }

    public void setModel_no(String model_no) {
        this.model_no = model_no;
    }

    public String getSer_no() {
        return ser_no;
    }

    public void setSer_no(String ser_no) {
        this.ser_no = ser_no;
    }

    public String getE_val() {
        return e_val;
    }

    public void setE_val(String e_val) {
        this.e_val = e_val;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public ArrayList<Nozzle> getNozzles() {
        return nozzles;
    }

    public void setNozzles(ArrayList<Nozzle> nozzles) {
        this.nozzles = nozzles;
    }
}
