-- Drop existing tables if they exist
DROP TABLE IF EXISTS Cart CASCADE;
DROP TABLE IF EXISTS Saved CASCADE;
DROP TABLE IF EXISTS Review CASCADE;
DROP TABLE IF EXISTS Payment CASCADE;
DROP TABLE IF EXISTS PaymentMethod CASCADE;
DROP TABLE IF EXISTS SavedCards CASCADE;
DROP TABLE IF EXISTS Booking CASCADE;
DROP TABLE IF EXISTS Service CASCADE;
DROP TABLE IF EXISTS Worker CASCADE;
DROP TABLE IF EXISTS Person CASCADE;
DROP TABLE IF EXISTS Role CASCADE;
DROP TABLE IF EXISTS Status CASCADE;
DROP TABLE IF EXISTS Category CASCADE;
DROP TABLE IF EXISTS Inquiry CASCADE;
DROP TABLE IF EXISTS WorkerService CASCADE;
DROP TABLE IF EXISTS WorkerCategory CASCADE;

-- ✅ Create Role table
CREATE TABLE Role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- ✅ Create Status table for booking statuses
CREATE TABLE Status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- ✅ Create Category table for service categories
CREATE TABLE Category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- ✅ Create Person table (users, customers, and providers)
CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phNumber VARCHAR(15) UNIQUE,
    address VARCHAR(255),
    postalCode VARCHAR(10),
    role_id INT,
    is_google_user BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE CASCADE
);

CREATE TABLE Inquiry (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- ✅ Create Service table
CREATE TABLE Service (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    category_id INT NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    image_Url TEXT,
    est_duration DECIMAL(10,2) CHECK (est_duration > 0),
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE
);

-- ✅ Create Worker table (Stores only provider ID and name from third-party system)
CREATE TABLE Worker (
    id SERIAL PRIMARY KEY,
    provider_id INT NOT NULL UNIQUE, -- Worker ID from third-party manpower database
    name VARCHAR(255) NOT NULL       -- Worker name from manpower company
);

-- ✅ Create Booking table
CREATE TABLE Booking (
    id BIGSERIAL PRIMARY KEY,
    requester_id INT NOT NULL,
    provider_id INT NULL, -- NULL initially, updated when a worker accepts the job
    service_id INT NOT NULL,
    status_id INT DEFAULT 1,
    date_requested DATE NOT NULL,
    time_requested TIME NOT NULL,
    phNumber VARCHAR(15) NOT NULL CHECK (phNumber ~ '^[0-9\+\-() ]+$'),
    address VARCHAR(255) NOT NULL,
    postalCode VARCHAR(10) NOT NULL CHECK (postalCode ~ '^[A-Za-z0-9 -]+$'),
    remark VARCHAR(255),

    FOREIGN KEY (requester_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (provider_id) REFERENCES Worker(provider_id) ON DELETE SET NULL,
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES Status(id) ON DELETE CASCADE
);

-- ✅ Create PaymentMethod table
CREATE TABLE PaymentMethod (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name IN ('Bank Card', 'QR Code'))
);

-- ✅ Create Payment table
CREATE TABLE Payment (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL,
    booking_id INT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    payment_method_id INT NOT NULL,
    payment_details BYTEA NOT NULL,
    billing_address VARCHAR(255),
    billing_postal_code VARCHAR(10) CHECK (billing_postal_code ~ '^[A-Za-z0-9 -]+$'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES Booking(id) ON DELETE CASCADE,
    FOREIGN KEY (payment_method_id) REFERENCES PaymentMethod(id) ON DELETE CASCADE
);

-- ✅ Create SavedCards table
CREATE TABLE SavedCards (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL,
    card_type VARCHAR(50) NOT NULL,
    last_four_digits VARCHAR(4) NOT NULL,
    card_token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date DATE NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE
);

-- ✅ Create Review table
CREATE TABLE Review (
    id SERIAL PRIMARY KEY,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    content TEXT,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    booking_id INT UNIQUE NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES Booking(id) ON DELETE CASCADE
);

-- ✅ Create Saved table
CREATE TABLE Saved (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL,
    service_id INT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE CASCADE
);


-- ✅ Function for creating bookings and payment
CREATE OR REPLACE FUNCTION create_bookings_and_payment(
    p_person_id INT,
    p_services INT[],
    p_status_id INT,
    p_date_requested DATE,
    p_time_requested TIME,
    p_phNumber VARCHAR(15),
    p_address VARCHAR(255),
    p_postalCode VARCHAR(10),
    p_remark TEXT,
    p_amount DECIMAL(10,2),
    p_payment_method_id INT,
    p_payment_details BYTEA,
    p_billing_address VARCHAR(255),
    p_billing_postal_code VARCHAR(10),
    OUT payment_id BIGINT
) 
LANGUAGE plpgsql 
AS $$ 
DECLARE
    new_booking_id BIGINT;
    booking_ids BIGINT[];
    service_id INT;
BEGIN
    BEGIN
        booking_ids := '{}';
        FOREACH service_id IN ARRAY p_services LOOP
            INSERT INTO Booking (requester_id, service_id, status_id, date_requested, time_requested, phNumber, address, postalCode, remark)
            VALUES (p_person_id, service_id, p_status_id, p_date_requested, p_time_requested, p_phNumber, p_address, p_postalCode, p_remark)
            RETURNING id INTO new_booking_id;
            booking_ids := array_append(booking_ids, new_booking_id);
        END LOOP;

        INSERT INTO Payment (person_id, booking_id, amount, payment_method_id, payment_details, billing_address, billing_postal_code)
        VALUES (p_person_id, booking_ids[1], p_amount, p_payment_method_id, p_payment_details, 
            CASE WHEN p_payment_method_id = 1 THEN p_billing_address ELSE NULL END,
            CASE WHEN p_payment_method_id = 1 THEN p_billing_postal_code ELSE NULL END)
        RETURNING id INTO payment_id;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Error in create_bookings_and_payment: %', SQLERRM;
    END;
END;
$$;

CREATE TABLE WorkerCategory (
    worker_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (worker_id, category_id),
    FOREIGN KEY (worker_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE
);

CREATE TABLE WorkerService (
    worker_id INT NOT NULL,
    service_id INT NOT NULL,
    PRIMARY KEY (worker_id, service_id),
    FOREIGN KEY (worker_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE CASCADE
);


-- ✅ Indexing for Performance Optimization
CREATE INDEX idx_booking_date ON Booking (date_requested, time_requested);
CREATE INDEX idx_booking_service ON Booking (service_id);
CREATE INDEX idx_payment_booking ON Payment (booking_id);
CREATE INDEX idx_payment_person ON Payment (person_id);
CREATE INDEX idx_savedcards_person ON SavedCards (person_id);
