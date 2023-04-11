package com.mariasher.qmobilitybusiness.Queues;

import static com.mariasher.qmobilitybusiness.Utils.Adapters.ViewQueuesViewAdapter.QUEUE_ID;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.enums.QueueStatus;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.ActivityQueueControlsBinding;

public class QueueControlsActivity extends AppCompatActivity {

    private ActivityQueueControlsBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String queueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueueControlsBinding.inflate(getLayoutInflater());
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
        //TODO
    }


    public void pauseQueueControlsButtonClicked(View view) {
        //TODO
    }

    public void resetQueueControlsButtonClicked(View view) {
        //TODO
    }


    public void deleteQueueControlsButtonClicked(View view) {
        //TODO
    }

    public void queueDetailsControlsButtonClicked(View view) {
        Intent intent = new Intent(this, QueueDetailsActivity.class);
        intent.putExtra(QUEUE_ID, queueId);
        startActivity(intent);
    }

    public void queueAnalyticsControlsButtonClicked(View view) {
        //TODO
    }
}