CREATE OR REPLACE FUNCTION calculate_expiration_date()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.expiration_date := NEW.activation_date + NEW.availability;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_calculate_expiration_date
    BEFORE INSERT OR UPDATE ON Licenses
    FOR EACH ROW
    EXECUTE FUNCTION calculate_expiration_date();