package model;

public class EntidadColaboradora {
    private String nombre;
    private String cuit;
    private String direccionPostal;
    private String correo;
    private String logo;

    public EntidadColaboradora(String direccionPostal, String correo, String logo, String nombre, String cuit) {
        this.direccionPostal = direccionPostal;
        this.correo = correo;
        this.logo = logo;
        this.nombre = nombre;
        this.cuit = cuit;
    }
}
