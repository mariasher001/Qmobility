package com.mariasher.qmobilitybusiness.Utils.Adapters;

import static com.mariasher.qmobilitybusiness.Counters.CounterMainActivity.CREATE_COUNTER;
import static com.mariasher.qmobilitybusiness.Queues.QueueMainActivity.QUEUE_CONTROLS;
import static com.mariasher.qmobilitybusiness.Queues.QueueMainActivity.VIEW_QUEUE_DETAILS;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariasher.qmobilitybusiness.Counters.CreateCounterActivity;
import com.mariasher.qmobilitybusiness.Queues.QueueControlsActivity;
import com.mariasher.qmobilitybusiness.Queues.QueueDetailsActivity;
import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.QueueDetailsCardLayoutBinding;

import java.util.List;

public class ViewQueuesViewAdapter extends RecyclerView.Adapter<ViewQueuesViewAdapter.ViewQueuesViewHolder> {

    public static final String QUEUE_ID = "queueId";
    private List<Queue> queues;
    private Context context;
    private String queueOperations;

    public ViewQueuesViewAdapter(List<Queue> queues, Context context, String queueOperations) {
        this.queues = queues;
        this.context = context;
        this.queueOperations = queueOperations;
    }

    public class ViewQueuesViewHolder extends RecyclerView.ViewHolder {
        QueueDetailsCardLayoutBinding binding;

        public ViewQueuesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = QueueDetailsCardLayoutBinding.bind(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return queues.size();
    }

    @NonNull
    @Override
    public ViewQueuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_details_card_layout, parent, false);
        return new ViewQueuesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewQueuesViewHolder holder, int position) {
        holder.binding.queueNameCardTextView.setText(queues.get(position).getQueueName());
        holder.binding.queueStatusCardTextView.setText(queues.get(position).getQueueStatus());
        holder.binding.activeCountersCardTextView.setText("" + queues.get(position).getNumberOfActiveCounters());

        holder.binding.queueDetailsCardView.setOnClickListener(v -> {
            navigateToQueueDetails(queues.get(position).getQueueId());
        });
        setOnclickForCardView(holder, position);
    }

    private void setOnclickForCardView(ViewQueuesViewHolder holder, int position) {
        switch (queueOperations) {
            case VIEW_QUEUE_DETAILS: {
                holder.binding.queueDetailsCardView.setOnClickListener(v -> {
                    navigateToQueueDetails(queues.get(position).getQueueId());
                });
                break;
            }
            case QUEUE_CONTROLS: {
                holder.binding.queueDetailsCardView.setOnClickListener(v -> {
                    navigateToQueueControls(queues.get(position).getQueueId());
                });
                break;
            }
            case CREATE_COUNTER: {
                holder.binding.queueDetailsCardView.setOnClickListener(v -> {
                    navigateToCreateCounterInQueue(queues.get(position).getQueueId());
                });
                break;
            }
            default:
                Toast.makeText(context, "Can't access queue information.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToQueueDetails(String queueId) {
        Intent intent = new Intent(context, QueueDetailsActivity.class);
        intent.putExtra(QUEUE_ID, queueId);
        context.startActivity(intent);
    }

    private void navigateToQueueControls(String queueId) {
        Intent intent = new Intent(context, QueueControlsActivity.class);
        intent.putExtra(QUEUE_ID, queueId);
        context.startActivity(intent);
    }

    private void navigateToCreateCounterInQueue(String queueId) {
        Intent intent = new Intent(context, CreateCounterActivity.class);
        intent.putExtra(QUEUE_ID, queueId);
        context.startActivity(intent);
    }
}
