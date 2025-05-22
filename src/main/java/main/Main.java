package main;


import database.ServicioDePersistenciaGestionProyectos;
import model.Informe;
import model.Proyectos;
import model.ServiceInformes;

public class Main {
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

        Proyectos proyectos = new Proyectos(new ServicioDePersistenciaGestionProyectos(), new ServiceInformes() {
            @Override
            public boolean verificarInformeParcialAprobado(int idInformeParcial) {
                return false;
            }

            @Override
            public Informe obtenerInforme(int idInforme) {
                return null;
            }

            @Override
            public void valorarInforme(Informe informe, int valor) {

            }
        });
        proyectos.asignarDocenteTutor(1, 9);
    }
}