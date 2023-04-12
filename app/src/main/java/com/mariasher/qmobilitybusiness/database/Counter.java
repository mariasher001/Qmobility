package com.mariasher.qmobilitybusiness.database;

import androidx.annotation.NonNull;

import com.mariasher.qmobilitybusiness.Utils.DateTimeUtils;
import com.mariasher.qmobilitybusiness.Utils.enums.CounterStatus;

import java.time.LocalDateTime;

public class Counter {
    @NonNull
    private String counterId;
    private String queueId;
    private String counterNumber;
    private String counterStatus;
    private int customerNumberOnCall;
    private int nextNumberOnCall;
    private String averageCustomerTime;

    public Counter() {
    }

    public Counter(@NonNull String counterId, String queueId, String counterNumber) {
        this.counterId = counterId;
        this.queueId = queueId;
        this.counterNumber = counterNumber;
        this.counterStatus = CounterStatus.INACTIVE.toString();
        this.customerNumberOnCall = 0;
        this.nextNumberOnCall = 0;
        this.averageCustomerTime = "";

    }

    public Counter(@NonNull String counterId, String queueId, String counterNumber, CounterStatus counterStatus, int customerNumberOnCall, int nextNumberOnCall, String averageCustomerTime) {
        this.counterId = counterId;
        this.queueId = queueId;
        this.counterNumber = counterNumber;
        this.counterStatus = counterStatus.toString();
        this.customerNumberOnCall = customerNumberOnCall;
        this.nextNumberOnCall = nextNumberOnCall;
        this.averageCustomerTime = averageCustomerTime;
    }

    public Counter(@NonNull String counterId, String queueId, String counterNumber, String counterStatus, int customerNumberOnCall, int nextNumberOnCall, String averageCustomerTime) {
        this.counterId = counterId;
        this.queueId = queueId;
        this.counterNumber = counterNumber;
        this.counterStatus = counterStatus;
        this.customerNumberOnCall = customerNumberOnCall;
        this.nextNumberOnCall = nextNumberOnCall;
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

    public String getCounterStatus() {
        return counterStatus;
    }

    public void setCounterStatus(String counterStatus) {
        this.counterStatus = counterStatus;
    }

    public int getCustomerNumberOnCall() {
        return customerNumberOnCall;
    }

    public void setCustomerNumberOnCall(int customerNumberOnCall) {
        this.customerNumberOnCall = customerNumberOnCall;
    }

    public int getNextNumberOnCall() {
        return nextNumberOnCall;
    }

    public void setNextNumberOnCall(int nextNumberOnCall) {
        this.nextNumberOnCall = nextNumberOnCall;
    }
}
