package com.mariasher.qmobilitybusiness.Queues;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.databinding.ActivityQueueMainBinding;

public class QueueMainActivity extends AppCompatActivity {

    private ActivityQueueMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQueueMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void createQueueButtonClicked(View view) {
    }

    public void viewQueueDetailsButtonClicked(View view) {
    }

    public void queueControlsButtonClicked(View view) {
    }

    public void queueAnalyticsButtonClicked(View view) {
    }
}