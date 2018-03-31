package com.example.sagar.buskaro;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseUser;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import Modules.BusRoutes;
import Modules.BusStop;
import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.DisplayRoute;
import Modules.Distance;
import Modules.Duration;
import Modules.Route;

public class Destinations extends AppCompatActivity {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyBFjK8UInAeNGfhx8attCH8UNY6xzNjuwU";
    private DirectionFinderListener listener;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    String origin;
    RecyclerView recyclerView2;
    DestAdapter adapter2;
    List<Object> destlist;
//    SearchView searchView;
    EditText neareststop;
    SearchView searchView2;
    List<BusStop> favourite_Destinations;
    List<BusStop> Recent_Destinations;
    List<BusRoutes> favourite_Bus_numbers;
    List<BusRoutes> Recent_Bus_numbers;
    List<BusStop> AllStops;
    List<BusRoutes> AllRoutes;
    private DatabaseReference dbr;
    private String nearest_busstop;
    private String nearestbusstop;

    BusRoutes r;
    List<String> key;

    public void execute(String or,String destination) throws UnsupportedEncodingException {
        onDirectionFinderStart();
        new Destinations.DownloadRawData().execute(createUrl(or,destination));
    }

    private String createUrl(String origin,String destination) throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        Log.d("Hey Baaby", "createUrl: " + urlOrigin);
        String urlDestination = URLEncoder.encode(destination, "utf-8");
        String URL = DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination +"&mode=transit&transit_mode=bus"  +"&key=" + GOOGLE_API_KEY;
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

