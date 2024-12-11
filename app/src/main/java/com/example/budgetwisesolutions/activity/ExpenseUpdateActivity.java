package com.example.budgetwisesolutions.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetwisesolutions.R;

public class ExpenseUpdateActivity extends AppCompatActivity {
    private EditText etDate, etNote, etAmount;
    private Button btnUppExpense,btnDelExpense;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenseupdate);

        // Initialize views
        etDate = findViewById(R.id.etDate);
        etNote = findViewById(R.id.etNote);
        etAmount = findViewById(R.id.etAmount);
        btnUppExpense = findViewById(R.id.btnUppExpense);
        btnDelExpense = findViewById(R.id.btnDelExpense);

        // Get data from intent
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        String date = intent.getStringExtra("date");
        String note = intent.getStringExtra("note");
        String amount = intent.getStringExtra("amount");

        // Populate fields with current data
        etDate.setText(date);
        etNote.setText(note);
        etAmount.setText(amount);

        // Handle update button
        btnUppExpense.setOnClickListener(v -> handleUpdate());

        // Handle Delete button
        btnDelExpense.setOnClickListener(v -> handleDelete());
    }

    private void handleUpdate() {
        String updatedDate = etDate.getText().toString().trim();
        String updatedNote = etNote.getText().toString().trim();
        String updatedAmount = etAmount.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(updatedDate)) {
            etDate.setError("Please enter a date");
            return;
        }
        if (TextUtils.isEmpty(updatedNote)) {
            etNote.setError("Please enter a note");
            return;
        }
        if (TextUtils.isEmpty(updatedAmount)) {
            etAmount.setError("Please enter an amount");
            return;
        }

        // Send updated data back
        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", position);
        resultIntent.putExtra("date", updatedDate);
        resultIntent.putExtra("note", updatedNote);
        resultIntent.putExtra("amount", updatedAmount);
        setResult(RESULT_OK, resultIntent);
        finish();

    }
    // Handle Delete button click
    private void handleDelete() {
        // Send delete signal to the calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", position);  // Send the position of the item to delete
        setResult(RESULT_FIRST_USER, resultIntent);  // Use RESULT_FIRST_USER for delete actions
        finish();
    }
}
