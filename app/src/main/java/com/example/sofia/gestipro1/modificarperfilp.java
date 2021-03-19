package com.example.sofia.gestipro1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class modificarperfilp extends AppCompatActivity implements View.OnClickListener {
    Button b, breg;
    EditText enombre, eap, eam,ecel;
    TextView tnombre, tap, tam, tcel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificarperfilp);
        b = (Button) findViewById(R.id.bag);
        breg = (Button) findViewById(R.id.breg);
        enombre = (EditText) findViewById(R.id.enombre);
        eap = (EditText) findViewById(R.id.eap);
        eam = (EditText) findViewById(R.id.eam);
        ecel = (EditText) findViewById(R.id.ecel);

        tnombre = (TextView) findViewById(R.id.tnombre);
        tap = (TextView) findViewById(R.id.tap);
        tam = (TextView) findViewById(R.id.tam);
        tcel = (TextView) findViewById(R.id.tcel);
        preferencias();
        b.setOnClickListener(this);
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modificarperfilp.this, index.class);
                startActivity(intent);
                finish();
            }
        });
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        final String usu = mispreferencias.getString("usu","");
        Thread ta= new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                final String resultado=recibirdatosGET(usu);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtdatosJSON(resultado);
                        String nombre="", ap="", am="", pas="";
                        String cel="";
                        if (r > 0) {
                            try {
                                JSONArray jsonarray = new JSONArray(resultado);
                                for(int i=0; i<jsonarray.length();i++){
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    nombre=jsonobject.getString("nombre");
                                    ap=jsonobject.getString("ap");
                                    am=jsonobject.getString("am");
                                    cel=jsonobject.getString("cel");
                                    enombre.setText(nombre);
                                    eap.setText(ap);
                                    eam.setText(am);
                                    ecel.setText(cel);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "no se pudo", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "no existe", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        ta.start();
    }
    @Override
    public void onClick(View v) {
        Thread tr= new Thread(){
            @Override
            public void run() {
                SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                final String usu = mispreferencias.getString("usu","");
                final String resultado=enviardatos(usu,enombre.getText().toString(),eap.getText().toString(),eam.getText().toString(),ecel.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtdatosJSON(resultado);
                                if (enombre.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Falta nombre", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    if (eap.getText().toString().isEmpty()){
                                        Toast.makeText(getApplicationContext(), "Falta apellido paterno", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        if (eam.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Falta apellido materno", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            if (ecel.getText().toString().isEmpty()){
                                                Toast.makeText(getApplicationContext(), "Falta celular", Toast.LENGTH_LONG).show();
                                            }
                                            else{

                                                    Intent i = new Intent(getApplicationContext(), index.class);
                                                    startActivity(i);
                                                    finish();
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



    public String recibirdatosGET(String usuario) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/cprov.php?usu="+usuario);
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
    public String enviardatos(String usuario, String nombre, String ap, String am, String cel ) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/modprov.php?usu="+usuario+"&nombre="+nombre+"&ap="+ap+"&am="+am+"&cel="+cel);
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
    }

}