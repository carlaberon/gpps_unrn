package model;

import java.time.LocalDate;

public class SinInforme extends Actividad{
    public SinInforme(String descripcion, LocalDate fechaInicio, int horas, boolean estado) {
        super(descripcion, fechaInicio, horas, estado);
    }
}
