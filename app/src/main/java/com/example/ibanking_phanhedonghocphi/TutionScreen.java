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
import com.example.ibanking_phanhedonghocphi.api.TuitionApiClient;
import com.example.ibanking_phanhedonghocphi.api.ApiService;
import com.example.ibanking_phanhedonghocphi.model.Student;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutionScreen extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvHoTen, tvMSSV, tvHocPhi, textView9;
    Button btnPay;
    TextInputEditText edtMSSV;
    TableLayout tbl;
    LinearLayout OTPLayout;
    PinView pinView;
    private ApiService apiService;


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
        // Khởi tạo retrofit service
        apiService = TuitionApiClient.getClient().create(ApiService.class);

        toolbar = findViewById(R.id.toolbar);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvHocPhi = findViewById(R.id.tvHocPhi);
        tvMSSV = findViewById(R.id.tvMSSV);
        edtMSSV = findViewById(R.id.edtMSSV);
        tbl = findViewById(R.id.tbl);
        btnPay = findViewById(R.id.btnPay);
        OTPLayout = findViewById(R.id.OTPLayout);
        pinView = findViewById(R.id.pinView);
        textView9 = findViewById(R.id.textView9);

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
        // Gọi API lấy thông tin sinh viên
        apiService.getStudentById(mssv).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Student student = response.body();

                    // Hiển thị table và bật nút Pay
                    tbl.setVisibility(View.VISIBLE);
                    btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    btnPay.setEnabled(true);

                    // Hiển thị thông tin lên các TextView
                    tvMSSV.setText(student.getMSSV());
                    tvHoTen.setText(student.getFullName());
                    tvHocPhi.setText(String.valueOf(student.getTuitionFee()));
                    textView9.setText("Vui lòng nhập mã OTP được gửi về\n" + student.getMSSV() + "@student.tdtu.edu.vn");
                } else {
                    Toast.makeText(TutionScreen.this, "Không tìm thấy MSSV", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Toast.makeText(TutionScreen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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