package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testGestionProyectos {
    @Test
    public void testeaEjecucionProponerProyecto() {
        //setup
        //creo un fake
        var servicioDePersistenciaFake = new ServicioDePersistenciaGestionProyectosFake();
        var proyectos = new Proyectos(servicioDePersistenciaFake);
        String nombreProyecto = "gpps";
        String descripcion = "desarrollar plataforma para la gestión de prácticas profesionales supervisadas en la UNRN";
        boolean estado = false; //el proyecto no se encuentra aprobado
        String areaDeInteres = "prácticas pre-profesionales";
        String nombreUsuarioEstudiante = "CarlaBeron";
        String nombreEstudiante = "carla";
        String contraseniaEstudiante = "1234";
        String emailEstudiante = "alracnoreb@gmail.com";
        String legajoEstudiante = "UNRN-18143";
        boolean regular = true;
        String dirPostal = "8500";

        var director = new Director(1, "director", "1234", "gabriel", "gabriel@gmail.com");


        //ejercitacion

        proyectos.propuestaDeProyecto(1, nombreProyecto, descripcion, estado, areaDeInteres, null, null, null);


        //verificacion

        assertEquals(nombreProyecto, servicioDePersistenciaFake.project().getNombre());
        assertEquals(descripcion, servicioDePersistenciaFake.project().getDescripcion());
        assertEquals(estado, servicioDePersistenciaFake.project().getEstado());
        assertEquals(areaDeInteres, servicioDePersistenciaFake.project().getAreaDeInteres());
        assertEquals(nombreEstudiante, servicioDePersistenciaFake.project().getEstudiante().getNombre());
        assertEquals(nombreUsuarioEstudiante, servicioDePersistenciaFake.project().getEstudiante().getNombreUsuario());
        assertEquals(emailEstudiante, servicioDePersistenciaFake.project().getEstudiante().getEmail());
        assertEquals(legajoEstudiante, servicioDePersistenciaFake.project().getEstudiante().getLegajo());
        assertEquals(legajoEstudiante, servicioDePersistenciaFake.project().getEstudiante().getLegajo());

        //aca puedo asertar los datos del director y del tutor


    }
}
