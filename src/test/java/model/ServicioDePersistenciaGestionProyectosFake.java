package model;

public class ServicioDePersistenciaGestionProyectosFake implements GestorDeProyectos{
    private Proyecto propuestaDeProyecto;
    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {

        this.propuestaDeProyecto = proyecto;

    }

    //este metodo es para llamar en los assert
    public Proyecto project(){
        return this.propuestaDeProyecto;
    }
}
