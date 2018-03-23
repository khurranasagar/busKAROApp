package com.example.sagar.buskaro;

/**
 * Created by Harshit Verma on 23-03-2018.
 */

public class Product {
    private int image;
    private String destname;

    public Product(int image, String destname) {
        this.image = image;
        this.destname = destname;
    }

    public int getImage() {
        return image;
    }

    public String getDestname() {
        return destname;
    }
}
