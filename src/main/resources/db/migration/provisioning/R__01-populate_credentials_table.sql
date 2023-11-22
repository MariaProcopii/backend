DELETE FROM Credentials;

INSERT INTO credentials(username, password) VALUES ('john.doe@endava.com', 'johndoe'),
                                                         ('jane.smith@endava.com', 'JaneSmith'),
                                                         ('steve.brown@endava.com', 'SteveBrown'),
                                                         ('emma.jones@endava.com', 'EmmaJones');
COMMIT;