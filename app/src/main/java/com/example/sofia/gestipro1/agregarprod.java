package com.example.sofia.gestipro1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class agregarprod extends AppCompatActivity implements View.OnClickListener {
    Button b, binfo, breg;
    EditText np;
    Spinner s1,s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_agregarprod);
        breg = (Button) findViewById(R.id.breg);
        b = (Button) findViewById(R.id.b);
        binfo = (Button) findViewById(R.id.binfo);
        np = (EditText) findViewById(R.id.np);
        s1=(Spinner) findViewById(R.id.s1);
        s2=(Spinner) findViewById(R.id.s2);
        b.setOnClickListener(this);
        breg = (Button) findViewById(R.id.breg);
        binfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(agregarprod.this, rb1.class);
                startActivity(intent);

            }
        });
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                final int tipo = mispreferencias.getInt("tipo",0);
                if(tipo==1){
                    Intent intent = new Intent(agregarprod.this, restaurante.class);
                    startActivity(intent);
                }else if(tipo==2){
                    Intent intent = new Intent(agregarprod.this, index.class);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        Thread tr= new Thread(){
            @Override
            public void run() {
                final String resultado=enviardatosGET(np.getText().toString(),s1.getSelectedItem().toString(),s2.getSelectedItem().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r1 = obtdatosJSON(resultado);
                        if (np.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Falta nombre de producto", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if (s1.getSelectedItem().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Falta um", Toast.LENGTH_LONG).show();
                            }
                            else{
                                if (s2.getSelectedItem().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Falta nombre", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    if  (r1 > 0) {
                                                    Toast.makeText(getApplicationContext(), "Ya existe", Toast.LENGTH_LONG).show();

                                    } else {
                                        Intent i = new Intent(getApplicationContext(), agregarprod.class);
                                        Toast.makeText(getApplicationContext(), "Producto agregado", Toast.LENGTH_LONG).show();
                                        i.putExtra("cod", np.getText().toString());
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

    public String enviardatosGET(String np, String s1, String s2) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/regproducto.php?nombre="+np+"&um="+s1+"&categoria="+s2);
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
}

