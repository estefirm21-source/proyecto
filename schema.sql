-- ==========================================================
-- GreenCert - Carbon Footprint & ISO 14001 Audit
-- Relational Database Creation Script
-- ==========================================================

CREATE DATABASE IF NOT EXISTS greencert_db;
USE greencert_db;

-- 1. Company or Users Table (Ensuring integrity)
CREATE TABLE IF NOT EXISTS company (
    id_company INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    sector VARCHAR(50),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Emission Sources Catalog Table (Ensuring integrity)
CREATE TABLE IF NOT EXISTS emission_source (
    id_source INT AUTO_INCREMENT PRIMARY KEY,
    source_name VARCHAR(50) NOT NULL, -- Ex: Electricity, Fuel, Waste
    unit_measure VARCHAR(20) NOT NULL  -- Ex: kWh, Liters, Kg
);

-- 3. Historical Consumptions Table (With Foreign Keys and Enforced Integrity)
CREATE TABLE IF NOT EXISTS monthly_consumption (
    id_consumption INT AUTO_INCREMENT PRIMARY KEY,
    id_company INT NOT NULL,
    id_source INT NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    generated_co2 DECIMAL(10,2) NOT NULL,
    entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Integrity Constraints
    CONSTRAINT fk_company FOREIGN KEY (id_company) REFERENCES company(id_company) ON DELETE CASCADE,
    CONSTRAINT fk_source FOREIGN KEY (id_source) REFERENCES emission_source(id_source) ON DELETE CASCADE,
    
    -- Prevent duplicates for the same period/source
    CONSTRAINT uq_consumption_period UNIQUE (id_company, id_source, month, year),
    
    -- Data validations
    CONSTRAINT chk_month CHECK (month BETWEEN 1 AND 12),
    CONSTRAINT chk_year CHECK (year >= 2000),
    CONSTRAINT chk_amount CHECK (amount >= 0),
    CONSTRAINT chk_co2 CHECK (generated_co2 >= 0)
);

-- Indexes for report optimization
CREATE INDEX idx_consumption_date ON monthly_consumption(year, month);
CREATE INDEX idx_consumption_company ON monthly_consumption(id_company);

-- Default catalog insertion
INSERT INTO emission_source (source_name, unit_measure) VALUES 
('Electricity', 'kWh'),
('Fuel', 'Liters'),
('Waste', 'Kg')
ON DUPLICATE KEY UPDATE source_name=source_name;
