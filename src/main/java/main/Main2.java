package main;


import front.CargarConvenio;

import javax.swing.SwingUtilities;

import database.ConvenioDAOJDBC;

public class Main2 {
    public static void main(String[] args) {
        ConvenioDAOJDBC dao = new ConvenioDAOJDBC();
        SwingUtilities.invokeLater(() -> new CargarConvenio(dao));
    }
}