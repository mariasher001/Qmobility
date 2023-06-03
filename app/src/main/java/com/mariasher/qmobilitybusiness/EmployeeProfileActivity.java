package com.mariasher.qmobilitybusiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
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
    private Employee employee;

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
        String CRUD = intent.getStringExtra("CRUD");

        if (CRUD.equals("UPDATE")) {
            binding.updateProfileImageView.setVisibility(View.VISIBLE);
        }

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getBusinessNameFromBusinessDetails(businessId, businessName -> {
            binding.businessNameEmployeeProfileHeaderTextView.setText(businessName);
        });

        getEmployeeDataFromFirebaseDatabase(employee -> {
            this.employee = employee;
            setEmployeeProfileDetails();
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
                            String name = snapshot.child("name").getValue(String.class);
                            String emailAddress = snapshot.child("emailAddress").getValue(String.class);
                            String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                            String businessId = snapshot.child("businessId").getValue(String.class);
                            String employeeId = snapshot.child("employeeId").getValue(String.class);
                            String accessType = snapshot.child("accessType").getValue(String.class);

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

    private void setEmployeeProfileDetails() {
        binding.employeeProfileNameEditText.setText(employee.getName());
        binding.employeeProfileEmailEditText.setText(employee.getEmailAddress());
        binding.employeeProfilePhoneNumberEditText.setText(employee.getPhoneNumber());
        binding.employeeProfileBusinessIdEditText.setText(employee.getBusinessId());
        binding.employeeProfileEmployeeIdEditText.setText(employee.getEmployeeId());
        binding.employeeProfileAccessTypeEditText.setText(employee.getAccessType());
    }

    public void updateProfileImageViewClicked(View view) {
        setEnabledAndColorForUpdateProfile(binding.employeeProfileNameEditText);
        setEnabledAndColorForUpdateProfile(binding.employeeProfilePhoneNumberEditText);
        setEnabledAndColorForUpdateProfile(binding.employeeProfileAccessTypeEditText);

        binding.updateProfileButton.setVisibility(View.VISIBLE);
    }

    public void updateProfileButtonClicked(View view) {
        String name = binding.employeeProfileNameEditText.getText().toString();
        String phoneNumber = binding.employeeProfilePhoneNumberEditText.getText().toString();
        String accessType = binding.employeeProfileAccessTypeEditText.getText().toString().toUpperCase();

        if (checkEmployeeInfo(name, phoneNumber, accessType)) {
            employee.setName(name);
            employee.setPhoneNumber(phoneNumber);
            employee.setAccessType(accessType);
            firebaseRealtimeUtils.addEmployeeToBusinessInRealtimeDatabase(employee, isSuccessful -> {
                if (isSuccessful) {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error Updating Profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean checkEmployeeInfo(String name, String phoneNumber, String accessType) {
        if (name.isEmpty())
            return setError(this.binding.employeeProfileNameEditText, "Name is Required!");
        if (phoneNumber.isEmpty())
            return setError(this.binding.employeeProfilePhoneNumberEditText, "Phone Number is required!");
        if (accessType.isEmpty())
            return setError(this.binding.employeeProfileAccessTypeEditText, "Access Type is required!");
        if ((!accessType.equals("ADMIN")) && (!accessType.equals("MANAGER")) && (!accessType.equals("OPERATOR")))
            return setError(binding.employeeProfileAccessTypeEditText, "Enter Correct Access Type");

        return true;
    }

    private boolean setError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
        return false;
    }

    private void setEnabledAndColorForUpdateProfile(EditText editText) {
        editText.setEnabled(true);
        editText.setTextColor(Color.WHITE);
    }
}