CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE  NOT NULL,
    password VARCHAR(255),
    role ENUM('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'STAFF'),
    position VARCHAR(50),
    nrc VARCHAR(50) UNIQUE ,
    birth_date DATE,
    join_date DATE,
    address VARCHAR(255),
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