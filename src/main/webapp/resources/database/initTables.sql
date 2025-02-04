-- Drop existing tables if they exist
DROP TABLE IF EXISTS Cart CASCADE;
DROP TABLE IF EXISTS Saved CASCADE;
DROP TABLE IF EXISTS Review CASCADE;
DROP TABLE IF EXISTS Payment CASCADE;
DROP TABLE IF EXISTS PaymentMethod CASCADE;
DROP TABLE IF EXISTS SavedCards CASCADE;
DROP TABLE IF EXISTS Booking CASCADE;
DROP TABLE IF EXISTS Service CASCADE;
DROP TABLE IF EXISTS Person CASCADE;
DROP TABLE IF EXISTS Role CASCADE;
DROP TABLE IF EXISTS Status CASCADE;
DROP TABLE IF EXISTS Category CASCADE;

-- Create Role table
CREATE TABLE Role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create Status table for booking statuses
CREATE TABLE Status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create Category table for service categories
CREATE TABLE Category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create Person table (users, customers, and providers)
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

-- Create Service table
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

-- Create Booking table
CREATE TABLE Booking (
    id BIGSERIAL PRIMARY KEY,
    requester_id INT NOT NULL,
    provider_id INT,
    service_id INT NOT NULL,
    status_id INT DEFAULT 1,
    date_requested DATE NOT NULL,
    time_requested TIME NOT NULL,
    phNumber VARCHAR(15) NOT NULL CHECK (phNumber ~ '^[0-9\+\-() ]+$'),
    address VARCHAR(255) NOT NULL,
    postalCode VARCHAR(10) NOT NULL CHECK (postalCode ~ '^[A-Za-z0-9 -]+$'),
    remark VARCHAR(255),
    
    CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES Person(id) ON DELETE CASCADE,
    CONSTRAINT fk_provider FOREIGN KEY (provider_id) REFERENCES Person(id) ON DELETE SET NULL,
    CONSTRAINT fk_service FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE CASCADE,
    CONSTRAINT fk_status FOREIGN KEY (status_id) REFERENCES Status(id) ON DELETE CASCADE,
    
    CONSTRAINT unique_booking UNIQUE (service_id, date_requested, time_requested, provider_id, requester_id)
);

-- Create PaymentMethod table (stores available payment methods)
CREATE TABLE PaymentMethod (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name IN ('Bank Card', 'QR Code'))
);

-- Create Payment table (stores payment history linked to Booking)
CREATE TABLE Payment (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL,
    booking_id INT NOT NULL UNIQUE, -- Links payment to a specific booking
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    payment_method_id INT NOT NULL, -- Foreign key linking to PaymentMethod
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES Booking(id) ON DELETE CASCADE,
    FOREIGN KEY (payment_method_id) REFERENCES PaymentMethod(id) ON DELETE CASCADE
);

-- Create SavedCards table (stores user bank cards securely for future payments)
CREATE TABLE SavedCards (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL,
    card_type VARCHAR(50) NOT NULL, -- e.g., 'Visa', 'MasterCard'
    last_four_digits VARCHAR(4) NOT NULL, -- Only stores last 4 digits for security
    card_token VARCHAR(255) NOT NULL UNIQUE, -- Encrypted token from payment provider
    expiry_date DATE NOT NULL,
    is_default BOOLEAN DEFAULT FALSE, -- User can set a default card
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE
);

-- Create Review table (linked to Booking)
CREATE TABLE Review (
    id SERIAL PRIMARY KEY,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    content TEXT,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    booking_id INT UNIQUE NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES Booking(id) ON DELETE CASCADE
);

-- Create Saved table (for users saving services for later)
CREATE TABLE Saved (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL,
    service_id INT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE CASCADE
);

-- Indexing for performance optimization
CREATE INDEX idx_booking_date ON Booking (date_requested, time_requested);
CREATE INDEX idx_booking_service ON Booking (service_id);
CREATE INDEX idx_payment_booking ON Payment (booking_id);
CREATE INDEX idx_payment_person ON Payment (person_id);
CREATE INDEX idx_savedcards_person ON SavedCards (person_id);
