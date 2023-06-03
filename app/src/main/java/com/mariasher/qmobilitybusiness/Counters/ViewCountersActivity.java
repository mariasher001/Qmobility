package com.mariasher.qmobilitybusiness.Counters;

import static com.mariasher.qmobilitybusiness.Counters.CounterMainActivity.COUNTER_OPERATIONS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariasher.qmobilitybusiness.Utils.Adapters.ViewCountersViewAdapter;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.database.Counter;
import com.mariasher.qmobilitybusiness.databinding.ActivityViewCountersBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCountersActivity extends AppCompatActivity {

    private ActivityViewCountersBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String counterOperations;
    private String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCountersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        counterOperations = intent.getStringExtra(COUNTER_OPERATIONS);
        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            this.businessId = businessId;
            firebaseRealtimeUtils.getAllCounters(businessId, counters -> {
                getQueueNamesFromQueueIdsFromFirebase(counters, queueNames -> {
                    binding.viewCountersRecyclerView.setAdapter(new ViewCountersViewAdapter(counters, queueNames, this, counterOperations));
                });
            });
        });
    }

    private void getQueueNamesFromQueueIdsFromFirebase(List<Counter> counters, Callback<List<String>> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Queues")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, String> queueIdToNameMap = new HashMap<>();
                        for (DataSnapshot queueSnapshot : snapshot.getChildren()) {
                            String queueId = queueSnapshot.child("queueId").getValue(String.class);
                            String queueName = queueSnapshot.child("queueName").getValue(String.class);
                            queueIdToNameMap.put(queueId, queueName);
                        }
                        List<String> queueNames = new ArrayList<>();
                        for (Counter counter : counters) {
                            String queueName = queueIdToNameMap.get(counter.getQueueId());
                            if (queueName != null) {
                                queueNames.add(queueName);
                            }
                        }
                        callback.onSuccess(queueNames);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}