package com.example.sagar.buskaro;

/**
 * Created by Harshit Verma on 27-03-2018.
 */

public class Dest2 {
    private int image;
    private String dest,busno,start;

    public int getImage() {
        return image;
    }

    public String getDest() {
        return dest;
    }

    public String getBusno() {
        return busno;
    }

    public String getStart() {
        return start;
    }

    public Dest2(int image, String dest, String busno, String start) {
        this.image = image;
        this.dest = dest;
        this.busno = busno;

        this.start = start;
    }
}
