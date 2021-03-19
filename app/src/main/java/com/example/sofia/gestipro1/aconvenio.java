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
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class aconvenio extends AppCompatActivity
{
    ArrayList pkproveedor= new ArrayList();
    ArrayList pxum = new ArrayList();
    ArrayList marca = new ArrayList();
    ArrayList pko = new ArrayList();
    ArrayList pkd = new ArrayList();
    Button breg;
    ListView listView;
    Context context = this;
    boolean connectionEnabled;
    TextView prod;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aconvenio);
        prod = (TextView) findViewById(R.id.Producto);
        listView = (ListView) findViewById(R.id.list2);
        breg = (Button) findViewById(R.id.breg);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        ActualizarServicios();
        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        final String pb = mispreferencias.getString("pb","");
        prod.setText(pb);
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(aconvenio.this, nconvenio.class);
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

    public String enviarDatosGet()
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;

        //Se establece el url
        try
        {
            SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
            String usu= mispreferencias.getString("usu","");
            String pb= mispreferencias.getString("pb","");

            url = new URL("http://192.168.1.13/webservice/cconvenios.php?usu="+usu+"&prod="+pb+"");
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
        pkproveedor.clear();
        pxum.clear();
        marca.clear();
        pko.clear();
        pkd.clear();
        final ProgressDialog dialogo = ProgressDialog.show(this, "Actualizando ofertas", "Por favor espera un momento...", true);
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                String usu= mispreferencias.getString("usu","");
                final String resultado = enviarDatosGet();
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
                                    pkproveedor.add(jsonobject.getString("pkproveedor"));
                                    pxum.add(jsonobject.getString("pxum"));
                                    marca.add(jsonobject.getString("marca"));
                                    pko.add(jsonobject.getString("pkoferta"));
                                    pkd.add(jsonobject.getString("pkbodega"));
                                }
                                listView.setAdapter(new aconvenio.ServicioAdapter(getApplicationContext()));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            dialogo.dismiss();
                            Toast toast1 = Toast.makeText(getApplicationContext(), "No hay ofertas de los productos que solicitas", Toast.LENGTH_SHORT);
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
        TextView pkp,px,mar;
        Button bs;

        public ServicioAdapter(Context applicationContext)
        {
            this.ctx= applicationContext;
            layoutInflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return pkproveedor.size();
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

            final ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.ac,null);

            bs = (Button) findViewById(R.id.bs);
            pkp = (TextView)viewGroup.findViewById(R.id.pkproveedor);
            px = (TextView)viewGroup.findViewById(R.id.pxum);
            mar = (TextView)viewGroup.findViewById(R.id.marca);
            pkp.setText(pkproveedor.get(position).toString());
            px.setText(pxum.get(position).toString());
            mar.setText(marca.get(position).toString());

            Button bs = (Button) viewGroup.findViewById(R.id.bs);

            bs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread tr= new Thread(){
                        @Override
                        public void run() {
                            SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                            String z = mispreferencias.getString("usu","");
                            final String resultado=enviardatosGET(pko.get(position).toString(),pkd.get(position).toString(),z,pkproveedor.get(position).toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int r = obtdatosJSON(resultado);
                                    String z=pkproveedor.get(position).toString();
                                    SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                                    final String pb = mispreferencias.getString("pb","");
                                    Intent i = new Intent(getApplicationContext(), restaurante.class);
                                    Toast.makeText(getApplicationContext(), pb+" solicitado a "+z, Toast.LENGTH_LONG).show();
                                    startActivity(i);
                                    finish();
                                }
                            });
                        }
                    };
                    tr.start();
                }
            });


            return viewGroup;

        }
    }



    public String enviardatosGET(String pko, String pkd,String usur, String usup) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/regconvenio.php?pko="+pko+"&pkd="+pkd+"&usur="+usur+"&usup="+usup);
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

    public String ed(String pko, String pkd,String usur, String usup) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/regconvenio.php?pko="+pko+"&pkd="+pkd+"&usur="+usur+"&usup="+usup);
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