package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.util.NavigationSetup;

public class RegisterActivity extends AppCompatActivity {

    private EditText email_field;
    private EditText password_field;
    private EditText phone_field;
    private EditText street_field;
    private EditText city_field;
    private EditText country_field;
    private EditText name_field;
    private EditText surname_field;
    private EditText repeat_password_field;
    private RadioGroup role_group;
    private RadioButton radio_button_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        phone_field = findViewById(R.id.phone_field);
        street_field = findViewById(R.id.street_field);
        city_field = findViewById(R.id.city_field);
        country_field = findViewById(R.id.country_field);
        name_field = findViewById(R.id.name_field);
        surname_field = findViewById(R.id.surname_field);
        repeat_password_field = findViewById(R.id.repeat_password_field);
        role_group = findViewById(R.id.role_group);
        radio_button_user = findViewById(R.id.radio_button_user);

        NavigationSetup.setupNavigation(this);

        Button RegisterBtn = findViewById(R.id.button_register);
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_field.getText().toString();
                String password = password_field.getText().toString();
                String phone = phone_field.getText().toString();
                String street = street_field.getText().toString();
                String city = city_field.getText().toString();
                String country = country_field.getText().toString();
                String name = name_field.getText().toString();
                String surname = surname_field.getText().toString();
                String repeat_password = repeat_password_field.getText().toString();

                if(email.isEmpty() || password.isEmpty() || phone.isEmpty() || street.isEmpty() || city.isEmpty() || country.isEmpty() || name.isEmpty() || surname.isEmpty() || repeat_password.isEmpty() || role_group.getCheckedRadioButtonId() != -1){
                    Toast.makeText(RegisterActivity.this, "Fill out all the fields to register!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        email_field.setText("");
        password_field.setText("");
        phone_field.setText("");
        street_field.setText("");
        city_field.setText("");
        country_field.setText("");
        name_field.setText("");
        surname_field.setText("");
        repeat_password_field.setText("");
        radio_button_user.setChecked(true);

        Log.d("log", "onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
}