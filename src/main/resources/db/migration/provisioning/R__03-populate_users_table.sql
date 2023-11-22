DELETE FROM Users;

INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('John Doe', 'DEVELOPER', 'DEVELOPMENT', 'MDD', 'ACTIVE', 100, 1);
INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('Jane Smith', 'MANAGER', 'CREATIVE_SERVICES', 'MDD', 'ACTIVE', 376, 2);
INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('Steve Brown', 'MANAGER', 'DEVELOPMENT', 'MDD', 'ACTIVE', 200, 3);
INSERT INTO Users (name, position, discipline, du, status, last_active, credentialId) VALUES ('Emma Jones', 'SENIOR_TESTER', 'TESTING', 'MDD', 'ACTIVE', 400, 4);

COMMIT;
