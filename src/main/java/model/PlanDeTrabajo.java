package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanDeTrabajo {
	private int id;
    private Proyecto idProyecto;
    private int cantHoras;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<Actividad> actividades = new ArrayList<>();
	private String recursos;
	private boolean aprobado;

    public PlanDeTrabajo(Proyecto proyectoAsignado, int cantHoras, List<Actividad> actividades, String recursos) {
        this.idProyecto = proyectoAsignado;
        this.cantHoras = cantHoras;
        this.actividades = actividades;
        this.recursos = recursos;
        this.aprobado = false; 
    }

    public PlanDeTrabajo(int idPlan, int idProyecto, int int1, LocalDate localDate, LocalDate localDate2) {
		// TODO Auto-generated constructor stub
	}

	public int getId() { return id; }
    public List<Actividad> getActividades() { return actividades; }
    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public double calcularProgreso() {
        if (actividades == null || actividades.isEmpty()) return 0.0;
        long realizadas = actividades.stream().filter(Actividad::isEstado).count();
        return ((double) realizadas / actividades.size()) * 100;
    }

    public void aprobar() {
        this.aprobado = true;
    }

    public void rechazar() {
        this.aprobado = false;
    }

    public boolean estaAprobado() {
        return this.aprobado;
    }
    public void agregarActividad(Actividad act) {
        actividades.add(act);
    }



}
