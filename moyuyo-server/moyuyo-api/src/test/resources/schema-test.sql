DROP TABLE IF EXISTS mo_user;
CREATE TABLE mo_user (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    avatar VARCHAR(500),
    phone VARCHAR(50),
    birthday DATE,
    points INT DEFAULT 0,
    email_verified BIT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    magic_link_token VARCHAR(500),
    magic_link_expire DATETIME,
    two_factor_secret VARCHAR(255),
    two_factor_enabled BIT DEFAULT 0,
    last_login_time DATETIME,
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_member;
CREATE TABLE mo_member (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    level VARCHAR(20) DEFAULT 'NORMAL',
    growth_value INT DEFAULT 0,
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_wallet;
CREATE TABLE mo_wallet (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0,
    total_recharged DECIMAL(10,2) DEFAULT 0,
    total_spent DECIMAL(10,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_category;
CREATE TABLE mo_category (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    image VARCHAR(500),
    sort_order INT DEFAULT 0,
    create_time DATETIME NOT NULL
);

DROP TABLE IF EXISTS mo_product;
CREATE TABLE mo_product (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id BIGINT,
    brand_ip_id BIGINT,
    description TEXT,
    detail TEXT,
    original_price DECIMAL(10,2),
    regular_price DECIMAL(10,2) NOT NULL,
    sale_price DECIMAL(10,2),
    main_image VARCHAR(500),
    stock INT DEFAULT 0,
    on_sale BIT DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_product_sku;
CREATE TABLE mo_product_sku (
    id BIGINT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sku_code VARCHAR(100),
    spec VARCHAR(500),
    price DECIMAL(10,2),
    stock INT DEFAULT 0,
    create_time DATETIME NOT NULL
);

DROP TABLE IF EXISTS mo_product_image;
CREATE TABLE mo_product_image (
    id BIGINT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    sort_order INT DEFAULT 0,
    create_time DATETIME NOT NULL
);

DROP TABLE IF EXISTS mo_order;
CREATE TABLE mo_order (
    id BIGINT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    address_id BIGINT,
    goods_amount DECIMAL(10,2) DEFAULT 0,
    freight DECIMAL(10,2) DEFAULT 0,
    tax_amount DECIMAL(10,2) DEFAULT 0,
    coupon_discount DECIMAL(10,2) DEFAULT 0,
    points_discount DECIMAL(10,2) DEFAULT 0,
    pay_amount DECIMAL(10,2) DEFAULT 0,
    currency VARCHAR(10) DEFAULT 'USD',
    status VARCHAR(20) DEFAULT 'PENDING_PAY',
    pay_channel VARCHAR(20),
    pay_transaction_id VARCHAR(255),
    woo_order_id BIGINT,
    sync_status INT DEFAULT 0,
    sync_retry_count INT DEFAULT 0,
    sync_last_time DATETIME,
    tracking_number VARCHAR(100),
    shipping_carrier VARCHAR(100),
    receiver_name VARCHAR(100),
    receiver_phone VARCHAR(50),
    receiver_address VARCHAR(500),
    receiver_zip VARCHAR(20),
    delete_status INT DEFAULT 0,
    create_time DATETIME NOT NULL,
    paid_at DATETIME,
    cancel_time DATETIME,
    cancel_reason VARCHAR(500),
    deliver_time DATETIME,
    received_time DATETIME,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_order_item;
CREATE TABLE mo_order_item (
    id BIGINT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT,
    sku_id BIGINT,
    product_name VARCHAR(255),
    sku_spec VARCHAR(500),
    main_image VARCHAR(500),
    price DECIMAL(10,2),
    quantity INT,
    subtotal DECIMAL(10,2),
    create_time DATETIME NOT NULL
);

DROP TABLE IF EXISTS mo_address;
CREATE TABLE mo_address (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    receiver_name VARCHAR(100) NOT NULL,
    receiver_phone VARCHAR(50) NOT NULL,
    country VARCHAR(50) DEFAULT 'US',
    state VARCHAR(100),
    city VARCHAR(100),
    district VARCHAR(100),
    address_line VARCHAR(500) NOT NULL,
    zip_code VARCHAR(20),
    is_default BIT DEFAULT 0,
    label VARCHAR(50),
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_cart;
CREATE TABLE mo_cart (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    sku_id BIGINT,
    product_name VARCHAR(255),
    main_image VARCHAR(500),
    price DECIMAL(10,2),
    quantity INT NOT NULL,
    selected BIT DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_coupon;
CREATE TABLE mo_coupon (
    id BIGINT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100),
    description VARCHAR(500),
    type VARCHAR(20) NOT NULL,
    discount_value DECIMAL(10,2),
    min_order_amount DECIMAL(10,2) DEFAULT 0,
    max_discount_amount DECIMAL(10,2),
    total_count INT,
    claimed_count INT DEFAULT 0,
    used_count INT DEFAULT 0,
    start_time DATETIME,
    end_time DATETIME,
    active BIT DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_user_coupon;
CREATE TABLE mo_user_coupon (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'UNUSED',
    used_time DATETIME,
    used_order_id BIGINT,
    create_time DATETIME NOT NULL
);

DROP TABLE IF EXISTS mo_points_log;
CREATE TABLE mo_points_log (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    change_value INT NOT NULL,
    type VARCHAR(50),
    biz_no VARCHAR(100),
    remark VARCHAR(500),
    deleted INT DEFAULT 0,
    created_at DATETIME NOT NULL
);

DROP TABLE IF EXISTS mo_logistics;
CREATE TABLE mo_logistics (
    id BIGINT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    carrier VARCHAR(100),
    tracking_number VARCHAR(200),
    traces TEXT,
    shipped_at DATETIME,
    received_at DATETIME
);

DROP TABLE IF EXISTS mo_refund;
CREATE TABLE mo_refund (
    id BIGINT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    refund_no VARCHAR(50) NOT NULL,
    type VARCHAR(20),
    amount DECIMAL(10,2),
    reason VARCHAR(500),
    description TEXT,
    images TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    woo_refund_id BIGINT,
    create_time DATETIME NOT NULL,
    complete_time DATETIME
);

DROP TABLE IF EXISTS mo_community_post;
CREATE TABLE mo_community_post (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT,
    images TEXT,
    topic VARCHAR(100),
    likes INT DEFAULT 0,
    comments INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PUBLISHED',
    deleted INT DEFAULT 0,
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_community_comment;
CREATE TABLE mo_community_comment (
    id BIGINT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_id BIGINT,
    content TEXT,
    deleted INT DEFAULT 0,
    create_time DATETIME NOT NULL
);

DROP TABLE IF EXISTS mo_community_like;
CREATE TABLE mo_community_like (
    id BIGINT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_time DATETIME NOT NULL
);

DROP TABLE IF EXISTS mo_flash_sale;
CREATE TABLE mo_flash_sale (
    id BIGINT PRIMARY KEY,
    name VARCHAR(200),
    product_id BIGINT,
    sku_id BIGINT,
    flash_price DECIMAL(10,2),
    original_price DECIMAL(10,2),
    total_stock INT DEFAULT 0,
    sold_stock INT DEFAULT 0,
    limit_per_user INT,
    start_time DATETIME,
    end_time DATETIME,
    active BIT DEFAULT 1,
    create_time DATETIME NOT NULL,
    update_time DATETIME
);

DROP TABLE IF EXISTS mo_flash_sale_order;
CREATE TABLE mo_flash_sale_order (
    id BIGINT PRIMARY KEY,
    flash_sale_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    order_id BIGINT,
    quantity INT DEFAULT 1,
    create_time DATETIME NOT NULL
);
