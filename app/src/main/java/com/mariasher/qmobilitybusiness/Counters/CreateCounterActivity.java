package com.mariasher.qmobilitybusiness.Counters;

import static com.mariasher.qmobilitybusiness.Utils.Adapters.ViewQueuesViewAdapter.QUEUE_ID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.database.Counter;
import com.mariasher.qmobilitybusiness.databinding.ActivityCreateCounterBinding;

import java.util.UUID;

public class CreateCounterActivity extends AppCompatActivity {

    private ActivityCreateCounterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String queueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCounterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        queueId = intent.getStringExtra(QUEUE_ID);

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

    }

    public void createCounterButtonClicked(View view) {
        String counterNumber = binding.counterNumberCreateEditText.getText().toString();

        if (counterNumber.isEmpty()) {
            setError(binding.counterNumberCreateEditText, "Counter Number cannot be empty");
            return;
        }

        firebaseRealtimeUtils.isCounterNumberAlreadyInDatabase(mAuth.getCurrentUser().getUid(), counterNumber, isCounterNumberAlreadyInDatabase -> {
            if (isCounterNumberAlreadyInDatabase) {
                setError(binding.counterNumberCreateEditText, "Counter Already Exists!");
                return;
            }

            String counterId = UUID.randomUUID().toString();
            Counter counter = new Counter(counterId, queueId, counterNumber);
            addCounterToFirebase(counter);

        });
    }

    private void addCounterToFirebase(Counter counter) {
        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            addCounterToCountersInFirebase(businessId, counter, isCounterAddedToCountersInFirebase -> {
                if (isCounterAddedToCountersInFirebase) {
                    addCounterToQueue(businessId, counter);
                }
            });
        });
    }

    private void addCounterToCountersInFirebase(String businessId, Counter counter, Callback<Boolean> callback) {
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
                        Toast.makeText(this, "Error creating counter", Toast.LENGTH_SHORT).show();
                        callback.onSuccess(false);
                    }
                });
    }


    private void addCounterToQueue(String businessId, Counter counter) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Queues")
                .child(counter.getQueueId())
                .child("activeCounterIds")
                .child(counter.getCounterId())
                .setValue(counter.getCounterNumber())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Counter Created Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error creating counter!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean setError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
        return false;
    }
}