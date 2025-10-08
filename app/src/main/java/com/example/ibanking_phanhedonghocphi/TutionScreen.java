package com.example.ibanking_phanhedonghocphi;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.ibanking_phanhedonghocphi.fragment.OtpBottomSheet;
import com.example.ibanking_phanhedonghocphi.api.ApiClient;
import com.example.ibanking_phanhedonghocphi.api.ApiService;
import com.example.ibanking_phanhedonghocphi.api.TuitionServiceApi;
import com.example.ibanking_phanhedonghocphi.api.OTPNotificationServiceApi;
import com.example.ibanking_phanhedonghocphi.repository.PaymentRepository;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitResponse;
import com.example.ibanking_phanhedonghocphi.model.Student;
import com.example.ibanking_phanhedonghocphi.model.dtoOtp.EmailRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutionScreen extends AppCompatActivity implements OtpBottomSheet.PaymentCallback {
    Toolbar toolbar;
    TextView tvHoTen, tvMSSV, tvHocPhi, tvStatus;
    Button btnPay, btnContinue;
    TextInputEditText edtMSSV;
    TableLayout tbl;
    PinView pinView;
    private TuitionServiceApi tuitionServiceApi;
    private PaymentRepository paymentRepository;
    private Student selectedStudent;
    private String lastTransactionId;
    private String lastMssv;
    private java.math.BigDecimal lastAmount;
    private long lastUserId;
    private static final String PREFS_NAME = "tuition_payment_prefs";
    private static final String KEY_TX_ID = "pending_tx_id";
    private static final String KEY_MSSV = "pending_mssv";
    private static final String KEY_AMOUNT = "pending_amount";
    private static final String KEY_USER_ID = "pending_user_id";


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
                paymentRepository.getPending(java.math.BigInteger.valueOf(userId)).enqueue(new retrofit2.Callback<com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitResponse> call, retrofit2.Response<com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitResponse res = response.body();
                            // Mở lại OTP với transactionId pending
                            lastTransactionId = res.getTransactionId();
                            lastMssv = selectedStudent != null ? selectedStudent.getMSSV() : null;
                            lastUserId = userId;
                            OtpBottomSheet otpBottomSheet = new OtpBottomSheet();
                            Bundle bundle = new Bundle();
                            bundle.putLong("USER_ID", userId);
                            bundle.putString("TRANSACTION_ID", res.getTransactionId());
                            bundle.putString("OTP_ID", res.getOtpId());
                            bundle.putString("MSSV", lastMssv);
                            bundle.putString("AMOUNT", lastAmount != null ? lastAmount.toPlainString() : null);
                            otpBottomSheet.setArguments(bundle);
                            otpBottomSheet.setPaymentCallback(TutionScreen.this);
                            otpBottomSheet.show(getSupportFragmentManager(), otpBottomSheet.getTag());
                        } else if (response.code() == 204) {
                            // Không có pending -> tiếp tục luồng initiate như cũ
                            proceedInitiate(userId);
                        } else {
                            proceedInitiate(userId);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitResponse> call, Throwable t) {
                        // Lỗi mạng -> fallback initiate
                        proceedInitiate(userId);
                    }
                });
            }
        });

    }

    private void proceedInitiate(long userId) {
        if (selectedStudent == null) {
            Toast.makeText(TutionScreen.this, "Vui lòng nhập MSSV hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedStudent.getTuitionFee() == 0.0) {
            Toast.makeText(TutionScreen.this, "Học phí đã thanh toán", Toast.LENGTH_LONG).show();
            return;
        }
        double tuitionFee = selectedStudent.getTuitionFee();
        java.math.BigDecimal amount = java.math.BigDecimal
                .valueOf(tuitionFee)
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .negate();

        PaymentInitRequest req = new PaymentInitRequest(
                java.math.BigInteger.valueOf(userId),
                selectedStudent.getMSSV(),
                amount
        );

        paymentRepository.initiate(req).enqueue(new retrofit2.Callback<PaymentInitResponse>() {
            @Override
            public void onResponse(retrofit2.Call<PaymentInitResponse> call, retrofit2.Response<PaymentInitResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PaymentInitResponse res = response.body();
                    lastTransactionId = res.getTransactionId();
                    lastMssv = selectedStudent.getMSSV();
                    lastAmount = amount;
                    lastUserId = userId;
                    savePendingPayment(lastTransactionId, lastMssv, lastAmount, lastUserId);
                    OtpBottomSheet otpBottomSheet = new OtpBottomSheet();
                    Bundle bundle = new Bundle();
                    bundle.putLong("USER_ID", userId);
                    bundle.putString("TRANSACTION_ID", res.getTransactionId());
                    bundle.putString("OTP_ID", res.getOtpId());
                    bundle.putString("MSSV", selectedStudent.getMSSV());
                    bundle.putString("AMOUNT", amount.toPlainString());
                    otpBottomSheet.setArguments(bundle);
                    otpBottomSheet.setPaymentCallback(TutionScreen.this);
                    otpBottomSheet.show(getSupportFragmentManager(), otpBottomSheet.getTag());
                } else {
                    String errorMsg = "Khởi tạo giao dịch thất bại";
                    if (!response.isSuccessful()) {
                        errorMsg += " - HTTP " + response.code();
                        if (response.code() == 409) {
                            errorMsg = "Sinh viên đang xử lý. Vui lòng thử lại sau";
                        } else if (response.code() == 400) {
                            errorMsg = "Dữ liệu không hợp lệ (400)";
                        } else if (response.code() == 404) {
                            errorMsg = "Không tìm thấy tài nguyên (404)";
                        } else if (response.code() == 500) {
                            errorMsg = "Lỗi server (500)";
                        }
                        if (response.errorBody() != null) {
                            try { String errorBody = response.errorBody().string(); errorMsg += "\nChi tiết: " + errorBody; } catch (Exception e) { errorMsg += " (Không đọc được error body)"; }
                        }
                    } else if (response.body() == null) {
                        errorMsg += " - Response body null";
                    }
                    Toast.makeText(TutionScreen.this, errorMsg, Toast.LENGTH_LONG).show();
                    OtpBottomSheet otpBottomSheet = new OtpBottomSheet();
                    Bundle bundle = new Bundle();
                    bundle.putLong("USER_ID", userId);
                    otpBottomSheet.setArguments(bundle);
                    otpBottomSheet.show(getSupportFragmentManager(), otpBottomSheet.getTag());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PaymentInitResponse> call, Throwable t) {
                Toast.makeText(TutionScreen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void savePendingPayment(String txId, String mssv, java.math.BigDecimal amount, long userId) {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit()
                    .putString(KEY_TX_ID, txId)
                    .putString(KEY_MSSV, mssv)
                    .putString(KEY_AMOUNT, amount != null ? amount.toPlainString() : null)
                    .putLong(KEY_USER_ID, userId)
                    .apply();
        } catch (Exception ignore) {}
    }

    private void clearPendingPayment() {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit().clear().apply();
        } catch (Exception ignore) {}
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

    // Implement PaymentCallback methods
    @Override
    public void onPaymentSuccess() {

        // Refresh student info để cập nhật trạng thái học phí
        if (selectedStudent != null) {
            showStudentInfo(selectedStudent.getMSSV());
        }
        
        // Broadcast intent để các màn hình khác refresh data
        Intent refreshIntent = new Intent("com.example.ibanking_phanhedonghocphi.PAYMENT_SUCCESS");
        sendBroadcast(refreshIntent);

        
        
        // Show success message
        Toast.makeText(this, "Thanh toán học phí thành công!", Toast.LENGTH_LONG).show();

        // Clear pending lưu trên máy (không bắt buộc, để đồng bộ với backend)
        clearPendingPayment();

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onPaymentError(String error) {
        Toast.makeText(this, "Thanh toán thất bại: " + error, Toast.LENGTH_LONG).show();
        // Xóa pending local để tránh kẹt trạng thái
        try { clearPendingPayment(); } catch (Exception ignore) {}
        // Quay về Home
        setResult(RESULT_CANCELED);
        finish();
    }

}