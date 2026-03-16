package com.example.rideledger.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rideledger.R;

public class SplashScreen extends AppCompatActivity {
    ImageView logo;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.appName);

        Animation fade = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        logo.startAnimation(fade);
        appName.startAnimation(fade);

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
            startActivity(intent);
            finish();

        },1000);


    }
}