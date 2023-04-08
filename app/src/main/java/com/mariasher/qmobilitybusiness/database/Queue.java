package com.mariasher.qmobilitybusiness.database;

import androidx.annotation.NonNull;

import com.mariasher.qmobilitybusiness.Utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

public class Queue {
    @NonNull
    private String queueId;
    private String queueName;
    private String queueStartTime;
    private String queueEndTime;
    private boolean isQueueStarted;
    private boolean isQueueActive;
    private int numberOfActiveCounters;
    private String averageCustomerTime;
    private List<String> activeCounterIds;

    public Queue() {
    }

    public Queue(@NonNull String queueId, String queueName, String queueStartTime, String queueEndTime, boolean isQueueStarted, boolean isQueueActive, int numberOfActiveCounters, LocalDateTime averageCustomerTime, List<String> activeCounterIds) {
        this.queueId = queueId;
        this.queueName = queueName;
        this.queueStartTime = queueStartTime;
        this.queueEndTime = queueEndTime;
        this.isQueueStarted = isQueueStarted;
        this.isQueueActive = isQueueActive;
        this.numberOfActiveCounters = numberOfActiveCounters;
        this.averageCustomerTime = DateTimeUtils.convertDateAndTimeToString(averageCustomerTime);
        this.activeCounterIds = activeCounterIds;
    }

    public Queue(@NonNull String queueId, String queueName, String queueStartTime, String queueEndTime, boolean isQueueStarted, boolean isQueueActive, int numberOfActiveCounters, String averageCustomerTime, List<String> activeCounterIds) {
        this.queueId = queueId;
        this.queueName = queueName;
        this.queueStartTime = queueStartTime;
        this.queueEndTime = queueEndTime;
        this.isQueueStarted = isQueueStarted;
        this.isQueueActive = isQueueActive;
        this.numberOfActiveCounters = numberOfActiveCounters;
        this.averageCustomerTime = averageCustomerTime;
        this.activeCounterIds = activeCounterIds;
    }

    @NonNull
    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(@NonNull String queueId) {
        this.queueId = queueId;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueStartTime() {
        return queueStartTime;
    }

    public void setQueueStartTime(String queueStartTime) {
        this.queueStartTime = queueStartTime;
    }

    public String getQueueEndTime() {
        return queueEndTime;
    }

    public void setQueueEndTime(String queueEndTime) {
        this.queueEndTime = queueEndTime;
    }

    public boolean isQueueStarted() {
        return isQueueStarted;
    }

    public void setQueueStarted(boolean queueStarted) {
        isQueueStarted = queueStarted;
    }

    public boolean isQueueActive() {
        return isQueueActive;
    }

    public void setQueueActive(boolean queueActive) {
        isQueueActive = queueActive;
    }

    public int getNumberOfActiveCounters() {
        return numberOfActiveCounters;
    }

    public void setNumberOfActiveCounters(int numberOfActiveCounters) {
        this.numberOfActiveCounters = numberOfActiveCounters;
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

    public List<String> getActiveCounterIds() {
        return activeCounterIds;
    }

    public void setActiveCounterIds(List<String> activeCounterIds) {
        this.activeCounterIds = activeCounterIds;
    }
}
