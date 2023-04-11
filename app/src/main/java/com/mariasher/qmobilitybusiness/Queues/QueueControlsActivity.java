package com.mariasher.qmobilitybusiness.Queues;

import static com.mariasher.qmobilitybusiness.Utils.Adapters.ViewQueuesViewAdapter.QUEUE_ID;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.Utils.DateTimeUtils;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.enums.QueueStatus;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.ActivityQueueControlsBinding;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class QueueControlsActivity extends AppCompatActivity {

    private ActivityQueueControlsBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String queueId;
    private String businessId;
    private Queue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueueControlsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        queueId = intent.getStringExtra(QUEUE_ID);

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        String Uid = mAuth.getCurrentUser().getUid();
        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(Uid, businessId -> {
            this.businessId = businessId;
        });

        firebaseRealtimeUtils.getQueueDataFromFirebaseDatabase(Uid, queueId, queue -> {
            this.queue = queue;
            setViewValues(queue);
        });
    }

    private void setViewValues(Queue queue) {
        binding.queueNameControlsHeaderTextView.setText(queue.getQueueName());
        binding.queueStatusControlsTextView.setText(queue.getQueueStatus());
        setButtonsAndStatus(queue.getQueueStatus());
    }

    private void setButtonsAndStatus(String queueStatus) {
        switch (QueueStatus.valueOf(queueStatus)) {
            case ACTIVE:
                binding.startQueueControlsButton.setEnabled(false);
                binding.pauseQueueControlsButton.setEnabled(true);
                binding.resetQueueControlsButton.setEnabled(true);
                binding.queueStatusControlsTextView.setTextColor(Color.GREEN);
                break;
            case INACTIVE:
                binding.startQueueControlsButton.setEnabled(true);
                binding.pauseQueueControlsButton.setEnabled(false);
                binding.resetQueueControlsButton.setEnabled(false);
                binding.queueStatusControlsTextView.setTextColor(Color.RED);
                break;
            case PAUSED:
                binding.startQueueControlsButton.setEnabled(true);
                binding.pauseQueueControlsButton.setEnabled(false);
                binding.resetQueueControlsButton.setEnabled(true);
                binding.queueStatusControlsTextView.setTextColor(Color.YELLOW);
                break;
            default:
                binding.startQueueControlsButton.setEnabled(false);
                binding.pauseQueueControlsButton.setEnabled(false);
                binding.resetQueueControlsButton.setEnabled(false);
        }
    }

    public void startQueueControlsButtonClicked(View view) {

        if(queue.getQueueStatus().equals(QueueStatus.INACTIVE.toString())) {
            queue.setQueueStartTime(DateTimeUtils.convertDateAndTimeToString(LocalDateTime.now()));
        }
        queue.setQueueEndTime("");

        queue.setQueueStatus(QueueStatus.ACTIVE.toString());
        updateQueueInFirebase("Queue Started");
    }


    public void pauseQueueControlsButtonClicked(View view) {
        queue.setQueueStatus(QueueStatus.PAUSED.toString());
        updateQueueInFirebase("Queue Paused");
    }

    public void resetQueueControlsButtonClicked(View view) {
        queue.setQueueStatus(QueueStatus.INACTIVE.toString());
        queue.setQueueEndTime(DateTimeUtils.convertDateAndTimeToString(LocalDateTime.now()));
        updateQueueInFirebase("Queue Reset Successful");
    }


    public void deleteQueueControlsButtonClicked(View view) {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Are you sure you want to delete " + queue.getQueueName() + " queue?")
                .setPositiveButton("YES", (dialog, i) -> {
                    deleteQueueFromFirebase();
                })
                .setNegativeButton("NO", (dialog, i) -> {
                    dialog.dismiss();
                }).show();
    }

    public void queueDetailsControlsButtonClicked(View view) {
        Intent intent = new Intent(this, QueueDetailsActivity.class);
        intent.putExtra(QUEUE_ID, queueId);
        startActivity(intent);
    }

    public void queueAnalyticsControlsButtonClicked(View view) {
        //TODO
    }

    private void updateQueueInFirebase(String queueToast) {
        Map<String, Object> queueMap = new HashMap<>();
        queueMap.put("queueId", queue.getQueueId());
        queueMap.put("creatorId", queue.getCreatorId());
        queueMap.put("queueName", queue.getQueueName());
        queueMap.put("queueStartTime", queue.getQueueStartTime());
        queueMap.put("queueEndTime", queue.getQueueEndTime());
        queueMap.put("queueStatus", queue.getQueueStatus());
        queueMap.put("numberOfActiveCounters", queue.getNumberOfActiveCounters());
        queueMap.put("averageCustomerTime", queue.getAverageCustomerTime());

        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Queues")
                .child(queueId)
                .setValue(queueMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, queueToast, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Unable to update queue", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteQueueFromFirebase() {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Queues")
                .child(queueId)
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Queue deleted successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error deleting the queue!", Toast.LENGTH_SHORT).show();
                    }
                });

        finish();
    }
}