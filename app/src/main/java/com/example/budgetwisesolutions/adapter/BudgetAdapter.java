package com.example.budgetwisesolutions.adapter;


import java.text.DecimalFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetwisesolutions.R;
import com.example.budgetwisesolutions.model.Budget;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private Context context;
    private List<Budget> budgetList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onEditClick(Budget budget);
        void onDeleteClick(Budget budget);
    }

    public BudgetAdapter(Context context, List<Budget> budgetList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.budgetList = budgetList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        holder.sourceTextView.setText(budget.getSource());
        holder.dateTextView.setText(budget.getStartDate() + " - " + budget.getEndDate());
        holder.amountTextView.setText(String.format("%.2f", budget.getAmount()));
// Định dạng số tiền trước khi hiển thị
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAmount = decimalFormat.format(budget.getAmount());
        holder.amountTextView.setText(formattedAmount);
        // Gán ảnh danh mục
        int categoryIcon = getCategoryIcon(budget.getCategory());
        holder.categoryIconImageView.setImageResource(categoryIcon);


        holder.itemView.setOnClickListener(v -> onItemClickListener.onEditClick(budget));
        holder.itemView.setOnLongClickListener(v -> {
            onItemClickListener.onDeleteClick(budget);
            return true;
        });
    }



    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView sourceTextView, dateTextView, amountTextView;
        ImageView categoryIconImageView;
        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);

            categoryIconImageView = itemView.findViewById(R.id.categoryIconImageView);

        }
    }

    private int getCategoryIcon(String category) {
        switch (category) {
            case "Rent":
                return R.drawable.ic_rent;
            case "Food":
                return R.drawable.ic_food;
            case "Entertainment/Leisure":
                return R.drawable.ic_entertainment;
            case "Education/Study materials":
                return R.drawable.ic_education;
            case "Personal shopping":
                return R.drawable.ic_shopping;
            case "Transportation":
                return R.drawable.ic_transportation;
            default:
                return R.drawable.ic_placeholder; // Hình ảnh mặc định nếu không khớp
        }
    }

}

