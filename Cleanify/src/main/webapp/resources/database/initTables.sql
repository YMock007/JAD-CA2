-- Drop tables if they already exist to reset (cascade dependencies)
DROP TABLE IF EXISTS Favorite CASCADE;
DROP TABLE IF EXISTS Cart CASCADE;
DROP TABLE IF EXISTS Saved CASCADE;
DROP TABLE IF EXISTS Review CASCADE;
DROP TABLE IF EXISTS Booking CASCADE;
DROP TABLE IF EXISTS Service CASCADE;
DROP TABLE IF EXISTS ApplyToBeCleaner CASCADE;
DROP TABLE IF EXISTS Person CASCADE;
DROP TABLE IF EXISTS Role CASCADE;
DROP TABLE IF EXISTS Status CASCADE;
DROP TABLE IF EXISTS Category CASCADE;

-- Create Role table
CREATE TABLE Role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

-- Create Status table for booking statuses
CREATE TABLE Status (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create Category table for service categories (general categories)
CREATE TABLE Category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create Person table
CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phNumber VARCHAR(15),
    address VARCHAR(255),
    postalCode VARCHAR(10),
    role_id INT,
    is_google_user BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE CASCADE
);

-- Create ApplyToBeCleaner table
CREATE TABLE ApplyToBeCleaner (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    date_applied TIMESTAMP NOT NULL,
    remarks TEXT
);

-- Create Service table
CREATE TABLE Service (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    category_id INT,
    description TEXT NOT NULL,
    price FLOAT NOT NULL,
    image_Url TEXT,
    est_duration FLOAT,
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE
);

CREATE TABLE Booking (
    id SERIAL PRIMARY KEY,
    requester_id INT,
    provider_id INT,
    service_id INT,
    status_id INT,
    date_requested DATE NOT NULL,
    time_requested TIME NOT NULL,
    phNumber VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL,
    postalCode VARCHAR(10) NOT NULL,
    remark VARCHAR(255),
    FOREIGN KEY (requester_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (provider_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES Status(id) ON DELETE CASCADE,
    UNIQUE (service_id, date_requested, time_requested, provider_id, requester_id) 
);


-- Create Review table first
CREATE TABLE Review (
    id SERIAL PRIMARY KEY,
    rating INT NOT NULL,
    content TEXT NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    booking_id INT UNIQUE,
    FOREIGN KEY (booking_id) REFERENCES Booking(id) ON DELETE CASCADE
);

-- Drop existing trigger and function if they exist
DROP TRIGGER IF EXISTS trg_validate_review_booking_status ON Review;
DROP FUNCTION IF EXISTS validate_review_booking_status;

-- Create function to validate booking status before inserting a review
CREATE FUNCTION validate_review_booking_status()
RETURNS TRIGGER AS $$
BEGIN
    -- Ensure the booking has the status 'Completed'
    IF NOT EXISTS (
        SELECT 1
        FROM Booking
        JOIN Status ON Booking.status_id = Status.id
        WHERE Booking.id = NEW.booking_id AND Status.name = 'Completed'
    ) THEN
        RAISE EXCEPTION 'Review can only be added for bookings with status "Completed"';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to invoke validation function before insert on Review
CREATE TRIGGER trg_validate_review_booking_status
BEFORE INSERT OR UPDATE ON Review
FOR EACH ROW
EXECUTE FUNCTION validate_review_booking_status();

-- Create Saved table
CREATE TABLE Saved (
    id SERIAL PRIMARY KEY,
    person_id INT,
    service_id INT,
    FOREIGN KEY (person_id) REFERENCES Person(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES Service(id) ON DELETE CASCADE
);
