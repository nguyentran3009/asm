package com.example.budgetwisesolutions.activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.budgetwisesolutions.adapter.BudgetCategoryAdapter;

import com.example.budgetwisesolutions.model.Budget;
import com.example.budgetwisesolutions.model.BudgetCategory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import android.app.AlertDialog;
import android.widget.Toast;
import java.text.ParseException;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import com.example.budgetwisesolutions.R;
public class UpdateBudgetDialog extends Dialog {

    private Budget budget;
    private OnBudgetUpdateListener updateListener;
    private Calendar calendar;
    public interface OnBudgetUpdateListener {
        void onBudgetUpdate(Budget updatedBudget);
        void onBudgetDelete(Budget budgetToDelete); // Thêm hàm callback riêng cho xóa
    }

    public UpdateBudgetDialog(Context context, Budget budget, OnBudgetUpdateListener updateListener) {
        super(context);
        this.budget = budget;
        this.updateListener = updateListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_budget);

        EditText sourceInput = findViewById(R.id.sourceInput);
        EditText amountInput = findViewById(R.id.amountInput);
        TextView startDateInput = findViewById(R.id.startDateInput);  // Use TextView to show the date
        TextView endDateInput = findViewById(R.id.endDateInput);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        EditText noteInput = findViewById(R.id.noteInput);


        Button updateButton = findViewById(R.id.updateButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        calendar = Calendar.getInstance();
        // Pre-fill the fields with existing budget data
        sourceInput.setText(budget.getSource());
        amountInput.setText(String.valueOf(budget.getAmount()));
        startDateInput.setText(budget.getStartDate());
        endDateInput.setText(budget.getEndDate());
        noteInput.setText(budget.getNote());  // Cập nhật note

// Gọi hàm định dạng số tiền
        handleAmountInputFormatting(amountInput);

        // Định dạng và hiển thị số tiền
        amountInput.setText(formatAmount(budget.getAmount()));
        // Set up the category spinner
        List<BudgetCategory> categories = Arrays.asList(
                new BudgetCategory("Rent", R.drawable.ic_rent),
                new BudgetCategory("Food", R.drawable.ic_food),
                new BudgetCategory("Entertainment/Leisure", R.drawable.ic_entertainment),
                new BudgetCategory("Education/Study materials", R.drawable.ic_education),

                new BudgetCategory("Personal shopping", R.drawable.ic_shopping),
                new BudgetCategory("Transportation", R.drawable.ic_transportation)
        );


        BudgetCategoryAdapter adapter = new BudgetCategoryAdapter(getContext(), categories);
        categorySpinner.setAdapter(adapter);

        // Set the selected category
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(budget.getCategory())) {
                categorySpinner.setSelection(i);
                break;
            }
        }


        startDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                        startDateInput.setText(selectedDate);

                        // Kiểm tra tính hợp lệ sau khi chọn ngày
                        String endDate = endDateInput.getText().toString();
                        if (!endDate.isEmpty() && !checkDateValidity(selectedDate, endDate)) {
                            startDateInput.setText("");
                        }
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        endDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                        endDateInput.setText(selectedDate);

                        // Kiểm tra tính hợp lệ sau khi chọn ngày
                        String startDate = startDateInput.getText().toString();
                        if (!startDate.isEmpty() && !checkDateValidity(startDate, selectedDate)) {
                            endDateInput.setText("");
                        }
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });


        updateButton.setOnClickListener(v -> {
            String updatedSource = sourceInput.getText().toString();
            String amountText = amountInput.getText().toString().replace(",", ""); // Loại bỏ dấu phẩy
            String updatedStartDate = startDateInput.getText().toString();
            String updatedEndDate = endDateInput.getText().toString();
            String updatedNote = noteInput.getText().toString();
            String updatedCategory = ((BudgetCategory) categorySpinner.getSelectedItem()).getName();

            if (updatedSource.isEmpty()) {
                Toast.makeText(getContext(), "Please enter the source", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amountText.isEmpty()) {
                Toast.makeText(getContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển đổi tiền tệ sang số và kiểm tra hợp lệ
            BigDecimal updatedAmount;
            try {
                updatedAmount = new BigDecimal(amountText);
                if (updatedAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    Toast.makeText(getContext(), "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Định dạng số tiền trước khi hiển thị
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            String formattedAmount = decimalFormat.format(updatedAmount);  // Định dạng số tiền sau khi đã parse
            amountInput.setText(formattedAmount);  // Đặt lại giá trị đã định dạng cho EditText

            if (updatedStartDate.isEmpty()) {
                Toast.makeText(getContext(), "Please select a start date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (updatedEndDate.isEmpty()) {
                Toast.makeText(getContext(), "Please select an end date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra tính hợp lệ của ngày trước khi cập nhật
            if (!checkDateValidity(updatedStartDate, updatedEndDate)) {
                return;
            }

            // Cập nhật lại dữ liệu vào đối tượng budget
            budget.setSource(updatedSource);
            budget.setAmount(updatedAmount.doubleValue());  // Cập nhật số tiền dưới dạng Double
            budget.setStartDate(updatedStartDate);
            budget.setEndDate(updatedEndDate);
            budget.setNote(updatedNote);
            budget.setCategory(updatedCategory);

            // Gọi hàm callback để cập nhật dữ liệu
            updateListener.onBudgetUpdate(budget);
            dismiss();
        });


        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Budget")
                    .setMessage("Are you sure you want to delete this budget?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Trigger the delete listener (handled in the parent activity)
                        updateListener.onBudgetDelete(budget); // Null indicates deletion
                        dismiss(); // Close the dialog after deletion
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Close the confirmation dialog
                    .show();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }



    private boolean checkDateValidity(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            if (start != null && end != null && start.after(end)) {
                Toast.makeText(getContext(), "Start Date cannot be after End Date", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void handleAmountInputFormatting(EditText amountInput) {
        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                amountInput.removeTextChangedListener(this); // Tạm thời gỡ listener để tránh vòng lặp
                try {
                    String input = editable.toString().replace(",", ""); // Loại bỏ dấu phẩy
                    if (input.length() > 11) {
                        input = input.substring(0, 11); // Giới hạn 11 chữ số
                    }

                    if (!input.isEmpty()) {
                        long parsedValue = Long.parseLong(input);
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String formattedValue = formatter.format(parsedValue);

                        amountInput.setText(formattedValue);
                        amountInput.setSelection(formattedValue.length());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                amountInput.addTextChangedListener(this); // Thêm lại listener
            }
        });
    }

    // Hàm định dạng số tiền
    private String formatAmount(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount);
    }
}
