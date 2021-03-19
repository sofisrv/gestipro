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

public class regp extends AppCompatActivity implements View.OnClickListener {
    Button b;
    EditText usuario, contrasena,nombre, ap, am,cel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regp);
        b = (Button) findViewById(R.id.breg);
        usuario = (EditText) findViewById(R.id.usuario);
        contrasena = (EditText) findViewById(R.id.contrasena);
        nombre = (EditText) findViewById(R.id.producto);
        ap = (EditText) findViewById(R.id.ap);
        am = (EditText) findViewById(R.id.am);
        cel = (EditText) findViewById(R.id.cel);
        preferencias();
        b.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Thread tr= new Thread(){
            @Override
            public void run() {
                final String resultado=enviardatosGET(usuario.getText().toString(),contrasena.getText().toString(),nombre.getText().toString(),ap.getText().toString(),am.getText().toString(),cel.getText().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtdatosJSON(resultado);
                        if (usuario.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Falta usuario", Toast.LENGTH_LONG).show();
                        }
                        else{
                                if (contrasena.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Falta contraseña", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    if (nombre.getText().toString().isEmpty()){
                                        Toast.makeText(getApplicationContext(), "Falta nombre", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        if (ap.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Falta apellido paterno", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            if (am.getText().toString().isEmpty()){
                                                Toast.makeText(getApplicationContext(), "Falta apellido materno", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                if (cel.getText().toString().isEmpty()){
                                                    Toast.makeText(getApplicationContext(), "Falta celular", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    if  (r > 0) {
                                                        Toast.makeText(getApplicationContext(), "Ya existe", Toast.LENGTH_LONG).show();

                                                    } else {
                                                        Intent i = new Intent(getApplicationContext(), index.class);
                                                        gpreferencias();
                                                        vpreferencias();
                                                        i.putExtra("cod", usuario.getText().toString());
                                                        startActivity(i);
                                                        finish();
                                                    }

                                                }

                                            }


                                        }

                                    }

                                }

                        }







                    }
                });
            }
        };
        tr.start();
    }

    public String enviardatosGET(String usuario, String contrasena,String nombre, String ap, String am, String cel ) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/regprov.php?usu="+usuario+"&pas="+contrasena+"&nombre="+nombre+"&ap="+ap+"&am="+am+"&cel="+cel);
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
        usuario.setText(mispreferencias.getString("usu",""));
    }
    public void gpreferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =mispreferencias.edit();
        sesion=1;
        tipo=2;
        String usu= usuario.getText().toString();
        String pas= contrasena.getText().toString();
        editor.putString("usu",usu);
        editor.putInt("tipo",tipo);
        editor.putInt("sesion",sesion);
        editor.commit();
    }
    public void vpreferencias(){
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        int sesion = mispreferencias.getInt("sesion",0);
        int tipo = mispreferencias.getInt("tipo",0);
        String usu= mispreferencias.getString("usu","");
    }
}