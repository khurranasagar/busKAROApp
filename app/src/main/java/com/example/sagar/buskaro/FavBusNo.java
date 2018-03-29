package com.example.sagar.buskaro;

public class FavBusNo {
    private String fav_busno;
    private String fav_dest;

    public String getFav_busno() {
        return fav_busno;
    }

    public String getFav_dest() {
        return fav_dest;
    }

    public FavBusNo(String fav_busno, String fav_dest) {

        this.fav_busno = fav_busno;
        this.fav_dest = fav_dest;
    }
}
