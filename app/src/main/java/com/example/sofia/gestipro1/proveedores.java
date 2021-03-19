package com.example.sofia.gestipro1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class proveedores extends AppCompatActivity {

    Button bip, brp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);
        bip = (Button) findViewById(R.id.bip);
        brp = (Button) findViewById(R.id.brp);
        bip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(proveedores.this, loginp.class);
                startActivity(intent);

            }
        });
        brp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(proveedores.this, regp.class);
                startActivity(intent);
            }
        });
    }
}