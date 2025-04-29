-- 用户表
CREATE TABLE IF NOT EXISTS app_user
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL,
    user_type   INT          NOT NULL DEFAULT 0 COMMENT '0-普通用户，1-VIP用户',
    create_time TIMESTAMP,
    update_time TIMESTAMP,
    status      INT          NOT NULL DEFAULT 1 COMMENT '0-禁用，1-启用'
);

-- 电影表
CREATE TABLE IF NOT EXISTS movie
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(100) NOT NULL,
    description  TEXT,
    release_date VARCHAR(50),
    duration     VARCHAR(50) COMMENT '电影时长(分钟)',
    cover_image  VARCHAR(255),
    region       VARCHAR(50),
    is_vip       INT          NOT NULL DEFAULT 0 COMMENT '0-否，1-是',
    play_count   INT                   DEFAULT 0,
    score        DOUBLE                DEFAULT 0,
    create_time  TIMESTAMP,
    update_time  TIMESTAMP
);

-- 电影类型表
CREATE TABLE IF NOT EXISTS movie_type
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 电影-类型关联表
CREATE TABLE IF NOT EXISTS movie_movie_type
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    type_id  BIGINT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE,
    FOREIGN KEY (type_id) REFERENCES movie_type (id) ON DELETE CASCADE
);

-- 演员表
CREATE TABLE IF NOT EXISTS actor
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    photo       VARCHAR(255),
    description TEXT
);

-- 导演表
CREATE TABLE IF NOT EXISTS director
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    photo       VARCHAR(255),
    description TEXT
);

-- 电影-演员关联表
CREATE TABLE IF NOT EXISTS movie_actor
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    actor_id BIGINT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES actor (id) ON DELETE CASCADE
);

-- 电影-导演关联表
CREATE TABLE IF NOT EXISTS movie_director
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id    BIGINT NOT NULL,
    director_id BIGINT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES director (id) ON DELETE CASCADE
);

-- 用户观影记录表
CREATE TABLE IF NOT EXISTS view_history
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id   BIGINT NOT NULL,
    movie_id  BIGINT NOT NULL,
    view_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE
);

-- 评分表
CREATE TABLE IF NOT EXISTS rating
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    movie_id    BIGINT NOT NULL,
    score       INT    NOT NULL COMMENT '评分(1-10)',
    comment     TEXT,
    create_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE
);

-- 支付订单表
CREATE TABLE IF NOT EXISTS payment_order
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no     VARCHAR(64)    NOT NULL COMMENT '订单号',
    user_id      BIGINT         NOT NULL COMMENT '用户ID',
    amount       DECIMAL(10, 2) NOT NULL COMMENT '金额',
    status       INT            NOT NULL DEFAULT 0 COMMENT '状态：0-待支付，1-支付成功，2-支付失败',
    pay_type     VARCHAR(20)    NOT NULL COMMENT '支付类型',
    trade_no     VARCHAR(64)    COMMENT '支付宝交易号',
    vip_duration VARCHAR(20)    COMMENT 'VIP时长：monthly/yearly',
    create_time  TIMESTAMP,
    update_time  TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);
