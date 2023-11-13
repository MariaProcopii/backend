CREATE SEQUENCE license_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE Licenses (
      license_id INT PRIMARY KEY DEFAULT nextval('license_id_seq'),
      license_name VARCHAR(255) NOT NULL,
      cost INT NOT NULL,
      availability INT NOT NULL,
      unused_period INT DEFAULT 0,
      license_type VARCHAR(255),
      activation_date DATE DEFAULT CURRENT_DATE,
      expiration_date DATE
);