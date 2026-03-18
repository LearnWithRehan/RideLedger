package com.example.rideledger.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Model.ExpenseModel;
import com.example.rideledger.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    Context context;
    List<ExpenseModel> list;
    FirebaseFirestore db;

    public ExpenseAdapter(Context context, List<ExpenseModel> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_expense, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {

        ExpenseModel model = list.get(position);

        // 🔥 Data Set
        holder.tvType.setText("Type: " + model.getType());
        holder.tvAmount.setText("Amount: ₹ " + model.getAmount());
        holder.tvDate.setText("Expense Date: " + model.getExpenseDate());

        // 🔥 Delete Logic
        holder.btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        db.collection("expenses")
                                .document(model.getId())
                                .delete()
                                .addOnSuccessListener(unused -> {

                                    list.remove(position);
                                    notifyDataSetChanged();

                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show());

                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvType, tvAmount, tvDate;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tvType);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}