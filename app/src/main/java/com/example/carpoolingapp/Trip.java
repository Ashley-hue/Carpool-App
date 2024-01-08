package com.example.carpoolingapp;

import java.io.Serializable;

public class Trip implements Serializable {

    public String source;
    public String destination;
    public String dating;
    public String timing;
    public String carRegNo;
    public int ratings;
    public int seatings;
    public String merlin;
    public String phoon;
    public String payment;

    public Trip(String source, String destination, String dating, String timing, String carRegNo, int ratings, int seatings) {
        this.source = source;
        this.destination = destination;
        this.dating = dating;
        this.timing = timing;
        this.carRegNo = carRegNo;
        this.ratings = ratings;
        this.seatings = seatings;
    }

    public String getMerlin() {
        return merlin;
    }

    public void setMerlin(String merlin) {
        this.merlin = merlin;
    }

    public String getPhoon() {
        return phoon;
    }

    public void setPhoon(String phoon) {
        this.phoon = phoon;
    }

    public String getSource() {
        return source;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDating() {
        return dating;
    }

    public void setDating(String dating) {
        this.dating = dating;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public int getSeatings() {
        return seatings;
    }

    public void setSeatings(int seatings) {
        this.seatings = seatings;
    }
}
