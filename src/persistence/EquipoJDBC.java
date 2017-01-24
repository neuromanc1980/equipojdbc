package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Equipo;
import model.Jugador;

public class EquipoJDBC {

    //conexion
    private Connection conexion;

    public void conectar() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/basket";
        String usr = "root";
        String pass = "";
        conexion = DriverManager.getConnection(url, usr, pass);
    }

    public void desconectar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    //0 limpiar db
    public void cleanDB() throws SQLException{
        Statement st = conexion.createStatement();
        st.executeUpdate("delete from player;");
        st.executeUpdate("delete from team;");
        st.close();
        
    }
    
    //2 insertar jugador
    public void insertJugador(Jugador j) throws SQLException {
        String insert = "insert into player values (?,?,?,?,?,?,?)";
        PreparedStatement ps = conexion.prepareStatement(insert);
        //parametrizamos
        ps.setString(1, j.getNombre());        
        ps.setDate(2, java.sql.Date.valueOf(j.getFecha_nac()));        
        ps.setInt(3, j.getCanastas());
        ps.setInt(4, j.getRebotes());
        ps.setInt(5, j.getAsistencias());
        ps.setString(6, j.getPosicion());
        ps.setString(7, j.getEquipo().getNombre());
        //ejecuta la consulta. query para select, execute para update insert o delete
        ps.executeUpdate();
        //cerramos recursos abiertos
        ps.close();
    }

    //1 insertar equipo
    public void insertEquipo(Equipo e) throws SQLException {
        String insert = "insert into team values (?,?,?)";
        PreparedStatement ps = conexion.prepareStatement(insert);
        ps.setString(1, e.getNombre());
        ps.setString(2, e.getLocalidad());
        ps.setDate(3, java.sql.Date.valueOf(e.getFecha_cre()));
        ps.executeUpdate();
        ps.close();
    }

    //3 modificar canastas, asistencias, rebotes
    public void modificarStats(Jugador j) throws SQLException {
        String insert = "update player set nbaskets = ?, nrebounds = ?, nassists = ? where name = ?";
        PreparedStatement ps = conexion.prepareStatement(insert);
        ps.setInt(1, j.getCanastas());
        ps.setInt(2, j.getRebotes());
        ps.setInt(3, j.getAsistencias());
        ps.setString(4, j.getNombre());
        ps.executeUpdate();
        ps.close();
    }

    //4 modificar el equipo de un jugador 
    public void modEquipoJugador(Jugador j) throws SQLException {
        String insert = "update player set team = ? where name = ?";
        PreparedStatement ps = conexion.prepareStatement(insert);
        ps.setString(1, j.getEquipo().getNombre());
        ps.setString(2, j.getNombre());
        ps.executeUpdate();
        ps.close();
    }

    //5 borrar jugador
    public void eliminarJugador(Jugador j) throws SQLException {
        String insert = "delete from player where name = ?";
        PreparedStatement ps = conexion.prepareStatement(insert);
        ps.setString(1, j.getNombre());
        ps.executeUpdate();
        ps.close();
    }

    //6 obtener jugador a partir del nombre
    public Jugador obtenerJugador(String nombre) throws SQLException {
        String query = "Select * from player where name = ?";
        PreparedStatement ps = conexion.prepareStatement(query);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        Jugador j = new Jugador();
        if (rs.next()) {
            j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
        }
        ps.close(); rs.close();
        //creamos un jugador j con esos parametros          
        return j;
    }

    //7 Obtener el listado de jugadores a partir de un nombre de manera que no haga
    // falta indicar el nombre completo.
    public List<Jugador> listaNombreIncompleto (String nombre) throws SQLException{
        List<Jugador> jugadores = new ArrayList<>();    
        String query = "Select * from player where name like '"+nombre+"%'";
        Statement st = conexion.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            Jugador j = new Jugador();
            j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            jugadores.add(j);
            //System.out.println(rs.getString("name"));            
        }
        st.close(); rs.close();
        return jugadores;       
    } 
    
    //8 Listado de jugadores que hayan conseguido un número mayor 
    //o igual a un número de canastas especificado como parámetro.
    public List<Jugador> jugadoresCanastas(int baskets) throws SQLException{
        List<Jugador> jugadores = new ArrayList<>(); 
        String query = "Select * from player where nbaskets >"+baskets;
        Statement st = conexion.prepareStatement(query);
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
             Jugador j = new Jugador();
            j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            jugadores.add(j);
            //System.out.println(rs.getString("name"));            
        }
        st.close(); rs.close();
        return jugadores; 
    }
    
    //9 Listado de jugadores que hayan efectuado un número de asistencias 
    //dentro de un rango especificado como parámetro
     public List<Jugador> jugadoresCanastasFork(int min, int max) throws SQLException{
        List<Jugador> jugadores = new ArrayList<>(); 
        String query = "Select * from player where nbaskets between "+min+" and "+max;
        Statement st = conexion.prepareStatement(query);
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            Jugador j = new Jugador();
            j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            jugadores.add(j);
            //System.out.println(rs.getString("name"));            
        }
        st.close(); rs.close();
        return jugadores; 
    }
     
    //10 Listado de jugadores que pertenezcan a una posición 
     //específica, por ejemplo: base.
     public List<Jugador> jugadoresPosition(String posicion) throws SQLException{
         List<Jugador> jugadores = new ArrayList<>(); 
        String query = "Select * from player where position = '"+posicion+"'";
        Statement st = conexion.prepareStatement(query);
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            Jugador j = new Jugador();
            j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            jugadores.add(j);
            //System.out.println(rs.getString("name")+" - plays as "+rs.getString("position"));            
        }
        st.close(); rs.close();
        return jugadores;
    }
     
    //11 Listado de jugadores que hayan nacido en una fecha anterior 
     //a una fecha especificada como parámetro.
     public List<Jugador> jugadoresFechaAntes(LocalDate fecha) throws SQLException{
         List<Jugador> jugadores = new ArrayList<>();
        String query = "Select * from player where birth < '"+fecha+"'";
        //System.out.println(query);
        Statement st = conexion.prepareStatement(query);
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            Jugador j = new Jugador();
            j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            jugadores.add(j);
            //System.out.println(rs.getString("name")+" - nacido en as "+rs.getString("birth")); 
            }
        st.close(); rs.close();
        return jugadores;
    }
     
     //12Agrupar los jugadores por la posición del campo y devolver para cada grupo la siguiente 
     //información: la media, el máximo y el mínimo de canastas, asistencias y rebotes.
     public List<String> jugadoresGroupByPosition() throws SQLException{
        String query = "Select position, round(Avg(nbaskets)), max(nbaskets), min(nbaskets) from player group by position";
        List<String> lista = new ArrayList<>();        
        //System.out.println(query);
        Statement st = conexion.prepareStatement(query);
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            String item = rs.getString("position")+": avg: "+rs.getString("round(avg(nbaskets))")+": max: "+rs.getString("max(nbaskets)")+": min: "+rs.getString("min(nbaskets)");
            lista.add(item);
            //System.out.println(rs.getString("position")+": avg: "+rs.getString("round(avg(nbaskets))")+": max: "+rs.getString("max(nbaskets)")+": min: "+rs.getString("min(nbaskets)")); 
            }
        st.close(); rs.close();
        return lista;
    }
     
     //13 Ranking de jugadores por número de canastas. 
     public List<Jugador> jugadoresRankingCanastas() throws SQLException{
        String query = "Select * from player order by nbaskets desc limit 3";
        //System.out.println(query);
        List<Jugador> jugadores = new ArrayList<>();
        Statement st = conexion.prepareStatement(query);
        ResultSet rs = st.executeQuery(query);
        int n = 1;
        while (rs.next()){
            //System.out.println(n+"º "+rs.getString("name")+" "+rs.getString("nbaskets")+" puntacos"); 
            n++;
            Jugador j = new Jugador();
            j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            jugadores.add(j);
            }
        st.close(); rs.close();
        return jugadores;
    }
     
     //14 Obtener la posición dentro del ranking para un jugador determinado.
     public Jugador posicionJugadorRanking(String nombre) throws SQLException{
        String query = "Select * from player order by nbaskets desc";
        Statement st = conexion.prepareStatement(query);
        ResultSet rs = st.executeQuery(query);
        Jugador j = new Jugador();
        int n = 0;
        while (rs.next()){
            n++;
            if (rs.getString("name").equals(nombre)){
            //System.out.println(n+"º "+rs.getString("name")+" "+rs.getString("nbaskets")+" puntacos");          
            
            j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            }
            //System.out.println(n+"º "+rs.getString("name")+" "+rs.getString("nbaskets")+" puntacos");
        }
        st.close(); rs.close();
        return j;
    }
     
     //15 Listado de equipos existentes en una localidad determinada.
     public List<Equipo> equiposLocalidad(String tokoro) throws SQLException{
         List<Equipo> equipos = new ArrayList<>();
        String query = "Select * from team where city = '"+tokoro+"'";
        Statement st = conexion.prepareStatement(query);
        ResultSet rs = st.executeQuery(query);
        Equipo e = new Equipo();
        while (rs.next()){
            //System.out.println(rs.getString("name"));            
            e.setNombre(rs.getString("name"));
            e.setLocalidad(rs.getString("city"));
            e.setFecha_cre(rs.getDate("creation").toLocalDate());            
        }
        equipos.add(e);
        st.close(); rs.close();
        return equipos;
        
     }
     
     //16 Listado de todos los jugadores de un equipo, a partir del nombre
     //completo del equipo.
     public List<Jugador> listaEquipo (String nombre) throws SQLException{
         List<Jugador> jugadores = new ArrayList<>();
        String query = "Select * from player where team = '"+nombre+"'";
        Statement st = conexion.createStatement();
        ResultSet rs = st.executeQuery(query);
        
        while (rs.next()){
            Jugador j = new Jugador();
           // System.out.println(rs.getString("name"));  
           j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            jugadores.add(j);
        }
        st.close(); rs.close();
        return jugadores;       
    }
     
     //17 Listado de todos los jugadores de un equipo, que además jueguen
     //en la misma posición (parámetro adicional de la consulta), por ejemplo, alero.
     public List<Jugador> listaEquipoPosicion (String nombre, String position) throws SQLException{
        List<Jugador> jugadores = new ArrayList<>();
         String query = "Select * from player where team = '"+nombre+"' and position = '"+position+"' ";
        Statement st = conexion.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
            Jugador j = new Jugador();
             j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            jugadores.add(j);
            //System.out.println(rs.getString("name")+" juega de "+position);            
        }
        st.close(); rs.close();
        return jugadores; 
        
    }
     
     //18 Devuelve el jugador que más canastas ha realizado de un equipo determinado como parámetro.
      public Jugador listaEstrellaEquipo (String nombre) throws SQLException{
         Jugador j = new Jugador();
        String query = "Select * from player where team = '"+nombre+"'  order by nbaskets desc limit 1";
        Statement st = conexion.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()){
             j.setNombre(rs.getString("name"));
            j.setPosicion(rs.getString("position"));
            j.setFecha_nac(rs.getDate("birth").toLocalDate());
            j.setCanastas(rs.getInt("nbaskets"));
            j.setRebotes(rs.getInt("nrebounds"));
            j.setAsistencias(rs.getInt("nassists"));
            j.setEquipo(new Equipo(rs.getString("team")));
            
            //System.out.println(rs.getString("name")+" tiene de "+rs.getString("nbaskets")+" puntos, es la estrella de "+nombre);            
        }
        st.close(); rs.close();
        return j;
    }
     
}
