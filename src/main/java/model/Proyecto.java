package model;

public class Proyecto {
    private int id_proyecto;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private String areaDeInteres;
    private Usuario estudiante;
    private Usuario director;
    private Usuario docenteSupervisor;

    public Proyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
                    Usuario estudiante, Usuario director, Usuario docenteSupervisor) {
        this.id_proyecto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.areaDeInteres = areaDeInteres;
        this.estudiante = estudiante;
        this.director = director;
        this.docenteSupervisor = docenteSupervisor;
    }
}
