package database;

import model.GestorDeProyectos;
import model.Proyecto;
import model.Tutor;
//el import...dependencia

public class ServicioDePersistenciaGestionProyectos implements GestorDeProyectos {
    @Override
    public void registrarPropuestaDeProyecto(Proyecto proyecto) {
        //aca implemento
        //ahora muestro cómo hacer las pruebas sin tener la implementacion

    }

    @Override
    public void registrarAsignacionDocenteTutor(int idProyecto, Tutor docente, Tutor tutor) {
        //aca implemento el update para darle valor a los atributos tutor y docente del proyecto en BD
    }
}
