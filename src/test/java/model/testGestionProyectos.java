package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testGestionProyectos {
    @Test
    public void testeaEjecucionProponerProyecto() {
        //setup
        //creo un fake
        var servicioDePersistenciaFake = new ServicioDePersistenciaGestionProyectosFake();
        var proyectos = new Proyectos(servicioDePersistenciaFake, new ServicioInformeFake());
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
        var ubicacion = "Finlandia";

        //ejercitacion

        proyectos.propuestaDeProyecto(1, nombreProyecto, descripcion, estado, areaDeInteres, null, null, null, ubicacion);


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

    @Test
    public void asignarDocenteYTutor() {
//        var docente = new Tutor(1, "Juan123", "123", "Juan", "juansito_123@hotmail.com", "Docente");
//        var tutorExterno = new Tutor(1, "Ana123", "1234", "Ana", "ana_123@hotmail.com", "Externo");
        var registroDocenteTutorFake = new ServicioDePersistenciaGestionProyectosFake();
        var proyectos = new Proyectos(registroDocenteTutorFake, new ServicioInformeFake());
        var proyecto = new Proyecto(1, "Proyecto1", "nada", false, "ninguna", null, null, null, "Finlandia");

        proyectos.asignarDocenteTutor(1, 11);

        assertEquals(11, registroDocenteTutorFake.idDocente());
        assertEquals(12, registroDocenteTutorFake.idTutor());
        assertEquals(1, registroDocenteTutorFake.idProyecto());
    }
}
