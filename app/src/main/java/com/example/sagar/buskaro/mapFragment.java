package com.example.sagar.buskaro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

import Modules.Users;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.LocationManager.*;
import static com.example.sagar.buskaro.HomeScreen.PERMISSION_REQUEST_LOCATION_CODE;


/**
 * A simple {@link Fragment} subclass.
 */
public class mapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleMap map;
    public LocationManager locationManager;
    private String provider;
    private double lat;
    private double lon;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location lastlocation;
    private LatLng newPnt;
    FloatingActionButton FAB;
    private Marker currentLocationMarker;
    private DatabaseReference dbr;

    private GoogleApiClient client;
    private FirebaseAuth firebaseauth;
    FloatingActionButton btnMyLocation;

    private View mMapView;

    public mapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_map, container, false);
        Button b1=(Button)v.findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),Destinations.class);
                if(lastlocation != null) {
                    intent.putExtra("Current Location", Double.toString(lastlocation.getLatitude()) + "," + Double.toString(lastlocation.getLongitude()));
                    Log.d("Location Passed", "ClickSearchBox: " + Double.toString(lastlocation.getLatitude()) + "," + Double.toString(lastlocation.getLongitude()));
                }
                startActivity(intent);

            }
        });
//        Button b2=(Button)v.findViewById(R.id.destbutton2);
//        b2.setOnClickListener(this);

        firebaseauth = FirebaseAuth.getInstance();
        dbr = FirebaseDatabase.getInstance().getReference();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }

        mMapView = (View) v.findViewById(R.id.map1);


        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 120);



        writeNewUser();

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                        else{
                            LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());

                            MarkerOptions markoptions = new MarkerOptions();
                            markoptions.position(latlng);
                            markoptions.title("Current Location");
                            markoptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 15);
                            map.animateCamera(cameraUpdate);
                        }
                    }
                });
        //btnMyLocation = (FloatingActionButton) v.findViewById(R.id.myLocationButton);







        return v;

    }



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
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Permission Denied",Toast.LENGTH_LONG).show();
                    }
                    return;
                }
        }
    }

    protected synchronized void buildGoogleApiClient(){


        client = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setIndoorEnabled(false);
        //map.getUiSettings().setMyLocationButtonEnabled(false);
        map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(28.5456,77.2732) , 10) );
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }
//        btnMyLocation.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Location loc = map.getMyLocation();
//                if (loc != null) {
//                    LatLng latLang = new LatLng(loc.getLatitude(), loc
//                            .getLongitude());
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang, 15);
//                    map.animateCamera(cameraUpdate);
//
//                }
//
//            }
//        });

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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 15);
        map.animateCamera(cameraUpdate);
    }




    public boolean checkLocationPermission(){

        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOCATION_CODE );
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOCATION_CODE );
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
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
