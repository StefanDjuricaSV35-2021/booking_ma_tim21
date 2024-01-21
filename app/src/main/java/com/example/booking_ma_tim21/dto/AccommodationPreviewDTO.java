package com.example.booking_ma_tim21.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AccommodationPreviewDTO implements Parcelable {
    private Long id;
    private String name;
    private String location;
    private String image;
    private Double price;

    public AccommodationPreviewDTO(Long id, String name, String location, String image,Double price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.image = image;
        this.price=price;
    }

    protected AccommodationPreviewDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        location = in.readString();
        image = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
    }

    public static final Creator<AccommodationPreviewDTO> CREATOR = new Creator<AccommodationPreviewDTO>() {
        @Override
        public AccommodationPreviewDTO createFromParcel(Parcel in) {
            return new AccommodationPreviewDTO(in);
        }

        @Override
        public AccommodationPreviewDTO[] newArray(int size) {
            return new AccommodationPreviewDTO[size];
        }
    };

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    public Double getPrice() { return price; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(image);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
    }
}
