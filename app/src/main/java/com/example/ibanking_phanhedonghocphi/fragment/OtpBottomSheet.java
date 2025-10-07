package com.example.ibanking_phanhedonghocphi.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.ibanking_phanhedonghocphi.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpBottomSheet extends BottomSheetDialogFragment {

    private ApiService apiService;
    private OTPNotificationServiceApi otpApi;
    private long userId;
    private String transactionId;
    TextView tv1;
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong("USER_ID", -1);
            transactionId = bundle.getString("TRANSACTION_ID", null);
            if (userId != -1) {
                getUserEmail(userId);
            } else {
                Toast.makeText(getContext(), "Không nhận được userId", Toast.LENGTH_SHORT).show();
            }
            if (transactionId == null) {
                Toast.makeText(getContext(), "Thiếu transactionId cho OTP", Toast.LENGTH_SHORT).show();
            }
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
            // Gọi API generate OTP
            otpApi = ApiClient.getOtpApiService();
            GenerateOtpRequest request = new GenerateOtpRequest(transactionId, BigInteger.valueOf(userId));
            otpApi.generateOtp(request).enqueue(new Callback<GenerateOtpResponse>() {
                @Override
                public void onResponse(Call<GenerateOtpResponse> call, Response<GenerateOtpResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Sau khi generate thành công => gửi email OTP
                        String newOtpId = response.body().getOtpId();
                        EmailRequest emailReq = new EmailRequest(BigInteger.valueOf(userId), newOtpId);
                        otpApi.sendEmail(emailReq).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> emailResp) {
                                if (emailResp.isSuccessful()) {
                                    Toast.makeText(getContext(), "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Gửi email OTP thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(), "Lỗi mạng khi gửi email OTP", Toast.LENGTH_SHORT).show();
                            }
                        });
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
}
