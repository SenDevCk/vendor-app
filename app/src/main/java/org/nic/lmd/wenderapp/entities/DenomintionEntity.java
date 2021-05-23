package org.nic.lmd.wenderapp.entities;

public class DenomintionEntity {
    private String value;
    private String vcId;
    private String vendorId;
    private String categoryId;
    private String name;
    private String denominationDesc;
    private String denominationOrder;
    private String remarks;
    private String checked; //(N/Y)
    private String quantity; //no of quantity
    private String set_m;//no of sets
    private String fee;
    private String val_year;
    private String pro_id;
    private String is_set;//(N/Y)

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDenominationDesc() {
        return denominationDesc;
    }

    public void setDenominationDesc(String denominationDesc) {
        this.denominationDesc = denominationDesc;
    }

    public String getDenominationOrder() {
        return denominationOrder;
    }

    public void setDenominationOrder(String denominationOrder) {
        this.denominationOrder = denominationOrder;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSet_m() {
        return set_m;
    }

    public void setSet_m(String set_m) {
        this.set_m = set_m;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getVal_year() {
        return val_year;
    }

    public void setVal_year(String val_year) {
        this.val_year = val_year;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getIs_set() {
        return is_set;
    }

    public void setIs_set(String is_set) {
        this.is_set = is_set;
    }

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

}
