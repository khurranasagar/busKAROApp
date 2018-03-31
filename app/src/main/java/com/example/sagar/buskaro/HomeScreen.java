package com.example.sagar.buskaro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Modules.BusRoutes;
import Modules.BusStop;
import Modules.Users;

public class HomeScreen extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener  {

    private GoogleMap mMap;
    private DatabaseReference dbr;

    private GoogleApiClient client;
    private FirebaseAuth firebaseauth;

    public String key = "-L8auraKaIA5Xvr_Ak3x";

//    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;

    private LocationRequest locationRequest;
    public DatabaseReference pass;

    private Location lastlocation;

    private Marker currentLocationMarker;

    public static final int PERMISSION_REQUEST_LOCATION_CODE = 99;
    private Button DestButton;

    private View mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lastlocation = new Location("IIIT Delhi");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        firebaseauth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        mMapView = (View) findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        DestButton=(Button)findViewById(R.id.destbutton);
        DestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Destinations.class);
                intent.putExtra("EXTRA_SESSION_ID", key);
                if(lastlocation != null) {
                    intent.putExtra("Current Location", Double.toString(lastlocation.getLatitude()) + "," + Double.toString(lastlocation.getLongitude()));
                }
                startActivity(intent);
            }
        });
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 150);
        mapFragment.getMapAsync(this);




//        try {
//            writeNewRoutes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    private void writeNewRoutes() throws IOException
//    {
//
//        dbr = FirebaseDatabase.getInstance().getReference();
//        InputStream is = getResources().openRawResource(R.raw.buskaro);
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(is, Charset.forName("UTF-8"))
//        );
//        String line;
//        String bus_numbers[] = new String[10];
//            if ((line = reader.readLine())!=null)
//            {
//                bus_numbers = line.split(",");
//
//                for(int i=0;i<9;i++)
//                {
//                    BusRoutes br = new BusRoutes();
//                    br.total_stations = 1;
//                    BusStop bs =  new BusStop();
//                    br.stations = new ArrayList<>();
////                    Log.d("BusNumber", "writeBusNumber : " + bus_numbers[i]);
//                    br.bus_number=bus_numbers[i];
//                    bs.name_of_bus_stop = "nothing";
//                    bs.coordinate =new LatLng(0,0);
//                    br.stations.add(0,bs);
//                    dbr.child("bus_routes_database").child(bus_numbers[i]).setValue(br);
//                }
//
//             }
//
//            String[] test = new String[100];
//            int index=0;
//            while ((line = reader.readLine())!=null )
//            {
//                final String names[]= line.split(",");
//                test[index++]=names[7];
//                for(int j=0;j<9;j++)
//                {
//                    final int temp = j ;
//
//                    DatabaseReference rf = dbr.child("bus_routes_database").child(bus_numbers[j]);
//
//                    rf.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            List<BusStop> br = new ArrayList<>();
//                            br = (List<BusStop>) dataSnapshot.child("stations").getValue();
//                            long no_of_stations = (long) dataSnapshot.child("total_stations").getValue();
//                            int tempp = (int) no_of_stations;
////                            Log.d("LENGTH OF BR", "Length : " + br.size() + " " + tempp );
//                            BusStop bs = new BusStop();
//                            bs.name_of_bus_stop = names[temp];
//                            bs.coordinate = new LatLng(0,0);
//
//                            if(tempp< br.size())
//                            {
//                                br.add(tempp,bs);
////                                Log.d("LENGTH OF BR", "Length : " + br.size());
//                            }
//
//                            tempp = tempp + 1;
////                            Log.d("Updated tempp value", "onDataChange: " + tempp);
//                            dataSnapshot.child("total_stations").getRef().setValue(tempp);
//                            dataSnapshot.child("stations").getRef().setValue(br);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
////                    Log.d("END WHILE", "WHILE HAS ENDED HERE ----------------");
//
//                }
//
//            }
//
//            BusRoutes br = new BusRoutes();
//            br.stations = new ArrayList<>();
//            br.bus_number="445A";
//            br.total_stations=0;
//            int k=0;
//            for ( String name : test)
//            {
//
////                Log.d("445A", "writeNewRoutes: " +name );
//                if( name!=null && !(name.equalsIgnoreCase("empty")))
//                {
//                    BusStop bs = new BusStop();
//                    bs.coordinate = new LatLng(0,0);
//                    bs.name_of_bus_stop=name;
//                    br.stations.add(k++,bs);
//                    br.total_stations++;
//                }
//            }
//
////        Log.d("LENGTH", "Length of 445A : " + br.stations.size());
//        DatabaseReference hello ;
//        hello  = dbr.child("bus_routes_database").child(br.bus_number).push();
//        key=hello.getKey();
//
//        hello.setValue(br);
//
//    }

    private void writeNewUser() {
        dbr = FirebaseDatabase.getInstance().getReference();
        Users us = new Users();
        FirebaseUser user = firebaseauth.getCurrentUser();
//        dbr.child("users").child(user.getDisplayName()).setValue(us);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case PERMISSION_REQUEST_LOCATION_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //Permission is granted
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                    else
                    {
                        Toast.makeText(this, "Permission Denied",Toast.LENGTH_LONG).show();
                    }
                    return;
                }
        }
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
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);


        }

    }

    protected synchronized void buildGoogleApiClient(){


        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

        lastlocation = location;

        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markoptions = new MarkerOptions();
        markoptions.position(latlng);
        markoptions.title("Current Location");
        markoptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        //currentLocationMarker = mMap.addMarker(markoptions);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        //mMap.animateCamera(CameraUpdateFactory.zoomBy(15));

        // if(client != null){
        //LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        //}

    }

    public boolean checkLocationPermission(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOCATION_CODE );
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOCATION_CODE );
            }
            return false;
        }
        else
        {
            return true;
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void ClickSearchBox(View v){
//        Intent intent = new Intent(this, Destinations.class);
//        Log.d(" ket in homescreen", "ClickSearchBox: " + key);
//        intent.putExtra("EXTRA_SESSION_ID", key);
//        startActivity(intent);
    }

}
