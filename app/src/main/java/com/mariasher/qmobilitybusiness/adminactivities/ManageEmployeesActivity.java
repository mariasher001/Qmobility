package com.mariasher.qmobilitybusiness.adminactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.databinding.ActivityManageEmployeesBinding;

public class ManageEmployeesActivity extends AppCompatActivity {

    private ActivityManageEmployeesBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageEmployeesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            firebaseRealtimeUtils.getBusinessNameFromBusinessDetails(businessId, businessName -> {
                binding.businessNameManageEmployeesHeaderTextView.setText(businessName);
            });
        });

    }


    public void addEmployeeButtonClicked(View view) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        startActivity(intent);
    }

    public void readEmployeesDataButtonClicked(View view) {
        Intent intent = new Intent(this, ReadEmployeeDataActivity.class);
        intent.putExtra("CRUD", "READ");
        startActivity(intent);
    }

    public void updateEmployeeDataButtonClicked(View view) {
        Intent intent = new Intent(this, ReadEmployeeDataActivity.class);
        intent.putExtra("CRUD", "UPDATE");
        startActivity(intent);
    }

    public void deleteEmployeeButtonClicked(View view) {
        Intent intent = new Intent(this, ReadEmployeeDataActivity.class);
        intent.putExtra("CRUD", "DELETE");
        startActivity(intent);
    }
}