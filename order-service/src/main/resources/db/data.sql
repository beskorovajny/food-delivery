-- Sample orders (use realistic IDs from your user & restaurant services)

-- Order 1: New order from customer 1 to restaurant 1
INSERT INTO orders (customer_id, restaurant_id, restaurant_name, street, house, apartment, city, status,
                    total_price, delivery_fee, service_fee, created_at)
VALUES (1, 1, 'Pizza Heaven', 'Doroshenka St', '12', '5A', 'Lviv',  'NEW', 299.00, 20.00,
        10.00, CURRENT_TIMESTAMP);

-- Order 2: Confirmed order
INSERT INTO orders (customer_id, restaurant_id, restaurant_name, street, house, apartment, city, status,
                    total_price, delivery_fee, service_fee, created_at, updated_at)
VALUES (2, 1, 'Pizza Heaven', 'Svobody Ave', '28', null, 'Lviv', 'CONFIRMED', 450.00, 25.00, 15.00,
        CURRENT_TIMESTAMP - INTERVAL '2 hours', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Order 3: Delivered order (completed)
INSERT INTO orders (customer_id, restaurant_id, restaurant_name, street, house, apartment, city, status, total_price,
                    delivery_fee, service_fee, created_at, updated_at)
VALUES (1, 2, 'Sushi Master', 'Horodocka St', '45', '10', 'Lviv', 'DELIVERED', 580.00, 30.00, 20.00,
        CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '30 minutes');

-- Order items for order 1 (id=1)
INSERT INTO order_items (order_id, menu_item_id, menu_item_name, description, price_at_order_time, quantity, subtotal)
VALUES (1, 10, 'Margherita Pizza', 'Classic tomato and mozzarella', 149.00, 2, 298.00);

-- Order items for order 2 (id=2)
INSERT INTO order_items (order_id, menu_item_id, menu_item_name, description, price_at_order_time, quantity, subtotal)
VALUES (2, 15, 'Pepperoni Pizza', 'Spicy pepperoni', 189.00, 2, 378.00),
       (2, 20, 'Cola 0.5L', 'Cold drink', 35.00, 2, 70.00);

-- Order items for order 3 (id=3)
INSERT INTO order_items (order_id, menu_item_id, menu_item_name, description, price_at_order_time, quantity, subtotal)
VALUES (3, 30, 'California Roll', 'Crab and avocado', 220.00, 2, 440.00),
       (3, 35, 'Miso Soup', 'Traditional soup', 70.00, 2, 140.00);