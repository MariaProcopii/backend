DELETE FROM UserLicenses;
DELETE FROM LicenseCredentials;
DELETE FROM Licenses;
DELETE FROM Requests;
DELETE FROM Users;
DELETE FROM Credentials;

SELECT setval('credentials_id_seq', 1, false);
SELECT setval('license_id_seq', 1, false);
SELECT setval('requests_id_seq', 1, false);
SELECT setval('users_id_seq', 1, false);

INSERT INTO Credentials(username, password) VALUES ('test.user1@gmail.com', 'test1');
INSERT INTO Credentials(username, password) VALUES ('test.user2@gmail.com', 'test2');


INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('USER_1', 'DEVELOPER', 'DEVELOPMENT', 'MDD', 'ACTIVE', 0, 1);
INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('USER_2', 'MANAGER', 'TESTING', 'MDD', 'ACTIVE', 0, 2);

INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('TestApp1', 1444, 120, 0, 'SOFTWARE', '2023-09-09', '2024-09-09', 'Postman API', 10, 250, false, '2023-10-31');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('TestApp2', 700, 180, 20, 'TRAINING', '2023-06-06', '2023-06-10', 'Adobe Studio API',  10, 250, false, '2023-11-01');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('TestApp3', 800, 0, 20, 'TRAINING', '2023-06-06', '2023-06-06', 'Postman API', 10, 250, false, '2023-12-13');

INSERT INTO Requests(app, start_of_use, user_id) VALUES ('TestApp1', '2023-09-09', 1);
INSERT INTO Requests(app, start_of_use, user_id) VALUES ('TestApp2', '2023-06-06', 2);

INSERT INTO UserLicenses (user_id, license_id) VALUES (1, 1);
INSERT INTO UserLicenses (user_id, license_id) VALUES (2, 2);