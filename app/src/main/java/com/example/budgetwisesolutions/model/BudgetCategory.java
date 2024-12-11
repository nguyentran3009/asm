package com.example.budgetwisesolutions.model;

public class BudgetCategory {
    private String name;
    private int iconResId;

    public BudgetCategory(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
}
