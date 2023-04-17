package com.mariasher.qmobilitybusiness.Utils.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariasher.qmobilitybusiness.Counters.CounterDetailsActivity;
import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.database.Counter;
import com.mariasher.qmobilitybusiness.databinding.ViewCounterCardLayoutBinding;

import java.util.List;

public class ViewCountersViewAdapter extends RecyclerView.Adapter<ViewCountersViewAdapter.ViewCountersViewHolder> {

    public static final String COUNTER_ID = "COUNTER_ID";
    private List<Counter> counters;
    private List<String> queueNames;
    private Context context;

    public ViewCountersViewAdapter(List<Counter> counters, List<String> queueNames, Context context) {
        this.counters = counters;
        this.queueNames = queueNames;
        this.context = context;
    }

    public class ViewCountersViewHolder extends RecyclerView.ViewHolder {
        private ViewCounterCardLayoutBinding binding;

        public ViewCountersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ViewCounterCardLayoutBinding.bind(itemView);
        }
    }


    @Override
    public int getItemCount() {
        return counters.size();
    }

    @NonNull
    @Override
    public ViewCountersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_counter_card_layout, parent, false);
        return new ViewCountersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCountersViewHolder holder, int position) {
        holder.binding.counterNumberCardTextView.setText(counters.get(position).getCounterNumber());
        holder.binding.counterStatusCardTextView2.setText(counters.get(position).getCounterStatus());
        holder.binding.queueNameCardTextView2.setText(queueNames.get(position));
        holder.binding.viewCountersCardView.setOnClickListener(v -> {
            viewCountersCardViewClicked(counters.get(position).getCounterId());
        });
    }

    private void viewCountersCardViewClicked(String counterId) {
        Intent intent = new Intent(context, CounterDetailsActivity.class);
        intent.putExtra(COUNTER_ID, counterId);
        context.startActivity(intent);
    }
}
