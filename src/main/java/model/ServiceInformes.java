package model;

import java.util.List;
import java.util.Map;

public interface ServiceInformes {

    //consulta el atributo "estado"
    boolean verificarInformeParcialAprobado(List<Integer> idInformesParciales);

    //obtiene los datos de un informe a partir de su id
    public Map<Integer, Boolean> obtenerEstadosInformes(List<Integer> idsInforme);
    //carga el puntaje
    void valorarInforme(int idInforme, int valor);


}
