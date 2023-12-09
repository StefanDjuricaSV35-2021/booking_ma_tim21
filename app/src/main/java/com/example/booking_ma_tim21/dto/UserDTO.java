package com.example.booking_ma_tim21.dto;

import com.example.booking_ma_tim21.model.enumeration.UserType;

public class UserDTO {
    private Long Id;

    private UserType type;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String country;
    private String city;
    private String street;
    private String phone;

    public UserDTO(Long id, UserType type, String email, String password, String name, String surname, String country, String city, String street, String phone) {
        this.Id = id;
        this.type = type;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.city = city;
        this.street = street;
        this.phone = phone;

    }

    public Long getId() {
        return Id;
    }

    public UserType getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getPhone() {
        return phone;
    }
}
