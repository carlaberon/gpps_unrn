package ui;

import model.GestorDeProyectos;
import model.Proyecto;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ListadoProyectos extends JFrame {

    private final GestorDeProyectos gestorDeProyectos;
    private JTable tabla;
    private JButton verProyectoBtn;

    public ListadoProyectos(GestorDeProyectos gestorDeProyectos) {
        super("Listado de Proyectos");
        this.gestorDeProyectos = gestorDeProyectos;

        initUI();
        cargarDatos();

        setSize(850, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initUI() {
        getContentPane().setBackground(new Color(0xBFBFBF));
        setLayout(new BorderLayout());

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
        tabla.setBackground(new Color(0xBFBFBF));
        tabla.setGridColor(Color.DARK_GRAY);
        tabla.setRowHeight(28);

        // Remarcar encabezados y usar letras blancas
        JTableHeader header = tabla.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.BOLD, 13f));
                setBackground(new Color(0x444444));
                setForeground(Color.WHITE);
                setOpaque(true);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) c).setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0x888888)));
                return c;
            }
        });

        // Centrar celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < cols.length; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Ocultar columna «id»
        tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(0));

        // Listener de selección
        tabla.getSelectionModel().addListSelectionListener(this::onSelectionChange);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(0xBFBFBF));
        scroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        add(scroll, BorderLayout.CENTER);

        // Botón inferior
        verProyectoBtn = new JButton("Ver Proyecto");
        verProyectoBtn.setEnabled(false);
        verProyectoBtn.addActionListener(e -> abrirProyectoSeleccionado());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(new Color(0xBFBFBF));
        south.add(verProyectoBtn);
        add(south, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        m.setRowCount(0);

        List<Proyecto> proyectos = gestorDeProyectos.obtenerProyectos();

        if (proyectos.isEmpty()) {
            m.addRow(new Object[m.getColumnCount()]);
            tabla.setEnabled(false);
            return;
        }

        tabla.setEnabled(true);

        for (Proyecto p : proyectos) {
            m.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.getAreaDeInteres(),
                    p.getUbicacion(),
                    (p.getEstado() ? "Aprobado" : "Pendiente"),
                    (p.estadoProyecto() != null ? p.estadoProyecto() : ""),
                    (p.tutorInterno() != null ? p.tutorInterno().nombre() : ""),
                    (p.tutorExterno() != null ? p.tutorExterno().nombre() : "")
            });
        }
    }

    private void onSelectionChange(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            verProyectoBtn.setEnabled(tabla.isEnabled() && tabla.getSelectedRow() != -1);
        }
    }

    private void abrirProyectoSeleccionado() {
        int viewRow = tabla.getSelectedRow();
        if (viewRow == -1) return;

        int modelRow = tabla.convertRowIndexToModel(viewRow);
        int idProyecto = (int) tabla.getModel().getValueAt(modelRow, 0);

        new DetalleProyecto(gestorDeProyectos, idProyecto).setVisible(true);
    }
}
