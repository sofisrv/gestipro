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

public class modificarperfilr extends AppCompatActivity implements View.OnClickListener {
    Button b, breg;
    EditText enombre, eap, eam,ecel, ecalle, enumero,eint, eec, eyc, ecp, ecol, etel, eciudad, epais, enombrer;
    TextView tnombre, tap, tam, tcel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_modificarperfilr);
        b = (Button) findViewById(R.id.bag);
        breg = (Button) findViewById(R.id.breg);
        enombre = (EditText) findViewById(R.id.enombre);
        enombrer = (EditText) findViewById(R.id.enombrer);
        eap = (EditText) findViewById(R.id.eap);
        eam = (EditText) findViewById(R.id.eam);
        ecel = (EditText) findViewById(R.id.ecel);

        ecalle = (EditText) findViewById(R.id.ecalle);
        enumero = (EditText) findViewById(R.id.enumero);
        eint = (EditText) findViewById(R.id.eint);
        eec = (EditText) findViewById(R.id.eec);
        eyc = (EditText) findViewById(R.id.eyc);
        ecp = (EditText) findViewById(R.id.ecp);
        ecol = (EditText) findViewById(R.id.ecol);
        etel = (EditText) findViewById(R.id.etel);
        eciudad = (EditText) findViewById(R.id.eciudad);
        epais = (EditText) findViewById(R.id.epais);

        tnombre = (TextView) findViewById(R.id.tnombre);
        tap = (TextView) findViewById(R.id.tap);
        tam = (TextView) findViewById(R.id.tam);
        tcel = (TextView) findViewById(R.id.tcel);
        preferencias();
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(modificarperfilr.this, restaurante.class);
                startActivity(intent);
                finish();
            }
        });
        b.setOnClickListener(this);
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
                        String nombrer="", nombre="", ap="", am="", calle="",numero="", interior="", ec="", yc="",cp="", colonia="", tel="", ciudad="", pais="";
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
                                    nombrer=jsonobject.getString("nombrer");

                                    calle=jsonobject.getString("calle");
                                    numero=jsonobject.getString("numero");
                                    interior=jsonobject.getString("interior");
                                    ec=jsonobject.getString("ec");
                                    yc=jsonobject.getString("yc");
                                    cp=jsonobject.getString("cp");
                                    colonia=jsonobject.getString("colonia");
                                    tel=jsonobject.getString("tel");
                                    ciudad=jsonobject.getString("ciudad");
                                    pais=jsonobject.getString("pais");

                                    ecalle.setText(calle);
                                    enumero.setText(numero);
                                    eint.setText(interior);
                                    eec.setText(ec);
                                    eyc.setText(yc);
                                    ecp.setText(cp);
                                    ecol.setText(colonia);
                                    etel.setText(tel);
                                    eciudad.setText(ciudad);
                                    epais.setText(pais);

                                    enombre.setText(nombre);
                                    enombrer.setText(nombrer);
                                    eam.setText(ap);
                                    eap.setText(am);
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
                final String resultado=enviardatos(usu,etel.getText().toString(),enombrer.getText().toString(),epais.getText().toString(),eciudad.getText().toString(),ecalle.getText().toString(),enumero.getText().toString(),eint.getText().toString(),eec.getText().toString(),eyc.getText().toString(),ecol.getText().toString(),ecp.getText().toString(),enombre.getText().toString(),eap.getText().toString(),eam.getText().toString(),ecel.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtdatosJSON(resultado);
                        if (enombre.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Falta nombre", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if (etel.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Falta telefono", Toast.LENGTH_LONG).show();
                            }
                            else{
                                if (enombrer.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Falta nombre del restaurante", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    if (epais.getText().toString().isEmpty()){
                                        Toast.makeText(getApplicationContext(), "Falta pais", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        if (eciudad.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Falta ciudad", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            if (ecalle.getText().toString().isEmpty()){
                                                Toast.makeText(getApplicationContext(), "Falta calle", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                if (enumero.getText().toString().isEmpty()){
                                                    Toast.makeText(getApplicationContext(), "Falta numero", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    if (eint.getText().toString().isEmpty()){
                                                        Toast.makeText(getApplicationContext(), "Falta interior", Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        if (eec.getText().toString().isEmpty()){
                                                            Toast.makeText(getApplicationContext(), "Falta una calle adyacente", Toast.LENGTH_LONG).show();
                                                        }
                                                        else{
                                                            if (eyc.getText().toString().isEmpty()){
                                                                Toast.makeText(getApplicationContext(), "Falta una calle adyacente", Toast.LENGTH_LONG).show();
                                                            }
                                                            else{
                                                                if (ecol.getText().toString().isEmpty()){
                                                                    Toast.makeText(getApplicationContext(), "Falta colonia", Toast.LENGTH_LONG).show();
                                                                }
                                                                else{
                                                                    if (ecp.getText().toString().isEmpty()){
                                                                        Toast.makeText(getApplicationContext(), "Falta codigo postal", Toast.LENGTH_LONG).show();
                                                                    }
                                                                    else{
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
                                                                                        if (r > 0) {
                                                                                            Toast.makeText(getApplicationContext(), "Ya existe", Toast.LENGTH_LONG).show();

                                                                                        } else {
                                                                                            Intent i = new Intent(getApplicationContext(), restaurante.class);
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


                                                        }


                                                    }

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



    public String recibirdatosGET(String usuario) {
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
    public String enviardatos(String usuario, String tel, String nombrer, String pais, String ciudad, String calle, String numero, String interior, String ec, String yc, String colonia, String cp, String nombre, String ap, String am, String cel) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/modrest.php?usu="+usuario+"&tel="+tel+"&nombrer="+nombrer+"&pais="+pais+"&ciudad="+ciudad+"&calle="+calle+"&numero="+numero+"&interior="+interior+"&ec="+ec+"&yc="+yc+"&colonia="+colonia+"&cp="+cp+"&nombre="+nombre+"&ap="+ap+"&am="+am+"&cel="+cel);

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