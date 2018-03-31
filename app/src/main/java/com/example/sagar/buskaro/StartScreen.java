package com.example.sagar.buskaro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartScreen extends AppCompatActivity {

    private boolean firstLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

//        SharedPreferences pref = getSharedPreferences("com.example.sagar.buskaro", MODE_PRIVATE);
//        firstLaunch = pref.getBoolean("firstrun", true);
//
//        if (firstLaunch) {
//
//        } else {
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putBoolean("firstTime", false);
//            editor.commit();
//            Intent i = new Intent(this, SignIn.class);
//            startActivity(i);
//        }


        Button startbutton=(Button)findViewById(R.id.startbutton);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            Intent intent = new Intent(StartScreen.this,Homepage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }


}