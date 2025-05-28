package front;

import database.ActividadDAOJDBC;
import model.Actividad;
import model.ActividadDAO;
import model.PlanDeTrabajo;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VentanaProgresoPlan extends JFrame {

    private JProgressBar barraProgreso;
    private JTable tablaActividades;
    private ActividadDAO actividadDAO;
    private PlanDeTrabajo plan;

    public VentanaProgresoPlan(PlanDeTrabajo plan) {
        this.plan = plan;
        this.actividadDAO = new ActividadDAOJDBC();

        setTitle("Progreso del Plan de Trabajo");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setValue((int) plan.calcularProgreso());
        barraProgreso.setStringPainted(true);

        tablaActividades = new JTable(new ActividadesTableModel(plan.getActividades()));
        tablaActividades.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tablaActividades.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(barraProgreso, BorderLayout.NORTH);
        add(new JScrollPane(tablaActividades), BorderLayout.CENTER);
    }

    class ActividadesTableModel extends AbstractTableModel {
        private final String[] columnas = {"Descripción", "Fecha Inicio", "Horas", "Estado", "Acción"};
        private final List<Actividad> actividades;

        public ActividadesTableModel(List<Actividad> actividades) {
            this.actividades = actividades;
        }

        @Override
        public int getRowCount() {
            return actividades.size();
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnas[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Actividad act = actividades.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> act.getDescripcion();
                case 1 -> act.getFechaInicio();
                case 2 -> act.horas();
                case 3 -> act.isEstado() ? "Completada" : "Pendiente";
                case 4 -> "Marcar como completada";
                default -> null;
            };
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 4 && !actividades.get(row).isEstado();
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                Actividad actividad = plan.getActividades().get(selectedRow);
                actividad.setEstado(true);
                try {
                    actividadDAO.marcarComoCompletado(actividad.getIdActividad());
                    barraProgreso.setValue((int) plan.calcularProgreso());
                    tablaActividades.repaint();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar la base de datos: " + ex.getMessage());
                    System.out.println("ID Actividad: " + actividad.getIdActividad());
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
