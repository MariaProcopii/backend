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


DROP TRIGGER IF EXISTS trigger_update_user_status ON Users;
DROP TRIGGER IF EXISTS trigger_insert_user_status ON Users;

CREATE TRIGGER trigger_update_user_status
    BEFORE UPDATE ON Users
    FOR EACH ROW
    EXECUTE FUNCTION update_user_status();

CREATE TRIGGER trigger_insert_user_status
    BEFORE INSERT ON Users
    FOR EACH ROW
    EXECUTE FUNCTION update_user_status();
