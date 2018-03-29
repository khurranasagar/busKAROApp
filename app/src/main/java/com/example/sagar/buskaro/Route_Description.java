package com.example.sagar.buskaro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Route_Description extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String buskaromsg="To find ETAs for the next 10 minutes, <b>3 busKARO</b> credits will be deducted.";
    private String buskarlimsg="Congratulations, you have earned <b>5 busKARO</b> credits. <br/> BALANCE: 7bk";
    RecyclerView recyclerView;
    RouteDescAdapter adapter;
    String origin;
    private static final String TAG="Route_Description";
    List<Routedesc_getters> routedescgettersList;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__description);
        backbutton=(ImageView)findViewById(R.id.backarrow);
        backbutton.bringToFront();

        origin = (String) getIntent().getStringExtra("Origin");
        

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);


        routedescgettersList =new ArrayList<>();

        recyclerView = findViewById(R.id.routedesc_recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routedescgettersList.add(new Routedesc_getters("OKHLA",R.drawable.gps));
        routedescgettersList.add(new Routedesc_getters("GOVIND PURI",R.drawable.stops));
        routedescgettersList.add(new Routedesc_getters("KALKAJI MANDIR",R.drawable.stops));
        routedescgettersList.add(new Routedesc_getters("GOVIND PURI",R.drawable.stops));
        routedescgettersList.add(new Routedesc_getters("KALKAJI MANDIR",R.drawable.stops));
        routedescgettersList.add(new Routedesc_getters("DHAULA KUAN",R.drawable.finaldest));

        adapter = new RouteDescAdapter(this, routedescgettersList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
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

    public void busKARLI(View view) {
        busKARLIdialog("busKARLI",Html.fromHtml(buskarlimsg));

    }

    private void busKARLIdialog(String title, Spanned message) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.buskarologo);
        builderSingle.setTitle(title);
        builderSingle.setMessage(message);
        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                busKARLIok();
            }
        });
        builderSingle.show();
    }

    public void busKARO(View view) {
        busKAROdialog("busKARO", Html.fromHtml(buskaromsg));
    }

    public void busKAROdialog(String title, Spanned message){
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.buskarologo);
        builderSingle.setTitle(title);
        builderSingle.setMessage(message);
        builderSingle.setNegativeButton(
                "Cancel",
        new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"buskaro Cancel called");
                busKAROcancel();

            }
        });
        builderSingle.setPositiveButton(
                "Confirm",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"Confirm called");
                        busKAROconfirm();

                    }
                });
        builderSingle.show();

    }

    private void busKAROcancel() {

    }
    private void busKAROconfirm() {
    }
    private void busKARLIok() {
    }

    public void backbutton(View view) {
        onBackPressed();
    }
}
