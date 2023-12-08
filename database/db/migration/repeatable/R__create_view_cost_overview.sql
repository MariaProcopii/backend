CREATE OR REPLACE VIEW cost_view AS
SELECT
    (SELECT SUM(cost) FROM Licenses WHERE EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE)) AS total_costs_current_year,
    (SELECT SUM(cost) FROM Licenses WHERE EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE)) -
    (SELECT SUM(cost) FROM Licenses WHERE EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE) - 1) AS delta_total_costs,
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'SOFTWARE' AND EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE)) AS software,
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'SOFTWARE' AND EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE)) -
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'SOFTWARE' AND EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE) - 1) AS delta_software,
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'TRAINING' AND EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE)) AS trainings,
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'TRAINING' AND EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE)) -
    (SELECT COUNT(*) FROM Licenses WHERE license_type = 'TRAINING' AND EXTRACT(YEAR FROM activation_date) = EXTRACT(YEAR FROM CURRENT_DATE) - 1) AS delta_trainings;

COMMENT ON VIEW cost_view IS 'Provides a summary of costs and counts of licenses for the current year and the previous year, differentiated by type (SOFTWARE or TRAINING). It includes total costs for the current year, the change in total costs from the previous year, and the count and change in count of SOFTWARE and TRAINING licenses.';
COMMENT ON COLUMN cost_view.total_costs_current_year IS 'Total cost of all licenses activated in the current year.';
COMMENT ON COLUMN cost_view.delta_total_costs IS 'Change in total costs of licenses from the previous year to the current year. It represents the difference in total costs between these years.';
COMMENT ON COLUMN cost_view.software IS 'Total count of SOFTWARE licenses activated in the current year.';
COMMENT ON COLUMN cost_view.delta_software IS 'Change in count of SOFTWARE licenses from the previous year to the current year. It represents the difference in the number of SOFTWARE licenses between these years.';
COMMENT ON COLUMN cost_view.trainings IS 'Total count of TRAINING licenses activated in the current year.';
COMMENT ON COLUMN cost_view.delta_trainings IS 'Change in count of TRAINING licenses from the previous year to the current year. It represents the difference in the number of TRAINING licenses between these years.';
