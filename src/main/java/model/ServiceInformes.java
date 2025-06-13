package model;

import java.util.List;
import java.util.Map;

public interface ServiceInformes {


    boolean verificarInformeParcialAprobado(List<Integer> idInformesParciales);

    public Map<Integer, Boolean> obtenerEstadosInformes(List<Integer> idsInforme);

    void valorarInforme(int idInforme, int valor);


}
