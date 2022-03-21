DELIMITER //

CREATE TRIGGER insert_card
    AFTER INSERT ON card
    FOR EACH ROW
        BEGIN
            UPDATE category SET cnt = cnt + 1 WHERE cname = NEW.category;
        END; //


CREATE TRIGGER delete_card
    BEFORE DELETE ON card
    FOR EACH ROW
        BEGIN
            UPDATE category SET cnt = cnt - 1 WHERE cname = OLD.category;
        END; //

CREATE TRIGGER update_card
    AFTER UPDATE ON card
    FOR EACH ROW
        BEGIN
            UPDATE category SET cnt = cnt - 1 WHERE cname = OLD.category;
            UPDATE category SET cnt = cnt + 1 WHERE cname = NEW.category;
        END; //

DELIMITER ;