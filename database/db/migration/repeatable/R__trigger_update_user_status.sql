CREATE OR REPLACE FUNCTION update_user_status()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'INACTIVE' THEN
        RETURN NEW;
END IF;

    IF NEW.last_active > 365 THEN
        NEW.status := 'INACTIVE';
    ELSIF NEW.last_active <= 365 THEN
        NEW.status := 'ACTIVE';
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;
