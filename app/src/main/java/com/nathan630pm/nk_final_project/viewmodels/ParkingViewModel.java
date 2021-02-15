package com.nathan630pm.nk_final_project.viewmodels;

import androidx.lifecycle.ViewModel;

import com.nathan630pm.nk_final_project.models.Parking;
import com.nathan630pm.nk_final_project.repositories.ParkingRepository;

public class ParkingViewModel extends ViewModel {

    private static final String TAG = "ParkingViewModel";
    private static final ParkingViewModel ourInstance = new ParkingViewModel();
    private final ParkingRepository parkingRepository = new ParkingRepository();

    public static ParkingViewModel getInstance() {
        return ourInstance;
    }

    private ParkingViewModel() {

    }

    public ParkingRepository getParkingRepository() {
        return parkingRepository;
    }

    public void addParking(String userEmail, Parking parking) {
        this.parkingRepository.addParking(userEmail, parking);
    }

    public boolean getAllParking(String userEmail) {
        Boolean result = this.parkingRepository.getAllParkingItems(userEmail);
        return result;
    }



}
