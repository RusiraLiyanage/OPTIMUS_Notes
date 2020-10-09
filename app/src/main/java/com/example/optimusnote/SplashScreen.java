package com.example.optimusnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.optimusnote.activities.PinCodeView;



public class SplashScreen extends AppCompatActivity {
    Animation topAnim,bottomAnim;
    ImageView image;
    TextView v1,v2,v3,v4;
    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image = findViewById(R.id.imageView4);
        v1 = findViewById(R.id.textView6);
        v2 = findViewById(R.id.textView8);
        v3 = findViewById(R.id.textView7);
        v4 = findViewById(R.id.textView9);

        image.setAnimation(topAnim);
        v1.setAnimation(bottomAnim);
        v2.setAnimation(bottomAnim);
        v3.setAnimation(bottomAnim);
        v4.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, PinCodeView.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);


    }
}