package com.example.budgetwisesolutions.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetwisesolutions.R;
import com.example.budgetwisesolutions.database.UserDb;
import com.example.budgetwisesolutions.model.UserModel;

import java.io.FileInputStream;

public class LoginActivity extends AppCompatActivity {

    TextView tvRegister, tvForgetPassword;
    EditText edtEmailLogin, edtPassLogin;  // Đổi edtUserLogin thành edtEmailLogin
    Button btnSubmit;
    UserDb userDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegister = findViewById(R.id.tvRegister);
        edtEmailLogin = findViewById(R.id.edtEmailLogin);  // Thay thế edtUserLogin
        edtPassLogin = findViewById(R.id.edtPassLogin);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        userDb = new UserDb(LoginActivity.this);

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        LoginWithDataBaseSQLite();
    }

    private void LoginWithDataFile(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtEmailLogin.getText().toString().trim();
                String pass = edtPassLogin.getText().toString().trim();
                if(TextUtils.isEmpty(user)){
                    edtEmailLogin.setError("Email can be not empty");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    edtPassLogin.setError("Password can be not empty");
                    return;
                }
                //doc du lieu tu file trong local storage
                //check du lieu nguoi dung dang nhap
                try{
                    FileInputStream fileInputStream = openFileInput("account.txt");
                    int read = -1;
                    StringBuilder builder = new StringBuilder();
                    while((read = fileInputStream.read()) != -1){
                        builder.append((char) read);
                    }
                    boolean checkLogin = false;
                    String[] infoAccount = null; //chua tat ca thong tin tai khoan
                    infoAccount = builder.toString().trim().split("\n");
                    for(String account : infoAccount){
                        String email = account.substring(0,account.indexOf("|")).trim();
                        String password = account.substring(account.indexOf("|")+1).trim();
                        if(email.equals(email) && password.equals(pass)){
                            checkLogin = true;
                            break;
                        }
                    }
                    if(checkLogin){
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                        Bundle bundle = new Bundle();
                        bundle.putString("MY_Email", user);

                        intent.putExtras(bundle); //dong goi
                        startActivity(intent); // gui di sang activity khac
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "Account Invalid",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    throw new RuntimeException();
                }



            }
        });
    }

    private void LoginWithDataBaseSQLite() {
        btnSubmit.setOnClickListener(view -> {
            String email = edtEmailLogin.getText().toString().trim();  // Đổi user thành email
            String pass = edtPassLogin.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                edtEmailLogin.setError("Email cannot be empty");
                return;
            }

            if (TextUtils.isEmpty(pass)) {
                edtPassLogin.setError("Password cannot be empty");
                return;
            }

            try {
                UserModel checkLogin = userDb.checkUserLogin(email, pass);  // Sửa phương thức kiểm tra

                if (checkLogin != null && checkLogin.getEmail() != null) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID_USER", checkLogin.getId());
                    bundle.putString("MY_Email", email);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Account", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

