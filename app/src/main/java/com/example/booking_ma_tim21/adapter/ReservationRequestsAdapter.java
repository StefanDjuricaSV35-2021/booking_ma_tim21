package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.ReservationRequestDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.AppConfig;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRequestsAdapter extends RecyclerView.Adapter<ReservationRequestsAdapter.ReservationRequestViewHolder> {

    Context context;
    private List<ReservationRequestDTO> reservationList;

    public List<ReservationRequestViewHolder> getHolders() {
        return holders;
    }

    List<ReservationRequestViewHolder> holders=new ArrayList<>();

    public ReservationRequestsAdapter(Context context,List<ReservationRequestDTO> reservations) {
        this.context=context;
        this.reservationList = reservations;
    }


    @NonNull
    @Override
    public ReservationRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cl_reservation_request,parent,false);
        ReservationRequestViewHolder holder=new ReservationRequestViewHolder(view);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationRequestViewHolder holder, int position) {
        ReservationRequestDTO reservation = reservationList.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationRequestViewHolder extends RecyclerView.ViewHolder {

        AccommodationService accService;
        UserService userService;

        public TextView getNameTextView() {
            return nameTextView;
        }

        private TextView nameTextView;
        private TextView dateFromTextView;
        private TextView dateToTextView;
        private TextView userNameTextView;
        private TextView guestsTextView;
        private TextView priceTextView;
        private TextView statusTextView;
        private MaterialButton acceptButton;
        private MaterialButton rejectButton;

        public ReservationRequestViewHolder(@NonNull View itemView) {

            super(itemView);

            RetrofitService ref=new RetrofitService();
            this.accService=ref.getRetrofit().create(AccommodationService.class);
            this.userService=ref.getRetrofit().create(UserService.class);

            // Initialize views
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateFromTextView = itemView.findViewById(R.id.dateFromTextView);
            dateToTextView = itemView.findViewById(R.id.dateToTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            guestsTextView = itemView.findViewById(R.id.guestsTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            acceptButton = itemView.findViewById(R.id.accept_btn);
            rejectButton = itemView.findViewById(R.id.decline_btn);
        }

        public void bind(ReservationRequestDTO reservation) {
            String[] dates=convertDates(reservation.getTimeSlot());
            // Bind data to views
            setAccommodationName(reservation.getAccommodationId());
            dateFromTextView.setText("From: " + dates[0]);
            dateToTextView.setText("To: " + dates[1]);
            setUserName(reservation.getUserId());
            guestsTextView.setText("Guests: " + String.valueOf(reservation.getGuestsNumber()));
            priceTextView.setText("Price: $" + String.valueOf(reservation.getPrice()));
            statusTextView.setText("Status: " + reservation.getStatus());

            // Set up click listeners for accept and reject buttons
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle accept button click
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle reject button click
                }
            });
        }

        void setAccommodationName(Long id){

            Call call=this.accService.getAccommodation(id);

            call.enqueue(new Callback<AccommodationDetailsDTO>() {
                @Override
                public void onResponse(Call<AccommodationDetailsDTO> call, Response<AccommodationDetailsDTO> response) {
                    if (response.code() == 200){

                        Log.d("REZ","Meesage recieved");
                        AccommodationDetailsDTO detailsDTO = response.body();
                        nameTextView.setText("Accommodation name: "+detailsDTO.getName());

                    }else{
                        Log.d("REZ","Meesage recieved: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<AccommodationDetailsDTO> call, Throwable t) {
                    t.printStackTrace();
                }


            });


        }

        void setUserName(Long id){

            Call call=this.userService.getUser(id);

            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.code() == 200){

                        Log.d("REZ","Meesage recieved");
                        userNameTextView.setText("User name: "+response.body().getName());

                    }else{
                        Log.d("REZ","Meesage recieved: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    t.printStackTrace();
                }


            });


        }

        String[] convertDates(TimeSlot ts){

            String[] dates= new String[2];

            long dv = Long.valueOf(ts.getStartDate())* AppConfig.UNIX_DIFF;// its need to be in milisecond
            Date df = new java.util.Date(dv);
            String vv = new SimpleDateFormat("yyyy-MM-dd").format(df);
            dates[0]=vv;

            dv = Long.valueOf(ts.getEndDate())* AppConfig.UNIX_DIFF;// its need to be in milisecond
            df = new java.util.Date(dv);
            vv = new SimpleDateFormat("yyyy-MM-dd").format(df);
            dates[1]=vv;


            return dates;


        }
    }
}