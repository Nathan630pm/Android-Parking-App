package com.nathan630pm.nk_final_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nathan630pm.nk_final_project.adapter.ParkingAdapter;
import com.nathan630pm.nk_final_project.models.Parking;
import com.nathan630pm.nk_final_project.viewmodels.ParkingViewModel;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class viewParkingFragment extends Fragment {

    private static final String TAG = "viewParkingFragment";
    private RecyclerView recyclerView;
    private LinearLayoutManager viewManager;
    private ParkingAdapter parkingAdapter;
    private ArrayList<Parking> parkingArray;
    private SwipeRefreshLayout swipeContainer;

    private ParkingViewModel parkingViewModel;
    private UserViewModel userViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_view_parking, container, false);

        return v;
    }
}