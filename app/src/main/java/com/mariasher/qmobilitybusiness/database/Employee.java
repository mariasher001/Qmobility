package com.mariasher.qmobilitybusiness.database;

import androidx.annotation.NonNull;

import com.mariasher.qmobilitybusiness.Utils.enums.AccessType;

public class Employee {
    @NonNull
    private String employeeId;
    private String name;
    private String emailAddress;
    private String BusinessId;
    private String phoneNumber;
    private String AccessType;

    public Employee() {
    }

    public Employee(@NonNull String employeeId, String name, String emailAddress, String businessId, String phoneNumber, AccessType accessType) {
        this.employeeId = employeeId;
        this.name = name;
        this.emailAddress = emailAddress;
        BusinessId = businessId;
        this.phoneNumber = phoneNumber;
        AccessType = accessType.toString();
    }

    public Employee(@NonNull String employeeId, String name, String emailAddress, String businessId, String phoneNumber, String accessType) {
        this.employeeId = employeeId;
        this.name = name;
        this.emailAddress = emailAddress;
        BusinessId = businessId;
        this.phoneNumber = phoneNumber;
        AccessType = accessType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccessType() {
        return AccessType;
    }

    public void setAccessType(String accessType) {
        AccessType = accessType;
    }

    public String getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(String businessId) {
        BusinessId = businessId;
    }
}
