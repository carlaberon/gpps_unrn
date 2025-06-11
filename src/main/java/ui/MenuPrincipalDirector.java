package ui;

import model.GestorDeProyectos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipalDirector extends JFrame {
    private GestorDeProyectos gestorDeProyectos;

    public MenuPrincipalDirector(GestorDeProyectos gestorDeProyectos) {
        this.gestorDeProyectos = gestorDeProyectos;
        setTitle("Menú");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 180);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // Crear botones
        JButton aprobarBtn = new JButton("Aprobar proyectos");
        JButton verListadoBtn = new JButton("Ver Listado de Proyectos");

        // Agregar listeners
        aprobarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AprobacionDeProyectos(gestorDeProyectos).setVisible(true);
            }
        });

        verListadoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ListadoProyectos(gestorDeProyectos).setVisible(true);
            }
        });

        // Panel de botones con espacio y centrado
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new BoxLayout(botonesPanel, BoxLayout.Y_AXIS));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Espacio alrededor

        aprobarBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        verListadoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonesPanel.add(aprobarBtn);
        botonesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre botones
        botonesPanel.add(verListadoBtn);

        // Panel contenedor para centrar en toda la ventana
        JPanel container = new JPanel(new GridBagLayout());
        container.add(botonesPanel);

        add(container);
    }


}

