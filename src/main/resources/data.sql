-- noinspection SpellCheckingInspectionForFile

-- 初始化用户数据
INSERT INTO app_user (username, password, email, user_type, status, create_time, update_time)
SELECT *
FROM (VALUES ('admin', '{bcrypt}$2a$10$Tz.HPbR9lr8K3mFiUSAyD.K7v1/uO.yvzapJIvsboZDKrAnq8pMrO',
              'admin@astralbridge.space', 1, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
             ('user1', '{bcrypt}$2a$10$z6pFN/YtkrY5RaOq5XJsteAofLZfq8CQX7gy9Gh5PFjPGvAJrhgt2',
              'user1@astralbridge.space', 0, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
             ('vipuser', '{bcrypt}$2a$10$VMXH3gqj3gbwfr8pbQ8zx.q2HPGsP5fwjlUVl9y/hB.lEQ7i8ra42',
              'vip@astralbridge.space', 1, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()))
         AS temp (username, password, email, user_type, status, create_time, update_time)
WHERE NOT EXISTS (SELECT 1 FROM app_user);

-- 初始化电影类型
INSERT INTO movie_type (name)
SELECT v.name
FROM (VALUES ('动作'), ('冒险'), ('喜剧'), ('剧情'), ('奇幻'), ('恐怖'), ('科幻'), ('爱情')) AS v(name)
WHERE NOT EXISTS (SELECT 1 FROM movie_type);

-- 初始化演员
INSERT INTO actor (name, photo, description)
SELECT v.name, v.photo, v.description
FROM (VALUES ('张三', '/images/actors/zhangsan.jpg', '著名演员张三'),
             ('李四', '/images/actors/lisi.jpg', '著名演员李四'),
             ('王五', '/images/actors/wangwu.jpg', '著名演员王五')
         ) AS v(name, photo, description)
WHERE NOT EXISTS (SELECT 1 FROM actor);

-- 初始化导演
INSERT INTO director (name, photo, description)
SELECT v.name, v.photo, v.description
FROM (VALUES ('赵导', '/images/directors/zhao.jpg', '著名导演赵导'),
             ('钱导', '/images/directors/qian.jpg', '著名导演钱导')
         ) AS v(name, photo, description)
WHERE NOT EXISTS (SELECT 1 FROM director);

-- 初始化电影
INSERT INTO movie (title, description, release_date, duration, cover_image, region, is_vip, play_count, score,
                   create_time, update_time)
SELECT *
FROM (VALUES ('电影一', '这是电影一的描述', '2023-01-15', '120分钟', '/images/movies/movie1.jpg', '中国', 0, 1000, 8.5,
              CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
             ('电影二', '这是电影二的描述', '2023-02-20', '135分钟', '/images/movies/movie2.jpg', '美国', 1, 800, 7.8,
              CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
             ('电影三', '这是电影三的描述', '2023-03-10', '110分钟', '/images/movies/movie3.jpg', '中国', 0, 1200, 9.0,
              CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
             ('电影四', '这是电影四的描述', '2023-04-05', '128分钟', '/images/movies/movie4.jpg', '美国', 1, 600, 8.2,
              CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())
         ) AS temp (title, description, release_date, duration, cover_image, region, is_vip, play_count, score,
                    create_time, update_time)
WHERE NOT EXISTS (SELECT 1 FROM movie);

-- 电影类型关联
INSERT INTO movie_movie_type (movie_id, type_id)
SELECT *
FROM (VALUES (1, 1), (1, 4), (2, 5), (2, 7), (3, 3), (3, 8), (4, 6), (4, 7)) AS temp(movie_id, type_id)
WHERE NOT EXISTS (SELECT 1 FROM movie_movie_type);

-- 电影演员关联
INSERT INTO movie_actor (movie_id, actor_id)
SELECT *
FROM (VALUES (1, 1),
             (1, 2),
             (2, 2),
             (2, 3),
             (3, 1),
             (3, 3),
             (4, 2),
             (4, 3)) AS temp(movie_id, actor_id)
WHERE NOT EXISTS (SELECT 1 FROM movie_actor);

-- 电影导演关联
INSERT INTO movie_director (movie_id, director_id)
SELECT *
FROM (VALUES (1, 1), (2, 2), (3, 1), (4, 2)) AS temp(movie_id, director_id)
WHERE NOT EXISTS (SELECT 1 FROM movie_director);

-- 初始化观看记录
INSERT INTO view_history (user_id, movie_id, view_time)
SELECT *
FROM (VALUES (1, 1, CURRENT_TIMESTAMP()),
             (1, 2, CURRENT_TIMESTAMP()),
             (2, 1, CURRENT_TIMESTAMP()),
             (3, 2, CURRENT_TIMESTAMP()),
             (3, 3, CURRENT_TIMESTAMP())) AS temp(user_id, movie_id, view_time)
WHERE NOT EXISTS (SELECT 1 FROM view_history);

-- 初始化评分
INSERT INTO rating (user_id, movie_id, score, comment, create_time)
SELECT *
FROM (VALUES (1, 1, 9, '很好看的电影', CURRENT_TIMESTAMP()),
             (1, 2, 8, '还不错', CURRENT_TIMESTAMP()),
             (2, 1, 7, '一般般', CURRENT_TIMESTAMP()),
             (3, 2, 10, '非常精彩', CURRENT_TIMESTAMP()),
             (3, 3, 8, '值得一看', CURRENT_TIMESTAMP())) AS temp(user_id, movie_id, score, comment, create_time)
WHERE NOT EXISTS (SELECT 1 FROM rating);
