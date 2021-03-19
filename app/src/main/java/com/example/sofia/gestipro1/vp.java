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

public class vp extends AppCompatActivity implements View.OnClickListener
{
    ArrayList nombre= new ArrayList();
    Button breg;
    String id_d = "";
    ListView listView;
    Context context = this;
    boolean connectionEnabled;
    Spinner s2;
    Button b;
    String categ="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp);

        listView = (ListView) findViewById(R.id.list3);
        breg = (Button) findViewById(R.id.breg);
        s2=(Spinner) findViewById(R.id.s2);
        b = (Button) findViewById(R.id.b);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        ActualizarServicios();

        b.setOnClickListener(this);
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                final int tipo = mispreferencias.getInt("tipo",0);
                if(tipo==1){
                    Intent intent = new Intent(vp.this, restaurante.class);
                    startActivity(intent);
                }else if(tipo==2){
                    Intent intent = new Intent(vp.this, index.class);
                    startActivity(intent);
                }
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
        try
        {
            //Se establece el url
            SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
            String categ= mispreferencias.getString("cat","");

            url = new URL("http://192.168.1.13/webservice/cproducto.php?categoria="+categ);
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
        nombre.clear();

        final ProgressDialog dialogo = ProgressDialog.show(this, "Actualizando datos", "Por favor espera un momento...", true);
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final String resultado = enviarDatosGet();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int r=objDatosJSON(resultado);

                            dialogo.dismiss();
                            try
                            {
                                JSONArray jsonarray = new JSONArray(resultado);
                                for (int i = 0; i < jsonarray.length(); i++)
                                {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    nombre.add(jsonobject.getString("nombre"));
                                }
                                listView.setAdapter(new vp.ServicioAdapter(getApplicationContext()));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                    }
                });
            }
        };
        tr.start();
    }

    @Override
    public void onClick(View v) {
        Thread ta= new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences mispreferencias=getSharedPreferences("usuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor =mispreferencias.edit();
                        String c=(s2.getSelectedItem().toString());
                        editor.putString("cat",c);
                        editor.commit();
                        ActualizarServicios();
                    }
                });
            }
        };
        ta.start();
    }


    private class ServicioAdapter extends BaseAdapter
    {
        Context ctx;
        LayoutInflater layoutInflater;
        TextView no;

        public ServicioAdapter(Context applicationContext)
        {
            this.ctx= applicationContext;
            layoutInflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return nombre.size();
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

            final ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.productito,null);

            no = (TextView)viewGroup.findViewById(R.id.producto);

            no.setText(nombre.get(position).toString());
            return viewGroup;
        }
    }
}




