package org.nic.lmd.wenderapp.entities;

public class Nozzle {
    private int id;
    private String sl_id;
    private String nozzle_num;
    private String product_id;
    private String cal_num;
    private String k_factor;
    private String tot_value;
    private String vendorId;
    private String vcId;

    public String getSl_id() {
        return sl_id;
    }

    public void setSl_id(String sl_id) {
        this.sl_id = sl_id;
    }

    public String getNozzle_num() {
        return nozzle_num;
    }

    public void setNozzle_num(String nozzle_num) {
        this.nozzle_num = nozzle_num;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCal_num() {
        return cal_num;
    }

    public void setCal_num(String cal_num) {
        this.cal_num = cal_num;
    }

    public String getK_factor() {
        return k_factor;
    }

    public void setK_factor(String k_factor) {
        this.k_factor = k_factor;
    }

    public String getTot_value() {
        return tot_value;
    }

    public void setTot_value(String tot_value) {
        this.tot_value = tot_value;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
