package com.example.booking_ma_tim21.activities;

import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationAnnualDataDTO;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.dto.ChartData;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.AnalyticsService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.DatePickerCreator;
import com.example.booking_ma_tim21.util.NavigationSetup;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsActivity extends AppCompatActivity {

    Long ownerId;
    AuthManager auth;
    AnalyticsService analyticsService;
    AccommodationService accService;
    AccommodationAnnualDataDTO annualData;
    PieChart pieChartProfit;
    PieChart pieChartReservations;
    CombinedChart combinedChart;
    TextView pieChartsButton;
    EditText yearInput;
    Spinner accSelection;
    Button getPiePdf;
    Button getCombinedPdf;
    List<PieEntry> profitData;
    List<PieEntry> reservationsData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = AuthManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        NavigationSetup.setupNavigation(this, auth);

        ownerId = auth.getUserIdLong();

        RetrofitService retrofitService = new RetrofitService();
        analyticsService = retrofitService.getRetrofit().create(AnalyticsService.class);
        accService = retrofitService.getRetrofit().create(AccommodationService.class);

        pieChartProfit = findViewById(R.id.pieChartProfit);
        pieChartReservations = findViewById(R.id.pieChartReservations);
        pieChartsButton = findViewById(R.id.pieCharts_selected_date_tv);
        combinedChart=findViewById(R.id.combinedChart);
        yearInput = findViewById(R.id.selected_year_et);
        accSelection = findViewById(R.id.spinnerDataset);
        getPiePdf=findViewById(R.id.pie_pdf_btn);
        getCombinedPdf=findViewById(R.id.combined_pdf_btn);


        setDatesInput();
        setSpinnerInput();
        setButtons();

    }


    void setButtons(){

        getPiePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePieChartsAndTableAsPdf();
            }
        });

        getCombinedPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCombinedChartAndTableAsPdf();
            }
        });

        yearInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkChartParams();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }



    void setDatesInput() {

        pieChartsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MaterialDatePicker mdp = DatePickerCreator.getDatePicker(null);

                mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                        calendar.setTimeInMillis(selection.first);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate1 = format.format(calendar.getTime());

                        calendar.setTimeInMillis(selection.second);
                        String formattedDate2 = format.format(calendar.getTime());

                        pieChartsButton.setText(formattedDate1 + "/" + formattedDate2);

                        setPieChartsData(formattedDate1, formattedDate2);
                    }
                });

                mdp.show(getSupportFragmentManager(), "TAG");

            }
        });

    }


    void setPieChartsData(String dateFrom, String dateTo) {
        setProfitData(dateFrom, dateTo);
    }

    void setPieCharts() {
        PieDataSet profitSet = new PieDataSet(profitData, "Profit Chart");
        PieDataSet resSet = new PieDataSet(reservationsData, "Reservations Chart");

        List<Integer> colors = getRandomColors(profitData.size());
        profitSet.setColors(colors);
        resSet.setColors(colors);

        PieData pieDataProfit = new PieData(profitSet);
        PieData pieDataRes = new PieData(resSet);
        AccommodationAnnualDataDTO annualData;

        pieChartProfit.setData(pieDataProfit);
        pieChartReservations.setData(pieDataRes);

        pieChartProfit.invalidate();
        pieChartReservations.invalidate();

        pieChartProfit.setDrawSliceText(false);
        pieDataProfit.setValueTextSize(20);
        pieChartProfit.setDrawHoleEnabled(false);

        pieChartReservations.setDrawSliceText(false);
        pieDataRes.setValueTextSize(20);
        pieChartReservations.setDrawHoleEnabled(false);

        pieChartProfit.setVisibility(View.VISIBLE);
        pieChartReservations.setVisibility(View.VISIBLE);
        getPiePdf.setVisibility(View.VISIBLE);


    }

    void setSpinnerInput() {

        accSelection.setPrompt("Accommodation Id");
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accSelection.setAdapter(adapter);
        adapter.add("Select Accommodation");


        Call call = accService.getOwnersAccommodations(ownerId);

        call.enqueue(new Callback<List<AccommodationPreviewDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationPreviewDTO>> call, Response<List<AccommodationPreviewDTO>> response) {
                if (response.code() == 200) {

                    Log.d("REZ", "Meesage recieved");

                    for (AccommodationPreviewDTO acc : response.body()) {
                        adapter.add(acc.getId().toString());
                    }


                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationPreviewDTO>> call, Throwable t) {
                t.printStackTrace();
            }


        });

        accSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                checkChartParams();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                accSelection.setPrompt("Accommodation id");
            }

        });


    }

    void setProfitData(String dateFrom, String dateTo) {
        Call call = analyticsService.getOwnerProfit(ownerId, dateFrom, dateTo);

        call.enqueue(new Callback<List<ChartData>>() {
            @Override
            public void onResponse(Call<List<ChartData>> call, Response<List<ChartData>> response) {
                if (response.code() == 200) {

                    Log.d("REZ", "Meesage recieved");
                    if(response.body().isEmpty()){
                        getPiePdf.setVisibility(View.GONE);
                        pieChartProfit.setVisibility(View.GONE);
                        pieChartReservations.setVisibility(View.GONE);
                    }
                    profitData = setPieChartData(response.body());
                    setReservationsData(dateFrom, dateTo);

                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ChartData>> call, Throwable t) {
                t.printStackTrace();
            }


        });

    }

    void setReservationsData(String dateFrom, String dateTo) {
        Call call = analyticsService.getOwnerReservationCount(ownerId, dateFrom, dateTo);

        call.enqueue(new Callback<List<ChartData>>() {
            @Override
            public void onResponse(Call<List<ChartData>> call, Response<List<ChartData>> response) {
                if (response.code() == 200) {

                    Log.d("REZ", "Meesage recieved");
                    reservationsData = setPieChartData(response.body());
                    setPieCharts();

                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ChartData>> call, Throwable t) {
                t.printStackTrace();
            }


        });

    }

    List<PieEntry> setPieChartData(List<ChartData> data) {

        List<PieEntry> entries = new ArrayList<>();

        for (ChartData c : data) {
            entries.add(new PieEntry(c.getValue().intValue(), c.getName()));
        }

        return entries;

    }

    List<Integer> getRandomColors(int number) {

        List<Integer> colors = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < number; i++) {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();

            Color randomColor = Color.valueOf(r, g, b);
            colors.add(randomColor.toArgb());
        }

        return colors;


    }

    void checkChartParams() {
        Long accId = null;
        Integer year = null;

        String selectedAccId = accSelection.getSelectedItem().toString();

        try {
            accId = Long.parseLong(selectedAccId);
        } catch (Exception e) {
            getCombinedPdf.setVisibility(View.GONE);
            combinedChart.setVisibility(View.GONE);
            return;
        }

        String selectedYear = yearInput.getText().toString();

        try {
            year = Integer.parseInt(selectedYear);
        } catch (Exception e) {
            getCombinedPdf.setVisibility(View.GONE);
            combinedChart.setVisibility(View.GONE);
            return;
        }

        getCombinedChartsData(accId, year);
    }

    void getCombinedChartsData(Long accId, Integer year) {

        Call call = analyticsService.getAccommodationAnnualData(accId, year);

        call.enqueue(new Callback<AccommodationAnnualDataDTO>() {
            @Override
            public void onResponse(Call<AccommodationAnnualDataDTO> call, Response<AccommodationAnnualDataDTO> response) {
                if (response.code() == 200) {

                    Log.d("REZ", "Meesage recieved");
                    setCombinedCharts(response.body());
                    annualData= response.body();

                } else {
                    Log.d("REZ", "Meesage recieved: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AccommodationAnnualDataDTO> call, Throwable t) {
                t.printStackTrace();
            }


        });

    }

    void setCombinedCharts(AccommodationAnnualDataDTO data){
        List<String> months = Arrays.asList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        List<BarEntry> barEntries = getBarEntries(data);

        BarDataSet barDataSet = new BarDataSet(barEntries, "Bar Data");
        barDataSet.setColor(Color.rgb(197, 216, 109)); // Line color

        List<Entry> lineEntries = getLineEntries(data);

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Line Data");
        lineDataSet.setColor(Color.rgb(46, 41, 78));
        lineDataSet.setLineWidth(2f); // Line width
        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);


        BarData barData = new BarData(barDataSet);
        LineData lineData = new LineData(lineDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        combinedData.setData(lineData);

        // Set combined data to the chart
        combinedChart.setData(combinedData);

        // Customize chart appearance
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        combinedChart.setDrawBarShadow(false);


        // Customize X axis
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setAxisMinimum(0f);

        // Refresh the chart
        combinedChart.invalidate();
        combinedChart.setVisibility(View.VISIBLE);
        getCombinedPdf.setVisibility(View.VISIBLE);

    }

    List<BarEntry> getBarEntries(AccommodationAnnualDataDTO data){

        List<BarEntry> barEntries = new ArrayList<>();

        for(int i=0;i<data.getProfit().length;i++){
            barEntries.add(new BarEntry((float)i,(float)data.getProfit()[i]));
        }
        return  barEntries;
    }

    List<Entry> getLineEntries(AccommodationAnnualDataDTO data){

        List<Entry> lineEntries = new ArrayList<>();

        for(int i=0;i<data.getReservations().length;i++){
            lineEntries.add(new Entry((float)i,(float)data.getReservations()[i]));
        }
        return  lineEntries;
    }

    private void saveCombinedChartAndTableAsPdf() {
        List<String> months = Arrays.asList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        try {
            Bitmap chartBitmap=combinedChart.getChartBitmap();
            // Create a directory for the PDF file
            File pdfDir = new File(getExternalFilesDir(null), "PDFs");
            if (!pdfDir.exists()) {
                if (!pdfDir.mkdirs()) {
                    Log.e(TAG, "Failed to create directory");
                    return;
                }
            }            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }

            // Create a PDF file
            File pdfFile = new File(pdfDir, "annual_chart_data.pdf");
            if (pdfFile.exists()) {
                // Delete the file
                if (pdfFile.delete()) {
                    Log.d(TAG, "File deleted successfully");
                    pdfFile = new File(pdfDir, "annual_chart_data.pdf");
                } else {
                    Log.e(TAG, "Failed to delete the file");
                }

            } else {
                Log.d(TAG, "File does not exist");
            }
            pdfFile.createNewFile();

            // Initialize iTextPDF document
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            chartBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream3);
            Image chartImage = Image.getInstance(stream3.toByteArray());

            // Set image dimensions as per your requirement
            chartImage.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight() / 2);

            // Add image to the document
            document.add(chartImage);

            // Add a table below the chart
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            // Add table headers
            PdfPCell headerCell1 = new PdfPCell(new Phrase("Month"));
            PdfPCell headerCell2 = new PdfPCell(new Phrase("Profits"));
            PdfPCell headerCell3 = new PdfPCell(new Phrase("Reservation"));
            table.addCell(headerCell1);
            table.addCell(headerCell2);
            table.addCell(headerCell3);


            // Add table data (replace with your actual data)
            for(int i=0;i<annualData.getProfit().length;i++){

                table.addCell(months.get(i));
                table.addCell(String.valueOf(annualData.getProfit()[i]));
                table.addCell(String.valueOf(annualData.getReservations()[i]));

            }

            // Add table to the document
            document.add(table);

            // Close the document
            document.close();

            Uri contentUri = FileProvider.getUriForFile(this,
                    "com.example.booking_ma_tim21.fileprovider",
                    pdfFile);

            // Grant temporary read permission to the content URI
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePieChartsAndTableAsPdf() {
        try {
            // Create a directory for the PDF file
            File pdfDir = new File(getExternalFilesDir(null), "PDFs");
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }

            // Create a PDF file
            File pdfFile = new File(pdfDir, "pie_charts_data.pdf");

            if (pdfFile.exists()) {
                // Delete the file
                if (pdfFile.delete()) {
                    Log.d(TAG, "File deleted successfully");
                    pdfFile = new File(pdfDir, "annual_chart_data.pdf");

                } else {
                    Log.e(TAG, "Failed to delete the file");
                }
            } else {
                Log.d(TAG, "File does not exist");
            }

            pdfFile.createNewFile();

            // Initialize iTextPDF document
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            // Convert PieChart1 to iTextPDF Image
            Bitmap pieChartBitmap1 = pieChartProfit.getChartBitmap();
            ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
            pieChartBitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream3);
            Image pieChartImage1 = Image.getInstance(stream3.toByteArray());
            pieChartImage1.scaleToFit(document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2);

            // Convert PieChart2 to iTextPDF Image
            Bitmap pieChartBitmap2 = pieChartReservations.getChartBitmap();
            stream3 = new ByteArrayOutputStream();
            pieChartBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream3);
            Image pieChartImage2 = Image.getInstance(stream3.toByteArray());
            pieChartImage2.scaleToFit(document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2);

            PdfPTable tableImages = new PdfPTable(2);
            tableImages.setWidthPercentage(100);
            // Add the first image to the first cell
            PdfPCell cell1 = new PdfPCell(pieChartImage1, true);
            tableImages.addCell(cell1);

            // Add the second image to the second cell
            PdfPCell cell2 = new PdfPCell(pieChartImage2, true);
            tableImages.addCell(cell2);

            document.add(tableImages);

            document.add(new Paragraph("\n"));



            // Add a table below the PieCharts
            PdfPTable table = new PdfPTable(2); // Two columns
            table.setWidthPercentage(100);

            // Add table headers
            table.addCell("Accommodation");
            table.addCell("Profit");

            // Add table data for PieChart 1 (replace with your actual data)
            for(PieEntry data:profitData){
                table.addCell(data.getLabel());
                table.addCell(String.valueOf(data.getValue()));
            }
            document.add(table);

            document.add(new Paragraph("\n"));

            // Add a table below the PieCharts
            PdfPTable table2 = new PdfPTable(2); // Two columns
            table2.setWidthPercentage(100);

            // Add table headers
            table2.addCell("Accommodation");
            table2.addCell("Reservations");

            // Add table data for PieChart 1 (replace with your actual data)
            for(PieEntry data:reservationsData){
                table2.addCell(data.getLabel());
                table2.addCell(String.valueOf(data.getValue()));
            }

            // Add table to the document
            document.add(table2);

            // Close the document
            document.close();

            Uri contentUri = FileProvider.getUriForFile(this,
                    "com.example.booking_ma_tim21.fileprovider",
                    pdfFile);

            // Grant temporary read permission to the content URI
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}


