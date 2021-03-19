package com.example.sofia.gestipro1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class loginp extends AppCompatActivity implements View.OnClickListener{
    Button b;
    EditText txtusu, txtpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginp);
        b = (Button) findViewById(R.id.breg);
        txtusu = (EditText) findViewById(R.id.usuvar);
        txtpass = (EditText) findViewById(R.id.txtpas);
        preferencias();
        b.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Thread tr= new Thread(){
            @Override
            public void run() {
                final String resultado=enviardatosGET(txtusu.getText().toString(),txtpass.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtdatosJSON(resultado);
                        if (r > 0) {
                            Intent i = new Intent(getApplicationContext(), index.class);
                            gpreferencias();
                            vpreferencias();
                            i.putExtra("cod", txtusu.getText().toString());


                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    public String enviardatosGET(String usu, String pas) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/lip.php?usu="+usu+"&pas="+pas);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            respuesta=connection.getResponseCode();

            result=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(connection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine())!=null){
                    result.append(linea);
                }
            }

        } catch (Exception e) {}
        return result.toString();
    }

    public int obtdatosJSON (String response){
        int res=0;
        try{
            JSONArray json=new JSONArray(response);
            if(json.length()>0){
                res=1;
            }
        }catch (Exception e){}
        return res;
    }

    int sesion, tipo;

    public void preferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        sesion=0;
        tipo=0;
        txtusu.setText(mispreferencias.getString("usu",""));
    }

    public void gpreferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =mispreferencias.edit();
        sesion=1;
        tipo=2;
        String usu= txtusu.getText().toString();
        String pas= txtpass.getText().toString();
        editor.putString("usu",usu);
        editor.putInt("tipo",tipo);
        editor.putInt("sesion",sesion);
        editor.commit();
    }
    public void vpreferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        int sesion = mispreferencias.getInt("sesion",0);
        int tipo = mispreferencias.getInt("tipo",0);
        int pk = mispreferencias.getInt("pk",0);
        String usu= mispreferencias.getString("usu","");
    }



    public String ed(String usu) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/cpkp.php?usu="+usu);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            respuesta=connection.getResponseCode();

            result=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(connection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                while ((linea=reader.readLine())!=null){
                    result.append(linea);
                }
            }
        } catch (Exception e) {}
        return result.toString();
    }

}
