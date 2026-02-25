-- data.sql
-- Full seed: 20 restaurants + realistic menus for each
-- All inserts use ON CONFLICT DO NOTHING for safe restarts

-- Restaurants (same as before, no lat/long)
INSERT INTO restaurants (name, address, city, description, average_rating, review_count, active, opening_time,
                         closing_time)
VALUES ('Pizza Celentano', 'вул. Дорошенка 45', 'Львів', 'Італійська кухня, найкраща піца в місті', 4.7, 1280, true,
        '10:00', '23:00'),
       ('SushiYa', 'пр. Чорновола 12', 'Львів', 'Свіжі суші та роли, доставка 30 хв', 4.5, 940, true, '11:00', '22:00'),
       ('Mama’s Kitchen', 'вул. Сихівська 8', 'Львів', 'Домашня українська кухня', 4.8, 670, true, '08:00', '21:00'),
       ('Burger Club', 'Галицька 22', 'Львів', 'Авторські бургери та фрі', 4.4, 1120, true, '11:00', '23:00'),
       ('Shaurma №1', 'Зелена 14', 'Львів', 'Найсмачніша шаурма 24/7', 4.3, 2150, true, '00:00', '23:59'),
       ('Vesuvio', 'Франка 6', 'Львів', 'Італійська паста та піца', 4.6, 580, true, '12:00', '22:00'),
       ('Dim Sum', 'Городоцька 33', 'Львів', 'Китайська кухня, дім-сам на пару', 4.5, 420, true, '12:00', '21:30'),
       ('Grill House', 'Личаківська 15', 'Львів', 'М’ясо на вогні, стейки', 4.7, 890, true, '16:00', '23:00'),
       ('La Piazza', 'Ринок 8', 'Львів', 'Італійське кафе на площі', 4.8, 760, true, '09:00', '22:00'),
       ('Pho Viet', 'Кульпарківська 93', 'Львів', 'В’єтнамська кухня, фо та спринг-роли', 4.6, 510, true, '11:00',
        '21:00'),
       ('Bistro 7', 'Хрещатик 15', 'Київ', 'Європейська кухня в центрі', 4.4, 1450, true, '08:00', '23:00'),
       ('KANAPA', 'Андріївський узвіз 19', 'Київ', 'Сучасна українська кухня', 4.9, 2100, true, '12:00', '22:00'),
       ('Dacha', 'Сагайдачного 23', 'Київ', 'Українська кухня на Подолі', 4.7, 980, true, '10:00', '23:00'),
       ('Roller Coaster', 'Дерибасівська 14', 'Одеса', 'Роли та суші з видом на море', 4.5, 870, true, '11:00',
        '23:00'),
       ('Mama’s', 'Пушкінська 14', 'Одеса', 'Домашня кухня Одеси', 4.6, 620, true, '08:00', '21:00'),
       ('Black Market', 'Мечникова 2', 'Дніпро', 'Авторська кухня, коктейлі', 4.8, 740, true, '18:00', '02:00'),
       ('Pasta Bar', 'Січових Стрільців 12', 'Дніпро', 'Італійська паста та віно', 4.5, 510, true, '12:00', '22:00'),
       ('Meat & Wine', 'Січових Стрільців 8', 'Дніпро', 'Стейки та червоне вино', 4.7, 890, true, '17:00', '23:00'),
       ('Vesna', 'Сумська 35', 'Харків', 'Сучасна українська кухня', 4.6, 680, true, '12:00', '22:00'),
       ('Urban Food', 'Пушкінська 32', 'Харків', 'Бургери та стріт-фуд', 4.4, 920, true, '11:00',
        '23:00') ON CONFLICT DO NOTHING;

-- Menus for all 20 restaurants (explicit inserts)

