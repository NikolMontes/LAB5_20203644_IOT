package com.example.telehealth.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Medicamento {
    private Integer id;
    private String nombre;
    private String tipo; // Pastilla, Jarabe, Ampolla, Cápsula
    private String dosis;
    private Integer frecuenciaHoras;
    private String fechaInicio;
    private String horaInicio;
    private Boolean activo;

    public Medicamento(){
    }
    public Medicamento(String nombre, String tipo, String dosis, Integer frecuenciaHoras,
                       String fechaInicio, String horaInicio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dosis = dosis;
        this.frecuenciaHoras = frecuenciaHoras;
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.activo = true;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public int getFrecuenciaHoras() {
        return frecuenciaHoras;
    }

    public void setFrecuenciaHoras(int frecuenciaHoras) {
        this.frecuenciaHoras = frecuenciaHoras;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public long getTimestampInicio() {
        try {
            String formato = "dd/MM/yyyy hh:mm a";
            SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());
            Date fecha = sdf.parse(this.fechaInicio + " " + this.horaInicio);
            return fecha.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis(); // fallback
        }
    }

}
