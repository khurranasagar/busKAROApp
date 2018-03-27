package com.example.sagar.buskaro;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Destinations extends AppCompatActivity {
    RecyclerView recyclerView2;
    DestAdapter adapter2;
    List<Object> destlist;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }

        searchView = (SearchView) findViewById(R.id.neareststop);
        searchView.setQueryHint("Nearest Stop");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Object> FilteredList = new ArrayList<Object>();
                FilteredList.add(new Dest(R.drawable.clock,"Kalkaji Mandir"));
                FilteredList.add( new Dest(R.drawable.clock,"Nehru Place"));
                FilteredList.add(new Dest2(R.drawable.clock,"Kalkaji Mandir","511A","Dhaula Kuan"));
                FilteredList.add(new Dest2(R.drawable.clock,"Kalkaji Mandir","611A","Dhaula Kuan"));
                adapter2.setfilter(FilteredList);
                return false;
            }
        });

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
        destlist.add(
                new Dest2(R.drawable.clock,"Rajiv Chowk","511A","Dhaula Kuan")
        );
        destlist.add(
                new Dest2(R.drawable.favorite2,"Rajiv Chowk","611A","Dhaula Kuan")
        );

        adapter2 = new DestAdapter(this, destlist);

        //setting adapter to recyclerview
        recyclerView2.setAdapter(adapter2);


    }


    public void backbutton(View view) {
        onBackPressed();
    }
}
