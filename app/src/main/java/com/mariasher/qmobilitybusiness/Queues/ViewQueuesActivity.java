package com.mariasher.qmobilitybusiness.Queues;

import static com.mariasher.qmobilitybusiness.Queues.QueueMainActivity.QUEUE_OPERATIONS;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariasher.qmobilitybusiness.Utils.Adapters.ViewQueuesViewAdapter;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.ActivityViewQueuesBinding;

import java.util.ArrayList;
import java.util.List;

public class ViewQueuesActivity extends AppCompatActivity {

    private ActivityViewQueuesBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewQueuesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String queueOperations = intent.getStringExtra(QUEUE_OPERATIONS);

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        getQueuesFromFirebaseDatabase(queues -> {
            binding.viewQueuesRecyclerView.setAdapter(new ViewQueuesViewAdapter(queues, this, queueOperations));
        });
    }

    private void getQueuesFromFirebaseDatabase(Callback<List<Queue>> callback) {
        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            mReal.getReference("QMobility")
                    .child("Businesses")
                    .child(businessId)
                    .child("Queues")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Queue> queues = new ArrayList<>();
                            for (DataSnapshot queueSnapshot : snapshot.getChildren()) {
                                String queueId = queueSnapshot.child("queueId").getValue(String.class);
                                String creatorId = queueSnapshot.child("creatorId").getValue(String.class);
                                String queueName = queueSnapshot.child("queueName").getValue(String.class);
                                String queueStartTime = queueSnapshot.child("queueStartTime").getValue(String.class);
                                String queueEndTime = queueSnapshot.child("queueEndTime").getValue(String.class);
                                String queueStatus = queueSnapshot.child("queueStatus").getValue(String.class);
                                Integer numberOfActiveCounters = queueSnapshot.child("numberOfActiveCounters").getValue(Integer.class);
                                String averageCustomerTime = queueSnapshot.child("averageCustomerTime").getValue(String.class);

                                List<String> activeCounterIds = new ArrayList<>();
                                for (DataSnapshot activeCounterSnapshot : queueSnapshot.child("activeCounterIds").getChildren()) {
                                    String activeCounterId = activeCounterSnapshot.getKey();
                                    String counterNumber = activeCounterSnapshot.getValue(String.class);
                                    activeCounterIds.add(activeCounterId);
                                }

                                Queue queue = new Queue(queueId, creatorId, queueName, queueStartTime, queueEndTime, queueStatus,
                                        numberOfActiveCounters, averageCustomerTime, activeCounterIds);

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
}