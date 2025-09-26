package com.example.ibanking_phanhedonghocphi;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

public class TutionScreen extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvHoTen, tvMSSV, tvHocPhi;
    Button btnPay;
    TextInputEditText edtMSSV;
    TableLayout tbl;
    LinearLayout OTPLayout;
    PinView pinView;


    boolean isOTPVisible = false;
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
        edtMSSV = findViewById(R.id.edtMSSV);
        tbl = findViewById(R.id.tbl);
        btnPay = findViewById(R.id.btnPay);
        OTPLayout = findViewById(R.id.OTPLayout);
        pinView = findViewById(R.id.pinView);

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

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOTPVisible) {
                    OTPLayout.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(TutionScreen.this, R.anim.slide_up);
                    OTPLayout.startAnimation(anim);
                    btnPay.setText("THANH TOÁN");
                    btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    btnPay.setEnabled(false);
                    pinView.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(pinView, InputMethodManager.SHOW_IMPLICIT);
                    isOTPVisible = true;
                } else {
                    Toast.makeText(TutionScreen.this, "Đang kiểm tra mã OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String otp = pinView.getText().toString().trim();
                if(otp.length() == 6) {
                    btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    btnPay.setEnabled(true);
                } else {
                    btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    btnPay.setEnabled(false);
                }
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
            tvHocPhi.setText(formatSoTien(12345000)); //Lay du lieu tu DB thong qua API
        } else if(mssv.equals("2")) {
            tvMSSV.setText("52300055");
            tvHoTen.setText("Ngô Xuân Quang");
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
//    private void animateOtpLayout() {
//        OTPLayout.setTranslationY(OTPLayout.getHeight());
//        OTPLayout.setVisibility(View.VISIBLE);
//        OTPLayout.animate()
//                .translationY(0)
//                .setDuration(500)
//                .start();
//    }
}