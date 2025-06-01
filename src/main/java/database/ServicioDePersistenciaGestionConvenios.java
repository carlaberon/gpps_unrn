package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import model.Convenio;
import model.GestorDeConvenios;


public class ServicioDePersistenciaGestionConvenios implements GestorDeConvenios {

	 public void create(Convenio convenio)  {
	        Connection conn;
	        PreparedStatement stmt;

	        try {
	            conn = Conn.getConnection();
	            stmt = conn.prepareStatement(
	                "INSERT INTO convenios (id_proyecto, id_entidad, fecha_inicio, fecha_fin, descripcion, activo) " +
	                "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

	            stmt.setInt(1, convenio.getIdProyecto());
	            stmt.setInt(2, convenio.getIdEntidad());
	            stmt.setDate(3, Date.valueOf(convenio.getFechaInicio()));
	            stmt.setDate(4, Date.valueOf(convenio.getFechaFin()));
	            stmt.setString(5, convenio.getDescripcion());
	            stmt.setBoolean(6, convenio.isActivo());

	            stmt.executeUpdate();

	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                convenio.setId(generatedKeys.getInt(1));
	            } else {
	                throw new DataBaseConnectionException("No se pudo obtener el ID generado del convenio.");
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("Error al crear el convenio: " + e.getMessage(), e);
	        } finally {
	            try {
	                Conn.disconnect();
	            } catch (Exception e) {
	                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage(), e);
	            }
	        }
	    }
	 
    public void updateArchivo(Convenio convenio) {
        PreparedStatement statement;
        Connection conn;

        try {
            conn = Conn.getConnection();

            statement = conn.prepareStatement(
                "UPDATE convenios SET archivopdf = ?, activo = ? WHERE id = ?");

            statement.setBytes(1, convenio.getArchivoPdf());
            statement.setBoolean(2, convenio.isActivo());
            statement.setInt(3, convenio.getId());

            int filas = statement.executeUpdate();

            if (filas <= 0) {
                throw new RuntimeException("No se pudo actualizar el archivo del convenio con ID: " + convenio.getId());
            }

        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException("Error al actualizar el archivo del convenio: " + e.getMessage(), e);
        } finally {
            try {
                Conn.disconnect();
            } catch (Exception e) {
                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
    }
    public Convenio buscarPorId(int id) {
        Connection conn;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            conn = Conn.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM convenios WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Convenio convenio = new Convenio(
                    rs.getInt("id_entidad_colaboradora"),
                    rs.getInt("id_proyecto"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin").toLocalDate()
                );
                convenio.setId(rs.getInt("id"));
                convenio.setArchivoPdf(rs.getBytes("archivoPdf"));
                if (rs.getBoolean("activo")) convenio.activar();
                return convenio;
            } else {
                throw new RuntimeException("No se encontró convenio con ID: " + id);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el convenio por ID: " + e.getMessage(), e);
        } finally {
            try {
                Conn.disconnect();
            } catch (Exception e) {
                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
    }
    @Override
    public byte[] obtenerArchivoPdfPorId(int id) {
        try {
            Connection conn = Conn.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT archivoPdf FROM convenios WHERE id = ?"
            );

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("archivoPdf");
            } else {
                throw new RuntimeException("No se encontró el convenio con ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el archivo PDF del convenio: " + e.getMessage(), e);
        } finally {
            try {
                Conn.disconnect();
            } catch (Exception e) {
                throw new RuntimeException("Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
    }

}

