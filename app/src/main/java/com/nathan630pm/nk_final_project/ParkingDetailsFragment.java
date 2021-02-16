package com.nathan630pm.nk_final_project;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nathan630pm.nk_final_project.models.Parking;
import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.repositories.ParkingRepository;
import com.nathan630pm.nk_final_project.repositories.UserRepository;
import com.nathan630pm.nk_final_project.viewmodels.ParkingViewModel;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ParkingDetailsFragment extends Fragment {

    private static final String TAG = "ParkingDetailsFragment";
    private View v;
    private UserViewModel userViewModel;
    private ParkingViewModel parkingViewModel;
    private String itemID;

    private Button BViewMap;
    private User currUser;
    private Parking currParking;

    private Context context;


    private EditText ETBuildingCode, ETPlateNumber, ETDate, ETHoursSelect, ETParkingAddr, ETSuit;

    private ArrayList<String> hoursSelect = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.v = inflater.inflate(R.layout.fragment_parking_details, container, false);

        this.ETBuildingCode = v.findViewById(R.id.ETBuildingCode);
        this.ETPlateNumber = v.findViewById(R.id.ETPlateNumber);
        this.ETDate = v.findViewById(R.id.ETDate);
        this.ETHoursSelect = v.findViewById(R.id.ETHoursSelect);
        this.ETParkingAddr = v.findViewById(R.id.ETParkingAddr);
        this.ETSuit = v.findViewById(R.id.ETSuit);

        ETBuildingCode.setText("");
        ETPlateNumber.setText("");
        ETDate.setText("");
        ETHoursSelect.setText("");
        ETParkingAddr.setText("");
        ETSuit.setText("");

        this.hoursSelect.add("1-hour or less");
        this.hoursSelect.add("4-hour");
        this.hoursSelect.add("12-hour");
        this.hoursSelect.add("24-hour");

        this.userViewModel = UserViewModel.getInstance();
        this.parkingViewModel = ParkingViewModel.getInstance();

        this.context = v.getContext();

        this.itemID = getArguments().getString("itemID");

        userViewModel.getUserRepository().userObject.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currUser = user;
                parkingViewModel.getParkingRepository().getParkingByID(user.getEmail(), itemID);
            }
        });

        parkingViewModel.getParkingRepository().singleParking.observe(getViewLifecycleOwner(), new Observer<Parking>() {
            @Override
            public void onChanged(Parking parking) {
                currParking = parking;
                ETBuildingCode.setText(parking.getBuildingCode());
                ETPlateNumber.setText(parking.getCarPlateNumber());
                ETDate.setText(parking.getDate().toString());
                ETHoursSelect.setText(hoursSelect.get(parking.getHoursSelection()));
                ETParkingAddr.setText(parking.getParkingAddr());
                ETSuit.setText(parking.getSuitNo());
            }
        });



        this.BViewMap = (Button) v.findViewById(R.id.BViewMap);
        BViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: BViewMap pressed.");
                viewOnMap();
            }
        });

        return this.v;
    }

    public void viewOnMap() {
        String parkingid = itemID;
        Bundle viewOnMap = new Bundle();
        viewOnMap.putString("itemID", parkingid);
        Navigation.findNavController(this.v).navigate(R.id.action_parkingDetailsFragment_to_parkingViewOnMapsFragment, viewOnMap);
    }


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



    private void getDetails(String id) {

    }



}