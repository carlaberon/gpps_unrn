package model;

import java.util.List;

public class ServicioInformeFake implements ServiceInformes {
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
