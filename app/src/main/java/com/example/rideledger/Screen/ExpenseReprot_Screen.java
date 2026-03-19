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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rideledger.Adapter.ReportAdapter;
import com.example.rideledger.Model.ExpenseModel;
import com.example.rideledger.Model.ReportModel;
import com.example.rideledger.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ExpenseReprot_Screen extends AppCompatActivity {

    EditText etFromDate, etToDate;
    Button btnFilter;
    RecyclerView recyclerReport;
    TextView tvTotalAmount;
    FirebaseFirestore db;

    List<ReportModel> reportList = new ArrayList<>();
    ReportAdapter adapter;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_reprot_screen);

        etFromDate = findViewById(R.id.etFromDate);
        etToDate = findViewById(R.id.etToDate);
        btnFilter = findViewById(R.id.btnFilter);
        recyclerReport = findViewById(R.id.recyclerReport);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        ImageView btnPdf = findViewById(R.id.btnPdfExpe);

        db = FirebaseFirestore.getInstance();

        adapter = new ReportAdapter(this, reportList);
        recyclerReport.setLayoutManager(new LinearLayoutManager(this));
        recyclerReport.setAdapter(adapter);

        setupDatePicker(etFromDate);
        setupDatePicker(etToDate);

        btnFilter.setOnClickListener(v -> loadReport());

        btnPdf.setOnClickListener(v -> {
            generatePDF();
        });
    }




    // 📅 Date Picker
    private void setupDatePicker(EditText editText) {

        editText.setOnClickListener(v -> {

            Calendar cal = Calendar.getInstance();

            new DatePickerDialog(this, (view, y, m, d) -> {
                String date = d + "/" + (m + 1) + "/" + y;
                editText.setText(date);
            },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    // 🔥 LOAD REPORT
    private void loadReport() {

        String from = etFromDate.getText().toString();
        String to = etToDate.getText().toString();

        if (from.isEmpty() || to.isEmpty()) {
            Toast.makeText(this, "Select both dates", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("expenses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {


                    reportList.clear();
                    int grandTotal = 0;

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        ExpenseModel model = doc.toObject(ExpenseModel.class);

                        try {
                            Date expenseDate = sdf.parse(model.getExpenseDate());
                            Date fromDate = sdf.parse(from);
                            Date toDate = sdf.parse(to);

                            if (expenseDate.compareTo(fromDate) >= 0 &&
                                    expenseDate.compareTo(toDate) <= 0) {

                                // 🔥 DIRECT ADD (NO GROUPING)
                                reportList.add(new ReportModel(
                                        model.getExpenseDate(),
                                        model.getType(),
                                        model.getAmount()
                                ));

                                // TOTAL
                                grandTotal += model.getAmount();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }



                    Collections.sort(reportList, (a, b) -> {
                        try {
                            return sdf.parse(a.getDate()).compareTo(sdf.parse(b.getDate()));
                        } catch (Exception e) {
                            return 0;
                        }
                    });

                    if (grandTotal > 5000) {
                        tvTotalAmount.setTextColor(Color.RED);
                    } else {
                        tvTotalAmount.setTextColor(Color.parseColor("#2E7D32"));
                    }

                    tvTotalAmount.setText("₹ " + grandTotal);

                    adapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show());

    }








//
//    private void generatePDF() {
//
//        PdfDocument pdfDocument = new PdfDocument();
//
//        Paint titlePaint = new Paint();
//        Paint headerPaint = new Paint();
//        Paint textPaint = new Paint();
//        Paint boxPaint = new Paint();
//
//        PdfDocument.PageInfo pageInfo =
//                new PdfDocument.PageInfo.Builder(1200, 2000, 1).create();
//
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//        Canvas canvas = page.getCanvas();
//
//        int y = 80;
//
//        // 🔷 HEADER
//        headerPaint.setColor(Color.parseColor("#009688"));
//        canvas.drawRect(0, 0, 1200, 180, headerPaint);
//
//        titlePaint.setColor(Color.WHITE);
//        titlePaint.setTextSize(42);
//        titlePaint.setFakeBoldText(true);
//        canvas.drawText("Expense Report", 380, 100, titlePaint);
//
//        y = 220;
//
//        // 📅 DATE INFO
//        textPaint.setTextSize(24);
//        textPaint.setFakeBoldText(true);
//
//        String currentDate = new SimpleDateFormat("dd MMM yyyy, hh:mm a")
//                .format(new Date());
//
//        canvas.drawText("Generated: " + currentDate, 40, y, textPaint);
//
//        y += 40;
//        canvas.drawText("From: " + etFromDate.getText().toString(), 40, y, textPaint);
//
//        y += 30;
//        canvas.drawText("To: " + etToDate.getText().toString(), 40, y, textPaint);
//
//        y += 60;
//
//        // 🎯 TOTAL BOX
//        boxPaint.setColor(Color.parseColor("#E3F2FD"));
//        canvas.drawRect(40, y, 500, y + 120, boxPaint);
//
//        textPaint.setColor(Color.BLACK);
//        canvas.drawText("Total Expense", 60, y + 40, textPaint);
//
//        textPaint.setTextSize(32);
//        textPaint.setFakeBoldText(true);
//        canvas.drawText(tvTotalAmount.getText().toString(), 60, y + 90, textPaint);
//
//        y += 180;
//
//        // 🔻 Divider
//        Paint linePaint = new Paint();
//        linePaint.setStrokeWidth(2);
//        canvas.drawLine(40, y, 1160, y, linePaint);
//
//        y += 40;
//
//        // 📄 HEADER
//        textPaint.setTextSize(26);
//        textPaint.setFakeBoldText(true);
//        canvas.drawText("Date", 60, y, textPaint);
//        canvas.drawText("Amount", 900, y, textPaint);
//
//        y += 30;
//
//        // 🔻 Divider
//        canvas.drawLine(40, y, 1160, y, linePaint);
//
//        y += 40;
//
//        // 📋 DATA LIST (🔥 MAIN PART)
//        textPaint.setTextSize(22);
//        textPaint.setFakeBoldText(false);
//
//        for (ReportModel model : reportList) {
//
//            boxPaint.setColor(Color.parseColor("#FAFAFA"));
//            canvas.drawRect(40, y - 25, 1160, y + 70, boxPaint);
//
//            // TYPE + DATE
//            textPaint.setColor(Color.BLACK);
//            canvas.drawText(model.getType() + " (" + model.getDate() + ")", 60, y, textPaint);
//
//            // AMOUNT
//            textPaint.setColor(Color.parseColor("#D32F2F"));
//            canvas.drawText("₹ " + model.getTotalAmount(), 900, y, textPaint);
//
//            y += 80;
//        }
//
//        pdfDocument.finishPage(page);
//
//        try {
//
//            File file = new File(getExternalFilesDir(null), "ExpenseReport.pdf");
//            pdfDocument.writeTo(new FileOutputStream(file));
//
//            Uri uri = FileProvider.getUriForFile(
//                    this,
//                    getPackageName() + ".provider",
//                    file
//            );
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "application/pdf");
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            startActivity(intent);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error opening PDF", Toast.LENGTH_SHORT).show();
//        }
//
//        pdfDocument.close();
//    }






    private void drawHeader(Canvas canvas, Paint headerPaint, Paint titlePaint, int pageWidth) {

        headerPaint.setColor(Color.parseColor("#009688"));
        canvas.drawRect(0, 0, pageWidth, 180, headerPaint);

        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextSize(42);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("Expense Report", 380, 100, titlePaint);
    }



    private void generatePDF() {

        PdfDocument pdfDocument = new PdfDocument();

        Paint titlePaint = new Paint();
        Paint headerPaint = new Paint();
        Paint textPaint = new Paint();
        Paint boxPaint = new Paint();
        Paint linePaint = new Paint();

        int pageWidth = 1200;
        int pageHeight = 2000;

        int y = 80;
        int pageNumber = 1;

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // 🔷 HEADER
        drawHeader(canvas, headerPaint, titlePaint, pageWidth);

        y = 220;

        // 📅 DATE
        textPaint.setTextSize(24);
        textPaint.setFakeBoldText(true);

        String currentDate = new SimpleDateFormat("dd MMM yyyy, hh:mm a")
                .format(new Date());

        canvas.drawText("Generated: " + currentDate, 40, y, textPaint);
        y += 40;
        canvas.drawText("From: " + etFromDate.getText().toString(), 40, y, textPaint);
        y += 30;
        canvas.drawText("To: " + etToDate.getText().toString(), 40, y, textPaint);

        y += 60;

        // 🎯 TOTAL
        boxPaint.setColor(Color.parseColor("#E3F2FD"));
        canvas.drawRect(40, y, 500, y + 120, boxPaint);

        canvas.drawText("Total Expense", 60, y + 40, textPaint);

        textPaint.setTextSize(32);
        canvas.drawText(tvTotalAmount.getText().toString(), 60, y + 90, textPaint);

        y += 180;

        linePaint.setStrokeWidth(2);
        canvas.drawLine(40, y, 1160, y, linePaint);

        y += 40;

        // HEADER ROW
        textPaint.setTextSize(26);
        textPaint.setFakeBoldText(true);
        canvas.drawText("Date", 60, y, textPaint);
        canvas.drawText("Amount", 900, y, textPaint);

        y += 30;
        canvas.drawLine(40, y, 1160, y, linePaint);
        y += 40;

        textPaint.setTextSize(22);
        textPaint.setFakeBoldText(false);

        // 🔥 LOOP
        for (ReportModel model : reportList) {

            // 👉 PAGE BREAK
            if (y > pageHeight - 100) {

                pdfDocument.finishPage(page);

                pageNumber++;

                pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();
                page = pdfDocument.startPage(pageInfo);
                canvas = page.getCanvas();

                drawHeader(canvas, headerPaint, titlePaint, pageWidth);

                y = 220;
            }

            // ROW BG
            boxPaint.setColor(Color.parseColor("#FAFAFA"));
            canvas.drawRect(40, y - 25, 1160, y + 50, boxPaint);

            // TEXT
            textPaint.setColor(Color.BLACK);
            canvas.drawText(model.getType() + " (" + model.getDate() + ")", 60, y, textPaint);

            textPaint.setColor(Color.parseColor("#D32F2F"));
            canvas.drawText("₹ " + model.getTotalAmount(), 900, y, textPaint);

            y += 70;
        }

        pdfDocument.finishPage(page);

        try {

            File file = new File(getExternalFilesDir(null), "ExpenseReport.pdf");
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



}