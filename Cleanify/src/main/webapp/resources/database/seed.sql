-- Insert general categories for services
INSERT INTO Category (name) VALUES 
    ('Cleaning'), 
    ('Repair'), 
    ('Maintenance'),
    ('Pest Control'),
    ('Installation');

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
    ('hank_gray', 'password444', 'hank@example.com', NULL, '707 Poplar St', '890123', 2, FALSE);


  
-- Insert sample services 
INSERT INTO Service (name, category_id, description, price, image_url, est_duration) VALUES 
    -- Cleaning Services
    ('Home Cleaning', 1, 'Our home cleaning service offers a thorough and deep cleaning for residential spaces, including dusting, vacuuming, mopping, and sanitation of key areas. We ensure your home is sparkling clean and free from allergens, using eco-friendly cleaning products.', 100.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/homeCleaning.jpg', 2),
    
    -- Repair Services
    ('Plumbing Repair', 2, 'Our plumbing repair service covers a wide range of issues, from fixing leaks and clogged drains to repairing pipes and installing new fixtures. We are experienced in handling both minor repairs and more complex plumbing challenges to keep your home running smoothly.', 120.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/PlumbingRepair.jpg', 3),
    ('Electrical Repair', 2, 'Our electrical repair service includes troubleshooting, diagnostics, and fixing electrical issues such as faulty wiring, circuit problems, and malfunctioning outlets. We prioritize safety and ensure that all repairs are done to code and meet industry standards.', 140.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/ElectricalRepair.jpg', 4),

    -- Maintenance Services
    ('HVAC Maintenance', 3, 'Our HVAC maintenance service provides comprehensive care for your heating, ventilation, and air conditioning systems. This includes cleaning, filter replacement, and system checks to ensure optimal efficiency and prevent unexpected breakdowns, helping you save on energy costs.', 120.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/HVACMaintenance.jpg', 3),
    ('Lawn Maintenance', 3, 'Our lawn maintenance service includes everything from mowing, edging, and trimming to ensuring the health of your lawn with proper fertilization and weed control. We work with you to create a customized care plan for a lush, green lawn all year round.', 70.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/LawnMaintenance.jpg', 2),
    ('Pool Maintenance', 3, 'Our pool maintenance service includes regular cleaning, debris removal, and chemical balancing to keep your pool safe, clean, and ready for swimming. We ensure your pool’s filtration system is in top condition and that water quality is maintained to the highest standards.', 130.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/PoolMaintenance.jpg', 4),

    -- Pest Control Services
    ('Pest Control', 4, 'Our pest control service provides a comprehensive approach to eliminating and preventing pests in your home. We offer treatments for a wide range of pests, including ants, termites, rodents, and bedbugs, using safe and effective methods to keep your home pest-free.', 110.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/PestControl.jpg', 2),

    -- Installation Services
    ('Appliance Installation', 5, 'Our appliance installation service ensures that your new home appliances, such as refrigerators, ovens, dishwashers, and washing machines, are properly set up and ready to use. Our professionals handle the installation with care, ensuring everything works efficiently and safely.', 150.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/ApplianceInstallation.jpg', 3),
    ('Furniture Assembly', 5, 'Our furniture assembly service includes the careful and precise assembly of various types of furniture, from bedroom sets and desks to bookcases and dining tables. We follow the manufacturer’s instructions to ensure your furniture is securely assembled and ready for use.', 130.00, 'https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/furnitureAssembly.jpg', 1);

-- Insert additional bookings with remarks
INSERT INTO Booking (requester_id, provider_id, service_id, status_id, date_requested, time_requested, phNumber, address, postalCode, remark) VALUES
    (2, 2, 2, 2, '2024-11-23', '09:00:00', '3456789012', '456 Elm St', '654321', 'Customer requested a follow-up call after service'),
    (4, 5, 5, 2, '2024-11-24', '10:00:00', '4567890123', '789 Oak St', '789012', 'Preferred timing was adjusted upon provider request'),
    (6, 8, 7, 2, '2024-11-25', '11:00:00', '5678901234', '202 Maple St', '890123', 'Requested eco-friendly cleaning materials'),
    (7, 2, 8, 2, '2024-11-26', '14:00:00', '6789012345', '303 Birch St', '456789', 'Asked for a detailed receipt post service'),
    (10, 5, 1, 2, '2024-11-27', '15:00:00', '7890123456', '404 Cedar St', '567890', 'Customer will be on-site for supervision'),
    (1, 8, 9, 2, '2024-11-28', '16:00:00', '8901234567', '505 Palm St', '678901', 'Requested additional equipment for service');

-- Insert additional reviews for completed bookings
INSERT INTO Review (rating, content, booking_id) VALUES
    (5, 'The plumber did an excellent job fixing the leak.', 1),
    (4, 'Timely and professional service.', 2),
    (3, 'Decent work, but there were some delays.', 3),
    (5, 'Amazing service! Everything was perfect.', 4),
    (4, 'Satisfied with the work, will hire again.', 5);
