package com.mariasher.qmobilitybusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mariasher.qmobilitybusiness.adminactivities.ManageEmployeesActivity;
import com.mariasher.qmobilitybusiness.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
//ToDO: Figure out the employee type and change intent accordingly.
    public void loginButtonClicked(View view) {
        Intent intent = new Intent(this, ManageEmployeesActivity.class);
        startActivity(intent);

    }

    public void RegisterBusinessActivity(View view) {
        Intent registerBusinessIntent = new Intent(this, RegisterBusinessActivity.class);
        startActivity(registerBusinessIntent);
        finish();
    }


}