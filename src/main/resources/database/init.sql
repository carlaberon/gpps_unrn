-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS ProyectoFinalDB;
USE ProyectoFinalDB;

-- Tabla Usuario
CREATE TABLE IF NOT EXISTS Usuario (
    idUsuario INT PRIMARY KEY,
    nombreUsuario VARCHAR(50) NOT NULL,
    contrasenia VARCHAR(100) NOT NULL,
    nombre VARCHAR(100),
    email VARCHAR(100)
);

-- Tabla Estudiante
CREATE TABLE IF NOT EXISTS Estudiante (
    id_Usuario INT PRIMARY KEY,
    legajo VARCHAR(20) UNIQUE,
    esRegular BOOLEAN,
    FOREIGN KEY (id_Usuario) REFERENCES Usuario(idUsuario)
);

-- Tabla Administrador
CREATE TABLE IF NOT EXISTS Administrador (
    id_Usuario INT PRIMARY KEY,
    FOREIGN KEY (id_Usuario) REFERENCES Usuario(idUsuario)
);

-- Tabla Director
CREATE TABLE IF NOT EXISTS Director (
    id_Usuario INT PRIMARY KEY,
    FOREIGN KEY (id_Usuario) REFERENCES Usuario(idUsuario)
);

-- Tabla Tutor
CREATE TABLE IF NOT EXISTS Tutor (
    id_Usuario INT PRIMARY KEY,
    tipo VARCHAR(50),
    FOREIGN KEY (id_Usuario) REFERENCES Usuario(idUsuario)
);

-- Tabla Informe
CREATE TABLE IF NOT EXISTS Informe (
    id_informe INT PRIMARY KEY,
    descripcion TEXT,
    fecha_entrega DATE,
    tipo VARCHAR(50),
    valoracionInforme INTEGER,
    estado BOOLEAN,
    archivo TEXT
);

-- Tabla Actividad (superclase)
CREATE TABLE IF NOT EXISTS Actividad (
    id_actividad INT PRIMARY KEY,
    descripcion TEXT,
    fecha_inicio DATE,
    horas INT,
    estado VARCHAR(50)
);

-- Subclase SinInforme (hereda de Actividad)
CREATE TABLE IF NOT EXISTS SinInforme (
    id_actividad INT PRIMARY KEY,
    FOREIGN KEY (id_actividad) REFERENCES Actividad(id_actividad)
);

-- Subclase ConInforme (hereda de Actividad + relación con Informe)
CREATE TABLE IF NOT EXISTS ConInforme (
    id_actividad INT PRIMARY KEY,
    id_informe INT,
    FOREIGN KEY (id_actividad) REFERENCES Actividad(id_actividad),
    FOREIGN KEY (id_informe) REFERENCES Informe(id_informe)
);

-- Tabla Plan_De_Trabajo
CREATE TABLE IF NOT EXISTS Plan_De_Trabajo (
    id_plan INT PRIMARY KEY,
    proyecto_asignado VARCHAR(100),
    cant_horas INT,
    estado VARCHAR(50)
);

-- Tabla Plan_Actividad (relación N a N entre Plan_De_Trabajo y Actividad)
CREATE TABLE IF NOT EXISTS Plan_Actividad (
    id_plan INT,
    id_actividad INT,
    PRIMARY KEY (id_plan, id_actividad),
    FOREIGN KEY (id_plan) REFERENCES Plan_De_Trabajo(id_plan),
    FOREIGN KEY (id_actividad) REFERENCES Actividad(id_actividad)
);

-- Tabla Proyecto
CREATE TABLE IF NOT EXISTS Proyecto (
    id_proyecto INT PRIMARY KEY,
    nombre VARCHAR(100),
    descripcion TEXT,
    areaDeInteres VARCHAR(100),
    ubicacion VARCHAR(100),
    docenteSupervisor INT,
    idUsuario_director INT,
    idUsuario_estudiante INT,
    estado BOOLEAN,
    FOREIGN KEY (docenteSupervisor) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuario_director) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuario_estudiante) REFERENCES Usuario(idUsuario)
);

