package com.example.mark.torneosanmartin.modelo;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class Noticia implements Serializable {
    private  String titulo;
    private  String cuerpo;
    private Date fecha;
    private  boolean estado;
    private  String UrlImagen;
    private Bitmap ImagenBitmap;
    private String Nombre;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Bitmap getImagenBitmap() {
        return ImagenBitmap;
    }

    public void setImagenBitmap(Bitmap imagenBitmap) {
        ImagenBitmap = imagenBitmap;
    }

    public Noticia(String titulo, String cuerpo, Date fecha, boolean estado, String urlImagen) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.fecha = fecha;
        this.estado = estado;
        UrlImagen = urlImagen;
    }

    public Noticia() {

    }



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getUrlImagen() {
        return UrlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        UrlImagen = urlImagen;
    }

    @Override
    public String toString() {
        return "Noticia{" +
                "titulo='" + titulo + '\'' +
                ", cuerpo='" + cuerpo + '\'' +
                ", fecha='" + fecha + '\'' +
                ", estado=" + estado +
                ", UrlImagen='" + UrlImagen + '\'' +
                '}';
    }
}
