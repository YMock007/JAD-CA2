-- Generate 100 Bookings with Unique Requester and Provider IDs
INSERT INTO Booking (requester_id, provider_id, service_id, status_id, date_requested, time_requested, phNumber, address, postalCode, remark)
SELECT 
    requester.id AS requester_id,
    provider.provider_id AS provider_id,
    (SELECT id FROM Service ORDER BY RANDOM() LIMIT 1) AS service_id,
    1, -- Pending status
    (CURRENT_DATE - INTERVAL '1 day' * FLOOR(RANDOM() * 365))::DATE AS date_requested,
    (ARRAY[
        '08:00:00'::TIME, 
        '10:00:00'::TIME, 
        '14:00:00'::TIME, 
        '16:00:00'::TIME, 
        '18:00:00'::TIME
    ])[FLOOR(RANDOM() * 5 + 1)] AS time_requested,
    COALESCE(requester.phNumber, '0000000000') AS phNumber,
    requester.address,
    requester.postalCode,
    'No special request'
FROM 
    (SELECT id, phNumber, address, postalCode FROM Person WHERE role_id = 2) AS requester
CROSS JOIN 
    (SELECT provider_id FROM Worker) AS provider
LIMIT 100;

-- Generate 100 Reviews for Existing Bookings
INSERT INTO Review (rating, content, booking_id, date_created)
SELECT 
    FLOOR(RANDOM() * 5 + 1) AS rating, 
    (ARRAY[
        'Excellent service! Very professional.', 
        'Good experience, will book again.', 
        'Average service, expected more.', 
        'Late arrival but decent service.', 
        'Terrible experience, not recommended.', 
        'Clean and efficient, highly satisfied.', 
        'Decent work, but pricing is high.', 
        'Prompt and well-mannered.', 
        'Could be better, left some areas uncleaned.', 
        'Highly recommend, job well done!'
    ])[FLOOR(RANDOM() * 10 + 1)] AS content, -- Random review text
    id AS booking_id,
    (CURRENT_DATE - INTERVAL '1 day' * FLOOR(RANDOM() * 30))::DATE AS date_created
FROM Booking
LIMIT 100;
