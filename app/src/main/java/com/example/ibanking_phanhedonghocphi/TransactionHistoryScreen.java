package com.example.ibanking_phanhedonghocphi;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibanking_phanhedonghocphi.adapter.TransactionAdapter;
import com.example.ibanking_phanhedonghocphi.model.TransactionItem;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionHistoryScreen extends AppCompatActivity {
    RecyclerView rcvHistory;
    MaterialToolbar toolbar;
    TextView tvBalance;
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

        tvBalance.setText(formatSoTien(1234000));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rcvHistory.setLayoutManager(new LinearLayoutManager(this));
        List<TransactionItem> list = new ArrayList<>();
        list.add(new TransactionItem(System.currentTimeMillis(), 300000000, "Nạp tiền"));
        list.add(new TransactionItem(System.currentTimeMillis(), -20000, "Mua cà phê"));
        list.add(new TransactionItem(System.currentTimeMillis(), -13450000, "Thanh toán học phí HK1-25/26 52300051"));
        list.add(new TransactionItem(System.currentTimeMillis(), -20000, "Mua cà phê"));
        list.add(new TransactionItem(System.currentTimeMillis(), -20000, "Mua cà phê"));
        list.add(new TransactionItem(System.currentTimeMillis(), -20000, "Mua cà phê"));
        list.add(new TransactionItem(System.currentTimeMillis(), -20000, "Mua cà phê"));
        list.add(new TransactionItem(System.currentTimeMillis(), -20000, "Mua cà phê"));
        list.add(new TransactionItem(System.currentTimeMillis(), -20000, "Mua cà phê"));


        TransactionAdapter adapter = new TransactionAdapter(this, list);

        rcvHistory.setAdapter(adapter);
    }

    private String formatSoTien(double hocPhi) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatterVND = NumberFormat.getCurrencyInstance(vietnam);
        return formatterVND.format(hocPhi).replace("₫", "VND");
    }
}