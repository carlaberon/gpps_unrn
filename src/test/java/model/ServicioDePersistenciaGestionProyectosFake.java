/*package model;

import java.sql.SQLException;
import java.util.List;

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
    public List<Proyecto> obtenerProyectos() throws SQLException {
        return List.of();
    }

    @Override
    public List<Proyecto> listarProyectosRelacionados(int idUsuario) {
        return List.of();
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

    @Override
    public Proyecto obtenerProyecto(int idProyecto) {
        return null;
    }

    @Override
    public PlanDeTrabajo obtenerPlan(int idProyecto) {
        return null;
    }

    @Override
    public List<Proyecto> obtenerProyectosSinAprobar() {
        return List.of();
    }

    @Override
    public void notificarComentarioDenegacion(String comentario) {

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

	@Override
	public void guardar(Proyecto proyecto) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardarSinEstudiante(Proyecto proyecto) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Proyecto> obtenerProyectos() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cargarInformeParcial(Informe informeParcial) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cargarInformeFinal(Informe informeFinal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tutor> obtenerTutoresPorProyecto(int idProyecto) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Actividad> obtenerActividadesPorPlan(int idPlan) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}*/
