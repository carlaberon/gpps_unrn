package main;


import database.ServicioDePersistenciaGestionConvenios;
import front.CargarConvenio;

import javax.swing.*;

public class Main2 {
    public static void main(String[] args) {
        ServicioDePersistenciaGestionConvenios dao = new ServicioDePersistenciaGestionConvenios();
        SwingUtilities.invokeLater(() -> new CargarConvenio(dao));


    }
}

//package main;
//
//import database.ServicioDePersistenciaGestionProyectos;
//import database.ServicioDePersistenciaGestionUsuarios;
//import model.*;
//import ui.ProyectoFormSwing;
//
//import java.sql.Connection;
//
//public class Main {
//    public static void main(String[] args) {
/// /        try {
/// /            Connection conexion = Conn.getConnection();
/// /            if (conexion != null && !conexion.isClosed()) {
/// /                System.out.println("✅ Conexión exitosa a la base de datos.");
/// /            }
/// /        } catch (SQLException e) {
/// /            System.err.println("❌ Error al conectar: " + e.getMessage());
/// /        }
//        //comento lo anterior
//        //HU: proponer proyecto
//        Connection conn = null;
//        var gestorDeProyectoPersistencia = new ServicioDePersistenciaGestionProyectos(conn);
//        var proyectos = new Proyectos(gestorDeProyectoPersistencia);
//        //hago una prueba, aportando datos
//        String nombreProyecto = "gpps";
//        String descripcion = "desarrollar plataforma para la gestión de practicas profesionales supervisadas";
//        boolean estado = false; //es una propuesta, el proyecto no se encuentra aprobado
//        String areaDeInteres = "practicas pre-profesionales";
//        //estudiante, director, tutor
//        var estudiante = new Estudiante(1, "carla", "contra1", "alrac", "alracnoreb@gmail.com", "UNRN-14183", true, "8500");
//        var tutorInterno = new Tutor(1, "tutorinterno", "1234", "gabriel", "gabriel@gmail.com", "tutor interno");
//        var tutorExterno = new Tutor(1, "tutorexterno", "1234", "hernan", "hernan@gmail.com", "tutor externo");
//
//        //proyectos.propuestaDeProyecto(1, nombreProyecto, descripcion, estado, areaDeInteres, estudiante, tutorInterno, tutorExterno);
//
//        GestorDeUsuarios gestorUsuarios = new ServicioDePersistenciaGestionUsuarios(conn);
//        GestorDeProyectos gestorProyectos = new ServicioDePersistenciaGestionProyectos(conn);
//        new ProyectoFormSwing(gestorUsuarios, gestorProyectos).setVisible(true);
//        //HAGO LA PRUEBA CON TEST UNITARIOS
//    }
//}

