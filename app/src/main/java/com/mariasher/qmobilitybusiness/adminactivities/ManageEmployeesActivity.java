package com.mariasher.qmobilitybusiness.adminactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.databinding.ActivityManageEmployeesBinding;

public class ManageEmployeesActivity extends AppCompatActivity {

    private ActivityManageEmployeesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageEmployeesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    public void addEmployeeButtonClicked(View view) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        startActivity(intent);
    }

    public void readEmployeesDataButtonClicked(View view) {
        Intent intent = new Intent(this, ReadEmployeeDataActivity.class);
        startActivity(intent);
    }

    public void updateEmployeeDataButtonClicked(View view) {
        Intent intent = new Intent(this, UpdateEmployeeDataActivity.class);
        startActivity(intent);
    }

    public void deleteEmployeeButtonClicked(View view) {
        Intent intent = new Intent(this, DeleteEmployeeActivity.class);
        startActivity(intent);
    }
}