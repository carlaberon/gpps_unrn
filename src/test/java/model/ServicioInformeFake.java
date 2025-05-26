package model;

import java.util.List;
import java.util.Map;

public class ServicioInformeFake implements ServiceInformes {
    @Override
    public boolean verificarInformeParcialAprobado(List<Integer> idInformesParciales) {
        return false;
    }

    @Override
    public Map<Integer, Boolean> obtenerEstadosInformes(List<Integer> idsInforme) {
        return Map.of();
    }
	@Override
	public void valorarInforme(int idInforme, int valor) {
		// TODO Auto-generated method stub
		
	}
}
