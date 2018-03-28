package com.example.sagar.buskaro;

import android.content.Intent;
import android.os.Bundle;
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

public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.myLocation);
        FAB.setVisibility(View.GONE);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        image = (ImageView) findViewById(R.id.startDrawer);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
//        android:drawable/ic_menu_mylocation

        mapFragment mapFragment = new mapFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout,mapFragment).commit();
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

        }else if(id == R.id.nav_feedback){
            Intent intent = new Intent(Homepage.this,feedbackpage.class);
            startActivity(intent);
        }else if(id == R.id.nav_share){

        }else if(id == R.id.nav_logout){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ClickSearchBox(View view) {
        Intent intent=new Intent(this,Destinations.class);
        /*Log.d(" ket in homescreen", "ClickSearchBox: " + key);
        intent.putExtra("EXTRA_SESSION_ID", key);*/
        startActivity(intent);
    }

    public void destbutton(View view) {
        Intent intent=new Intent(this,Destinations.class);
        startActivity(intent);
    }
}
