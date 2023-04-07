package com.mariasher.qmobilitybusiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.database.Employee;
import com.mariasher.qmobilitybusiness.databinding.ActivityEmployeeProfileBinding;

import java.util.Map;

public class EmployeeProfileActivity extends AppCompatActivity {

    private ActivityEmployeeProfileBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String businessId;
    private String employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        businessId = intent.getStringExtra("businessId");
        employeeId = intent.getStringExtra("employeeId");

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getBusinessNameFromBusinessDetails(businessId, businessName -> {
            binding.businessNameEmployeeProfileHeaderTextView.setText(businessName);
        });

        getEmployeeDataFromFirebaseDatabase(employee -> {
            binding.employeeProfileNameEditText.setText(employee.getName());
            binding.employeeProfileEmailEditText.setText(employee.getEmailAddress());
            binding.employeeProfilePhoneNumberEditText.setText(employee.getPhoneNumber());
            binding.employeeProfileBusinessIdEditText.setText(employee.getBusinessId());
            binding.employeeProfileEmployeeIdEditText.setText(employee.getEmployeeId());
            binding.employeeProfileAccessTypeEditText.setText(employee.getAccessType());
        });
    }

    private void getEmployeeDataFromFirebaseDatabase(Callback<Employee> callback) {
        if (businessId != null && employeeId != null && !businessId.isEmpty() && !employeeId.isEmpty()) {
            mReal.getReference("QMobility")
                    .child("Businesses")
                    .child(businessId)
                    .child("EmployeeDetails")
                    .child(employeeId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, Object> employeeMap = (Map<String, Object>) snapshot.getValue();
                            String name = (String) employeeMap.get("name");
                            String emailAddress = (String) employeeMap.get("emailAddress");
                            String phoneNumber = (String) employeeMap.get("phoneNumber");
                            String businessId = (String) employeeMap.get("businessId");
                            String employeeId = (String) employeeMap.get("employeeId");
                            String accessType = (String) employeeMap.get("accessType");

                            Employee employee = new Employee(employeeId, name, emailAddress, businessId, phoneNumber, accessType);
                            callback.onSuccess(employee);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } else {
            Toast.makeText(this, "Intent Data Transfer incomplete", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}