-- Restaurant 1: Pizza Celentano
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (1, 'Класична піца', 1),
       (1, 'Авторська піца', 2),
       (1, 'Салати', 3),
       (1, 'Напої', 4) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (1, (SELECT id FROM menu_categories WHERE restaurant_id = 1 AND name = 'Класична піца'), 'Маргарита',
        'Томатний соус, моцарела, базилік', 169.00, 'https://cdn.example.com/margherita.jpg', true),
       (1, (SELECT id FROM menu_categories WHERE restaurant_id = 1 AND name = 'Класична піца'), 'Пепероні',
        'Гостра салямі, моцарела', 189.00, NULL, true),
       (1, (SELECT id FROM menu_categories WHERE restaurant_id = 1 AND name = 'Авторська піца'), 'Чотири сири',
        'Моцарела, горгонзола, пармезан, гауда', 229.00, NULL, true),
       (1, (SELECT id FROM menu_categories WHERE restaurant_id = 1 AND name = 'Салати'), 'Цезар з куркою',
        'Романо, курка, сухарики', 149.00, NULL, true),
       (1, (SELECT id FROM menu_categories WHERE restaurant_id = 1 AND name = 'Напої'), 'Кола 0.5л', '', 35.00, NULL,
        true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 2: SushiYa
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (2, 'Фірмові сети', 1),
       (2, 'Класичні роли', 2),
       (2, 'Теплі роли', 3),
       (2, 'Закуски', 4) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (2, (SELECT id FROM menu_categories WHERE restaurant_id = 2 AND name = 'Фірмові сети'), 'Філадельфія сет',
        '16 шт', 499.00, NULL, true),
       (2, (SELECT id FROM menu_categories WHERE restaurant_id = 2 AND name = 'Класичні роли'), 'Каліфорнія',
        'Краб, авокадо, огірок', 149.00, NULL, true),
       (2, (SELECT id FROM menu_categories WHERE restaurant_id = 2 AND name = 'Теплі роли'), 'Темпура креветка',
        'Теплий рол у темпурі', 179.00, NULL, true),
       (2, (SELECT id FROM menu_categories WHERE restaurant_id = 2 AND name = 'Закуски'), 'Чука салат',
        'Водорості з горіховим соусом', 99.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 3: Mama’s Kitchen
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (3, 'Перші страви', 1),
       (3, 'М’ясні страви', 2),
       (3, 'Десерти', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (3, (SELECT id FROM menu_categories WHERE restaurant_id = 3 AND name = 'Перші страви'), 'Борщ з пампушками',
        'Зі сметаною', 89.00, NULL, true),
       (3, (SELECT id FROM menu_categories WHERE restaurant_id = 3 AND name = 'М’ясні страви'), 'Свиняча відбивна',
        'З картоплею', 169.00, NULL, true),
       (3, (SELECT id FROM menu_categories WHERE restaurant_id = 3 AND name = 'Десерти'), 'Сирники',
        'Зі сметаною та варенням', 65.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 4: Burger Club
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (4, 'Бургери', 1),
       (4, 'Закуски', 2),
       (4, 'Напої', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (4, (SELECT id FROM menu_categories WHERE restaurant_id = 4 AND name = 'Бургери'), 'Classic Burger',
        'Яловичина, чеддер', 149.00, NULL, true),
       (4, (SELECT id FROM menu_categories WHERE restaurant_id = 4 AND name = 'Бургери'), 'Double Cheese',
        'Подвійна котлета', 199.00, NULL, true),
       (4, (SELECT id FROM menu_categories WHERE restaurant_id = 4 AND name = 'Закуски'), 'Фрі великі', 'З соусом',
        79.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 5: Shaurma №1
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (5, 'Шаурма', 1),
       (5, 'Фалафель', 2),
       (5, 'Додатки', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (5, (SELECT id FROM menu_categories WHERE restaurant_id = 5 AND name = 'Шаурма'), 'Шаурма класична',
        'Курка, овочі, соус', 95.00, NULL, true),
       (5, (SELECT id FROM menu_categories WHERE restaurant_id = 5 AND name = 'Шаурма'), 'Шаурма з подвійним м’ясом',
        '', 135.00, NULL, true),
       (5, (SELECT id FROM menu_categories WHERE restaurant_id = 5 AND name = 'Фалафель'), 'Фалафель у піті',
        'З тахіні', 89.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 6: Vesuvio
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (6, 'Паста', 1),
       (6, 'Піца', 2),
       (6, 'Десерти', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (6, (SELECT id FROM menu_categories WHERE restaurant_id = 6 AND name = 'Паста'), 'Карбонара',
        'Вершки, бекон, яйце', 169.00, NULL, true),
       (6, (SELECT id FROM menu_categories WHERE restaurant_id = 6 AND name = 'Піца'), 'Капрезе',
        'Томати, моцарела, базилік', 179.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 7: Dim Sum
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (7, 'Дім-сам', 1),
       (7, 'Локшина', 2),
       (7, 'Супи', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (7, (SELECT id FROM menu_categories WHERE restaurant_id = 7 AND name = 'Дім-сам'), 'Креветкові дім-сам',
        'На пару', 129.00, NULL, true),
       (7, (SELECT id FROM menu_categories WHERE restaurant_id = 7 AND name = 'Локшина'), 'Локшина з яловичиною',
        'Устрічний соус', 149.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 8: Grill House
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (8, 'Стейки', 1),
       (8, 'Гриль', 2),
       (8, 'Салати', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (8, (SELECT id FROM menu_categories WHERE restaurant_id = 8 AND name = 'Стейки'), 'Рибай 300г',
        'Середньої прожарки', 399.00, NULL, true),
       (8, (SELECT id FROM menu_categories WHERE restaurant_id = 8 AND name = 'Гриль'), 'Курячі стегна BBQ', '', 169.00,
        NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 9: La Piazza
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (9, 'Піца', 1),
       (9, 'Паста', 2),
       (9, 'Десерти', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (9, (SELECT id FROM menu_categories WHERE restaurant_id = 9 AND name = 'Піца'), 'Капрезе', 'Томати, моцарела',
        179.00, NULL, true),
       (9, (SELECT id FROM menu_categories WHERE restaurant_id = 9 AND name = 'Паста'), 'Песто з куркою', '', 169.00,
        NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 10: Pho Viet
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (10, 'Фо супи', 1),
       (10, 'Роли', 2),
       (10, 'Рисові страви', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (10, (SELECT id FROM menu_categories WHERE restaurant_id = 10 AND name = 'Фо супи'), 'Pho Bo', 'Яловичий фо',
        139.00, NULL, true),
       (10, (SELECT id FROM menu_categories WHERE restaurant_id = 10 AND name = 'Роли'), 'Спринг-роли', 'З креветками',
        99.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 11: Bistro 7
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (11, 'Сніданки', 1),
       (11, 'Основні страви', 2),
       (11, 'Десерти', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (11, (SELECT id FROM menu_categories WHERE restaurant_id = 11 AND name = 'Сніданки'), 'Яєчня з беконом',
        'З тостами', 99.00, NULL, true),
       (11, (SELECT id FROM menu_categories WHERE restaurant_id = 11 AND name = 'Основні страви'), 'Лазанья', 'М’ясна',
        179.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 12: KANAPA
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (12, 'Холодні закуски', 1),
       (12, 'Гарячі страви', 2),
       (12, 'Десерти', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (12, (SELECT id FROM menu_categories WHERE restaurant_id = 12 AND name = 'Холодні закуски'), 'Сало з часником',
        'З бородинським хлібом', 89.00, NULL, true),
       (12, (SELECT id FROM menu_categories WHERE restaurant_id = 12 AND name = 'Гарячі страви'), 'Качина грудка',
        'З вишневим соусом', 249.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 13: Dacha
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (13, 'Салати', 1),
       (13, 'М’ясо з вогню', 2),
       (13, 'Супи', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (13, (SELECT id FROM menu_categories WHERE restaurant_id = 13 AND name = 'Салати'), 'Салат з буряком',
        'З сиром та горіхами', 129.00, NULL, true),
       (13, (SELECT id FROM menu_categories WHERE restaurant_id = 13 AND name = 'М’ясо з вогню'), 'Ребра BBQ',
        'Свинячі ребра', 219.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 14: Roller Coaster
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (14, 'Сети', 1),
       (14, 'Роли', 2),
       (14, 'Гарячі страви', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (14, (SELECT id FROM menu_categories WHERE restaurant_id = 14 AND name = 'Сети'), 'Сет Дракон', '24 шт', 799.00,
        NULL, true),
       (14, (SELECT id FROM menu_categories WHERE restaurant_id = 14 AND name = 'Роли'), 'Рол з вугрем', 'З авокадо',
        189.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 15: Mama’s (Odessa)
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (15, 'Риба та морепродукти', 1),
       (15, 'Салати', 2),
       (15, 'Перші страви', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (15, (SELECT id FROM menu_categories WHERE restaurant_id = 15 AND name = 'Риба та морепродукти'),
        'Бички в томаті', 'Одеський класик', 139.00, NULL, true),
       (15, (SELECT id FROM menu_categories WHERE restaurant_id = 15 AND name = 'Салати'), 'Салат з мідіями',
        'З овочами', 169.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 16: Black Market
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (16, 'Авторські страви', 1),
       (16, 'Коктейлі', 2),
       (16, 'Десерти', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (16, (SELECT id FROM menu_categories WHERE restaurant_id = 16 AND name = 'Авторські страви'), 'Тартар з лосося',
        'З авокадо', 249.00, NULL, true),
       (16, (SELECT id FROM menu_categories WHERE restaurant_id = 16 AND name = 'Коктейлі'), 'Old Fashioned',
        'Класичний', 189.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 17: Pasta Bar
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (17, 'Паста', 1),
       (17, 'Салати', 2),
       (17, 'Вина', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (17, (SELECT id FROM menu_categories WHERE restaurant_id = 17 AND name = 'Паста'), 'Спагеті карбонара',
        'З беконом', 169.00, NULL, true),
       (17, (SELECT id FROM menu_categories WHERE restaurant_id = 17 AND name = 'Салати'), 'Капрезе', 'З бальзаміком',
        139.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 18: Meat & Wine
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (18, 'Стейки', 1),
       (18, 'Вино', 2),
       (18, 'Закуски', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (18, (SELECT id FROM menu_categories WHERE restaurant_id = 18 AND name = 'Стейки'), 'Рибай 400г',
        'Середньої прожарки', 499.00, NULL, true),
       (18, (SELECT id FROM menu_categories WHERE restaurant_id = 18 AND name = 'Закуски'), 'Тартар з яловичини',
        'З жовтком', 219.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 19: Vesna
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (19, 'Українська кухня', 1),
       (19, 'Салати', 2),
       (19, 'Десерти', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (19, (SELECT id FROM menu_categories WHERE restaurant_id = 19 AND name = 'Українська кухня'),
        'Вареники з вишнею', 'Зі сметаною', 99.00, NULL, true),
       (19, (SELECT id FROM menu_categories WHERE restaurant_id = 19 AND name = 'Салати'), 'Салат Олів’є', 'Класичний',
        89.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;

-- Restaurant 20: Urban Food
INSERT INTO menu_categories (restaurant_id, name, display_order)
VALUES (20, 'Бургери', 1),
       (20, 'Стріт-фуд', 2),
       (20, 'Напої', 3) ON CONFLICT (restaurant_id, name) DO NOTHING;

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, image_url, available)
VALUES (20, (SELECT id FROM menu_categories WHERE restaurant_id = 20 AND name = 'Бургери'), 'Urban Double',
        'Подвійна котлета, сир, бекон', 189.00, NULL, true),
       (20, (SELECT id FROM menu_categories WHERE restaurant_id = 20 AND name = 'Стріт-фуд'), 'Такос з куркою',
        'З соусом сальса', 129.00, NULL, true) ON CONFLICT (restaurant_id, name) DO NOTHING;