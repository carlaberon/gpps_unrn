package model;

public class Proyectos {
    private GestorDeProyectos gestorDeProyectos;

    public Proyectos(GestorDeProyectos gestorDeProyectos) {
        this.gestorDeProyectos = gestorDeProyectos;
    }

    public void propuestaDeProyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
                                    Estudiante estudiante, Tutor tutor, Tutor docenteSupervisor) {
        var proyecto = new Proyecto(id, nombre, descripcion, estado, areaDeInteres, estudiante, tutor, docenteSupervisor);
        this.gestorDeProyectos.registrarPropuestaDeProyecto(proyecto);
        //registra el proyecto
    }

    public void asignarDocenteTutor(int idProyecto, int idDocente, int idTutor) {
        this.gestorDeProyectos.registrarAsignacionDocenteTutor(idProyecto, idDocente, idTutor);
    }
}
