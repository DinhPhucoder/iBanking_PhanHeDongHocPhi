package com.example.ibanking_phanhedonghocphi;

import com.example.ibanking_phanhedonghocphi.api.ApiService;
import com.example.ibanking_phanhedonghocphi.api.ApiClient;
import com.example.ibanking_phanhedonghocphi.model.LoginRequest;
import com.example.ibanking_phanhedonghocphi.model.LoginResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class LoginScreen extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        etUsername = findViewById(R.id.edtUsername);
        etPassword = findViewById(R.id.edtPW);
        btnLogin = findViewById(R.id.btnLogin);

        apiService = ApiClient.getUserApiService();

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            LoginRequest request = new LoginRequest(username, password);

            apiService.login(request).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Lưu userID vào Application
                        UserApp.getInstance().setUserID(response.body().getUserID());

                        BigInteger userId = response.body().getUserID();
                        Toast.makeText(LoginScreen.this, "Login thành công", Toast.LENGTH_SHORT).show();

                        // Chuyển qua HomeActivity
                        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                        intent.putExtra("USER_ID", userId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginScreen.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginScreen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}