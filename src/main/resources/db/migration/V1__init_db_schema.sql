CREATE TABLE IF NOT EXISTS rooms (
                       no INT NOT NULL,
                       base_price DOUBLE,
                       capacity INT,
                       floor ENUM('FOURTH', 'FIFTH', 'SEVENTH', 'EIGHTH'),
                       current_status ENUM('AVAILABLE', 'NORMAL_STAY', 'LONG_STAY', 'IN_SERVICE', 'STORE'),
                       PRIMARY KEY (no)
);