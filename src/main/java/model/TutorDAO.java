package model;

import java.util.List;

public interface TutorDAO {
    List<Tutor> obtenerTodos();
    Tutor obtenerPorId(int id);
} 