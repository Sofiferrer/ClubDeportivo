-- Script de inicializacion de la Base de Datos ClubDeportivo

PRAGMA foreign_keys = ON;

-- ELIMINAR TABLAS EXISTENTES
DROP TABLE IF EXISTS pagos;
DROP TABLE IF EXISTS alumnos;
DROP TABLE IF EXISTS usuarios;

-- 1. TABLA DE USUARIOS (Administrativos)
CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL
);

-- 2. TABLA DE ALUMNOS (Socios y No Socios)
CREATE TABLE IF NOT EXISTS alumnos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    dni TEXT NOT NULL UNIQUE,
    es_socio INTEGER NOT NULL DEFAULT 0 CHECK (es_socio IN (0, 1)),
    apto_fisico INTEGER NOT NULL DEFAULT 0 CHECK (apto_fisico IN (0, 1)),
    fecha_alta TEXT NOT NULL DEFAULT (date('now')),
    fecha_vencimiento TEXT,
    habilitado INTEGER NOT NULL DEFAULT 1 CHECK (habilitado IN (0, 1))
);

-- 3. TABLA DE PAGOS
CREATE TABLE IF NOT EXISTS pagos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    alumno_id INTEGER NOT NULL,
    monto REAL NOT NULL,
    metodo_pago TEXT NOT NULL CHECK (metodo_pago IN ('Efectivo', 'Tarjeta')),
    cuotas INTEGER NOT NULL DEFAULT 1 CHECK (cuotas IN (1, 3, 6)),
    fecha_pago TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
    periodo_desde TEXT NOT NULL,
    periodo_hasta TEXT NOT NULL,
    FOREIGN KEY (alumno_id) REFERENCES alumnos(id) ON DELETE CASCADE
);

-- DATOS INICIALES (SEED)
INSERT OR IGNORE INTO usuarios (username, password, nombre, apellido)
VALUES ('admin', 'admin123', 'Carlos', 'Mendoza');

INSERT OR IGNORE INTO alumnos (nombre, apellido, dni, es_socio, apto_fisico, fecha_alta, fecha_vencimiento, habilitado)
VALUES ('Carlos', 'Mendoza', '33222111', 1, 1, '2025-03-01', '2026-10-31', 1);

INSERT OR IGNORE INTO alumnos (nombre, apellido, dni, es_socio, apto_fisico, fecha_alta, fecha_vencimiento, habilitado)
VALUES ('Ana', 'Gomez', '40111222', 1, 1, '2025-04-15', '2026-09-01', 1);

INSERT OR IGNORE INTO alumnos (nombre, apellido, dni, es_socio, apto_fisico, fecha_alta, fecha_vencimiento, habilitado)
VALUES ('Lucas', 'Martinez', '42333444', 0, 0, '2025-05-20', '2026-08-15', 1);

INSERT OR IGNORE INTO alumnos (nombre, apellido, dni, es_socio, apto_fisico, fecha_alta, fecha_vencimiento, habilitado)
VALUES ('Mario', 'Rossa', '35999888', 1, 1, date('now', '-30 days'), date('now'), 1);
