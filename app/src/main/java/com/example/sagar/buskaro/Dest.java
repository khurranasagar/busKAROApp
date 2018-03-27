package com.example.sagar.buskaro;

import Modules.BusStop;

/**
 * Created by Harshit Verma on 23-03-2018.
 */

public class Dest {
    private int image;
    private BusStop stop;

    public Dest(int image, BusStop destname) {
        this.image = image;
        this.stop = destname;
    }

    public int getImage() {
        return image;
    }

    public BusStop getBusStop() {
        return stop;
    }
}
