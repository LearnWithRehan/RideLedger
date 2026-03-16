package com.example.rideledger.Screen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rideledger.Model.RideModel;
import com.example.rideledger.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlatfomrWiseIncome_Screen extends AppCompatActivity {
    TextView tvTotalIncome,tvTotalProfit,tvTotalFuel,tvTotalCng;
    TextView tvUberIncome,tvOlaIncome,tvRapidoIncome;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platfomr_wise_income_screen);

        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalProfit = findViewById(R.id.tvTotalProfit);
        tvTotalFuel = findViewById(R.id.tvTotalFuel);
        tvTotalCng = findViewById(R.id.tvTotalCng);

        tvUberIncome = findViewById(R.id.tvUberIncome);
        tvOlaIncome = findViewById(R.id.tvOlaIncome);
        tvRapidoIncome = findViewById(R.id.tvRapidoIncome);

        db = FirebaseFirestore.getInstance();

        loadDashboard();
    }

    private void loadDashboard(){

        db.collection("ride_entries")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int totalIncome = 0;
                    int totalProfit = 0;
                    int totalFuel = 0;
                    int totalCng = 0;

                    int uberIncome = 0;
                    int olaIncome = 0;
                    int rapidoIncome = 0;

                    for(DocumentSnapshot doc : queryDocumentSnapshots){

                        RideModel model = doc.toObject(RideModel.class);

                        totalIncome += model.getTotal();
                        totalProfit += model.getProfit();
                        totalFuel += model.getFuel();
                        totalCng += model.getCng();

                        String platform = model.getPlatform();

                        if(platform.equalsIgnoreCase("Uber")){
                            uberIncome += model.getTotal();
                        }

                        if(platform.equalsIgnoreCase("Ola")){
                            olaIncome += model.getTotal();
                        }

                        if(platform.equalsIgnoreCase("Rapido")){
                            rapidoIncome += model.getTotal();
                        }

                    }

                    tvTotalIncome.setText("Total Income ₹ " + totalIncome);
                    tvTotalProfit.setText("Total Profit ₹ " + totalProfit);
                    tvTotalFuel.setText("Total Fuel ₹ " + totalFuel);
                    tvTotalCng.setText("Total CNG ₹ " + totalCng);

                    tvUberIncome.setText(String.valueOf(uberIncome));
                    tvOlaIncome.setText(String.valueOf(olaIncome));
                    tvRapidoIncome.setText(String.valueOf(rapidoIncome));

                });

    }
}