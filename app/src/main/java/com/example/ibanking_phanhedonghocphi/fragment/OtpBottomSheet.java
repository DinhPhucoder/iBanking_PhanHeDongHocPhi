package com.example.ibanking_phanhedonghocphi.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chaos.view.PinView;
import com.example.ibanking_phanhedonghocphi.R;
import com.example.ibanking_phanhedonghocphi.api.ApiClient;
import com.example.ibanking_phanhedonghocphi.api.ApiService;
import com.example.ibanking_phanhedonghocphi.api.OTPNotificationServiceApi;
import com.example.ibanking_phanhedonghocphi.model.dtoOtp.GenerateOtpRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoOtp.GenerateOtpResponse;
import com.example.ibanking_phanhedonghocphi.model.dtoOtp.EmailRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentConfirmRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentConfirmResponse;
import com.example.ibanking_phanhedonghocphi.model.User;
import com.example.ibanking_phanhedonghocphi.repository.PaymentRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpBottomSheet extends BottomSheetDialogFragment {

    private ApiService apiService;
    private OTPNotificationServiceApi otpApi;
    private PaymentRepository paymentRepository;
    private long userId;
    private String transactionId;
    private String otpId;
    private String mssv;
    private String amount;
    private String generatedOtpId; // OTP ID mới được generate
    TextView tv1;
    
    // Callback interface để thông báo kết quả về TutionScreen
    public interface PaymentCallback {
        void onPaymentSuccess();
        void onPaymentError(String error);
    }
    
    private PaymentCallback paymentCallback;
    
    // Method để set callback
    public void setPaymentCallback(PaymentCallback callback) {
        this.paymentCallback = callback;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo PaymentRepository
        paymentRepository = new PaymentRepository();
        
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong("USER_ID", -1);
            transactionId = bundle.getString("TRANSACTION_ID", null);
            otpId = bundle.getString("OTP_ID", null);
            mssv = bundle.getString("MSSV", null);
            amount = bundle.getString("AMOUNT", null);
            
            // Debug: Log Bundle data
            String debugMsg = "OtpBottomSheet received: userId=" + userId + 
                ", transactionId=" + transactionId + 
                ", otpId=" + otpId + 
                ", mssv=" + mssv + 
                ", amount=" + amount;
            Log.d("OtpBottomSheet", debugMsg);
            
            if (userId != -1) {
                // Chỉ hiển thị email người dùng; OTP đã được backend gửi khi initiate
                getUserEmail(userId);
            } else {
                Toast.makeText(getContext(), "Không nhận được userId", Toast.LENGTH_SHORT).show();
            }
            if (transactionId == null) {
                Toast.makeText(getContext(), "Thiếu transactionId cho OTP", Toast.LENGTH_SHORT).show();
            }
            if (otpId == null) {
                Toast.makeText(getContext(), "Thiếu otpId cho confirm payment", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Bundle is null!", Toast.LENGTH_SHORT).show();
        }


        PinView pinView = view.findViewById(R.id.pinView);
        MaterialButton btnSendAgain = view.findViewById(R.id.btnSendAgain);
        MaterialButton btnPay = view.findViewById(R.id.btnPay);
        tv1 = view.findViewById(R.id.tv1);
        TextView tvCountdown = view.findViewById(R.id.tvCountDown);

        pinView.requestFocus();
        pinView.postDelayed(() -> {
            InputMethodManager imm =
                    (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(pinView, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);

        btnSendAgain.setOnClickListener(v -> {
            // Gọi API generate OTP và để PaymentService gửi email (hoặc tự gửi một lần nữa nếu muốn)
            otpApi = ApiClient.getOtpApiService();
            GenerateOtpRequest request = new GenerateOtpRequest(transactionId, BigInteger.valueOf(userId));
            otpApi.generateOtp(request).enqueue(new Callback<GenerateOtpResponse>() {
                @Override
                public void onResponse(Call<GenerateOtpResponse> call, Response<GenerateOtpResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String newOtpId = response.body().getOtpId();
                        // Cập nhật otpId để confirm bằng mã mới
                        otpId = newOtpId;
                        Toast.makeText(getContext(), "Đã tạo lại OTP. Vui lòng kiểm tra email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Gửi lại OTP thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GenerateOtpResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng khi gửi lại OTP", Toast.LENGTH_SHORT).show();
                }
            });
        });

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) { }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String otp = pinView.getText().toString().trim();
                if (otp.length() == 6) {
                    btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    btnPay.setEnabled(true);
                } else {
                    btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    btnPay.setEnabled(false);
                }
            }
        });

        // Xử lý khi bấm nút "Thanh toán"
        btnPay.setOnClickListener(v -> {
            String otpCode = pinView.getText().toString().trim();
            if (otpCode.length() != 6) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ 6 số OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (transactionId == null || otpId == null) {
                Toast.makeText(getContext(), "Thiếu thông tin giao dịch", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Gọi API confirm payment
            // Sử dụng OTP ID từ PaymentInitResponse (không phải generatedOtpId)
            if (otpId != null && !otpId.isEmpty()) {
                Log.d("OtpBottomSheet", "Using OTP ID from PaymentInit: " + otpId);
                confirmPayment(transactionId, otpId, otpCode);
            } else {
                Toast.makeText(getContext(), "Thiếu OTP ID. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvCountdown.setText("Mã OTP sẽ hết hạn sau " + millisUntilFinished / 1000 + " giây");
            }

            public void onFinish() {
                tvCountdown.setText("OTP đã hết hạn. Vui lòng gửi lại mã.");
                btnSendAgain.setEnabled(true);
            }
        }.start();
    }
    
    private void confirmPayment(String transactionId, String otpId, String otpCode) {
        Log.d("OtpBottomSheet", "Confirming payment with: transactionId=" + transactionId + ", otpId=" + otpId + ", otpCode=" + otpCode);
        
        PaymentConfirmRequest request = new PaymentConfirmRequest(transactionId, otpId, otpCode);
        Log.d("OtpBottomSheet", "PaymentConfirmRequest created: transactionId=" + request.getTransactionId() + ", otpId=" + request.getOtpId() + ", otpCode=" + request.getOtpCode());
        
        paymentRepository.confirm(request).enqueue(new Callback<PaymentConfirmResponse>() {
            @Override
            public void onResponse(Call<PaymentConfirmResponse> call, Response<PaymentConfirmResponse> response) {
                Log.d("OtpBottomSheet", "Payment confirm response: code=" + response.code() + ", success=" + response.isSuccessful());
                
                if (response.isSuccessful() && response.body() != null) {
                    PaymentConfirmResponse confirmResponse = response.body();
                    String status = confirmResponse.getStatus();
                    Log.d("OtpBottomSheet", "Payment confirm result: status=" + status + ", transactionId=" + confirmResponse.getTransactionId());
                    
                    if ("SUCCESS".equals(status)) {
                        Toast.makeText(getContext(), "Thanh toán thành công!", Toast.LENGTH_LONG).show();
                        
                        // Gọi callback để thông báo kết quả về TutionScreen
                        if (paymentCallback != null) {
                            paymentCallback.onPaymentSuccess();
                        }
                        
                        // Đóng BottomSheet và quay về màn hình trước
                        dismiss();
                    } else {
                        String errorMsg = "Thanh toán thất bại: " + status;
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        
                        // Gọi callback để thông báo lỗi về TutionScreen
                        if (paymentCallback != null) {
                            paymentCallback.onPaymentError(errorMsg);
                        }
                    }
                } else {
                    String errorMsg = "Xác nhận thanh toán thất bại";
                    if (!response.isSuccessful()) {
                        errorMsg += " - HTTP " + response.code();
                        
                        if (response.code() == 400) {
                            errorMsg = "Mã OTP không đúng hoặc đã hết hạn (400)";
                        } else if (response.code() == 404) {
                            errorMsg = "Không tìm thấy giao dịch (404)";
                        } else if (response.code() == 409) {
                            errorMsg = "Giao dịch đã được xử lý (409)";
                        }
                    }
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentConfirmResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void getUserEmail(long userId) {
        apiService = ApiClient.getUserApiService();

        apiService.getUserById(BigInteger.valueOf(userId)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String email = response.body().getEmail();
                    tv1.setText("Vui lòng nhập mã OTP được gửi về " + email);
                } else {
                    Toast.makeText(getContext(), "Không lấy được email người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối khi lấy email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtpEmail() {
        Log.d("OtpBottomSheet", "Sending OTP email with existing OTP ID: " + otpId);
        otpApi = ApiClient.getOtpRetrofit().create(OTPNotificationServiceApi.class);
        
        // Gửi email với OTP ID từ PaymentInitResponse
        EmailRequest emailReq = new EmailRequest(BigInteger.valueOf(userId), otpId);
        otpApi.sendEmail(emailReq).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> emailResp) {
                if (emailResp.isSuccessful()) {
                    Log.d("OtpBottomSheet", "OTP email sent successfully");
                    Toast.makeText(getContext(), "Đã gửi mã OTP đến email của bạn", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("OtpBottomSheet", "Failed to send OTP email: " + emailResp.code());
                    Toast.makeText(getContext(), "Lỗi gửi email OTP: " + emailResp.code(), Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("OtpBottomSheet", "Failed to send OTP email: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi gửi email: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
