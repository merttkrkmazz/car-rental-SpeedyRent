INSERT INTO user (username, password) VALUES
  ('jdoe',     'john1234'),
  ('akar',     'ayse2024'),
  ('lwang',    'liWpass!'),
  ('erossi',   'elena88'),
  ('ademir',   'ahmet1907'),
  ('sgarcia',  'sofiaES'),
  ('akim',     'alexkr'),
  ('mjohnson', 'mikeJ!23'),
  ('btanaka',  'bTanaka456'),
  ('cnovak',   'cno_vak99'),
  ('dfischer', 'fischerDE'),
  ('eric.y',   'eYuan82'),
  ('giulia.b', 'giuliab07'),
  ('hasan.k',  'hasanTR'),
  ('nina.r',   'nina_r88');


INSERT INTO Admin (user_id, salary) VALUES 
(1, 8000.00),
(3, 9000.00);


INSERT INTO Customer (user_id, occupation) VALUES 
  (2,  'Graphic Designer'),
  (4,  'Marketing Specialist'),
  (5,  'Software Engineer'),
  (6,  'Economist'),
  (7,  'Data Analyst'),
  (8,  'IT Consultant'),
  (9,  'Project Manager'),
  (10, 'UX Designer'),
  (11, 'Mechanical Engineer'),
  (12, 'Research Assistant'),
  (13, 'Civil Engineer'),
  (14, 'Electrician'),
  (15, 'Journalist');



INSERT INTO VehicleSpecification (color, fuel_type, transmission_type, seating_capacity)
VALUES 
  ('Red',        'petrol',     'manual',    5),
  ('Black',      'diesel',     'automatic', 4),
  ('White',      'hybrid',     'automatic', 5),
  ('Blue',       'gasoline',   'automatic', 4),
  ('Grey',       'petrol',     'automatic', 5),
  ('Silver',     'diesel',     'manual',    4),
  ('Green',      'gasoline',   'automatic', 2),
  ('Yellow',     'petrol',     'manual',    2),
  ('Orange',     'hybrid',     'manual',    5),
  ('Beige',      'diesel',     'automatic', 7),
  ('Brown',      'petrol',     'automatic', 5),
  ('Navy',       'hybrid',     'automatic', 5),
  ('Maroon',     'diesel',     'manual',    4),
  ('Purple',     'gasoline',   'automatic', 2),
  ('White',      'petrol',     'manual',    4),
  ('Black',      'hybrid',     'manual',    5),
  ('Blue',       'diesel',     'automatic', 7),
  ('Green',      'petrol',     'automatic', 5),
  ('Silver',     'gasoline',   'automatic', 4),
  ('Red',        'hybrid',     'automatic', 6),
  ('Cyan',       'petrol',     'automatic', 4),
  ('Dark Grey',  'diesel',     'manual',    5),
  ('Ivory',      'hybrid',     'manual',    4),
  ('Turquoise',  'gasoline',   'automatic', 5),
  ('Coral',      'petrol',     'automatic', 2),
  ('Amber',      'diesel',     'manual',    4),
  ('Teal',       'gasoline',   'manual',    5),
  ('Charcoal',   'hybrid',     'automatic', 5),
  ('Olive',      'diesel',     'automatic', 7),
  ('Mint',       'petrol',     'manual',    5),
  ('Lavender',   'hybrid',     'automatic', 5),
  ('Gold',       'gasoline',   'manual',    4),
  ('Steel Blue', 'diesel',     'automatic', 6),
  ('Champagne',  'petrol',     'manual',    4);


