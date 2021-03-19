package com.example.sofia.gestipro1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class uno extends AppCompatActivity {
    ImageButton btnr;
    ImageButton btnp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uno);
        btnr = (ImageButton) findViewById(R.id.btnr);
        btnp = (ImageButton) findViewById(R.id.btnp);
        btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(uno.this, restaurantes.class);
                startActivity(intent);

            }
        });
        btnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(uno.this, proveedores.class);
                startActivity(intent);

            }
        });
    }

}