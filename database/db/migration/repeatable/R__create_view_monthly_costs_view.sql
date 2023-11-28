CREATE VIEW monthly_costs_view AS
SELECT
    TO_CHAR(activation_date, 'Mon YY') AS month,
    SUM(cost) AS value
FROM Licenses
WHERE activation_date >= '2022-01-01' AND activation_date < '2023-01-01'
GROUP BY TO_CHAR(activation_date, 'Mon YY');

COMMENT ON VIEW monthly_costs_view IS 'Provides a monthly breakdown of costs for licenses activated in the year 2022. This view aggregates the total costs per month, helping in financial analysis and budgeting.';
COMMENT ON COLUMN monthly_costs_view.month IS 'Represents the month and year of license activation, formatted as "Mon YY" (e.g., Jan 22 for January 2022).';
COMMENT ON COLUMN monthly_costs_view.value IS 'The aggregated sum of costs for licenses activated in the corresponding month of 2022. Represents the total financial expenditure on licenses for each month.';
