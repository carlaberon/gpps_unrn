package main;


import database.Conn;
import database.ServicioDePersistenciaGestionProyectos;
import database.ServicioDePersistenciaGestionUsuarios;
import model.Proyectos;
import ui.MainView;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize database connection
                Connection conn = Conn.getConnection();
                if (conn != null && !conn.isClosed()) {
                    System.out.println("✅ Conexión exitosa a la base de datos.");
                }

                // Initialize services
                ServicioDePersistenciaGestionProyectos servicioProyectos =
                        new ServicioDePersistenciaGestionProyectos(conn);
                ServicioDePersistenciaGestionUsuarios servicioUsuarios =
                        new ServicioDePersistenciaGestionUsuarios(conn);
                Proyectos proyectos = new Proyectos(servicioProyectos);

                // Create and show main window
                MainView mainView = new MainView(servicioProyectos, servicioUsuarios);
                mainView.setVisible(true);

                // Test project creation (commented out as it's for testing)
                /*
                String nombreProyecto = "gpps";
                String descripcion = "desarrollar plataforma para la gestión de practicas profesionales supervisadas";
                boolean estado = false; //es una propuesta, el proyecto no se encuentra aprobado
                String areaDeInteres = "practicas pre-profesionales";
                
                var estudiante = new Estudiante(1, "carla", "contra1", "alrac", "alracnoreb@gmail.com", "UNRN-14183", true, "8500");
                var tutorInterno = new Tutor(1, "tutorinterno", "1234", "gabriel", "gabriel@gmail.com", "tutor interno");
                var tutorExterno = new Tutor(1, "tutorexterno", "1234", "hernan", "hernan@gmail.com", "tutor externo");

                //proyectos.propuestaDeProyecto(1, nombreProyecto, descripcion, estado, areaDeInteres, estudiante, tutorInterno, tutorExterno);
                */

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Error al conectar con la base de datos: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
} 


import database.ServicioDePersistenciaGestionProyectos;
import database.ServicioVerificacionInformes;
import model.Proyectos;

public class Main3 {
    public static void main(String[] args) {
//            try {
//                Connection conexion = Conn.getConnection();
//                if (conexion != null && !conexion.isClosed()) {
//                    System.out.println("✅ Conexión exitosa a la base de datos.");
//                }
//            } catch (SQLException e) {
//                System.err.println("❌ Error al conectar: " + e.getMessage());
//            }
        //comento lo anterior
//        //HU: proponer proyecto
//        var gestorDeProyectoPersistencia = new ServicioDePersistenciaGestionProyectos();
//        var proyectos = new Proyectos(gestorDeProyectoPersistencia);
//        //hago una prueba, aportando datos
//        String nombreProyecto = "gpps";
//        String descripcion = "desarrollar plataforma para la gestión de practicas profesionales supervisadas";
//        boolean estado = false; //es una propuesta, el proyecto no se encuentra aprobado
//        String areaDeInteres = "practicas pre-profesionales";
//        //estudiante, director, tutor
//        var estudiante = new Estudiante("carla","contra1", "alrac", "alracnoreb@gmail.com", "UNRN-14183", true, "8500");
//        var director = new Director(1,"director", "1234", "gabriel", "gabriel@gmail.com");
//        var tutor = new Tutor(1, "tutor", "1234","hernan", "hernan@gmail.com", "interno");
//
//        proyectos.propuestaDeProyecto(1, nombreProyecto, descripcion, estado, areaDeInteres, estudiante, director,tutor);

        //HAGO LA PRUEBA CON TEST UNITARIOS

        //Pruebo metodo registrarAsignacionDocenteTutor en BD. Pruebo con los datos cargados ya en la BD

//        Proyectos proyectos = new Proyectos(new ServicioDePersistenciaGestionProyectos(), );
//        proyectos.asignarDocenteTutor(1, 11, 12);

        //cargar informe final
        ServicioVerificacionInformes servicioVerificacionInformes = new ServicioVerificacionInformes();
        ServicioDePersistenciaGestionProyectos servicioDePersistenciaGestionProyectos = new ServicioDePersistenciaGestionProyectos();
        Proyectos proyectos = new Proyectos(servicioDePersistenciaGestionProyectos, servicioVerificacionInformes);
        //cargar informe final
        proyectos.cargarInformeFinal("Informe final de prueba", "final", java.util.List.of(3,4), 5);



    }
}

