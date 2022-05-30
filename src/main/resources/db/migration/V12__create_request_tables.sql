create table card_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL,
    answer TEXT NOT NULL,
    tags VARCHAR(255),
    request_status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    category_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL
) engine=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

create table other_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    request_status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    modified_at DATETIME NOT NULL,
    requester_id BIGINT NOT NULL
) engine=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;