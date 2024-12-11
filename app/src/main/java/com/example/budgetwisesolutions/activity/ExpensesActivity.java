package com.example.budgetwisesolutions.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.budgetwisesolutions.R;
import com.example.budgetwisesolutions.adapter.ExpenseAdapter;
import com.example.budgetwisesolutions.model.Expenses;

import java.util.ArrayList;
import java.util.Calendar;

public class ExpensesActivity extends AppCompatActivity {
    private EditText etDate, etNote, etAmount;
    private Button btnAddExpense;
    private RecyclerView rvExpenses;
    private ExpenseAdapter expenseAdapter;
    private ArrayList<Expenses> expenseList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        // Initialize views
        etDate = findViewById(R.id.etDate);
        etNote = findViewById(R.id.etNote);
        etAmount = findViewById(R.id.etAmount);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        rvExpenses = findViewById(R.id.rvExpenses);

        // Initialize expense list and adapter
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList);
        expenseAdapter.setOnItemClickListener((expense, position) -> {
            Intent intent = new Intent(ExpensesActivity.this, ExpenseUpdateActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("date", expense.getDate());
            intent.putExtra("note", expense.getNote());
            intent.putExtra("amount", expense.getAmount());
            startActivity(intent);
        });

        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        rvExpenses.setAdapter(expenseAdapter);

        // Date picker setup
        etDate.setOnClickListener(v -> showDatePicker());

        // Handle Add Expense button
        btnAddExpense.setOnClickListener(v -> handleAddExpense());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the result is for the Update Expense request
        if (data != null) {
            int position = data.getIntExtra("position", -1);

            if (resultCode == RESULT_OK) {
                // Update the expense
                String updatedDate = data.getStringExtra("date");
                String updatedNote = data.getStringExtra("note");
                String updatedAmount = data.getStringExtra("amount");

                if (position != -1) {
                    Expenses updatedExpense = new Expenses(updatedDate, updatedNote, updatedAmount);
                    expenseList.set(position, updatedExpense);
                    expenseAdapter.notifyItemChanged(position);
                }
            } else if (resultCode == RESULT_FIRST_USER) {
                // Delete the expense
                if (position != -1) {
                    expenseList.remove(position);
                    expenseAdapter.notifyItemRemoved(position);
                }
            }
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    etDate.setText(selectedDate);

                    // Clear the error if a date is selected
                    etDate.setError(null);
                }, year, month, day);
        datePickerDialog.show();
    }


    private void handleAddExpense() {
        String date = etDate.getText().toString().trim();
        String note = etNote.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();

        // Input validation
        if (TextUtils.isEmpty(date)) {
            etDate.setError("Please select a date");
            return;
        }
        if (TextUtils.isEmpty(note)) {
            etNote.setError("Please enter a note");
            return;
        }
        if (TextUtils.isEmpty(amountStr)) {
            etAmount.setError("Please enter an amount");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            // Add new expense to the list
            expenseList.add(new Expenses( date, note, String.valueOf(amount)));
            expenseAdapter.notifyDataSetChanged();

            // Clear input fields
            etDate.setText("");
            etNote.setText("");
            etAmount.setText("");

            Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            etAmount.setError("Please enter a valid number");
        }
    }
}
