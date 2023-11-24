ALTER TABLE licenses ADD COLUMN logo bytea,
                     ADD COLUMN website VARCHAR(500),
                     ADD COLUMN description VARCHAR(1000),
                     ALTER COLUMN cost TYPE DECIMAL(8,3),
                     ADD COLUMN currency VARCHAR(3) DEFAULT 'USD',
                     ADD COLUMN seats_available int,
                     ADD COLUMN seats_total int,
                     ADD COLUMN is_recurring boolean,
                     ADD COLUMN creating_date DATE;
