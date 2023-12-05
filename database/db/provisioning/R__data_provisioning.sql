ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE credentials_id_seq RESTART WITH 1;
ALTER SEQUENCE license_id_seq RESTART WITH 1;
ALTER SEQUENCE requests_id_seq RESTART WITH 1;

DELETE FROM UserLicenses;
DELETE FROM Users;
DELETE FROM Licenses;
DELETE FROM Requests;
DELETE FROM Credentials;

INSERT INTO Credentials(username, password) VALUES ('john.doe@endava.com', 'johndoe');
INSERT INTO Credentials(username, password) VALUES ('jane.smith@endava.com', 'JaneSmith');
INSERT INTO Credentials(username, password) VALUES ('steve.brown@endava.com', 'SteveBrown');
INSERT INTO Credentials(username, password) VALUES ('emma.jones@endava.com', 'EmmaJones');

INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('John Doe', 'DEVELOPER', 'DEVELOPMENT', 'MDD', 'ACTIVE', 100, 1);
INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('Jane Smith', 'MANAGER', 'CREATIVE_SERVICES', 'MDD', 'ACTIVE', 376, 2);
INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('Steve Brown', 'MANAGER', 'DEVELOPMENT', 'MDD', 'ACTIVE', 200, 3);
INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('Emma Jones', 'SENIOR_TESTER', 'TESTING', 'MDD', 'ACTIVE', 400, 4);

INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Postman', 666, 365, 13, 'SOFTWARE', '2023-10-31', '2024-10-31', 'Postman API', 10, 250, false, '2023-10-31');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Adobe Suite', 1313, 730, 30, 'SOFTWARE', '2023-11-01', '2024-11-01', 'Adobe Studio API',  10, 250, false, '2023-11-01');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('O`Reilly', 999, 90, 7, 'TRAINING', '2023-12-13', '2024-12-13', 'Postman API', 10, 250, false, '2023-12-13');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Udemy', 1999, 60, 21, 'TRAINING', '2023-09-09', '2024-03-09', 'O`Reilly API', 10, 250, false, '2023-09-09');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('JetBrains', 1444, 120, 0, 'SOFTWARE', '2023-06-06', '2024-06-06',  'JetBrains API', 10, 250, true, '2023-06-06');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Visual Studio', 800, 360, 10, 'SOFTWARE', '2021-01-15', '2021-01-19', 'Visual Studio API', 10, 250, false, '2021-01-15');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Sketch', 1200, 365, 15, 'SOFTWARE', '2021-02-20', '2021-03-29', 'Sketch API', 10, 250, false, '2021-02-20');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Coursera', 700, 180, 20, 'TRAINING', '2021-03-25', '2021-03-29', 'Coursera API', 10, 250, false, '2021-03-25');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Pluralsight', 1500, 120, 25, 'TRAINING', '2021-04-30', '2021-06-29', 'Pluralsight API', 10, 250, false, '2021-04-30');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('IntelliJ IDEA', 900, 365, 5, 'SOFTWARE', '2022-05-05', '2022-06-04', 'IntelliJ IDEA API', 10, 250, false, '2022-05-05');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Figma', 1100, 730, 10, 'SOFTWARE', '2022-06-10', '2022-07-09', 'Figma API', 10, 250, false, '2022-06-10');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('LinkedIn Learning', 800, 90, 15, 'TRAINING', '2022-07-15', '2022-08-14', 'LinkedIn Learning API', 10, 250, false, '2022-07-15');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Codecademy', 1400, 60, 20, 'TRAINING', '2022-08-20', '2022-09-19', 'Codecademy API', 10, 250, false, '2022-08-20');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('GitLab', 500, 365, 10, 'SOFTWARE', '2023-01-10', '2024-01-10', 'GitLab DevOps Platform', 5, 100, true, '2023-01-10');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Photoshop', 750, 180, 15, 'SOFTWARE', '2023-02-15', '2024-02-15', 'Adobe Photoshop', 10, 200, false, '2023-02-15');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Tableau', 600, 365, 20, 'SOFTWARE', '2023-03-20', '2024-03-20', 'Tableau for Data Visualization', 8, 150, true, '2023-03-20');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Slack', 450, 90, 5, 'SOFTWARE', '2023-04-05', '2024-04-05', 'Slack for Team Communication', 15, 300, false, '2023-04-05');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Asana', 550, 120, 7, 'SOFTWARE', '2023-05-07', '2024-05-07', 'Asana Project Management', 12, 250, false, '2023-05-07');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Trello', 300, 60, 3, 'SOFTWARE', '2023-06-03', '2024-06-03', 'Trello for Task Management', 20, 400, false, '2023-06-03');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Zoom', 400, 30, 2, 'SOFTWARE', '2023-07-02', '2024-07-02', 'Zoom Video Conferencing', 25, 500, true, '2023-07-02');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Salesforce', 800, 365, 14, 'SOFTWARE', '2023-08-14', '2024-08-14', 'Salesforce CRM', 7, 140, true, '2023-08-14');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('JIRA', 700, 180, 10, 'SOFTWARE', '2023-09-10', '2024-09-10', 'JIRA for Agile Management', 10, 200, true, '2023-09-10');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Confluence', 350, 90, 4, 'SOFTWARE', '2023-10-04', '2024-10-04', 'Confluence for Documentation', 18, 360, false, '2023-10-04');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('MongoDB', 650, 120, 6, 'SOFTWARE', '2023-11-06', '2024-11-06', 'MongoDB Database Service', 9, 180, true, '2023-11-06');
INSERT INTO Licenses (license_name, cost, availability, unused_period, license_type, activation_date, expiration_date, description, seats_available, seats_total, is_recurring, creating_date) VALUES ('Node.js', 400, 60, 5, 'SOFTWARE', '2023-12-05', '2024-12-05', 'Node.js Runtime Environment', 16, 320, false, '2023-12-05');

INSERT INTO UserLicenses (user_id, license_id) VALUES (1, 1);
INSERT INTO UserLicenses (user_id, license_id) VALUES (1, 3);
INSERT INTO UserLicenses (user_id, license_id) VALUES (2, 2);
INSERT INTO UserLicenses (user_id, license_id) VALUES (2, 4);
INSERT INTO UserLicenses (user_id, license_id) VALUES (3, 5);
INSERT INTO UserLicenses (user_id, license_id) VALUES (4, 6);
INSERT INTO UserLicenses (user_id, license_id) VALUES (3, 7);
INSERT INTO UserLicenses (user_id, license_id) VALUES (4, 8);

INSERT INTO Requests(app, start_of_use, user_id) VALUES ('Udemy', '2023-09-09', 1);
INSERT INTO Requests(app, start_of_use, user_id) VALUES ('JetBrains', '2023-06-06', 2);
INSERT INTO Requests(app, start_of_use, user_id) VALUES ('JetBrains', '2023-06-06', 3);
INSERT INTO Requests(app, start_of_use, user_id) VALUES ('Postman', '2023-10-31', 4);
INSERT INTO Requests(app, start_of_use, user_id) VALUES ('Postman', '2023-10-31', 2);
INSERT INTO Requests(app, start_of_use, user_id) VALUES ('JetBrains', '2023-06-06', 3);

COMMIT;
