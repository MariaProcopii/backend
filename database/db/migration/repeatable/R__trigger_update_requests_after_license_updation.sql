CREATE OR REPLACE FUNCTION update_request_app()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE requests SET app = NEW.license_name WHERE app = OLD.license_name;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_license_name_at_requests
    AFTER UPDATE ON Licenses
    FOR EACH ROW
    EXECUTE FUNCTION update_request_app();
