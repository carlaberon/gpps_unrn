package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Convenio;


public class ConvenioDAO {

    public void create(Convenio convenio) throws DataBaseConnectionException {
        PreparedStatement statement;
        Connection conn;

        try {
        	 conn = Conn.getConnection();

            statement = conn.prepareStatement(
                "INSERT INTO convenio (id_entidad, id_proyecto, descripcion, fecha_inicio, fecha_fin) " +
                "VALUES (?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, convenio.getIdEntidad());
            statement.setInt(2, convenio.getIdProyecto());
            statement.setString(3, convenio.getDescripcion());
            statement.setDate(4, Date.valueOf(convenio.getFechaInicio()));
            statement.setDate(5, Date.valueOf(convenio.getFechaFin()));
            /*
            int cantidad = statement.executeUpdate();
            
            
            if (cantidad <= 0) {
                throw new DataBaseInsertionException("No se pudo insertar el convenio.");
            }
*/
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                convenio.setId(idGenerado);  
            }
        } catch (SQLException e) {
            throw new DataBaseConnectionException("Error al conectar con la base de datos.");
        } finally {
            try {
            	Conn.disconnect();
            } catch (SQLException e) {
                throw new DataBaseConnectionException("Error al cerrar la conexión.");
            }
        }
    }
    public void updateArchivo(Convenio convenio) throws DataBaseConnectionException {
        PreparedStatement statement;
        Connection conn;

        try {
            conn = Conn.getConnection();

            statement = conn.prepareStatement(
                "UPDATE convenio SET archivo_pdf = ?, activo = ? WHERE id = ?");

            statement.setBytes(1, convenio.getArchivoPdf());
            statement.setBoolean(2, convenio.isActivo());
            statement.setInt(3, convenio.getId());

            int filas = statement.executeUpdate();

            if (filas <= 0) {
                throw new DataBaseConnectionException("No se pudo actualizar el convenio con el archivo.");
            }

        } catch (SQLException e) {
            throw new DataBaseConnectionException("Error al actualizar el archivo del convenio.");
        } finally {
            try {
                Conn.disconnect();
            } catch (SQLException e) {
                throw new DataBaseConnectionException("Error al cerrar la conexión.");
            }
        }
    }

}
