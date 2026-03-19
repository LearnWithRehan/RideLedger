package com.example.rideledger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Model.ReportModel;
import com.example.rideledger.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    Context context;
    List<ReportModel> list;

    public ReportAdapter(Context context, List<ReportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_expense_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ReportModel model = list.get(position);

        // DATE
        holder.tvDate.setText(model.getDate());

        // AMOUNT
        holder.tvAmount.setText("₹ " + model.getTotalAmount());

        // 👉 Optional: Color logic (high amount = red, low = green)
        if (model.getTotalAmount() > 1000) {
            holder.tvAmount.setTextColor(Color.parseColor("#D32F2F")); // red
        } else {
            holder.tvAmount.setTextColor(Color.parseColor("#2E7D32")); // green
        }

        // 👉 Optional: Type show (if needed)
     //   holder.tvType.setText("Daily Expense");
        holder.tvType.setText(model.getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 🔥 VIEW HOLDER
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvType, tvDate, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}