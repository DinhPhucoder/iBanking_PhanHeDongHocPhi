package com.example.ibanking_phanhedonghocphi;

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

import com.chaos.view.PinView;
import com.example.ibanking_phanhedonghocphi.api.ApiClient;
import com.example.ibanking_phanhedonghocphi.api.ApiService;
import com.example.ibanking_phanhedonghocphi.api.TuitionServiceApi;
import com.example.ibanking_phanhedonghocphi.fragment.OtpBottomSheet;
import com.example.ibanking_phanhedonghocphi.repository.PaymentRepository;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitResponse;
import com.example.ibanking_phanhedonghocphi.model.Student;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutionScreen extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvHoTen, tvMSSV, tvHocPhi, tvStatus;
    Button btnPay, btnContinue;
    TextInputEditText edtMSSV;
    TableLayout tbl;
    PinView pinView;
    private TuitionServiceApi tuitionServiceApi;
    private PaymentRepository paymentRepository;
    private Student selectedStudent;


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
        tuitionServiceApi = ApiClient.getTuitionApiService();
        paymentRepository = new PaymentRepository();

        toolbar = findViewById(R.id.toolbar);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvHocPhi = findViewById(R.id.tvHocPhi);
        tvMSSV = findViewById(R.id.tvMSSV);
        tvStatus = findViewById(R.id.tvStatus);
        edtMSSV = findViewById(R.id.edtMSSV);
        tbl = findViewById(R.id.tbl);
//        btnPay = findViewById(R.id.btnPay);
        btnContinue = findViewById(R.id.btnContinue);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        long userId = getIntent().getLongExtra("USER_ID", -1);
        //Toast.makeText(this, "USER_ID nhận được: " + userId, Toast.LENGTH_LONG).show();


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

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtpBottomSheet otpBottomSheet = new OtpBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putLong("USER_ID", userId); //  Truyền userId vào Bundle
                otpBottomSheet.setArguments(bundle); //  Gắn bundle vào OtpBottomSheet
                otpBottomSheet.show(getSupportFragmentManager(), otpBottomSheet.getTag());
                if (selectedStudent == null) {
                    Toast.makeText(TutionScreen.this, "Vui lòng nhập MSSV hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Khởi tạo payment: userId, mssv, amount
                PaymentInitRequest req = new PaymentInitRequest(
                        java.math.BigInteger.valueOf(userId),
                        selectedStudent.getMSSV(),
                        java.math.BigDecimal.valueOf(selectedStudent.getTuitionFee())
                );
                paymentRepository.initiate(req).enqueue(new retrofit2.Callback<PaymentInitResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PaymentInitResponse> call, retrofit2.Response<PaymentInitResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PaymentInitResponse res = response.body();
                            OtpBottomSheet otpBottomSheet = new OtpBottomSheet();
                            Bundle bundle = new Bundle();
                            bundle.putLong("USER_ID", userId);
                            bundle.putString("TRANSACTION_ID", res.getTransactionId());
                            bundle.putString("OTP_ID", res.getOtpId() != null ? res.getOtpId().toString() : null);
                            bundle.putString("MSSV", selectedStudent.getMSSV());
                            bundle.putDouble("AMOUNT", selectedStudent.getTuitionFee());
                            otpBottomSheet.setArguments(bundle);
                            otpBottomSheet.show(getSupportFragmentManager(), otpBottomSheet.getTag());
                        } else {
                            Toast.makeText(TutionScreen.this, "Khởi tạo giao dịch thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PaymentInitResponse> call, Throwable t) {
                        Toast.makeText(TutionScreen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void showStudentInfo(String mssv) {
        // Gọi API lấy thông tin sinh viên
        tuitionServiceApi.getStudentById(mssv).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Student student = response.body();
                    selectedStudent = student; // giữ nguyên hiển thị cũ, chỉ gán để dùng khi initiate

                    tbl.setVisibility(View.VISIBLE);
                    btnContinue.setEnabled(true);
                    // Hiển thị thông tin lên các TextView
                    tvMSSV.setText(student.getMSSV());
                    tvHoTen.setText(student.getFullName());
                    tvStatus.setText(student.getStatus());
                    //tvHocPhi.setText(String.valueOf(student.getTuitionFee()));
                    tvHocPhi.setText(formatSoTien(student.getTuitionFee()));
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


}