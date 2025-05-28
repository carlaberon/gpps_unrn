package model;

import java.sql.SQLException;
import java.util.List;

public interface ProyectoDAO {
    void guardar(Proyecto proyecto) throws SQLException;

    List<Proyecto> obtenerProyectos() throws SQLException;
}


