package com.mariasher.qmobilitybusiness.adminactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariasher.qmobilitybusiness.database.BusinessInfo;
import com.mariasher.qmobilitybusiness.database.Employee;
import com.mariasher.qmobilitybusiness.databinding.ActivityAddEmployeeBinding;

public class AddEmployeeActivity extends AppCompatActivity {
    ActivityAddEmployeeBinding binding;
    Employee employee;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    BusinessInfo businessInfo = new BusinessInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        getBusinessInfoFromFirebase();
        setEmployeeBusinessID();
    }

    private void getBusinessInfoFromFirebase() {
        mReal.getReference("QMobility")
                .child("Business")
                .child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        businessInfo = snapshot.getValue(BusinessInfo.class);
                        setEmployeeBusinessID();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddEmployeeActivity.this, "Error retrieving Business Info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEmployeeBusinessID() {
        getBusinessIdFromFirebase();
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessInfo.getBusinessID())
                .child("EmployeeDetails")
                .child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String adminBusinessID = snapshot.getValue(String.class);
                            binding.employeeBusinessIdEditText.setText(adminBusinessID);
                        } else
                            Toast.makeText(AddEmployeeActivity.this, "Business ID not found!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddEmployeeActivity.this, "Error retrieving business ID: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getBusinessIdFromFirebase() {

    }


    public void registerEmployeeButtonClicked(View view) {
        setEmployeeData();
    }

    private void setEmployeeData() {
        String businessID = binding.employeeBusinessIdEditText.getText().toString();
        //String employeeID = binding.employeeBusinessIdEditText.getText().toString();
        String employeeName = binding.employeeNameEditText.getText().toString();
        String employeeEmail = binding.employeeEmailEditText.getText().toString();
        String employeePassword = binding.employeePasswordEditText.getText().toString();
        String employeePhoneNumber = binding.employeePhonenumberEditText.getText().toString();
        String employeeAccessType = binding.employeeAccessTypeEditText.getText().toString();

        employee = new Employee("", employeeName, employeeEmail, businessID, employeePhoneNumber, employeeAccessType);
        if (checkEmployeeDetails(employeeName, employeeEmail, employeePassword, employeePhoneNumber, employeeAccessType)) {
            registerEmployeeWithFirebaseAuth(businessInfo, employee, employeePassword);
        }

    }

    private boolean checkEmployeeDetails(String employeeName, String employeeEmail, String employeePassword, String employeePhoneNumber, String employeeAccessType) {
        if (employeeName.isEmpty())
            return setError(this.binding.employeeNameEditText, "Employee Name is Required!");
        if (employeeEmail.isEmpty())
            return setError(this.binding.employeeEmailEditText, "Employee Email is Required!");
        if (!Patterns.EMAIL_ADDRESS.matcher(employeeEmail).matches())
            return setError(this.binding.employeeEmailEditText, "OOOPs! Please Enter correct email!");
        if (employeePassword.isEmpty())
            return setError(this.binding.employeePasswordEditText, "Password is required!");
        if (employeePassword.length() < 6)
            return setError(this.binding.employeePasswordEditText, "Password length should be more than 6");
        if (employeePhoneNumber.isEmpty())
            return setError(this.binding.employeePhonenumberEditText, "Employee PhoneNumber is Required!");
        if (employeeAccessType.isEmpty())
            return setError(this.binding.employeeAccessTypeEditText, "Employee AccessType is Required! (Admin,Manager,Operator)");

        return true;
    }


    private boolean setError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
        return false;
    }

    private void registerEmployeeWithFirebaseAuth(BusinessInfo businessInfo, Employee employee, String password) {
        mAuth.createUserWithEmailAndPassword(employee.getEmailAddress(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String employeeId = businessInfo.getBusinessName() + "@" + task.getResult().getUser().getUid();
                        employee.setEmployeeId(employeeId);
                        createRealtimeDataBase(businessInfo, employee);
                        mAuth.signOut();
                    } else {
                        Toast.makeText(this, "FirebaseAuth Registration Unsuccessful!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createRealtimeDataBase(BusinessInfo businessInfo, Employee employee) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessInfo.getBusinessID())
                .child("EmployeeDetails")
                .child(employee.getEmployeeId())
                .setValue(employee)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show();
                        ChangeActivity();
                    } else {
                        Toast.makeText(this, "Firebase RealTime Registration Unsuccessful!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void ChangeActivity() {
        Intent intent = new Intent(this, ManageEmployeesActivity.class);
        startActivity(intent);
    }
}