package database;

import model.Informe;
import model.ServiceInformes;

import java.util.List;

public class ServicioDePersistenciaInforme implements ServiceInformes {
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
