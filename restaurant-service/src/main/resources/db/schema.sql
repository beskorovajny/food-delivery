-- schema.sql

CREATE TABLE IF NOT EXISTS restaurants
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL,
    address VARCHAR
(
    255
) NOT NULL,
    city VARCHAR
(
    100
) NOT NULL,
    description TEXT,
    average_rating DECIMAL
(
    3,
    2
) DEFAULT 0.00,
    review_count INTEGER DEFAULT 0,
    active BOOLEAN DEFAULT TRUE NOT NULL,
    opening_time TIME,
    closing_time TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS idx_restaurants_city ON restaurants(city);
CREATE INDEX IF NOT EXISTS idx_restaurants_active ON restaurants(active);
CREATE INDEX IF NOT EXISTS idx_restaurants_name ON restaurants(name);


CREATE TABLE IF NOT EXISTS menu_categories
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    restaurant_id
    BIGINT
    NOT
    NULL
    REFERENCES
    restaurants
(
    id
) ON DELETE CASCADE,
    name VARCHAR
(
    100
) NOT NULL,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE
(
    restaurant_id,
    name
)
    );

CREATE INDEX IF NOT EXISTS idx_menu_categories_restaurant
    ON menu_categories(restaurant_id);


CREATE TABLE IF NOT EXISTS menu_items
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    restaurant_id
    BIGINT
    NOT
    NULL
    REFERENCES
    restaurants
(
    id
) ON DELETE CASCADE,
    category_id BIGINT REFERENCES menu_categories
(
    id
)
  ON DELETE SET NULL,
    name VARCHAR
(
    150
) NOT NULL,
    description TEXT,
    price DECIMAL
(
    10,
    2
) NOT NULL CHECK
(
    price
    >=
    0
),
    image_url VARCHAR
(
    500
),
    available BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE
(
    restaurant_id,
    name
)
    );

CREATE INDEX IF NOT EXISTS idx_menu_items_restaurant
    ON menu_items(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_menu_items_category
    ON menu_items(category_id);
CREATE INDEX IF NOT EXISTS idx_menu_items_available
    ON menu_items(available);