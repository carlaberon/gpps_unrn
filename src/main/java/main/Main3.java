package main;

import javax.swing.SwingUtilities;

import front.GenerarConvenio;
import model.Administrador;
import model.ConvenioDAO;

public class Main3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Administrador admin = new Administrador(0, null, null, null, null);
            ConvenioDAO dao = new database.ConvenioDAOJDBC();
            new GenerarConvenio(admin, dao).setVisible(true);
        });
    }
}
