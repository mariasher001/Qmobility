package com.mariasher.qmobilitybusiness.Counters;

import static com.mariasher.qmobilitybusiness.Utils.Adapters.ViewCountersViewAdapter.COUNTER_ID;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.databinding.ActivityCounterDetailsBinding;

public class CounterDetailsActivity extends AppCompatActivity {

    private ActivityCounterDetailsBinding binding;
    private String counterId;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCounterDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        counterId = intent.getStringExtra(COUNTER_ID);

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        setCounterDetailsInView();
    }

    private void setCounterDetailsInView() {
        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            this.businessId = businessId;

            firebaseRealtimeUtils.getCounterDetailsFromFirebase(businessId, counterId, counter -> {
                firebaseRealtimeUtils.getQueueDataFromFirebaseWithBusinessId(businessId, counter.getQueueId(), queue -> {
                    binding.counterNumberCounterDetailsTextView.setText(counter.getCounterNumber());
                    binding.queueNameCounterDetailsTextView.setText(queue.getQueueName());
                    binding.counterStatusCounterDetailsTextView2.setText(counter.getCounterStatus());
                    binding.customerNumberOnCallCounterDetailsTextView.setText("" + counter.getCustomerNumberOnCall());
                    binding.nextNumberOnCallCounterDetailsTextView.setText("" + counter.getNextNumberOnCall());
                    binding.averageCustomerTimeCounterDetailsTextView.setText(counter.getAverageCustomerTime());
                });
            });
        });
    }
}