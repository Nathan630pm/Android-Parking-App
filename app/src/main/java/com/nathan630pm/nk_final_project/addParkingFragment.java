package com.nathan630pm.nk_final_project;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nathan630pm.nk_final_project.managers.LocationManager;
import com.nathan630pm.nk_final_project.models.Parking;
import com.nathan630pm.nk_final_project.repositories.ParkingRepository;
import com.nathan630pm.nk_final_project.repositories.UserRepository;
import com.nathan630pm.nk_final_project.viewmodels.ParkingViewModel;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

public class addParkingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "addParkingFragment";
    private TextView TVBuildingCodeError, TVPlateNoError, TVSuitError, TVAddressError, TVDateError;
    private EditText ETBuildingCode, ETPlateNo, ETSuit, ETAddress;
    private CheckBox CBUseLocation;
    private DatePicker DPDatePicker;
    private Spinner SHours;
    private Button BAddParking;

    private int hoursSelect = 0;
    private ArrayList<String> hoursSelectArray = new ArrayList<String>();


    private Context context;

    private Boolean locationIsChecked = false;

    private LatLng currentLocation;
    private LocationManager locationManager;
    private LocationCallback locationCallback;
    private Geocoder geocoder;

    private List<Address> addresses;
    private String address;

    private List<Address> calculatedAddresses;
    private LatLng calculatedCoords;

    private Boolean formOk = false;


    private UserViewModel userViewModel;
    private ParkingViewModel parkingViewModel;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.userViewModel = UserViewModel.getInstance();
        this.parkingViewModel = ParkingViewModel.getInstance();

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_add_parking, container, false);


        this.geocoder = new Geocoder(v.getContext(), Locale.getDefault());
        this.address = "Loading...";


        this.locationManager = LocationManager.getInstance();
        this.locationManager.checkPermissions(v.getContext());




        this.context = v.getContext();

        this.hoursSelectArray.add("1 Hour or less");
        this.hoursSelectArray.add("4-hour");
        this.hoursSelectArray.add("12-hour");
        this.hoursSelectArray.add("24-hour");


        TVBuildingCodeError = v.findViewById(R.id.TVBuildingCodeError);
        TVPlateNoError = v.findViewById(R.id.TVPlateNoError);
        TVSuitError = v.findViewById(R.id.TVSuitError);
        TVAddressError = v.findViewById(R.id.TVAddressError);
        TVDateError = v.findViewById(R.id.TVDateError);

        ETBuildingCode = v.findViewById(R.id.ETBuildingCode);
        ETPlateNo = v.findViewById(R.id.ETPlateNo);
        ETPlateNo.setText(userViewModel.getUserRepository().userObject.getValue().getCarPlateNumber());
        ETSuit = v.findViewById(R.id.ETSuit);
        ETAddress = v.findViewById(R.id.ETAddress);

        CBUseLocation = v.findViewById(R.id.CBUseLocation);
        CBUseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: checkbox clicked");
                if(locationIsChecked){
                    locationIsChecked = false;

                    ETAddress.setText("");
                    ETAddress.setEnabled(true);

                }else {
                    TVAddressError.setText("");
                    TVAddressError.setVisibility(GONE);
                    locationIsChecked = true;
                    getUserLocation();
                    ETAddress.setEnabled(false);
                }

            }
        });

        DPDatePicker = v.findViewById(R.id.DPDatePicker);

        SHours = v.findViewById(R.id.SHours);
        SHours.setOnItemSelectedListener(this);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, hoursSelectArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SHours.setAdapter(aa);

        BAddParking = v.findViewById(R.id.BAddParking);

        BAddParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TVBuildingCodeError.setText("");
                TVBuildingCodeError.setVisibility(GONE);
                TVPlateNoError.setText("");
                TVPlateNoError.setVisibility(GONE);
                TVSuitError.setText("");
                TVSuitError.setVisibility(GONE);
                TVAddressError.setText("");
                TVAddressError.setVisibility(GONE);
                TVDateError.setText("");
                TVDateError.setVisibility(GONE);

                if(ETBuildingCode.getText().toString().length() != 5) {
                    TVBuildingCodeError.setVisibility(View.VISIBLE);
                    TVBuildingCodeError.setText("Building codes must be exactly 5 characters!");
                    formOk = false;
                }
                else {
                    formOk = true;
                }

                if(ETPlateNo.getText().toString().length() <2 || ETPlateNo.getText().toString().length() > 8) {
                    TVPlateNoError.setVisibility(View.VISIBLE);
                    TVPlateNoError.setText("Plate Numbers must be 2-8 characters!");
                    formOk = false;
                }
                else {
                    formOk = true;
                }

                if(ETSuit.getText().toString().length() <2 || ETSuit.getText().toString().length() > 5) {
                    TVSuitError.setVisibility(View.VISIBLE);
                    TVSuitError.setText("Suit Numbers must be 2-5 characters!");
                    formOk = false;
                }
                else {
                    formOk = true;
                }

                if(!locationIsChecked && !ETAddress.getText().toString().equals("")) {
                    findLocation();
                }
                else if(ETAddress.getText().toString().equals("")){
                    TVAddressError.setVisibility(View.VISIBLE);
                    TVAddressError.setText("Please enter an address.");
                    formOk = false;
                }
                else {
                    formOk = true;
                }

                if(formOk) {
                    Toast.makeText(context, "Adding Parking. Please wait...", Toast.LENGTH_SHORT).show();
                    addParkingToDatabase();
                }
            }
        });


        if (this.locationManager.locationPermissionGranted){

            locationCallback = new LocationCallback() {

                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location loc : locationResult.getLocations()) {

                        currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

                        Log.d(TAG, "onLocationResult: " + currentLocation.toString());

                        if(locationIsChecked) {
                            getUserLocation();
                        }




                        Log.e(TAG, "Lat : " + loc.getLatitude() + "\nLng : " + loc.getLongitude());
                        Log.e(TAG, "Number of locations" + locationResult.getLocations().size());
                    }
                }
            };
            this.locationManager.requestLocationUpdates(context, locationCallback);
        }






        return v;
    }

    private void addParkingToDatabase() {
        LatLng coords;
        if(locationIsChecked) {
            coords = new LatLng(currentLocation.latitude, currentLocation.longitude);
        }else {
            coords = calculatedCoords;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(DPDatePicker.getYear(), DPDatePicker.getMonth(), DPDatePicker.getDayOfMonth());

        Parking parkingToAdd = new Parking();
        parkingToAdd.setBuildingCode(ETBuildingCode.getText().toString());
        parkingToAdd.setCarPlateNumber(ETPlateNo.getText().toString());
        parkingToAdd.setDate(calendar.getTime());
        parkingToAdd.setEmail(userViewModel.getUserObject().getEmail());
        parkingToAdd.setHoursSelection(hoursSelect);
        parkingToAdd.setParkingAddr(ETAddress.getText().toString());
        parkingToAdd.setParkingLat(coords.latitude);
        parkingToAdd.setParkingLon(coords.longitude);
        parkingToAdd.setSuitNo(ETSuit.getText().toString());

        Log.d(TAG, "addParkingToDatabase: GENERATED PARKING: " + parkingToAdd);

        Boolean result = parkingViewModel.addParking(userViewModel.getUserObject().getEmail(), parkingToAdd);

        if(result) {
            Log.d(TAG, "Successfully added Parking to Database!");
            Toast.makeText(context, "Successfully added Parking to Database!", Toast.LENGTH_LONG).show();
        }
        if(!result) {
            Log.e(TAG, "Something went wrong, please try again in a moment.");
            Toast.makeText(context, "Something went wrong, please try again in a moment.", Toast.LENGTH_LONG).show();
        }


    }


    private void findLocation(){

        try {
            calculatedAddresses = geocoder.getFromLocationName(ETAddress.getText().toString(), 1);
            if(calculatedAddresses.size() > 0) {
                calculatedCoords = new LatLng(calculatedAddresses.get(0).getLatitude(), calculatedAddresses.get(0).getLongitude());
            }
            else {
                TVAddressError.setText("Could not find coords matching that address");
                TVAddressError.setVisibility(View.VISIBLE);
                formOk = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getUserLocation() {


        try {
            if(currentLocation != null) {
                addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                address = addresses.get(0).getAddressLine(0);
            }
            if(locationIsChecked) {
                ETAddress.setText(address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        hoursSelect = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}