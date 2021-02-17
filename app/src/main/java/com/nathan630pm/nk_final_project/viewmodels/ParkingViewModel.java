package com.nathan630pm.nk_final_project.viewmodels;

import androidx.lifecycle.ViewModel;

import com.nathan630pm.nk_final_project.models.Parking;
import com.nathan630pm.nk_final_project.models.User;
import com.nathan630pm.nk_final_project.repositories.ParkingRepository;

//Created By: Nathan Kennedy, Student ID: 101333351

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

    public boolean addParking(String userEmail, Parking parking) {
        return this.parkingRepository.addParking(userEmail, parking);
    }

    public boolean getAllParking(String userEmail) {
        Boolean result = this.parkingRepository.getAllParkingItems(userEmail);
        return result;
    }

    public void deleteParking(User user, Parking parking) {parkingRepository.deleteParking(user, parking);}



}
