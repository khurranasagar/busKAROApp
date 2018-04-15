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
import android.widget.ImageView;

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
    ImageView backbutton;
    String Origin;
    BusStop DestinationBusStop;
    private DatabaseReference dbr;
    BusRoutes route;
    int idx;
    List<String> ET;
    String bus_towards = null;
    String bustowards[];
    BusRoutes r445;

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyBFjK8UInAeNGfhx8attCH8UNY6xzNjuwU";
    private DirectionFinderListener listener;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    String bus_number = null;
    List<BusRoutes> all_routes;
    List<BusStop> all_station;


    RoutesAdapter adapter2;
    List<BusRoutes> routes_names;
    List<String> key;
    List<String> route_names;


    private void sendRequest(String ori,String desti) {

        String origin = ori;
        String destination = desti ;

        try {
            execute(origin, destination);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus__routes__search__result);
        backbutton=(ImageView)findViewById(R.id.backarrow2);
        backbutton.bringToFront();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SearchResultsRecyclerView = (RecyclerView) findViewById(R.id.routesoptions);
        SearchResultsRecyclerView.setHasFixedSize(true);
        SearchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        routes_names = new ArrayList<>();
        //routes_names.add(new BusRoutes("445A"));


        //setting adapter to recyclerview


        DestinationBusStop = (BusStop) getIntent().getSerializableExtra("EndDestinationBusStop");
        Origin = (String) getIntent().getStringExtra("Origin");
        CurentLocLatLng = (String) getIntent().getStringExtra("LatLngCurrentLocation");
//        all_routes = (List<BusRoutes>) getIntent().getSerializableExtra("Routes");
        r445 = (BusRoutes) getIntent().getSerializableExtra("445A");
        key = new ArrayList<String>();
        key.add("-L8bGaUG6Zja4Lvd4AhJ");
        key.add("-L8bPAYu2rAdR3ZbwyMv");

        route_names = new ArrayList<>();
        route_names.add("445A");
        route_names.add("445STL");

        adapter2 = new RoutesAdapter(this, routes_names, Origin, CurentLocLatLng, DestinationBusStop);

        SearchResultsRecyclerView.setAdapter(adapter2);

        sendRequest(Origin,DestinationBusStop.getStopname());


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


        for (int i = 0; i < jsonRoutes.length(); i++)
        {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonSteps = jsonLegs.getJSONObject(0);
            JSONArray jsonStep = jsonSteps.getJSONArray("steps");

            for(int j=0;j< jsonStep.length(); j++)
            {
                DisplayRoute dr = new DisplayRoute();
                JSONObject current_step = jsonStep.getJSONObject(j);

                if(current_step.has("transit_details"))
                {
                    bus_towards = current_step.getString("html_instructions");
                    bustowards = bus_towards.split(" towards ");
                    bus_towards = bustowards[1];

                    bus_number = current_step.getJSONObject("transit_details").getJSONObject("line").getString("short_name");

                }


            }
            bus_number = "445A";

            Log.d("GAAAAA", "parseJSon: " + bus_number + " " + bus_towards);


            int ijk = route_names.indexOf(bus_number);

            String numberbc = route_names.get(i);
            String key1 = key.get(ijk);

            Log.d("SEDN NUDES", "parseJSon: " + bus_number + " " + key1 + " " + numberbc);

            dbr = FirebaseDatabase.getInstance().getReference();

            DatabaseReference stations  = dbr.child("bus_routes_database").child(numberbc).child(key1).child("stations");
            Log.d("HEHE", "parseJSon: OK" + stations.toString());
            stations.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d("GGGG", "onDataChange: " + dataSnapshot.toString());
                    int temp=0;
                    all_station = new ArrayList<>();
                    for(DataSnapshot helll : dataSnapshot.getChildren()){
                        BusStop x = new BusStop(helll.child("name_of_bus_stop").getValue().toString());
                        all_station.add(x);
                    }

                    Log.d("All_station size", "onDataChange: " + all_station.size());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            BusRoutes r = new BusRoutes(numberbc);
            r.setStations(all_station);
            routes_names.clear();
            routes_names.add(r445);
            adapter2.setfilter(routes_names);




        }

        Log.d("Busnumber used", "parseJSon: " + bus_number);

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
