package com.mariasher.qmobilitybusiness.database;

import androidx.annotation.NonNull;

import com.mariasher.qmobilitybusiness.Utils.DateTimeUtils;

import java.time.LocalDateTime;

public class Client {
    @NonNull
    private String clientId;
    private String clientName;
    private String clientEmail;
    private String clientPhoneNumber;
    private String clientStatus;
    private String businessId;
    private String queueId;
    private int assignedNumberInQueue;
    private String assignedCounter;
    private String queueEntryTime;
    private String queueExitTime;

    public Client() {
    }

    public Client(String clientId, String clientName, String clientEmail, String clientPhoneNumber, String clientStatus, String businessId, String queueId, int assignedNumberInQueue, String assignedCounter, String queueEntryTime, String queueExitTime) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhoneNumber = clientPhoneNumber;
        this.clientStatus = clientStatus;
        this.businessId = businessId;
        this.queueId = queueId;
        this.assignedNumberInQueue = assignedNumberInQueue;
        this.assignedCounter = assignedCounter;
        this.queueEntryTime = queueEntryTime;
        this.queueExitTime = queueExitTime;
    }

    public Client(String clientId, String clientName, String clientEmail, String clientPhoneNumber, String clientStatus, String businessId, String queueId, int assignedNumberInQueue, String assignedCounter, LocalDateTime queueEntryTime, LocalDateTime queueExitTime) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhoneNumber = clientPhoneNumber;
        this.clientStatus = clientStatus;
        this.businessId = businessId;
        this.queueId = queueId;
        this.assignedNumberInQueue = assignedNumberInQueue;
        this.assignedCounter = assignedCounter;
        this.queueEntryTime = DateTimeUtils.convertDateAndTimeToString(queueEntryTime);
        this.queueExitTime = DateTimeUtils.convertDateAndTimeToString(queueExitTime);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public int getAssignedNumberInQueue() {
        return assignedNumberInQueue;
    }

    public void setAssignedNumberInQueue(int assignedNumberInQueue) {
        this.assignedNumberInQueue = assignedNumberInQueue;
    }

    public String getAssignedCounter() {
        return assignedCounter;
    }

    public void setAssignedCounter(String assignedCounter) {
        this.assignedCounter = assignedCounter;
    }

    public String getQueueEntryTime() {
        return queueEntryTime;
    }

    public void setQueueEntryTime(String queueEntryTime) {
        this.queueEntryTime = queueEntryTime;
    }

    public String getQueueExitTime() {
        return queueExitTime;
    }

    public void setQueueExitTime(String queueExitTime) {
        this.queueExitTime = queueExitTime;
    }
}
