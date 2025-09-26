package com.example.ibanking_phanhedonghocphi;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

public class TutionScreen extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvHoTen, tvMSSV, tvHocPhi, tvHocKi;
    Button btnPay;
    TextInputEditText edtMSSV;
    TableLayout tbl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tution_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvHocPhi = findViewById(R.id.tvHocPhi);
        tvMSSV = findViewById(R.id.tvMSSV);
        tvHocKi = findViewById(R.id.tvHocKi);
        edtMSSV = findViewById(R.id.edtMSSV);
        tbl = findViewById(R.id.tbl);
        btnPay = findViewById(R.id.btnPay);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        edtMSSV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String mssv = edtMSSV.getText().toString().trim();
                if(!mssv.isEmpty()) {
                    showStudentInfo(mssv);
                }
                return false;
            }
        });
    }
    private void showStudentInfo(String mssv) {
        tbl.setVisibility(View.VISIBLE);
        btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        btnPay.setEnabled(true);
        if(mssv.equals("1")) {
            tvMSSV.setText("52300051");
            tvHoTen.setText("Phan Đình Phú");
            tvHocKi.setText("HK1-2025/26");
            tvHocPhi.setText(formatSoTien(12345000)); //Lay du lieu tu DB thong qua API
        } else if(mssv.equals("2")) {
            tvMSSV.setText("52300055");
            tvHoTen.setText("Ngô Xuân Quang");
            tvHocKi.setText("HK1-2025/26");
            tvHocPhi.setText(formatSoTien(1234500)); //Lay du lieu tu DB thong qua API
        } else {
            Toast.makeText(TutionScreen.this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
            tbl.setVisibility(View.GONE);
            btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            btnPay.setEnabled(false);
        }
    }
    private String formatSoTien(double hocPhi) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatterVND = NumberFormat.getCurrencyInstance(vietnam);
        return formatterVND.format(hocPhi).replace("₫", "VND");
    }
}