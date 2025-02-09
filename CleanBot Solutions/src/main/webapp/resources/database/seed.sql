
-- ✅ Insert Roles (Admin, Worker)
INSERT INTO Role (name) VALUES 
    ('Admin'),
    ('Worker')
    ON CONFLICT (name) DO NOTHING;

-- ✅ Insert Service Categories
INSERT INTO Category (name) VALUES 
    ('Cleaning'),
    ('Repair'),
    ('Maintenance'),
    ('Pest Control'),
    ('Installation')
    ON CONFLICT (name) DO NOTHING;

-- ✅ Insert Sample Workers (Password must be hashed in the application before inserting)
INSERT INTO Worker (name, email, phone, password, role_id) VALUES 
    ('Michael Cleaner', 'michael.cleaner@example.com', '9123456789', '$2a$10$abcdefg1234567890', 2),
    ('Sarah Repair', 'sarah.repair@example.com', '9234567890', '$2a$10$abcdefg1234567890', 2),
    ('Tom HVAC', 'tom.hvac@example.com', '9345678901', '$2a$10$abcdefg1234567890', 2),
    ('Linda Pest', 'linda.pest@example.com', '9456789012', '$2a$10$abcdefg1234567890', 2),
    ('Chris Install', 'chris.install@example.com', '9567890123', '$2a$10$abcdefg1234567890', 2)
    ON CONFLICT (email) DO NOTHING;

-- ✅ Insert Worker-Category Assignments (Each worker is assigned to relevant service categories)
INSERT INTO WorkerCategory (worker_id, category_id) VALUES
    (1, 1), 
    (2, 2), 
    (3, 3), 
    (4, 4),
    (5, 5),
    (1, 3), 
    (2, 5), 
    (3, 1),
    (4, 2)  
    ON CONFLICT (worker_id, category_id) DO NOTHING;

