package com.example.sofia.gestipro1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

public class nconvenio extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button b, breg;
    private Spinner s1;
    // array para listar las productos
    private ArrayList<objpb> plist;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nconvenio);
        breg = (Button) findViewById(R.id.breg);
        b = (Button) findViewById(R.id.ba);
        s1 = (Spinner) findViewById(R.id.s1);
        plist = new ArrayList<objpb>();
        s1.setOnItemSelectedListener(this);
        new nconvenio.Getproductos().execute();
        b.setOnClickListener(this);

        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =mispreferencias.edit();
        String c="nada";
        editor.putString("pb",c);
        editor.commit();


        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nconvenio.this, restaurante.class);
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
            pDialog = new ProgressDialog(nconvenio.this);
            pDialog.setMessage("Buscando productos");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
            String usu= mispreferencias.getString("usu","");
            String json = jsonParser.makeServiceCall("http://192.168.1.13/webservice/cpb.php?usu="+usu+"", ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray producto = jsonObj.getJSONArray("producto");

                        for (int i = 0; i < producto.length(); i++) {
                            JSONObject catObj = (JSONObject) producto.get(i);
                            objpb cat = new objpb(catObj.getString("nombrep"));
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor =mispreferencias.edit();
                        String c=(s1.getSelectedItem().toString());
                        editor.putString("pb",c);
                        editor.commit();
                        Intent intent = new Intent(nconvenio.this, aconvenio.class);
                        startActivity(intent);
                    }
                });
            }
        };
        tr.start();
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





