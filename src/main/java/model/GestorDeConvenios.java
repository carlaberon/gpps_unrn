package model;

import java.io.InputStream;

public interface GestorDeConvenios {
    void create(Convenio convenio);

    void upDateArchivo(Convenio convenio);

    void cargarArchivoConvenioFirmado(int idConvenio, InputStream archivoPDF);
}















