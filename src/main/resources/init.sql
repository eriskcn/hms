USE hms_db;

-- user: 'hms_user'@'localhost'
-- pass: 'javaTLU#2024'

CREATE TABLE guests (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       id_card VARCHAR(50),
                       gender ENUM('Male', 'Female', 'Other'),
                       phone VARCHAR(15),
                       total_amount DECIMAL(10, 2) DEFAULT 0,
                       is_deleted BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE rooms (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      number VARCHAR(10) NOT NULL,
                      type ENUM('Standard', 'Suite', 'VIP') NOT NULL,
                      price DECIMAL(10, 2) NOT NULL,
                      status ENUM('Available', 'Unavailable') NOT NULL,
                      is_deleted BOOLEAN DEFAULT FALSE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE bookings (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         guest_id INT,
                         room_id INT NULL,
                         check_in DATE NOT NULL,
                         check_out DATE NULL,
                         amount DECIMAL(10, 2) NULL,
                         is_pre_booking BOOLEAN DEFAULT TRUE,
                         is_deleted BOOLEAN DEFAULT FALSE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (guest_id) REFERENCES guests(id),
                         FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE services (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         category VARCHAR(100),
                         price DECIMAL(10, 2) NOT NULL,
                         status ENUM('Available', 'Unavailable') NOT NULL,
                         is_deleted BOOLEAN DEFAULT FALSE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE service_bookings (
                                service_id INT,
                                booking_id INT,
                                is_deleted BOOLEAN DEFAULT FALSE,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (service_id, booking_id),
                                FOREIGN KEY (service_id) REFERENCES services(id),
                                FOREIGN KEY (booking_id) REFERENCES bookings(id)
);
