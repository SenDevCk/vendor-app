package org.nic.lmd.wenderapp.entities;


public class VehicleTankDetails {
    private int vid;
    private String regNumber;
    private String engineNumber;
    private String chechisNumber;
    private String ownerFirmName;
    private String country;
    private int denomId;

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getChechisNumber() {
        return chechisNumber;
    }

    public void setChechisNumber(String chechisNumber) {
        this.chechisNumber = chechisNumber;
    }

    public String getOwnerFirmName() {
        return ownerFirmName;
    }

    public void setOwnerFirmName(String ownerFirmName) {
        this.ownerFirmName = ownerFirmName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public int getDenomId() {
        return denomId;
    }

    public void setDenomId(int denomId) {
        this.denomId = denomId;
    }
}
