package com.mariasher.qmobilitybusiness.Queues;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mariasher.qmobilitybusiness.databinding.ActivityQueueMainBinding;

public class QueueMainActivity extends AppCompatActivity {

    private ActivityQueueMainBinding binding;

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
    }

    public void queueControlsMainButtonClicked(View view) {
    }

    public void queueAnalyticsMainButtonClicked(View view) {
    }
}