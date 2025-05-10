package model;

import database.DataBaseConnectionException;

import java.util.List;

public interface EstudianteDAO {
    List<Estudiante> obtenerTodos();
    Estudiante obtenerPorId(int id);
    void seleccionarProyecto(Estudiante estudiante) throws DataBaseConnectionException;
} 