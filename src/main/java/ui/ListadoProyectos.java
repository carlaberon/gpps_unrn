package ui;

import model.GestorDeProyectos;
import model.Proyecto;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListadoProyectos extends JFrame {

    private final GestorDeProyectos gestorDeProyectos;
    private JTable tabla;
    private JButton verProyectoBtn;

    // --------------------------- ctor --------------------------------------
    public ListadoProyectos(GestorDeProyectos gestorDeProyectos) {
        super("Listado de Proyectos");
        this.gestorDeProyectos = gestorDeProyectos;

        initUI();
        cargarDatos();

        setSize(850, 420);          // Ventana amplia (más en horizontal)
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // --------------------------- UI ----------------------------------------
    private void initUI() {
        /* Modelo de tabla:
           Columna 0 -> id (oculta)
           Resto     -> visibles al usuario                              */
        String[] cols = {
                "id", "Nombre", "Descripción", "Área de interés",
                "Ubicación", "Estado de aprobación",
                "Estado del proyecto", "Tutor interno", "Tutor externo"
        };

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(model);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ocultar columna «id»
        tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(0));

        // Listener para habilitar o no el botón
        tabla.getSelectionModel().addListSelectionListener(this::onSelectionChange);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón «Ver Proyecto»
        verProyectoBtn = new JButton("Ver Proyecto");
        verProyectoBtn.setEnabled(false);
        verProyectoBtn.addActionListener(e -> abrirProyectoSeleccionado());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(verProyectoBtn);
        add(south, BorderLayout.SOUTH);
    }

    // -------------------- Carga de proyectos -------------------------------
    private void cargarDatos() {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        m.setRowCount(0);                     // Limpiar

        List<Proyecto> proyectos = gestorDeProyectos.obtenerProyectos();

        if (proyectos.isEmpty()) {
            /* Insertamos una fila vacía para que el header no “flote” solo
               y deshabilitamos la tabla para evitar selección fantasma.     */
            m.addRow(new Object[m.getColumnCount()]);
            tabla.setEnabled(false);
            return;
        }

        tabla.setEnabled(true);

        for (Proyecto p : proyectos) {
            m.addRow(new Object[]{
                    p.getId(),                                  // id (oculto)
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getAreaDeInteres(),
                    p.getUbicacion(),
                    (p.getEstado() ? "Aprobado" : "Pendiente"), // boolean -> texto
                    (p.estadoProyecto() != null ? p.estadoProyecto() : ""),
                    // Acceso al nombre de los tutores
                    (p.tutorInterno() != null ? p.tutorInterno().nombre() : ""),
                    (p.tutorExterno() != null ? p.tutorExterno().nombre() : "")
            });
        }
    }

    // -------------------- Cambios de selección -----------------------------
    private void onSelectionChange(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            verProyectoBtn.setEnabled(tabla.isEnabled() && tabla.getSelectedRow() != -1);
        }
    }

    // ----------------- Acción «Ver Proyecto» -------------------------------
    private void abrirProyectoSeleccionado() {
        int viewRow = tabla.getSelectedRow();
        if (viewRow == -1) return;

        // Convertimos índice de vista -> modelo (por la columna oculta)
        int modelRow = tabla.convertRowIndexToModel(viewRow);
        int idProyecto = (int) tabla.getModel().getValueAt(modelRow, 0);

        new DetalleProyecto(gestorDeProyectos, idProyecto).setVisible(true);

        JOptionPane.showMessageDialog(
                this,
                "Abrir detalles del proyecto con id = " + idProyecto,
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

