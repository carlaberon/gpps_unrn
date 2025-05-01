package model;

import java.time.LocalDate;

public class ConInforme extends Actividad{

    private Informe informe;
    public ConInforme(String descripcion, LocalDate fechaInicio, int horas, boolean estado, Informe informe) {
        super(descripcion, fechaInicio, horas, estado);
        this.informe = informe;
    }
}