-- Tabla Entidad_Colaboradora
CREATE TABLE IF NOT EXISTS Entidad_Colaboradora (
    id_entidad_colaboradora INT PRIMARY KEY,
    cuit VARCHAR(20) UNIQUE,
    nombre VARCHAR(100),
    direccion_postal TEXT,
    correo VARCHAR(100),
    logo TEXT
);

-- Tabla Convenio
CREATE TABLE IF NOT EXISTS Convenio (
    id_proyecto INT,
    id_entidad_colaboradora INT,
    id_usuario INT,
    fecha_inicio DATE,
    fecha_fin DATE,
    descripcion TEXT,
    PRIMARY KEY (id_proyecto, id_entidad_colaboradora, id_usuario),
    FOREIGN KEY (id_proyecto) REFERENCES Proyecto(id_proyecto),
    FOREIGN KEY (id_entidad_colaboradora) REFERENCES Entidad_Colaboradora(id_entidad_colaboradora),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(idUsuario)
);

-- Insertar usuarios de ejemplo
-- Estudiantes
INSERT INTO Usuario (idUsuario, nombreUsuario, contrasenia, nombre, email) VALUES
(1, 'jperez', '123456', 'Juan Pérez', 'jperez@unrn.edu.ar'),
(2, 'mgarcia', '123456', 'María García', 'mgarcia@unrn.edu.ar'),
(3, 'lrodriguez', '123456', 'Luis Rodríguez', 'lrodriguez@unrn.edu.ar'),
(4, 'acastro', '123456', 'Ana Castro', 'acastro@unrn.edu.ar'),
(5, 'jgonzalez', '123456', 'José González', 'jgonzalez@unrn.edu.ar');

INSERT INTO Estudiante (id_Usuario, legajo, esRegular) VALUES
(1, '12345', TRUE),
(2, '23456', TRUE),
(3, '34567', TRUE),
(4, '45678', TRUE),
(5, '56789', TRUE);

-- Directores
INSERT INTO Usuario (idUsuario, nombreUsuario, contrasenia, nombre, email) VALUES
(6, 'dperez', '123456', 'Dr. Diego Pérez', 'dperez@unrn.edu.ar'),
(7, 'mgomez', '123456', 'Dra. Marta Gómez', 'mgomez@unrn.edu.ar'),
(8, 'jhernandez', '123456', 'Dr. Jorge Hernández', 'jhernandez@unrn.edu.ar');

INSERT INTO Director (id_Usuario) VALUES
(6),
(7),
(8);

-- Tutores
INSERT INTO Usuario (idUsuario, nombreUsuario, contrasenia, nombre, email) VALUES
(9, 'tmartinez', '123456', 'Prof. Tomás Martínez', 'tmartinez@unrn.edu.ar'),
(10, 'lfernandez', '123456', 'Prof. Laura Fernández', 'lfernandez@unrn.edu.ar'),
(11, 'rgonzalez', '123456', 'Prof. Roberto González', 'rgonzalez@unrn.edu.ar'),
(12, 'msanchez', '123456', 'Prof. María Sánchez', 'msanchez@unrn.edu.ar');

INSERT INTO Tutor (id_Usuario, tipo) VALUES
(9, 'Tutor Académico'),
(10, 'Tutor Industrial'),
(11, 'Tutor Académico'),
(12, 'Tutor Industrial');

-- Insertar un proyecto de ejemplo
INSERT INTO Proyecto (id_proyecto, nombre, descripcion, areaDeInteres, ubicacion, 
                     docenteSupervisor, idUsuario_director, idUsuario_estudiante, estado) 
VALUES (1, 'Sistema de Gestión de Proyectos', 
        'Desarrollo de un sistema para gestionar proyectos de la universidad',
        'Desarrollo de Software', 'Laboratorio de Informática',
        9, 6, 1, TRUE); 