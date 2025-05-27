package database;

import model.Informe;
import model.ServiceInformes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

public class ServicioVerificacionInformes implements ServiceInformes {
    List<Informe> informes;

    public ServicioVerificacionInformes() {
        this.informes = new ArrayList<>();
    }

    @Override
    public boolean verificarInformeParcialAprobado(List<Integer> idInformesParciales) {

        Map<Integer, Boolean> estados = obtenerEstadosInformes(idInformesParciales);
        boolean hayInformes = !estados.isEmpty();
        boolean todosAprobados = estados.values().stream().allMatch(estado -> estado);
        return hayInformes && todosAprobados;
    }

    @Override
    public Map<Integer, Boolean> obtenerEstadosInformes(List<Integer> idsInforme) {
        if (idsInforme.isEmpty()) {
            return Collections.emptyMap();
        }

        String sql = "SELECT id_informe, estado FROM informes WHERE id_informe IN (" +
                idsInforme.stream().map(id -> "?").collect(Collectors.joining(", ")) + ")";

        try (Connection conn = Conn.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            for (int i = 0; i < idsInforme.size(); i++) {
                statement.setInt(i + 1, idsInforme.get(i));
            }

            ResultSet rs = statement.executeQuery();
            Map<Integer, Boolean> estados = new HashMap<>();

            while (rs.next()) {
                estados.put(rs.getInt("id_informe"), rs.getBoolean("estado"));
            }

            return estados;
        } catch (Exception e) {
            throw new RuntimeException("Problema al obtener los estados de los informes", e);
        }
    }


    @Override
    public void valorarInforme(int idInforme, int valor) {
        // TODO Auto-generated method stub

    }

    public void valorarInforme(Informe informe, int valor) {

    }

}
