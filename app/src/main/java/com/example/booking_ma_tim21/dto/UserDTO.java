package com.example.booking_ma_tim21.dto;

import com.example.booking_ma_tim21.model.enumeration.Role;

public class UserDTO {
    private Long id;

    private Role role;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String country;
    private String city;
    private String street;
    private String phone;
    private boolean enabled;
    private boolean blocked;

    public UserDTO() {}
    public UserDTO(Long id, Role role, String email, String password, String name, String surname, String country, String city, String street, String phone, boolean enabled, boolean blocked) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.city = city;
        this.street = street;
        this.phone = phone;
        this.enabled = enabled;
        this.blocked = blocked;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
