package com.mariasher.qmobilitybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mariasher.qmobilitybusiness.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void loginButtonClicked(View view) {

    }

    public void RegisterBusinessActivity(View view) {
        Intent registerBusinessIntent = new Intent(this, RegisterBusinessActivity.class);
        startActivity(registerBusinessIntent);
    }


}