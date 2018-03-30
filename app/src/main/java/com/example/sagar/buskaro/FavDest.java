package com.example.sagar.buskaro;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public class FavDest{
    private String fav_dest;

    public FavDest(Favorites favorites, List<FavDest> destList) {
    }

    public String getFav_dest() {
        return fav_dest;
    }

    public FavDest(String fav_dest) {

        this.fav_dest = fav_dest;
    }
}
