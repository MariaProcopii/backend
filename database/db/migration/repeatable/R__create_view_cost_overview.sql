CREATE VIEW cost_view AS
SELECT
    (SELECT SUM(cost) FROM Licenses WHERE EXTRACT(YEAR FROM activation_date) = 2023) AS total_costs_2023,
    (SELECT SUM(cost) FROM Licenses WHERE EXTRACT(YEAR FROM activation_date) = 2023) -
    (SELECT SUM(cost) FROM Licenses WHERE EXTRACT(YEAR FROM activation_date) = 2022) AS delta_total_costs_2023,
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'SOFTWARE' AND EXTRACT(YEAR FROM activation_date) = 2023) AS software,
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'SOFTWARE' AND EXTRACT(YEAR FROM activation_date) = 2023) -
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'SOFTWARE' AND EXTRACT(YEAR FROM activation_date) = 2022) AS delta_software,
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'TRAINING' AND EXTRACT(YEAR FROM activation_date) = 2023) AS trainings,
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'TRAINING' AND EXTRACT(YEAR FROM activation_date) = 2023) -
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'TRAINING' AND EXTRACT(YEAR FROM activation_date) = 2022) AS delta_trainings;

COMMENT ON VIEW cost_view IS 'Provides a summary of costs and counts of licenses for the years 2022 and 2023, differentiated by type (SOFTWARE or TRAINING). It includes total costs for 2023, the change in total costs from 2022 to 2023, and the count and change in count of SOFTWARE and TRAINING licenses.';
COMMENT ON COLUMN cost_view.total_costs_2023 IS 'Total cost of all licenses activated in the year 2023.';
COMMENT ON COLUMN cost_view.delta_total_costs_2023 IS 'Change in total costs of licenses from the year 2022 to 2023. It represents the difference in total costs between these years.';
COMMENT ON COLUMN cost_view.software IS 'Total count of SOFTWARE licenses activated in the year 2023.';
COMMENT ON COLUMN cost_view.delta_software IS 'Change in count of SOFTWARE licenses from the year 2022 to 2023. It represents the difference in the number of SOFTWARE licenses between these years.';
COMMENT ON COLUMN cost_view.trainings IS 'Total count of TRAINING licenses activated in the year 2023.';
COMMENT ON COLUMN cost_view.delta_trainings IS 'Change in count of TRAINING licenses from the year 2022 to 2023. It represents the difference in the number of TRAINING licenses between these years.';
