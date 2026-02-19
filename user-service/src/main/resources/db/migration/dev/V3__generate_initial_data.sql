INSERT INTO users (email, full_name, phone, role, active, created_at)
SELECT
    'user' || i || '@example.com' AS email,
    'Користувач ' || i AS full_name,
    '+380' || (670000000 + i) AS phone,
    CASE
        WHEN i % 20 = 0 THEN 'ADMIN'
        WHEN i % 10 = 0 THEN 'RESTAURANT_OWNER'
        WHEN i % 5 = 0 THEN 'DELIVERY_PERSON'
        ELSE 'CUSTOMER'
        END AS role,
    true AS active,
    CURRENT_TIMESTAMP - (i || ' days')::interval AS created_at
FROM generate_series(1, 100) AS i
    ON CONFLICT (email) DO NOTHING;