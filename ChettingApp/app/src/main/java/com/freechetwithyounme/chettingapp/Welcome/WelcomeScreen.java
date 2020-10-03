package com.freechetwithyounme.chettingapp.Welcome;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.freechetwithyounme.chettingapp.LogIn;
import com.freechetwithyounme.chettingapp.MainActivity;
import com.freechetwithyounme.chettingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreen extends AppCompatActivity {
    private String TAG= "WelcomeScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int SPLASH_TIME = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser!= null){
                    Intent intent= new Intent(WelcomeScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(WelcomeScreen.this, LogIn.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME);
    }
}
