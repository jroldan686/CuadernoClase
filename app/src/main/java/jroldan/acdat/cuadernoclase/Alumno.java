package jroldan.acdat.cuadernoclase;

import java.io.Serializable;

public class Alumno implements Serializable {
    int id;
    String nombre, direccion, ciudad, codpostal, telefono, email;

    public Alumno() {
    }

    public Alumno(int id, String nombre, String direccion, String ciudad, String codpostal, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codpostal = codpostal;
        this.telefono = telefono;
        this.email = email;
    }

    public Alumno(String nombre, String direccion, String ciudad, String codpostal, String telefono, String email) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codpostal = codpostal;
        this.telefono = telefono;
        this.email = email;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodpostal() {
        return codpostal;
    }

    public void setCodpostal(String codpostal) {
        this.codpostal = codpostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
