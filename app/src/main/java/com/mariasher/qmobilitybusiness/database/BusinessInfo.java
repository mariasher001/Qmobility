package com.mariasher.qmobilitybusiness.database;

import java.util.UUID;

public class BusinessInfo {
    private String businessName;
    private String phoneNumber;
    private String businessEmail;
    private String businessAddress;
    private String businessLatitude;
    private String businessLongitude;
    private String businessType;
    private String locationManagersName;
    private String businessID;


    //Constructor
    public BusinessInfo(String businessName, String phoneNumber, String businessEmail, String businessAddress,
                        String businessLatitude, String businessLongitude, String businessType,
                        String locationManagersName, String businessID)
    {
        this.businessName = businessName;
        this.phoneNumber = phoneNumber;
        this.businessEmail = businessEmail;
        this.businessAddress = businessAddress;
        this.businessLatitude = businessLatitude;
        this.businessLongitude = businessLongitude;
        this.businessType = businessType;
        this.locationManagersName = locationManagersName;
        this.businessID = businessID;
    }

 //Empty constructor
    public BusinessInfo() {
    }

//Getters and Setters
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

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
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

    public String getLocationManagersName() {
        return locationManagersName;
    }

    public void setLocationManagersName(String locationManagersName) {
        this.locationManagersName = locationManagersName;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }
}
