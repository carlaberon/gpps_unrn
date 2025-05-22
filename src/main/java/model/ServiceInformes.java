package model;

public interface ServiceInformes {

    //consulta el atributo "estado"
    boolean verificarInformeParcialAprobado(int idInformeParcial);
    //obtiene los datos de un informe a partir de su id
    Informe obtenerInforme(int idInforme);
    //carga el puntaje
    void valorarInforme(Informe informe, int valor);


}
