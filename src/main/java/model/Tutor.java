package model;

public class Tutor extends Usuario {
    private String tipo;

    public Tutor(int id, String nombreUsuario, String contrasenia, String nombre, String email,Rol rol, String tipo) {
    	super(id, nombreUsuario, contrasenia, nombre, email, rol);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
