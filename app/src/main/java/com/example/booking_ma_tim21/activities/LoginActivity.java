package com.example.booking_ma_tim21.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.util.NavigationSetup;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NavigationSetup.setupNavigation(this);

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        Button LoginBtn = findViewById(R.id.button_login);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String emailValue = emailField.getText().toString();
                String passwordValue = passwordField.getText().toString();

                if(emailValue.isEmpty() || passwordValue.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fill out all the fields to login!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button RegisterBtn = findViewById(R.id.button_register);
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        emailField.setText("");
        passwordField.setText("");
        Log.d("log", "onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        NavigationSetup.closeDrawer(findViewById(R.id.drawerLayout));
    }
}