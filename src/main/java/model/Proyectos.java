package model;

public class Proyectos {
    private GestorDeProyectos gestorDeProyectos;
    private ServiceInformes servicioDeVerificacionInformes;

    public Proyectos(GestorDeProyectos gestorDeProyectos, ServiceInformes verificacion) {
        this.gestorDeProyectos = gestorDeProyectos;
        this.servicioDeVerificacionInformes = verificacion;
    }

    public void propuestaDeProyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
                                    Estudiante estudiante, Tutor tutor, Tutor docenteSupervisor, String ubicacion) {
        var proyecto = new Proyecto(id, nombre, descripcion, estado, areaDeInteres, estudiante, tutor, docenteSupervisor, ubicacion);
        this.gestorDeProyectos.registrarPropuestaDeProyecto(proyecto);
        //registra el proyecto
    }

    public void asignarDocenteTutor(int idProyecto, int idTutorInterno) {
        this.gestorDeProyectos.registrarAsignacionTutorInterno(idProyecto, idTutorInterno);
    }
    public void cargarInformeParcial(int idInformeParcial, String descripcionInformeParcial, String tipo){

        var informeParcial = new Informe(idInformeParcial, descripcionInformeParcial, tipo);
        this.gestorDeProyectos.cargarInformeParcial(informeParcial);
    }
    public void cargarInformeFinal(int idInformeParcial, int idInformeFinal,String descripcionInformeFinal, String tipo){
        if (! this.servicioDeVerificacionInformes.verificarInformeParcialAprobado(idInformeParcial)){ //lista de informes parciales
            throw new RuntimeException("El informe parcial no se encuentra aprobado. Intente en otro momento.");
        }
        var informeFinal = new Informe(idInformeFinal, descripcionInformeFinal, tipo);
        this.gestorDeProyectos.cargarInformeFinal(informeFinal);
    }
    //HU evaluar informe parcial o final
    public void valorarInforme(int idInforme, int valorInforme){
        var informe = servicioDeVerificacionInformes.obtenerInforme(idInforme);
        this.servicioDeVerificacionInformes.valorarInforme(informe, valorInforme);
    }
}
