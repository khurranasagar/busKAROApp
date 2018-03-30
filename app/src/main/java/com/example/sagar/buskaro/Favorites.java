package com.example.sagar.buskaro;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity implements View.OnClickListener{
    private Boolean isFabOpen = false;
    List<FavBusNo> buslist;
    List<FavDest> destList;
    final Context context = this;
    RecyclerView recyclerView,recyclerView2;
    private FloatingActionButton fab,fab1,fab2;
    TextView dest,busno;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.fav_rv1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2 = (RecyclerView) findViewById(R.id.fav_rv2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        buslist = new ArrayList<>();
        buslist.add(new FavBusNo("511A","Dhaula Kuan"));
        buslist.add(new FavBusNo("611A","Kailash Colony"));
        buslist.add(new FavBusNo("511A","Dhaula Kuan"));
        buslist.add(new FavBusNo("611A","Kailash Colony"));
        buslist.add(new FavBusNo("511A","Dhaula Kuan"));
        buslist.add(new FavBusNo("611A","Kailash Colony"));
        FavBusnoAdapter adapter = new FavBusnoAdapter(this, buslist);

        recyclerView.setAdapter(adapter);

        destList = new ArrayList<>();
        destList.add(new FavDest("Dhaula Kuan"));
        destList.add(new FavDest("Kailash Colony"));
        destList.add(new FavDest("Dhaula Kuan"));
        destList.add(new FavDest("Kailash Colony"));
        destList.add(new FavDest("Dhaula Kuan"));
        destList.add(new FavDest("Kailash Colony"));
        FavDestAdapter adapter2 = new FavDestAdapter(this, destList);

        recyclerView2.setAdapter(adapter2);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        dest=findViewById(R.id.desttext);
        busno=findViewById(R.id.busnotext);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts2, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput2);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                break;

            case R.id.fab2:

                LayoutInflater li2 = LayoutInflater.from(context);
                View promptsView2 = li2.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder2.setView(promptsView2);

                final EditText userInput2 = (EditText) promptsView2
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder2
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                                                            }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog2 = alertDialogBuilder2.create();

                // show it
                alertDialog2.show();


                break;
        }
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            dest.startAnimation(fab_close);
            busno.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Favorites", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            dest.startAnimation(fab_open);
            busno.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Favorites","open");

        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}