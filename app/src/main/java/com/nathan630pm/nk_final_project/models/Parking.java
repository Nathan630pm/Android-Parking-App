package com.nathan630pm.nk_final_project.models;

import java.util.Date;

public class Parking {
    String buildingCode;
    String carPlateNumber;
    Date date;
    String email;
    String hoursSelection;
    String parkingAddr;
    Double parkingLat;
    Double parkingLon;
    int suitNo;

    public Parking(String buildingCode, String carPlateNumber, Date date, String email, String hoursSelection, String parkingAddr, Double parkingLat, Double parkingLon, int suitNo) {
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

    public String getHoursSelection() {
        return hoursSelection;
    }

    public void setHoursSelection(String hoursSelection) {
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

    public int getSuitNo() {
        return suitNo;
    }

    public void setSuitNo(int suitNo) {
        this.suitNo = suitNo;
    }
}
