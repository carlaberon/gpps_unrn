package model;

import java.util.List;

public class ServicioVerificacionInformes implements ServiceInformes{

    public ServicioVerificacionInformes() {
        // Constructor vacío
    }
    @Override
    public boolean verificarInformeParcialAprobado(List<Integer> idInformesParciales) {
        return false;
    }

    @Override
    public Informe obtenerInforme(int idInforme) {
        return null;
    }

    @Override
    public void valorarInforme(Informe informe, int valor) {

    }
}
