CREATE OR REPLACE FUNCTION get_average_training_cost()
RETURNS FLOAT AS $$
DECLARE
totalCost FLOAT;
    userCount INT;
    averageCost FLOAT;
BEGIN

SELECT SUM(cost) INTO totalCost
FROM Licenses
WHERE license_type = 'TRAINING';

SELECT COUNT(*) INTO userCount
FROM Users;

IF userCount > 0 THEN
        averageCost := totalCost / userCount;
ELSE
        averageCost := 0;
END IF;

RETURN averageCost;
END;
$$ LANGUAGE plpgsql;


CREATE VIEW average_user_cost_view AS
SELECT
    get_average_training_cost() AS calculation,
    u.discipline AS discipline_name,
    SUM(l.cost) / COUNT(DISTINCT u.id) AS average_costs_user_discipline
FROM Users u
         JOIN UserLicenses ul ON u.id = ul.user_id
         JOIN Licenses l ON ul.license_id = l.id AND l.license_type = 'TRAINING'
GROUP BY u.discipline;

COMMENT ON VIEW average_user_cost_view IS 'Provides a summary of average costs associated with TRAINING licenses, grouped by user discipline.';
COMMENT ON COLUMN average_user_cost_view.calculation IS 'Average cost of TRAINING licenses per user across all users.';
COMMENT ON COLUMN average_user_cost_view.discipline_name IS 'The discipline to which the user belongs.';
COMMENT ON COLUMN average_user_cost_view.average_costs_user_discipline IS 'Average cost of TRAINING licenses per user within a specific discipline.';
COMMENT ON FUNCTION get_average_training_cost() IS 'Calculates the average cost of TRAINING licenses across all users. This function computes the total cost of TRAINING licenses and divides it by the total number of users to get the average cost per user.';
