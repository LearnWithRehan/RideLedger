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

public class EntryReideAdapter extends RecyclerView.Adapter<EntryReideAdapter.ViewHolder>{

    Context context;
    List<RideModel> list;
    OnDeleteClick listener;


    public EntryReideAdapter(Context context, List<RideModel> list, OnDeleteClick listener){
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_today_ride,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){

        RideModel model = list.get(position);




        holder.tvPlatform.setText(model.getPlatform());

        holder.tvCash.setText("Cash ₹ " + model.getCash());
        holder.tvOnline.setText("Online ₹ " + model.getOnline());

        holder.tvFuel.setText("Fuel ₹ " + model.getFuel());
        holder.tvCng.setText("CNG ₹ " + model.getCng());

        holder.tvProfit.setText("Profit ₹ " + model.getProfit());
        holder.btnDelete.setOnClickListener(v -> {
            listener.onDelete(model, position);
        });

    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvPlatform,tvCash,tvOnline,tvFuel,tvCng,tvProfit;
        ImageView btnDelete;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tvPlatform = itemView.findViewById(R.id.tvPlatform);
            tvCash = itemView.findViewById(R.id.tvCash);
            tvOnline = itemView.findViewById(R.id.tvOnline);
            tvFuel = itemView.findViewById(R.id.tvFuel);
            tvCng = itemView.findViewById(R.id.tvCng);
            tvProfit = itemView.findViewById(R.id.tvProfit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnDeleteClick {
        void onDelete(RideModel model, int position);
    }
}