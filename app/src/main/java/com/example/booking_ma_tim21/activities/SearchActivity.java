package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.AccommodationPreviewDTO;
import com.example.booking_ma_tim21.fragments.AccommodatioPreviewRecycleViewFragment;
import com.example.booking_ma_tim21.fragments.AccommodationFilterFragment;
import com.example.booking_ma_tim21.fragments.SearchClosedFragment;
import com.example.booking_ma_tim21.fragments.SearchFragment;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchClosedFragment.SearchOpenListener,AccommodationFilterFragment.OnFilter {

    AccommodationService accService;
    Button filterBtn;
    DialogFragment filterDialog;
    String guests;
    String location;
    String date;
    String filter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RetrofitService retrofitService= new RetrofitService();
        accService = retrofitService.getRetrofit().create(AccommodationService.class);

        this.filterDialog=new AccommodationFilterFragment();

        getSearchParamsFromIntent();
        initiateClosedSearchFragment();
        getPreviews(location,guests,date,null);

        filterBtn =findViewById(R.id.filter_btn);

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.show(getSupportFragmentManager(),"filter");
            }
        });



    }

    void getSearchParamsFromIntent(){

        Intent intent=getIntent();
        guests=intent.getStringExtra("guests");
        date= intent.getStringExtra("date");
        location=intent.getStringExtra("location");

    }

    void initiateRecycleView(ArrayList<AccommodationPreviewDTO> previews){



        Fragment fragment= AccommodatioPreviewRecycleViewFragment.newInstance(previews);
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        fragmentTransaction.replace(R.id.preview_recycler_fragment, fragment, null);
        fragmentTransaction.commit();


    }

    void initiateClosedSearchFragment(){

        Intent intent=getIntent();
        String guests=intent.getStringExtra("guests");
        String date= intent.getStringExtra("date");
        String location=intent.getStringExtra("location");

        Fragment fragment= SearchClosedFragment.newInstance(location,guests,date);
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        fragmentTransaction.add(R.id.search_fragment, fragment, null);
        fragmentTransaction.commit();
    }

    private void getPreviews(String location,String guests,String date,String filter){
        Call call = null;
        String[]dates=date.split("/");

        call = accService.getFilteredAccommodations(dates[0], dates[1], Integer.parseInt(guests), location, filter);

        enqueuePreviewCall(call);
    }

    void enqueuePreviewCall(Call call){

        call.enqueue(new Callback<List<AccommodationPreviewDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationPreviewDTO>> call, Response<List<AccommodationPreviewDTO>> response) {
                if (response.code() == 200){

                    Log.d("REZ","Meesage recieved");
                    List<AccommodationPreviewDTO> previewDTOs = response.body();
                    initiateRecycleView((ArrayList)previewDTOs);

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

    void setPreviewFragment(List<AccommodationPreviewDTO> previews){



    }

    @Override
    public void openSearch() {

        float density =getResources().getDisplayMetrics().density;
        int px = Math.round(310 * density);

        Fragment fragment= new SearchFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .addSharedElement(findViewById(R.id.search_bg_ll),"openBg")
                .addSharedElement(findViewById(R.id.search_btn),"openSearch");

        fragmentTransaction.replace(R.id.search_fragment, fragment);
        fragmentTransaction.commit();




    }

    @Override
    public void filter(String data) {

        Intent intent=getIntent();
        String guests=intent.getStringExtra("guests");
        String date= intent.getStringExtra("date");
        String location=intent.getStringExtra("location");

        getPreviews(location,guests,date,data);

    }
}