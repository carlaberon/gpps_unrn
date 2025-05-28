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


//        Proyectos proyectos = new Proyectos(new ServicioDePersistenciaGestionProyectos(), new ServicioDePersistenciaInforme());
                proyectos.asignarDocenteTutor(1, 6);

                // Test project creation (commented out as it's for testing)

                String nombreProyecto = "gpps";
                String descripcion = "desarrollar plataforma para la gestión de practicas profesionales supervisadas";
                boolean estado = false; //es una propuesta, el proyecto no se encuentra aprobado
                String areaDeInteres = "practicas pre-profesionales";

//                var estudiante = new Estudiante(1, "carla", "contra1", "alrac", "alracnoreb@gmail.com", "UNRN-14183", true, "8500");
//                var tutorInterno = new Tutor(1, "tutorinterno", "1234", "gabriel", "gabriel@gmail.com", "tutor interno");
//                var tutorExterno = new Tutor(1, "tutorexterno", "1234", "hernan", "hernan@gmail.com", "tutor externo");

                //proyectos.propuestaDeProyecto(1, nombreProyecto, descripcion, estado, areaDeInteres, estudiante, tutorInterno, tutorExterno);


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



