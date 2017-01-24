
package model;

import java.time.LocalDate;


public class Equipo {
    
    private String nombre, localidad;
    private LocalDate fecha_cre;

    public Equipo(String nombre) {
        this.nombre = nombre;
    }
    
    

    public Equipo(String nombre, String localidad, LocalDate fecha_cre) {
        this.nombre = nombre;
        this.localidad = localidad;
        this.fecha_cre = fecha_cre;
    }

    public Equipo() {    }

    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }

    public String getLocalidad() {        return localidad;    }
    public void setLocalidad(String localidad) {        this.localidad = localidad;    }

    public LocalDate getFecha_cre() {        return fecha_cre;    }
    public void setFecha_cre(LocalDate fecha_cre) {        this.fecha_cre = fecha_cre;    }

    @Override
    public String toString() {
        return "Equipo{" + "nombre=" + nombre + ", localidad=" + localidad + ", fecha_cre=" + fecha_cre + '}';
    }
    
    
}
