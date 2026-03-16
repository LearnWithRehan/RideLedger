package com.example.rideledger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Model.RideModel;
import com.example.rideledger.R;

import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.MyViewHolder> {

    Context context;
    List<RideModel> list;

    public RideAdapter(Context context, List<RideModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_ride, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RideModel model = list.get(position);

        holder.txtDate.setText(model.date);
        holder.txtPlatform.setText(model.platform);
        holder.txtCash.setText("₹ " + model.cash);
        holder.txtOnline.setText("₹ " + model.online);
        holder.txtFuel.setText("₹ " + model.fuel);
        holder.txtCng.setText("₹ " + model.cng);
        holder.txtTotal.setText("₹ " + model.total);
        holder.txtProfit.setText("₹ " + model.profit);

        holder.btnDelete.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {

                list.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, list.size());

            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtPlatform, txtCash, txtOnline, txtFuel, txtCng, txtTotal, txtProfit;
        ImageView btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtPlatform = itemView.findViewById(R.id.txtPlatform);
            txtCash = itemView.findViewById(R.id.txtCash);
            txtOnline = itemView.findViewById(R.id.txtOnline);
            txtFuel = itemView.findViewById(R.id.txtFuel);
            txtCng = itemView.findViewById(R.id.txtCng);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtProfit = itemView.findViewById(R.id.txtProfit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}