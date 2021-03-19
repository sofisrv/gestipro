package com.example.sofia.gestipro1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

public class miperfilr extends AppCompatActivity {
    Button breg;
    TextView tnombre,tnombrer, tap, tam, tcel, ttel, tcalle, tnumero, tinterior, tec, tyc, tcp, tcolonia, tciudad, tpais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfilr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tnombrer = (TextView) findViewById(R.id.nombrer);
        tnombre = (TextView) findViewById(R.id.producto);
        tap = (TextView) findViewById(R.id.ap);
        tam = (TextView) findViewById(R.id.am);
        tcel = (TextView) findViewById(R.id.cel);
        ttel = (TextView) findViewById(R.id.telefono);
        tcalle = (TextView) findViewById(R.id.calle);
        tnumero = (TextView) findViewById(R.id.numero);
        tinterior = (TextView) findViewById(R.id.interior);
        tec = (TextView) findViewById(R.id.ec);
        tyc = (TextView) findViewById(R.id.yc);
        tcp = (TextView) findViewById(R.id.cp);
        tcolonia = (TextView) findViewById(R.id.tcolonia);
        tciudad = (TextView) findViewById(R.id.tciudad);
        tpais = (TextView) findViewById(R.id.tpais);
        breg = (Button) findViewById(R.id.breg);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(miperfilr.this, restaurante.class);
                startActivity(intent);
                finish();
            }
        });
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        final String usu = mispreferencias.getString("usu","");
        Thread tr= new Thread(){
            @Override
            public void run() {
                final String resultado=enviardatosGET(usu);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtdatosJSON(resultado);
                        String nombrer="",nombre="", ap="", am="", calle="", ec="", yc="", colonia="";
                        String cel="", telefono="",  numero="", vi="", cp="", ciudad="", pais="";
                        if (r > 0) {
                            try {
                                JSONArray jsonarray = new JSONArray(resultado);
                                for(int i=0; i<jsonarray.length();i++){
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    nombrer=jsonobject.getString("nombrer");
                                    nombre=jsonobject.getString("nombre");
                                    ap=jsonobject.getString("ap");
                                    am=jsonobject.getString("am");
                                    calle=jsonobject.getString("calle");
                                    colonia=jsonobject.getString("colonia");
                                    ec=jsonobject.getString("ec");
                                    yc=jsonobject.getString("yc");
                                    cel=jsonobject.getString("cel");
                                    numero=jsonobject.getString("numero");
                                    vi=jsonobject.getString("interior");
                                    cp=jsonobject.getString("cp");
                                    telefono=jsonobject.getString("tel");
                                    ciudad=jsonobject.getString("ciudad");
                                    pais=jsonobject.getString("pais");

                                            tnombrer.setText(nombrer);
                                            tnombre.setText(nombre);
                                            tap.setText(ap);
                                            tam.setText(am);
                                            tcalle.setText(calle);
                                            tec.setText(ec);
                                            tyc.setText(yc);
                                            tcolonia.setText(colonia);
                                            tcel.setText(cel);
                                            tnumero.setText(numero);
                                            tcp.setText(cp);
                                            tinterior.setText(vi);
                                            ttel.setText(telefono);
                                            tciudad.setText(ciudad);
                                            tpais.setText(pais);
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
        tr.start();

    }

    public String enviardatosGET(String usuario) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/crest.php?usu="+usuario);
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

