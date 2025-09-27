CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE  NOT NULL,
    password VARCHAR(225),
    role ENUM('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'STAFF'),
    enabled BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rooms (
                       no INT NOT NULL,
                       base_price DOUBLE,
                       capacity INT,
                       floor ENUM('FOURTH', 'FIFTH', 'SEVENTH', 'EIGHTH'),
                       current_status ENUM('AVAILABLE', 'NORMAL_STAY', 'LONG_STAY', 'IN_SERVICE', 'STORE'),
                       PRIMARY KEY (no)
);