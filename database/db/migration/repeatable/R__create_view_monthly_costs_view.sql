DROP VIEW IF EXISTS monthly_costs_view;
CREATE OR REPLACE VIEW monthly_costs_view AS
SELECT
    TO_CHAR(activation_date, 'Mon YY') AS month,
    SUM(cost) AS value
FROM Licenses
WHERE activation_date >= DATE_TRUNC('year', CURRENT_DATE) AND activation_date < DATE_TRUNC('year', CURRENT_DATE) + INTERVAL '1 year'
GROUP BY TO_CHAR(activation_date, 'Mon YY'), DATE_TRUNC('month', activation_date)
ORDER BY DATE_TRUNC('month', activation_date);

COMMENT ON VIEW monthly_costs_view IS 'Provides a monthly breakdown of costs for licenses activated in the current year. This view aggregates the total costs per month, helping in financial analysis and budgeting.';
COMMENT ON COLUMN monthly_costs_view.month IS 'Represents the month and year of license activation, formatted as "Mon YY" (e.g., Jan 22 for January 2023).';
COMMENT ON COLUMN monthly_costs_view.value IS 'The aggregated sum of costs for licenses activated in the corresponding month of the current year. Represents the total financial expenditure on licenses for each month.';
