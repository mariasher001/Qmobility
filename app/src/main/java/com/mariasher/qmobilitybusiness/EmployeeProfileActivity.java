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

    public void updateProfileImageViewClicked(View view) {
        //name, email, phone, access type enable, color
        //button visible
        setEnabledAndColorForUpdateProfile(binding.employeeProfileNameEditText);
        setEnabledAndColorForUpdateProfile(binding.employeeProfileEmailEditText);
        setEnabledAndColorForUpdateProfile(binding.employeeProfilePhoneNumberEditText);
        setEnabledAndColorForUpdateProfile(binding.employeeProfileAccessTypeEditText);

        binding.updateProfileButton.setVisibility(View.VISIBLE);
    }

    public void updateProfileButtonClicked(View view) {
        String name = binding.employeeProfileNameEditText.getText().toString();
        String emailAddress = binding.employeeProfileEmailEditText.getText().toString();
        String phoneNumber = binding.employeeProfilePhoneNumberEditText.getText().toString();
        String businessId = binding.employeeProfileBusinessIdEditText.getText().toString();
        String employeeId = binding.employeeProfileEmployeeIdEditText.getText().toString();
        String accessType = binding.employeeProfileAccessTypeEditText.getText().toString().toUpperCase();

        if (checkEmployeeInfo(name, emailAddress, phoneNumber, accessType)) {
            Employee employee = new Employee(employeeId, name, emailAddress, businessId, phoneNumber, accessType);
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

    private boolean checkEmployeeInfo(String name, String emailAddress, String phoneNumber, String accessType) {
        if (name.isEmpty())
            return setError(this.binding.employeeProfileNameEditText, "Name is Required!");
        if (emailAddress.isEmpty())
            return setError(this.binding.employeeProfileEmailEditText, "Email is Required!");
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
            return setError(this.binding.employeeProfileEmailEditText, "Enter correct email!");
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