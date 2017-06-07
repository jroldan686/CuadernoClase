package jroldan.acdat.cuadernoclase;

import java.io.Serializable;

public class Falta implements Serializable {

    int id;
    String fecha;
    String alumno;
    int tipo;
    int trabajo;
    int actitud;
    String observaciones;

    public Falta() {}

    public Falta(String fecha, String alumno, int tipo, int trabajo, int actitud, String observaciones) {
        this.fecha = fecha;
        this.alumno = alumno;
        this.tipo = tipo;
        this.trabajo = trabajo;
        this.actitud = actitud;
        this.observaciones = observaciones;
    }

    public Falta(int id, String fecha, String alumno, int tipo, int trabajo, int actitud, String observaciones) {
        this.id = id;
        this.fecha = fecha;
        this.alumno = alumno;
        this.tipo = tipo;
        this.trabajo = trabajo;
        this.actitud = actitud;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(int trabajo) {
        this.trabajo = trabajo;
    }

    public int getActitud() {
        return actitud;
    }

    public void setActitud(int actitud) {
        this.actitud = actitud;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
