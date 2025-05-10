package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Convenio;
import model.ConvenioDAO;


public class ConvenioDAOJDBC implements ConvenioDAO {

    @Override
    public void create(Convenio convenio) {
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
        } catch (Exception e) {
            try {
                throw new DataBaseConnectionException("Error al conectar con la base de datos.");
            } catch (DataBaseConnectionException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
            	Conn.disconnect();
            } catch (Exception e) {
                try {
                    throw new DataBaseConnectionException("Error al cerrar la conexión.");
                } catch (DataBaseConnectionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
    @Override
    public void updateArchivo(Convenio convenio)  {
        PreparedStatement statement;
        Connection conn;

        try {
            conn = Conn.getConnection();

            statement = conn.prepareStatement(
                "UPDATE convenio SET archivo_pdf = ?, activo = ? WHERE id = ?");

            statement.setBytes(1, convenio.getArchivoPdf());
            statement.setBoolean(2, convenio.isActivo());
            statement.setInt(3, convenio.getId());


            if (statement.executeUpdate() <= 0) {
                throw new RuntimeException("error");
            }

        } catch (SQLException e) {
            throw new RuntimeException("error");
        } finally {
            try {
                Conn.disconnect();
            } catch (Exception e) {
                throw new RuntimeException("Error al cerrar la conexión.", e);
            }
        }
    }

}
