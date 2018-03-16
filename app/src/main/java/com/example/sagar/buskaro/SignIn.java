package com.example.sagar.buskaro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.SignInButton;

public class SignIn extends AppCompatActivity {
    SignInButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        button = (SignInButton) findViewById(R.id.googleBtn);
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/

    }
}
