package com.nathan630pm.nk_final_project;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nathan630pm.nk_final_project.adapter.ParkingAdapter;
import com.nathan630pm.nk_final_project.models.Parking;
import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.viewmodels.ParkingViewModel;
import com.nathan630pm.nk_final_project.viewmodels.UserViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class viewParkingFragment extends Fragment implements View.OnClickListener, OnParkingClickListener{

    private static final String TAG = "viewParkingFragment";
    private RecyclerView recyclerView;
    private LinearLayoutManager viewManager;
    private ParkingAdapter parkingAdapter;
    private ArrayList<Parking> parkingArray;
    private SwipeRefreshLayout swipeContainer;

    private ParkingViewModel parkingViewModel;
    private UserViewModel userViewModel;

    private User currUser;

    private Context context;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_view_parking, container, false);

        this.context = v.getContext();

        this.swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        this.parkingViewModel = ParkingViewModel.getInstance();
        this.userViewModel = UserViewModel.getInstance();

        this.parkingArray = new ArrayList<>();
        this.parkingAdapter = new ParkingAdapter(context, parkingArray, this);

        this.viewManager = new LinearLayoutManager(v.getContext());
        viewManager.setOrientation(LinearLayoutManager.VERTICAL);




        userViewModel.userObject.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currUser = user;
                Log.e(TAG, "user profile: " + user.toString());
                parkingViewModel.getAllParking(currUser.getEmail());
                parkingAdapter.notifyDataSetChanged();
            }
        });

        parkingViewModel.getAllParking("nathan630pm@outlook.com");

        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(this.viewManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(parkingAdapter);
        // lmao I literally spent an our trying to figure out why I had no adapter attached, and then realized I didn't attach one... *sigh*
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));


        this.parkingViewModel.getParkingRepository().parkingList.observe(getViewLifecycleOwner(), new Observer<List<Parking>>() {
            @Override
            public void onChanged(List<Parking> parkings) {
                if(parkings != null) {
                    Log.d(TAG, "DATA Changed: " + parkings.toString());
                    parkingArray.addAll(parkings);
                    parkingAdapter.notifyDataSetChanged();
                }
            }
        });

        this.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parkingArray.clear();
                Boolean result = parkingViewModel.getAllParking(currUser.getEmail());
                if(result){
                    swipeContainer.setRefreshing(false);
                }
            }
        });






        return v;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onParkingClickListener(Parking parking) {

    }
}