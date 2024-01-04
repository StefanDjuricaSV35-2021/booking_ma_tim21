package com.example.booking_ma_tim21.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    private final List<String> sliderImageSrc;

    public ImageAdapter(Context context, List<String> images) {
        this.mContext = context;
        sliderImageSrc=images;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ImageView) object);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load("http://10.0.2.2:8080/images/"+ sliderImageSrc.get(position)).into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return sliderImageSrc.size();
    }
}

