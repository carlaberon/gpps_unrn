package model;

import java.util.List;

public class Usuarios {
    GestorDeUsuarios gestorDeUsuarios;

    public Usuarios(GestorDeUsuarios gestorDeUsuarios) {
        this.gestorDeUsuarios = gestorDeUsuarios;
    }

    public List<Estudiante> obtenerEstudiantes() {
        return gestorDeUsuarios.obtenerTodosEstudiantes();
    }

    public List<Tutor> obtenerTutores() {
        return this.gestorDeUsuarios.obtenerTodosTutores();
    }
}
