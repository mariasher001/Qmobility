package com.mariasher.qmobilitybusiness.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.Utils.FirebaseRealtimeUtils;
import com.mariasher.qmobilitybusiness.database.Counter;
import com.mariasher.qmobilitybusiness.databinding.ViewCounterCardLayoutBinding;

import java.util.List;

public class ViewCountersViewAdapter extends RecyclerView.Adapter<ViewCountersViewAdapter.ViewCountersViewHolder> {

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
    }
}
