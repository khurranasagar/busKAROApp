package com.example.sagar.buskaro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class Destinations extends AppCompatActivity {
    RecyclerView recyclerView2;
    ProductAdapter adapter2;
    List<Product> destlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);
        destlist=new ArrayList<>();
        recyclerView2 = (RecyclerView) findViewById(R.id.destrec);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        destlist.add(
                new Product(R.drawable.clock,"Lajpat Nagar")
        );
        destlist.add(
                new Product(R.drawable.clock,"Lajpat Nagar")
        );

        adapter2 = new ProductAdapter(this, destlist);

        //setting adapter to recyclerview
        recyclerView2.setAdapter(adapter2);

    }
}
