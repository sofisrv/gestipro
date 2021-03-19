package com.example.sofia.gestipro1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;



public class nbodega extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button b, binfo, breg;
    EditText cr, ci;
    private Spinner s1;
    // array para listar las productos
    private ArrayList<productos> plist;
    ProgressDialog pDialog;
    private String URL_LISTA_p = "http://192.168.1.13/spinner/listar.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nbodega);
        breg = (Button) findViewById(R.id.br);
        b = (Button) findViewById(R.id.ba);
        binfo = (Button) findViewById(R.id.binfo);
        cr = (EditText) findViewById(R.id.cxe);
        ci = (EditText) findViewById(R.id.pxum);
        s1 = (Spinner) findViewById(R.id.s1);
        plist = new ArrayList<productos>();
        s1.setOnItemSelectedListener(this);
        new Getproductos().execute();
        b.setOnClickListener(this);


        binfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(nbodega.this, rb2.class);
                startActivity(intent);
                finish();

            }
        });
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nbodega.this, restaurante.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < plist.size(); i++) {
            lables.add(plist.get(i).getnombre());
        }


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(spinnerAdapter);
    }


    private class Getproductos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(nbodega.this);
            pDialog.setMessage("Buscando productos");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_LISTA_p, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray producto = jsonObj.getJSONArray("producto");

                        for (int i = 0; i < producto.length(); i++) {
                            JSONObject catObj = (JSONObject) producto.get(i);
                            productos cat = new productos(catObj.getString("um"),catObj.getString("nombre"), catObj.getString("categoria"));
                            plist.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "¿No ha recibido ningún dato desde el servidor!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinner();
        }

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onClick(View v) {
        Thread tr= new Thread(){
            @Override
            public void run() {


                final String resultado=enviardatosGET(s1.getSelectedItem().toString(), cr.getText().toString(),ci.getText().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r1 = obtdatosJSON(resultado);
                        if (ci.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Falta cantidad por envase", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if (s1.getSelectedItem().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "Falta seleccionar producto", Toast.LENGTH_LONG).show();
                            }
                            else{
                                if (cr.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Falta precio por unidad", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    if  (r1 > 0) {
                                        Toast.makeText(getApplicationContext(), "Ya existe", Toast.LENGTH_LONG).show();

                                    } else {
                                        Intent i = new Intent(getApplicationContext(), restaurante.class);
                                        Toast.makeText(getApplicationContext(), "Producto agregado a mi bodega", Toast.LENGTH_LONG).show();
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
    public String enviardatosGET(String nproducto, String cr, String ci) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
            String usu= mispreferencias.getString("usu","");
            url= new URL("http:192.168.1.13/webservice/regbodega.php?usurest="+usu+"&nproducto="+nproducto+"&ideal="+ci+"&real="+cr+"");
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

