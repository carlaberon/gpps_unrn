package model;

public class ServicioInformeFake implements ServiceInformes {
    @Override
    public boolean verificarInformeParcialAprobado(int idInformeParcial) {
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
