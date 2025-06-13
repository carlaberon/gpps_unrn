package ui;

import model.GestorDeProyectos;
import model.Proyecto;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

        // Pinta el fondo del frame
        getContentPane().setBackground(new Color(0xBFBFBF));

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

        // Pinta el fondo de la tabla
        tabla.setBackground(new Color(0xE0E0E0));
        tabla.setGridColor(new Color(0x555555)); // Color más oscuro para las líneas

        // Pinta solo las líneas horizontales y con mayor grosor
        tabla.setShowGrid(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 2)); // Espacio vertical entre celdas para remarcar líneas horizontales

        // Ocultar ID visualmente (columna 0)
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        // Remarcar cabecera con fondo oscuro y letra blanca negrita
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(0x3A3A3A));
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        header.setBorder(new LineBorder(new Color(0x222222), 2)); // borde más marcado en el header

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(0xBFBFBF)); // Fondo del área visible del scroll igual que frame

        // Borde visible y oscuro alrededor del JScrollPane
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x333333), 2));

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
                    gestorDeProyectos.aprobarProyecto(id);
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
        panelInferior.setBackground(new Color(0xBFBFBF));

        panelInferior.add(btnVer, BorderLayout.WEST);

        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDerecha.setBackground(new Color(0xBFBFBF));
        panelDerecha.add(btnDenegar);
        panelDerecha.add(btnAprobar);
        panelInferior.add(panelDerecha, BorderLayout.EAST);

        // Agregar al frame
        JLabel etiqueta = new JLabel("Seleccione un proyecto pendiente de aprobación:");
        etiqueta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(etiqueta, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }
}
