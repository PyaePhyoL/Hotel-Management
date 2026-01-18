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

CREATE TABLE IF NOT EXISTS room_types (
    id VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    price INT,
    capacity INT,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS rooms (
                       room_no INT NOT NULL,
                       floor ENUM('FOURTH', 'FIFTH', 'SEVENTH', 'EIGHTH'),
                       current_status ENUM('AVAILABLE', 'NORMAL_STAY', 'LONG_STAY', 'IN_SERVICE', 'STORE'),
                       current_reservation_id BIGINT,
                       notes TEXT,
                       room_type_id VARCHAR(50),
                       PRIMARY KEY (room_no),
                       CONSTRAINT fk_rooms_room_type
                       FOREIGN KEY (room_type_id) REFERENCES room_types(id)
);

CREATE TABLE IF NOT EXISTS room_pricing_rules (
                                           id INT NOT NULL AUTO_INCREMENT,
                                           room_type_id VARCHAR(50) NOT NULL,
                                           stay_type VARCHAR(50) NOT NULL,
                                           no_of_guests INT,
                                           hours INT,
                                           price INT NOT NULL,
                                           PRIMARY KEY (id),
                                           CONSTRAINT fk_normal_pricing_room_type
                                               FOREIGN KEY (room_type_id)
                                                   REFERENCES room_types(id)
                                                   ON DELETE CASCADE
);
