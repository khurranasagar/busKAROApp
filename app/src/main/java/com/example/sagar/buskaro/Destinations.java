package com.example.sagar.buskaro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Destinations extends AppCompatActivity {
    RecyclerView recyclerView2;
    DestAdapter adapter2;
    List<Dest> destlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);
        destlist=new ArrayList<>();
        recyclerView2 = (RecyclerView) findViewById(R.id.destrec);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        destlist.add(
                new Dest(R.drawable.clock,"Lajpat Nagar")
        );
        destlist.add(
                new Dest(R.drawable.favorite2,"Kalkaji Mandir")
        );
        destlist.add(
                new Dest(R.drawable.clock,"Kailash Colony")
        );
        destlist.add(
                new Dest(R.drawable.favorite2,"Greater Kailash")
        );

        adapter2 = new DestAdapter(this, destlist);

        //setting adapter to recyclerview
        recyclerView2.setAdapter(adapter2);

    }
}
