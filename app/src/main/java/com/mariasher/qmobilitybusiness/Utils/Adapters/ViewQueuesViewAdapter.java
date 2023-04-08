package com.mariasher.qmobilitybusiness.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.QueueDetailsCardLayoutBinding;

import java.util.List;

public class ViewQueuesViewAdapter extends RecyclerView.Adapter<ViewQueuesViewAdapter.ViewQueuesViewHolder> {

    private List<Queue> queues;
    private Context context;

    public ViewQueuesViewAdapter(List<Queue> queues, Context context) {
        this.queues = queues;
        this.context = context;
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
            queueDetailsCardViewClicked();
        });
    }

    private void queueDetailsCardViewClicked() {
        //Intent intent = new Intent(context, QueueDataActivity.class);
    }
}
