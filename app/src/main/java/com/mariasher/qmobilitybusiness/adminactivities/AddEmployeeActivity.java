package com.mariasher.qmobilitybusiness.adminactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.mariasher.qmobilitybusiness.databinding.ActivityAddEmployeeBinding;

public class AddEmployeeActivity extends AppCompatActivity {
    private ActivityAddEmployeeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            firebaseRealtimeUtils.getBusinessNameFromBusinessDetails(businessId, businessName -> {
                binding.businessNameAddEmployeesHeaderTextView.setText(businessName);
                binding.employeeBusinessIdEditText.setText(businessId);
            });
        });

    }

    public void registerEmployeeButtonClicked(View view) {
        setEmployeeData();
    }

    private void setEmployeeData() {
        String businessID = binding.employeeBusinessIdEditText.getText().toString();
        String employeeName = binding.employeeNameEditText.getText().toString();
        String employeeEmail = binding.employeeEmailEditText.getText().toString();
        String employeePassword = binding.employeePasswordEditText.getText().toString();
        String employeePhoneNumber = binding.employeePhonenumberEditText.getText().toString();
        String employeeAccessType = binding.employeeAccessTypeEditText.getText().toString();

        if (checkEmployeeDetails(employeeName, employeeEmail, employeePassword, employeePhoneNumber, employeeAccessType)) {
            Employee employee = new Employee("", employeeName, employeeEmail, businessID, employeePhoneNumber, employeeAccessType);
            registerEmployeeWithFirebaseAuth(employee, employeePassword);
        }

    }

    private void registerEmployeeWithFirebaseAuth(Employee employee, String password) {
        mAuth.createUserWithEmailAndPassword(employee.getEmailAddress(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String employeeId = task.getResult().getUser().getUid();
                        employee.setEmployeeId(employeeId);
                        firebaseRealtimeUtils.createEmployeeBusinessLinkInRealtimeDatabase(employee.getEmployeeId(), employee.getBusinessId());
                        firebaseRealtimeUtils.addEmployeeToBusinessInRealtimeDatabase(employee, isSuccessful -> {
                            if (isSuccessful)
                                changeActivity();
                        });
                        mAuth.signOut();
                    } else {
                        Toast.makeText(this, "FirebaseAuth Registration Unsuccessful!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void changeActivity() {
        Intent intent = new Intent(this, ManageEmployeesActivity.class);
        startActivity(intent);
        finish();
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
}