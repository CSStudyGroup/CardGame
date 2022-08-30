create table card (
    id BIGINT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    question VARCHAR(255),
    answer TEXT,
    tags VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE category (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cnt BIGINT NOT NULL,
    owner_id BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE member (
    id BIGINT PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    nickname VARCHAR(20) NOT NULL,
    password VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE member_role (
    member_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE bookmark (
    id BIGINT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;