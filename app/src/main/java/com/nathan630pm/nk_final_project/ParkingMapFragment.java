package com.nathan630pm.nk_final_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

//Created By: Nathan Kennedy, Student ID: 101333351


public class ParkingMapFragment extends Fragment {

    private static final String TAG = "ParkingMapFragment";
    private View v;
    private String itemID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        this.v = inflater.inflate(R.layout.fragment_parking_map, container, false);

        this.itemID = getArguments().getString("itemID");
        Log.d(TAG, "onCreateView: ITEM ID: "  + itemID);


        return v;
    }
}