package com.mariasher.qmobilitybusiness.Utils.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariasher.qmobilitybusiness.EmployeeProfileActivity;
import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.database.Employee;
import com.mariasher.qmobilitybusiness.databinding.EmployeeDataCardLayoutBinding;

import java.util.List;

public class EmployeeDataViewAdapter extends RecyclerView.Adapter<EmployeeDataViewAdapter.EmployeeDataViewHolder> {


    private List<Employee> employees;
    private Context context;

    public EmployeeDataViewAdapter(List<Employee> employees, Context context) {
        this.employees = employees;
        this.context = context;
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

        holder.binding.employeeDataCardView.setOnClickListener(v -> {
            employeeDataCardViewClicked(employees.get(position).getBusinessId(), employees.get(position).getEmployeeId());
        });
    }

    private void employeeDataCardViewClicked(String businessId, String employeeId) {
        Intent intent = new Intent(context, EmployeeProfileActivity.class);
        intent.putExtra("businessId", businessId);
        intent.putExtra("employeeId", employeeId);
        context.startActivity(intent);

        //TODO finish the activity
    }
}
