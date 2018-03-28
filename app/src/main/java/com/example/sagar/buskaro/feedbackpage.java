package com.example.sagar.buskaro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

public class feedbackpage extends AppCompatActivity {

    public RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackpage);

        // Initialize RatingBar
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

    }

    /**
     * Display rating by calling getRating() method.
     * @param view
     */
    public void rateMe(View view){
        Toast.makeText(getApplicationContext(),
                String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
        Intent home = new Intent(feedbackpage.this,Homepage.class);
        startActivity(home);
        finish();
    }
}


