package model;

import java.sql.SQLException;
import java.util.List;

public interface GestorDeUsuarios {

    List<Estudiante> obtenerTodosEstudiantes();

    List<Tutor> obtenerTodosTutores();

    List<Director> obtenerTodosDirector();

    Usuario buscarUsuario(String nombreUsuario, String contrasenia) throws SQLException;

    Integer obtenerIdProyectoEstudiante(int idEstudiante) throws SQLException;

}
