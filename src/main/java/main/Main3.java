package main;

import database.ServicioDePersistenciaGestionConvenios;
import front.GenerarConvenio;
import model.Administrador;

import javax.swing.*;

public class Main3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Administrador admin = new Administrador(0, null, null, null, null, null);
            var dao = new ServicioDePersistenciaGestionConvenios();
            new GenerarConvenio(admin, dao).setVisible(true);
        });
    }
}

//package main;
//
//import database.ServicioDePersistenciaGestionProyectos;
//import database.ServicioVerificacionInformes;
//import model.Proyectos;
//
//import java.sql.Connection;
//
//public class Main3 {
//    public static void main(String[] args) {
/// /            try {
/// /                Connection conexion = Conn.getConnection();
/// /                if (conexion != null && !conexion.isClosed()) {
/// /                    System.out.println("✅ Conexión exitosa a la base de datos.");
/// /                }
/// /            } catch (SQLException e) {
/// /                System.err.println("❌ Error al conectar: " + e.getMessage());
/// /            }
//        //comento lo anterior
/// /        //HU: proponer proyecto
/// /        var gestorDeProyectoPersistencia = new ServicioDePersistenciaGestionProyectos();
/// /        var proyectos = new Proyectos(gestorDeProyectoPersistencia);
/// /        //hago una prueba, aportando datos
/// /        String nombreProyecto = "gpps";
/// /        String descripcion = "desarrollar plataforma para la gestión de practicas profesionales supervisadas";
/// /        boolean estado = false; //es una propuesta, el proyecto no se encuentra aprobado
/// /        String areaDeInteres = "practicas pre-profesionales";
/// /        //estudiante, director, tutor
/// /        var estudiante = new Estudiante("carla","contra1", "alrac", "alracnoreb@gmail.com", "UNRN-14183", true, "8500");
/// /        var director = new Director(1,"director", "1234", "gabriel", "gabriel@gmail.com");
/// /        var tutor = new Tutor(1, "tutor", "1234","hernan", "hernan@gmail.com", "interno");
/// /
/// /        proyectos.propuestaDeProyecto(1, nombreProyecto, descripcion, estado, areaDeInteres, estudiante, director,tutor);
//
//        //HAGO LA PRUEBA CON TEST UNITARIOS
//
//        //Pruebo metodo registrarAsignacionDocenteTutor en BD. Pruebo con los datos cargados ya en la BD
//
////        Proyectos proyectos = new Proyectos(new ServicioDePersistenciaGestionProyectos(), );
////        proyectos.asignarDocenteTutor(1, 11, 12);
//
//        //cargar informe final
//        Connection conn = null;
//        ServicioVerificacionInformes servicioVerificacionInformes = new ServicioVerificacionInformes();
//        ServicioDePersistenciaGestionProyectos servicioDePersistenciaGestionProyectos = new ServicioDePersistenciaGestionProyectos(conn);
//        Proyectos proyectos = new Proyectos(servicioDePersistenciaGestionProyectos, servicioVerificacionInformes);
//        //cargar informe final
//        proyectos.cargarInformeFinal("Informe final de prueba", "final", java.util.List.of(3, 4), 5);
//
//
//    }
//}

