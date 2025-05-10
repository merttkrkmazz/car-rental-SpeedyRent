-- Drop and create the SpeedyRent database
DROP DATABASE IF EXISTS srent;
CREATE DATABASE srent;
USE srent;

-- Users table (for login)
CREATE TABLE User (
                      user_id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50),
                      password VARCHAR(50)
);

-- Admin table (for login control)
CREATE TABLE Admin (
                       user_id INT PRIMARY KEY,
                       salary DOUBLE,
                       FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- Customer table (for login control)
CREATE TABLE Customer (
                          user_id INT PRIMARY KEY,
                          occupation VARCHAR(100),
                          FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- Vehicle Specifications table
CREATE TABLE VehicleSpecification (
                                      specification_id INT AUTO_INCREMENT PRIMARY KEY,
                                      color VARCHAR(20),
                                      fuel_type VARCHAR(20),
                                      transmission_type VARCHAR(20),
                                      seating_capacity INT
);

-- Cars table
CREATE TABLE Car (
                     car_id INT AUTO_INCREMENT PRIMARY KEY,
                     model VARCHAR(50),
                     daily_rent DOUBLE,
                     deposit DOUBLE,
                     mileage INT,
                     vehicle_status VARCHAR(20)
);

-- Car to VehicleSpecification relation
CREATE TABLE has (
                     car_id INT,
                     specification_id INT,
                     FOREIGN KEY (car_id) REFERENCES Car(car_id),
                     FOREIGN KEY (specification_id) REFERENCES VehicleSpecification(specification_id)
);

-- Admins managing cars
CREATE TABLE manages (
                         user_id INT,
                         car_id INT,
                         FOREIGN KEY (car_id) REFERENCES Car(car_id)
);

-- Booking table
CREATE TABLE Booking (
                         booking_id INT AUTO_INCREMENT PRIMARY KEY,
                         start_date DATE,
                         end_date DATE,
                         booking_status VARCHAR(20),
                         amount DOUBLE
);

-- Booking to car relation
CREATE TABLE reserves (
                          booking_id INT,
                          car_id INT,
                          FOREIGN KEY (booking_id) REFERENCES Booking(booking_id),
                          FOREIGN KEY (car_id) REFERENCES Car(car_id)
);

-- === Sample vehicle specifications ===
INSERT INTO VehicleSpecification (color, fuel_type, transmission_type, seating_capacity)
VALUES
    ('Black', 'Gasoline', 'Automatic', 5),
    ('White', 'Electric', 'Automatic', 4),
    ('Red', 'Diesel', 'Manual', 7);

-- === Sample cars ===
INSERT INTO Car (model, daily_rent, deposit, mileage, vehicle_status)
VALUES
    ('Toyota Corolla', 500.0, 1000.0, 25000, 'available'),
    ('Tesla Model 3', 1200.0, 2500.0, 10000, 'available'),
    ('Renault Traffic', 800.0, 1500.0, 40000, 'available');

-- === Assign specifications to cars ===
INSERT INTO has (car_id, specification_id) VALUES (1, 1), (2, 2), (3, 3);