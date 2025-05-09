package database;

import model.Director;
import java.util.List;

public interface DirectorDAO {
    List<Director> obtenerTodos();
    Director obtenerPorId(int id);
} 