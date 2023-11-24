CREATE SEQUENCE requests_id_seq
    START WITH 1
    INCREMENT BY 1
    CACHE 1;

CREATE TABLE Requests (
                          id INT PRIMARY KEY DEFAULT nextval('requests_id_seq'),
                          status VARCHAR(8) CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')) DEFAULT 'PENDING',
                          app VARCHAR(100) NOT NULL,
                          request_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
                          start_of_use TIMESTAMP NOT NULL,
                          user_id int references users(id) on delete cascade
);


COMMENT ON TABLE requests is 'Table for tracking and managing information about requests from users for training licenses';
COMMENT ON COLUMN requests.id is 'request_id is an id for rows in License table';
COMMENT ON COLUMN requests.status is 'status – Represents the status of requests. There are 3 types of statuses– Pending/Approved/Rejected';
COMMENT ON COLUMN requests.app is 'App – represents the training licenses';
COMMENT ON COLUMN requests.request_date is 'request_date - represents the date of the user''s request for a certain license';
COMMENT ON COLUMN requests.start_of_use is 'start_of_use - represents the the date the User has submitted in the form. Even if it''s rejected/pending -> this date will come from Users side';
COMMENT ON COLUMN requests.user_id is 'user_id –  the person who made the request to have access to the training license';
