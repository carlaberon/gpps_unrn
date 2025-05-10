package model;

public class Director extends Usuario {
    public Director(int id, String nombreUsuario, String contrasenia, String nombre, String email) {
        super(id, nombreUsuario, contrasenia, nombre, email);
    }
    public void aprobarPlan(PlanDeTrabajo plan) {
        plan.aprobar();
    }

    public void rechazarPlan(PlanDeTrabajo plan) {
        plan.rechazar();
    }
}