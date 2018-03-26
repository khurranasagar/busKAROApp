package com.example.sagar.buskaro;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import java.io.UnsupportedEncodingException;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Modules.BusStop;
import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.DisplayRoute;
import Modules.Distance;
import Modules.Duration;
import Modules.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    private DirectionFinderListener listener;
    private String origin;
    private String destination;
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private List<String> busroute1  = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private DatabaseReference dbr;
    private String bus_karli_bus_number;
    private GoogleApiClient client;
    private FirebaseAuth firebaseauth;
    private boolean check_buskarli_status = false;
    public List<String> ETAs = new ArrayList<>();
    int globall=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebaseauth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference();


//        linearLayout = (LinearLayout) findViewById(R.id.ll);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest();
            }
        });
    }

    public void execute(String or,String destination) throws UnsupportedEncodingException {
       onDirectionFinderStart();
        new DownloadRawData().execute(createUrl(or,destination));
    }

    private String createUrl(String origin,String destination) throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");
        String URL = DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination +"&key=" + GOOGLE_API_KEY;
        Log.d("URL ", "createUrl: " + URL);
        return URL;
    }

    private void sendRequest() {

        String origin = null ;
        String destination = null ;
        origin = etOrigin.getText().toString();
        destination = etDestination.getText().toString();
        FirebaseUser user = firebaseauth.getCurrentUser();
        String name = user.getDisplayName();
        DatabaseReference child = dbr.child("users").child(name);
        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long temp = (long) dataSnapshot.child("journey_number").getValue();
                temp = temp + 1;
                dataSnapshot.child("journey_number").getRef().setValue(temp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }



        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest_ETA(String ori,String desti) {

        String origin = null ;
        String destination = null ;

        origin = ori;
        destination = desti;


        try {
            execute(ori,desti);
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
            Log.d("JSON_TIME", "parseJSon: " + json_time);
            ETAs.add(globall++,json_time);
            Log.d("LOL", "onDataChange: " + ETAs.size());
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
            for(int j=0;j< jsonStep.length(); j++)
            {
                DisplayRoute dr = new DisplayRoute();
                JSONObject current_step = jsonStep.getJSONObject(j);
                if(current_step.has("transit_details"))
                {
                    dr.arrival_time = current_step.getJSONObject("transit_details").getJSONObject("arrival_time").getString("text");
                    JSONObject jsondistance = current_step.getJSONObject("distance");
                    JSONObject jsonduration = current_step.getJSONObject("duration");
                    dr.distance = jsondistance.getString("text");
                    dr.duration = jsonduration.getString("text");
                    dr.instruction = current_step.getString("html_instructions");
                    dr.bus_number = current_step.getJSONObject("transit_details").getJSONObject("line").getString("short_name");
                    dr.no_of_stops =  current_step.getJSONObject("transit_details").getString("num_stops");
//                    Log.d(" No of stops : ", " STOP : " + dr.no_of_stops + " " + dr.bus_number);
                }

                if(!(current_step.has("transit_details")))
                {

                    JSONObject jsondistance = current_step.getJSONObject("distance");
                    JSONObject jsonduration = current_step.getJSONObject("duration");
                    dr.distance = jsondistance.getString("text");
                    dr.duration = jsonduration.getString("text");
                    dr.instruction = current_step.getString("html_instructions");
                    dr.bus_number=null;
                    dr.no_of_stops = null;
                    dr.arrival_time = null;


                }

//              Log.d("Instruction", "Instruction for current path :  " + dr.instruction);

                drs.add(dr);

            }

            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));

            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
            routes.add(route);

        }



        onDirectionFinderSuccess(routes,drs);

    }


        @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(28.550631, 7.268803);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .position(hcmus)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

    }


    @Override
    public void onDirectionFinderStart() {
//        progressDialog = ProgressDialog.show(this, "Please wait.",
//                "", true);

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




    @Override
    public void onDirectionFinderSuccess(final List<Route> routes, List<DisplayRoute> drs) {
//        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        dbr = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseauth.getCurrentUser();
        String name = user.getDisplayName();
        TextView bus = (TextView) findViewById(R.id.busnumber);
        String towards = null ;
        for (DisplayRoute dr : drs)
        {
            Log.d("Instruction", "Instruction : " + dr.instruction);
            if (dr.bus_number != null) {
                towards = dr.instruction;
                bus_karli_bus_number=dr.bus_number;
                bus.setText(bus_karli_bus_number);
                Log.d("BUSKARLI BUS NUMBER", "Number : " + bus_karli_bus_number);
                final DisplayRoute hello = dr;
                DatabaseReference child = dbr.child("users").child(user.getDisplayName());
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map point = (Map) dataSnapshot.child("bus_numbers_used_order").getValue();
                        Log.d("In RATTEY", "onDirectionFinderSuccess: " + point.get(dataSnapshot.child(hello.bus_number).getValue()));
                        if (point.get(dataSnapshot.child(hello.bus_number).getValue()) != null) {
                            Log.d("NOT NULL", "IF mein hai ");
                            Integer temp = (Integer) point.get(dataSnapshot.child(hello.bus_number).getValue());
                            temp = temp + 1;
                            point.remove(hello.bus_number);
                            point.put(hello.bus_number, temp);
                            dataSnapshot.child("bus_numbers_used_order").getRef().setValue(point);
//                            Log.d("In DRRRR", "onDirectionFinderSuccess: " + hello.bus_number);

                        } else {
                            Log.d("NULL", "else mein hai ");
                            Log.d("In else", "Bus number : " + hello.bus_number);
                            point.put(hello.bus_number, 1);
                            Log.d("Else mein hai abhi bhe", "point : " + point.get(hello.bus_number));
                            dataSnapshot.child("bus_numbers_used_order").getRef().setValue(point);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


        }

        if(check_buskarli_status==false)
        {
            bus_karli_bus_number="445A";

            for (final Route route : routes) {
                Log.d("ROUTES", "in route for loop");
                DatabaseReference child = dbr.child("users").child(user.getDisplayName());
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long temp = (long) dataSnapshot.child("journey_number").getValue();
                        int hello = (int) temp;
                        String start = route.startAddress;
                        String destination = route.endAddress;
                        List<String> starters = (List<String>) dataSnapshot.child("starts").getValue();
                        List<String> desters = (List<String>) dataSnapshot.child("destinations").getValue();
                        starters.add(hello, start);
                        desters.add(hello, destination);
                        dataSnapshot.child("starts").getRef().setValue(starters);
                        dataSnapshot.child("destinations").getRef().setValue(desters);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            Button bus_karli = (Button) findViewById(R.id.buskarli);
            final String finalTowards = towards;
            bus_karli.setOnClickListener(new View.OnClickListener() {

                List<BusStop> all_stations;
                @Override
                public void onClick(View view) {

                    check_buskarli_status = true ;

                    DatabaseReference buses = dbr.child("bus_routes_database").child(bus_karli_bus_number);
                    Calendar calobj = Calendar.getInstance();
                    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    final String current_time = df.format(calobj.getTime());
                    String key = getIntent().getStringExtra("EXTRA_SESSION_ID");
                Log.d("KEY", "Key value : " + key);
                Log.d("TIME", "Current time :  " + current_time);
                    DatabaseReference stations  = dbr.child("bus_routes_database").child(bus_karli_bus_number).child(key).child("stations");
                    stations.addValueEventListener(new ValueEventListener() {
                        @Override

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int temp=0;
                            List<String> all_station = new ArrayList<>();
                            for(DataSnapshot hel : dataSnapshot.getChildren()){
//                            BusStop busStop = hel.getValue(BusStop.class);
//                            Log.d("COUNT of stations array", "onClick: " + hel.child("name_of_bus_stop").getValue());
                                all_station.add(temp++,hel.child("name_of_bus_stop").getValue().toString());

                            }


                            findETA(all_station,current_time, finalTowards);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });
        }




    }

    void findETA(final List<String> all_station, final String current_time, final String towards)
    {

        FirebaseUser user = firebaseauth.getCurrentUser();
        final List<String> all_stations = all_station;
        final int length = all_stations.size();
        DatabaseReference hello = dbr.child("users").child(user.getDisplayName());
        hello.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long temp = (long) dataSnapshot.child("journey_number").getValue();
                int journey_number = (int) temp;
                List<String> starts = (List<String>) dataSnapshot.child("starts").getValue();
                List<String> dests = (List<String>) dataSnapshot.child("destinations").getValue();
                String start_address = starts.get(journey_number);
                String end_address = dests.get(journey_number);
                Log.d("Start and End address", "Start address : " + start_address + " End address :  " + end_address + " " + current_time);
                int index = 0;
                String start_Address_ETA = null;
                if (start_address.contains("Ma Anandmayee Marg, NSIC Estate")) {
                    start_Address_ETA = "Govind Puri Metro Station";

                }
                while (index < length && !(all_stations.get(index).contains(start_Address_ETA))) {
                    index++;
                }


                String check[] = towards.split("towards");
                String final_destinations = check[1];


                    for(int j=index-1;j>=0;j--)
                    {
                        String end_ETA = all_stations.get(j);
                        String lol[] = end_ETA.split("\\.");
                        sendRequest_ETA(start_Address_ETA,lol[1]);
                    }

//                for(int i=index;i< length ;i++)
//                {
//                    ETAs.add(globall++,"0");
//                }

                send_ETA_to_database(ETAs);

            }

            private void send_ETA_to_database(final List<String> etAs) {

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

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    long[] get_time_difference(String dateStart,String dateStop)
    {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        long[] dif = new long[4];

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");


            dif[0]=diffDays;
            dif[1]=diffHours;
            dif[2]=diffMinutes;
            dif[3]=diffSeconds;




        } catch (Exception e) {
            e.printStackTrace();
        }

        return dif;

    }

}
