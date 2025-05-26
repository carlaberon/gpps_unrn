package model;

public class ServicioVerificacionInformes implements ServiceInformes{

    public ServicioVerificacionInformes() {
        // Constructor vacío
    }
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
