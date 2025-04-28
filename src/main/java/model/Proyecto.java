package model;

public class Proyecto {
    private int id_proyecto;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private String prioridad;
    private Usuario propietario; 
    private Usuario tutorExterno;
    private Usuario docenteSupervisor;

    public Proyecto(int id, String nombre, String descripcion, Boolean estado, String prioridad,
                    Usuario propietario, Usuario tutorExterno, Usuario docenteSupervisor) {
        this.id_proyecto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.propietario = propietario;
        this.tutorExterno = tutorExterno;
        this.docenteSupervisor = docenteSupervisor;
    }
}
