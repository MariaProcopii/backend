CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE Users (
                       id INT PRIMARY KEY DEFAULT nextval('users_id_seq'),
                       name VARCHAR(255),
                       email VARCHAR(255) UNIQUE,
                       position VARCHAR(255),
                       discipline VARCHAR(255),
                       du VARCHAR(3),
                       status VARCHAR(20) DEFAULT 'ACTIVE',
                       last_active INT DEFAULT 0,
                       role VARCHAR(10) NOT NULL
);