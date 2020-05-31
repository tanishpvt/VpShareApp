package com.example.vpshareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class LoadingScreen extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar=findViewById(R.id.progress_bar);
        textView=findViewById(R.id.text_view);
        LottieAnimationView slideImageView=(LottieAnimationView)findViewById(R.id.img);
        slideImageView.setSpeed(0.6f);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        progressBarAnimation();
    }

    private void progressBarAnimation() {
    ProgressBarAnimation progressBarAnimation=new ProgressBarAnimation(this,progressBar,textView,0f,100f);
    progressBarAnimation.setDuration(6000);
    progressBar.setAnimation(progressBarAnimation);

    }
}
