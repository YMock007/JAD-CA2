-- Drop existing tables if they exist
DROP TABLE IF EXISTS WorkerCategory CASCADE;
DROP TABLE IF EXISTS Worker CASCADE;
DROP TABLE IF EXISTS Category CASCADE;
DROP TABLE IF EXISTS Role CASCADE;

-- ✅ Create Role Table (Defines roles: Admin, Worker)
CREATE TABLE Role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- ✅ Create Category Table (Service categories workers can choose)
CREATE TABLE Category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- ✅ Create Worker Table (Stores workers and links them to roles)
CREATE TABLE Worker (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL CHECK (phone ~ '^[0-9\+\-() ]+$'),
    password VARCHAR(255) NOT NULL, -- Hashed password
    role_id INT NOT NULL, -- Links to Role table
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE CASCADE
);

-- ✅ Create WorkerCategory Table (Links workers to categories they work in)
CREATE TABLE WorkerCategory (
    worker_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (worker_id, category_id),
    
    FOREIGN KEY (worker_id) REFERENCES Worker(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE
);

-- ✅ Indexing for performance
CREATE INDEX idx_worker_category ON WorkerCategory (worker_id, category_id);
CREATE INDEX idx_worker_role ON Worker (role_id);
