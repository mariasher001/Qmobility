package com.mariasher.qmobilitybusiness.AdminActivities;

import static com.mariasher.qmobilitybusiness.LoginActivity.ACCESS_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mariasher.qmobilitybusiness.Counters.CounterMainActivity;
import com.mariasher.qmobilitybusiness.LoginActivity;
import com.mariasher.qmobilitybusiness.Queues.QueueMainActivity;
import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.enums.AccessType;
import com.mariasher.qmobilitybusiness.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {
    private ActivityAdminMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String accessType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        accessType = intent.getStringExtra(ACCESS_TYPE);
        mAuth = FirebaseAuth.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            firebaseRealtimeUtils.getBusinessNameFromBusinessDetails(businessId, businessName -> {
                binding.businessNameAdminMainHeaderTextView.setText(businessName);
            });
        });


        switch (AccessType.valueOf(accessType)) {
            case ADMIN:
                binding.manageEmployeesAdminButton.setVisibility(View.VISIBLE);
                binding.manageQueuesAdminButton.setVisibility(View.VISIBLE);
                binding.manageCounterAdminButton.setVisibility(View.VISIBLE);
                break;
            case MANAGER:
                binding.manageEmployeesAdminButton.setVisibility(View.GONE);
                binding.manageQueuesAdminButton.setVisibility(View.VISIBLE);
                binding.manageCounterAdminButton.setVisibility(View.VISIBLE);
                break;
            case OPERATOR:
                binding.manageEmployeesAdminButton.setVisibility(View.GONE);
                binding.manageQueuesAdminButton.setVisibility(View.GONE);
                binding.manageCounterAdminButton.setVisibility(View.VISIBLE);
                break;
            default:
                binding.manageEmployeesAdminButton.setVisibility(View.GONE);
                binding.manageQueuesAdminButton.setVisibility(View.GONE);
                binding.manageCounterAdminButton.setVisibility(View.GONE);
        }
    }

    public void manageEmployeesAdminButtonClicked(View view) {
        Intent intent = new Intent(this, ManageEmployeesActivity.class);
        startActivity(intent);
    }

    public void manageQueuesAdminButtonClicked(View view) {
        Intent intent = new Intent(this, QueueMainActivity.class);
        startActivity(intent);
    }

    public void manageCounterAdminButtonClicked(View view) {
        Intent intent = new Intent(this, CounterMainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void logoutItemClicked(@NonNull MenuItem item) {
        logoutEmployee();
    }

    private void logoutEmployee() {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}