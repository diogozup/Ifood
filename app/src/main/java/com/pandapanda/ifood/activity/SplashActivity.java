package com.pandapanda.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pandapanda.ifood.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirAutenticacao();
            }
        },1500);

    }
    private void abrirAutenticacao(){
        Intent i = new Intent(SplashActivity.this, AuthenticationActivity.class);
        startActivity(i);
        finish();
    }



}

