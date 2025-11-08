CREATE TABLE IF NOT EXISTS staffs (
    id INT AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE  NOT NULL,
    password VARCHAR(255),
    phone_number VARCHAR(20),
    father_name VARCHAR(50),
    role VARCHAR(50),
    position VARCHAR(50),
    nrc VARCHAR(50) UNIQUE ,
    birth_date DATE,
    join_date DATE,
    address VARCHAR(255),
    enabled BOOLEAN,
    notes TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rooms (
                       room_no INT NOT NULL,
                       base_price INT,
                       capacity INT,
                       floor ENUM('FOURTH', 'FIFTH', 'SEVENTH', 'EIGHTH'),
                       current_status ENUM('AVAILABLE', 'NORMAL_STAY', 'LONG_STAY', 'IN_SERVICE', 'STORE'),
                       current_reservation_id BIGINT,
                       room_type VARCHAR(50),
                       notes TEXT,
                       PRIMARY KEY (room_no)
);