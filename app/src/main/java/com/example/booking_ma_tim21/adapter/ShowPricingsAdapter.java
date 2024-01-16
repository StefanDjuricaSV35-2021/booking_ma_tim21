package com.example.booking_ma_tim21.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.model.AccommodationPricing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShowPricingsAdapter extends RecyclerView.Adapter<ShowPricingsAdapter.ViewHolderPricings> {
    private List<AccommodationPricing> pricingList;

    public ShowPricingsAdapter(List<AccommodationPricing> pricingList) {
        this.pricingList = pricingList;
    }

    @NonNull
    @Override
    public ShowPricingsAdapter.ViewHolderPricings onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pricing_row, parent, false);
        return new ShowPricingsAdapter.ViewHolderPricings(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowPricingsAdapter.ViewHolderPricings holder, int position) {
        AccommodationPricing pricing = pricingList.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        holder.textStartDate.setText(dateFormat.format(new Date(pricing.getTimeSlot().getStartDate())));
        holder.textEndDate.setText(dateFormat.format(new Date(pricing.getTimeSlot().getEndDate())));
        holder.textPrice.setText(String.valueOf(pricing.getPrice()));
    }

    @Override
    public int getItemCount() {
        return pricingList.size();
    }
    public static class ViewHolderPricings extends RecyclerView.ViewHolder {
        TextView textStartDate;
        TextView textEndDate;
        TextView textPrice;
        ShowPricingsAdapter adapter;
        public ViewHolderPricings(@NonNull View itemView, ShowPricingsAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            textStartDate = itemView.findViewById(R.id.PricingtextStartDate);
            textEndDate = itemView.findViewById(R.id.PricingtextEndDate);
            textPrice = itemView.findViewById(R.id.PricingtextPrice);
        }
    }

    public List<AccommodationPricing> getPricingList() {
        return pricingList;
    }
}
