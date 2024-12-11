package com.example.budgetwisesolutions.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.budgetwisesolutions.R;
import com.google.android.material.navigation.NavigationView;


public class HomeActivity extends AppCompatActivity {
    private ImageView menuIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        menuIcon = findViewById(R.id.menuIcon);

        // Set an OnClickListener on the menu icon
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MenuActivity when the menu icon is clicked
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        // Lấy tham chiếu đến các Layout
        LinearLayout sectionExpenses = findViewById(R.id.sectionExpenses);
        LinearLayout sectionIncome = findViewById(R.id.sectionIncome);
        LinearLayout sectionBudget = findViewById(R.id.sectionBudget);

        // Gắn sự kiện OnClickListener
        sectionExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang ExpensesActivity
                Intent intent = new Intent(HomeActivity.this, ExpensesActivity.class);
                startActivity(intent);
            }
        });

        sectionIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang IncomeActivity
                Intent intent = new Intent(HomeActivity.this, IncomeActivity.class);
                startActivity(intent);
            }
        });

        sectionBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang BudgetActivity
                Intent intent = new Intent(HomeActivity.this, BudgetActivity.class);
                startActivity(intent);
            }
        });
    }

 }


