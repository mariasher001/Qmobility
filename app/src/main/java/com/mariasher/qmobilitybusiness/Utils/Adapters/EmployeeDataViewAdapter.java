package com.mariasher.qmobilitybusiness.Utils.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.EmployeeProfileActivity;
import com.mariasher.qmobilitybusiness.R;
import com.mariasher.qmobilitybusiness.Utils.Interfaces.Callback;
import com.mariasher.qmobilitybusiness.database.Employee;
import com.mariasher.qmobilitybusiness.databinding.EmployeeDataCardLayoutBinding;

import java.util.List;

public class EmployeeDataViewAdapter extends RecyclerView.Adapter<EmployeeDataViewAdapter.EmployeeDataViewHolder> {


    private List<Employee> employees;
    private Context context;
    private String CRUD;

    public EmployeeDataViewAdapter(List<Employee> employees, Context context, String CRUD) {
        this.employees = employees;
        this.context = context;
        this.CRUD = CRUD;
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

        if (!CRUD.equals("DELETE")) {
            holder.binding.employeeDataCardView.setOnClickListener(v -> {
                employeeDataCardViewClicked(employees.get(position).getBusinessId(), employees.get(position).getEmployeeId());
            });
        } else {
            holder.binding.employeeDataCardView.setOnClickListener(v -> {
                deleteEmployeeCardViewClicked(employees.get(position));
            });
        }
    }

    private void employeeDataCardViewClicked(String businessId, String employeeId) {
        Intent intent = new Intent(context, EmployeeProfileActivity.class);
        intent.putExtra("businessId", businessId);
        intent.putExtra("employeeId", employeeId);
        intent.putExtra("CRUD", CRUD);
        context.startActivity(intent);

        //TODO finish the activity
    }

    private void deleteEmployeeCardViewClicked(Employee employee) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("Are you sure you want to delete " + employee.getName() + "'s Profile?")
                .setPositiveButton("YES", (dialog, i) -> {
                    deleteEmployee(employee);
                })
                .setNegativeButton("NO", (dialog, i) -> {
                    dialog.dismiss();
                }).show();
    }

    private void deleteEmployee(Employee employee) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mReal = FirebaseDatabase.getInstance();

        deleteFromFirebaseRealtimeBusinesses(mReal, employee, isSuccessful -> {
            if (isSuccessful)
                deleteFromFirebaseRealtimeEmployeeBusinessLink(mReal, employee.getEmployeeId(), isSuccessful1 -> {
                    if (isSuccessful1) {
                        Toast.makeText(context, "Employee Deleted From Realtime DB", Toast.LENGTH_SHORT).show();
                        //TODO deleteEmployeeFromFirebaseAuth(mAuth, employee);
                    }
                });
        });
    }

    private void deleteFromFirebaseRealtimeBusinesses(FirebaseDatabase mReal, Employee employee, Callback<Boolean> callback) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(employee.getBusinessId())
                .child("EmployeeDetails")
                .child(employee.getEmployeeId())
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        callback.onSuccess(true);
                    else
                        callback.onSuccess(false);
                });
    }

    private void deleteFromFirebaseRealtimeEmployeeBusinessLink(FirebaseDatabase mReal, String employeeId, Callback<Boolean> callback) {
        mReal.getReference("QMobility")
                .child("EmployeeBusinessLink")
                .child(employeeId)
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        callback.onSuccess(true);
                    else
                        callback.onSuccess(false);
                });
    }
}
