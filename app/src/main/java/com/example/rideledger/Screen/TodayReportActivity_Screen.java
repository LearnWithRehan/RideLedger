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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodayReportActivity_Screen extends AppCompatActivity {

    EditText etFromDate, etToDate;
    Button btnShow;

    TextView tvTotalProfit, tvTotalFuel, tvTotalCng;

    RecyclerView recyclerReport;

    FirebaseFirestore db;

    TodayRideAdapter adapter;

    List<RideModel> list = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_report_screen);

        etFromDate = findViewById(R.id.etFromDate);
        etToDate = findViewById(R.id.etToDate);
        btnShow = findViewById(R.id.btnShow);

        tvTotalProfit = findViewById(R.id.tvTotalProfit);
        tvTotalFuel = findViewById(R.id.tvTotalFuel);
        tvTotalCng = findViewById(R.id.tvTotalCng);

        recyclerReport = findViewById(R.id.recyclerReport);

        recyclerReport.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TodayRideAdapter(this, list);
        recyclerReport.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        setupDatePicker(etFromDate);
        setupDatePicker(etToDate);

        btnShow.setOnClickListener(v -> {

            String fromDate = etFromDate.getText().toString();
            String toDate = etToDate.getText().toString();

            if(fromDate.isEmpty() || toDate.isEmpty()){
                Toast.makeText(this,"Please select From and To date",Toast.LENGTH_SHORT).show();
                return;
            }

            loadReport(fromDate, toDate);

        });

    }


    private void setupDatePicker(EditText editText){

        editText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this,(view,y,m,d)->{

                String date = d + "/" + (m+1) + "/" + y;

                editText.setText(date);

            },year,month,day);

            dialog.show();

        });

    }



    private void loadReport(String fromDate, String toDate){

        try {

            Date from = sdf.parse(fromDate);
            Date to = sdf.parse(toDate);

            db.collection("ride_entries")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        list.clear();

                        int totalProfit = 0;
                        int totalFuel = 0;
                        int totalCng = 0;

                        for(DocumentSnapshot doc : queryDocumentSnapshots){

                            RideModel model = doc.toObject(RideModel.class);

                            try {

                                Date ride = sdf.parse(model.getRideDate());

                                if(ride.compareTo(from) >= 0 && ride.compareTo(to) <= 0){

                                    list.add(model);

                                    totalProfit += model.getProfit();
                                    totalFuel += model.getFuel();
                                    totalCng += model.getCng();

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        adapter.notifyDataSetChanged();

                        tvTotalProfit.setText("Total Profit ₹ " + totalProfit);
                        tvTotalFuel.setText("Total Fuel ₹ " + totalFuel);
                        tvTotalCng.setText("Total CNG ₹ " + totalCng);

                    });

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}