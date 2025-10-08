package com.example.ibanking_phanhedonghocphi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibanking_phanhedonghocphi.adapter.MenuAdapter;
import com.example.ibanking_phanhedonghocphi.api.AccountServiceApi;
import com.example.ibanking_phanhedonghocphi.api.ApiClient;
import com.example.ibanking_phanhedonghocphi.api.ApiService;
import com.example.ibanking_phanhedonghocphi.model.MenuItem;
import com.example.ibanking_phanhedonghocphi.model.User;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity {
    RecyclerView rvMenu;
    List<MenuItem> menuList;
    TextView tvBalacnce;
    ImageView ivEye;
    MenuAdapter adapter;
    Button btnAccount;
    TextView textView;
    private ApiService apiService;
    private AccountServiceApi accountServiceApi;
    private BroadcastReceiver paymentSuccessReceiver;

    Double balance = 0.0;

    private Map<String, Class<?>> activityMap;
    final boolean[] isHidden = {true};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiService = ApiClient.getUserApiService();
        // Lấy userID từ LoginScreen
        long userId = getIntent().getLongExtra("USER_ID", -1);
        //Toast.makeText(this, "USER_ID nhận được: " + userId, Toast.LENGTH_LONG).show();

        if (userId != -1) {
            // Gọi API lấy thông tin user
            apiService.getUserById(BigInteger.valueOf(userId)).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        Log.d("API_USER", "User response: " + new Gson().toJson(user));
                        textView.setText("Xin chào " + user.getFullName());
                    }
                    else {
                        Log.e("API_USER", "Error: " + response.code() + " - " + response.message());
                        textView.setText("Không tải được thông tin người dùng");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    textView.setText("Xin chào (lỗi tải tên)");
                }
            });

        }

        rvMenu = findViewById(R.id.rvMenu);
        tvBalacnce = findViewById(R.id.tvBalance);
        ivEye = findViewById(R.id.ivEye);
        btnAccount = findViewById(R.id.btnAccount);
        textView = findViewById(R.id.textView);
        accountServiceApi = ApiClient.getAccountApiService();

        // Mặc định ẩn số dư khi vào màn hình
        tvBalacnce.setText("********* VND");
        ivEye.setImageResource(R.drawable.eye);

        accountServiceApi.getBalance(User.getInstance().getUserId()).enqueue(new Callback<BigDecimal>() {
            @Override
            public void onResponse(Call<BigDecimal> call, Response<BigDecimal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    balance = response.body().doubleValue();
                } else {
                    balance = 0d;
                }
            }

            @Override
            public void onFailure(Call<BigDecimal> call, Throwable t) {
                balance = 0d;
            }
        });
        // Tạo danh sách chức năng
        menuList = new ArrayList<>();
        menuList.add(new MenuItem(R.drawable.transfer, "Chuyển tiền"));
        menuList.add(new MenuItem(R.drawable.credit_card, "Quản lý thẻ"));
        menuList.add(new MenuItem(R.drawable.edu, "Đóng học phí"));
        menuList.add(new MenuItem(R.drawable.invoice, "Thanh toán hóa đơn"));
        menuList.add(new MenuItem(R.drawable.plane, "Mua vé máy bay"));
        menuList.add(new MenuItem(R.drawable.setting, "Cài đặt"));

        activityMap = new HashMap<>();
        activityMap.put("Đóng học phí", TutionScreen.class);

        adapter = new MenuAdapter(this, menuList, new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                Class<?> targetActivity = activityMap.get(item.getTitle());
                if (targetActivity != null) {
                    Intent intent = new Intent(HomeScreen.this, targetActivity);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeScreen.this, "Chưa có chức năng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rvMenu.setLayoutManager(new GridLayoutManager(this, 3)); // 3 cột
        rvMenu.setAdapter(adapter);

        ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHidden[0]) {
                    double vnd = balance != null ? balance : 0d;
                    tvBalacnce.setText(formatHocPhi(vnd));
                    ivEye.setImageResource(R.drawable.eye_slash);
                } else {
                    tvBalacnce.setText("********* VND");
                    ivEye.setImageResource(R.drawable.eye);
                }
                isHidden[0] = !isHidden[0];
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeScreen.this, TransactionHistoryScreen.class);
                startActivity(myIntent);

            }
        });
        
        // Register BroadcastReceiver để nhận thông báo payment success
        paymentSuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.example.ibanking_phanhedonghocphi.PAYMENT_SUCCESS".equals(intent.getAction())) {
                    Log.d("HomeScreen", "Received payment success broadcast, refreshing balance...");
                    refreshBalance();
                }
            }
        };
        
        IntentFilter filter = new IntentFilter("com.example.ibanking_phanhedonghocphi.PAYMENT_SUCCESS");
        registerReceiver(paymentSuccessReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (paymentSuccessReceiver != null) {
            unregisterReceiver(paymentSuccessReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Mỗi lần quay lại màn hình, ẩn số dư và reset icon mắt
        isHidden[0] = true;
        tvBalacnce.setText("********* VND");
        ivEye.setImageResource(R.drawable.eye);
        // Refresh số dư nhưng không hiển thị nếu đang ẩn
        refreshBalance();
    }
    
    private void refreshBalance() {
        accountServiceApi.getBalance(User.getInstance().getUserId()).enqueue(new Callback<BigDecimal>() {
            @Override
            public void onResponse(Call<BigDecimal> call, Response<BigDecimal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    balance = response.body().doubleValue();
                    if (!isHidden[0]) {
                        tvBalacnce.setText(formatHocPhi(balance));
                    }
                    Log.d("HomeScreen", "Balance refreshed: " + balance);
                }
            }
            
            @Override
            public void onFailure(Call<BigDecimal> call, Throwable t) {
                Log.d("HomeScreen", "Failed to refresh balance: " + t.getMessage());
            }
        });
    }
    
    private String formatHocPhi(double hocPhi) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatterVND = NumberFormat.getCurrencyInstance(vietnam);
        return formatterVND.format(hocPhi).replace("₫", "VND");
    }
}