package com.mariasher.qmobilitybusiness.Queues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.ActivityQueueDetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class QueueDetailsActivity extends AppCompatActivity {

    private ActivityQueueDetailsBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String queueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueueDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        queueId = intent.getStringExtra("queueId");

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getQueueDataFromFirebaseDatabase(mAuth.getCurrentUser().getUid(), queueId, queue -> {
            setViewValues(queue);
        });
    }

    private void setViewValues(Queue queue) {
        getCreatorName(queue, creatorName -> {
            binding.creatorNameQueueDetailsTextView.setText(creatorName);
        });

        binding.queueNameQueueDetailsTextView.setText(queue.getQueueName());
        binding.queueStartTimeQueueDetailsTextView.setText(queue.getQueueStartTime());
        binding.queueEndTimeQueueDetailsTextView.setText(queue.getQueueEndTime());
        binding.queueStatusQueueDetailsTextView.setText(queue.getQueueStatus());
        binding.numberOfActiveCountersQueueDetailsTextView.setText("" + queue.getNumberOfActiveCounters());
        binding.averageCustomerTimeQueueDetailsTextView.setText(queue.getAverageCustomerTime());
    }

    private void getCreatorName(Queue queue, Callback<String> callback) {
        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(queue.getCreatorId(), businessId -> {
            mReal.getReference("QMobility")
                    .child("Businesses")
                    .child(businessId)
                    .child("EmployeeDetails")
                    .child(queue.getCreatorId())
                    .child("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.getValue(String.class);
                            callback.onSuccess(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });
    }
}