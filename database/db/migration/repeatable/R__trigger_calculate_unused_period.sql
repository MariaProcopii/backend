CREATE OR REPLACE FUNCTION calculate_unused_period()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.expiration_date < CURRENT_DATE THEN
        NEW.unused_period := (SELECT
        (CURRENT_DATE - NEW.expiration_date) AS days_difference);
    ELSE NEW.unused_period := 0;
END IF;
RETURN NEW;
END;

$$ LANGUAGE plpgsql;

DO $$
BEGIN
        IF NOT EXISTS (
            SELECT 1 FROM pg_trigger
            WHERE tgname = 'trigger_calculate_unused_period'
        ) THEN
CREATE TRIGGER trigger_calculate_unused_period
    BEFORE UPDATE OR INSERT ON Licenses
    FOR EACH ROW EXECUTE FUNCTION calculate_unused_period();
END IF;
END;
$$;