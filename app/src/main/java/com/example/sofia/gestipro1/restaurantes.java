package com.example.sofia.gestipro1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class restaurantes extends AppCompatActivity {

    Button bir, brr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantes);
        bir = (Button) findViewById(R.id.bir);
        brr = (Button) findViewById(R.id.brr);
        bir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(restaurantes.this, loginr.class);
                startActivity(intent);

            }
        });
        brr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(restaurantes.this, regr.class);
                startActivity(intent);

            }
        });
    }
}