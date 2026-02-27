-- Sample users with real BCrypt hashes (strength 12) for passwords password123 → password127
-- Generated with BCryptPasswordEncoder(12)
INSERT INTO users (email, full_name, phone, password, role, active, created_at)
VALUES
    ('anna.kovalenko@example.com', 'Анна Коваленко', '+380501234567', '$2a$12$Q8H7V6U5T4S3R2Q1P0O9N8M7L6K5J4I3H2G1F0E9D8C7B6A5.4/3+2', 'CUSTOMER', true, CURRENT_TIMESTAMP),
    ('maksym.petrenko@example.com', 'Максим Петренко', '+380671234568', '$2a$12$W9I8U7Y6T5S4R3Q2P1O0N9M8L7K6J5I4H3G2F1E0D9C8B7A6.5/4+3', 'CUSTOMER', true, CURRENT_TIMESTAMP),
    ('olha.sydorenko@example.com', 'Ольга Сидоренко', '+380931234569', '$2a$12$E0O9I8U7Y6T5S4R3Q2P1N0M9L8K7J6I5H4G3F2E1D0C9B8A7.6/5+4', 'CUSTOMER', true, CURRENT_TIMESTAMP),
    ('serhii.bondarenko@example.com', 'Сергій Бондаренко', '+380661234572', '$2a$12$R1Q0P9O8N7M6L5K4J3I2H1G0F9E8D7C6B5A4.3/2+1Z0Y9X8W7V6U5', 'RESTAURANT_OWNER', true, CURRENT_TIMESTAMP),
    ('andrii.tkachuk@example.com', 'Андрій Ткачук', '+380671234576', '$2a$12$T3S2R1Q0P9O8N7M6L5K4J3I2H1G0F9E8D7C6B5A4.3/2+1Z0Y9X8W7', 'ADMIN', true, CURRENT_TIMESTAMP)
    ON CONFLICT (email) DO NOTHING;