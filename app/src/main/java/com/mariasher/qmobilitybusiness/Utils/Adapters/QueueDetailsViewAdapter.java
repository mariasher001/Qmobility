package com.mariasher.qmobilitybusiness.Utils.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.database.Queue;
import com.mariasher.qmobilitybusiness.databinding.QueueDetailsCardLayoutBinding;

import java.util.List;

public class QueueDetailsViewAdapter extends RecyclerView.Adapter<QueueDetailsViewAdapter.QueueDetailsViewHolder> {

    private List<Queue> queues;

    public QueueDetailsViewAdapter(List<Queue> queues) {
        this.queues = queues;
    }

    public class QueueDetailsViewHolder extends RecyclerView.ViewHolder {
        QueueDetailsCardLayoutBinding binding;

        public QueueDetailsViewHolder(@NonNull View itemView) {
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
    public QueueDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_details_card_layout, parent, false);
        return new QueueDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueDetailsViewHolder holder, int position) {
        holder.binding.queueNameCardTextView.setText(queues.get(position).getQueueName());
        holder.binding.queueStatusCardTextView.setText(queues.get(position).getQueueStatus());
        holder.binding.activeCountersCardTextView.setText("" + queues.get(position).getNumberOfActiveCounters());

        //TODO set onclicklistener for cardview
    }
}
