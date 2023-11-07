CREATE OR REPLACE FUNCTION increase_unused_period()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.expiration_date < CURRENT_DATE THEN
        NEW.unused_period := NEW.unused_period + 1;
    END IF;
    RETURN NEW;
END;

$$ LANGUAGE plpgsql;

DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1 FROM pg_trigger
            WHERE tgname = 'trigger_increase_unused_period'
        ) THEN
            CREATE TRIGGER trigger_increase_unused_period
                BEFORE UPDATE ON Licenses
                FOR EACH ROW EXECUTE FUNCTION increase_unused_period();
        END IF;
    END;
$$;
