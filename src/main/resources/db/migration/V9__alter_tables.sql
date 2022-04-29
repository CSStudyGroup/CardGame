alter table card
    change cid cname varchar(255) not null,
    modify question varchar(255) not null,
    modify answer text not null;

alter table category
    change cid id bigint auto_increment,
    change cname name varchar(255) not null,
    modify cnt int default 0;

DROP TRIGGER insert_card;
DROP TRIGGER delete_card;
DROP TRIGGER update_card;