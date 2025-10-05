package com.example.ibanking_phanhedonghocphi.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chaos.view.PinView;
import com.example.ibanking_phanhedonghocphi.R;
import com.google.android.material.button.MaterialButton;

public class OtpBottomSheet extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
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

        PinView pinView = view.findViewById(R.id.pinView);
        MaterialButton btnSendAgain = view.findViewById(R.id.btnSendAgain);
        MaterialButton btnPay = view.findViewById(R.id.btnPay);

        btnSendAgain.setOnClickListener(v -> {
            Toast.makeText(getContext(), "OTP sent again!", Toast.LENGTH_SHORT).show();
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
}

