//package main;
//
//import javax.swing.JOptionPane;
//import javax.swing.SwingUtilities;
//
//import model.PlanDeTrabajo;
//import model.PlanDAO;
//import front.VentanaProgresoPlan;
//import database.PlanDeTrabajoDAOJDBC;
//
//public class Main5 {
//    public static void main(String[] args) {
//        try {
//            PlanDAO planDAO = new PlanDeTrabajoDAOJDBC();
//            PlanDeTrabajo plan = planDAO.obtenerPorProyecto(1);
//            SwingUtilities.invokeLater(() -> new VentanaProgresoPlan(plan).setVisible(true));
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
//        }
//    }
//}

