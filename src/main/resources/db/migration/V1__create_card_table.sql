create table card (
    id BIGINT NOT NULL auto_increment PRIMARY KEY,
    category varchar(255),
    question varchar(255),
    answer text,
    tags varchar(255)
) engine=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;