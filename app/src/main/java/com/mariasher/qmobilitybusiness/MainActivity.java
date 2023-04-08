package com.mariasher.qmobilitybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mariasher.qmobilitybusiness.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = new Intent(this, LoginActivity.class);

        Runnable runnable = () -> {
            startActivity(intent);
            finish();
        };

        binding.initialTextView.postDelayed(runnable, 1000);
    }

}