package ui;

import model.GestorDeProyectos;
import model.Proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ProyectosACargo extends JFrame {
    private GestorDeProyectos gestorDeProyectos;
    private int idUsuario;

    public ProyectosACargo(GestorDeProyectos gestor, int idUsuario) {
        this.gestorDeProyectos = gestor;
        this.idUsuario = idUsuario;

        setTitle("Proyectos a cargo");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color fondo = Color.decode("#BFBFBF");
        getContentPane().setBackground(fondo);
        setLayout(new BorderLayout());

        // Obtener proyectos
        List<Proyecto> proyectos = gestorDeProyectos.listarProyectosRelacionados(idUsuario);

        // Modelo de tabla
        String[] columnas = {"ID", "Nombre", "Descripción"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Proyecto p : proyectos) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion()
            });
        }

        // Crear tabla
        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(40);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setShowGrid(true);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(160, 160, 160)); // gris medio
        tabla.setBackground(fondo);
        tabla.setSelectionBackground(new Color(184, 207, 229));

        // Estilo de cabecera
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(64, 64, 64));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.LEFT);

        // Ocultar columna de ID
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(fondo);

        // Panel norte con label más marcado
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNorte.setBackground(fondo);
        JLabel lbl = new JLabel("Seleccione un proyecto:");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(new Color(40, 40, 40));
        panelNorte.add(lbl);

        // Panel sur con botón
        JButton btnVerProyecto = new JButton("Ver proyecto");
        btnVerProyecto.setEnabled(false);
        JPanel panelSur = new JPanel();
        panelSur.setBackground(fondo);
        panelSur.add(btnVerProyecto);

        // Habilitar botón solo si hay selección
        tabla.getSelectionModel().addListSelectionListener(e -> {
            btnVerProyecto.setEnabled(tabla.getSelectedRow() != -1);
        });

        // Acción del botón
        btnVerProyecto.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int idProyecto = (int) modelo.getValueAt(fila, 0);
                new VerProyectoInformes(gestorDeProyectos, idProyecto);
            }
        });

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        setVisible(true);
    }
}

