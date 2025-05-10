package database;

import model.Proyecto;

import java.sql.SQLException;

public interface ProyectoDAO {
    void guardar(Proyecto proyecto) throws SQLException;
}
