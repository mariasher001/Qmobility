package com.mariasher.qmobilitybusiness.adminactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mariasher.qmobilitybusiness.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {
    private ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void manageEmployeesButtonClicked(View view) {
        Intent intent = new Intent(this, ManageEmployeesActivity.class);
        startActivity(intent);
    }

    //TODO: Change the class name from ManageEmployeeActivity to ManageQueueActivity
    public void manageQueuesButtonClicked(View view) {
//        Intent intent = new Intent(this, ManageEmployeesActivity.class);
//        startActivity(intent);
    }

    //TODO: Change the class name from ManageEmployeeActivity to ManageAnalyticsActivity
    public void viewAnalyticsButtonClicked(View view) {
//        Intent intent = new Intent(this, ManageEmployeesActivity.class);
//        startActivity(intent);
    }
}