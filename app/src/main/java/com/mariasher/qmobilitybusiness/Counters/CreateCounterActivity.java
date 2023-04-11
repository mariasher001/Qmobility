package com.mariasher.qmobilitybusiness.Counters;

import static com.mariasher.qmobilitybusiness.Utils.Adapters.ViewQueuesViewAdapter.QUEUE_ID;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mariasher.qmobilitybusiness.databinding.ActivityCreateCounterBinding;

public class CreateCounterActivity extends AppCompatActivity {

    private ActivityCreateCounterBinding binding;
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

    }

    public void createCounterButtonClicked(View view) {
        //TODO
    }
}