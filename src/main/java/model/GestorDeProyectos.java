package model;

import java.sql.SQLException;

public interface GestorDeProyectos {

    void registrarPropuestaDeProyecto(Proyecto proyecto);

    void guardar(Proyecto proyecto) throws SQLException;

    void guardarSinEstudiante(Proyecto proyecto) throws SQLException;
}
