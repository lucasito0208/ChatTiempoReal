package com.example.myforo;

public class Mensaje {

    private String mensaje;
    private String urlFoto;
    private String nombre;
    private String fotoMensaje;
    private String tipoMensaje;
    private String hora;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, String fotoMensaje, String tipoMensaje, String hora) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoMensaje = fotoMensaje;
        this.tipoMensaje = tipoMensaje;
        this.hora = hora;
    }

    public Mensaje(String mensaje, String urlFoto, String nombre, String fotoMensaje, String tipoMensaje, String hora) {
        this.mensaje = mensaje;
        this.urlFoto = urlFoto;
        this.nombre = nombre;
        this.fotoMensaje = fotoMensaje;
        this.tipoMensaje = tipoMensaje;
        this.hora = hora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(String fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