    private void sendRequest() {

        String origin = " ";
        String destination = null ;
        origin=getIntent().getStringExtra("Current Location");
//        Log.d("HELLO", "sendRequest: " + origin);
        destination = "Kailash Colony";
        if(origin != null && !origin.equals(" ")) {
            try {
                execute(origin, destination);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
            for(int j=0;j< jsonStep.length(); j++)
            {
                DisplayRoute dr = new DisplayRoute();
                if(j==0)
                {
                    JSONObject current_step = jsonStep.getJSONObject(j);
                    nearest_busstop = current_step.getString("html_instructions").toString();

                }


            }


        }


        if(nearest_busstop != null) {
            String[] busstop = nearest_busstop.split(" to ");
            nearestbusstop = busstop[1];
            neareststop.setText(nearestbusstop);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        key = new ArrayList<String>();
        key.add("-L8bGaUG6Zja4Lvd4AhJ");
        key.add("-L8bPAYu2rAdR3ZbwyMv");


        AllStops = new ArrayList<>();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }


        neareststop = (EditText) findViewById(R.id.neareststop);
        searchView2 = (SearchView) findViewById(R.id.destination);
        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Object> FilteredList = new ArrayList<Object>();
                if (newText.equals("") || newText.equals(" ")) {
                    FilteredList = new ArrayList<>();
                    for(int i=0;i<favourite_Destinations.size();i++){
                        FilteredList.add(new Dest(R.drawable.favorite2,favourite_Destinations.get(i)));
                    }
                    for(int i=0;i<Recent_Bus_numbers.size();i++){
                        FilteredList.add(new Dest2(R.drawable.clock,Recent_Bus_numbers.get(i)));
                    }
                    for(int i=0;i<Recent_Destinations.size();i++){
                        FilteredList.add(new Dest(R.drawable.clock,Recent_Destinations.get(i)));
                    }
                    for(int i=0;i<favourite_Bus_numbers.size();i++){
                        FilteredList.add(new Dest2(R.drawable.favorite2,favourite_Bus_numbers.get(i)));
                    }
                }
                else {
                    for (int i = 0; i < AllStops.size(); i++) {
                        if (AllStops.get(i).getStopname().toLowerCase().startsWith(newText.toLowerCase())) {
                            FilteredList.add(new Dest(R.drawable.finaldest, AllStops.get(i)));
                        }
                    }
                    for (int i = 0; i < AllRoutes.size(); i++) {
                        if (AllRoutes.get(i).getBus_number().toLowerCase().startsWith(newText.toLowerCase())) {
                            FilteredList.add(new Dest2(R.drawable.favorite2, AllRoutes.get(i)));
                        }
                    }
                    for (int i = 0; i < favourite_Bus_numbers.size(); i++) {
                        if (favourite_Bus_numbers.get(i).getBus_number().toLowerCase().startsWith(newText.toLowerCase())) {
                            FilteredList.add(new Dest2(R.drawable.favorite2, favourite_Bus_numbers.get(i)));
                        }
                    }
                    for (int i = 0; i < Recent_Bus_numbers.size(); i++) {
                        if (Recent_Bus_numbers.get(i).getBus_number().toLowerCase().startsWith(newText.toLowerCase())) {
                            FilteredList.add(new Dest2(R.drawable.clock, Recent_Bus_numbers.get(i)));
                        }
                    }
                    for (int i = 0; i < Recent_Destinations.size(); i++) {
                        if (Recent_Destinations.get(i).getStopname().toLowerCase().startsWith(newText.toLowerCase())) {
                            FilteredList.add(new Dest(R.drawable.clock, Recent_Destinations.get(i)));
                        }
                    }
                    for (int i = 0; i < favourite_Destinations.size(); i++) {
                        if (favourite_Destinations.get(i).getStopname().toLowerCase().startsWith(newText.toLowerCase())) {
                            FilteredList.add(new Dest(R.drawable.favorite2, favourite_Destinations.get(i)));
                        }
                    }
                }
                adapter2.setfilter(FilteredList);
                return false;
            }
        });

        destlist=new ArrayList<>();
        recyclerView2 = (RecyclerView) findViewById(R.id.destrec);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        AllRoutes = new ArrayList<BusRoutes>();



        dbr = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Route_Reference = dbr.child("bus_routes_database");
        Route_Reference.addValueEventListener(new ValueEventListener() {
            int i = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String busroute;
                for(DataSnapshot hel : dataSnapshot.getChildren()){
                    if(i<2)
                    {
                        busroute = hel.child(key.get(i)).child("bus_number").getValue().toString();
                        Log.d("Fuck u bitch", "onDataChange: " + busroute);
                        r = new BusRoutes(busroute);
                        AllRoutes.add(r);
                        DataSnapshot dbx = hel.child(key.get(i)).child("stations");
                        List<BusStop> stations = new ArrayList<BusStop>();
                        i++;
                        for(DataSnapshot stops : dbx.getChildren()){
                            stations.add(new BusStop(stops.child("name_of_bus_stop").getValue().toString().split("\\.")[1]));
                            AllStops.add(new BusStop(stops.child("name_of_bus_stop").getValue().toString().split("\\.")[1]));
                            Log.d("DFUCK", "onDataChange: " + stops.child("name_of_bus_stop").getValue().toString());
                        }
                        r.setStations(stations);
                    }

                }
                Log.d("OK", "onDataChange: " + AllRoutes.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        Log.d("Cex", "onCreate: " + AllRoutes.size());
//
//        for(int i=0;i<AllRoutes.size();i++) {
//            r = AllRoutes.get(i);
//            DatabaseReference Stations_Reference = dbr.child("bus_routes_database").child(r.getBus_number()).child(key.get(i)).child("stations");
//            Stations_Reference.addValueEventListener(new ValueEventListener() {
//                List<BusStop> stations = new ArrayList<BusStop>();
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot hel : dataSnapshot.getChildren()) {
////                            BusStop busStop = hel.getValue(BusStop.class);
////                            Log.d("COUNT of stations array", "onClick: " + hel.child("name_of_bus_stop").getValue());
//                        stations.add(new BusStop(hel.child("name_of_bus_stop").getValue().toString()));
//                        AllStops.add(new BusStop(hel.child("name_of_bus_stop").getValue().toString()));
//
//                    }
//                    Log.d("Main tera hogaya", "onDataChange: " + r.getBus_number().toString());
//                    r.setStations(stations);
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }

        favourite_Destinations = new ArrayList<>();
        Recent_Bus_numbers = new ArrayList<>();
        Recent_Destinations = new ArrayList<>();
        favourite_Bus_numbers = new ArrayList<>();


        favourite_Bus_numbers.add(new BusRoutes("511A"));
        Recent_Bus_numbers.add(new BusRoutes("611A"));
        favourite_Destinations.add(new BusStop("Kailash Colony"));
        Recent_Destinations.add(new BusStop("Rajouri Garden"));

        for(int i=0;i<favourite_Destinations.size();i++){
            destlist.add(new Dest(R.drawable.favorite2,favourite_Destinations.get(i)));
        }
        for(int i=0;i<Recent_Bus_numbers.size();i++){
            destlist.add(new Dest2(R.drawable.clock,Recent_Bus_numbers.get(i)));
        }
        for(int i=0;i<Recent_Destinations.size();i++){
            destlist.add(new Dest(R.drawable.clock,Recent_Destinations.get(i)));
        }
        for(int i=0;i<favourite_Bus_numbers.size();i++){
            destlist.add(new Dest2(R.drawable.favorite2,favourite_Bus_numbers.get(i)));
        }




//        destlist.add(
//                new Dest(R.drawable.clock,"Lajpat Nagar")
//        );
//        destlist.add(
//                new Dest(R.drawable.favorite2,"Kalkaji Mandir")
//        );
//        destlist.add(
//                new Dest(R.drawable.clock,"Kailash Colony")
//        );
//        destlist.add(
//                new Dest(R.drawable.favorite2,"Greater Kailash")
//        );
//        destlist.add(
//                new Dest2(R.drawable.clock,"Rajiv Chowk","511A","Dhaula Kuan")
//        );
//        destlist.add(
//                new Dest2(R.drawable.favorite2,"Rajiv Chowk","611A","Dhaula Kuan")
//        );

        sendRequest();

        adapter2 = new DestAdapter(this, destlist,neareststop, origin,AllRoutes);

        //setting adapter to recyclerview
        recyclerView2.setAdapter(adapter2);


    }


}
