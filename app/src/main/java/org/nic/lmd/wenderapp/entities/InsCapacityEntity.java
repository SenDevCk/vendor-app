package org.nic.lmd.wenderapp.entities;

public class InsCapacityEntity {
    private String capacityId;
    private String categoryId;
    private String capacityValue;
    private String capacityDesc;
    private String capacityOrder;
    private String checked;

    public String getCapacityId() {
        return capacityId;
    }

    public void setCapacityId(String capacityId) {
        this.capacityId = capacityId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCapacityValue() {
        return capacityValue;
    }

    public void setCapacityValue(String capacityValue) {
        this.capacityValue = capacityValue;
    }

    public String getCapacityDesc() {
        return capacityDesc;
    }

    public void setCapacityDesc(String capacityDesc) {
        this.capacityDesc = capacityDesc;
    }

    public String getCapacityOrder() {
        return capacityOrder;
    }

    public void setCapacityOrder(String capacityOrder) {
        this.capacityOrder = capacityOrder;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}
