DROP TRIGGER insert_card;
DROP TRIGGER delete_card;
DROP TRIGGER update_card;

DELIMITER //

CREATE TRIGGER insert_card
    AFTER INSERT ON card
    FOR EACH ROW
        BEGIN
            UPDATE category SET cnt = cnt + 1 WHERE cid = NEW.cid;
        END; //

CREATE TRIGGER delete_card
    BEFORE DELETE ON card
    FOR EACH ROW
        BEGIN
            UPDATE category SET cnt = cnt - 1 WHERE cid = OLD.cid;
        END; //

CREATE TRIGGER update_card
    AFTER UPDATE ON card
    FOR EACH ROW
        BEGIN
            UPDATE category SET cnt = cnt - 1 WHERE cid = OLD.cid;
            UPDATE category SET cnt = cnt + 1 WHERE cid = NEW.cid;
        END; //

DELIMITER ;