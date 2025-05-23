package model;

public class EntidadColaboradora {
    private int id; 
    private String nombre;
    private String cuit;
    private String direccionPostal;
    private String correo;
    private String logo;

    public EntidadColaboradora() {
    	
    }

    public EntidadColaboradora(int id, String direccionPostal, String correo, String logo, String nombre, String cuit) {
        this.id = id;
        this.direccionPostal = direccionPostal;
        this.correo = correo;
        this.logo = logo;
        this.nombre = nombre;
        this.cuit = cuit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getDireccionPostal() {
        return direccionPostal;
    }

    public void setDireccionPostal(String direccionPostal) {
        this.direccionPostal = direccionPostal;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
