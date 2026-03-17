package com.example.rideledger.Screen;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Adapter.TodayRideAdapter;
import com.example.rideledger.Model.RideModel;
import com.example.rideledger.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodayReportActivity_Screen extends AppCompatActivity {

    EditText etFromDate, etToDate;
    Button btnShow;

    TextView tvTotalAmount,tvTotalProfit, tvTotalFuel, tvTotalCng;

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

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTotalProfit = findViewById(R.id.tvTotalProfit);
        tvTotalFuel = findViewById(R.id.tvTotalFuel);
        tvTotalCng = findViewById(R.id.tvTotalCng);
        ImageView btnPdf = findViewById(R.id.btnPdf);
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

        btnPdf.setOnClickListener(v -> {
            generatePDF();
        });

    }


    private void generatePDF() {

        PdfDocument pdfDocument = new PdfDocument();

        Paint titlePaint = new Paint();
        Paint headerPaint = new Paint();
        Paint labelPaint = new Paint();
        Paint valuePaint = new Paint();
        Paint boxPaint = new Paint();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(1200, 2000, 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int y = 80;

        // 🔷 HEADER BACKGROUND
        headerPaint.setColor(Color.parseColor("#3F51B5"));
        canvas.drawRect(0, 0, 1200, 180, headerPaint);

        // 🔷 TITLE
        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextSize(42);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("Ride Report", 420, 90, titlePaint);

        // 🚘 Vehicle Number
        titlePaint.setTextSize(26);
        canvas.drawText("Vehicle: HR55AZ9134", 420, 130, titlePaint);

        y = 220;

        // 📅 Date
        String currentDate = new SimpleDateFormat("dd MMM yyyy, hh:mm a")
                .format(new Date());

        labelPaint.setTextSize(24);
        labelPaint.setFakeBoldText(true);
        canvas.drawText("Generated: " + currentDate, 40, y, labelPaint);

        y += 40;

        canvas.drawText("From: " + etFromDate.getText(), 40, y, labelPaint);
        y += 30;
        canvas.drawText("To: " + etToDate.getText(), 40, y, labelPaint);

        y += 60;

        // 🎯 SUMMARY BOX STYLE
        int boxWidth = 250;
        int boxHeight = 120;
        int startX = 40;

        // Total Amount
        boxPaint.setColor(Color.parseColor("#E3F2FD"));
        canvas.drawRect(startX, y, startX + boxWidth, y + boxHeight, boxPaint);

        labelPaint.setColor(Color.BLACK);
        canvas.drawText("Amount", startX + 20, y + 40, labelPaint);

        valuePaint.setTextSize(28);
        valuePaint.setFakeBoldText(true);
        canvas.drawText("₹ " + tvTotalAmount.getText(), startX + 20, y + 90, valuePaint);

        // Fuel
        startX += 270;
        boxPaint.setColor(Color.parseColor("#FFF3E0"));
        canvas.drawRect(startX, y, startX + boxWidth, y + boxHeight, boxPaint);

        canvas.drawText("Fuel", startX + 20, y + 40, labelPaint);
        canvas.drawText("₹ " + tvTotalFuel.getText(), startX + 20, y + 90, valuePaint);

        // CNG
        startX += 270;
        boxPaint.setColor(Color.parseColor("#F3E5F5"));
        canvas.drawRect(startX, y, startX + boxWidth, y + boxHeight, boxPaint);

        canvas.drawText("CNG", startX + 20, y + 40, labelPaint);
        canvas.drawText("₹ " + tvTotalCng.getText(), startX + 20, y + 90, valuePaint);

        // Profit
        startX += 270;
        boxPaint.setColor(Color.parseColor("#E8F5E9"));
        canvas.drawRect(startX, y, startX + boxWidth, y + boxHeight, boxPaint);

        canvas.drawText("Profit", startX + 20, y + 40, labelPaint);
        canvas.drawText("₹ " + tvTotalProfit.getText(), startX + 20, y + 90, valuePaint);

        y += 160;

        // 🔻 Divider
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(2);
        canvas.drawLine(40, y, 1160, y, linePaint);

        y += 40;

        // 📄 LIST HEADER
        labelPaint.setFakeBoldText(true);
        canvas.drawText("Ride Details", 40, y, labelPaint);

        y += 40;

        // 📋 LIST DATA
        for (RideModel model : list) {

            // Card background
            boxPaint.setColor(Color.parseColor("#FAFAFA"));
            canvas.drawRect(40, y - 30, 1160, y + 100, boxPaint);

            // Platform
            labelPaint.setColor(Color.parseColor("#1A237E"));
            canvas.drawText(model.getPlatform(), 60, y, labelPaint);

            // Cash & Online
            labelPaint.setColor(Color.BLACK);
            canvas.drawText("Cash: ₹" + model.getCash()
                    + " | Online: ₹" + model.getOnline(), 60, y + 30, labelPaint);

            // Fuel & CNG
            canvas.drawText("Fuel: ₹" + model.getFuel()
                    + " | CNG: ₹" + model.getCng(), 60, y + 60, labelPaint);

            // Profit
            labelPaint.setColor(Color.parseColor("#2E7D32"));
            canvas.drawText("Profit: ₹" + model.getProfit(), 60, y + 90, labelPaint);

            y += 140;
        }

        pdfDocument.finishPage(page);

        try {

            File file = new File(getExternalFilesDir(null), "RideReport.pdf");
            pdfDocument.writeTo(new FileOutputStream(file));

            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    file
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
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

                        int totalAmount = 0;
                        int totalProfit = 0;
                        int totalFuel = 0;
                        int totalCng = 0;

                        for(DocumentSnapshot doc : queryDocumentSnapshots){

                            RideModel model = doc.toObject(RideModel.class);

                            try {

                                Date ride = sdf.parse(model.getRideDate());

                                if(ride.compareTo(from) >= 0 && ride.compareTo(to) <= 0){

                                    list.add(model);

                                    totalAmount += model.getTotal();
                                    totalProfit += model.getProfit();
                                    totalFuel += model.getFuel();
                                    totalCng += model.getCng();

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        adapter.notifyDataSetChanged();

                        tvTotalAmount.setText(String.valueOf(totalAmount));
                        tvTotalFuel.setText(String.valueOf(totalFuel));
                        tvTotalCng.setText(String.valueOf(totalCng));
                        tvTotalProfit.setText(String.valueOf(totalProfit));

                    });

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}