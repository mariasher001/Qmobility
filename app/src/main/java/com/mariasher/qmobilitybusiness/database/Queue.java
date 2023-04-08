package com.mariasher.qmobilitybusiness.database;

import androidx.annotation.NonNull;

import com.mariasher.qmobilitybusiness.Utils.DateTimeUtils;
import com.mariasher.qmobilitybusiness.Utils.enums.QueueStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Queue {
    @NonNull
    private String queueId;
    private String creatorId;
    private String queueName;
    private String queueStartTime;
    private String queueEndTime;
    private String queueStatus;
    private int numberOfActiveCounters;
    private String averageCustomerTime;
    private List<String> activeCounterIds;

    public Queue() {
    }

    public Queue(@NonNull String queueId, String creatorId, String queueName) {
        this.queueId = queueId;
        this.creatorId = creatorId;
        this.queueName = queueName;
        queueStartTime = "";
        queueEndTime = "";
        queueStatus = QueueStatus.INACTIVE.toString();
        numberOfActiveCounters = 0;
        averageCustomerTime = "";
        activeCounterIds = new ArrayList<>();
    }

    public Queue(@NonNull String queueId, String creatorId, String queueName, LocalDateTime queueStartTime, LocalDateTime queueEndTime, String queueStatus, int numberOfActiveCounters, LocalDateTime averageCustomerTime, List<String> activeCounterIds) {
        this.queueId = queueId;
        this.creatorId = creatorId;
        this.queueName = queueName;
        this.queueStartTime = DateTimeUtils.convertDateAndTimeToString(queueStartTime);
        this.queueEndTime = DateTimeUtils.convertDateAndTimeToString(queueEndTime);
        this.queueStatus = queueStatus;
        this.numberOfActiveCounters = numberOfActiveCounters;
        this.averageCustomerTime = DateTimeUtils.convertDateAndTimeToString(averageCustomerTime);
        this.activeCounterIds = activeCounterIds;
    }

    public Queue(@NonNull String queueId, String creatorId, String queueName, String queueStartTime, String queueEndTime, String queueStatus, int numberOfActiveCounters, String averageCustomerTime, List<String> activeCounterIds) {
        this.queueId = queueId;
        this.creatorId = creatorId;
        this.queueName = queueName;
        this.queueStartTime = queueStartTime;
        this.queueEndTime = queueEndTime;
        this.queueStatus = queueStatus;
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public String getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(String queueStatus) {
        this.queueStatus = queueStatus;
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
