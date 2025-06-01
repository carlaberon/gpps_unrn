package model;

public interface GestorDeConvenios {
    void create(Convenio convenio);
    void updateArchivo(Convenio convenio);

    Convenio buscarPorId(int id);
    byte[] obtenerArchivoPdfPorId(int id);
}















