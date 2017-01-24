
package model;

import model.Equipo;
import java.time.LocalDate;


public class Jugador {
    
    private String nombre, posicion;
    private LocalDate fecha_nac;
    private int canastas, rebotes, asistencias;
    private Equipo equipo;

    public Jugador(String nombre, String posicion, LocalDate fecha_nac, int canastas, int rebotes, int asistencias, Equipo equipo) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.fecha_nac = fecha_nac;
        this.canastas = canastas;
        this.rebotes = rebotes;
        this.asistencias = asistencias;
        this.equipo = equipo;
    }

    public Jugador() {    }

    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }

    public String getPosicion() {        return posicion;    }
    public void setPosicion(String posicion) {        this.posicion = posicion;    }

    public LocalDate getFecha_nac() {        return fecha_nac;    }
    public void setFecha_nac(LocalDate fecha_nac) {        this.fecha_nac = fecha_nac;    }

    public int getCanastas() {        return canastas;    }
    public void setCanastas(int canastas) {        this.canastas = canastas;    }

    public int getRebotes() {        return rebotes;    }
    public void setRebotes(int rebotes) {        this.rebotes = rebotes;    }

    public int getAsistencias() {        return asistencias;    }
    public void setAsistencias(int asistencias) {        this.asistencias = asistencias;    }

    public Equipo getEquipo() {        return equipo;    }
    public void setEquipo(Equipo equipo) {        this.equipo = equipo;    }

    @Override
    public String toString() {
        return "Jugador{" + "nombre=" + nombre + ", posicion=" + posicion + ", fecha_nac=" + fecha_nac + ", canastas=" + canastas + ", rebotes=" + rebotes + ", asistencias=" + asistencias + ", equipo=" + equipo + '}';
    }
    
    
}
