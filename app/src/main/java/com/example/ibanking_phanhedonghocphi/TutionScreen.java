package com.example.ibanking_phanhedonghocphi;

import android.content.Intent;
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
                if (selectedStudent == null) {
                    Toast.makeText(TutionScreen.this, "Vui lòng nhập MSSV hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Ngăn thanh toán lại khi học phí đã bằng 0 (đã thanh toán)
                if (selectedStudent.getTuitionFee() == 0.0) {
                    Toast.makeText(TutionScreen.this, "Học phí đã thanh toán", Toast.LENGTH_LONG).show();
                    return;
                }
                // Khởi tạo payment: userId, mssv, amount
                // Fix BigDecimal creation to avoid parsing issues
                double tuitionFee = selectedStudent.getTuitionFee();
                // Gửi số âm để phù hợp rule mới của backend (amount âm = số tiền cần gạch nợ)
                java.math.BigDecimal amount = java.math.BigDecimal
                        .valueOf(tuitionFee)
                        .setScale(2, java.math.RoundingMode.HALF_UP)
                        .negate();
                

                
                PaymentInitRequest req = new PaymentInitRequest(
                        java.math.BigInteger.valueOf(userId),
                        selectedStudent.getMSSV(),
                        amount
                );
                // Debug: Log request details
                Toast.makeText(TutionScreen.this, "Đang gửi request: userId=" + userId + ", mssv=" + selectedStudent.getMSSV() + ", amount=" + selectedStudent.getTuitionFee(), Toast.LENGTH_LONG).show();
                
                paymentRepository.initiate(req).enqueue(new retrofit2.Callback<PaymentInitResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PaymentInitResponse> call, retrofit2.Response<PaymentInitResponse> response) {
                        // Debug: Log response details
                        String debugMsg = "Response code: " + response.code() + ", Success: " + response.isSuccessful();
                        if (response.body() != null) {
                            debugMsg += ", TransactionId: " + response.body().getTransactionId();
                        } else {
                            debugMsg += ", Body is null";
                        }
                        Toast.makeText(TutionScreen.this, debugMsg, Toast.LENGTH_LONG).show();
                        
                        if (response.isSuccessful() && response.body() != null) {
                            PaymentInitResponse res = response.body();
                            // lưu thông tin giao dịch để gửi email xác nhận sau khi confirm thành công
                            lastTransactionId = res.getTransactionId();
                            lastMssv = selectedStudent.getMSSV();
                            lastAmount = amount;
                            lastUserId = userId;
                            OtpBottomSheet otpBottomSheet = new OtpBottomSheet();
                            Bundle bundle = new Bundle();
                            bundle.putLong("USER_ID", userId);
                            bundle.putString("TRANSACTION_ID", res.getTransactionId());
                            bundle.putString("OTP_ID", res.getOtpId());
                            bundle.putString("MSSV", selectedStudent.getMSSV());
                            bundle.putString("AMOUNT", amount.toPlainString());
                            otpBottomSheet.setArguments(bundle);
                            
                            // Set callback để nhận kết quả payment
                            otpBottomSheet.setPaymentCallback(TutionScreen.this);
                            
                            otpBottomSheet.show(getSupportFragmentManager(), otpBottomSheet.getTag());
                        } else {
                            // More detailed error message
                            String errorMsg = "Khởi tạo giao dịch thất bại";
                            if (!response.isSuccessful()) {
                                errorMsg += " - HTTP " + response.code();
                                
                                // Handle specific error codes
                                if (response.code() == 409) {
                                    errorMsg = "Giao dịch đã tồn tại hoặc có xung đột dữ liệu (409)";
                                } else if (response.code() == 400) {
                                    errorMsg = "Dữ liệu không hợp lệ (400)";
                                } else if (response.code() == 404) {
                                    errorMsg = "Không tìm thấy tài nguyên (404)";
                                } else if (response.code() == 500) {
                                    errorMsg = "Lỗi server (500)";
                                }
                                
                                if (response.errorBody() != null) {
                                    try {
                                        String errorBody = response.errorBody().string();
                                        errorMsg += "\nChi tiết: " + errorBody;
                                    } catch (Exception e) {
                                        errorMsg += " (Không đọc được error body)";
                                    }
                                }
                            } else if (response.body() == null) {
                                errorMsg += " - Response body null";
                            }
                            Toast.makeText(TutionScreen.this, errorMsg, Toast.LENGTH_LONG).show();
                            
                            // Vẫn show BottomSheet nhưng chỉ có USER_ID (fallback)
                            OtpBottomSheet otpBottomSheet = new OtpBottomSheet();
                            Bundle bundle = new Bundle();
                            bundle.putLong("USER_ID", userId);
                            // Không có TRANSACTION_ID, OTP_ID, MSSV, AMOUNT
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

        // Gửi email xác nhận giao dịch (non-blocking). Bỏ qua nếu thiếu dữ liệu cần thiết
        try {
            if (lastTransactionId != null && lastAmount != null && lastMssv != null && lastUserId > 0) {
                OTPNotificationServiceApi otpApi = ApiClient.getOtpApiService();
                EmailRequest confirmEmail = new EmailRequest(
                        java.math.BigInteger.valueOf(lastUserId),
                        lastTransactionId,
                        lastAmount,
                        lastMssv
                );
                otpApi.sendEmail(confirmEmail).enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                        // Thông báo nhẹ, không chặn luồng
                        if (!response.isSuccessful()) {
                            Toast.makeText(TutionScreen.this, "Gửi email xác nhận thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                        Toast.makeText(TutionScreen.this, "Lỗi gửi email xác nhận: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception ignore) {}
        
        // Show success message
        Toast.makeText(this, "Thanh toán học phí thành công!", Toast.LENGTH_LONG).show();

        // Quay lại màn hình trước (Home) để người dùng thấy dữ liệu đã được reload
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onPaymentError(String error) {
        Toast.makeText(this, "Thanh toán thất bại: " + error, Toast.LENGTH_LONG).show();
    }

}