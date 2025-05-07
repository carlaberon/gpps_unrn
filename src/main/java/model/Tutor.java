package model;

public class Tutor extends Usuario {
    private String tipo;

    public Tutor(int id, String nombreUsuario, String contrasenia, String nombre, String email, String tipo) {
        super(id, nombreUsuario, contrasenia, nombre, email);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
