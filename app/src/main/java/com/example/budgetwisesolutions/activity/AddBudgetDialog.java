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
import android.widget.Toast;


import com.example.budgetwisesolutions.adapter.BudgetCategoryAdapter;
import com.example.budgetwisesolutions.model.Budget;
import com.example.budgetwisesolutions.model.BudgetCategory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.example.budgetwisesolutions.R;
public class AddBudgetDialog extends Dialog {

    private OnBudgetAddListener listener;
    private TextView startDateInput;
    private TextView endDateInput;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    public interface OnBudgetAddListener {
        void onBudgetAdd(Budget budget);
    }

    public AddBudgetDialog(Context context, OnBudgetAddListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_budget);

        EditText sourceInput = findViewById(R.id.sourceInput);
        EditText amountInput = findViewById(R.id.amountInput);
        EditText noteInput = findViewById(R.id.noteInput);
        Button saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Xử lý định dạng số và giới hạn ký tự nhập cho amountInput
        handleAmountInputFormatting(amountInput);

        // Set the listener for the Start Date input
        startDateInput.setOnClickListener(v -> showStartDatePickerDialog());

        // Set the listener for the End Date input
        endDateInput.setOnClickListener(v -> showEndDatePickerDialog());

        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        List<BudgetCategory> categories = Arrays.asList(
                new BudgetCategory("Rent", R.drawable.ic_rent),
                new BudgetCategory("Food", R.drawable.ic_food),
                new BudgetCategory("Entertainment/Leisure", R.drawable.ic_entertainment),
                new BudgetCategory("Education/Study materials", R.drawable.ic_education),
                new BudgetCategory("Personal shopping", R.drawable.ic_shoppingtu),
                new BudgetCategory("Transportation", R.drawable.ic_transportation)
        );

        BudgetCategoryAdapter adapter = new BudgetCategoryAdapter(getContext(), categories);
        categorySpinner.setAdapter(adapter);

        saveButton.setOnClickListener(v -> {
            String source = sourceInput.getText().toString();
            String amountText = amountInput.getText().toString().replace(",", ""); // Loại bỏ dấu phẩy trước khi xử lý
            String category = ((BudgetCategory) categorySpinner.getSelectedItem()).getName();
            String startDate = startDateInput.getText().toString();
            String endDate = endDateInput.getText().toString();
            String note = noteInput.getText().toString();

            // Kiểm tra nếu bất kỳ trường nào không được điền đầy đủ
            if (source.isEmpty()) {
                Toast.makeText(getContext(), "Please enter the source", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amountText.isEmpty()) {
                Toast.makeText(getContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    Toast.makeText(getContext(), "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Định dạng số tiền
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedAmount = decimalFormat.format(amount);

            if (category.isEmpty()) {
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startDate.isEmpty()) {
                Toast.makeText(getContext(), "Please select a start date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (endDate.isEmpty()) {
                Toast.makeText(getContext(), "Please select an end date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu tất cả trường hợp kiểm tra đều hợp lệ, tiến hành lưu ngân sách
            Budget newBudget = new Budget(0, source, startDate, endDate, category, note, Double.parseDouble(formattedAmount.replace(",", "")));
            listener.onBudgetAdd(newBudget);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
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

    private void showStartDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    calendar.set(year1, monthOfYear, dayOfMonth);
                    startDateInput.setText(dateFormat.format(calendar.getTime()));
                    checkDateValidity();
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showEndDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    calendar.set(year1, monthOfYear, dayOfMonth);
                    endDateInput.setText(dateFormat.format(calendar.getTime()));
                    checkDateValidity();
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void checkDateValidity() {
        try {
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();

            startCalendar.setTime(dateFormat.parse(startDateInput.getText().toString()));
            endCalendar.setTime(dateFormat.parse(endDateInput.getText().toString()));

            if (startCalendar.after(endCalendar)) {
                Toast.makeText(getContext(), "Start Date cannot be after End Date", Toast.LENGTH_SHORT).show();
                startDateInput.setText("");
            }
        } catch (Exception e) {
            // Handle the case where the dates are not yet set
        }
    }
}
