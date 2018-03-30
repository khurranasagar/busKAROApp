package com.example.sagar.buskaro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

import Modules.BusStop;
import Modules.DirectionFinderListener;
import Modules.DisplayRoute;
import Modules.Distance;
import Modules.Duration;
import Modules.Route;

public class Route_Description extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String buskaromsg="To find ETAs for the next 10 minutes, <b>3 busKARO</b> credits will be deducted.";
    private String buskarlimsg="Congratulations, you have earned <b>5 busKARO</b> credits. <br/> BALANCE: 7bk";
    RecyclerView recyclerView;
    RouteDescAdapter adapter;
    String origin;
    String LatlngLocation;
    private static final String TAG="Route_Description";
    List<Routedesc_getters> routedescgettersList;
    ImageView backbutton;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    private DirectionFinderListener listener;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private FirebaseAuth firebaseauth;
    private DatabaseReference dbr;
    public List<String> ETAs = new ArrayList<>();
    private boolean check_buskarli_status = false;
    String bus_karli_bus_number = null;
    int globall=0;
    int index = 0;
    List<String> all_station;


    public void execute(String or,String destination) throws UnsupportedEncodingException {
        onDirectionFinderStart();
        new Route_Description.DownloadRawData().execute(createUrl(or,destination));
    }

    private String createUrl(String origin,String destination) throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");
        String URL = DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination +"&mode=transit&transit_mode=bus"  +"&key=" + GOOGLE_API_KEY;
        Log.d("URL ", "createUrl: " + URL);
        Log.d("FIND ETAS KE LEI", "createUrl: ");
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

    private void sendRequest(String ori,String desti) {

        Log.d("SEDN REQUEST", "sendRequest: ");

        String origin = ori ;
        String destination = desti ;

        try {
            execute(origin,destination);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                Log.d("ON POST EXECUTION", "sendRequest: ");
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String data) throws JSONException {

        Log.d("IN ParseJson Function", "");

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
            ETAs.add(globall++,json_time);


            Log.d("ETAS", "parseJSon: " + ETAs.size());
            if(index>=0)
            {
                String hello = all_station.get(index);
                String hel[]=hello.split("\\.");
                sendRequest(origin,hel[1]);
                index--;
            }

            if(index<0)
            {
                send_ETA_to_database(ETAs);
            }






//            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
//            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
//            for(int j=0;j< jsonStep.length(); j++)
//            {
//                DisplayRoute dr = new DisplayRoute();
//                JSONObject current_step = jsonStep.getJSONObject(j);
//                if(current_step.has("transit_details"))
//                {
//                    dr.arrival_time = current_step.getJSONObject("transit_details").getJSONObject("arrival_time").getString("text");
//                    JSONObject jsondistance = current_step.getJSONObject("distance");
//                    JSONObject jsonduration = current_step.getJSONObject("duration");
//                    dr.distance = jsondistance.getString("text");
//                    dr.duration = jsonduration.getString("text");
//                    dr.instruction = current_step.getString("html_instructions");
//                    dr.bus_number = current_step.getJSONObject("transit_details").getJSONObject("line").getString("short_name");
//                    dr.no_of_stops =  current_step.getJSONObject("transit_details").getString("num_stops");
////                    Log.d(" No of stops : ", " STOP : " + dr.no_of_stops + " " + dr.bus_number);
//                }
//
//                if(!(current_step.has("transit_details")))
//                {
//
//                    JSONObject jsondistance = current_step.getJSONObject("distance");
//                    JSONObject jsonduration = current_step.getJSONObject("duration");
//                    dr.distance = jsondistance.getString("text");
//                    dr.duration = jsonduration.getString("text");
//                    dr.instruction = current_step.getString("html_instructions");
//                    dr.bus_number=null;
//                    dr.no_of_stops = null;
//                    dr.arrival_time = null;
//
//
//                }
//
//                drs.add(dr);
//
//            }
//
//            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
//            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
//            route.endAddress = jsonLeg.getString("end_address");
//            route.startAddress = jsonLeg.getString("start_address");
//            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
//            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
//            routes.add(route);

        }



//        onDirectionFinderSuccess(routes,drs);


    }


