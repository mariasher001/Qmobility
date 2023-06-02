package com.mariasher.qmobilitybusiness.Counters;

import static com.mariasher.qmobilitybusiness.Utils.Adapters.ViewCountersViewAdapter.COUNTER_ID;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.Utils.DateTimeUtils;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.Utils.enums.ClientStatus;
import com.mariasher.qmobilitybusiness.Utils.enums.CounterStatus;
import com.mariasher.qmobilitybusiness.Utils.enums.QueueStatus;
import com.mariasher.qmobilitybusiness.database.Client;
import com.mariasher.qmobilitybusiness.database.Counter;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.ActivityCounterControlsBinding;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CounterControlsActivity extends AppCompatActivity {

    private ActivityCounterControlsBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mReal;
    private FirebaseRealtimeUtils firebaseRealtimeUtils;
    private String counterId;
    private String businessId;
    private Queue queue;
    private Counter counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCounterControlsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        counterId = intent.getStringExtra(COUNTER_ID);

        mAuth = FirebaseAuth.getInstance();
        mReal = FirebaseDatabase.getInstance();
        firebaseRealtimeUtils = new FirebaseRealtimeUtils(this);

        String Uid = mAuth.getCurrentUser().getUid();

        firebaseRealtimeUtils.getBusinessIdFromEmployeeBusinessLink(Uid, businessId -> {
            this.businessId = businessId;

            firebaseRealtimeUtils.getCounterDetailsFromFirebase(this.businessId, counterId, counter -> {
                this.counter = counter;
                firebaseRealtimeUtils.getQueueDataFromFirebaseWithBusinessId(this.businessId, this.counter.getQueueId(), queue -> {
                    this.queue = queue;

                    calculateNextNumberOnCall(nextNumberOnCall -> {
                        counter.setNextNumberOnCall(nextNumberOnCall);
                        setViewValues();
                    });
                });
            });
        });
    }

    private void calculateNextNumberOnCall(Callback<Integer> callback) {
        firebaseRealtimeUtils.getClientsInQueue(queue.getClientsInQueue(), clients -> {
            Client firstClient = null;
            if (!clients.isEmpty()) {
                firstClient = clients.stream()
                        .filter(client1 -> client1.getClientStatus().equals(ClientStatus.QUEUED.toString()))
                        .min(Comparator.comparingInt(Client::getAssignedNumberInQueue))
                        .orElse(null);
            }
            int nextNumber = (firstClient != null ? firstClient.getAssignedNumberInQueue() : 0);
            callback.onSuccess(nextNumber);
        });
    }

    private void setViewValues() {
        binding.queueNameCounterControlsTextView.setText(queue.getQueueName());
        binding.counterNumberCounterControlsTextView.setText(counter.getCounterNumber());
        binding.counterStatusCounterControlsTextView.setText(counter.getCounterStatus());
        binding.customerNumberOnCallCounterControlsTextView.setText(String.valueOf(counter.getCustomerNumberOnCall()));
        binding.nextNumberOnCallCounterControlsTextView.setText(String.valueOf(counter.getNextNumberOnCall()));

        setButtonsAndStatus(counter.getCounterStatus());
    }

    private void setButtonsAndStatus(String counterStatus) {
        binding.startCounterControlsButton.setEnabled(false);
        binding.pauseCounterControlsButton.setEnabled(false);
        binding.resetCounterControlsButton.setEnabled(false);
        binding.nextCustomerCounterControlsButton.setEnabled(false);
        binding.skipCustomerCounterControlsButton.setEnabled(false);

        switch (CounterStatus.valueOf(counterStatus)) {
            case ACTIVE:
                binding.pauseCounterControlsButton.setEnabled(true);
                binding.resetCounterControlsButton.setEnabled(true);
                binding.nextCustomerCounterControlsButton.setEnabled(true);
                binding.skipCustomerCounterControlsButton.setEnabled(true);
                binding.counterStatusCounterControlsTextView.setTextColor(Color.GREEN);
                break;
            case INACTIVE:
                if (queue.getQueueStatus().equals(QueueStatus.ACTIVE.toString())) {
                    binding.startCounterControlsButton.setEnabled(true);
                }
                binding.counterStatusCounterControlsTextView.setTextColor(Color.RED);
                break;
            case PAUSED:
                if (queue.getQueueStatus().equals(QueueStatus.ACTIVE.toString())) {
                    binding.startCounterControlsButton.setEnabled(true);
                }
                if (queue.getQueueStatus().equals(QueueStatus.PAUSED.toString())) {
                    binding.startCounterControlsButton.setEnabled(true);
                }
                binding.resetCounterControlsButton.setEnabled(true);
                binding.counterStatusCounterControlsTextView.setTextColor(Color.YELLOW);
                break;
        }
    }

    public void startCounterControlsButtonClicked(View view) {
        counter.setCounterStatus(CounterStatus.ACTIVE.toString());

        queue.setNumberOfActiveCounters(queue.getNumberOfActiveCounters() + 1);
        firebaseRealtimeUtils.updateQueueInFirebase(businessId, queue, isQueueUpdated -> {
        });

        firebaseRealtimeUtils.updateCounterInFirebase(businessId, counter, isCounterUpdated -> {
            if (isCounterUpdated) {
                Toast.makeText(this, "Counter updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating counter!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pauseCounterControlsButtonClicked(View view) {
        counter.setCounterStatus(CounterStatus.PAUSED.toString());

        queue.setNumberOfActiveCounters(queue.getNumberOfActiveCounters() - 1);
        firebaseRealtimeUtils.updateQueueInFirebase(businessId, queue, isQueueUpdated -> {
        });

        firebaseRealtimeUtils.updateCounterInFirebase(businessId, counter, isCounterUpdated -> {
            if (isCounterUpdated) {
                Toast.makeText(this, "Counter updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating counter!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void resetCounterControlsButtonClicked(View view) {
        if (!counter.getCounterStatus().equals(CounterStatus.PAUSED.toString())) {
            queue.setNumberOfActiveCounters(queue.getNumberOfActiveCounters() - 1);
        }

        counter.setCounterStatus(CounterStatus.INACTIVE.toString());
        counter.setAverageCustomerTime("");
        counter.setCustomerNumberOnCall(0);
        counter.setNextNumberOnCall(0);

        firebaseRealtimeUtils.updateQueueInFirebase(businessId, queue, isQueueUpdated -> {
        });

        firebaseRealtimeUtils.updateCounterInFirebase(businessId, counter, isCounterUpdated -> {
            if (isCounterUpdated) {
                Toast.makeText(this, "Counter updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating counter!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteCounterControlsButtonClicked(View view) {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Are you sure you want to delete " + counter.getCounterNumber() + " counter?")
                .setPositiveButton("YES", (dialog, i) -> {

                    if (counter.getCounterStatus().equals(CounterStatus.ACTIVE.toString())) {
                        queue.setNumberOfActiveCounters(queue.getNumberOfActiveCounters() - 1);
                        firebaseRealtimeUtils.updateQueueInFirebase(businessId, queue, isQueueUpdated -> {
                        });
                    }

                    deleteCounterFromFirebase(isCounterDeleted -> {
                        if (isCounterDeleted) {
                            deleteCounterFromQueue();
                        } else {
                            Toast.makeText(this, "Couldn't delete the counter!", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("NO", (dialog, i) -> {
                    dialog.dismiss();
                }).show();
    }

    public void nextCustomerCounterControlsButtonClicked(View view) {

        firebaseRealtimeUtils.getClientsInQueue(queue.getClientsInQueue(), clients -> {
            Client currentClient = null;
            if (!clients.isEmpty()) {
                List<Client> currentClients = clients.stream()
                        .filter(client1 ->
                                client1.getClientStatus().equals(ClientStatus.ONCALL.toString()) &&
                                        (client1.getAssignedCounter().equals(counter.getCounterNumber()))
                        )
                        .collect(Collectors.toList());
                //LOGS
                for (Client client: currentClients) {
                    Log.i("lol", client.getClientEmail());
                }

                if (!currentClients.isEmpty()) {
                    currentClient = currentClients.get(0);
                }
            }

            //LOG
            Log.i("lol", currentClient!=null?currentClient.getClientName():"NULL");

            if (currentClient != null) {
                currentClient.setClientStatus(ClientStatus.DEQUEUED.toString());
                currentClient.setQueueId("");
                currentClient.setBusinessId("");
                currentClient.setAssignedCounter("");
                currentClient.setAssignedNumberInQueue(0);
                currentClient.setQueueExitTime(DateTimeUtils.convertDateAndTimeToString(LocalDateTime.now()));

                Map<String, Object> clientsInQueue = queue.getClientsInQueue();
                clientsInQueue.remove(currentClient.getClientId());
                queue.setClientsInQueue(clientsInQueue);

                //TODO calculate average time

                firebaseRealtimeUtils.updateClientsInFirebase(currentClient, isClientUpdated -> {
                    if (isClientUpdated) {
                        Toast.makeText(this, "Client Dequeued!", Toast.LENGTH_SHORT).show();
                    }
                });
                firebaseRealtimeUtils.updateQueueInFirebase(businessId, queue, isQueueUpdated -> {
                });
            }

            Client firstClient = null;
            if (!clients.isEmpty()) {
                firstClient = clients.stream()
                        .filter(client1 -> client1.getClientStatus().equals(ClientStatus.QUEUED.toString()))
                        .min(Comparator.comparingInt(Client::getAssignedNumberInQueue))
                        .orElse(null);
            }

            if (firstClient != null) {
                firstClient.setClientStatus(ClientStatus.ONCALL.toString());
                firstClient.setAssignedCounter(counter.getCounterNumber());
                String firstClientId = firstClient.getClientId();

                counter.setCustomerNumberOnCall(firstClient.getAssignedNumberInQueue());

                Client nextClient = clients.stream()
                        .filter(client1 -> client1.getClientStatus().equals(ClientStatus.QUEUED.toString()))
                        .filter(client2 -> !client2.getClientId().equals(firstClientId))
                        .min(Comparator.comparingInt(Client::getAssignedNumberInQueue))
                        .orElse(null);

                int nextNumber = (nextClient != null ? nextClient.getAssignedNumberInQueue() : 0);
                counter.setNextNumberOnCall(nextNumber);

                firebaseRealtimeUtils.updateClientsInFirebase(firstClient, isClientUpdated -> {
                    if (isClientUpdated)
                        Toast.makeText(this, "Next number called!", Toast.LENGTH_SHORT).show();
                });
                firebaseRealtimeUtils.updateCounterInFirebase(businessId, counter, isCounterUpdated -> {
                });
            } else {
                Toast.makeText(this, "Queue is empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void skipCustomerCounterControlsButtonClicked(View view) {
        //TODO WHEN YOU ADD CLIENTS TO QUEUE
    }

    public void counterDetailsCounterControlsButtonClicked(View view) {
        Intent intent = new Intent(this, CounterDetailsActivity.class);
        intent.putExtra(COUNTER_ID, counterId);
        startActivity(intent);
    }

    public void counterAnalyticsCounterControlsButtonClicked(View view) {
        //TODO WHEN YOU ADD ANALYTICS
    }

    private void deleteCounterFromFirebase(Callback<Boolean> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Counters")
                .child(counter.getCounterId())
                .removeValue()
                .addOnCompleteListener(task -> callback.onSuccess(task.isSuccessful()));

    }

    private void deleteCounterFromQueue() {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessId)
                .child("Queues")
                .child(queue.getQueueId())
                .child("queueCounters")
                .child(counter.getCounterId())
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Counter Deleted successfully!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(this, "Couldn't delete the counter!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}