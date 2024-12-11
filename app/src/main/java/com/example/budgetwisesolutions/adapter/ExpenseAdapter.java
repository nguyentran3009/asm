package com.example.budgetwisesolutions.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetwisesolutions.R;
import com.example.budgetwisesolutions.activity.ExpenseUpdateActivity;
import com.example.budgetwisesolutions.activity.ExpensesActivity;
import com.example.budgetwisesolutions.model.Expenses;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>{
    private final ArrayList<Expenses> expensesList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Expenses expense, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ExpenseAdapter(ArrayList<Expenses> expensesList) {
        this.expensesList = expensesList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expenses expense = expensesList.get(position);
        holder.bind(expense);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(expense, position);

                Intent intent = new Intent(holder.itemView.getContext(), ExpenseUpdateActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("date", expense.getDate());
                intent.putExtra("note", expense.getNote());
                intent.putExtra("amount", expense.getAmount());
                ((ExpensesActivity) holder.itemView.getContext()).startActivityForResult(intent, 100);
            }
        });


    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvNote, tvAmount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }

        public void bind(Expenses expense) {
            tvDate.setText(expense.getDate());
            tvNote.setText(expense.getNote());
            tvAmount.setText(expense.getAmount());
        }
    }

}

