package org.nic.lmd.wenderapp.entities;

public class Block {
    private String value;
    private String name;
    private String districtId;
    private String estbSubdivId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getEstbSubdivId() {
        return estbSubdivId;
    }

    public void setEstbSubdivId(String estbSubdivId) {
        this.estbSubdivId = estbSubdivId;
    }
}
