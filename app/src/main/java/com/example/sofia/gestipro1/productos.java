package com.example.sofia.gestipro1;

/**
 * Created by Sofia on 27/05/2017.
 */

public class productos {
    private String um;
    private String nombre;
    private String categoria;

    public productos(String um, String nombre,String categoria){
        this.setum(um);
        this.setnombre(nombre);
        this.setcategoria(categoria);
    }
    public String getnombre() {
        return nombre;
    }

    public void setnombre(String nombre) {
        this.nombre = nombre;
    }

    public String getum() {
        return um;
    }

    public void setum(String um) {
        this.um = um;
    }

    public String getcategoria() {
        return categoria;
    }

    public void setcategoria(String categoria) {
        this.categoria = categoria;
    }
}