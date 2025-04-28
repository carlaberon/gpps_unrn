package model;

public class EntidadColaboradora {
    private String nombre;
    private String direccionPostal;
    private String correo;
    private String logo;

    public EntidadColaboradora(String direccionPostal, String correo, String logo, String nombre) {
        this.direccionPostal = direccionPostal;
        this.correo = correo;
        this.logo = logo;
        this.nombre = nombre;
    }
}
