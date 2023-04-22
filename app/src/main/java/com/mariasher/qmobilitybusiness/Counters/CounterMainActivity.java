package com.mariasher.qmobilitybusiness.Counters;

import static com.mariasher.qmobilitybusiness.Queues.QueueMainActivity.QUEUE_OPERATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mariasher.qmobilitybusiness.LoginActivity;
import com.mariasher.qmobilitybusiness.Queues.ViewQueuesActivity;
import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.databinding.ActivityCounterMainBinding;

public class CounterMainActivity extends AppCompatActivity {

    public static final String CREATE_COUNTER = "CreateCounter";
    public static final String COUNTER_OPERATIONS = "CounterOperations";
    public static final String COUNTER_DETAILS = "CounterDetails";
    public static final String COUNTER_CONTROLS = "CounterControls";
    private ActivityCounterMainBinding binding;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCounterMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
    }

    public void createCounterMainButtonClicked(View view) {
        Intent intent = new Intent(this, ViewQueuesActivity.class);
        intent.putExtra(QUEUE_OPERATIONS, CREATE_COUNTER);
        startActivity(intent);
    }

    public void viewCounterDetailsMainButtonClicked(View view) {
        Intent intent = new Intent(this, ViewCountersActivity.class);
        intent.putExtra(COUNTER_OPERATIONS, COUNTER_DETAILS);
        startActivity(intent);
    }

    public void counterControlsMainButtonClicked(View view) {
        Intent intent = new Intent(this, ViewCountersActivity.class);
        intent.putExtra(COUNTER_OPERATIONS, COUNTER_CONTROLS);
        startActivity(intent);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void logoutItemClicked(@NonNull MenuItem item) {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void counterAnalyticsMainButtonClicked(View view) {
        //TODO
    }
}