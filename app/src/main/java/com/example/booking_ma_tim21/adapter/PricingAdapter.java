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
import com.example.booking_ma_tim21.model.enumeration.Amenity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class PricingAdapter extends RecyclerView.Adapter<PricingAdapter.ViewHolder>{

    private List<AccommodationPricing> pricingList;

    public PricingAdapter(List<AccommodationPricing> pricingList) {
        this.pricingList = pricingList;
    }

    @NonNull
    @Override
    public PricingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeslot_row, parent, false);
        return new PricingAdapter.ViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull PricingAdapter.ViewHolder holder, int position) {
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

    public void addItem(AccommodationPricing newItem) {
        pricingList.add(newItem);
        notifyItemInserted(pricingList.size() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < pricingList.size()) {
            pricingList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pricingList.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textStartDate;
        TextView textEndDate;
        TextView textPrice;
        Button btnRemove;
        PricingAdapter adapter;
        public ViewHolder(@NonNull View itemView, PricingAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            textStartDate = itemView.findViewById(R.id.textStartDate);
            textEndDate = itemView.findViewById(R.id.textEndDate);
            textPrice = itemView.findViewById(R.id.textPrice);
            btnRemove = itemView.findViewById(R.id.btnRemove);

            btnRemove.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    this.adapter.removeItem(position);
                }
            });
        }
    }
}
