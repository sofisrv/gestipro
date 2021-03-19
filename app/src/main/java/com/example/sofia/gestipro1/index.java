package com.example.sofia.gestipro1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Notificacion que ya mero sale atte sofi :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView usuvar;
        usuvar= (TextView) findViewById(R.id.usuvar);
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String usu;
        usuvar.setText(mispreferencias.getString("usu",""));
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cs) {
            SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =mispreferencias.edit();
            int sesion, tipo;
            String usu;
            sesion=0;
            tipo=0;
            usu= "";
            editor.putString("usu",usu);
            editor.putInt("tipo",tipo);
            editor.putInt("sesion",sesion);
            editor.commit();
            Intent i = new Intent(getApplicationContext(),uno.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.miperfilp) {
            Intent i = new Intent(getApplicationContext(),miperfilp.class);
            startActivity(i);
        } else if (id == R.id.modificarperfilp) {
            Intent i = new Intent(getApplicationContext(),modificarperfilp.class);
            startActivity(i);
        }else if (id == R.id.verproductos) {
            Intent i = new Intent(getApplicationContext(),vp.class);
            startActivity(i);
        }else if (id == R.id.agregarproductos) {
            Intent i = new Intent(getApplicationContext(),agregarprod.class);
            startActivity(i);
        }else if (id == R.id.oferta) {
            Intent i = new Intent(getApplicationContext(),oferta.class);
            startActivity(i);
        }else if (id == R.id.agregaroferta) {
            Intent i = new Intent(getApplicationContext(),noferta.class);
            startActivity(i);
        }else if (id == R.id.convenio) {
            Intent i = new Intent(getApplicationContext(),miperfilp.class);
            startActivity(i);
        }else if (id == R.id.nconvenio) {
            Intent i = new Intent(getApplicationContext(),modificarperfilp.class);
            startActivity(i);
        }else if (id == R.id.ajustes) {
            Intent i = new Intent(getApplicationContext(),modificarperfilp.class);
            startActivity(i);
        }else if (id == R.id.ad) {
            Intent i = new Intent(getApplicationContext(), ad.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
