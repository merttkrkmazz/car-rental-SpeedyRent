-- Veritabanı oluşturma
CREATE DATABASE IF NOT EXISTS speedyrent;
USE speedyrent;

-- Araçlar tablosu
CREATE TABLE IF NOT EXISTS cars (
    id INT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(50),
    model VARCHAR(50),
    fuel_type VARCHAR(20),
    transmission VARCHAR(20),
    seating_capacity INT,
    rental_price DOUBLE
);

-- Örnek araç verileri
INSERT INTO cars (brand, model, fuel_type, transmission, seating_capacity, rental_price) VALUES
('Toyota', 'Corolla', 'Gasoline', 'Automatic', 5, 45.0),
('Honda', 'Civic', 'Diesel', 'Manual', 5, 40.0),
('Tesla', 'Model 3', 'Electric', 'Automatic', 5, 70.0);
