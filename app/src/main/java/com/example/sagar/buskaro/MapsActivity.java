package com.example.sagar.buskaro;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Point;
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

import java.io.UnsupportedEncodingException;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.DisplayRoute;
import Modules.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LinearLayout linearLayout;

    private DatabaseReference dbr;

    private GoogleApiClient client;
    private FirebaseAuth firebaseauth;

//    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;



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

    private void sendRequest() {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();

        FirebaseUser user = firebaseauth.getCurrentUser();
        String name =  user.getDisplayName();
        DatabaseReference child = dbr.child("users").child(name);
        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long temp =  (long) dataSnapshot.child("journey_number").getValue();
                temp=temp+1;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(28.550631, 7.268803);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .position(hcmus)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
//        View locationButton = ((View) mMap.getView().findViewById(1).getParent()).findViewById(2);
//        LinearLayout.LayoutParams rlp = (LinearLayout.LayoutParams) locationButton.getLayoutParams();
//        rlp.addRule(LinearLayout.ALIGN_PARENT_TOP, 0);
//        rlp.addRule(LinearLayout.ALIGN_PARENT_BOTTOM, LinearLayoutt.TRUE);
//        rlp.setMargins(0, 0, 30, 30);

    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

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
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }



    @Override
    public void onDirectionFinderSuccess(final List<Route> routes, List<DisplayRoute> drs) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        FirebaseUser user = firebaseauth.getCurrentUser();
        String name =  user.getDisplayName();
        for ( DisplayRoute dr: drs)
        {
            Log.d("Instruction", "Instruction : " + dr.instruction);
            if(dr.bus_number!=null)
            {
                final DisplayRoute hello = dr ;
                DatabaseReference child = dbr.child("users").child(user.getDisplayName());
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map point = (Map) dataSnapshot.child("bus_numbers_used_order").getValue();
                        Log.d("In RATTEY", "onDirectionFinderSuccess: " + point.get(dataSnapshot.child(hello.bus_number).getValue()));
                        if(point.get(dataSnapshot.child(hello.bus_number).getValue())!=null)
                        {

                            Integer temp = (Integer) point.get(dataSnapshot.child(hello.bus_number).getValue());
                            temp=temp+1;
                            point.remove(hello.bus_number);
                            point.put(hello.bus_number,temp);
                            dataSnapshot.child("bus_numbers_used_order").getRef().setValue(point);
//                            Log.d("In DRRRR", "onDirectionFinderSuccess: " + hello.bus_number);

                        }
                        else
                        {
                            Log.d("In MEIN", "onDirectionFinderSuccess: " + hello.bus_number);
                            point.put(hello.bus_number,1);
                            dataSnapshot.child("bus_numbers_used_order").getRef().setValue(point);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


        }





        for (final Route route : routes) {
            Log.d("ROUTES","in route for loop");
            DatabaseReference child = dbr.child("users").child(user.getDisplayName());
            child.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long temp =  (long) dataSnapshot.child("journey_number").getValue();
                    int hello = (int) temp;
                    String start = route.startAddress;
                    String destination = route.endAddress;
                    List<String> starters = (List<String>) dataSnapshot.child("starts").getValue();
                    List<String> desters = (List<String>) dataSnapshot.child("destinations").getValue();
                    starters.add(hello,start);
                    desters.add(hello,destination);
                    dataSnapshot.child("starts").getRef().setValue(starters);
                    dataSnapshot.child("destinations").getRef().setValue(desters);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
//
//            originMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
//                    .title(route.startAddress)
//                    .position(route.startLocation)));
//            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
//                    .title(route.endAddress)
//                    .position(route.endLocation)));
//
//            PolylineOptions polylineOptions = new PolylineOptions().
//                    geodesic(true).
//                    color(Color.BLUE).
//                    width(10);
//
//            for (int i = 0; i < route.points.size(); i++)
//                polylineOptions.add(route.points.get(i));
//
//            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
