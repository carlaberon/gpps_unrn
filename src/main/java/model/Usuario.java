package model;

public abstract class Usuario {
    private String nombreUsuario;
    private String contrasenia;
    private String nombre;
    private String email;

    public Usuario(String nombreUsuario, String contrasenia, String nombre, String email) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
