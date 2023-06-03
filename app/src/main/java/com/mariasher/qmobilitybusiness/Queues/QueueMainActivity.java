package com.mariasher.qmobilitybusiness.Queues;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mariasher.qmobilitybusiness.databinding.ActivityQueueMainBinding;

public class QueueMainActivity extends AppCompatActivity {

    private ActivityQueueMainBinding binding;
    public static final String QUEUE_OPERATIONS = "QUEUE_OPERATIONS";
    public static final String VIEW_QUEUE_DETAILS = "ViewQueueDetails";
    public static final String QUEUE_CONTROLS = "QueueControls";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueueMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void createQueueMainButtonClicked(View view) {
        Intent intent = new Intent(this, CreateQueueActivity.class);
        startActivity(intent);
    }

    public void viewQueueDetailsMainButtonClicked(View view) {
        Intent intent = new Intent(this, ViewQueuesActivity.class);
        intent.putExtra(QUEUE_OPERATIONS, VIEW_QUEUE_DETAILS);
        startActivity(intent);
    }

    public void queueControlsMainButtonClicked(View view) {
        Intent intent = new Intent(this, ViewQueuesActivity.class);
        intent.putExtra(QUEUE_OPERATIONS, QUEUE_CONTROLS);
        startActivity(intent);
    }

    public void queueAnalyticsMainButtonClicked(View view) {
    }
}