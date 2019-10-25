package com.pandapanda.ifood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.pandapanda.ifood.R;

public class SplashActivity extends AppCompatActivity {

    Button sub;
    ImageView ballon;
    Animation frombottom, fromtop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sub = (Button) findViewById(R.id.sub);
        ballon = (ImageView) findViewById(R.id.ballon);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);



        sub.setAnimation(frombottom);
        ballon.setAnimation(fromtop);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirAutenticacao();
            }
        },2500);




    }

    private void abrirAutenticacao(){
        Intent i = new Intent(SplashActivity.this, AuthenticationActivity.class);
        startActivity(i);
        finish();
    }



}

