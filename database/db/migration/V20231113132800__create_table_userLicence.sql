CREATE TABLE UserLicenses (
      user_id INT REFERENCES Users(id),
      license_id INT REFERENCES Licenses(license_id),
      PRIMARY KEY (user_id, license_id)
);