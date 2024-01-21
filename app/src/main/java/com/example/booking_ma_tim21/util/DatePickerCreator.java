package com.example.booking_ma_tim21.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.MainActivity;
import com.example.booking_ma_tim21.activities.SearchActivity;
import com.example.booking_ma_tim21.fragments.SearchFragment;
import com.example.booking_ma_tim21.model.TimeSlot;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.DayViewDecorator;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DatePickerCreator {


    public static MaterialDatePicker getDatePicker(List<TimeSlot> ts){
        MaterialDatePicker<Pair<Long,Long>> picker;

        if(ts==null){
            picker=MaterialDatePicker.Builder.dateRangePicker()
                    .setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen)
                    .setSelection(new Pair(null,null))
                    .build();
        }else {
            RangeDateValidator validator=new RangeDateValidator(ts);

            picker=MaterialDatePicker.Builder.dateRangePicker()
                    .setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen)
                    .setSelection(new Pair(null, null))
                    .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(validator).build())
                    .build();
        }

        picker.addOnNegativeButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                picker.dismiss();
            }
        });

        return picker;

    }



}

 class RangeDateValidator implements CalendarConstraints.DateValidator {
     private List<TimeSlot> ts;

     public RangeDateValidator(Parcel p) {
     }

     public RangeDateValidator(List<TimeSlot> ts) {
         this.ts=ts;
     }
     public static final Creator<RangeDateValidator> CREATOR = new Creator<RangeDateValidator>() {
         @Override
         public RangeDateValidator createFromParcel(Parcel in) {
             return new RangeDateValidator(in);
         }

         @Override
         public RangeDateValidator[] newArray(int size) {
             return new RangeDateValidator[size];
         }
     };


     @Override
    public boolean isValid(long date) {

         Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND, 0);

         if(date<calendar.getTimeInMillis()){
             return false;
         }

         calendar.setTimeInMillis(date);
         calendar.set(Calendar.HOUR_OF_DAY, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND, 0);

         Long unix=calendar.getTimeInMillis()/1000;

         for(TimeSlot ts:this.ts){
             if(unix>=ts.getStartDate()&&unix<ts.getEndDate()){
                 return true;
             }
         }

         return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}


class BackgroundHighlightDecorator extends DayViewDecorator {

    private List<TimeSlot> myDates;

    private ColorStateList backgroundHighlightColor;

    BackgroundHighlightDecorator(Parcel p){
    }

    public BackgroundHighlightDecorator(List<TimeSlot> ts) {

        myDates=ts;
    }


    public static final Creator<BackgroundHighlightDecorator> CREATOR = new Creator<BackgroundHighlightDecorator>() {
        @Override
        public BackgroundHighlightDecorator createFromParcel(Parcel in) {
            return new BackgroundHighlightDecorator(in);
        }

        @Override
        public BackgroundHighlightDecorator[] newArray(int size) {
            return new BackgroundHighlightDecorator[size];
        }
    };


    @Override
    public void initialize(Context context) {

        int highlightColor = Color.BLUE;
        backgroundHighlightColor = ColorStateList.valueOf(highlightColor);

    }

    @Override
    public ColorStateList getBackgroundColor(Context context, int year, int month, int day, boolean valid, boolean selected) {
        return valid && !selected && shouldShowHighlight(year, month, day)
                ? backgroundHighlightColor
                : null;
    }

    private boolean shouldShowHighlight(int year, int month, int day) {

         Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        // Note: Calendar.MONTH is zero-based, so you need to subtract 1 from the provided month
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Long unix=calendar.getTimeInMillis()/1000;

        for(TimeSlot ts:myDates){
            if(unix>=ts.getStartDate()&&unix<ts.getEndDate()){
                return true;
            }
        }

        return false;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }

    /** CREATOR code goes here **/
}
