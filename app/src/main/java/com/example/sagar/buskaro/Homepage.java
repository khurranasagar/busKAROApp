package com.example.sagar.buskaro;

import android.content.Intent;
import android.net.Uri;

import com.firebase.ui.auth.AuthUI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Modules.Users;
import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public CircleImageView dp1;
    private ImageView image;
    public TextView name;
    public TextView credlele;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbr;
    private GoogleApiClient client;
    private FirebaseAuth firebaseauth;
    TextView dusracredlele;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        firebaseauth = FirebaseAuth.getInstance();

        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.myLocation);
        FAB.setVisibility(View.GONE);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        writeNewUser();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        credlele = headerView.findViewById(R.id.credid);
        dbr = FirebaseDatabase.getInstance().getReference();
        Users us;
        FirebaseUser user = firebaseauth.getCurrentUser();
        DatabaseReference vc = dbr.child("users").child(user.getDisplayName());
        vc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long x =(long) dataSnapshot.child("buskaro_credits").getValue();
                credlele.setText(Long.toString(x) + " credits");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        name = headerView.findViewById(R.id.username2);
        dp1 = headerView.findViewById(R.id.dp);
        dp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3;
                intent3 = new Intent(view.getContext(),Account.class);
                startActivity(intent3);
            }
        });

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));

        image = (ImageView) findViewById(R.id.startDrawer);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        name.setText(firebaseUser.getDisplayName());
        Uri uri = firebaseUser.getPhotoUrl();
        String TAG="url";
        Log.d(TAG, String.valueOf(uri));
        // Setting profile pic
        Picasso.with(getApplicationContext())
                .load(uri)
                .error(android.R.drawable.sym_def_app_icon)
                .into(dp1);


//        android:drawable/ic_menu_mylocation

        mapFragment mapFragment = new mapFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout,mapFragment).commit();
    }

    private void writeNewUser() {
        dbr = FirebaseDatabase.getInstance().getReference();
        Users us = new Users();
        FirebaseUser user = firebaseauth.getCurrentUser();
        dbr.child("users").child(user.getDisplayName()).setValue(us);
        Log.d("User", "writeNewUser: " + user.getDisplayName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//        locationManager.requestLocationUpdates(provider,400,1,this);
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favorite) {
            Intent intent2 = new Intent(Homepage.this,Favorites.class);
            startActivity(intent2);

        }else if(id == R.id.nav_help) {
            Intent intent = new Intent(Homepage.this,Help.class);
            startActivity(intent);
        }else if(id == R.id.nav_feedback){
            Intent intent2 = new Intent(Homepage.this,feedbackpage.class);
            startActivity(intent2);
        }else if(id == R.id.nav_share){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "busKARO");
                String sAux = "\nPlease spread BBI event as widely as possible\n\n";
                sAux = sAux + "https://www.facebook.com/events/564177907285068/";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }

        }else if(id == R.id.nav_logout){

//            firebaseAuth.getInstance().signOut();
            AuthUI.getInstance()
                    .signOut(Homepage.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>(){

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            // do something here
                            startActivity(new Intent(Homepage.this,SignIn.class));
                            finish();

                        }
                    });

            }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