//    @Override
//    public void onDirectionFinderSuccess(final List<Route> routes, List<DisplayRoute> drs) {
//        polylinePaths = new ArrayList<>();
//        originMarkers = new ArrayList<>();
//        destinationMarkers = new ArrayList<>();
//        dbr = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = firebaseauth.getCurrentUser();
//        String name = user.getDisplayName();
//        String towards = null ;
//        for (final Route route : routes) {
//            Log.d("ROUTES", "in route for loop");
//            DatabaseReference child = dbr.child("users").child(user.getDisplayName());
//            child.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    long temp = (long) dataSnapshot.child("journey_number").getValue();
//                    int hello = (int) temp;
//                    String start = route.startAddress;
//                    String destination = route.endAddress;
//                    List<String> starters = (List<String>) dataSnapshot.child("starts").getValue();
//                    List<String> desters = (List<String>) dataSnapshot.child("destinations").getValue();
//                    starters.add(hello, start);
//                    desters.add(hello, destination);
//                    dataSnapshot.child("starts").getRef().setValue(starters);
//                    dataSnapshot.child("destinations").getRef().setValue(desters);
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__description);
        backbutton=(ImageView)findViewById(R.id.backarrow);
        backbutton.bringToFront();
        firebaseauth = FirebaseAuth.getInstance();
        origin = (String) getIntent().getStringExtra("Origin");
        Log.d("Origin in RD.java", "onCreate: " + origin);
        if(origin == null || origin.equals(""));
        {
            LatlngLocation = (String) getIntent().getStringExtra("LatLngCurrentLocation");
        }
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

        dbr = FirebaseDatabase.getInstance().getReference();
        busKARLIdialog("busKARLI",Html.fromHtml(buskarlimsg));
//        DatabaseReference buses = dbr.child("bus_routes_database").child(bus_karli_bus_number);
        Calendar calobj = Calendar.getInstance();
        TextView hello = (TextView) findViewById(R.id.Busno2);

        bus_karli_bus_number = hello.getText().toString();
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        final String current_time = df.format(calobj.getTime());
        String key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        Log.d("KEY", "Key value : " + key);
        key="-L8bGaUG6Zja4Lvd4AhJ";
        Log.d("TIME", "Current key :  " + key + " Number : " +  bus_karli_bus_number);

        DatabaseReference stations  = dbr.child("bus_routes_database").child(bus_karli_bus_number).child(key).child("stations");
        stations.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                int temp=0;
                 all_station = new ArrayList<>();
                for(DataSnapshot helll : dataSnapshot.getChildren()){
                    all_station.add(temp++,helll.child("name_of_bus_stop").getValue().toString());
                }


                String finalTowards = null;
                Log.d("All_station size", "onDataChange: " + all_station.size());
                try {
                    findETA(all_station,current_time, finalTowards);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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

    void findETA(final List<String> all_station, final String current_time, final String towards) throws InterruptedException {

            FirebaseUser user = firebaseauth.getCurrentUser();
            final List<String> all_stations = all_station;
            final int length = all_stations.size();
            String start_Address_ETA = origin;

            String hello = all_station.get(index);
            String[] nam = hello.split("\\.");
            while (index < length && !(nam[1].equalsIgnoreCase(start_Address_ETA))) {
                index++;
                hello = all_station.get(index);
                nam = hello.split("\\.");
            }

             Log.d("INDEX VALUE", "onDataChange: " + index);

            // If origin not present popup

//            for(int j=index-1;j>=0;j--)
//            {
                String end_ETA = all_stations.get(index-2);
                String lol[] = end_ETA.split("\\.");
                sendRequest(start_Address_ETA,lol[1]);

//                Thread.sleep(2000);
//            }



        }


    private void send_ETA_to_database(final List<String> etAs) {



        Log.d("SEND FUNCTIONS", "send_ETA_to_database: " + etAs.size());
        DatabaseReference hello = dbr.child("bus_routes_database").child(bus_karli_bus_number);
        hello.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child("ETAS").getRef().setValue(etAs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("SEND NUDES", "send_ETA_to_database: " + etAs.size());

    }



}
