package ui;

import model.GestorDeProyectos;
import model.GestorDeUsuarios;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    
    private final GestorDeProyectos proyectoDAO;
    private final GestorDeUsuarios usuarioDAO;
    
    public MainView(GestorDeProyectos proyectoDAO, GestorDeUsuarios usuarioDAO) {
        this.proyectoDAO = proyectoDAO;
        this.usuarioDAO = usuarioDAO;
        initializeUI();
    }
    
    private void initializeUI() {
        // Set window properties
        setTitle("Sistema GPPS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen
        
        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Create main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create toolbar with buttons
        JToolBar toolBar = createToolBar();
        mainPanel.add(toolBar, BorderLayout.NORTH);
        
        // Add components to main panel
        JLabel welcomeLabel = new JLabel("Bienvenido al Sistema GPPS", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Create buttons
        JButton conveniosBtn = new JButton("Convenios");
        JButton proyectosBtn = new JButton("Proyectos");
        
        // Style buttons
        Dimension buttonSize = new Dimension(120, 30);
        conveniosBtn.setPreferredSize(buttonSize);
        proyectosBtn.setPreferredSize(buttonSize);
        
        // Add action listeners
        conveniosBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad de Convenios en desarrollo");
        });
        
        proyectosBtn.addActionListener(e -> {
            ProyectosView proyectosView = new ProyectosView(proyectoDAO, usuarioDAO);
            proyectosView.setVisible(true);
        });
        
        // Add buttons to toolbar
        toolBar.add(conveniosBtn);
        toolBar.add(Box.createHorizontalStrut(10)); // Add spacing between buttons
        toolBar.add(proyectosBtn);
        
        return toolBar;
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("Archivo");
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Ayuda");
        JMenuItem aboutItem = new JMenuItem("Acerca de");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sistema GPPS\nVersión 1.0",
                "Acerca de",
                JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
}
