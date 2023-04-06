package com.mariasher.qmobilitybusiness.Utils.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.database.Employee;
import com.mariasher.qmobilitybusiness.databinding.EmployeeDataCardLayoutBinding;

import java.util.List;

public class EmployeeDataViewAdapter extends RecyclerView.Adapter<EmployeeDataViewAdapter.EmployeeDataViewHolder> {


    private List<Employee> employees;

    public EmployeeDataViewAdapter(List<Employee> employees) {
        this.employees = employees;
    }

    public class EmployeeDataViewHolder extends RecyclerView.ViewHolder {

        EmployeeDataCardLayoutBinding binding;

        public EmployeeDataViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = EmployeeDataCardLayoutBinding.bind(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    @NonNull
    @Override
    public EmployeeDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_data_card_layout, parent, false);
        return new EmployeeDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeDataViewHolder holder, int position) {
        holder.binding.employeeNameCardLayoutTextView.setText(employees.get(position).getName());
        holder.binding.employeeEmailCardLayoutTextView.setText(employees.get(position).getEmailAddress());

        //TODO holder.binding.employeeDataCardView.setOnClickListener();
    }
}
