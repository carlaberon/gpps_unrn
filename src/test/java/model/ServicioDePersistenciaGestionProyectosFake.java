package model;

public class ServicioDePersistenciaGestionProyectosFake implements GestorDeProyectos {
    private Proyecto propuestaDeProyecto;
    private int idProyecto;
    private int idTutor;
    private int idDocente;

    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {

        this.propuestaDeProyecto = proyecto;

    }

    @Override
    public void registrarAsignacionTutorInterno(int idProyecto, int idTutorInterno) {
        this.idProyecto = idProyecto;
        this.idDocente = idTutorInterno;
        this.idTutor = idTutor;
    }

    @Override
    public void cargarInforme(Informe informeParcial) {

    }



    //este metodo es para llamar en los assert
    public Proyecto project() {
        return this.propuestaDeProyecto;
    }

    public int idProyecto() {
        return this.idProyecto;
    }

    public int idDocente() {
        return this.idDocente;
    }

    public int idTutor() {
        return this.idTutor;
    }
}
