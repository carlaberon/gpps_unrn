package model;

public class Estudiante extends Usuario {
    private static int id;
    private String legajo;
    private Boolean regular;
    private String direccionPostal;
    private int id_proyecto;
    private Proyecto proyectoSeleccionado;

    public Estudiante(String nombreUsuario, String contrasenia, String nombre, String email,
                      String legajo, Boolean regular, String direccionPostal) {
        super(id, nombreUsuario, contrasenia, nombre, email);
        this.legajo = legajo;
        this.regular = regular;
        this.direccionPostal = direccionPostal;
    }

    public String getLegajo() {
        return legajo;
    }


    public Boolean isRegular() {
        return regular;
    }

    public String getDireccionPostal() {
        return direccionPostal;
    }

    public Proyecto getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    /*    public int getId() {
            return id;
        }
    */
    public void seleccionarProyecto(Proyecto proyecto) {
        if (this.proyectoSeleccionado != null) {
            throw new IllegalStateException("El estudiante ya seleccionó un proyecto.");
        }
        this.proyectoSeleccionado = proyecto;
    }

    public void setId_proyecto(int id_proyecto) {
        this.id_proyecto = id_proyecto;
    }
}
