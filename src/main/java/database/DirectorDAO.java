package database;

import model.Director;
import model.Usuario;

import java.util.List;

public interface DirectorDAO {
    List<Usuario> obtenerTodos();
    Director obtenerPorId(int id);
} 