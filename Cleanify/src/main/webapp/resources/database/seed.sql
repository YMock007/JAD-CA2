-- Insert general categories for services
INSERT INTO Category (name) VALUES 
    ('Cleaning'), 
    ('Repair'), 
    ('Maintenance'),
    ('Pest Control'),
    ('Installation')
    ON CONFLICT (name) DO NOTHING;

-- Insert sample roles
INSERT INTO Role (name) VALUES 
    ('admin'), 
    ('member'), 
    ('cleaner');

-- Insert sample statuses for bookings
INSERT INTO Status (name) VALUES 
    ('Pending'),
    ('Completed'),
    ('Cancelled');

-- Insert sample persons
INSERT INTO Person (name, password, email, phNumber, address, postalCode, role_id, is_google_user) VALUES 
    ('john_doe', 'password123', 'john@example.com', '1234567890', '123 Main St', '123456', 2, FALSE),
    ('jane_smith', 'password456', 'jane@example.com', NULL, '456 Elm St', '654321', 3, FALSE),
    ('alice_johnson', 'password789', 'alice@example.com', '9876543210', '789 Oak St', '789012', 1, FALSE),
    ('bob_williams', 'password321', 'bob@example.com', NULL, '101 Pine St', '345678', 2, FALSE),
    ('carol_white', 'password654', 'carol@example.com', '5647382910', '202 Maple St', '890123', 3, FALSE),
    ('dave_black', 'password987', 'dave@example.com', '3456789012', '303 Birch St', '456789', 1, FALSE),
    ('eve_green', 'password111', 'eve@example.com', '1234567891', '404 Cedar St', '567890', 2, FALSE),
    ('frank_brown', 'password222', 'frank@example.com', NULL, '505 Palm St', '678901', 3, FALSE),
    ('grace_blue', 'password333', 'grace@example.com', '6789012345', '606 Fir St', '789012', 1, FALSE),
    ('hank_gray', 'password444', 'hank@example.com', NULL, '707 Poplar St', '890123', 2, FALSE),
    ('mike_cleaner', 'password123', 'mike@example.com', '912345678', '88 Cleaning St', '123123', 3, FALSE),
    ('sarah_repair', 'password456', 'sarah@example.com', '923456789', '77 Repair Ave', '234234', 3, FALSE),
    ('tom_hvac', 'password789', 'tom@example.com', '934567890', '66 HVAC Rd', '345345', 3, FALSE),
    ('linda_pest', 'password321', 'linda@example.com', '945678901', '55 Pest St', '456456', 3, FALSE),
    ('chris_install', 'password654', 'chris@example.com', '956789012', '44 Install Ln', '567567', 3, FALSE);


    INSERT INTO Inquiry (email, username, title, description, created_at) VALUES 
    ('john@example.com', 'John Doe', 'Login Issue', 'I cannot log into my account. Please help.', '2024-02-08 09:30:00'),
    ('jane@example.com', 'Jane Smith', 'Billing Question', 'I have a question about my recent invoice.', '2024-02-08 10:00:00'),
    ('alice@example.com', 'Alice Johnson', 'Service Request', 'I need an urgent service appointment.', '2024-02-08 10:30:00'),
    ('mike@example.com', 'Mike Cleaner', 'Cleaning Schedule', 'Can I reschedule my cleaning service?', '2024-02-08 11:00:00'),
    ('sarah@example.com', 'Sarah Repair', 'Repair Delay', 'My repair service is delayed. Any updates?', '2024-02-08 11:30:00'),
    ('tom@example.com', 'Tom HVAC', 'Air Conditioning Issue', 'My AC unit is making a loud noise.', '2024-02-08 12:00:00'),
    ('linda@example.com', 'Linda Pest', 'Pest Control Request', 'I need a pest control appointment next week.', '2024-02-08 12:30:00'),
    ('randomuser1@testmail.com', 'David Miller', 'General Inquiry', 'I would like to know more about your services.', '2024-02-08 13:00:00'),
    ('randomuser2@testmail.com', 'Emma Wilson', 'Price Quote', 'Can you provide a quote for HVAC maintenance?', '2024-02-08 13:30:00'),
    ('randomuser3@testmail.com', 'Oliver Scott', 'Job Inquiry', 'Are you hiring technicians at the moment?', '2024-02-08 14:00:00'),
    ('randomuser4@testmail.com', 'Sophia Green', 'Cancellation Policy', 'What is your cancellation policy for services?', '2024-02-08 14:30:00'),
    ('randomuser5@testmail.com', 'Liam Brown', 'Customer Support', 'I need assistance with a service I booked.', '2024-02-08 15:00:00'),
    ('randomuser6@testmail.com', 'Noah White', 'Part Replacement', 'Do you sell replacement parts for appliances?', '2024-02-08 15:30:00'),
    ('randomuser7@testmail.com', 'Ava Thomas', 'Website Issue', 'Your website has a broken link on the services page.', '2024-02-08 16:00:00'),
    ('randomuser8@testmail.com', 'James Black', 'Membership Question', 'How do I become a member of your service program?', '2024-02-08 16:30:00');

   -- Insert sample Worker-Category relationships (Each worker can do multiple categories)
