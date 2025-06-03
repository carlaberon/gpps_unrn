//package ui;
//
//import model.GestorDeProyectos;
//import model.GestorDeUsuarios;
//import javax.swing.*;
//import java.awt.*;
//
//public class ProyectosView extends JFrame {
//
//    private final GestorDeProyectos proyectoDAO;
//    private final GestorDeUsuarios usuarioDAO;
//
//    public ProyectosView(GestorDeProyectos proyectoDAO, GestorDeUsuarios usuarioDAO) {
//        this.proyectoDAO = proyectoDAO;
//        this.usuarioDAO = usuarioDAO;
//        initializeUI();
//    }
//
//    private void initializeUI() {
//        setTitle("Gestión de Proyectos");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(600, 400);
//        setLocationRelativeTo(null);
//
//        // Create main panel with BorderLayout
//        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // Create buttons panel
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//
//        JButton cargarBtn = new JButton("Cargar Proyecto");
//        JButton proponerBtn = new JButton("Proponer Proyecto");
//        JButton seleccionarBtn = new JButton("Seleccionar Proyecto");
//
//        // Add action listeners
//        cargarBtn.addActionListener(e -> {
//            ProyectoFormSwing proyectoForm = new ProyectoFormSwing(usuarioDAO, proyectoDAO);
//            proyectoForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            proyectoForm.setVisible(true);
//        });
//
//        proponerBtn.addActionListener(e -> {
//            JOptionPane.showMessageDialog(this, "Funcionalidad de Proponer Proyecto en desarrollo");
//        });
//
//        seleccionarBtn.addActionListener(e -> {
//            SeleccionarProyecto seleccionarProyecto = new SeleccionarProyecto(proyectoDAO);
//            seleccionarProyecto.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            seleccionarProyecto.setVisible(true);
//        });
//
//        // Add buttons to panel
//        buttonPanel.add(cargarBtn);
//        buttonPanel.add(proponerBtn);
//        buttonPanel.add(seleccionarBtn);
//
//        // Add panels to main panel
//        mainPanel.add(buttonPanel, BorderLayout.CENTER);
//
//        // Add main panel to frame
//        add(mainPanel);
//    }
//}