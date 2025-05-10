-- Veritabanı oluşturma
DROP DATABASE IF EXISTS srent;
CREATE DATABASE srent;
USE srent;

-- Kullanıcılar
CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50)
);

-- Admin
CREATE TABLE Admin (
    user_id INT PRIMARY KEY,
    salary DOUBLE,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- Customer
CREATE TABLE Customer (
    user_id INT PRIMARY KEY,
    occupation VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- Kart bilgisi (zorunlu değil ama 2'de vardı)
CREATE TABLE Card (
    card_id INT AUTO_INCREMENT PRIMARY KEY,
    card_brand ENUM('Visa','Mastercard','Maestro','Troy','Other'),
    card_number CHAR(16),
    exp_date DATE,
    name_on_card VARCHAR(60)
);

-- Teknik Özellikler
CREATE TABLE VehicleSpecification (
    specification_id INT AUTO_INCREMENT PRIMARY KEY,
    color VARCHAR(20),
    fuel_type VARCHAR(20),
    transmission_type VARCHAR(20),
    seating_capacity INT
);

-- Araçlar
CREATE TABLE Car (
    car_id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(50),
    daily_rent DOUBLE,
    deposit DOUBLE,
    mileage INT,
    vehicle_status VARCHAR(20)
);

-- has ilişkisi
CREATE TABLE has (
    car_id INT,
    specification_id INT,
    FOREIGN KEY (car_id) REFERENCES Car(car_id),
    FOREIGN KEY (specification_id) REFERENCES VehicleSpecification(specification_id)
);

-- manages ilişkisi
CREATE TABLE manages (
    user_id INT,
    car_id INT,
    FOREIGN KEY (car_id) REFERENCES Car(car_id)
);

-- Rezervasyonlar
CREATE TABLE Booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    start_date DATE,
    end_date DATE,
    booking_status VARCHAR(20),
    secure_deposit DOUBLE, -- booking için gerekli
    amount DOUBLE,
    drive_option ENUM('self','chauffeur') DEFAULT 'self',
    reading INT,
    date_out DATE
);

-- makes ilişkisi (kullanıcı ile booking arasında)
CREATE TABLE makes (
    user_id INT,
    booking_id INT,
    PRIMARY KEY (user_id, booking_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (booking_id) REFERENCES Booking(booking_id)
);

-- reserves ilişkisi
CREATE TABLE reserves (
    booking_id INT,
    car_id INT,
    FOREIGN KEY (booking_id) REFERENCES Booking(booking_id),
    FOREIGN KEY (car_id) REFERENCES Car(car_id)
);B;
