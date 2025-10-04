package com.example.ibanking_phanhedonghocphi;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.ibanking_phanhedonghocphi.api.ApiClient;
import com.example.ibanking_phanhedonghocphi.api.ApiService;
import com.example.ibanking_phanhedonghocphi.model.MenuItem;
import com.example.ibanking_phanhedonghocphi.model.User;

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
        apiService = ApiClient.getClient().create(ApiService.class);
        // Lấy userID từ LoginScreen
        long userId = getIntent().getLongExtra("USER_ID", -1);
        //Toast.makeText(this, "USER_ID nhận được: " + userId, Toast.LENGTH_LONG).show();

        if (userId != -1) {
            // Gọi API lấy thông tin user
            apiService.getUserById(userId).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String fullName = response.body().getFullName();
                        textView.setText("Xin chào " + fullName);
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
                    double vnd = 13450000; //Lay du lieu tu DB thong qua API
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
    }
    private String formatHocPhi(double hocPhi) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatterVND = NumberFormat.getCurrencyInstance(vietnam);
        return formatterVND.format(hocPhi).replace("₫", "VND");
    }
}