package com.example.booking_ma_tim21.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.adapter.AmenitiesAdapter;
import com.example.booking_ma_tim21.adapter.FileAdapter;
import com.example.booking_ma_tim21.adapter.PricingAdapter;
import com.example.booking_ma_tim21.authentication.AuthManager;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.model.Accommodation;
import com.example.booking_ma_tim21.model.AccommodationPricing;
import com.example.booking_ma_tim21.model.TimeSlot;
import com.example.booking_ma_tim21.model.enumeration.AccommodationType;
import com.example.booking_ma_tim21.model.enumeration.Amenity;
import com.example.booking_ma_tim21.retrofit.AccommodationPricingService;
import com.example.booking_ma_tim21.retrofit.AccommodationService;
import com.example.booking_ma_tim21.retrofit.FileUploadService;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.FileUtil;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationCreation extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    AuthManager authManager;
    UserService userService;
    AccommodationService accommodationService;
    FileUploadService fileUploadService;

    AccommodationPricingService accommodationPricingService;
    private EditText street_input_field;
    private EditText city_input_field;
    private EditText country_input_field;
    private EditText accommodation_name;
    private EditText min_guests;
    private EditText max_guests;
    private EditText cancellation_days;
    private EditText descriptionEditText;
    private Spinner accommodation_type;
    private RecyclerView amenities;
    private RecyclerView timeslots;
    private AmenitiesAdapter amenitiesAdapter;
    private PricingAdapter pricingAdapter;
    private Button btnStartDate;
    private Button btnEndDate;
    private Calendar startDate;
    private Calendar endDate;
    private EditText price_input_field;
    private  Button submit_price;
    private RecyclerView images;
    private FileAdapter fileAdapter;
    private Button btnSelectFile;
    private TextView selected_file_name;
    private Button submit_file;
    private File selected_file;
    private Button submit_accommodation;

    private Long userId;

    private RadioButton radio_button_per_night;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_creation);

        authManager = AuthManager.getInstance(getApplicationContext());
        NavigationSetup.setupNavigation(this, authManager);

        RetrofitService retrofitService= new RetrofitService();
        userService=retrofitService.getRetrofit().create(UserService.class);
        accommodationService=retrofitService.getRetrofit().create(AccommodationService.class);
        accommodationPricingService=retrofitService.getRetrofit().create(AccommodationPricingService.class);
        fileUploadService=retrofitService.getRetrofit().create(FileUploadService.class);

        getUserId(authManager.getUserId());

//        initialize normal fields
        street_input_field = findViewById(R.id.street_input_field);
        city_input_field = findViewById(R.id.city_input_field);
        country_input_field = findViewById(R.id.country_input_field);
        accommodation_name = findViewById(R.id.accommodation_name);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        min_guests = findViewById(R.id.min_guests);
        max_guests = findViewById(R.id.max_guests);
        cancellation_days = findViewById(R.id.cancellation_days);

