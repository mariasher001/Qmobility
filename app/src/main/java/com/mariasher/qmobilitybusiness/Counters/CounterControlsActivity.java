package com.mariasher.qmobilitybusiness.Counters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mariasher.qmobilitybusiness.databinding.ActivityCounterControlsBinding;

public class CounterControlsActivity extends AppCompatActivity {

    private ActivityCounterControlsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCounterControlsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}