package database;

import model.ConvenioDAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Convenio;


public class ConvenioDAOJDBC implements ConvenioDAO {

	 public void create(Convenio convenio) throws DataBaseConnectionException {
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
	        } catch (SQLException e) {
	            throw new DataBaseConnectionException("Error al crear convenio: " + e.getMessage());
	        } finally {
	            try {
	                Conn.disconnect();
	            } catch (SQLException e) {
	                throw new DataBaseConnectionException("Error al cerrar conexión.");
	            }
	        }
	    }
	 
    public void updateArchivo(Convenio convenio) throws DataBaseConnectionException {
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
                throw new DataBaseConnectionException("No se pudo actualizar el convenio con el archivo.");
            }

        } catch (SQLException e) {
        	e.printStackTrace();
            throw new DataBaseConnectionException("Error al actualizar el archivo del convenio.");
        } finally {
            try {
                Conn.disconnect();
            } catch (SQLException e) {
                throw new DataBaseConnectionException("Error al cerrar la conexión.");
            }
        }
    }
    public Convenio buscarPorId(int id) throws DataBaseConnectionException {
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
                throw new DataBaseConnectionException("Convenio no encontrado.");
            }

        } catch (SQLException e) {
            throw new DataBaseConnectionException("Error al buscar convenio.");
        } finally {
            try {
                Conn.disconnect();
            } catch (SQLException e) {
                throw new DataBaseConnectionException("Error al cerrar conexión.");
            }
        }
    }
    @Override
    public byte[] obtenerArchivoPdfPorId(int id) throws DataBaseConnectionException {
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
                throw new DataBaseConnectionException("No se encontró convenio con ID " + id);
            }
        } catch (SQLException e) {
            throw new DataBaseConnectionException("Error al obtener el PDF del convenio: " + e.getMessage());
        } finally {
            try {
                Conn.disconnect();
            } catch (SQLException e) {
                throw new DataBaseConnectionException("Error al cerrar conexión.");
            }
        }
    }

}

