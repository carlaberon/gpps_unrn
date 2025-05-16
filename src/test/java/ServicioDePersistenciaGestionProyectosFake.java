package model;

public class ServicioDePersistenciaGestionProyectosFake implements GestorDeProyectos {
    private Proyecto propuestaDeProyecto;
    private int idProyecto;
    private Tutor tutor;
    private Tutor docente;

    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {

        this.propuestaDeProyecto = proyecto;

    }

    @Override
    public void registrarAsignacionDocenteTutor(int idProyecto, Tutor docente, Tutor tutor) {
        this.idProyecto = idProyecto;
        this.docente = docente;
        this.tutor = tutor;
    }

    //este metodo es para llamar en los assert
    public Proyecto project() {
        return this.propuestaDeProyecto;
    }

    public int idProyecto() {
        return this.idProyecto;
    }

    public Tutor docente() {
        return this.docente;
    }

    public Tutor tutor() {
        return this.tutor;
    }
}
