package com.example.carpoolingapp;

public class Paymeant {
    public String drive;
    public int pricings;
    public String passenger;

    public Paymeant(String drive,  int pricings) {
        this.drive = drive;
        this.pricings = pricings;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public int getPricings() {
        return pricings;
    }

    public void setPricings(int pricings) {
        this.pricings = pricings;
    }
}
