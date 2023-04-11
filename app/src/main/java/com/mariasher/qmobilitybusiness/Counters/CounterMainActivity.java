package com.mariasher.qmobilitybusiness.Counters;

import static com.mariasher.qmobilitybusiness.Queues.QueueMainActivity.QUEUE_OPERATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mariasher.qmobilitybusiness.Queues.ViewQueuesActivity;
import com.mariasher.qmobilitybusiness.databinding.ActivityCounterMainBinding;

public class CounterMainActivity extends AppCompatActivity {

    public static final String CREATE_COUNTER = "CreateCounter";
    private ActivityCounterMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCounterMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void createCounterMainButtonClicked(View view) {
        Intent intent = new Intent(this, ViewQueuesActivity.class);
        intent.putExtra(QUEUE_OPERATIONS, CREATE_COUNTER);
        startActivity(intent);
    }

    public void viewCounterDetailsMainButtonClicked(View view) {
        //TODO
    }

    public void counterControlsMainButtonClicked(View view) {
        //TODO
    }

    public void counterAnalyticsMainButtonClicked(View view) {
        //TODO
    }
}