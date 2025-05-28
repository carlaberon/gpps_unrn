package model;

public class Proyecto {
    private int id_proyecto;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private String areaDeInteres;
    private String ubicacion;
    private Tutor tutor;
    private Tutor docenteSupervisor;

    public Proyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
                    Tutor tutor, Tutor docenteSupervisor, String ubicacion) {
        this.id_proyecto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.areaDeInteres = areaDeInteres;
        this.tutor = tutor;
        this.ubicacion = ubicacion;
        this.docenteSupervisor = docenteSupervisor;
    }

    public boolean esValido() {
        return nombre != null && !nombre.isEmpty()
                && descripcion != null && !descripcion.isEmpty()
                && areaDeInteres != null && !areaDeInteres.isEmpty()
                && tutor != null && docenteSupervisor != null;
    }

    public int getId() {
        return id_proyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getAreaDeInteres() {
        return areaDeInteres;
    }

    public boolean getEstado() {
        return estado;
    }

    public Tutor getDocenteSupervisor() {
        return docenteSupervisor;
    }

    public Tutor getTutor() {
        return tutor;
    }

}
