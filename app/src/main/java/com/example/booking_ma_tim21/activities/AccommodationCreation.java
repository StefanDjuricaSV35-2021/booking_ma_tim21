package com.example.booking_ma_tim21.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import com.example.booking_ma_tim21.adapter.AmenityCheckBoxAdapter;
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
    private static final int REQUEST_STORAGE_PERMISSION = 100;

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
    private AmenityCheckBoxAdapter amenitiesAdapter;
    private PricingAdapter pricingAdapter;
    private Button btnStartDate;
    private Button btnEndDate;
    private Calendar startDate;
    private Calendar endDate;
    private EditText price_input_field;
    private  Button submit_price;
    private Button btnSelectFile;
    private List<Uri> selected_files = new ArrayList<>();
    private Button submit_accommodation;

    private Long userId;

    private RadioButton radio_button_per_night;

    private RecyclerView images;
    private FileAdapter fileAdapter;

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

        getUserId(authManager.getUserEmail());

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

        amenitiesAdapter = new AmenityCheckBoxAdapter(amenitiesList);
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
        images = findViewById(R.id.images);
        images.setLayoutManager(new LinearLayoutManager(this));

        fileAdapter = new FileAdapter(selected_files,this);
        images.setAdapter(fileAdapter);

        btnSelectFile = findViewById(R.id.btnSelectFile);

        btnSelectFile.setOnClickListener(v -> openFilePicker());

        submit_accommodation = findViewById(R.id.submit_accommodation);
        submit_accommodation.setOnClickListener(v -> {

            String street = street_input_field.getText().toString().trim();
            String city = city_input_field.getText().toString().trim();
            String country = country_input_field.getText().toString().trim();
            String name = accommodation_name.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            String minGuestsText = min_guests.getText().toString().trim();
            String maxGuestsText = max_guests.getText().toString().trim();
            String cancellationDaysText = cancellation_days.getText().toString().trim();

            String accommodationTypeString = accommodation_type.getSelectedItem().toString();
            AccommodationType accommodationType;

            if (accommodationTypeString == null) {
                Toast.makeText(getApplicationContext(), "Accommodation type is required!", Toast.LENGTH_SHORT).show();
                return;
            }else{
                accommodationType = AccommodationType.fromString(accommodationTypeString);
                if(accommodationType == null){
                    Toast.makeText(getApplicationContext(), "Something went wrong with accommodation type.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (street.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Street is required!", Toast.LENGTH_SHORT).show();
                return;
            } else if (city.isEmpty()) {
                Toast.makeText(getApplicationContext(), "City is required!", Toast.LENGTH_SHORT).show();
                return;
            } else if (country.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Country is required!", Toast.LENGTH_SHORT).show();
                return;
            } else if (name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Accommodation name is required!", Toast.LENGTH_SHORT).show();
                return;
            } else if(userId == null){
                Toast.makeText(getApplicationContext(), "Something went wrong with your id.", Toast.LENGTH_SHORT).show();
                return;
            } else if(description.isEmpty()){
                Toast.makeText(getApplicationContext(), "Description is required!", Toast.LENGTH_SHORT).show();
                return;
            }



            int minGuests;
            int maxGuests;
            int cancellationDays;
            try {
                minGuests = Integer.parseInt(minGuestsText);

                try {
                    maxGuests = Integer.parseInt(maxGuestsText);

                    try {
                        cancellationDays = Integer.parseInt(cancellationDaysText);

                        if (minGuests > maxGuests) {
                            Toast.makeText(getApplicationContext(), "Min guests should be less than or equal to max guests!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Enter a valid number for cancellation days!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Enter a valid number for max guests!", Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Enter a valid number for min guests!", Toast.LENGTH_SHORT).show();
                return;
            }


            Accommodation accommodation = new Accommodation();
            accommodation.setAmenities(amenitiesAdapter.getSelectedAmenities());
            accommodation.setName(name);
            accommodation.setId(0l);
            accommodation.setType(accommodationType);
            accommodation.setDescription(description);
            accommodation.setPhotos(getLoadedFileNames());
            accommodation.setDaysForCancellation(cancellationDays);
            accommodation.setMaxGuests(maxGuests);
            accommodation.setMinGuests(minGuests);
            accommodation.setOwnerId(userId);
            accommodation.setEnabled(false);
            accommodation.setPerNight(radio_button_per_night.isChecked());
            accommodation.setLocation(street + ',' + city + ',' + country);
            accommodation.setDates(new ArrayList<>());


            createAccommodation(accommodation);
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
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);

        uploadImagesLauncher.launch(intent);
    }

    private List<String> getLoadedFileNames() {
        List<String> ret = new ArrayList<>();
        for (Uri uri : fileAdapter.getUriList()) {
            File file = new File(getPathFromUri(uri));
            ret.add(file.getName());
        }
        return ret;
    }

    ActivityResultLauncher<Intent> uploadImagesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if(data != null){
                        ClipData clipData = data.getClipData();

                        if (clipData != null){
                            for(int i = 0; i<clipData.getItemCount();i++){
                                Uri selectedImageUri = clipData.getItemAt(i).getUri();
                                fileAdapter.addItem(selectedImageUri);
                            }
                        } else {
                            Uri selectedImageUri = data.getData();
                            fileAdapter.addItem(selectedImageUri);
                        }
                    }
                }
            }
    );

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

        for (Uri uri : fileAdapter.getUriList()) {
            File file = new File(getPathFromUri(uri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
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
                    Toast.makeText(getApplicationContext(), "Uploaded images.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to create images.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d("REZ","Image meesage failed to be received.");
            }
        });
    }
    private String getPathFromUri(Uri uri) {
        String path = "";

        if (uri == null) {
            return path;
        }

        try {
            // Check if the URI uses the content scheme
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Use ContentResolver to open an InputStream for the URI
                try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        path = cursor.getString(columnIndex);
                    }
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // For file scheme, directly get the path
                path = uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

}