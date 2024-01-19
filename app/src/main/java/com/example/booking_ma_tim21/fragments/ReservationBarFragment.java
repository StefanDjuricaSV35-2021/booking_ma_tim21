package com.example.booking_ma_tim21.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.MainActivity;
import com.example.booking_ma_tim21.activities.ReservationSuccessActivity;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.NotificationDTO;
import com.example.booking_ma_tim21.dto.NotificationType;
import com.example.booking_ma_tim21.dto.ReservationRequestDTO;
import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.model.enumeration.ReservationRequestStatus;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.NotificationsService;
import com.example.booking_ma_tim21.retrofit.ReservationRequestService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.services.NotificationService;
import com.google.android.material.button.MaterialButton;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationBarFragment extends Fragment {

    public TextView date;
    public TextView guests;
    public TextView price;
    MaterialButton reserve;
    MaterialButton edit;
    MaterialButton makeRes;
    Bundle resParams;
    Bundle resRestrictions;
    AccommodationService serviceAcc;
    ReservationRequestService serviceResReq;




    public ReservationBarFragment() {
        // Required empty public constructor
    }

    public static ReservationBarFragment newInstance(Bundle resParams, Bundle resRestrictions) {
        ReservationBarFragment fragment = new ReservationBarFragment();

        Bundle args = new Bundle();
        args.putBundle("res_params", resParams);
        args.putBundle("res_restrict", resRestrictions);

        fragment.setArguments(args);
        return fragment;
    }

    public static ReservationBarFragment newInstance( Bundle resRestrictions) {
        ReservationBarFragment fragment = new ReservationBarFragment();

        Bundle args = new Bundle();
        args.putBundle("res_restrict", resRestrictions);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resParams=getArguments().getBundle("res_params");
        resRestrictions=getArguments().getBundle("res_restrict");

        RetrofitService rs=new RetrofitService();
        serviceAcc =rs.getRetrofit().create(AccommodationService.class);
        serviceResReq=rs.getRetrofit().create(ReservationRequestService.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation_bar, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        date=view.findViewById(R.id.date_res_tv);
        guests=view.findViewById(R.id.guests_res_tv);
        price=view.findViewById(R.id.price_res_tv);
        reserve=view.findViewById(R.id.res_btn);
        edit=view.findViewById(R.id.edit_btn);
        makeRes=view.findViewById(R.id.make_res_btn);

        if(resParams.getBoolean("searched")==true){
            setResParams(resParams);
        }

        setButtonClicks();

    }

    void setButtonClicks(){

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthManager auth=AuthManager.getInstance(getContext());

                String[] dates=date.getText().toString().split("/");
                ZoneId zoneId = ZoneId.of("GMT");
                LocalDate dateFrom = LocalDate.parse(dates[0]);
                LocalDate dateTo = LocalDate.parse(dates[1]);

                Long userId=auth.getUserIdLong();
                Long accId=resParams.getLong("id");
                Integer noGuests=Integer.parseInt(guests.getText().toString());
                Double priceVal=Double.parseDouble(price.getText().toString());
                TimeSlot ts=new TimeSlot(dateFrom.atStartOfDay(zoneId).toEpochSecond(),dateTo.atStartOfDay(zoneId).toEpochSecond());
                ReservationRequestStatus reqStauts=ReservationRequestStatus.Waiting;

                Long ownerId=resRestrictions.getLong("owner");

                NotificationDTO notif=new NotificationDTO(NotificationType.RESERVATION_REQUEST,"You have a new reservation request",ownerId);
                NotificationService.getInstance().sendNotification(notif);

                ReservationRequestDTO req=new ReservationRequestDTO(userId,accId,noGuests,priceVal,ts,reqStauts);
//                NotificationDTO notification = new NotificationDTO(0l, NotificationType.RESERVATION_REQUEST,"You have a new reservation request!",);
//                NotificationService.getInstance();
                Call call=serviceResReq.createReservationRequest(req);
                enqueueResReqCall(call);

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReservationParamsEditFragment dialogFragment= ReservationParamsEditFragment.newInstance(resRestrictions);
                dialogFragment.show(getChildFragmentManager(),"My  Fragment");

            }
        });

        makeRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationParamsEditFragment dialogFragment= ReservationParamsEditFragment.newInstance(resRestrictions);
                dialogFragment.show(getChildFragmentManager(),"My  Fragment");
            }
        });


    }

    public void setResParams(Bundle b) {
        makeRes.setVisibility(View.GONE);
        this.date.setText(b.getString("dates"));
        this.guests.setText(b.getString("guests"));

        setPrice(b);

    }

    void setPrice(Bundle b){
        Integer guests=Integer.parseInt(b.getString("guests"));
        String dateFrom=b.getString("dates").split("/")[0];
        String dateTo=b.getString("dates").split("/")[1];
        Long id=resParams.getLong("id");

        Call call= serviceAcc.getAccommodationsPrice(guests,dateFrom,dateTo,id);
        enqueuePriceCall(call);

    }


    void enqueueResReqCall(Call call){

        call.enqueue(new Callback<ReservationRequestDTO>() {
            @Override
            public void onResponse(Call<ReservationRequestDTO> call, Response<ReservationRequestDTO> response) {
                if (response.code() == 201){

                    Log.d("REZ","Meesage recieved");

                    Intent intent = new Intent(getActivity(), ReservationSuccessActivity.class); // Replace HomeActivity with your home page activity
                    startActivity(intent);
                    getActivity().finish();


                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ReservationRequestDTO> call, Throwable t) {
                t.printStackTrace();
            }


        });


    }
    void enqueuePriceCall(Call call){
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.code() == 200){

                    Log.d("REZ","Meesage recieved");
                    Double priceVal = response.body();
                    price.setText(priceVal.toString());

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                t.printStackTrace();
            }


        });

    }


}