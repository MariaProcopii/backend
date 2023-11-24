ALTER TABLE users
    ADD COLUMN credentialId INT REFERENCES credentials(id) ON DELETE  CASCADE;
