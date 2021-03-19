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

public class regr extends AppCompatActivity implements View.OnClickListener {
    Button b;

    EditText usuario, contrasena,tel,nr,pais,ciudad,calle,numero,interior,ec,yc,colonia,cp,nombre, ap, am,cel;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regr);
        b = (Button) findViewById(R.id.breg);
        usuario = (EditText) findViewById(R.id.usuario);
        contrasena = (EditText) findViewById(R.id.contrasena);
        tel=(EditText) findViewById(R.id.tel);
        nr =(EditText) findViewById(R.id.cxe);
        pais=(EditText) findViewById(R.id.pais);
        ciudad=(EditText) findViewById(R.id.ciudad);
        calle=(EditText) findViewById(R.id.calle);
        numero= (EditText) findViewById(R.id.numero);
        interior= (EditText) findViewById(R.id.interior);
        ec=(EditText) findViewById(R.id.ec);
        yc=(EditText) findViewById(R.id.yc);
        colonia=(EditText) findViewById(R.id.colonia);
        cp=(EditText) findViewById(R.id.cp);
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
                final String resultado=enviardatosGET(usuario.getText().toString(),contrasena.getText().toString(),tel.getText().toString(),nr.getText().toString(),nombre.getText().toString(),ap.getText().toString(),am.getText().toString(),cel.getText().toString(),calle.getText().toString(),numero.getText().toString(),interior.getText().toString(),ec.getText().toString(),yc.getText().toString(),colonia.getText().toString(),cp.getText().toString(),pais.getText().toString(),ciudad.getText().toString());
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
                                if (tel.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Falta telefono", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    if (nr.getText().toString().isEmpty()){
                                        Toast.makeText(getApplicationContext(), "Falta nombre del restaurante", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        if (nombre.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Falta nombre", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            if (ap.getText().toString().isEmpty()){
                                                Toast.makeText(getApplicationContext(), "Falta apellido materno", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                if (cel.getText().toString().isEmpty()){
                                                    Toast.makeText(getApplicationContext(), "Falta celular", Toast.LENGTH_LONG).show();
                                                }
                                                else{
                                                    if (calle.getText().toString().isEmpty()){
                                                        Toast.makeText(getApplicationContext(), "Falta calle", Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        if (numero.getText().toString().isEmpty()){
                                                            Toast.makeText(getApplicationContext(), "Falta numero de telefono", Toast.LENGTH_LONG).show();
                                                        }
                                                        else{
                                                            if (interior.getText().toString().isEmpty()){
                                                                Toast.makeText(getApplicationContext(), "Falta interior", Toast.LENGTH_LONG).show();
                                                            }
                                                            else{
                                                                if (ec.getText().toString().isEmpty()){
                                                                    Toast.makeText(getApplicationContext(), "Falta una calle", Toast.LENGTH_LONG).show();
                                                                }
                                                                else{
                                                                    if (yc.getText().toString().isEmpty()){
                                                                        Toast.makeText(getApplicationContext(), "Falta una calle", Toast.LENGTH_LONG).show();
                                                                    }
                                                                    else{
                                                                        if (colonia.getText().toString().isEmpty()){
                                                                            Toast.makeText(getApplicationContext(), "Falta colonia", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        else{
                                                                            if (cp.getText().toString().isEmpty()){
                                                                                Toast.makeText(getApplicationContext(), "Falta codigo postal", Toast.LENGTH_LONG).show();
                                                                            }
                                                                            else{
                                                                                if (ciudad.getText().toString().isEmpty()){
                                                                                    Toast.makeText(getApplicationContext(), "Falta ciudad", Toast.LENGTH_LONG).show();
                                                                                }
                                                                                else{
                                                                                    if (pais.getText().toString().isEmpty()){
                                                                                        Toast.makeText(getApplicationContext(), "Falta pais", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                    else{
                                                                                        if (r > 0) {
                                                                                            Toast.makeText(getApplicationContext(), "Ya existe", Toast.LENGTH_LONG).show();

                                                                                        } else {
                                                                                            Intent i = new Intent(getApplicationContext(), rb1.class);
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

    public String enviardatosGET(String usuario, String contrasena,String tel, String nr ,String nombre, String ap, String am, String cel,String calle ,String numero ,String interior ,String ec ,String yc ,String colonia ,String cp ,String ciudad ,String pais ) {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder result = null;
        try {
            url= new URL("http://192.168.1.13/webservice/regrest.php?usu="+usuario+"&pas="+contrasena+"&tel="+tel+"&nombrer="+nr+"&pais="+pais+"&ciudad="+ciudad+"&calle="+calle+"&numero="+numero+"&interior="+interior+"&ec="+ec+"&yc="+yc+"&numero="+numero+"&colonia="+colonia+"&cp="+cp+"&nombre="+nombre+"&ap="+ap+"&am="+am+"&cel="+cel);
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
        tipo=1;
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
