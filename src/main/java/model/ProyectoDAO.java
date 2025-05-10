package model;

import java.util.List;

public interface ProyectoDAO {
    void guardar(Proyecto proyecto);
    public List<Proyecto> obtenerProyectos();
}
