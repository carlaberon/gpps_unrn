package model;

import java.util.List;

public interface GestorDeUsuarios {

    List<Estudiante> obtenerTodosEstudiantes();

    List<Tutor> obtenerTodosTutores();

    List<Director> obtenerTodosDirector();
}
