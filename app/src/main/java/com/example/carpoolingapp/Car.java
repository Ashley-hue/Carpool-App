package com.example.carpoolingapp;

public class Car {
    String email;
    String reg;
    String carname;
    int modelYear;
    byte[] image;

    public Car(String email, String reg, String carname, int modelYear, byte[] image) {
        this.email = email;
        this.reg = reg;
        this.carname = carname;
        this.modelYear = modelYear;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public String getReg() {
        return reg;
    }

    public String getCarname() {
        return carname;
    }

    public int getModelYear() {
        return modelYear;
    }

    public byte[] getImage() {
        return image;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
