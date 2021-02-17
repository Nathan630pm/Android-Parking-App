package com.nathan630pm.nk_final_project.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;

//Created By: Nathan Kennedy, Student ID: 101333351

@IgnoreExtraProperties
public class Parking {
    @Exclude String id;
    String buildingCode;
    String carPlateNumber;
    Date date;
    String email;
    int hoursSelection;
    String parkingAddr;
    Double parkingLat;
    Double parkingLon;
    String suitNo;

    public Parking(String id, String buildingCode, String carPlateNumber, Date date, String email, int hoursSelection, String parkingAddr, Double parkingLat, Double parkingLon, String suitNo) {
        this.id = id;
        this.buildingCode = buildingCode;
        this.carPlateNumber = carPlateNumber;
        this.date = date;
        this.email = email;
        this.hoursSelection = hoursSelection;
        this.parkingAddr = parkingAddr;
        this.parkingLat = parkingLat;
        this.parkingLon = parkingLon;
        this.suitNo = suitNo;
    }

    public Parking() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHoursSelection() {
        return hoursSelection;
    }

    public void setHoursSelection(int hoursSelection) {
        this.hoursSelection = hoursSelection;
    }

    public String getParkingAddr() {
        return parkingAddr;
    }

    public void setParkingAddr(String parkingAddr) {
        this.parkingAddr = parkingAddr;
    }

    public Double getParkingLat() {
        return parkingLat;
    }

    public void setParkingLat(Double parkingLat) {
        this.parkingLat = parkingLat;
    }

    public Double getParkingLon() {
        return parkingLon;
    }

    public void setParkingLon(Double parkingLon) {
        this.parkingLon = parkingLon;
    }

    public String getSuitNo() {
        return suitNo;
    }

    public void setSuitNo(String suitNo) {
        this.suitNo = suitNo;
    }
}
