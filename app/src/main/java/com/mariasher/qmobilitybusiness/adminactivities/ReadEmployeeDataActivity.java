package com.mariasher.qmobilitybusiness.adminactivities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariasher.qmobilitybusiness.Utils.Adapters.EmployeeDataViewAdapter;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.database.Employee;
import com.mariasher.qmobilitybusiness.databinding.ActivityReadEmployeeDataBinding;

import java.util.ArrayList;
import java.util.List;

public class ReadEmployeeDataActivity extends AppCompatActivity {

    private ActivityReadEmployeeDataBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadEmployeeDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String CRUD = intent.getStringExtra("CRUD");
        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);
        setHeader();

        getAllEmployeesFromBusiness(employees -> {
            binding.readEmployeeDataRecyclerView.setAdapter(new EmployeeDataViewAdapter(employees, this, CRUD));
        });
    }

    private void setHeader() {
        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            firebaseRealtimeUtils.getBusinessNameFromBusinessDetails(businessId, businessName -> {
                binding.businessNameReadEmployeesHeaderTextView.setText(businessName);
            });
        });
    }

    private void getAllEmployeesFromBusiness(Callback<List<Employee>> callback) {
        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            mReal.getReference("QMobility")
                    .child("Businesses")
                    .child(businessId)
                    .child("EmployeeDetails")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<Employee> employees = new ArrayList<>();

                            for (DataSnapshot employeeSnapshot : snapshot.getChildren()) {
                                String employeeId = employeeSnapshot.getKey();
                                String businessId = employeeSnapshot.child("businessId").getValue(String.class);
                                String name = employeeSnapshot.child("name").getValue(String.class);
                                String email = employeeSnapshot.child("emailAddress").getValue(String.class);
                                String phoneNumber = employeeSnapshot.child("phoneNumber").getValue(String.class);
                                String accessType = employeeSnapshot.child("accessType").getValue(String.class);

                                Employee employee = new Employee(employeeId, name, email, businessId, phoneNumber, accessType);
                                employees.add(employee);
                            }

                            callback.onSuccess(employees);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });

    }
}