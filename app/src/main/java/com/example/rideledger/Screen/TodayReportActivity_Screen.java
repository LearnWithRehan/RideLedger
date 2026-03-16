package com.example.rideledger.Screen;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Adapter.TodayRideAdapter;
import com.example.rideledger.Model.RideModel;
import com.example.rideledger.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodayReportActivity_Screen extends AppCompatActivity {

    EditText etSelectDate;
    Button btnShow;

    TextView tvTotalProfit,tvTotalFuel,tvTotalCng;

    RecyclerView recyclerReport;

    FirebaseFirestore db;

    TodayRideAdapter adapter;

    List<RideModel> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_report_screen);

        etSelectDate = findViewById(R.id.etSelectDate);
        btnShow = findViewById(R.id.btnShow);

        tvTotalProfit = findViewById(R.id.tvTotalProfit);
        tvTotalFuel = findViewById(R.id.tvTotalFuel);
        tvTotalCng = findViewById(R.id.tvTotalCng);

        recyclerReport = findViewById(R.id.recyclerReport);

        recyclerReport.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TodayRideAdapter(this,list);
        recyclerReport.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        setupDatePicker();

        btnShow.setOnClickListener(v -> {

            String date = etSelectDate.getText().toString();

            if(date.isEmpty()){

                Toast.makeText(this,"Please select date",Toast.LENGTH_SHORT).show();
                return;
            }

            loadReport(date);

        });

    }

    private void setupDatePicker(){

        etSelectDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this,(view,y,m,d)->{

                String date = d + "/" + (m+1) + "/" + y;

                etSelectDate.setText(date);

            },year,month,day);

            dialog.show();

        });

    }

    private void loadReport(String selectedDate){

        db.collection("ride_entries")
                .whereEqualTo("rideDate",selectedDate)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    list.clear();

                    int totalProfit = 0;
                    int totalFuel = 0;
                    int totalCng = 0;

                    for(DocumentSnapshot doc : queryDocumentSnapshots){

                        RideModel model = doc.toObject(RideModel.class);

                        list.add(model);

                        totalProfit += model.getProfit();
                        totalFuel += model.getFuel();
                        totalCng += model.getCng();

                    }

                    adapter.notifyDataSetChanged();

                    tvTotalProfit.setText("Total Profit ₹ " + totalProfit);
                    tvTotalFuel.setText("Total Fuel ₹ " + totalFuel);
                    tvTotalCng.setText("Total CNG ₹ " + totalCng);

                });

    }

}