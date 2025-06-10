package model;

import java.sql.SQLException;
import java.util.List;

public interface GestorDeConvenios {
    void create(Convenio convenio);
    void updateArchivo(Convenio convenio);

    Convenio buscarPorId(int id);
    byte[] obtenerArchivoPdfPorId(int id);
    List<EntidadColaboradora> obtenerTodas() throws SQLException;
    public List<Proyecto> obtenerProyectosConEstudiante() throws SQLException;
}















