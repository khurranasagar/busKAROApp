package com.example.sagar.buskaro;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import Modules.BusRoutes;
import Modules.BusStop;
import Modules.DirectionFinderListener;
import Modules.DisplayRoute;
import Modules.Route;

public class Bus_Routes_Search_Result extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String buskaromsg = "To find ETAs for the next 10 minutes, <b>3 busKARO</b> credits will be deducted.";
    RecyclerView SearchResultsRecyclerView;
    private static final String TAG = "Bus_Routes_";
    String CurentLocLatLng;
    String Origin;
    BusStop DestinationBusStop;
    private DatabaseReference dbr;
    BusRoutes route;
    int idx;
    List<String> ET;

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    private DirectionFinderListener listener;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();


    RoutesAdapter adapter2;
    List<BusRoutes> routes_names;

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

        routes_names = new ArrayList<>();


        //setting adapter to recyclerview
        SearchResultsRecyclerView.setAdapter(adapter2);

        DestinationBusStop = (BusStop) getIntent().getSerializableExtra("EndDestinationBusStop");
        Origin = (String) getIntent().getStringExtra("Origin");
        CurentLocLatLng = (String) getIntent().getStringExtra("LatLngCurrentLocation");

        adapter2 = new RoutesAdapter(this, routes_names, Origin, CurentLocLatLng, DestinationBusStop);

        try {
            execute(Origin, DestinationBusStop.getStopname().toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public void execute(String or, String destination) throws UnsupportedEncodingException {
        onDirectionFinderStart();
        new Bus_Routes_Search_Result.DownloadRawData().execute(createUrl(or, destination));
    }

    private String createUrl(String origin, String destination) throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        Log.d("Hey Baaby", "createUrl: " + urlOrigin);
        String urlDestination = URLEncoder.encode(destination, "utf-8");
        String URL = DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&mode=transit&transit_mode=bus" + "&key=" + GOOGLE_API_KEY;
        Log.d("URL ", "createUrl: " + URL);
        return URL;
    }


    public void onDirectionFinderStart() {

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }


    public void backbutton(View view) {
        onBackPressed();
    }


    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void parseJSon(String data) throws JSONException {
        Log.d("IN MAPSA WALA PARSE", "HEKKKII: ");

        if (data == null)
            return;

        List<Route> routes = new ArrayList<Route>();
        List<DisplayRoute> drs = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");

        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();
            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonSteps = jsonLegs.getJSONObject(0);
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONArray jsonStep = jsonSteps.getJSONArray("steps");
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            String json_time = jsonLeg.getJSONObject("duration").getString("text");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");


        }


//        Log.d("NEAREST BUSSTOP", "parseJSon: " + busstop[1]);

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

        Marker m1 = mMap.addMarker(new MarkerOptions().position(getLocationFromAddress(this, Origin)).title(Origin));
        Marker m2 = mMap.addMarker(new MarkerOptions().position(getLocationFromAddress(this, DestinationBusStop.getStopname())).title(DestinationBusStop.getStopname()));
        m1.showInfoWindow();
        m2.showInfoWindow();

        List<Marker> markers = new ArrayList<>();
        markers.add(m1);
        markers.add(m2);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }


        final LatLngBounds bounds = builder.build();

        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }
        });


        // Add a marker in Sydney and move the camera

    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public void busKARO(View view) {
        busKAROdialog("busKARO", Html.fromHtml(buskaromsg));
    }

    public void busKAROdialog(String title, Spanned message) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.buskarologo);
        builderSingle.setTitle(title);
        builderSingle.setMessage(message);
        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "buskaro Cancel called");
                        busKAROcancel();

                    }
                });
        builderSingle.setPositiveButton(
                "Confirm",
                new DialogInterface.OnClickListener() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Confirm called");
                        busKAROconfirm();

                    }
                });
        builderSingle.show();

    }

    private void busKAROcancel() {

    }

    private void busKAROconfirm() {

        dbr = FirebaseDatabase.getInstance().getReference();
        int i = 0;
        for(i= 0; i< routes_names.size(); i++) {
            route = routes_names.get(i);
            DatabaseReference hello = dbr.child("bus_routes_database").child(route.getBus_number());
            hello.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    ET = (List<String>) dataSnapshot.child("ETAS").getValue();
                    ArrayList<String> stopnames = new ArrayList<>();
                    for (BusStop stop : route.getStations()) {
                        stopnames.add(stop.getStopname().toString().toLowerCase());
                    }
                    idx = stopnames.indexOf(Origin.toLowerCase());
                    route.setETAs(ET);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }



            });
            routes_names.clear();
            routes_names.add(route);
            adapter2.setfilter(routes_names);

        }
    }
}