/* ---------- INSERT sample cars ---------- */
INSERT INTO car (model, daily_rent, deposit, mileage, vehicle_status)
VALUES
  ('Peugeot 208 1.2 PureTech',     36.00, 350.00, 58700, 'rented'),
  ('Volkswagen Passat 1.5 TSI',    62.00, 600.00, 42300, 'rented'),
  ('Hyundai Tucson 1.6',           73.00, 700.00, 47800, 'rented'),
  ('Citroën C4 Cactus 1.2',        41.00, 400.00, 51200, 'rented'),
  ('Toyota Camry 2.5 Hybrid',      92.00, 850.00, 28900, 'rented'),
  ('Honda Jazz 1.3 i-VTEC',        39.00, 350.00, 68300, 'available'),
  ('Ford Kuga 1.5 EcoBoost',       70.00, 680.00, 40100, 'service'),
  ('Renault Megane 1.3 TCe',       49.00, 500.00, 37600, 'available'),
  ('Fiat Panda 1.2',               29.00, 250.00, 72300, 'available'),
  ('BMW X1 1.5 sDrive',            98.00, 850.00, 31400, 'rented'),
  ('Mercedes-Benz GLA 200',       112.00, 950.00, 28700, 'available'),
  ('Nissan Micra 1.0 IG-T',        34.00, 300.00, 64500, 'available'),
  ('Skoda Octavia 1.0 TSI',        52.00, 500.00, 48800, 'service'),
  ('Mitsubishi ASX 1.6',           54.00, 500.00, 46300, 'available'),
  ('Kia Ceed 1.4 MPI',             46.00, 450.00, 51000, 'available'),
  ('Mazda CX-3 2.0',               67.00, 680.00, 42900, 'rented'),
  ('Suzuki Swift 1.2',             31.00, 280.00, 81200, 'available'),
  ('Jeep Compass 1.4',             85.00, 750.00, 39200, 'available'),
  ('Seat Arona 1.0 TSI',           47.00, 460.00, 47300, 'available'),
  ('Audi Q2 1.0 TFSI',             89.00, 800.00, 31700, 'available'),
  ('Opel Insignia 1.5 Turbo',      63.00, 600.00, 50200, 'available'),
  ('Ford Fiesta 1.1',              35.00, 300.00, 69800, 'retired'),
  ('Dacia Sandero 1.0',            32.00, 290.00, 77400, 'available'),
  ('Tesla Model Y Long Range',    140.00, 1100.00, 15800, 'rented'),
  ('Volkswagen T-Roc 1.5',         76.00, 700.00, 34500, 'available'),
  ('Peugeot 2008 1.2 PureTech',    59.00, 550.00, 36800, 'available'),
  ('BMW 118i',                     85.00, 800.00, 30900, 'service'),
  ('Volvo V40 1.5 T3',             69.00, 700.00, 38900, 'available'),
  ('Alfa Romeo Stelvio 2.0T',     125.00, 1100.00, 21000, 'available'),
  ('Toyota Aygo 1.0',              30.00, 250.00, 85200, 'retired'),
  ('Renault Talisman 1.6',         60.00, 600.00, 43100, 'available'),
  ('Honda CR-V 2.0 Hybrid',       110.00, 950.00, 29700, 'rented'),
  ('Citroën C3 1.2',               37.00, 350.00, 62200, 'available'),
  ('volkswagen t-roc',             50.00, 370.00, 92900, 'available'),  
  ('Lexus NX 300h',               130.00, 1100.00, 22500, 'available');


INSERT INTO has (car_id, specification_id)
VALUES
  (1,  1),   (2,  2),   (3,  3),   (4,  4),   (5,  5),   (6,  6),
  (7,  7),   (8,  8),   (9,  9),   (10, 10),  (11, 11),  (12, 12),
  (13, 13),  (14, 14),  (15, 15),  (16, 16),  (17, 17),  (18, 18),
  (19, 19),  (20, 20),  (21, 21),  (22, 22),  (23, 23),  (24, 24),
  (25, 25),  (26, 26),  (27, 27),  (28, 28),  (29, 29),  (30, 30),
  (31, 31),  (32, 32),  (33, 33),  (34, 34);
  
  
INSERT INTO Booking (start_date, end_date, booking_status, secure_deposit, amount, drive_option, reading, date_out)
VALUES
  ('2025-05-01', '2025-05-03', 'confirmed', 350.00, 108.00, 'self',     58700, '2025-05-01'),  -- booking_id = 1
  ('2025-05-04', '2025-05-07', 'confirmed', 600.00, 248.00, 'self',     42300, '2025-05-04'),  -- booking_id = 2
  ('2025-05-02', '2025-05-05', 'confirmed', 700.00, 219.00, 'chauffeur',47800, '2025-05-02'),  -- booking_id = 3
  ('2025-05-06', '2025-05-08', 'confirmed', 400.00, 123.00, 'self',     51200, '2025-05-06'),  -- booking_id = 4
  ('2025-05-10', '2025-05-12', 'confirmed', 850.00, 276.00, 'self',     28900, '2025-05-10');  -- booking_id = 5


INSERT INTO makes (user_id, booking_id)
VALUES
  (2, 1),
  (4, 2),
  (5, 3),
  (6, 4),
  (7, 5);


INSERT INTO reserves (booking_id, car_id)
VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5);



