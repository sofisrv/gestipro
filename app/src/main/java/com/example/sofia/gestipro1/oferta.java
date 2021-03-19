package com.example.sofia.gestipro1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

public class oferta extends AppCompatActivity
{
    ArrayList producto= new ArrayList();
    ArrayList pxum = new ArrayList();
    ArrayList cxe = new ArrayList();
    ArrayList marca = new ArrayList();
    String id_d = "";
    Button breg;
    ListView listView;
    Context context = this;
    boolean connectionEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oferta);

        listView = (ListView) findViewById(R.id.list2);
        breg = (Button) findViewById(R.id.breg);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        ActualizarServicios();
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(oferta.this, index.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        if(connectionEnabled)
        {
            ActualizarServicios();
        }
        else
        {
            Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
        }
    }

    public String enviarDatosGet(String usu)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            //Se establece el url
            url = new URL("http://192.168.1.13/webservice/coferta.php?usu="+usu);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();
            resul = new StringBuilder();
            if(respuesta == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while((linea = reader.readLine()) != null)
                {
                    resul.append(linea);
                }
            }
        }
        catch (Exception e)
        {

        }
        return resul.toString();
    }

    public int objDatosJSON(String response)
    {
        int res =0;
        try
        {
            JSONArray json =    new JSONArray(response);
            if(json.length() > 0)
            {
                res = 1;
            }
        }
        catch(Exception e)
        {

        }
        return res;
    }

    private void ActualizarServicios()
    {
        producto.clear();
        pxum.clear();
        cxe.clear();
        marca.clear();

        final ProgressDialog dialogo = ProgressDialog.show(this, "Actualizando ofertas", "Por favor espera un momento...", true);
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                String usu= mispreferencias.getString("usu","");
                final String resultado = enviarDatosGet(usu);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int r=objDatosJSON(resultado);
                        if(r > 0)
                        {
                            dialogo.dismiss();
                            try
                            {
                                JSONArray jsonarray = new JSONArray(resultado);
                                for (int i = 0; i < jsonarray.length(); i++)
                                {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    producto.add(jsonobject.getString("pkproducto"));
                                    pxum.add(jsonobject.getString("pxum"));
                                    cxe.add(jsonobject.getString("cxe"));
                                    marca.add(jsonobject.getString("marca"));
                                }
                                listView.setAdapter(new oferta.ServicioAdapter(getApplicationContext()));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            dialogo.dismiss();
                            Toast toast1 = Toast.makeText(getApplicationContext(), "No hay ofertas nuevas", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    private class ServicioAdapter extends BaseAdapter
    {
        Context ctx;
        LayoutInflater layoutInflater;
        TextView prod,px,cx,mar;
        ImageView icono;

        public ServicioAdapter(Context applicationContext)
        {
            this.ctx= applicationContext;
            layoutInflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return producto.size();
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @RequiresApi(api= Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            final ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.ofertita,null);

            prod = (TextView)viewGroup.findViewById(R.id.producto);
            px = (TextView)viewGroup.findViewById(R.id.pxum);
            cx = (TextView)viewGroup.findViewById(R.id.cxe);
            mar = (TextView)viewGroup.findViewById(R.id.marca);
            prod.setText(producto.get(position).toString());
            px.setText(pxum.get(position).toString());
            cx.setText(cxe.get(position).toString());
            mar.setText(marca.get(position).toString());
            return viewGroup;
        }
    }
}