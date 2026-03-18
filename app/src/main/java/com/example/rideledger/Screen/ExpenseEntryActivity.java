package com.example.rideledger.Screen;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Adapter.ExpenseAdapter;
import com.example.rideledger.Model.ExpenseModel;
import com.example.rideledger.R;
import com.google.firebase.firestore.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseEntryActivity extends AppCompatActivity {

    EditText etDate, etAmount;
    Spinner spType;
    Button btnSave;
    RecyclerView recyclerExpense;

    FirebaseFirestore db;

    String selectedType = "";

    List<ExpenseModel> expenseList = new ArrayList<>();
    ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);

        etDate = findViewById(R.id.etDate);
        etAmount = findViewById(R.id.etAmount);
        spType = findViewById(R.id.spType);
        btnSave = findViewById(R.id.btnSave);
        recyclerExpense = findViewById(R.id.recyclerExpense);

        db = FirebaseFirestore.getInstance();

        setupDatePicker();
        setupSpinner();
        setupRecycler();

        btnSave.setOnClickListener(v -> saveExpense());
    }

    // 📅 Date Picker
    private void setupDatePicker() {

        etDate.setOnClickListener(v -> {

            Calendar cal = Calendar.getInstance();

            new DatePickerDialog(this, (view, y, m, d) -> {

                String date = d + "/" + (m + 1) + "/" + y;
                etDate.setText(date);

            },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();

        });
    }

    // 🔽 Spinner
    private void setupSpinner() {

        String[] types = {"Select Type", "MCD", "Toll Tax", "FASTag", "State Tax", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                types
        );

        spType.setAdapter(adapter);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedType = types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // 📱 Recycler Setup
    private void setupRecycler() {

        adapter = new ExpenseAdapter(this, expenseList);

        recyclerExpense.setLayoutManager(new LinearLayoutManager(this));
        recyclerExpense.setAdapter(adapter);

        loadTodayExpenses();
    }

    // 💾 Save Expense
    private void saveExpense() {

        String expenseDate = etDate.getText().toString();
        String amountStr = etAmount.getText().toString();

        if (expenseDate.isEmpty() || amountStr.isEmpty() || selectedType.equals("Select Type")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = Integer.parseInt(amountStr);

        // 🔥 Today date (entry date)
        String createdDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());

        Map<String, Object> data = new HashMap<>();
        data.put("expenseDate", expenseDate);
        data.put("createdDate", createdDate);
        data.put("type", selectedType);
        data.put("amount", amount);

        db.collection("expenses")
                .add(data)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(this, "Expense Saved", Toast.LENGTH_SHORT).show();

                    etAmount.setText("");
                    spType.setSelection(0);

                    loadTodayExpenses(); // 🔥 refresh

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error saving", Toast.LENGTH_SHORT).show());
    }

    // 🔥 Load Today Expenses
    private void loadTodayExpenses() {

        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());

        db.collection("expenses")
                .whereEqualTo("createdDate", today)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    expenseList.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        ExpenseModel model = doc.toObject(ExpenseModel.class);

                        if (model != null) {
                            model.setId(doc.getId()); // 🔥 important for delete
                            expenseList.add(model);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Load Failed", Toast.LENGTH_SHORT).show());
    }
}