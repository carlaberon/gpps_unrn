package model;

public class Proyecto {
    private int id_proyecto;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private String areaDeInteres;
    private Tutor tutorExterno;
    private Tutor tutorInterno;
    private String ubicacion;
    private String estado_Proyecto;

    public Proyecto(int id, String nombre, String descripcion, Boolean estado, String areaDeInteres,
                    Tutor tutorExterno, Tutor tutorInterno, String ubicacion) {
        this.id_proyecto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.areaDeInteres = areaDeInteres;
        this.tutorExterno = tutorExterno;
        this.tutorInterno = tutorInterno;
        this.ubicacion = ubicacion;
    }

    public Proyecto(int id, String nombre2, String nombre3, Object object, String nombre4, Object object2,
                    Object object3, Object object4) {
        // TODO Auto-generated constructor stub
    }


    public boolean esValido() {
        return nombre != null && !nombre.isEmpty()
                && descripcion != null && !descripcion.isEmpty()
                && areaDeInteres != null && !areaDeInteres.isEmpty()
                && tutorExterno != null && tutorInterno != null;
    }

    public int getId() {
        return id_proyecto;
    }

    public void setId(int id) {
        this.id_proyecto = id;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre2) {
        this.nombre = nombre2;

    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getAreaDeInteres() {
        return areaDeInteres;
    }

    public boolean getEstado() {
        return estado;
    }

    public Tutor tutorInterno() {
        return tutorInterno;
    }

    public Tutor tutorExterno() {
        return tutorExterno;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getIdUsuarioTutorInterno() {
        return tutorInterno.getId();
    }

    public int getIdUsuarioTutorExterno() {
        return tutorExterno.getId();
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String estadoProyecto() {
        return estado_Proyecto;
    }

    public void setEstadoProyecto(String estado_Proyecto) {
        this.estado_Proyecto = estado_Proyecto;
    }
}