//        accommodation type adapter
        accommodation_type = findViewById(R.id.accommodation_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.accommodation_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accommodation_type.setAdapter(adapter);

//        amenity adapter
        List<Amenity> amenitiesList = new ArrayList<>();
        amenitiesList.add(Amenity.TV);
        amenitiesList.add(Amenity.Parking);
        amenitiesList.add(Amenity.WiFi);
        amenitiesList.add(Amenity.SmokeAlarm);

        amenities = findViewById(R.id.amenities);
        amenities.setLayoutManager(new LinearLayoutManager(this));

        amenitiesAdapter = new AmenitiesAdapter(amenitiesList);
        amenities.setAdapter(amenitiesAdapter);

//        accommodation pricing adapter
        List<AccommodationPricing> accommodationPricings = new ArrayList<>();

        timeslots = findViewById(R.id.timeslots);
        timeslots.setLayoutManager(new LinearLayoutManager(this));

        pricingAdapter = new PricingAdapter(accommodationPricings);
        timeslots.setAdapter(pricingAdapter);

//        fields for accommodation pricing
        radio_button_per_night = findViewById(R.id.radio_button_per_night);

        btnStartDate = findViewById(R.id.btnStartDate);
        startDate = Calendar.getInstance();

        btnStartDate.setOnClickListener(v -> showDatePickerDialog(startDate, btnStartDate));

        btnEndDate = findViewById(R.id.btnEndDate);
        endDate = Calendar.getInstance();

        btnEndDate.setOnClickListener(v -> showDatePickerDialog(endDate, btnEndDate));

        price_input_field = findViewById(R.id.price_input_field);

        submit_price = findViewById(R.id.submit_price);
        submit_price.setOnClickListener(v -> {
            if(isValidPricingInput()){
                Date start = createMidnightDate(startDate);
                Date end = createMidnightDate(endDate);

                AccommodationPricing pricing = new AccommodationPricing(0l,0l,new TimeSlot(start.getTime(),end.getTime()),Double.parseDouble(price_input_field.getText().toString()));

                if(pricingAdapter.addItem(pricing)){
                    Toast.makeText(getApplicationContext(), "Added new pricing!", Toast.LENGTH_SHORT).show();
                    btnStartDate.setText("Pick Start Date");
                    startDate = Calendar.getInstance();

                    btnEndDate.setText("Pick End Date");
                    endDate = Calendar.getInstance();

                    price_input_field.setText("");
                }else{
                    Toast.makeText(getApplicationContext(), "Can't add new pricing since it overlaps with another!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//      image list
        List<File> fileList = new ArrayList<>();

        images = findViewById(R.id.images);
        images.setLayoutManager(new LinearLayoutManager(this));

        fileAdapter = new FileAdapter(fileList);
        images.setAdapter(fileAdapter);

        btnSelectFile = findViewById(R.id.btnSelectFile);
        selected_file_name = findViewById(R.id.selected_file_name);
        submit_file = findViewById(R.id.submit_file);

        btnSelectFile.setOnClickListener(v -> openFilePicker());
        submit_file.setOnClickListener(v -> {
            if(selected_file != null){
                if(fileAdapter.addItem(selected_file)){
                    Toast.makeText(getApplicationContext(), "Added new photo!", Toast.LENGTH_SHORT).show();
                    selected_file_name.setText("");
                    selected_file = null;
                }else{
                    Toast.makeText(getApplicationContext(), "Can't add new photo since it shares a name with another added photo!", Toast.LENGTH_SHORT).show();
                }
            }
        });
//      submiting file
        submit_accommodation = findViewById(R.id.submit_accommodation);
        submit_accommodation.setOnClickListener(v -> {

//            String street = street_input_field.getText().toString().trim();
//            String city = city_input_field.getText().toString().trim();
//            String country = country_input_field.getText().toString().trim();
//            String name = accommodation_name.getText().toString().trim();
//            String description = descriptionEditText.getText().toString().trim();
//
//            String minGuestsText = min_guests.getText().toString().trim();
//            String maxGuestsText = max_guests.getText().toString().trim();
//            String cancellationDaysText = cancellation_days.getText().toString().trim();
//
//            String accommodationTypeString = accommodation_type.getSelectedItem().toString();
//            AccommodationType accommodationType;
//
//            if (accommodationTypeString == null) {
//                Toast.makeText(getApplicationContext(), "Accommodation type is required!", Toast.LENGTH_SHORT).show();
//                return;
//            }else{
//                accommodationType = AccommodationType.fromString(accommodationTypeString);
//                if(accommodationType == null){
//                    Toast.makeText(getApplicationContext(), "Something went wrong with accommodation type.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//
//            if (street.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "Street is required!", Toast.LENGTH_SHORT).show();
//                return;
//            } else if (city.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "City is required!", Toast.LENGTH_SHORT).show();
//                return;
//            } else if (country.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "Country is required!", Toast.LENGTH_SHORT).show();
//                return;
//            } else if (name.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "Accommodation name is required!", Toast.LENGTH_SHORT).show();
//                return;
//            } else if(userId == null){
//                Toast.makeText(getApplicationContext(), "Something went wrong with your id.", Toast.LENGTH_SHORT).show();
//                return;
//            } else if(description.isEmpty()){
//                Toast.makeText(getApplicationContext(), "Description is required!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//
//
//            int minGuests;
//            int maxGuests;
//            int cancellationDays;
//            try {
//                minGuests = Integer.parseInt(minGuestsText);
//
//                try {
//                    maxGuests = Integer.parseInt(maxGuestsText);
//
//                    try {
//                        cancellationDays = Integer.parseInt(cancellationDaysText);
//
//                        if (minGuests > maxGuests) {
//                            Toast.makeText(getApplicationContext(), "Min guests should be less than or equal to max guests!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    } catch (NumberFormatException e) {
//                        Toast.makeText(getApplicationContext(), "Enter a valid number for cancellation days!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                } catch (NumberFormatException e) {
//                    Toast.makeText(getApplicationContext(), "Enter a valid number for max guests!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            } catch (NumberFormatException e) {
//                Toast.makeText(getApplicationContext(), "Enter a valid number for min guests!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//
//            Accommodation accommodation = new Accommodation();
//            accommodation.setAmenities(amenitiesAdapter.getSelectedAmenities());
//            accommodation.setName(name);
//            accommodation.setId(0l);
//            accommodation.setType(accommodationType);
//            accommodation.setDescription(description);
//            accommodation.setPhotos(fileAdapter.getFileNameList());
//            accommodation.setDaysForCancellation(cancellationDays);
//            accommodation.setMaxGuests(maxGuests);
//            accommodation.setMinGuests(minGuests);
//            accommodation.setOwnerId(userId);
//            accommodation.setEnabled(false);
//            accommodation.setPerNight(radio_button_per_night.isChecked());
//            accommodation.setLocation(street + ',' + city + ',' + country);
//            accommodation.setDates(new ArrayList<>());
//
//
//            createAccommodation(accommodation);
            uploadImages();
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("log", "onRestart()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
    private void showDatePickerDialog(Calendar selectedDate, Button btnPickDate) {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButtonText(selectedDate,btnPickDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
    private void updateDateButtonText(Calendar selectedDate,Button btnPickDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        btnPickDate.setText(dateFormat.format(selectedDate.getTime()));
    }
    private boolean isValidPricingInput() {
        if (startDate == null || endDate == null || price_input_field.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields are required in order to submit a new accommodation pricing!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (startDate.after(endDate) || startDate.equals(endDate)) {
            Toast.makeText(getApplicationContext(), "Start date must be before end date!", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double price = Double.parseDouble(price_input_field.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Price must be double value!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private Date createMidnightDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                File selectedImageFile = FileUtil.fromUri(selectedImageUri, this);
                if (selectedImageFile != null) {
                    selected_file = selectedImageFile;
                    selected_file_name.setText(selected_file.getName());
                }
            }
        }
    }

    private void getUserId(String email){
        retrofit2.Call<UserDTO> call = userService.getUser(email);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(retrofit2.Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    userId = userDTO.getId();
                } else {
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.d("REZ","Meesage failed to be received.");
            }
        });
    }

    private void createAccommodation(Accommodation accommodation){
        Call<Accommodation> call = accommodationService.createAccommodation(accommodation);
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.isSuccessful()) {
                    Accommodation ac = response.body();
                    Toast.makeText(getApplicationContext(), "Created accommodation!", Toast.LENGTH_SHORT).show();

                    for (AccommodationPricing ap: pricingAdapter.getPricingList()) {
                        ap.setAccommodationId(ac.getId());
                        createAccommodationPricing(ap);
                    }
                    Toast.makeText(getApplicationContext(), "Created accommodation pricing.", Toast.LENGTH_SHORT).show();


                } else {
                    Log.d("REZ","ne.");
                    Toast.makeText(getApplicationContext(), "Failed to create accomodation.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.d("REZ","Meesage failed to be received.");
            }
        });
    }

    private void createAccommodationPricing(AccommodationPricing accommodationPricing){
        Call<AccommodationPricing> call = accommodationPricingService.createAccommodationPricing(accommodationPricing);
        call.enqueue(new Callback<AccommodationPricing>() {
            @Override
            public void onResponse(Call<AccommodationPricing> call, Response<AccommodationPricing> response) {
                if (response.isSuccessful()) {
                    AccommodationPricing ac = response.body();

                } else {
                    Log.d("REZ","ne.");
                    Toast.makeText(getApplicationContext(), "Failed to create accomodation pricing.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationPricing> call, Throwable t) {
                Log.d("REZ","Pricing Meesage failed to be received.");
            }
        });
    }

    private void uploadImages(){
        List<MultipartBody.Part> parts = new ArrayList<>();

        for (File file : fileAdapter.getFileList()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
            parts.add(body);
        }

        if (parts.isEmpty()){
            return;
        }

        Call<List<String>> call = fileUploadService.uploadFiles(parts);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> fileNames = response.body();

                } else {
                    Log.d("REZ","ne.");
                    Toast.makeText(getApplicationContext(), "Failed to create images.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d("REZ","Image meesage failed to be received.");
            }
        });
    }
}