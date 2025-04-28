package model;

public class Tutor extends Usuario {
    private String tipo;

    public Tutor(String nombreUsuario, String contrasenia, String nombre, String email, String tipo) {
        super(nombreUsuario, contrasenia, nombre, email);
        this.tipo = tipo;
    }
}
