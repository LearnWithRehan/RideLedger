package com.example.rideledger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Model.RideModel;
import com.example.rideledger.R;

import java.util.List;

public class TodayRideAdapter extends RecyclerView.Adapter<TodayRideAdapter.ViewHolder>{

    Context context;
    List<RideModel> list;

    public TodayRideAdapter(Context context,List<RideModel> list){
        this.context = context;
        this.list = list;
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

        holder.tvDate.setText(model.rideDate);

        holder.tvCash.setText("Cash ₹ " + model.getCash());
        holder.tvOnline.setText("Online ₹ " + model.getOnline());

        holder.tvFuel.setText("Fuel ₹ " + model.getFuel());
        holder.tvCng.setText("CNG ₹ " + model.getCng());

        holder.tvProfit.setText("Profit ₹ " + model.getProfit());

    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvPlatform,tvCash,tvOnline,tvFuel,tvCng,tvProfit,tvDate;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tvPlatform = itemView.findViewById(R.id.tvPlatform);
            tvCash = itemView.findViewById(R.id.tvCash);
            tvOnline = itemView.findViewById(R.id.tvOnline);
            tvFuel = itemView.findViewById(R.id.tvFuel);
            tvCng = itemView.findViewById(R.id.tvCng);
            tvProfit = itemView.findViewById(R.id.tvProfit);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}