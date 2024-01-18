package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.ReservationRequestDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.model.enumeration.ReservationRequestStatus;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.ReservationRequestService;
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

public class ReservationRequestsAdapter extends RecyclerView.Adapter<ReservationRequestsAdapter.ReservationRequestViewHolder> {

    Context context;
    List<ReservationRequestDTO> reservationList;

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

    public class ReservationRequestViewHolder extends RecyclerView.ViewHolder {
        AuthManager auth;
        AccommodationService accService;
        UserService userService;
        ReservationRequestService reservationRequestService;

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
            this.reservationRequestService=ref.getRetrofit().create(ReservationRequestService.class);

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

        public void bind(ReservationRequestDTO reservationRequestDTO) {
            String[] dates=convertDates(reservationRequestDTO.getTimeSlot());
            // Bind data to views
            setAccommodationName(reservationRequestDTO.getAccommodationId());
            dateFromTextView.setText("From: " + dates[0]);
            dateToTextView.setText("To: " + dates[1]);
            setUserName(reservationRequestDTO.getUserId());
            guestsTextView.setText("Guests: " + String.valueOf(reservationRequestDTO.getGuestsNumber()));
            priceTextView.setText("Price: $" + String.valueOf(reservationRequestDTO.getPrice()));
            statusTextView.setText("Status: " + reservationRequestDTO.getStatus());

            // Set up click listeners for accept and reject buttons
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(reservationRequestDTO.getStatus() != ReservationRequestStatus.Waiting) {
                        Toast.makeText(context, "Can't Accept Request.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    reservationRequestDTO.setStatus(ReservationRequestStatus.Accepted);
                    Call<ReservationRequestDTO> call = reservationRequestService.updateReservationRequest(reservationRequestDTO);
                    call.enqueue(new Callback<ReservationRequestDTO>() {
                        @Override
                        public void onResponse(Call<ReservationRequestDTO> call, Response<ReservationRequestDTO> response) {
                            if (response.isSuccessful()) {
                                ReservationRequestDTO reservationRequest = response.body();
                                updateRequests();
//                                notifyDataSetChanged();
//                                Toast.makeText(context, "Request Accepted.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("API Call", "Error: " + response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<ReservationRequestDTO> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(reservationRequestDTO.getStatus() != ReservationRequestStatus.Waiting) {
                        Toast.makeText(context, "Can't Decline Request.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    reservationRequestDTO.setStatus(ReservationRequestStatus.Declined);

                    Call<ReservationRequestDTO> call = reservationRequestService.updateReservationRequest(reservationRequestDTO);

                    call.enqueue(new Callback<ReservationRequestDTO>() {
                        @Override
                        public void onResponse(Call<ReservationRequestDTO> call, Response<ReservationRequestDTO> response) {
                            if (response.isSuccessful()) {
                                ReservationRequestDTO reservationRequest = response.body();
                                notifyDataSetChanged();
                                Toast.makeText(context, "Request Declined.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("API Call", "Error: " + response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<ReservationRequestDTO> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            });
        }

        private void updateRequests() {

            auth=AuthManager.getInstance(context);
            Call<List<ReservationRequestDTO>> call = reservationRequestService.getOwnerReservationRequests(auth.getUserIdLong());
            call.enqueue(new Callback<List<ReservationRequestDTO>>() {
                @Override
                public void onResponse(Call<List<ReservationRequestDTO>> call, Response<List<ReservationRequestDTO>> response) {
                    if (response.isSuccessful()) {
                        List<ReservationRequestDTO> reservationRequests = response.body();
                        reservationList.clear();
                        reservationList.addAll(reservationRequests);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Request Accepted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("API Call", "Error: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<List<ReservationRequestDTO>> call, Throwable t) {
                    t.printStackTrace();
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