package com.mariasher.qmobilitybusiness.Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.database.Client;
import com.mariasher.qmobilitybusiness.database.Counter;
import com.mariasher.qmobilitybusiness.database.Employee;
import com.mariasher.qmobilitybusiness.database.Queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRealtimeUtils {

    private FirebaseDatabase mReal;
    private Context context;

    public FirebaseRealtimeUtils(Context context) {
        mReal = FirebaseDatabase.getInstance();
        this.context = context;
    }

    public void getBusinessIdFromEmployeeBusinessLink(String employeeId, Callback<String> callback) {
        mReal.getReference("QMobility")
                .child("EmployeeBusinessLink")
                .child(employeeId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String businessId = snapshot.getValue(String.class);
                        callback.onSuccess(businessId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void getBusinessNameFromBusinessDetails(String businessId, Callback<String> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("BusinessDetails")
                .child("businessName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String businessName = snapshot.getValue(String.class);
                        callback.onSuccess(businessName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void createEmployeeBusinessLinkInRealtimeDatabase(String employeeId, String businessId) {
        mReal.getReference("QMobility")
                .child("EmployeeBusinessLink")
                .child(employeeId)
                .setValue(businessId);
    }

    public void addEmployeeToBusinessInRealtimeDatabase(Employee employee, Callback<Boolean> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(employee.getBusinessId())
                .child("EmployeeDetails")
                .child(employee.getEmployeeId())
                .setValue(employee)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onSuccess(false);
                    }
                });
    }

    public void getQueueDataFromFirebaseDatabase(String userId, String queueId, Callback<Queue> callback) {
        getBusinessIdFromEmployeeBusinessLink(userId, businessId -> {
            getQueueDataFromFirebaseWithBusinessId(businessId, queueId, callback);
        });
    }

    public void getQueueDataFromFirebaseWithBusinessId(String businessId, String queueId, Callback<Queue> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Queues")
                .child(queueId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            Queue queue = getQueueFields(snapshot);
                            callback.onSuccess(queue);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getQueuesFromFirebaseDatabase(String userId, Callback<List<Queue>> callback) {
        getBusinessIdFromEmployeeBusinessLink(userId, businessId -> {
            mReal.getReference("QMobility")
                    .child("Businesses")
                    .child(businessId)
                    .child("Queues")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Queue> queues = new ArrayList<>();
                            for (DataSnapshot queueSnapshot : snapshot.getChildren()) {
                                Queue queue = getQueueFields(queueSnapshot);
                                queues.add(queue);
                            }
                            callback.onSuccess(queues);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
    }

    public void updateQueueInFirebase(String businessId, Queue queue, Callback<Boolean> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Queues")
                .child(queue.getQueueId())
                .setValue(queue)
                .addOnCompleteListener(task -> {
                    callback.onSuccess(task.isSuccessful());
                });
    }

    public void getAllCounters(String businessId, Callback<List<Counter>> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Counters")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Counter> counters = new ArrayList<>();
                        for (DataSnapshot counterSnapshot : snapshot.getChildren()) {
                            Counter counter = getCounterFields(counterSnapshot);
                            counters.add(counter);
                        }
                        callback.onSuccess(counters);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getAllCountersOnce(String businessId, Callback<List<Counter>> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Counters")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Counter> counters = new ArrayList<>();
                        for (DataSnapshot counterSnapshot : snapshot.getChildren()) {
                            Counter counter = getCounterFields(counterSnapshot);
                            counters.add(counter);
                        }
                        callback.onSuccess(counters);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getCounterDetailsFromFirebase(String businessId, String counterId, Callback<Counter> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Counters")
                .child(counterId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            Counter counter = getCounterFields(snapshot);
                            callback.onSuccess(counter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private Counter getCounterFields(DataSnapshot counterSnapshot) {
        String counterId = counterSnapshot.child("counterId").getValue(String.class);
        String queueId = counterSnapshot.child("queueId").getValue(String.class);
        String counterNumber = counterSnapshot.child("counterNumber").getValue(String.class);
        String counterStatus = counterSnapshot.child("counterStatus").getValue(String.class);
        Integer customerNumberOnCall = counterSnapshot.child("customerNumberOnCall").getValue(Integer.class);
        Integer nextNumberOnCall = counterSnapshot.child("nextNumberOnCall").getValue(Integer.class);
        String averageCustomerTime = counterSnapshot.child("averageCustomerTime").getValue(String.class);

        return new Counter(counterId, queueId, counterNumber, counterStatus, customerNumberOnCall, nextNumberOnCall, averageCustomerTime);
    }

    public void isCounterNumberAlreadyInDatabase(String userId, String counterNumber, Callback<Boolean> callback) {
        getBusinessIdFromEmployeeBusinessLink(userId, businessId -> {
            mReal.getReference("QMobility")
                    .child("Businesses")
                    .child(businessId)
                    .child("Counters")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean counterNumberMatched = false;
                            for (DataSnapshot counterSnapshot : snapshot.getChildren()) {
                                String counterNumberSnapshot = counterSnapshot.child("counterNumber").getValue(String.class);
                                if (counterNumberSnapshot.equals(counterNumber)) {
                                    counterNumberMatched = true;
                                    break;
                                }
                            }
                            callback.onSuccess(counterNumberMatched);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
    }

    private Queue getQueueFields(DataSnapshot snapshot) {
        String queueId = snapshot.child("queueId").getValue(String.class);
        String creatorId = snapshot.child("creatorId").getValue(String.class);
        String queueName = snapshot.child("queueName").getValue(String.class);
        String queueStartTime = snapshot.child("queueStartTime").getValue(String.class);
        String queueEndTime = snapshot.child("queueEndTime").getValue(String.class);
        String queueStatus = snapshot.child("queueStatus").getValue(String.class);
        Integer numberOfActiveCounters = snapshot.child("numberOfActiveCounters").getValue(Integer.class);
        String averageCustomerTime = snapshot.child("averageCustomerTime").getValue(String.class);
        Double totalClientWaitingTime = snapshot.child("totalClientWaitingTime").getValue(Double.class);
        Integer totalClients = snapshot.child("totalClients").getValue(Integer.class);

        Map<String, Object> queueCounters = new HashMap<>();
        for (DataSnapshot queueCounterSnapshot : snapshot.child("queueCounters").getChildren()) {
            String queueCounterId = queueCounterSnapshot.getKey();
            String counterNumber = queueCounterSnapshot.getValue(String.class);
            queueCounters.put(queueCounterId, counterNumber);
        }

        Map<String, Object> clientsInQueue = new HashMap<>();
        for (DataSnapshot queueCounterSnapshot : snapshot.child("clientsInQueue").getChildren()) {
            String clientId = queueCounterSnapshot.getKey();
            String clientStatus = queueCounterSnapshot.getValue(String.class);
            clientsInQueue.put(clientId, clientStatus);
        }
        Queue queue = new Queue(queueId, creatorId, queueName, queueStartTime, queueEndTime, queueStatus,
                numberOfActiveCounters, averageCustomerTime, queueCounters, clientsInQueue, totalClientWaitingTime, totalClients);

        return queue;
    }

    public void updateCounterInFirebase(String businessId, Counter counter, Callback<Boolean> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Counters")
                .child(counter.getCounterId())
                .setValue(counter)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);
                    } else {
                        callback.onSuccess(false);
                    }
                });
    }

    public void getClientsInQueue(Map<String, Object> clientsInQueue, Callback<List<Client>> callback) {
        List<Client> clients = new ArrayList<>();

        if (clientsInQueue.isEmpty()) {
            callback.onSuccess(clients);
            return;
        }

        mReal.getReference("QMobility")
                .child("Clients")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot clientSnapshot : snapshot.getChildren()) {
                            if (clientsInQueue.containsKey(clientSnapshot.getKey())) {
                                Client client = getClientDetails(clientSnapshot);
                                clients.add(client);
                            }
                        }
                        callback.onSuccess(clients);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private Client getClientDetails(DataSnapshot clientSnapshot) {
        String clientId = clientSnapshot.child("clientId").getValue(String.class);
        String clientName = clientSnapshot.child("clientName").getValue(String.class);
        String clientEmail = clientSnapshot.child("clientEmail").getValue(String.class);
        String clientPhoneNumber = clientSnapshot.child("clientPhoneNumber").getValue(String.class);
        String clientStatus = clientSnapshot.child("clientStatus").getValue(String.class);
        String businessId = clientSnapshot.child("businessId").getValue(String.class);
        String queueId = clientSnapshot.child("queueId").getValue(String.class);
        int assignedNumberInQueue = clientSnapshot.child("assignedNumberInQueue").getValue(Integer.class);
        String assignedCounter = clientSnapshot.child("assignedCounter").getValue(String.class);
        String queueEntryTime = clientSnapshot.child("queueEntryTime").getValue(String.class);
        String queueExitTime = clientSnapshot.child("queueExitTime").getValue(String.class);

        return new Client(clientId, clientName, clientEmail, clientPhoneNumber, clientStatus, businessId,
                queueId, assignedNumberInQueue, assignedCounter, queueEntryTime, queueExitTime);
    }

    public void updateClientsInFirebase(Client client, Callback<Boolean> callback) {
        mReal.getReference("QMobility")
                .child("Clients")
                .child(client.getClientId())
                .setValue(client)
                .addOnCompleteListener(task -> {
                   callback.onSuccess(task.isSuccessful());
                });
    }
}
