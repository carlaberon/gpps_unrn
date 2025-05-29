package model;

public class Director extends Usuario {
	 public Director(int id, String nombreUsuario, String contrasenia, String nombre, String email, Rol rol) {
	        super(id, nombreUsuario, contrasenia, nombre, email, rol);
	    }
    public void aprobarPlan(PlanDeTrabajo plan) {
        plan.aprobar();
    }

    public void rechazarPlan(PlanDeTrabajo plan) {
        plan.rechazar();
    }
}