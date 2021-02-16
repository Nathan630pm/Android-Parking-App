package com.nathan630pm.nk_final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.graphics.Camera;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nathan630pm.nk_final_project.managers.LocationManager;
import com.nathan630pm.nk_final_project.models.Parking;
import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.viewmodels.ParkingViewModel;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParkingViewOnMapsFragment extends Fragment {

    private static final String TAG = "ParkingViewOnMapsFragment";
    private View v;
    private String itemID;

    private UserViewModel userViewModel;
    private ParkingViewModel parkingViewModel;

    private User currUser;
    private Parking currParking;

    private final Float DEFAULT_ZOOM = 15.0f;

    private LatLng currentLocation;

    private GoogleMap GMap;
    private LocationManager locationManager;
    private LocationCallback locationCallback;
    private Marker marker;
    private int testUpdateNo = 1;

    private LatLng sydney = new LatLng(-34, 151);

    private Geocoder geocoder;

    private List<Address> addresses;
    private String address;



    private ArrayList<LatLng> markers = new ArrayList<LatLng>();


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {


            GMap = googleMap;

            marker = GMap.addMarker(new MarkerOptions().position(sydney).title("Loading..."));







//
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG,"Back Button Pressed");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG,"home on backpressed");
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        v = inflater.inflate(R.layout.fragment_parking_view_on_maps, container, false);

        this.geocoder = new Geocoder(v.getContext(), Locale.getDefault());
        this.address = "Current Location";

        this.userViewModel = UserViewModel.getInstance();
        this.parkingViewModel = ParkingViewModel.getInstance();

        this.locationManager = LocationManager.getInstance();
        this.locationManager.checkPermissions(v.getContext());

        if (this.locationManager.locationPermissionGranted){

            locationCallback = new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location loc : locationResult.getLocations()) {

                        currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                        GMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
                        marker.remove();


                        try {
                            addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                            address = "Current Location: (" + addresses.get(0).getAddressLine(0) + ")";

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        marker = GMap.addMarker(new MarkerOptions().position(currentLocation).title(address));

                        LatLng parkingAddr = new LatLng(currParking.getParkingLat(), currParking.getParkingLon());

                        GMap.addMarker(new MarkerOptions().position(parkingAddr).title(currParking.getParkingAddr()));

                        markers.add(currentLocation);
                        markers.add(parkingAddr);

                        Polyline line = GMap.addPolyline(new PolylineOptions()
                        .add(currentLocation, parkingAddr)
                        .width(5)
                        .color(Color.BLUE));

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng marker : markers) {
                            builder.include(marker);
                        }
                        LatLngBounds bounds = builder.build();

                        int padding = 200;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);


//            googleMap.moveCamera((CameraUpdateFactory.newLatLng(parkingAddr)));
                        GMap.animateCamera(cu);
                        GMap.setPadding(5, 5, 5, 5);
                        locationManager.stopLocationUpdates(v.getContext(), locationCallback);



                        Log.e(TAG, "Lat : " + loc.getLatitude() + "\nLng : " + loc.getLongitude());
                        Log.e(TAG, "Number of locations" + locationResult.getLocations().size());
                    }
                }
            };
            this.locationManager.requestLocationUpdates(v.getContext(), locationCallback);
        }

        this.currUser = userViewModel.getUserRepository().getUserObject();
        parkingViewModel.getParkingRepository().getParkingByID(currUser.getEmail(), itemID);

        parkingViewModel.getParkingRepository().singleParking.observe(getViewLifecycleOwner(), new Observer<Parking>() {
            @Override
            public void onChanged(Parking parking) {
                currParking = parking;
            }
        });

        this.currParking = parkingViewModel.getParkingRepository().singleParking.getValue();



        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}