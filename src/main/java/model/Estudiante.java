package model;

public class Estudiante extends Usuario {
    private String legajo;
    private Boolean regular;
    private String direccionPostal;

    public Estudiante(int id, String nombreUsuario, String contrasenia, String nombre, String email, String legajo, Boolean regular, String direccionPostal) {
        super(id, nombreUsuario, contrasenia, nombre, email);
        this.legajo = legajo;
        this.regular = regular;
        this.direccionPostal = direccionPostal;
    }

    public String getLegajo() {
        return legajo;
    }

    public Boolean getRegular() {
        return regular;
    }

    public String getDireccionPostal() {
        return direccionPostal;
    }
}
