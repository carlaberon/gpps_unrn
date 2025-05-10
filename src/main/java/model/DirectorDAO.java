package model;

import java.util.List;

public interface DirectorDAO {
    List<Director> obtenerTodos();
    Director obtenerPorId(int id);
} 