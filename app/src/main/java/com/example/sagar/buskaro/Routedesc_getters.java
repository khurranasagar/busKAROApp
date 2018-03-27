package com.example.sagar.buskaro;

/**
 * Created by Harshit Verma on 27-03-2018.
 */

public class Routedesc_getters {
    String stop;
    int location;

    public Routedesc_getters(String stop, int location) {
        this.stop = stop;
        this.location=location;
    }

    public int getLocation(){
        return location;
    }
    public String getStop() {
        return stop;
    }
}
