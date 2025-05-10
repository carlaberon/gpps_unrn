package database;

import model.Tutor;
import java.util.List;

public interface TutorDAO {
    List<Tutor> obtenerTodos();
    Tutor obtenerPorId(int id);
} 