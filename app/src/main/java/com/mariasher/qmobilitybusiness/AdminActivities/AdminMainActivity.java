package com.mariasher.qmobilitybusiness.AdminActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mariasher.qmobilitybusiness.LoginActivity;
import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {
    private ActivityAdminMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            firebaseRealtimeUtils.getBusinessNameFromBusinessDetails(businessId, businessName -> {
                binding.businessNameAdminMainHeaderTextView.setText(businessName);
            });
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_admin_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void logoutAdminItemClicked(@NonNull MenuItem item) {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}