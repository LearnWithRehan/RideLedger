package com.example.rideledger.Screen;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Adapter.TodayRideAdapter;
import com.example.rideledger.Model.RideModel;
import com.example.rideledger.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideEntry_Screen extends AppCompatActivity {

    TextInputEditText etDate, etCash, etOnline, etFuel, etCng, etTotal, etProfit;
    Spinner spPlatform;
    Button btnSaveRide;

    TextView tvTodayTotalProfit;
    FirebaseFirestore db;
    FirebaseAuth auth;


    RecyclerView recyclerTodayRide;
    TodayRideAdapter adapter;
    List<RideModel> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_entry_screen);

        etDate = findViewById(R.id.etDate);
        etCash = findViewById(R.id.etCash);
        etOnline = findViewById(R.id.etOnline);
        etFuel = findViewById(R.id.etFuel);
        etCng = findViewById(R.id.etCng);
        etTotal = findViewById(R.id.etTotal);
        etProfit = findViewById(R.id.etProfit);
        tvTodayTotalProfit = findViewById(R.id.tvTodayTotalProfit);

        spPlatform = findViewById(R.id.spPlatform);
        btnSaveRide = findViewById(R.id.btnSaveRide);

        // RecyclerView initialize FIRST
        recyclerTodayRide = findViewById(R.id.recyclerTodayRide);

        recyclerTodayRide.setLayoutManager(new LinearLayoutManager(this));
        recyclerTodayRide.setNestedScrollingEnabled(false);

        adapter = new TodayRideAdapter(this, list);
        recyclerTodayRide.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadTodayEntries();


        setupSpinner();
        setupDatePicker();
        setupCalculation();

        btnSaveRide.setOnClickListener(v -> saveRide());
    }

    private void updateTodayProfit(){

        int totalProfit = 0;

        for(RideModel model : list){
            totalProfit += model.getProfit();
        }

        tvTodayTotalProfit.setText("Profit ₹ " + totalProfit);
    }


    private void loadTodayEntries(){

        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(new Date());

        db.collection("ride_entries")
                .whereEqualTo("createdDate",today)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    list.clear();

                    for(DocumentSnapshot doc : queryDocumentSnapshots){

                        RideModel model = doc.toObject(RideModel.class);
                        list.add(model);

                    }

                    adapter.notifyDataSetChanged();
                    updateTodayProfit();
                });
    }


    // Platform Spinner
    private void setupSpinner(){

        String[] platform = {"Select Platform","Uber","Ola","Rapido","Other"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        platform);

        spPlatform.setAdapter(adapter);
    }

    // Date Picker
    private void setupDatePicker(){

        etDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, y, m, d) -> {

                        String date = d + "/" + (m+1) + "/" + y;
                        etDate.setText(date);
                        // Error remove
                        etDate.setError(null);
                    },year,month,day);

            dialog.show();
        });
    }

    // Auto Calculation
    private void setupCalculation(){

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etCash.addTextChangedListener(watcher);
        etOnline.addTextChangedListener(watcher);
        etFuel.addTextChangedListener(watcher);
        etCng.addTextChangedListener(watcher);
    }

    private void calculate(){

        int cash = getValue(etCash);
        int online = getValue(etOnline);
        int fuel = getValue(etFuel);
        int cng = getValue(etCng);

        int total = cash + online;
        int profit = total - (fuel + cng);

        etTotal.setText(String.valueOf(total));
        etProfit.setText(String.valueOf(profit));
    }

    private int getValue(TextInputEditText edit){

        String value = edit.getText().toString().trim();

        if(value.isEmpty())
            return 0;

        return Integer.parseInt(value);
    }

    // Save Data to Firebase
    // Save Data to Firebase
//    private void saveRide(){
//
//        String date = etDate.getText().toString().trim();
//        String platform = spPlatform.getSelectedItem().toString();
//        String cashmode = etCash.getText().toString().trim();
//        String onlinemode = etOnline.getText().toString().trim();
//
//        // Date Validation
//        if(date.isEmpty()){
//            etDate.setError("Please select date");
//            etDate.requestFocus();
//            return;
//        }
//
//        // Platform Validation
//        if(spPlatform.getSelectedItemPosition() == 0){
//            Toast.makeText(this,"Please select platform",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Cash OR Online required
//        if(cashmode.isEmpty() && onlinemode.isEmpty()){
//            Toast.makeText(this,"Please enter Cash or Online amount",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int cash = getValue(etCash);
//        int online = getValue(etOnline);
//        int fuel = getValue(etFuel);
//        int cng = getValue(etCng);
//        int total = getValue(etTotal);
//        int profit = getValue(etProfit);
//
//        String userId = auth.getCurrentUser().getUid();
//
//        Map<String,Object> ride = new HashMap<>();
//
//        ride.put("userId",userId);
//        ride.put("date",date);
//        ride.put("platform",platform);
//        ride.put("cash",cash);
//        ride.put("online",online);
//        ride.put("fuel",fuel);
//        ride.put("cng",cng);
//        ride.put("total",total);
//        ride.put("profit",profit);
//
//        db.collection("ride_entries")
//                .add(ride)
//                .addOnSuccessListener(documentReference -> {
//
//                    Toast.makeText(this,
//                            "Ride Entry Saved",
//                            Toast.LENGTH_SHORT).show();
//                    loadTodayEntries();
//
//                    clearFields();
//
//                })
//                .addOnFailureListener(e -> {
//
//                    Toast.makeText(this,
//                            e.getMessage(),
//                            Toast.LENGTH_SHORT).show();
//                });
//    }

    private void saveRide(){

        String rideDate = etDate.getText().toString().trim();
        String platform = spPlatform.getSelectedItem().toString();
        String cashmode = etCash.getText().toString().trim();
        String onlinemode = etOnline.getText().toString().trim();

        // Date Validation
        if(rideDate.isEmpty()){
            etDate.setError("Please select date");
            etDate.requestFocus();
            return;
        }

        // Platform Validation
        if(spPlatform.getSelectedItemPosition() == 0){
            Toast.makeText(this,"Please select platform",Toast.LENGTH_SHORT).show();
            return;
        }

        // Cash OR Online required
        if(cashmode.isEmpty() && onlinemode.isEmpty()){
            Toast.makeText(this,"Please enter Cash or Online amount",Toast.LENGTH_SHORT).show();
            return;
        }

        int cash = getValue(etCash);
        int online = getValue(etOnline);
        int fuel = getValue(etFuel);
        int cng = getValue(etCng);
        int total = getValue(etTotal);
        int profit = getValue(etProfit);

        String userId = auth.getCurrentUser().getUid();

        // Current Date (system date)
        String todayDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(new Date());

        Map<String,Object> ride = new HashMap<>();

        ride.put("userId",userId);
        ride.put("rideDate",rideDate);      // user selected date
        ride.put("createdDate",todayDate);  // current system date
        ride.put("platform",platform);
        ride.put("cash",cash);
        ride.put("online",online);
        ride.put("fuel",fuel);
        ride.put("cng",cng);
        ride.put("total",total);
        ride.put("profit",profit);

        db.collection("ride_entries")
                .add(ride)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(this,
                            "Ride Entry Saved",
                            Toast.LENGTH_SHORT).show();

                    clearFields();
                    loadTodayEntries();

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this,
                            e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields(){

        etDate.setText("");
        etCash.setText("");
        etOnline.setText("");
        etFuel.setText("");
        etCng.setText("");
        etTotal.setText("");
        etProfit.setText("");
    }
}