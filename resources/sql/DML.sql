-- === Örnek Kullanıcılar ===
INSERT INTO user (username, password) VALUES 
('admin1', 'adminpass'),
('customer1', 'custpass'),
('customer2', 'custpass');

-- === Admin ===
INSERT INTO Admin (user_id, salary) VALUES 
(1, 8000.00);

-- === Customer ===
INSERT INTO Customer (user_id, occupation) VALUES 
(2, 'Engineer'),
(3, 'Student');

-- === Kartlar (isteğe bağlı kullanılabilir) ===
INSERT INTO Card (card_brand, card_number, exp_date, name_on_card) VALUES
('Visa', '1111222233334444', '2027-12-31', 'Customer One'),
('Mastercard', '5555666677778888', '2026-11-30', 'Customer Two');

-- === Teknik Özellikler ===
INSERT INTO VehicleSpecification (color, fuel_type, transmission_type, seating_capacity) VALUES
('Black', 'Gasoline', 'Automatic', 5),
('White', 'Electric', 'Automatic', 4),
('Red', 'Diesel', 'Manual', 7);

-- === Araçlar ===
INSERT INTO Car (model, daily_rent, deposit, mileage, vehicle_status) VALUES
('Toyota Corolla', 500.0, 1000.0, 25000, 'available'),
('Tesla Model 3', 1200.0, 2500.0, 10000, 'available'),
('Renault Traffic', 800.0, 1500.0, 40000, 'available');

-- === Araçlara teknik özellik ata ===
INSERT INTO has (car_id, specification_id) VALUES 
(1, 1),
(2, 2),
(3, 3);

-- === Admin araç yönetimi (isteğe bağlı) ===
INSERT INTO manages (user_id, car_id) VALUES 
(1, 1),
(1, 2),
(1, 3);
