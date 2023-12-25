package com.example.booking_ma_tim21.adapter.account;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.dto.UserDTO;

public class AccountViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView surname;
    private TextView street;
    private TextView city;
    private TextView country;
    private TextView phone;
    private TextView email;
    private TextView role;
    private TextView password;

    public AccountViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.account_name);
        surname = itemView.findViewById(R.id.account_surname);
        street = itemView.findViewById(R.id.account_street);
        city = itemView.findViewById(R.id.account_city);
        country = itemView.findViewById(R.id.account_country);
        phone = itemView.findViewById(R.id.account_phone);
        email = itemView.findViewById(R.id.account_email);
        role = itemView.findViewById(R.id.account_role);
    }

    public void bind(UserDTO user) {
        name.setText(user.getName());
        surname.setText(user.getSurname());
        street.setText(user.getStreet());
        city.setText(user.getCity());
        country.setText(user.getCountry());
        phone.setText(user.getPhone());
        email.setText(user.getEmail());
        role.setText(String.valueOf(user.getRole().toString()));
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getSurname() {
        return surname;
    }

    public void setSurname(TextView surname) {
        this.surname = surname;
    }

    public TextView getStreet() {
        return street;
    }

    public void setStreet(TextView street) {
        this.street = street;
    }

    public TextView getCity() {
        return city;
    }

    public void setCity(TextView city) {
        this.city = city;
    }

    public TextView getCountry() {
        return country;
    }

    public void setCountry(TextView country) {
        this.country = country;
    }

    public TextView getPhone() {
        return phone;
    }

    public void setPhone(TextView phone) {
        this.phone = phone;
    }

    public TextView getEmail() {
        return email;
    }

    public void setEmail(TextView email) {
        this.email = email;
    }

    public TextView getRole() {
        return role;
    }

    public void setRole(TextView role) {
        this.role = role;
    }

    public TextView getPassword() {
        return password;
    }

    public void setPassword(TextView password) {
        this.password = password;
    }
}
