package com.example.budgetwisesolutions.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetwisesolutions.R;
import com.example.budgetwisesolutions.database.UserDb;

public class UpdatePasswordActivity extends AppCompatActivity {

    EditText edtNewPassword, edtConfirmPassword;
    Button btnUpdatePassword;
    UserDb userDb;

    Intent intent;
    Bundle bundle;

    private String username;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdate);
        userDb = new UserDb(UpdatePasswordActivity.this);

        intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            username = bundle.getString("USERNAME_ACCOUNT", "");
            email = bundle.getString("EMAIL_ACCOUNT", "");
        }
        updatePassword();
    }

    private void updatePassword() {
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = edtNewPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword)) {
                    edtNewPassword.setError("New password cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    edtConfirmPassword.setError("Confirm password cannot be empty");
                    return;
                }
                if (!confirmPassword.equals(newPassword)) {
                    edtConfirmPassword.setError("Confirm password does not match new password");
                    return;
                }
                if (newPassword.length() < 8) {
                    edtNewPassword.setError("Password must be at least 8 characters long");
                    return;
                }
                if (!containsUppercase(newPassword) || !containsLowercase(newPassword) || !containsDigit(newPassword) || !containsSpecialCharacter(newPassword)) {
                    edtNewPassword.setError("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
                    return;
                }
                if (newPassword.contains(username)) {
                    edtNewPassword.setError("Password cannot contain the username");
                    return;
                }
                if (containsEightConsecutiveNumbers(newPassword)) {
                    edtNewPassword.setError("Password cannot contain consecutive numbers");
                    return;
                }

                int update = userDb.updatePassword(newPassword, username, email);
                if (update == -1) {
                    // Lỗi không cập nhật được
                    Toast.makeText(UpdatePasswordActivity.this, "Update Failure", Toast.LENGTH_SHORT).show();
                } else {
                    // Thành công
                    Toast.makeText(UpdatePasswordActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                    Intent intentLogin = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
            }
        });
    }

    private boolean containsEightConsecutiveNumbers(String password) {
        for (int i = 0; i <= password.length() - 8; i++) {
            if (Character.isDigit(password.charAt(i)) &&
                    Character.isDigit(password.charAt(i + 1)) &&
                    Character.isDigit(password.charAt(i + 2)) &&
                    Character.isDigit(password.charAt(i + 3)) &&
                    Character.isDigit(password.charAt(i + 4)) &&
                    Character.isDigit(password.charAt(i + 5)) &&
                    Character.isDigit(password.charAt(i + 6)) &&
                    Character.isDigit(password.charAt(i + 7))) {

                // Kiểm tra xem 8 số này có phải là số liên tiếp không
                int first = Character.getNumericValue(password.charAt(i));
                if (first + 1 == Character.getNumericValue(password.charAt(i + 1)) &&
                        first + 2 == Character.getNumericValue(password.charAt(i + 2)) &&
                        first + 3 == Character.getNumericValue(password.charAt(i + 3)) &&
                        first + 4 == Character.getNumericValue(password.charAt(i + 4)) &&
                        first + 5 == Character.getNumericValue(password.charAt(i + 5)) &&
                        first + 6 == Character.getNumericValue(password.charAt(i + 6)) &&
                        first + 7 == Character.getNumericValue(password.charAt(i + 7))) {
                    return true; // Có dãy 8 số liên tiếp
                }
            }
        }
        return false; // Không có dãy 8 số liên tiếp
    }
    private boolean containsUppercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsLowercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialCharacter(String password) {
        String specialCharacters = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";
        for (char c : password.toCharArray()) {
            if (specialCharacters.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
}