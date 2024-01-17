package com.example.booking_ma_tim21.fragments;

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
import com.example.booking_ma_tim21.adapter.UpdatingPreviewAdapter;
import com.example.booking_ma_tim21.adapter.UserReportsAdapter;
import com.example.booking_ma_tim21.dto.AccommodationChangeRequestDTO;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.dto.UserReportDTO;
import com.example.booking_ma_tim21.model.UserReportWithEmails;
import com.example.booking_ma_tim21.retrofit.AccommodationChangeRequestService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserReportService;
import com.example.booking_ma_tim21.retrofit.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserReportRecycleViewFragment extends Fragment {

    List<UserReportWithEmails> userReports = new ArrayList<>();
    RecyclerView userReportRecycler;
    UserReportsAdapter userReportsAdapter;
    UserReportService userReportService;
    UserService userService;

    UserDTO reportedUser;
    UserDTO reporterUser;

    RelativeLayout loadingPanel;

    public UserReportRecycleViewFragment() {
        // Required empty public constructor
    }

    public static UserReportRecycleViewFragment newInstance() {
        UserReportRecycleViewFragment fragment = new UserReportRecycleViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_report_recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RetrofitService retrofitService = new RetrofitService();
        userReportService = retrofitService.getRetrofit().create(UserReportService.class);
        userService = retrofitService.getRetrofit().create(UserService.class);

        userReportRecycler = view.findViewById(R.id.user_reports_recycler);

        userReportsAdapter = new UserReportsAdapter(getContext(), userReports, new UserReportsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(UserReportWithEmails preview) {
                // Handle item click if needed
            }
        });

        userReportRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        userReportRecycler.setAdapter(userReportsAdapter);

        fetchUserReports();
    }

    private void fetchUserReports() {
        Call<List<UserReportDTO>> newCall = userReportService.getUserReports();
        newCall.enqueue(new Callback<List<UserReportDTO>>() {
            @Override
            public void onResponse(Call<List<UserReportDTO>> call, Response<List<UserReportDTO>> response) {
                if (response.isSuccessful()) {
                    List<UserReportDTO> userReportDTOS = response.body();
                    processUserReports(userReportDTOS);
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserReportDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void processUserReports(List<UserReportDTO> userReportDTOS) {
        for (UserReportDTO userReportDTO : userReportDTOS) {
            processUserReport(userReportDTO);
        }
        userReportsAdapter.notifyDataSetChanged();
    }

    private void processUserReport(UserReportDTO userReportDTO) {
        Call<UserDTO> callReported = userService.getUser(userReportDTO.getReportedId());
        callReported.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO reportedUser = response.body();
                    processReporter(userReportDTO, reportedUser);
                } else {
                    // Handle failure
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void processReporter(UserReportDTO userReportDTO, UserDTO reportedUser) {
        Call<UserDTO> callReporter = userService.getUser(userReportDTO.getReporterId());
        callReporter.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO reporterUser = response.body();
                    UserReportWithEmails userReport = new UserReportWithEmails(userReportDTO.getId(), reportedUser, reporterUser, userReportDTO.getDescription());
                    userReports.add(userReport);
                    userReportsAdapter.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initializeReports() {
        setReportsRecycler(this.userReports);
    }

    private void setReportsRecycler(List<UserReportWithEmails> userReports) {
        userReportRecycler = getView().findViewById(R.id.user_reports_recycler);

        if (userReports != null && !userReports.isEmpty()) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            userReportRecycler.setLayoutManager(layoutManager);
            userReportsAdapter = new UserReportsAdapter(getContext(), userReports, new UserReportsAdapter.ItemClickListener() {
                @Override
                public void onItemClick(UserReportWithEmails preview) {
                }
            });
            userReportRecycler.setAdapter(userReportsAdapter);
        } else {
            userReportRecycler.setVisibility(View.GONE);
        }
    }
}
