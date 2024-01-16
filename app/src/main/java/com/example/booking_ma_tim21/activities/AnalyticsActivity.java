package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.dto.AccommodationProfitDTO;
import com.example.booking_ma_tim21.dto.ChartData;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.AnalyticsService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.util.DatePickerCreator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

public class AnalyticsActivity extends AppCompatActivity {

    AnalyticsService analyticsService;
    PieChart pieChartProfit;
    PieChart pieChartReservations;
    TextView pieChartsButton;
    List<PieEntry> profitData;
    List<PieEntry> reservationsData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        RetrofitService retrofitService= new RetrofitService();
        analyticsService=retrofitService.getRetrofit().create(AnalyticsService.class);

        pieChartProfit = findViewById(R.id.pieChartProfit);
        pieChartReservations = findViewById(R.id.pieChartReservations);
        pieChartsButton=findViewById(R.id.pieCharts_selected_date_tv);


    }

    void setDatesInput(){

        pieChartsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MaterialDatePicker mdp= DatePickerCreator.getDatePicker(null);

                mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                        calendar.setTimeInMillis(selection.first);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate1  = format.format(calendar.getTime());

                        calendar.setTimeInMillis(selection.second);
                        String formattedDate2  = format.format(calendar.getTime());

                        pieChartsButton.setText(formattedDate1+"/"+formattedDate2);

                        setPieCharts(formattedDate1,formattedDate2);
                    }
                });

                mdp.show(getSupportFragmentManager(),"TAG");

            }
        });

    }

    void setPieCharts(String dateFrom,String dateTo){

        setProfitData(dateFrom,dateTo);
        setReservationsData(dateFrom,dateTo);

    }

    void setProfitData(String dateFrom,String dateTo){
        Call call=analyticsService.getOwnerProfit(3L,dateFrom,dateTo);

        try {
            Response<List<ChartData>> response=call.execute();

            if (response.code() == 200){

                Log.d("REZ","Meesage recieved");
                this.profitData=setPieChartData(response.body());

            }else{
                Log.d("REZ","Meesage recieved: "+response.code());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    void setReservationsData(String dateFrom,String dateTo){
        Call call=analyticsService.getOwnerReservationCount(3L,dateFrom,dateTo);

        try {
            Response<List<ChartData>> response=call.execute();

            if (response.code() == 200){

                Log.d("REZ","Meesage recieved");
                this.reservationsData=setPieChartData(response.body());

            }else{
                Log.d("REZ","Meesage recieved: "+response.code());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    List<PieEntry> setPieChartData(List<ChartData> data){

        List<PieEntry> entries=new ArrayList<>();

        for(ChartData c : data){
            entries.add(new PieEntry(c.getValue().intValue(),c.getName()));
        }

        return entries;

    }
}