-- ==========================================================
-- GreenCert - Auditoría de Huella de Carbono e ISO 14001
-- Script de Creación de Base de Datos relacional
-- ==========================================================

CREATE DATABASE IF NOT EXISTS greencert_db;
USE greencert_db;

-- 1. Tabla de Empresas o Usuarios (Asegurando integridad)
CREATE TABLE IF NOT EXISTS empresa (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    sector VARCHAR(50),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla del Catálogo de Fuentes de Emisión (Asegurando integridad)
CREATE TABLE IF NOT EXISTS fuente_emision (
    id_source INT AUTO_INCREMENT PRIMARY KEY,
    nombre_fuente VARCHAR(50) NOT NULL, -- Ej: Electricidad, Combustible, Residuos
    unidad_medida VARCHAR(20) NOT NULL  -- Ej: kWh, Litros, Kg
);

-- 3. Tabla de Consumos Históricos (Con Llaves Foráneas e Integridad Reforzada)
CREATE TABLE IF NOT EXISTS consumo_mensual (
    id_consumo INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT NOT NULL,
    id_source INT NOT NULL,
    mes INT NOT NULL,
    anio INT NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    co2_generado DECIMAL(10,2) NOT NULL,
    fecha_ingreso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Restricciones de Integridad
    CONSTRAINT fk_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa) ON DELETE CASCADE,
    CONSTRAINT fk_fuente FOREIGN KEY (id_source) REFERENCES fuente_emision(id_source) ON DELETE CASCADE,
    
    -- Evitar duplicados para el mismo periodo/fuente
    CONSTRAINT uq_consumo_periodo UNIQUE (id_empresa, id_source, mes, anio),
    
    -- Validaciones de datos
    CONSTRAINT chk_mes CHECK (mes BETWEEN 1 AND 12),
    CONSTRAINT chk_anio CHECK (anio >= 2000),
    CONSTRAINT chk_cantidad CHECK (cantidad >= 0),
    CONSTRAINT chk_co2 CHECK (co2_generado >= 0)
);

-- Índices para optimización de reportes
CREATE INDEX idx_consumo_fecha ON consumo_mensual(anio, mes);
CREATE INDEX idx_consumo_empresa ON consumo_mensual(id_empresa);

-- Inserción de catálogo por defecto
INSERT INTO fuente_emision (nombre_fuente, unidad_medida) VALUES 
('Electricidad', 'kWh'),
('Combustible', 'Litros'),
('Residuos', 'Kg')
ON DUPLICATE KEY UPDATE nombre_fuente=nombre_fuente;
