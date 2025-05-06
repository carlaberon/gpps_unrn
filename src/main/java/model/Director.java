package model;

public class Director extends Usuario {

    public Director(String nombreUsuario, String contraseña, String nombre, String email) {
        super(nombreUsuario, contraseña, nombre, email);
    }

    public void aprobarPlan(PlanDeTrabajo plan) {
        plan.aprobar();
    }

    public void rechazarPlan(PlanDeTrabajo plan) {
        plan.rechazar();
    }
}
