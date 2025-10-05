package com.example.ibanking_phanhedonghocphi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibanking_phanhedonghocphi.R;
import com.example.ibanking_phanhedonghocphi.model.TransactionItem;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<TransactionItem> transactionList;
//    private OnItemClickListener listener;
    private Context context;

//    public interface OnItemClickListener {
//        void onItemClick(TransactionItem item);
//    }

    public TransactionAdapter(Context context, List<TransactionItem> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionItem item = transactionList.get(position);

        Date date = new Date(); // Giá trị mặc định

        try {
            Instant instant = Instant.parse(item.getTimestamp());
            date = Date.from(instant);
        } catch (Exception e) {

        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvTimestamp.setText(sdf.format(date));
        holder.tvDescription.setText(item.getDescription());
        String amountText = ((item.getAmount() >= 0 ? "+" : "") + formatSoTien(item.getAmount()));
        holder.tvAmount.setText(amountText);
        if(item.getAmount()>= 0) {
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.tvAmountUnit.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.ivTransaction.setImageResource(R.drawable.down);
        } else {
            holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.tvAmountUnit.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.ivTransaction.setImageResource(R.drawable.up);

        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvAmount, tvTimestamp, tvAmountUnit;
        ImageView ivTransaction;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvAmountUnit = itemView.findViewById(R.id.tvAmountUnit);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivTransaction = itemView.findViewById(R.id.ivTransaction);
        }
    }

    private String formatSoTien(double hocPhi) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat formatterVND = NumberFormat.getCurrencyInstance(vietnam);
        return formatterVND.format(hocPhi).replace("₫", "");
    }
}
