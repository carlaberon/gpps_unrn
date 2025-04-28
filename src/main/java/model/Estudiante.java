package model;

public class Estudiante extends Usuario {
    private String legajo;
    private Boolean regular;
    private String direccionPostal;

    public Estudiante(String nombreUsuario, String contrasenia, String nombre, String email, String legajo, Boolean regular, String direccionPostal) {
        super(nombreUsuario, contrasenia, nombre, email);
        this.legajo = legajo;
        this.regular = regular;
        this.direccionPostal = direccionPostal;
    }
}
