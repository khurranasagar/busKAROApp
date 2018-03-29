package com.example.sagar.buskaro;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(R.string.account_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView sname= findViewById(R.id.textView21);
        TextView email= findViewById(R.id.textView23);
        TextView fname= findViewById(R.id.textView13);
        final TextView credit= findViewById(R.id.textView22);
        CircleImageView dp=findViewById(R.id.imageView13);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Uri uri = firebaseUser.getPhotoUrl();
        String str = firebaseUser.getDisplayName();
        String[] splitStr = str.split("\\s+");
        fname.setText(splitStr[0]);
        sname.setText(splitStr[1]);
        email.setText(firebaseUser.getEmail());
        String TAG="url";
        Log.d(TAG, String.valueOf(uri));
        // Setting profile pic
        Picasso.with(getApplicationContext())
                .load(uri)
                .error(android.R.drawable.sym_def_app_icon)
                .into(dp);
    }




    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
