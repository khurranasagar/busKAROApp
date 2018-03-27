package com.example.sagar.buskaro;

import Modules.BusRoutes;

/**
 * Created by Harshit Verma on 27-03-2018.
 */

public class Dest2 {
    private int image;
    private BusRoutes route;

    public int getImage() {
        return image;
    }

    public BusRoutes getBusRoute(){
        return route;
    }

    public Dest2(int image, BusRoutes route) {
        this.image = image;
        this.route = route;
    }
}
