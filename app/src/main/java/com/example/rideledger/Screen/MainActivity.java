package com.example.rideledger.Screen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Adapter.DashboardAdapter;
import com.example.rideledger.Model.DashboardItem;
import com.example.rideledger.Model.RideModel;
import com.example.rideledger.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerDashboard;

    TextView tvTodayIncome,tvTodayFuel,tvTodayCng,tvTodayProfit;

    FirebaseFirestore db;

    int todayIncome = 0;
    int todayFuel = 0;
    int todayCng = 0;
    int todayProfit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerDashboard = findViewById(R.id.recyclerDashboard);

        tvTodayIncome = findViewById(R.id.tvTodayIncome);
        tvTodayFuel = findViewById(R.id.tvTodayFuel);
        tvTodayCng = findViewById(R.id.tvTodayCng);
        tvTodayProfit = findViewById(R.id.tvTodayProfit);

        db = FirebaseFirestore.getInstance();

        loadTodayData();

        setupDashboard();
    }

    private void loadTodayData(){
        String todayDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        db.collection("ride_entries")
                .whereEqualTo("createdDate", todayDate)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int income = 0;
                    int fuel = 0;
                    int cng = 0;
                    int profit = 0;

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        Long total = doc.getLong("total");
                        Long fuelAmount = doc.getLong("fuel");
                        Long cngAmount = doc.getLong("cng");

                        if (total != null) income += total;
                        if (fuelAmount != null) fuel += fuelAmount;
                        if (cngAmount != null) cng += cngAmount;

                        profit += (total - fuelAmount - cngAmount);
                    }

                    tvTodayIncome.setText("₹ " + income);
                    tvTodayFuel.setText("₹ " + fuel);
                    tvTodayCng.setText("₹ " + cng);
                    tvTodayProfit.setText("₹ " + profit);

                });

    }


    private void setupDashboard(){

        List<DashboardItem> list = new ArrayList<>();

        list.add(new DashboardItem("🧾", "Ride Entry"));
        list.add(new DashboardItem("📅", "Date Wise Report"));
        list.add(new DashboardItem("💰", "Earnings Dashboard"));

        recyclerDashboard.setLayoutManager(new GridLayoutManager(this,2));
        recyclerDashboard.setAdapter(new DashboardAdapter(this,list));

    }

}