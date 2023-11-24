CREATE TABLE LicenseCredentials(
    id_credential INT REFERENCES credentials(id),
    id_license INT REFERENCES licenses(id),
    PRIMARY KEY (id_credential,id_license)
);