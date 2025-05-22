package model;

public interface GestorDeProyectos {

    void registrarPropuestaDeProyecto(Proyecto proyecto);

    void registrarAsignacionTutorInterno(int idProyecto, int idTutorInterno);

    void cargarInformeParcial(Informe informeParcial);

    void cargarInformeFinal(Informe informeFinal);
}
