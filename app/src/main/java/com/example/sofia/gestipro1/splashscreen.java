package com.example.sofia.gestipro1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
public class splashscreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        final String usu = mispreferencias.getString("usu","");
        final int sesion = mispreferencias.getInt("sesion",0);
        final int tipo = mispreferencias.getInt("tipo",0);
        final int pk = mispreferencias.getInt("pk",0);
        if(sesion!=0){
            Toast.makeText(getApplicationContext(), ""+usu, Toast.LENGTH_LONG).show();}

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sesion==0){
                    Intent intent = new Intent(splashscreen.this, uno.class);
                    startActivity(intent);
                }else{

                    if (tipo==0){
                        Intent intent = new Intent(splashscreen.this, uno.class);
                        startActivity(intent);
                    }else if(tipo==1){
                        Intent intent = new Intent(splashscreen.this, restaurante.class);
                        startActivity(intent);
                    }else if(tipo==2){
                        Intent intent = new Intent(splashscreen.this, index.class);
                        startActivity(intent);
                    }
                }
                finish();
            }
        },4000);
    }
}
