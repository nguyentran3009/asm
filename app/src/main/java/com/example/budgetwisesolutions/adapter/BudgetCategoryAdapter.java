package com.example.budgetwisesolutions.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.budgetwisesolutions.R;
import com.example.budgetwisesolutions.model.BudgetCategory;

import java.util.List;


public class BudgetCategoryAdapter extends ArrayAdapter<BudgetCategory> {

    public BudgetCategoryAdapter(Context context, List<BudgetCategory> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category_budget, parent, false);
        }

        BudgetCategory category = getItem(position);

        TextView categoryName = convertView.findViewById(R.id.categoryName);
        ImageView categoryIcon = convertView.findViewById(R.id.categoryIcon);

        if (category != null) {
            categoryName.setText(category.getName());
            categoryIcon.setImageResource(category.getIconResId());
        }

        return convertView;
    }
}
