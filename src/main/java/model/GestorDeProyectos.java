package model;

public interface GestorDeProyectos {

    void registrarPropuestaDeProyecto(Proyecto proyecto);

    void registrarAsignacionDocenteTutor(int idProyecto, Tutor docente, Tutor tutor);
}
