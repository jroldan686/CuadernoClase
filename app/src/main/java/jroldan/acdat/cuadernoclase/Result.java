package jroldan.acdat.cuadernoclase;

import java.io.Serializable;
import java.util.ArrayList;

public class Result implements Serializable {
    boolean code;
    int status;
    String message;
    ArrayList<Alumno> alumnos;
    ArrayList<Falta> faltas;
    int last;

    public boolean getCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(ArrayList<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public ArrayList<Falta> getFaltas() {
        return faltas;
    }

    public void setFaltas(ArrayList<Falta> faltas) {
        this.faltas = faltas;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }
}