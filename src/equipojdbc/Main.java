
package equipojdbc;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import model.Equipo;
import model.Jugador;
import persistence.EquipoJDBC;
import utilidades.EntradaDatos;


public class Main {
    
    public static void main(String[] args) {
        
        EquipoJDBC gestor = new EquipoJDBC();
        
        
        try {
                 
        gestor.conectar();
        System.out.println("conectado"); 
        gestor.cleanDB();
        
        //1
        System.out.println("\n1.Testeando Insercion de equipo: Chicago bulls");
        Equipo e = new Equipo("Chicago Bulls", "Chicago", LocalDate.of(1940, 10, 22));
        gestor.insertEquipo(e);
        System.out.println("\n1.Testeando Insercion de equipo: Cleveland Cavaliers");
        Equipo e2 = new Equipo("Cleveland Cavaliers", "Cleveland", LocalDate.of(1932, 2, 10)); 
        gestor.insertEquipo(e2);  
        
        //2
        System.out.println("\n2.Testeando Insercion de jugador: Michael Jordan, chicago bulls con 500 puntos");
        Jugador j = new Jugador("Michael Jordan", "Escolta", LocalDate.of(1947, 1, 17), 500, 400, 450, e);  
            j.setEquipo(e2);    gestor.insertJugador(j);
        System.out.println("\n2.Testeando Insercion de jugador: Kevin Love y el resto tb");
        Jugador j2 = new Jugador("Kevin Love", "Alero", LocalDate.of(1977, 5, 2), 200, 100, 250, e2); 
           j2.setEquipo(e2);    gestor.insertJugador(j2);
        Jugador j3 = new Jugador("Mike Myers", "Pivot", LocalDate.of(1987, 5, 24), 230, 120, 150, e2); 
           j3.setEquipo(e);     gestor.insertJugador(j3);
        Jugador j4 = new Jugador("Larry Bird", "Alero", LocalDate.of(1965, 11, 21), 215, 109, 132, e2); 
           j4.setEquipo(e);     gestor.insertJugador(j4);
        Jugador j5 = new Jugador("Marc Gasol", "Pivot", LocalDate.of(1991, 2, 2), 290, 129, 152, e2); 
           j5.setEquipo(e2);    gestor.insertJugador(j5);
        Jugador j6 = new Jugador("Stephen Curry", "Escolta", LocalDate.of(1988, 8, 21), 267, 126, 152, e2); 
           j6.setEquipo(e);     gestor.insertJugador(j6);
           
        //3 Modificar canastas, asistencias y rebotes de un jugador determinado.
        System.out.println("\n3.Testeando Modificación de jugador: Michael Jordan, le pondremos 1000 puntacos");
        j.setCanastas(1000); j.setAsistencias(500); j.setRebotes(250);
        gestor.modificarStats(j);
        
        //4 modificar equipo
        System.out.println("\n4.Testeando cambio de equipo, Michael Jordan a los Cleveland");
        j.setEquipo(e2);
        gestor.modEquipoJugador(j);
        
        //5 borrar jugador
        System.out.println("\n5.Testeando borrar jugador, Kevin Love");
        gestor.modEquipoJugador(j2);
        
        //6 jugador a partir del nombre
        System.out.println("\n6.Testeando obtener objeto jugador, Kevin Love");
        gestor.obtenerJugador("Kevin Love");
        
        //7 obtener jugador por nombre incompleto
        System.out.println("\n7.Testeando lista de jugadores que empiezen por Mi");
        for (Jugador x: gestor.listaNombreIncompleto("Mi")){
            System.out.println(x.getNombre());            
        }
        
        //8 listado de jugadores con más de 200 canastas
        System.out.println("\n8.Testeando lista de jugadores con más de 200 canastas");
        for (Jugador x: gestor.jugadoresCanastas(200)){
            System.out.println(x.getNombre()+" tiene estas canastas: "+x.getCanastas());            
        }
        
        //9 Listado de jugadores canastas dentro parametros
        System.out.println("\n9.Testeando lista de jugadores entre 200 y 300 canastas");        
        for (Jugador x: gestor.jugadoresCanastasFork(200, 300)){
            System.out.println(x.getNombre()+" tiene estas canastas: "+x.getCanastas());            
        }
        
        //10 Listado de jugadores canastas dentro parametros
        System.out.println("\n10.Testeando lista de jugadores de la posicion Pivot");
        for (Jugador x: gestor.jugadoresPosition("Pivot")){
            System.out.println(x.getNombre()+" juega de "+x.getPosicion());            
        }
        
        //11 Listado de jugadores que hayan nacido en una fecha anterior 
        //a una fecha especificada como parámetro.
        System.out.println("\n11.Testeando lista de jugadore nacidos antes de 1980,10,10");
        for (Jugador x: gestor.jugadoresFechaAntes(LocalDate.of(1980,10,10))){
            System.out.println(x.getNombre()+" nacido en "+x.getFecha_nac());            
        }
        
        //12Agrupar los jugadores por la posición del campo y devolver para cada grupo la siguiente 
         //información: la media, el máximo y el mínimo de canastas, asistencias y rebotes.
         System.out.println("\n12.Agrupar por posicion y dar la media de canastas");
         gestor.jugadoresGroupByPosition();
         for (String x: gestor.jugadoresGroupByPosition()){
            System.out.println(x);            
        }
         
        //13 Ranking de jugadores por número de canastas.  
        System.out.println("\n13.top 3 en canastas");
         for (Jugador x: gestor.jugadoresRankingCanastas()){
            System.out.println(x.getNombre()+" tiene estos puntos: "+x.getCanastas());            
        }
         
        //14 posicion de jugador en raking.  
        System.out.println("\n14.Vamos a ver la posicion de gasol");
         gestor.posicionJugadorRanking("Marc Gasol");
         System.out.println(gestor.posicionJugadorRanking("Marc Gasol").getNombre()+" tiene estos puntacos: "+
                 gestor.posicionJugadorRanking("Marc Gasol").getCanastas()); 
         
         //15 equipos de tal ciudad  
        System.out.println("\n15.Equipos de Chicago:");
         gestor.equiposLocalidad("Chicago");
          for (Equipo x: gestor.equiposLocalidad("Chicago")){
            System.out.println(x.getNombre());            
        }
         
         //16 jugadores de un equipo
         System.out.println("\n16.Jugadores de los bulls:");
         for (Jugador x: gestor.listaEquipo("Chicago Bulls")){
            System.out.println(x.getNombre());            
        }
         
         //17 jugadores de un equipo y misma posicion
         System.out.println("\n17.Jugadores de los bulls y que son Pivot");
         gestor.listaEquipoPosicion("Chicago Bulls", "Pivot");
         for (Jugador x: gestor.listaEquipoPosicion("Chicago Bulls", "Pivot")){
            System.out.println(x.getNombre()+" juega de "+x.getPosicion());            
        }
         
         //18 maximo anotador
         System.out.println("\n18.Maximo anotador de los bulls");
         gestor.listaEstrellaEquipo("Chicago Bulls");
         System.out.println(gestor.listaEstrellaEquipo("Chicago Bulls").getNombre()+" tiene estos puntacos: "+
                 gestor.listaEstrellaEquipo("Chicago Bulls").getCanastas()); 
         
         

            } catch (SQLException ex){
               System.out.println("error conexion"+ex.getMessage());
           }
    }
}
