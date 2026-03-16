package com.example.rideledger.Screen;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Adapter.DashboardAdapter;
import com.example.rideledger.Model.DashboardItem;
import com.example.rideledger.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerDashboard = findViewById(R.id.recyclerDashboard);

        List<DashboardItem> list = new ArrayList<>();

        list.add(new DashboardItem("🧾", "Ride Entry"));
        list.add(new DashboardItem("📅", "Date Wise Report"));
        list.add(new DashboardItem("👤", "Invoice Wise Report"));

        recyclerDashboard.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerDashboard.setAdapter(new DashboardAdapter(this, list));


    }
}