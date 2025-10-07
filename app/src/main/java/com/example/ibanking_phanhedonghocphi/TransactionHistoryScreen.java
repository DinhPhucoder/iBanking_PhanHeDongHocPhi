package com.example.ibanking_phanhedonghocphi;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibanking_phanhedonghocphi.adapter.TransactionAdapter;
import com.example.ibanking_phanhedonghocphi.api.AccountServiceApi;
import com.example.ibanking_phanhedonghocphi.api.ApiService;
import com.example.ibanking_phanhedonghocphi.api.TuitionServiceApi;
import com.example.ibanking_phanhedonghocphi.model.Student;
import com.example.ibanking_phanhedonghocphi.model.TransactionItem;
import com.example.ibanking_phanhedonghocphi.model.User;
import com.google.android.material.appbar.MaterialToolbar;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.ibanking_phanhedonghocphi.api.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryScreen extends AppCompatActivity {
    RecyclerView rcvHistory;
    MaterialToolbar toolbar;
    TextView tvBalance, tvFullName, tvSDT, tvMail;

    List<TransactionItem> list = new ArrayList<>();
    double balance = 0d;


    private AccountServiceApi accountServiceApi;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_history_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rcvHistory = findViewById(R.id.rcvHistory);
        toolbar = findViewById(R.id.toolbar2);
        tvBalance = findViewById(R.id.tvBalance);
        tvFullName = findViewById(R.id.tvFullName);
        tvSDT = findViewById(R.id.tvSDT);
        tvMail = findViewById(R.id.tvMail);

        accountServiceApi = ApiClient.getAccountApiService();
        apiService = ApiClient.getUserApiService();

        accountServiceApi.getBalance(User.getInstance().getUserId()).enqueue(new Callback<BigDecimal>() {
            @Override
            public void onResponse(Call<BigDecimal> call, Response<BigDecimal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    balance = response.body().doubleValue();
                } else {
                    balance = 0d; // fallback 0, không hiện lỗi
                }
                tvBalance.setText(formatSoTien(balance));
            }

            @Override
            public void onFailure(Call<BigDecimal> call, Throwable t) {
                balance = 0d; // fallback 0
                tvBalance.setText(formatSoTien(balance));
            }
        });

        apiService.getUserById(User.getInstance().getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    tvFullName.setText(user.getFullName());
                    tvMail.setText(user.getEmail());
                    tvSDT.setText(user.getPhone());
                } else {
                    Toast.makeText(TransactionHistoryScreen.this, "Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(TransactionHistoryScreen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rcvHistory.setLayoutManager(new LinearLayoutManager(this));


        accountServiceApi.getHistory(User.getInstance().getUserId()).enqueue(new Callback<List<TransactionItem>>() {
            @Override
            public void onResponse(Call<List<TransactionItem>> call, Response<List<TransactionItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list = response.body();
                } else {
                    list = new ArrayList<>(); // fallback empty
                }
                updateTransactionList(list);
            }
            @Override
            public void onFailure(Call<List<TransactionItem>> call, Throwable t) {
                updateTransactionList(new ArrayList<>()); // fallback empty, không văng app
            }
        });
    }

    private String formatSoTien(double hocPhi) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatterVND = NumberFormat.getCurrencyInstance(vietnam);
        return formatterVND.format(hocPhi).replace("₫", "VND");
    }
    private void updateTransactionList(List<TransactionItem> list) {
        TransactionAdapter adapter = new TransactionAdapter(this, list);
        rcvHistory.setAdapter(adapter);
    }
}