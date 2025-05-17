package model;

public interface GestorDeProyectos {

    void registrarPropuestaDeProyecto(Proyecto proyecto);

    void registrarAsignacionDocenteTutor(int idProyecto, int idDocente, int idTutor);

    void cargarInformeParcial(Informe informeParcial);

    void cargarInformeFinal(Informe informeFinal);
}
