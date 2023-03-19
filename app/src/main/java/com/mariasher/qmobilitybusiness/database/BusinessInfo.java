package com.mariasher.qmobilitybusiness.database;

import androidx.annotation.NonNull;

public class BusinessInfo {

    @NonNull
    private String businessID;
    private String businessName;
    private String phoneNumber;
    private String businessAddress;
    private String businessLatitude;
    private String businessLongitude;
    private String businessType;

    public BusinessInfo(@NonNull String businessID, String businessName, String phoneNumber, String businessAddress, String businessLatitude, String businessLongitude, String businessType) {
        this.businessID = businessID;
        this.businessName = businessName;
        this.phoneNumber = phoneNumber;
        this.businessAddress = businessAddress;
        this.businessLatitude = businessLatitude;
        this.businessLongitude = businessLongitude;
        this.businessType = businessType;
    }

    public BusinessInfo() {
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessLatitude() {
        return businessLatitude;
    }

    public void setBusinessLatitude(String businessLatitude) {
        this.businessLatitude = businessLatitude;
    }

    public String getBusinessLongitude() {
        return businessLongitude;
    }

    public void setBusinessLongitude(String businessLongitude) {
        this.businessLongitude = businessLongitude;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }
}