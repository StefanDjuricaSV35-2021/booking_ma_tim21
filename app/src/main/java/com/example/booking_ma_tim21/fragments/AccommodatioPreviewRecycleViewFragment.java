package com.example.booking_ma_tim21.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.AccommodationActivity;
import com.example.booking_ma_tim21.activities.AccountActivity;
import com.example.booking_ma_tim21.activities.OwnersAccommodationsActivity;
import com.example.booking_ma_tim21.adapter.PreviewAdapter;
import com.example.booking_ma_tim21.adapter.account.AccountAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.AccommodationDetailsDTO;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodatioPreviewRecycleViewFragment extends Fragment {
    private UserDTO loggedInUser;
    AuthManager authManager;

    RecyclerView previewRecycler;
    PreviewAdapter previewAdapter;
    AccommodationService service;
    private UserService userService;
    RelativeLayout loadingPanel;
    String location;
    String[] dates=null;
    String guests=null;
    String filter;

    boolean showOwnersAccommodations = false;



    public AccommodatioPreviewRecycleViewFragment() {
        // Required empty public constructor
    }

    public static AccommodatioPreviewRecycleViewFragment newInstance(String location, String guests, String date,String filter, boolean showOwnersAccommodations) {

        AccommodatioPreviewRecycleViewFragment fragment = new AccommodatioPreviewRecycleViewFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        args.putString("guests", guests);
        args.putString("date", date);
        args.putString("filter",filter);
        args.putBoolean("showOwnersAccommodations", showOwnersAccommodations);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSearchParamsFromArgs();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {return inflater.inflate(R.layout.fragment_accommodatio_preview_recycle_view, container, false);}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RetrofitService retrofitService= new RetrofitService();
        this.userService=retrofitService.getRetrofit().create(UserService.class);
        service=retrofitService.getRetrofit().create(AccommodationService.class);

        loadingPanel=getView().findViewById(R.id.loadingPanel);
        initializePreviews();

    }

    void getSearchParamsFromArgs(){

        Bundle args=getArguments();
        if(args==null){return;}
        guests=args.getString("guests");
        dates = args.getString("date", "Date/Date").split("/");
        location=args.getString("location");
        filter=args.getString("filter");
        showOwnersAccommodations=(args.getBoolean("showOwnersAccommodations"));
    }

    private void initializePreviews(){
        Call call = null;

        Bundle args = getArguments();
        if(args !=null) {
            if (showOwnersAccommodations) {
                initializeUserAndAccommodations();
                return;
            } else {
                call = service.getFilteredAccommodations(dates[0], dates[1], Integer.parseInt(guests), location, filter);
            }


        }else {
            call = service.getAllAccommodations();
        }

        enqueuePreviewCall(call);
    }

    void enqueuePreviewCall(Call call){

        call.enqueue(new Callback<List<AccommodationPreviewDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationPreviewDTO>> call, Response<List<AccommodationPreviewDTO>> response) {
                if (response.code() == 200){

                    Log.d("REZ","Meesage recieved");
                    List<AccommodationPreviewDTO> previewDTOs = response.body();
                    loadingPanel.setVisibility(View.GONE);
                    setPreviewRecycler(previewDTOs);

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationPreviewDTO>> call, Throwable t) {
                t.printStackTrace();
            }


        });


    }


    private void setPreviewRecycler(List<AccommodationPreviewDTO> accommodationPreviewDTOs){

        previewRecycler = getView().findViewById(R.id.preview_recycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        previewRecycler.setLayoutManager(layoutManager);
        previewAdapter= new PreviewAdapter(getContext(), accommodationPreviewDTOs, new PreviewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(AccommodationPreviewDTO preview) {

                Call call= service.getAccommodation(preview.getId());
                enqueueDetailsCall(call);

            }
        });
        previewRecycler.setAdapter(previewAdapter);

    }

    void enqueueDetailsCall(Call call){
        call.enqueue(new Callback<AccommodationDetailsDTO>() {
            @Override
            public void onResponse(Call<AccommodationDetailsDTO> call, Response<AccommodationDetailsDTO> response) {
                if (response.code() == 200){

                    Log.d("REZ","Meesage recieved");
                    AccommodationDetailsDTO detailsDTO = response.body();
                    loadingPanel.setVisibility(View.GONE);
                    showDetails(detailsDTO);

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

    void showDetails(AccommodationDetailsDTO detailsDTO){



        Intent intent=new Intent(getActivity(), AccommodationActivity.class);
        intent.putExtra("accommodation",detailsDTO);

        if(guests!=null&&dates!=null) {
            Bundle searchParams = new Bundle();
            searchParams.putString("guests", guests);
            searchParams.putString("dates", dates[0] + "/" + dates[1]);
            searchParams.putLong("id",detailsDTO.getId());
            searchParams.putBoolean("searched",true);
            intent.putExtras(searchParams);
        }else{
            Bundle searchParams = new Bundle();
            searchParams.putLong("id",detailsDTO.getId());
            searchParams.putBoolean("searched",false);
            intent.putExtras(searchParams);
        }

        startActivity(intent);

    }

    private void initializeUserAndAccommodations() {
        OwnersAccommodationsActivity activity = (OwnersAccommodationsActivity) getActivity();
        authManager = activity.getAuthManager();
        String email = authManager.getUserId();
        Call<UserDTO> call = this.userService.getUser(email);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    loggedInUser = response.body();
                    Call newCall = service.getOwnersAccommodations(loggedInUser.getId());
                    enqueuePreviewCall(newCall);
                } else {
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePreviews();
    }
}