package com.mariasher.qmobilitybusiness.database;

import androidx.annotation.NonNull;

import com.mariasher.qmobilitybusiness.Utils.DateTimeUtils;

import java.time.LocalDateTime;

public class Counter {
    @NonNull
    private String counterId;
    private String queueId;
    private String counterNumber;
    private boolean isCounterStarted;
    private boolean isCounterActive;
    private int customerNumberOnCall;
    private int nextNumber;

    private String averageCustomerTime;

    public Counter() {
    }

    public Counter(@NonNull String counterId, String queueId, String counterNumber, boolean isCounterStarted, boolean isCounterActive, int customerNumberOnCall, int nextNumber, LocalDateTime averageCustomerTime) {
        this.counterId = counterId;
        this.queueId = queueId;
        this.counterNumber = counterNumber;
        this.isCounterStarted = isCounterStarted;
        this.isCounterActive = isCounterActive;
        this.customerNumberOnCall = customerNumberOnCall;
        this.nextNumber = nextNumber;
        this.averageCustomerTime = DateTimeUtils.convertDateAndTimeToString(averageCustomerTime);
    }

    public Counter(@NonNull String counterId, String queueId, String counterNumber, boolean isCounterStarted, boolean isCounterActive, int customerNumberOnCall, int nextNumber, String averageCustomerTime) {
        this.counterId = counterId;
        this.queueId = queueId;
        this.counterNumber = counterNumber;
        this.isCounterStarted = isCounterStarted;
        this.isCounterActive = isCounterActive;
        this.customerNumberOnCall = customerNumberOnCall;
        this.nextNumber = nextNumber;
        this.averageCustomerTime = averageCustomerTime;
    }

    public String getAverageCustomerTime() {
        return averageCustomerTime;
    }

    public void setAverageCustomerTime(String averageCustomerTime) {
        this.averageCustomerTime = averageCustomerTime;
    }

    public LocalDateTime getAverageCustomerTimeInLocalDateTimeFormat() {
        return DateTimeUtils.convertStringToLocalDateTime(averageCustomerTime);
    }

    public void setAverageCustomerTimeInLocalDateTimeFormat(LocalDateTime averageCustomerTime) {
        this.averageCustomerTime = DateTimeUtils.convertDateAndTimeToString(averageCustomerTime);
    }

    @NonNull
    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(@NonNull String counterId) {
        this.counterId = counterId;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getCounterNumber() {
        return counterNumber;
    }

    public void setCounterNumber(String counterNumber) {
        this.counterNumber = counterNumber;
    }

    public boolean isCounterStarted() {
        return isCounterStarted;
    }

    public void setCounterStarted(boolean counterStarted) {
        isCounterStarted = counterStarted;
    }

    public boolean isCounterActive() {
        return isCounterActive;
    }

    public void setCounterActive(boolean counterActive) {
        isCounterActive = counterActive;
    }

    public int getCustomerNumberOnCall() {
        return customerNumberOnCall;
    }

    public void setCustomerNumberOnCall(int customerNumberOnCall) {
        this.customerNumberOnCall = customerNumberOnCall;
    }

    public int getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(int nextNumber) {
        this.nextNumber = nextNumber;
    }
}
