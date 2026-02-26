-- Orders table (main aggregate)
CREATE TABLE IF NOT EXISTS orders (
    id                BIGSERIAL PRIMARY KEY,
    customer_id       BIGINT NOT NULL,
    restaurant_id     BIGINT NOT NULL,
    restaurant_name   VARCHAR(255) NOT NULL,
    street            VARCHAR(255) NOT NULL,
    house             VARCHAR(50) NOT NULL,
    apartment         VARCHAR(50),
    city              VARCHAR(100) NOT NULL,
    status            VARCHAR(50) NOT NULL DEFAULT 'NEW',
    total_price       DECIMAL(10,2) NOT NULL,
    delivery_fee      DECIMAL(10,2),
    service_fee       DECIMAL(10,2),
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP
    );

-- Order items (child table)
CREATE TABLE IF NOT EXISTS order_items (
    id                    BIGSERIAL PRIMARY KEY,
    order_id              BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    menu_item_id          BIGINT NOT NULL,
    menu_item_name        VARCHAR(150) NOT NULL,
    description           TEXT,
    price_at_order_time   DECIMAL(10,2) NOT NULL,
    quantity              INTEGER NOT NULL DEFAULT 1,
    subtotal              DECIMAL(10,2) NOT NULL,
    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Indexes for performance (very important for real load)
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_restaurant_id ON orders(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_menu_item_id ON order_items(menu_item_id);

-- Optional: unique constraint if needed (e.g. prevent duplicate menu items in same order)
-- ALTER TABLE order_items ADD CONSTRAINT uk_order_item UNIQUE (order_id, menu_item_id);