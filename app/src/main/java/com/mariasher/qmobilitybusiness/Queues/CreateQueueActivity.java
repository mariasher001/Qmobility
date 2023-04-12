package com.mariasher.qmobilitybusiness.Queues;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.ActivityCreateQueueBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateQueueActivity extends AppCompatActivity {

    private ActivityCreateQueueBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateQueueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);
    }

    public void createQueueButtonClicked(View view) {
        String queueName = binding.queueNameEditText.getText().toString();

        if (queueName.isEmpty()) {
            setError(binding.queueNameEditText, "Queue name cannot be empty");
            return;
        }

        String queueId = UUID.randomUUID().toString();
        String creatorId = mAuth.getCurrentUser().getUid();
        Queue queue = new Queue(queueId, creatorId, queueName);

        addQueueToFirebaseDatabase(queue);
    }

    private void addQueueToFirebaseDatabase(Queue queue) {

        Map<String, Object> queueMap = new HashMap<>();
        queueMap.put("queueId", queue.getQueueId());
        queueMap.put("creatorId", queue.getCreatorId());
        queueMap.put("queueName", queue.getQueueName());
        queueMap.put("queueStartTime", queue.getQueueStartTime());
        queueMap.put("queueEndTime", queue.getQueueEndTime());
        queueMap.put("queueStatus", queue.getQueueStatus());
        queueMap.put("numberOfActiveCounters", queue.getNumberOfActiveCounters());
        queueMap.put("averageCustomerTime", queue.getAverageCustomerTime());
        queueMap.put("queueCounters", new HashMap<String, Object>());

        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(mAuth.getCurrentUser().getUid(), businessId -> {
            mReal.getReference("QMobility")
                    .child("Businesses")
                    .child(businessId)
                    .child("Queues")
                    .child(queue.getQueueId())
                    .setValue(queueMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Queue Created Successfully!", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    });
        });
    }

    private boolean setError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
        return false;
    }
}