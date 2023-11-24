DELETE FROM Licenses;

INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description,  seats_available, seats_total, is_recurring, creating_date) VALUES
    ('Postman', 666, 365, 13, 'SOFTWARE', '2023-10-31', '2024-10-31', 'Postman API', 10, 250, false, '2023-10-31'),
    ('Adobe Suite', 1313, 730, 30, 'SOFTWARE', '2023-11-01', '2024-11-01', 'Adobe Studio API',  10, 250, false, '2023-11-01'),
    ('O`Reilly', 999, 90, 7, 'TRAINING', '2023-12-13', '2024-12-13', 'Postman API', 10, 250, false, '2023-12-13'),
    ('Udemy', 1999, 60, 21, 'TRAINING', '2023-09-09', '2024-03-09', 'O`Reilly API', 10, 250, false, '2023-09-09'),
    ('JetBrains', 1444, 120, 0, 'SOFTWARE', '2023-06-06', '2024-06-06',  'JetBrains API', 10, 250, true, '2023-06-06');

INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES
    ('Visual Studio', 800, 360, 10, 'SOFTWARE', '2021-01-15', '2021-01-19', 'Visual Studio API', 10, 250, false, '2021-01-15'),
    ('Sketch', 1200, 365, 15, 'SOFTWARE', '2021-02-20', '2021-03-29', 'Sketch API', 10, 250, false, '2021-02-20'),
    ('Coursera', 700, 180, 20, 'TRAINING', '2021-03-25', '2021-03-29', 'Coursera API', 10, 250, false, '2021-03-25'),
    ('Pluralsight', 1500, 120, 25, 'TRAINING', '2021-04-30', '2021-06-29', 'Pluralsight API', 10, 250, false, '2021-04-30'),
    ('IntelliJ IDEA', 900, 365, 5, 'SOFTWARE', '2022-05-05', '2022-06-04', 'IntelliJ IDEA API', 10, 250, false, '2022-05-05'),
    ('Figma', 1100, 730, 10, 'SOFTWARE', '2022-06-10', '2022-07-09', 'Figma API', 10, 250, false, '2022-06-10'),
    ('LinkedIn Learning', 800, 90, 15, 'TRAINING', '2022-07-15', '2022-08-14', 'LinkedIn Learning API', 10, 250, false, '2022-07-15'),
    ('Codecademy', 1400, 60, 20, 'TRAINING', '2022-08-20', '2022-09-19', 'Codecademy API', 10, 250, false, '2022-08-20');

COMMIT;
