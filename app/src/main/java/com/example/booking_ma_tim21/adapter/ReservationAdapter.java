package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.ReservationDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.model.enumeration.ReservationStatus;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.ReservationService;
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

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    Context context;
    List<ReservationDTO> reservationList;

    public List<ReservationAdapter.ReservationViewHolder> getHolders() {
        return holders;
    }

    List<ReservationAdapter.ReservationViewHolder> holders=new ArrayList<>();

    public ReservationAdapter(Context context,List<ReservationDTO> reservations) {
        this.context=context;
        this.reservationList = reservations;
    }


    @NonNull
    @Override
    public ReservationAdapter.ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cl_reservation,parent,false);
        ReservationAdapter.ReservationViewHolder holder=new ReservationAdapter.ReservationViewHolder(view);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.ReservationViewHolder holder, int position) {
        ReservationDTO reservation = reservationList.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }


    public class ReservationViewHolder extends RecyclerView.ViewHolder {
        AccommodationService accService;
        UserService userService;
        ReservationService reservationService;


        private TextView nameTextView;
        private TextView dateFromTextView;
        private TextView dateToTextView;
        private TextView userNameTextView;
        private TextView guestsTextView;
        private TextView priceTextView;
        private TextView statusTextView;
        private MaterialButton cancelButton;
        private int daysForCancellation= 0;
        public ReservationViewHolder(@NonNull View itemView) {

            super(itemView);

            RetrofitService ref=new RetrofitService();
            this.accService=ref.getRetrofit().create(AccommodationService.class);
            this.userService=ref.getRetrofit().create(UserService.class);
            this.reservationService=ref.getRetrofit().create(ReservationService.class);

            // Initialize views
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateFromTextView = itemView.findViewById(R.id.dateFromTextView);
            dateToTextView = itemView.findViewById(R.id.dateToTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            guestsTextView = itemView.findViewById(R.id.guestsTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            cancelButton = itemView.findViewById(R.id.cancel_btn);
        }

        public void bind(ReservationDTO reservationDTO) {
            String[] dates=convertDates(reservationDTO.getTimeSlot());
            setAccommodationName(reservationDTO.getAccommodationId());
            dateFromTextView.setText("From: " + dates[0]);
            dateToTextView.setText("To: " + dates[1]);
            setUserName(reservationDTO.getUserId());
            guestsTextView.setText("Guests: " + String.valueOf(reservationDTO.getGuestsNumber()));
            priceTextView.setText("Price: $" + String.valueOf(reservationDTO.getPrice()));
            statusTextView.setText("Status: " + reservationDTO.getStatus());

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(reservationDTO.getStatus() != ReservationStatus.Active) {
                        Toast.makeText(context, "Can't Cancel Reservation.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long lastDayForCancellationMillis = System.currentTimeMillis() + ((daysForCancellation - 1) * 24L * 60 * 60 * 1000);
                    long lastDayForCancellationSeconds = lastDayForCancellationMillis / AppConfig.UNIX_DIFF;
                    long startDate = reservationDTO.getTimeSlot().getStartDate() / AppConfig.UNIX_DIFF;
                    if (startDate <= lastDayForCancellationSeconds) {
                        Toast.makeText(context, "Period For Cancellation Has Passed.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    reservationDTO.setStatus(ReservationStatus.Cancelled);

                    Call<ReservationDTO> call = reservationService.updateReservation(reservationDTO);

                    call.enqueue(new Callback<ReservationDTO>() {
                        @Override
                        public void onResponse(Call<ReservationDTO> call, Response<ReservationDTO> response) {
                            if (response.isSuccessful()) {
                                ReservationDTO reservationDTO1 = response.body();
                                notifyDataSetChanged();
                                Toast.makeText(context, "Request Declined.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("API Call", "Error: " + response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<ReservationDTO> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
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
                        daysForCancellation = detailsDTO.getDaysForCancellation();
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
