package com.example.sagar.buskaro;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Bus_Routes_Search_Result extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    RecyclerView SearchResultsRecyclerView;
    RoutesAdapter adapter2;
    List<String> routes_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus__routes__search__result);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SearchResultsRecyclerView = (RecyclerView) findViewById(R.id.routesoptions);
        SearchResultsRecyclerView.setHasFixedSize(true);
        SearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        routes_names = new ArrayList<String>();

        routes_names.add("Sagar Khurana");
        routes_names.add("Abhijeet Singh");

        adapter2 = new RoutesAdapter(this, routes_names);

        //setting adapter to recyclerview
        SearchResultsRecyclerView.setAdapter(adapter2);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}