package com.mariasher.qmobilitybusiness.Counters;

import static com.mariasher.qmobilitybusiness.Utils.Adapters.ViewCountersViewAdapter.COUNTER_ID;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.Utils.enums.CounterStatus;
import com.mariasher.qmobilitybusiness.Utils.enums.QueueStatus;
import com.mariasher.qmobilitybusiness.database.Counter;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.ActivityCounterControlsBinding;

public class CounterControlsActivity extends AppCompatActivity {

    private ActivityCounterControlsBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String counterId;
    private String businessId;
    private Queue queue;
    private Counter counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCounterControlsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        counterId = intent.getStringExtra(COUNTER_ID);

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        String Uid = mAuth.getCurrentUser().getUid();

        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(Uid, businessId -> {
            this.businessId = businessId;

            firebaseRealtimeUtils.getCounterDetailsFromFirebase(this.businessId, counterId, counter -> {
                this.counter = counter;
                firebaseRealtimeUtils.getQueueDataFromFirebaseDatabaseWithBusinessId(this.businessId, this.counter.getQueueId(), queue -> {
                    this.queue = queue;

                    setViewValues();
                });
            });
        });
    }

    private void setViewValues() {
        binding.queueNameCounterControlsTextView.setText(queue.getQueueName());
        binding.counterNumberCounterControlsTextView.setText(counter.getCounterNumber());
        binding.counterStatusCounterControlsTextView.setText(counter.getCounterStatus());
        binding.customerNumberOnCallCounterControlsTextView.setText(String.valueOf(counter.getCustomerNumberOnCall()));
        binding.nextNumberOnCallCounterControlsTextView.setText(String.valueOf(counter.getNextNumberOnCall()));

        setButtonsAndStatus(counter.getCounterStatus());
    }

    private void setButtonsAndStatus(String counterStatus) {
        binding.startCounterControlsButton.setEnabled(false);
        binding.pauseCounterControlsButton.setEnabled(false);
        binding.resetCounterControlsButton.setEnabled(false);
        binding.nextCustomerCounterControlsButton.setEnabled(false);
        binding.skipCustomerCounterControlsButton.setEnabled(false);

        switch (CounterStatus.valueOf(counterStatus)) {
            case ACTIVE:
                binding.pauseCounterControlsButton.setEnabled(true);
                binding.resetCounterControlsButton.setEnabled(true);
                binding.nextCustomerCounterControlsButton.setEnabled(true);
                binding.skipCustomerCounterControlsButton.setEnabled(true);
                binding.counterStatusCounterControlsTextView.setTextColor(Color.GREEN);
                break;
            case INACTIVE:
                if (queue.getQueueStatus().equals(QueueStatus.ACTIVE.toString())) {
                    binding.startCounterControlsButton.setEnabled(true);
                }
                binding.counterStatusCounterControlsTextView.setTextColor(Color.RED);
                break;
            case PAUSED:
                if (queue.getQueueStatus().equals(QueueStatus.ACTIVE.toString())) {
                    binding.startCounterControlsButton.setEnabled(true);
                }
                binding.resetCounterControlsButton.setEnabled(true);
                binding.counterStatusCounterControlsTextView.setTextColor(Color.YELLOW);
                break;
        }
    }

    public void startCounterControlsButtonClicked(View view) {
        counter.setCounterStatus(CounterStatus.ACTIVE.toString());
        firebaseRealtimeUtils.updateCounterInFirebase(businessId, counter, isCounterUpdated -> {
            if (isCounterUpdated) {
                Toast.makeText(this, "Counter updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating counter!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pauseCounterControlsButtonClicked(View view) {
        counter.setCounterStatus(CounterStatus.PAUSED.toString());
        firebaseRealtimeUtils.updateCounterInFirebase(businessId, counter, isCounterUpdated -> {
            if (isCounterUpdated) {
                Toast.makeText(this, "Counter updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating counter!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void resetCounterControlsButtonClicked(View view) {
        counter.setCounterStatus(CounterStatus.INACTIVE.toString());
        counter.setAverageCustomerTime("");
        counter.setCustomerNumberOnCall(0);
        counter.setNextNumberOnCall(0);

        firebaseRealtimeUtils.updateCounterInFirebase(businessId, counter, isCounterUpdated -> {
            if (isCounterUpdated) {
                Toast.makeText(this, "Counter updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating counter!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteCounterControlsButtonClicked(View view) {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Are you sure you want to delete " + counter.getCounterNumber() + " counter?")
                .setPositiveButton("YES", (dialog, i) -> {
                    deleteCounterFromFirebase(isCounterDeleted -> {
                        if (isCounterDeleted) {
                            deleteCounterFromQueue();
                        } else {
                            Toast.makeText(this, "Couldn't delete the counter!", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("NO", (dialog, i) -> {
                    dialog.dismiss();
                }).show();
    }

    public void nextCustomerCounterControlsButtonClicked(View view) {
        //TODO WHEN YOU ADD CLIENTS TO QUEUE
    }

    public void skipCustomerCounterControlsButtonClicked(View view) {
        //TODO WHEN YOU ADD CLIENTS TO QUEUE
    }

    public void counterDetailsCounterControlsButtonClicked(View view) {
        Intent intent = new Intent(this, CounterDetailsActivity.class);
        intent.putExtra(COUNTER_ID, counterId);
        startActivity(intent);
    }

    public void counterAnalyticsCounterControlsButtonClicked(View view) {
        //TODO WHEN YOU ADD ANALYTICS
    }

    private void deleteCounterFromFirebase(Callback<Boolean> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Counters")
                .child(counter.getCounterId())
                .removeValue()
                .addOnCompleteListener(task -> callback.onSuccess(task.isSuccessful()));

    }

    private void deleteCounterFromQueue() {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Queues")
                .child(queue.getQueueId())
                .child("queueCounters")
                .child(counter.getCounterId())
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Counter Deleted successfully!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(this, "Couldn't delete the counter!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}