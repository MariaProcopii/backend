CREATE SEQUENCE credentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE credentials
(

    id INT PRIMARY KEY DEFAULT nextval('credentials_id_seq'),
    username VARCHAR (255) UNIQUE NOT NULL,
    password VARCHAR (255) NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'USER'
);

COMMENT ON TABLE credentials is 'Table for tracking and managing information about credentials of users';
COMMENT ON COLUMN credentials.id is 'request_id is an id for rows in Credentials table';
COMMENT ON COLUMN credentials.username is 'username - is an email of User';
COMMENT ON COLUMN credentials.password is 'password - for security';
COMMENT ON COLUMN credentials.role is 'role - position in system hierarchy';
