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
    public void cargarInformeParcial(Informe informeParcial) {

    }

    @Override
    public void cargarInformeFinal(Informe informeFinal) {

    }

    @Override
    public void cargarPlanDeTrabajo(PlanDeTrabajo plan, int idProyecto) {

    }

    @Override
    public void aprobarPlanDeTrabajo(int idPlan) {

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