INSERT INTO WorkerCategory (worker_id, category_id) VALUES 
    (11, 1), (8, 3), 
    (12, 2), 
    (13, 3), (13, 4),
    (14, 4),
    (15, 5);  
    
    
    
-- Insert sample Worker-Service relationships
INSERT INTO WorkerService (worker_id, service_id) 
SELECT wc.worker_id, s.id 
FROM WorkerCategory wc
JOIN Service s ON wc.category_id = s.category_id;

-- Insert sample services 
INSERT INTO Service (name, category_id, description, price, image_url, est_duration) VALUES 
    ('Home Cleaning', 1, 'Deep cleaning for residential spaces.', 100.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/homeCleaning.jpg', 2),
    ('Plumbing Repair', 2, 'Fix leaks, clogs, and pipe repairs.', 120.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/PlumbingRepair.jpg', 3),
    ('Electrical Repair', 2, 'Troubleshoot and fix electrical issues.', 140.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/ElectricalRepair.jpg', 4),
    ('HVAC Maintenance', 3, 'Regular check-ups and maintenance.', 120.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/HVACMaintenance.jpg', 3),
    ('Lawn Maintenance', 3, 'Mowing, trimming, and lawn care.', 70.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/LawnMaintenance.jpg', 2),
    ('Pool Maintenance', 3, 'Cleaning and chemical balancing.', 130.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/PoolMaintenance.jpg', 4),
    ('Pest Control', 4, 'Safe and effective pest extermination.', 110.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/PestControl.jpg', 2),
    ('Appliance Installation', 5, 'Professional appliance setup.', 150.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/ApplianceInstallation.jpg', 3),
    ('Furniture Assembly', 5, 'Secure assembly of furniture.', 130.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/furnitureAssembly.jpg', 1);

-- Insert sample bookings (No provider assigned, Status = Pending)
INSERT INTO Booking (requester_id, provider_id, service_id, status_id, date_requested, time_requested, phNumber, address, postalCode, remark) VALUES
    (1, NULL, 1, 1, '2024-12-01', '09:00:00', '1234567890', '123 Main St', '123456', 'Customer requested eco-friendly cleaning'),
    (2, NULL, 3, 1, '2024-12-02', '10:00:00', '2345678901', '456 Elm St', '654321', 'Electrical inspection needed'),
    (3, NULL, 5, 1, '2024-12-03', '11:00:00', '3456789012', '789 Oak St', '789012', 'Lawn maintenance for front and backyard'),
    (4, NULL, 7, 1, '2024-12-04', '14:00:00', '4567890123', '202 Maple St', '890123', 'Emergency pest control service'),
    (5, NULL, 8, 1, '2024-12-05', '15:00:00', '5678901234', '303 Birch St', '456789', 'Appliance installation for new fridge'),
    (6, NULL, 2, 1, '2024-12-06', '16:00:00', '6789012345', '404 Cedar St', '567890', 'Plumbing leak in the kitchen'),
    (7, NULL, 4, 1, '2024-12-07', '17:00:00', '7890123456', '505 Palm St', '678901', 'HVAC maintenance before winter'),
    (8, NULL, 6, 1, '2024-12-08', '18:00:00', '8901234567', '606 Fir St', '789012', 'Pool maintenance before holiday season'),
    (9, NULL, 9, 1, '2024-12-09', '19:00:00', '9012345678', '707 Poplar St', '890123', 'Furniture assembly for new apartment'),
    (10, NULL, 1, 1, '2024-12-10', '20:00:00', '0123456789', '808 Willow St', '901234', 'Deep cleaning for move-in');
    

INSERT INTO Worker (provider_id, name) VALUES
    (101, 'Michael Cleaner'),
    (102, 'Sarah Repair'),
    (103, 'Tom HVAC'),
    (104, 'Linda Pest'),
    (105, 'Chris Install');

-- Sample scenario: Update booking when a worker accepts a job
UPDATE Booking SET provider_id = 101 WHERE id = 1; -- Michael Cleaner accepts job 1
UPDATE Booking SET provider_id = 102 WHERE id = 2; -- Sarah Repair accepts job 2
-- Insert sample payment methods (For future use)
INSERT INTO PaymentMethod (name) VALUES 
    ('Bank Card'), 
    ('QR Code');

