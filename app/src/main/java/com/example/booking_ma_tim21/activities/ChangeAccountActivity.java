package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.UserDTO;
import com.example.booking_ma_tim21.model.enumeration.Role;
import com.example.booking_ma_tim21.retrofit.RetrofitService;
import com.example.booking_ma_tim21.retrofit.UserService;
import com.example.booking_ma_tim21.util.NavigationSetup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeAccountActivity extends AppCompatActivity {
    UserService service;
    UserDTO user = new UserDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);
        NavigationSetup.setupNavigation(this);

        RetrofitService retrofitService = new RetrofitService();
        service = retrofitService.getRetrofit().create(UserService.class);

        setUpInputFields();
        setUpSaveChangesButton();
        setUpDeleteButton();

    }

    public void setUpInputFields() {
        // Make a Retrofit API call to get the user information
        Call<UserDTO> call = service.getUser();
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    user = response.body();

                    EditText streetField = findViewById(R.id.street_field);
                    streetField.setText(user.getStreet());
                    setUpRequiredValidation(streetField, "Street");

                    EditText cityField = findViewById(R.id.city_field);
                    cityField.setText(user.getCity());
                    setUpRequiredValidation(cityField, "City");

                    EditText countryField = findViewById(R.id.country_field);
                    countryField.setText(user.getCountry());
                    setUpRequiredValidation(countryField, "Country");

                    EditText nameField = findViewById(R.id.name_field);
                    nameField.setText(user.getName());
                    setUpRequiredValidation(nameField, "Name");

                    EditText surnameField = findViewById(R.id.surname_field);
                    surnameField.setText(user.getSurname());
                    setUpRequiredValidation(surnameField, "Surname");

                    EditText phoneField = findViewById(R.id.phone_field);
                    phoneField.setText(user.getPhone());
                    setUpRequiredValidation(phoneField, "Phone");
                    setUpMinLengthValidation(phoneField, 6);

                    EditText emailField = findViewById(R.id.email_field);
                    emailField.setText(user.getEmail());
                    setUpRequiredValidation(emailField, "Email");
                    setUpEmailValidation();

                    EditText passwordField = findViewById(R.id.password_field);
                    setUpMinLengthValidation(passwordField, 5);

                    EditText repeatPasswordField = findViewById(R.id.repeat_password_field);
                    setUpMinLengthValidation(repeatPasswordField, 5);


                } else {
                    Toast.makeText(ChangeAccountActivity.this, "Failed to fetch user. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(ChangeAccountActivity.this, "Failed to fetch user. Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpSaveChangesButton() {
        Button saveChangesBtn = findViewById(R.id.button_save_account_changes);
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isInputValid()) {
                    Toast.makeText(ChangeAccountActivity.this, "Not all input fields are valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                String street = ((EditText) findViewById(R.id.street_field)).getText().toString();
                String city = ((EditText) findViewById(R.id.city_field)).getText().toString();
                String country = ((EditText) findViewById(R.id.country_field)).getText().toString();
                String name = ((EditText) findViewById(R.id.name_field)).getText().toString();
                String surname = ((EditText) findViewById(R.id.surname_field)).getText().toString();
                String phone = ((EditText) findViewById(R.id.phone_field)).getText().toString();
                String email = ((EditText) findViewById(R.id.email_field)).getText().toString();
                String password = ((EditText) findViewById(R.id.password_field)).getText().toString();
                String repeatPassword = ((EditText) findViewById(R.id.repeat_password_field)).getText().toString();

                if (!password.equals(repeatPassword)) {
                    Toast.makeText(ChangeAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.setStreet(street);
                user.setCity(city);
                user.setCountry(country);
                user.setName(name);
                user.setSurname(surname);
                user.setPhone(phone);
                user.setEmail(email);
                if (!password.isEmpty()) {
                    user.setPassword(password);
                }

                Call<UserDTO> call = service.updateUser(user);
                call.enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ChangeAccountActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ChangeAccountActivity.this, "Failed to update user. Email is already in use", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {
                        Toast.makeText(ChangeAccountActivity.this, "Failed to update user. Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setUpDeleteButton() {
        Button deleteAccountBtn = findViewById(R.id.button_delete_account);

        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofit2.Call<Void> call = service.deleteUser();
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(ChangeAccountActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ChangeAccountActivity.this, "Failed to delete user. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ChangeAccountActivity.this, "Failed to delete user. Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setUpRequiredValidation(EditText field, String fieldName) {
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (field.getText().toString().trim().equals("")) {
                    field.setError(fieldName + " is required");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setUpEmailValidation() {
        EditText emailField = findViewById(R.id.email_field);
        String email = emailField.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.matches(emailPattern)) {
                    emailField.setError("Please provide a valid email");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setUpMinLengthValidation(EditText editText, int minLength) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.length() < minLength) {
                    editText.setError("Minimum length is " + minLength + " characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isInputValid() {
        List<EditText> fields = new ArrayList<>();
        fields.add(findViewById(R.id.street_field));
        fields.add(findViewById(R.id.city_field));
        fields.add(findViewById(R.id.country_field));
        fields.add(findViewById(R.id.name_field));
        fields.add(findViewById(R.id.surname_field));
        fields.add(findViewById(R.id.phone_field));
        fields.add(findViewById(R.id.email_field));
        fields.add(findViewById(R.id.password_field));
        fields.add(findViewById(R.id.repeat_password_field));

        for (EditText field : fields) {
            if (field.getError() != null) return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
}