package com.example.budgetwisesolutions.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetwisesolutions.R;
import com.example.budgetwisesolutions.database.UserDb;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class RegisterActivity extends AppCompatActivity {

    TextView tvLogin;
    EditText edtUser, edtPass, edtEmail, edtPhone, edtAddress;
    Button btnSignup;
    UserDb userDb;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvLogin = findViewById(R.id.btn_login);
        edtUser = findViewById(R.id.et_username);
        edtPass = findViewById(R.id.et_password);
        btnSignup = findViewById(R.id.btn_sign_up);
        edtEmail = findViewById(R.id.et_email);
        edtPhone = findViewById(R.id.et_phone);
        edtAddress = findViewById(R.id.et_address);
        userDb = new UserDb(RegisterActivity.this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupWithDatabase();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void signupWithDatabase(){
        String user = edtUser.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(user)){
            edtUser.setError("Username not empty");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            edtPass.setError("Password cannot be empty");
            return;
        }
        if (pass.length() < 8) {
            edtPass.setError("Password must be at least 8 characters long");
            return;
        }
        if (pass.contains(user)) {
            edtPass.setError("Password cannot contain the username");
            return;
        }
        if (containsEightConsecutiveNumbers(pass)) {
            edtPass.setError("Password cannot contain consecutive numbers");
            return;
        }
        if (!containsUppercase(pass) || !containsLowercase(pass) || !containsDigit(pass) || !containsSpecialCharacter(pass)) {
            edtPass.setError("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
            return;
        }
        if (TextUtils.isEmpty(email)|| !email.contains("@")){
            edtEmail.setError("Email not empty");
            return;
        }
        if (TextUtils.isEmpty(phone)|| phone.length() != 10){
            edtPhone.setError("Phone not empty");
            return;
        }
        long insert = userDb.addNewAccountUser(user,pass,email,phone,address);
        if(insert == -1){
            //loi khong insert duoc
            Toast.makeText(RegisterActivity.this, "Create Failure", Toast.LENGTH_SHORT).show();
        }else{
            //thanh cong
            Toast.makeText(RegisterActivity.this, "Create Successfully", Toast.LENGTH_SHORT).show();
            Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        }
    }

    private void  signupWithDataFile(){
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                if(TextUtils.isEmpty(user)){
                    edtUser.setError("Username can be not empty");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    edtPass.setError("Password can be not empty");
                    return;
                }
                //luu du lieu vao local storage
                //bo nho trong cua thiet bi di dong
                //luu duoi dang la 1 file
                FileOutputStream fileOutputStream = null;
                try{
                    user = user + "|"; //ngan cach giua user va pass
                    fileOutputStream = openFileOutput("account.txt", Context.MODE_APPEND);
                    fileOutputStream.write(user.getBytes(StandardCharsets.UTF_8));
                    fileOutputStream.write(pass.getBytes(StandardCharsets.UTF_8));
                    fileOutputStream.write('\n');
                    fileOutputStream.close();
                    edtUser.setText(""); //xoa du lieu o editText
                    edtPass.setText("");
                    Toast.makeText(RegisterActivity.this,"Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception exception){
                    exception.printStackTrace();
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
