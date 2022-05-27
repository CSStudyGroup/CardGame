create table member (
    id BIGINT NOT NULL auto_increment PRIMARY KEY,
    email varchar(50),
    password varchar(100)
) engine=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

create table member_role (
    member_id BIGINT NOT NULL,
    role varchar(20) NOT NULL
) engine=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;