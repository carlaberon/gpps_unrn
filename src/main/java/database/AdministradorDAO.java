package database;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdministradorDAO {

    public void cargarArchivoConvenio(int idConvenio, InputStream archivoPDF)
            throws DataBaseConnectionException {

        try (Connection conn = Conn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "UPDATE convenios SET archivo_pdf = ?, activo = TRUE WHERE id = ?")) {

            stmt.setBlob(1, archivoPDF);
            stmt.setInt(2, idConvenio);

            int filas = stmt.executeUpdate();
            if (filas == 0) {
                throw new DataBaseConnectionException("No se encontró el convenio para actualizar.");
            }

        } catch (SQLException e) {
            throw new DataBaseConnectionException("Error al guardar el archivo PDF del convenio.");
        }
    }
}

