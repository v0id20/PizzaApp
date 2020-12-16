package com.github.v0id20.pizzaapp;

import com.google.android.gms.maps.model.LatLng;

public class Store {

    private LatLng coordrinates;
    private String address;

    public Store(LatLng coords, String address){
        coordrinates = coords;
        this.address = address;
    }

    public LatLng getCoordrinates() {
        return coordrinates;
    }

    public String getAddress() {
        return address;
    }
}
