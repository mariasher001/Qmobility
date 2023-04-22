package com.mariasher.qmobilitybusiness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.AdminActivities.AdminMainActivity;
import com.mariasher.qmobilitybusiness.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    public static final String ACCESS_TYPE = "ACCESS_TYPE";
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();

        if (mAuth.getCurrentUser() != null) {
            loginUserWithAccessType(mAuth.getCurrentUser().getUid());
        }
    }

    //ToDO: Figure out the employee type and change intent accordingly.
    public void loginButtonClicked(View view) {
        String email = binding.emailAddressLoginEditText.getText().toString();
        String password = binding.passwordLoginEditText.getText().toString();
        if (checkLoginDetails(email, password)) {
            loginUserWithFirebaseAuth(email, password);
        }
    }

    private void loginUserWithFirebaseAuth(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String employeeId = task.getResult().getUser().getUid();
                        loginUserWithAccessType(employeeId);
                    } else {
                        Toast.makeText(this, "Failed to Login!! Please Check Again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginUserWithAccessType(String employeeId) {
        getBusinessIdFromRealtimeDatabase(employeeId, businessId -> {
            getAccessTypeFromRealtimeDatabase(businessId, employeeId, accessType -> {
                login(accessType);
            });
        });
    }

    private void getBusinessIdFromRealtimeDatabase(String employeeId, Callback<String> callback) {
        mReal.getReference("QMobility")
                .child("EmployeeBusinessLink")
                .child(employeeId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String businessId = snapshot.getValue(String.class);
                        callback.onSuccess(businessId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void getAccessTypeFromRealtimeDatabase(String businessId, String employeeId, Callback<String> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("EmployeeDetails")
                .child(employeeId)
                .child("accessType")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String accessType = snapshot.getValue(String.class);
                        callback.onSuccess(accessType);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void login(String accessType) {
        Intent intent = new Intent(this, AdminMainActivity.class);
        intent.putExtra(ACCESS_TYPE, accessType);
        startActivity(intent);
        finish();
    }

    public void RegisterBusinessActivity(View view) {
        Intent registerBusinessIntent = new Intent(this, RegisterBusinessActivity.class);
        startActivity(registerBusinessIntent);
        finish();
    }

    public boolean checkLoginDetails(String email, String password) {
        if (email.isEmpty())
            return setError(binding.emailAddressLoginEditText, "Admin Email is Required!");
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return setError(this.binding.emailAddressLoginEditText, "Enter correct email!");
        if (password.isEmpty())
            return setError(this.binding.passwordLoginEditText, "Password is required!");

        return true;
    }

    private boolean setError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
        return false;
    }

}