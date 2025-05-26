package model;

import java.util.List;

public interface ServiceInformes {

    //consulta el atributo "estado"
    boolean verificarInformeParcialAprobado(List<Integer> idInformesParciales);

    //obtiene los datos de un informe a partir de su id
    Informe obtenerInforme(int idInforme);
    //carga el puntaje
    void valorarInforme(Informe informe, int valor);


}
