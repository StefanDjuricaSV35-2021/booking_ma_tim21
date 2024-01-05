package com.example.booking_ma_tim21.fragments;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.booking_ma_tim21.R;
import com.example.booking_ma_tim21.activities.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    Geocoder geocoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        this.geocoder=new Geocoder(view.getContext());


        // Add the Google Maps Lite fragment dynamically
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map_fl, mapFragment);
        transaction.commit();

        // Set up the map
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        Bundle b=getArguments();
        String location=b.getString("location");

        // Load location based on its name (geocoding)
        new GeocodeAsyncTask().execute(location);
    }

    private void loadLocationByName(String locationName) {

        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);

            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(locationName));

                // Move camera to the geocoded location
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GeocodeAsyncTask extends AsyncTask<String, Void, LatLng> {

        @Override
        protected LatLng doInBackground(String... params) {
            String locationName = params[0];
            Geocoder geocoder = new Geocoder(getActivity());

            try {
                // Geocode the location name to get coordinates
                List<Address> addresses = geocoder.getFromLocationName(locationName, 1);

                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    return new LatLng(address.getLatitude(), address.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(LatLng location) {
            super.onPostExecute(location);

            if (location != null) {
                // Add a marker to the map
                googleMap.addMarker(new MarkerOptions()
                        .position(location));

                // Move the camera to the marker position
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
            }
        }
    }
}
