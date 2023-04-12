package com.mariasher.qmobilitybusiness.Queues;

import static com.mariasher.qmobilitybusiness.Queues.QueueMainActivity.QUEUE_OPERATIONS;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.Utils.Adapters.ViewQueuesViewAdapter;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.databinding.ActivityViewQueuesBinding;

public class ViewQueuesActivity extends AppCompatActivity {

    private ActivityViewQueuesBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewQueuesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String queueOperations = intent.getStringExtra(QUEUE_OPERATIONS);

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        firebaseRealtimeUtils.getQueuesFromFirebaseDatabase(mAuth.getCurrentUser().getUid(), queues -> {
            binding.viewQueuesRecyclerView.setAdapter(new ViewQueuesViewAdapter(queues, this, queueOperations));
        });
    }
}