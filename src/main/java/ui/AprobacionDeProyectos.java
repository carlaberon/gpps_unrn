package ui;

import model.GestorDeProyectos;
import model.Proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AprobacionDeProyectos extends JFrame {
    private GestorDeProyectos gestorDeProyectos;

    public AprobacionDeProyectos(GestorDeProyectos gestor) {
        this.gestorDeProyectos = gestor;

        setTitle("Proyectos para aprobar");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Obtener proyectos no aprobados
        List<Proyecto> proyectos = gestorDeProyectos.obtenerProyectosSinAprobar();

        // Modelo de tabla
        String[] columnas = {"ID", "Nombre", "Descripción"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Proyecto p : proyectos) {
            modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getDescripcion()});
        }

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);

        // Ocultar ID visualmente
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);

        // Botones
        JButton btnVer = new JButton("Ver proyecto");
        JButton btnAprobar = new JButton("Aprobar proyecto");
        JButton btnDenegar = new JButton("Denegar proyecto");

        btnVer.setEnabled(false);
        btnAprobar.setEnabled(false);
        btnDenegar.setEnabled(false);

        // Activar botones al seleccionar fila
        tabla.getSelectionModel().addListSelectionListener(e -> {
            boolean filaSeleccionada = tabla.getSelectedRow() != -1;
            btnVer.setEnabled(filaSeleccionada);
            btnAprobar.setEnabled(filaSeleccionada);
            btnDenegar.setEnabled(filaSeleccionada);
        });

        // Acción Ver Proyecto
        btnVer.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int id = (int) modelo.getValueAt(fila, 0);
                new VerProyectoDirector(gestorDeProyectos, id);
            }
        });

        // Acción Aprobar Proyecto
        btnAprobar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int id = (int) modelo.getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "¿Estás seguro de que deseas aprobar este proyecto?",
                        "Confirmar aprobación",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    gestorDeProyectos.aprobarPlanDeTrabajo(id);
                    modelo.removeRow(fila);
                    JOptionPane.showMessageDialog(this, "Proyecto aprobado correctamente.");
                }
            }
        });

        // Acción Denegar Proyecto
        btnDenegar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "¿Estás seguro de que deseas denegar este proyecto?",
                        "Confirmar denegación",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // Mini-frame para comentario
                    JFrame comentarioFrame = new JFrame("Proyecto denegado");
                    comentarioFrame.setSize(350, 150);
                    comentarioFrame.setLocationRelativeTo(this);
                    comentarioFrame.setLayout(new BorderLayout());
                    comentarioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    JPanel panel = new JPanel(new BorderLayout(5, 5));
                    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    panel.add(new JLabel("Comentario:"), BorderLayout.NORTH);

                    JTextField campoComentario = new JTextField();
                    panel.add(campoComentario, BorderLayout.CENTER);

                    JButton btnEnviar = new JButton("Enviar");
                    btnEnviar.addActionListener(e2 -> {
                        String comentario = campoComentario.getText().trim();
                        if (!comentario.isEmpty()) {
                            int id = (int) modelo.getValueAt(fila, 0);
                            gestorDeProyectos.notificarComentarioDenegacion(comentario);
                            modelo.removeRow(fila);
                            comentarioFrame.dispose();
                            gestorDeProyectos.denegarProyecto(id);
                            JOptionPane.showMessageDialog(this, "Proyecto denegado con comentario: " + comentario);
                        } else {
                            JOptionPane.showMessageDialog(comentarioFrame, "Debe ingresar un comentario.");
                        }
                    });

                    comentarioFrame.add(panel, BorderLayout.CENTER);
                    comentarioFrame.add(btnEnviar, BorderLayout.SOUTH);
                    comentarioFrame.setVisible(true);
                }
            }
        });

        // Panel inferior con distribución personalizada
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelInferior.add(btnVer, BorderLayout.WEST);

        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerecha.add(btnDenegar);
        panelDerecha.add(btnAprobar);
        panelInferior.add(panelDerecha, BorderLayout.EAST);

        // Agregar al frame
        add(new JLabel("Seleccione un proyecto pendiente de aprobación:"), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }
}

