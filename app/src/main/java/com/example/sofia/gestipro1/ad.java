package com.example.sofia.gestipro1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ad extends AppCompatActivity {

    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ok = (Button) findViewById(R.id.br);
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        final String usu = mispreferencias.getString("usu","");
        final int sesion = mispreferencias.getInt("sesion",0);
        final int tipo = mispreferencias.getInt("tipo",0);
        final int pk = mispreferencias.getInt("pk",0);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sesion==0){
                    Intent intent = new Intent(ad.this, splashscreen.class);
                    startActivity(intent);
                }else{

                    if (tipo==0){
                        Intent intent = new Intent(ad.this, splashscreen.class);
                        startActivity(intent);
                    }else if(tipo==1){
                        Intent intent = new Intent(ad.this, restaurante.class);
                        startActivity(intent);
                    }else if(tipo==2){
                        Intent intent = new Intent(ad.this, index.class);
                        startActivity(intent);
                    }
                }
                finish();



    }
        });
}}